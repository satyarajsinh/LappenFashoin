package com.lappenfashion.ui.wishlist

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.ybq.android.spinkit.SpinKitView
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
        var txtOutOfStock = itemview.findViewById<TextView>(R.id.txtOutOfStock)
        var txtDiscount = itemview.findViewById<TextView>(R.id.txtDiscount)
        var txtMoveToBag = itemview.findViewById<TextView>(R.id.txtMoveToBag)
        var cardView = itemview.findViewById<CardView>(R.id.cardView)
        var productMrp = itemview.findViewById<TextView>(R.id.txtMrp)
        val progressBar: SpinKitView = itemview.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.row_layout_wishlist, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(data?.get(position)?.product!=null) {
            if (data?.get(position)?.product?.isStockAvailable!! > 0) {
                holder.txtMoveToBag.visibility = View.VISIBLE
                holder.txtOutOfStock.visibility = View.GONE
            } else {
                holder.txtMoveToBag.visibility = View.GONE
                holder.txtOutOfStock.visibility = View.VISIBLE
            }

            holder.progressBar.visibility = View.VISIBLE
            Glide.with(context).load(data?.get(position)?.product?.mainImageName)
                .placeholder(R.mipmap.no_image)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(holder.productImage)

            holder.productName.text = data?.get(position)?.product?.productName
            holder.productDetails.text = data?.get(position)?.product?.description

            holder.productPrice.text = "₹" + data?.get(position)?.product?.salePrice
            holder.productMrp.text = "₹" + data!![position]?.product?.mrp
            holder.productMrp.setPaintFlags(holder.productMrp.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)

            holder.imgClose.setOnClickListener {
                context.removeFromWishList(data?.get(position), position)
            }

            if (data?.get(position)?.product?.discount!! <= 0) {
                holder.txtDiscount.visibility = View.GONE
            } else {
                holder.txtDiscount.setText("( "+data?.get(position)?.product?.discount.toString() + "% )")
                holder.txtDiscount.visibility = View.VISIBLE
            }

            holder.txtMoveToBag.setOnClickListener {
                context.addToCart(data?.get(position), position)
            }
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