package com.lappenfashion.ui.wishlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainWishList


class WishlistAdapter : RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    lateinit var context : WishListActivity
    var data : ArrayList<ResponseMainWishList.Payload?>?

    constructor(
        context: WishListActivity,
        advertisementList: ArrayList<ResponseMainWishList.Payload?>?,
    ) {
        this.context = context
        this.data = advertisementList
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var productImage = itemview.findViewById<ImageView>(R.id.imgProudctImage)
        var imgClose = itemview.findViewById<ImageView>(R.id.imgClose)
        var productName = itemview.findViewById<TextView>(R.id.txtProductName)
        var productDetails = itemview.findViewById<TextView>(R.id.txtProductDetails)
        var productPrice = itemview.findViewById<TextView>(R.id.txtPrice)
        var txtMoveToBag = itemview.findViewById<TextView>(R.id.txtMoveToBag)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.row_layout_wishlist, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context).load(data?.get(position)?.product?.mainImageName).into(holder.productImage)

        holder.productName.text = data?.get(position)?.product?.productName
        holder.productDetails.text = data?.get(position)?.product?.description

        holder.productPrice.text = "₹"+data?.get(position)?.product?.salePrice

        holder.imgClose.setOnClickListener {
            context.removeFromWishList(data?.get(position),position)
        }

        holder.txtMoveToBag.setOnClickListener {
            context.addToCart(data?.get(position),position)
        }
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    fun removeData(position: Int){
        data!!.removeAt(position)
        notifyDataSetChanged()
    }
}