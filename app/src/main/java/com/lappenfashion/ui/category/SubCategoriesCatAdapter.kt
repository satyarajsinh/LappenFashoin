package com.lappenfashion.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainCategories
import com.lappenfashion.data.model.ResponseMainHome

class SubCategoriesCatAdapter : RecyclerView.Adapter<SubCategoriesCatAdapter.ViewHolder> {

    lateinit var context: Context
    var data:  List<ResponseMainCategories.Payload.SubCategory?>?

    constructor(context: Context, data: List<ResponseMainCategories.Payload.SubCategory?>?) {
        this.context = context
        this.data = data
    }

    class ViewHolder(view : View):RecyclerView.ViewHolder(view) {
        val txtSubCategoryName : TextView = view.findViewById(R.id.txtSubCategoryName)
        val imgDown : ImageView = view.findViewById(R.id.imgDown)
        val recyclerProductCategory : RecyclerView = view.findViewById(R.id.recyclerProductCategories)
        val view : View = view.findViewById(R.id.view)
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
        holder.view.visibility = View.GONE
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
                var offersAdapter = ProductCategoriesCatAdapter(
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