package coder.seventy.two.ecommerce

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import coder.seventy.two.ecommerce.adapters.ProductAdapter
import coder.seventy.two.ecommerce.libby.H
import coder.seventy.two.ecommerce.libby.H.Companion.USER_TOKEN
import coder.seventy.two.ecommerce.libby.H.Companion.l
import coder.seventy.two.ecommerce.modals.Products
import coder.seventy.two.ecommerce.services.ServiceBuilder
import coder.seventy.two.ecommerce.services.WebService
import kotlinx.android.synthetic.main.activity_single_category_products.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingleCategoryProducts : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_category_products)

        if (H.checkUserAuth()) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        var bundle: Bundle = intent.extras
        var catId = bundle.getString("cat_id")

        singCategoryProductRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        loadAllProductOfACategory(catId)
    }

    private fun loadAllProductOfACategory(catId: String) {
        val services: WebService = ServiceBuilder.buildService(WebService::class.java)
        val responseProduct: Call<Products> = services.getProductsOfACategory("Bearer $USER_TOKEN", catId)

        responseProduct.enqueue(object : Callback<Products> {
            override fun onFailure(call: Call<Products>, t: Throwable) {
                l(t.message!!)
            }

            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    val prod: Products = response.body()!!
                    val products = prod.products
                    singCategoryProductRecycler.adapter = ProductAdapter(this@SingleCategoryProducts, products)
                } else {
                    l("Something not right")
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
