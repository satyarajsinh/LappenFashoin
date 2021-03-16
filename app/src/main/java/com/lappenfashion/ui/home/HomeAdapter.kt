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

class HomeAdapter(private val context: Context,
                  private val data: List<ResponseMainHome.Payload.Category?>?
) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    class ViewHolder(view : View):RecyclerView.ViewHolder(view) {
        val txtCategoryName : TextView = view.findViewById(R.id.txtCategoryName)
        val imgPosterImage : ImageView = view.findViewById(R.id.imgPosterImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_layout_space,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtCategoryName.text = data?.get(position)?.title
        Glide.with(context).load(data?.get(position)?.image).into(holder.imgPosterImage)
    }


}