package com.lappenfashion.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMain
import retrofit2.Response

class CategoriesAdapter(private val context: Context,
                        private val data: Response<ResponseMain>) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    class ViewHolder(view : View):RecyclerView.ViewHolder(view) {
        val imgPosterImage : ImageView = view.findViewById(R.id.imgPosterImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_layout_categories,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.body()?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(data.body()?.get(position)?.imageurl).into(holder.imgPosterImage)
    }


}