package com.lappenfashion.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainHome
import com.makeramen.roundedimageview.RoundedImageView


class AccessoriesAdapter(
    private val context: Context,
    private val data: List<ResponseMainHome.Payload.Accessory?>?
) :
    RecyclerView.Adapter<AccessoriesAdapter.ViewHolder>() {

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val imgOfferPoster : RoundedImageView = view.findViewById(R.id.imgOfferPoster)
        val txtAccesoriesName : TextView = view.findViewById(R.id.txtAccesoriesName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.row_layout_accesories,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(data?.get(position)?.image).into(holder.imgOfferPoster)
        holder.txtAccesoriesName.text = data?.get(position)?.title
    }


}