package com.lappenfashion.ui.placeOrder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainPlaceOrderView
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper


class PlaceOrderProductAdapter(
    val context: Context,
    val cartList: List<ResponseMainPlaceOrderView.Payload.Cart?>?
) :
    RecyclerView.Adapter<PlaceOrderProductAdapter.ViewHolder>() {

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val txtProductName : TextView = view.findViewById(R.id.txtProductName)
        val txtProductPrice : TextView = view.findViewById(R.id.txtProductPrice)
        val txtSize : TextView = view.findViewById(R.id.txtSize)
        val txtColor : TextView = view.findViewById(R.id.txtColor)
        val imgProductImage : ImageView = view.findViewById(R.id.imgProductImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.row_layout_place_order_product,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(cartList?.get(position)?.product?.productName!=null) {
            holder.txtProductName.text = cartList?.get(position)?.product?.productName
        }

        holder.txtProductPrice.text = "₹"+cartList?.get(position)?.product?.salePrice.toString()
        holder.txtSize.text = "Size : "+cartList?.get(position)?.product?.size.toString()

        if(cartList?.get(position)?.product?.mainImageName!=null){
            Glide.with(context).load(cartList?.get(position)?.product?.mainImageName).into(holder.imgProductImage)
        }
    }


}