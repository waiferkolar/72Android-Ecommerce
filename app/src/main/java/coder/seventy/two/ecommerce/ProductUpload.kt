package coder.seventy.two.ecommerce

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import coder.seventy.two.ecommerce.libby.H
import coder.seventy.two.ecommerce.libby.H.Companion.USER_TOKEN
import coder.seventy.two.ecommerce.modals.ErrorMessager
import coder.seventy.two.ecommerce.modals.FileInfo
import coder.seventy.two.ecommerce.services.ServiceBuilder
import coder.seventy.two.ecommerce.services.WebService
import kotlinx.android.synthetic.main.activity_product_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream

class ProductUpload : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_upload)

        upload_product_image.setOnClickListener {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            101 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    toast("Permission Deny")
                } else {
                    fileUpload()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun fileUpload() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Choose Carefully"), 103)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 103 && resultCode == Activity.RESULT_OK && data != null) {
            val data: Uri = data.data
            val inst: InputStream = contentResolver.openInputStream(data)
            val bitmap = BitmapFactory.decodeStream(inst)
            upload_product_image.imageBitmap = bitmap

            var imagePath = getImagePath(data)
            H.l(imagePath)

            var file = File(imagePath)

            val requestBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, requestBody)

            val services: WebService = ServiceBuilder.buildService(WebService::class.java)
            val responseUpload: Call<FileInfo> = services.imageUpload("Bearer $USER_TOKEN", body)

            responseUpload.enqueue(object : Callback<FileInfo> {
                override fun onFailure(call: Call<FileInfo>, t: Throwable) {
                    H.l(t.message!!)
                }

                override fun onResponse(call: Call<FileInfo>, response: Response<FileInfo>) {
                    if (response.isSuccessful) {
                        val info: FileInfo = response.body()!!
                        toast(info.name)
                        uploadProduct(info.name)
                    } else {
                        H.l("Something wrong!")
                    }
                }

            })
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadProduct(image: String) {
        val cart_id = 1
        val name = "New More Product"
        val price = 20.0
        val description = "This is description special!"
        val services: WebService = ServiceBuilder.buildService(WebService::class.java)
        val responseUpload: Call<ErrorMessager> = services.newProductUpload(
            "Bearer $USER_TOKEN",
            cart_id,
            name,
            price,
            image,
            description
        )

        responseUpload.enqueue(object : Callback<ErrorMessager> {
            override fun onFailure(call: Call<ErrorMessager>, t: Throwable) {
                H.l(t.message!!)
            }

            override fun onResponse(call: Call<ErrorMessager>, response: Response<ErrorMessager>) {
                if (response.isSuccessful) {
                    val message: ErrorMessager = response.body()!!
                    toast(message.msg)
                    H.l(message.msg)
                } else {
                    H.l("Something wrong!")
                }
            }
        })
    }

    private fun getImagePath(uri: Uri): String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)!!
        cursor!!.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        var mediaPath = cursor.getString(columnIndex)
        cursor.close()
        return mediaPath
    }

}
