package com.example.lab1nativeui.productlist

import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab1nativeui.R
import com.example.lab1nativeui.model.Product

class ProductListAdapter(private val productList: List<Product>): RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = productList[position].name
        holder.quantity.text = productList[position].quantity.toString()
        holder.price.text = productList[position].price.toString() +"$"
        holder.picture.setImageResource(R.drawable.cupcake)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.productTitle)
        val quantity = itemView.findViewById<TextView>(R.id.quantity)
        val price = itemView.findViewById<TextView>(R.id.price)
        val picture = itemView.findViewById<ImageView>(R.id.productImage)
    }

}