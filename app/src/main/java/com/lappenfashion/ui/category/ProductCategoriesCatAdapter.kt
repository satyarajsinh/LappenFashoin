package com.lappenfashion.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemvvm.utils.Constants
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainCategories
import com.lappenfashion.ui.profile.CategoryFragment
import com.pixplicity.easyprefs.library.Prefs

class ProductCategoriesCatAdapter : RecyclerView.Adapter<ProductCategoriesCatAdapter.ViewHolder> {

    lateinit var context: Context
    var data: List<ResponseMainCategories.Payload.SubCategory.ProductCategory?>?
    lateinit var categoryFragment: CategoryFragment

    constructor(
        context: Context,
        data: List<ResponseMainCategories.Payload.SubCategory.ProductCategory?>?,
        categoryFragment: CategoryFragment
    ) {
        this.context = context
        this.data = data
        this.categoryFragment = categoryFragment
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
            Prefs.putString(Constants.PREF_PRODUCT_TITLE, data?.get(position)?.title.toString())
            Prefs.putString(Constants.PREF_PRODUCT_CATEGORY_ID,data?.get(position)?.productCategoryId.toString())
            categoryFragment.goToProductDetails()
        }
    }

}