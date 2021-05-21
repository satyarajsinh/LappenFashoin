package com.lappenfashion.ui.products

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lappenfashion.R
import com.lappenfashion.`interface`.CategoriesInterface
import com.lappenfashion.data.model.ResponseMainFilter
import com.lappenfashion.data.model.ResponseMainHome
import kotlinx.android.synthetic.main.activity_categories_details.*

class FilterSizeAdapter : RecyclerView.Adapter<FilterSizeAdapter.ViewHolder> {

    private var rawIndex: Int = -1
    lateinit var categoriesInterface : CategoriesInterface
    lateinit var context: ProductsByProductCategoryActivity
    var data: List<ResponseMainFilter.Payload.Size?>?

    constructor(context: ProductsByProductCategoryActivity, data: List<ResponseMainFilter.Payload.Size?>?) {
        this.context = context
        this.data = data
    }

    class ViewHolder(view : View):RecyclerView.ViewHolder(view) {
        val txtSize : TextView = view.findViewById(R.id.txtSize)
        val txtSizeValue : TextView = view.findViewById(R.id.txtSizeValue)
        val imgCheck : ImageView = view.findViewById(R.id.imgCheck)
        val relativeMain : RelativeLayout = view.findViewById(R.id.relativeMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_layout_filter_size,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtSizeValue.text = data?.get(position)?.size

        if(rawIndex == position){
            holder.imgCheck.visibility = View.VISIBLE
        }else{
            holder.imgCheck.visibility = View.GONE
        }

        holder.relativeMain.setOnClickListener {
            rawIndex = position
            context.selectedSize = data?.get(position)?.size!!
            notifyDataSetChanged()
        }
    }


}