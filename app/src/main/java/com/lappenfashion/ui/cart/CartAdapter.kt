package com.lappenfashion.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseCart
import com.lappenfashion.data.model.ResponseMainAddress


class CartAdapter : RecyclerView.Adapter<CartAdapter.ViewHolder> {
    lateinit var context : Context
    lateinit var data : ArrayList<ResponseCart>

    constructor(
        context: Context,
        advertisementList: ArrayList<ResponseCart>,
    ) {
        this.context = context
        this.data = advertisementList
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var productImage = itemview.findViewById<ImageView>(R.id.imgProductImage)
        var imgAdd = itemview.findViewById<ImageView>(R.id.imgAdd)
        var imgMinus = itemview.findViewById<ImageView>(R.id.imgMinus)
        var productName = itemview.findViewById<TextView>(R.id.txtProductName)
        var productPrice = itemview.findViewById<TextView>(R.id.txtProductPrice)
        var txtQty = itemview.findViewById<TextView>(R.id.txtQty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.row_layout_cart, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context).load(data[position].image).into(holder.productImage)

        holder.productName.text = data[position].productName
        holder.productPrice.text = data[position].price

        holder.imgAdd.setOnClickListener {
            holder.txtQty.text= (holder.txtQty.text.toString().toInt()+1).toString()
        }

        holder.imgMinus.setOnClickListener {
            if(holder.txtQty.text.toString().toInt() != 1) {
                holder.txtQty.text = (holder.txtQty.text.toString().toInt() - 1).toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}