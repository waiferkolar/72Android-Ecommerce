package coder.seventy.two.ecommerce

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import coder.seventy.two.ecommerce.adapters.CategoryAdapter
import coder.seventy.two.ecommerce.libby.H.Companion.USER_TOKEN
import coder.seventy.two.ecommerce.libby.H.Companion.checkUserAuth
import coder.seventy.two.ecommerce.libby.H.Companion.l
import coder.seventy.two.ecommerce.modals.Category
import coder.seventy.two.ecommerce.services.ServiceBuilder
import coder.seventy.two.ecommerce.services.WebService
import kotlinx.android.synthetic.main.activity_category.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        if (checkUserAuth()) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        categoryRecycler.layoutManager = GridLayoutManager(this, 2)

        loadAllCats()
    }

    private fun loadAllCats() {
        val services: WebService = ServiceBuilder.buildService(WebService::class.java)
        val responseCats: Call<List<Category>> = services.getAllCat("Bearer $USER_TOKEN")

        responseCats.enqueue(object : Callback<List<Category>> {
            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                l(t.message!!)
            }

            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    val cats: List<Category> = response.body()!!
                    categoryRecycler.adapter = CategoryAdapter(this@CategoryActivity, cats)
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val item: MenuItem = menu.findItem(R.id.cart)
        MenuItemCompat.setActionView(item, R.layout.my_cart_layout)
        return super.onCreateOptionsMenu(menu)
    }
}
