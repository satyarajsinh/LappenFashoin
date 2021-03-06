package com.lappenfashion.ui.products

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.ybq.android.spinkit.SpinKitView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainProductsByCategory
import kotlinx.android.synthetic.main.activity_product_details.*


class ProductsByCategoryAdapter : RecyclerView.Adapter<ProductsByCategoryAdapter.ViewHolder> {
    lateinit var context: ProductsByProductCategoryActivity
    lateinit var data: List<ResponseMainProductsByCategory.Payload.Data?>

    constructor(
        context: ProductsByProductCategoryActivity,
        advertisementList: List<ResponseMainProductsByCategory.Payload.Data?>?,
    ) {
        this.context = context
        this.data = advertisementList!!
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var productImage = itemview.findViewById<ImageView>(R.id.imgProudctImage)
        var imgLiked = itemview.findViewById<ImageView>(R.id.imgLiked)
        var productName = itemview.findViewById<TextView>(R.id.txtProductName)
        var txtDiscount = itemview.findViewById<TextView>(R.id.txtDiscount)
        var productDetails = itemview.findViewById<TextView>(R.id.txtProductDetails)
        var productPrice = itemview.findViewById<TextView>(R.id.txtPrice)
        var productMrp = itemview.findViewById<TextView>(R.id.txtMrp)
        var relativeMain = itemview.findViewById<RelativeLayout>(R.id.relativeMain)
        var cardView = itemview.findViewById<CardView>(R.id.cardView)
        var txtOutOfStock = itemview.findViewById<TextView>(R.id.txtOutOfStock)
        val progressBar: SpinKitView = itemview.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context)
            .inflate(R.layout.row_layout_products_by_category, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.progressBar.visibility = View.VISIBLE
        Glide.with(context).load(data[position]?.mainImageName).placeholder(R.mipmap.no_image)
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

        if(data[position]?.isStockAvailable!! > 0){
            holder.txtOutOfStock.visibility = View.GONE
            holder.productMrp.visibility = View.VISIBLE
            holder.productPrice.visibility = View.VISIBLE
        }else{
            holder.txtOutOfStock.visibility = View.VISIBLE
            holder.productMrp.visibility = View.GONE
            holder.productPrice.visibility = View.GONE
        }

        holder.productName.text = data[position]?.productName
        holder.productDetails.text = data[position]?.description
        holder.productPrice.text = "₹" + data[position]?.salePrice
        holder.productPrice.text = "₹" + data[position]?.salePrice
        holder.productMrp.text = "₹" + data[position]?.mrp
        holder.productMrp.setPaintFlags(holder.productMrp.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)

        if(data.get(position)?.discount!! <= 0){
            holder.cardView.visibility = View.GONE
        }else{
            holder.txtDiscount.setText(data.get(position)?.discount.toString()+"%")
            holder.cardView.visibility = View.VISIBLE
        }

        holder.relativeMain.setOnClickListener {
            context.goToDetails(data.get(position))
        }

        holder.imgLiked.setOnClickListener {
            context.addToWishList(data[position]?.productId,position)
        }

        if(data[position]?.isWishList == 1){
            holder.imgLiked.setColorFilter(
                ContextCompat.getColor(context, R.color.colorAccent),
                android.graphics.PorterDuff.Mode.SRC_ATOP
            )
        }else{
            holder.imgLiked.setColorFilter(
                ContextCompat.getColor(context, R.color.black),
                android.graphics.PorterDuff.Mode.SRC_ATOP
            )
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setWishLIst(position: Int){
        if(data[position]?.isWishList == 1){
            data[position]?.isWishList = 0
        }else{
            data[position]?.isWishList = 1
        }

        notifyDataSetChanged()
    }

}