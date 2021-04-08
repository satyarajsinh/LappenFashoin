package com.lappenfashion.ui.products

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
import com.lappenfashion.data.model.ResponseMainProductsByCategory
import com.lappenfashion.data.model.ResponseWishlist


class ProductsByCategoryAdapter : RecyclerView.Adapter<ProductsByCategoryAdapter.ViewHolder> {
    lateinit var context : ProductsByProductCategoryActivity
    lateinit var data : List<ResponseMainProductsByCategory.Payload.Data?>

    constructor(
        context: ProductsByProductCategoryActivity,
        advertisementList: List<ResponseMainProductsByCategory.Payload.Data?>?,
    ) {
        this.context = context
        this.data = advertisementList!!
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var productImage = itemview.findViewById<ImageView>(R.id.imgProudctImage)
        var productName = itemview.findViewById<TextView>(R.id.txtProductName)
        var productDetails = itemview.findViewById<TextView>(R.id.txtProductDetails)
        var productPrice = itemview.findViewById<TextView>(R.id.txtPrice)
        var relativeMain = itemview.findViewById<RelativeLayout>(R.id.relativeMain)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.row_layout_products_by_category, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(data[position]?.mainImageName).into(holder.productImage)
        holder.productName.text = data[position]?.productName
        holder.productDetails.text = data[position]?.description
        holder.productPrice.text = "₹"+data[position]?.salePrice

        holder.relativeMain.setOnClickListener {
            context.goToDetails(data.get(position))
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}