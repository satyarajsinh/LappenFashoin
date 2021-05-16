package com.lappenfashion.ui.search

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.ybq.android.spinkit.SpinKitView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainHome
import com.lappenfashion.data.model.ResponseMainSearchProduct
import kotlinx.android.synthetic.main.activity_categories_details.*

class SearchProductAdapter : RecyclerView.Adapter<SearchProductAdapter.ViewHolder> {

    lateinit var context: SearchProductActivity
    var data: List<ResponseMainSearchProduct.Payload?>?

    constructor(context: SearchProductActivity, data: List<ResponseMainSearchProduct.Payload?>?) {
        this.context = context
        this.data = data
    }

    class ViewHolder(view : View):RecyclerView.ViewHolder(view) {
        val productName : TextView = view.findViewById(R.id.productName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_layout_search_product,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productName.text = data?.get(position)?.productName

        holder.productName.setOnClickListener {
            context.goToProductListing(data?.get(position)?.productName);
        }
    }


}