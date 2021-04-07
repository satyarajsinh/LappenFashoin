package com.lappenfashion.ui.categoriesDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainHome

class SubCategoriesAdapter : RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder> {

    lateinit var context: Context
    var data: List<ResponseMainHome.Payload.Category.SubCategory?>?

    constructor(context: Context, data: List<ResponseMainHome.Payload.Category.SubCategory?>?) {
        this.context = context
        this.data = data
    }

    class ViewHolder(view : View):RecyclerView.ViewHolder(view) {
        val txtSubCategoryName : TextView = view.findViewById(R.id.txtSubCategoryName)
        val imgDown : ImageView = view.findViewById(R.id.imgDown)
        val recyclerProductCategory : RecyclerView = view.findViewById(R.id.recyclerProductCategories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_layout_subcategory,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtSubCategoryName.text = data?.get(position)?.title

        holder.txtSubCategoryName.setOnClickListener {
            if(holder.imgDown.rotation == 360f){
                holder.imgDown.rotation = 180f
                holder.recyclerProductCategory.visibility = View.VISIBLE
                holder.recyclerProductCategory.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                holder.recyclerProductCategory.setHasFixedSize(true)
                var offersAdapter = ProductCategoriesAdapter(
                    context,
                    data?.get(position)?.productCategory
                )
                holder.recyclerProductCategory.adapter = offersAdapter
            }else{
                holder.imgDown.rotation = 360f
                holder.recyclerProductCategory.visibility = View.GONE

            }
        }


    }

}