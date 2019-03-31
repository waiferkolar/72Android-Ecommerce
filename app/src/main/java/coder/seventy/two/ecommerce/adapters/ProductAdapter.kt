package coder.seventy.two.ecommerce.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coder.seventy.two.ecommerce.R
import coder.seventy.two.ecommerce.SingleProductActivity
import coder.seventy.two.ecommerce.modals.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_row.view.*
import org.jetbrains.anko.toast

class ProductAdapter(val context: Context, val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.product_row, p0, false))
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val product = products[p1]
        p0.itemView.tvTitle.text = product.name
        Picasso.get().load(product.image).into(p0.itemView.tvImage)
        p0.itemView.tvPrice.text = product.price.toString()
        p0.itemView.btnDetail.setOnClickListener {
            val intent = Intent(context, SingleProductActivity::class.java)
            intent.putExtra("product", product)
            context.startActivity(intent)
        }

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}