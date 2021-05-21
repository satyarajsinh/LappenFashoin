package com.lappenfashion.ui.products

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.lappenfashion.R
import com.lappenfashion.`interface`.CategoriesInterface
import com.lappenfashion.data.model.ResponseMainFilter
import kotlinx.android.synthetic.main.activity_categories_details.*

class FilterColorAdapter : RecyclerView.Adapter<FilterColorAdapter.ViewHolder> {

    private var rawIndex: Int = -1
    lateinit var categoriesInterface : CategoriesInterface
    lateinit var context: ProductsByProductCategoryActivity
    var data: List<ResponseMainFilter.Payload.Color?>

    constructor(context: ProductsByProductCategoryActivity, data: List<ResponseMainFilter.Payload.Color?>) {
        this.context = context
        this.data = data
    }

    class ViewHolder(view : View):RecyclerView.ViewHolder(view) {
        val cardView : CardView = view.findViewById(R.id.cardView)
        val txtColor : TextView = view.findViewById(R.id.txtColor)
        val imgCheck : ImageView = view.findViewById(R.id.imgCheck)
        val relativeMain : RelativeLayout = view.findViewById(R.id.relativeMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_layout_filter_color,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtColor.text = data?.get(position)?.color

        holder.cardView.setCardBackgroundColor(Color.parseColor(data?.get(position)?.colorCode))

        if(rawIndex == position){
            holder.imgCheck.visibility = View.VISIBLE
        }else{
            holder.imgCheck.visibility = View.GONE
        }

        holder.relativeMain.setOnClickListener {
            rawIndex = position
            context.selectedColor = data.get(position)?.color!!
            notifyDataSetChanged()
        }
    }


}