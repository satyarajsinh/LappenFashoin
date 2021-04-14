package com.lappenfashion.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainLocalCart


class LocalCartAdapter : RecyclerView.Adapter<LocalCartAdapter.ViewHolder> {
    lateinit var context : CartActivity
    var data : ArrayList<ResponseMainLocalCart>

    constructor(
        context: CartActivity,
        advertisementList: ArrayList<ResponseMainLocalCart>,
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
        var txtRemove = itemview.findViewById<TextView>(R.id.txtRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.row_layout_cart, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context).load(data!![position]?.cartImage).into(holder.productImage)

        holder.productName.text = data!![position]?.cartTitle
        holder.productPrice.text = "₹"+data!![position]?.cartAmount
        holder.productPrice.text = "₹"+data!![position]?.cartAmount
        holder.txtQty.text = data!![position]?.cartQty

        holder.imgAdd.setOnClickListener {
            holder.txtQty.text= (holder.txtQty.text.toString().toInt()+1).toString()
            context.updateQuantity(data!![position],holder.txtQty.text.toString().toInt())
        }

        holder.imgMinus.setOnClickListener {
            if(holder.txtQty.text.toString().toInt() != 1) {
                holder.txtQty.text = (holder.txtQty.text.toString().toInt() - 1).toString()
                context.updateQuantity(data!![position],holder.txtQty.text.toString().toInt())
            }
        }

        holder.txtRemove.setOnClickListener {
            context.removeProduct(data!![position])
            data.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

}