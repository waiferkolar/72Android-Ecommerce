package coder.seventy.two.ecommerce

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import coder.seventy.two.ecommerce.adapters.CartAdapter
import coder.seventy.two.ecommerce.libby.H
import coder.seventy.two.ecommerce.libby.H.Companion.USER_TOKEN
import coder.seventy.two.ecommerce.modals.CartProduct
import coder.seventy.two.ecommerce.modals.ErrorMessager
import coder.seventy.two.ecommerce.services.ServiceBuilder
import coder.seventy.two.ecommerce.services.WebService
import kotlinx.android.synthetic.main.activity_my_cart.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCart : AppCompatActivity() {
    var cartCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)

        supportActionBar?.title = "My Crat's Items"

        val cartKeys = H.getAllKeys()

        H.l("$cartKeys")

        cartRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        getCartItems(cartKeys)
    }

    private fun getCartItems(cartKeys: String) {
        val services: WebService = ServiceBuilder.buildService(WebService::class.java)
        val responseCartProducts: Call<List<CartProduct>> = services.getCartPreviewItems("Bearer $USER_TOKEN", cartKeys)

        responseCartProducts.enqueue(object : Callback<List<CartProduct>> {
            override fun onFailure(call: Call<List<CartProduct>>, t: Throwable) {
                H.l(t.message!!)
            }

            override fun onResponse(call: Call<List<CartProduct>>, response: Response<List<CartProduct>>) {
                if (response.isSuccessful) {
                    val products: List<CartProduct> = response.body()!!
                    cartRecycler.adapter = CartAdapter(this@MyCart, products)
                } else {
                    H.l("Something went wrong!")
                }
            }

        })
    }

    private fun billOut() {
        val cartKeys = H.getAllKeys()
        val services: WebService = ServiceBuilder.buildService(WebService::class.java)
        val responseCartOrder: Call<ErrorMessager> = services.billOutOrder("Bearer $USER_TOKEN", cartKeys)


        responseCartOrder.enqueue(object : Callback<ErrorMessager> {
            override fun onFailure(call: Call<ErrorMessager>, t: Throwable) {
                H.l(t.message!!)
            }

            override fun onResponse(call: Call<ErrorMessager>, response: Response<ErrorMessager>) {
                if (response.isSuccessful) {
                    val message: ErrorMessager = response.body()!!
                    toast(message.msg)
                    H.clearCart()
                } else {
                    H.l("Something wrong!")
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cart_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.billOut) {
            billOut()
        } else if (item?.itemId == R.id.product_upload) {
            startActivity(Intent(this@MyCart, ProductUpload::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}
