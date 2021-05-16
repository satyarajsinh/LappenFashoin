package com.lappenfashion.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainCartNew
import com.lappenfashion.data.model.ResponseMainCheckoutCart


class CheckoutCartAdapter : RecyclerView.Adapter<CheckoutCartAdapter.ViewHolder> {
    lateinit var context : CartActivity
    var data : List<ResponseMainCheckoutCart.Payload?>

    constructor(
        context: CartActivity,
        advertisementList: List<ResponseMainCheckoutCart.Payload?>,
    ) {
        this.context = context
        this.data = advertisementList
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var productImage = itemview.findViewById<ImageView>(R.id.imgProductImage)
        var productName = itemview.findViewById<TextView>(R.id.txtProductName)
        var productPrice = itemview.findViewById<TextView>(R.id.txtProductPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.row_layout_checkout_cart, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(data!![position]?.mainImageName).into(holder.productImage)
        holder.productName.text = data!![position]?.productName
        holder.productPrice.text = "₹"+data!![position]?.mrp.toString()
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

}