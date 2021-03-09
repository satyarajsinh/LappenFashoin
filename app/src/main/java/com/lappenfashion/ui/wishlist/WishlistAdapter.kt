package com.lappenfashion.ui.wishlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseCart
import com.lappenfashion.data.model.ResponseWishlist


class WishlistAdapter : RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    lateinit var context : Context
    lateinit var data : ArrayList<ResponseWishlist>

    constructor(
        context: Context,
        advertisementList: ArrayList<ResponseWishlist>,
    ) {
        this.context = context
        this.data = advertisementList
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var productImage = itemview.findViewById<ImageView>(R.id.imgProudctImage)
        var productName = itemview.findViewById<TextView>(R.id.txtProductName)
        var productDetails = itemview.findViewById<TextView>(R.id.txtProductDetails)
        var productPrice = itemview.findViewById<TextView>(R.id.txtPrice)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.row_layout_wishlist, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context).load(data[position].image).into(holder.productImage)

        holder.productName.text = data[position].productName
        holder.productDetails.text = data[position].productDetail

        holder.productPrice.text = "₹"+data[position].price

    }

    override fun getItemCount(): Int {
        return data.size
    }

}