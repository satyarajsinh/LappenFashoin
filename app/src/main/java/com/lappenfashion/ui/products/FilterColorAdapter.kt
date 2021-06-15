package com.lappenfashion.ui.products

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
import com.lappenfashion.utils.Helper
import java.util.regex.Matcher
import java.util.regex.Pattern

class FilterColorAdapter : RecyclerView.Adapter<FilterColorAdapter.ViewHolder> {

    private var rawIndex: Int = -1
    lateinit var categoriesInterface : CategoriesInterface
    lateinit var context: ProductsByProductCategoryActivity
    var data: List<ResponseMainFilter.Payload.Color?>
    var selectedColor :String = ""

    constructor(
        context: ProductsByProductCategoryActivity,
        data: List<ResponseMainFilter.Payload.Color?>,
        selectedColor: String
    ) {
        this.context = context
        this.data = data
        this.selectedColor = selectedColor
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

        try{
            if(isValidHexaCode(data.get(position)?.colorCode!!)) {
                if(data?.get(position)?.colorCode!=null && data?.get(position)?.colorCode!="") {
                    holder.cardView.visibility = View.VISIBLE
                    holder.cardView.setCardBackgroundColor(Color.parseColor(data?.get(position)?.colorCode))
                }
            }else{
                holder.cardView.visibility = View.GONE
            }
        }catch (e : Exception){
            Helper.showTost(context,"Display color issues.")
        }


        if(selectedColor == data?.get(position)?.color){
            rawIndex = position
        }

        if(rawIndex == position){
            holder.imgCheck.visibility = View.VISIBLE
        }else{
            holder.imgCheck.visibility = View.GONE
        }

        holder.relativeMain.setOnClickListener {
            rawIndex = position
            selectedColor = data?.get(position)?.color!!
            context.selectedColor = data.get(position)?.color!!
            notifyDataSetChanged()
        }
    }

    fun isValidHexaCode(colorCode : String): Boolean {
        val regex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"
        val p: Pattern = Pattern.compile(regex)
        val m: Matcher = p.matcher(colorCode)
        return m.matches()
    }
}