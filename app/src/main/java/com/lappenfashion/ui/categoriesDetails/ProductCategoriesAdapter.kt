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

class ProductCategoriesAdapter : RecyclerView.Adapter<ProductCategoriesAdapter.ViewHolder> {

    lateinit var context: CategoriesDetailsActivity
    var data: List<ResponseMainHome.Payload.Category.SubCategory.ProductCategory?>?

    constructor(context: CategoriesDetailsActivity, data: List<ResponseMainHome.Payload.Category.SubCategory.ProductCategory?>?) {
        this.context = context
        this.data = data
    }

    class ViewHolder(view : View):RecyclerView.ViewHolder(view) {
        val txtProductCategoryName : TextView = view.findViewById(R.id.txtProductCategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_layout_productcategory,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtProductCategoryName.text = data?.get(position)?.title
        holder.txtProductCategoryName.setOnClickListener {
            context.goToProducts(data?.get(position))
        }
    }

}