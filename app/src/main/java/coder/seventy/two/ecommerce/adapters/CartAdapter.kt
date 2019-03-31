package coder.seventy.two.ecommerce.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coder.seventy.two.ecommerce.R
import coder.seventy.two.ecommerce.modals.CartProduct
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_row.view.*

class CartAdapter(val context: Context, val products: List<CartProduct>) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_row, p0, false))
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val product = products[p1]
        p0.itemView.tvTitle.text = "${product.name } (${product.count})"
        Picasso.get().load(product.image).into(p0.itemView.tvImage)
        p0.itemView.tvPrice.text = product.price.toString()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}