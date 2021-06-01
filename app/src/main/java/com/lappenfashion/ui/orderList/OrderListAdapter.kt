package com.lappenfashion.ui.orderList

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.ybq.android.spinkit.SpinKitView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainOrderList
import com.lappenfashion.data.model.ResponseMainProductDetails


class OrderListAdapter() :
    RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    lateinit var context : OrderListActivity
    lateinit var data:  List<ResponseMainOrderList.Payload.Data?>

    constructor(
        context: OrderListActivity,
        data: List<ResponseMainOrderList.Payload.Data?>
    ) : this() {
        this.context = context
        this.data = data!!
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val txtId : TextView = view.findViewById(R.id.txtId)
        val txtOrderStatus : TextView = view.findViewById(R.id.txtOrderStatus)
        val imgProductImage : ImageView = view.findViewById(R.id.imgProductImage)
        val progressBar: SpinKitView = view.findViewById(R.id.progressBar)
        val relativeMain : RelativeLayout = view.findViewById(R.id.relativeMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.row_layout_order_list,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.progressBar.visibility = View.VISIBLE
        Glide.with(context).load(data[position]?.products!![0]?.mainImageName).placeholder(R.mipmap.no_image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(holder.imgProductImage)

        holder.txtId.text = data.get(position)?.display_status.toString()
        holder.txtOrderStatus.text = data.get(position)?.products!![0]?.productName.toString()

        holder.relativeMain.setOnClickListener {
            context.goToOrderDetails(data.get(position)!!)
        }
    }


}