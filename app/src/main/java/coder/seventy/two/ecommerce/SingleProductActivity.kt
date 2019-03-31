package coder.seventy.two.ecommerce

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import coder.seventy.two.ecommerce.libby.H
import coder.seventy.two.ecommerce.modals.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_sing_product.*

class SingleProductActivity : AppCompatActivity() {
    var cartCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_product)

        val product = intent.getParcelableExtra<Product>("product")

        productTitle.text = product.name
        Picasso.get().load(product.image).into(productImage)
        productPrice.text = product.price.toString()
        productDescription.text = product.description


        addToCartImage.setOnClickListener {
            H.addToCart(product.id)
            cartCount?.text = H.getCartCount().toString()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val item: MenuItem = menu.findItem(R.id.cart)
        MenuItemCompat.setActionView(item, R.layout.my_cart_layout)

        val cartView: View? = MenuItemCompat.getActionView(item)
        cartCount = cartView?.findViewById(R.id.cartCount)
        val cartImage: ImageView? = cartView?.findViewById(R.id.cartImage)

        cartCount?.text = H.getCartCount().toString()

        cartImage?.setOnClickListener {
            startActivity(Intent(this@SingleProductActivity, MyCart::class.java))
        }
        return super.onCreateOptionsMenu(menu)
    }
}
