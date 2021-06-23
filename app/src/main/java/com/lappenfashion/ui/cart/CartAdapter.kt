package com.lappenfashion.ui.cart

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainCartNew
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import java.util.regex.Matcher
import java.util.regex.Pattern


class CartAdapter : RecyclerView.Adapter<CartAdapter.ViewHolder> {
    lateinit var context: CartActivity
    var data: ArrayList<ResponseMainCartNew.Payload.Cart?>?

    constructor(
        context: CartActivity,
        advertisementList: ArrayList<ResponseMainCartNew.Payload.Cart?>?,
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
        var cardView = itemview.findViewById<CardView>(R.id.cardView)
        var txtSize = itemview.findViewById<TextView>(R.id.txtSize)
        var txtSizeOther = itemview.findViewById<TextView>(R.id.txtSizeOther)
        var txtMoveToWishList = itemview.findViewById<TextView>(R.id.txtMoveToWishList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.row_layout_cart, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (data!![position]?.product?.mainImageName != null && data!![position]?.product?.mainImageName != "") {
            Glide.with(context).load(data!![position]?.product?.mainImageName)
                .placeholder(R.mipmap.no_image).into(holder.productImage)
        }

        try {
            if (isValidHexaCode(data?.get(position)?.product?.colorCode!!)) {
                if (data?.get(position)?.product?.colorCode != null && data?.get(position)?.product?.colorCode != "") {
                    holder.cardView.visibility = View.VISIBLE
                    holder.cardView.setCardBackgroundColor(Color.parseColor(data?.get(position)?.product?.colorCode))
                }
            } else {
                holder.cardView.visibility = View.GONE
            }
        } catch (e: Exception) {
            Helper.showTost(context, "Display color issues.")
        }


        if (data?.get(position)?.product?.size != null && data?.get(position)?.product?.size != "") {
            if(data?.get(position)?.product?.size_view_flag == 1) {
                holder.txtSize.visibility = View.VISIBLE
                holder.txtSizeOther.visibility = View.GONE
                holder.txtSize.text = data?.get(position)?.product?.size
            }else{
                holder.txtSize.visibility = View.GONE
                holder.txtSizeOther.visibility = View.VISIBLE
                holder.txtSizeOther.text = data?.get(position)?.product?.size
            }
        } else {
            holder.txtSize.visibility = View.GONE
            holder.txtSizeOther.visibility = View.GONE
        }

        holder.productName.text = data!![position]?.product?.productName
        holder.productPrice.text = "₹" + data!![position]?.amount.toString()
        holder.txtQty.text = data!![position]?.quantity.toString()

        holder.imgAdd.setOnClickListener {
            holder.txtQty.text = (holder.txtQty.text.toString().toInt() + 1).toString()
            if (NetworkConnection.checkConnection(context)) {
                Helper.showLoader(context)
                context.updateCart(data!!.get(position), holder.txtQty.text.toString())
            } else {
                Helper.showTost(context, context.resources.getString(R.string.no_internet))
            }
        }

        holder.imgMinus.setOnClickListener {
            if (holder.txtQty.text.toString().toInt() != 1) {
                holder.txtQty.text = (holder.txtQty.text.toString().toInt() - 1).toString()
                if (NetworkConnection.checkConnection(context)) {
                    Helper.showLoader(context)
                    context.updateCart(data!!.get(position), holder.txtQty.text.toString())
                } else {
                    Helper.showTost(context, context.resources.getString(R.string.no_internet))
                }
            }
        }

        holder.txtRemove.setOnClickListener {
            if (NetworkConnection.checkConnection(context)) {
                Helper.showLoader(context)
                context.removeCart(data!![position])
            } else {
                Helper.showTost(context, context.resources.getString(R.string.no_internet))
            }
        }

        holder.txtMoveToWishList.setOnClickListener {
            context.addToWishList(data!![position]?.product?.productId)
        }
    }

    fun isValidHexaCode(colorCode : String): Boolean {
        val regex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"
        val p: Pattern = Pattern.compile(regex)
        val m: Matcher = p.matcher(colorCode)
        return m.matches()
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

}