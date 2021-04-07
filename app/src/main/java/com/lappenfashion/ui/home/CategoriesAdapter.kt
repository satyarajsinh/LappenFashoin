package com.lappenfashion.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lappenfashion.R
import com.lappenfashion.`interface`.CategoriesInterface
import com.lappenfashion.data.model.ResponseMainHome

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    lateinit var categoriesInterface : CategoriesInterface
    lateinit var context: Context
    var data: List<ResponseMainHome.Payload.Category?>?

    constructor(context: Context, data: List<ResponseMainHome.Payload.Category?>?,homeFragment: HomeFragment) {
        this.categoriesInterface = homeFragment
        this.context = context
        this.data = data
    }

    class ViewHolder(view : View):RecyclerView.ViewHolder(view) {
        val txtCategoryName : TextView = view.findViewById(R.id.txtCategoryName)
        val imgPosterImage : ImageView = view.findViewById(R.id.imgPosterImage)
        val linearMain : LinearLayout = view.findViewById(R.id.linearMain)
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

        holder.linearMain.setOnClickListener {
            categoriesInterface.goToSubCategories(data?.get(position))
        }
    }


}