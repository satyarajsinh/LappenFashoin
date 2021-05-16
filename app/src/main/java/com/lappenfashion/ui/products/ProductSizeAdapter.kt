package com.lappenfashion.ui.products

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemvvm.utils.Constants
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainProductDetails
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs


class ProductSizeAdapter() :
    RecyclerView.Adapter<ProductSizeAdapter.ViewHolder>() {

    var rawIndex : Int = -1
    lateinit var context : ProductDetailsActivity
    lateinit var data: List<ResponseMainProductDetails.Payload.Size>
    var productId: Int = 0

    constructor(context: ProductDetailsActivity,data: List<ResponseMainProductDetails.Payload.Size>,productId: Int) : this() {
        this.context = context
        this.data = data
        this.rawIndex = productId
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val txtSize : TextView = view.findViewById(R.id.txtSize)
        val linearMain : RelativeLayout = view.findViewById(R.id.linearMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.row_layout_product_size,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(rawIndex == data.get(position).productId){
            holder.linearMain.setBackgroundResource(R.drawable.rounded_border_accent_20)
        }else{
            holder.linearMain.setBackgroundResource(R.drawable.rounded_border_grey_20)
        }

        holder.linearMain.setOnClickListener {
            rawIndex = data.get(position).productId!!
            notifyDataSetChanged()
            if (NetworkConnection.checkConnection(context)) {
                Prefs.putString(Constants.PREF_SELECTED_PRODUCT_SIZE,data.get(position).productId.toString())
                Helper.showLoader(context)
                context.getProductDetailsById(data.get(position).productId!!)
            } else {
                Helper.showTost(context, context.resources.getString(R.string.no_internet))
            }

        }

        holder.txtSize.text = data?.get(position)?.size
    }


}