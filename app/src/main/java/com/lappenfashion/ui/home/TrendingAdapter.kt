package com.lappenfashion.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainHome


class TrendingAdapter(
    private val context: Context,
    private val data: List<ResponseMainHome.Payload.Trending?>?
) :
    RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val imgTrendingPoster : ImageView = view.findViewById(R.id.imgTrendingPoster)
        val txtDiscountPrice : TextView = view.findViewById(R.id.txtDiscountPrice)
        val txtOrignalPrice : TextView = view.findViewById(R.id.txtOrignalPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.row_layout_trending,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(data?.get(position)?.image).into(holder.imgTrendingPoster)
        holder.txtDiscountPrice.text = "₹"+data?.get(position)?.salePrice
        holder.txtOrignalPrice.text = "₹"+data?.get(position)?.price
    }


}