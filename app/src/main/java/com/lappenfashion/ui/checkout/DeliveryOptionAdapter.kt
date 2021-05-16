package com.lappenfashion.ui.checkout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainDeliveryOption
import com.lappenfashion.data.model.ResponseMainWishList


class DeliveryOptionAdapter : RecyclerView.Adapter<DeliveryOptionAdapter.ViewHolder> {
    lateinit var context : CheckoutActivity
    var data : ArrayList<ResponseMainDeliveryOption.Payload?>?
    var rawIndex : Int = -1

    constructor(
        context: CheckoutActivity,
        advertisementList: ArrayList<ResponseMainDeliveryOption.Payload?>?,
    ) {
        this.context = context
        this.data = advertisementList
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var txtTitle = itemview.findViewById<TextView>(R.id.txtTitle)
        var txtDescription = itemview.findViewById<TextView>(R.id.txtDescription)
        var radio = itemview.findViewById<RadioButton>(R.id.radio)
        var relativeMain = itemview.findViewById<RelativeLayout>(R.id.relativeMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.row_layout_delivery_option, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTitle.text = data?.get(position)?.title
        holder.txtDescription.text = data?.get(position)?.description

        if(rawIndex == position){
            holder.radio.isChecked = true
        }else{
            holder.radio.isChecked = false
        }

        holder.relativeMain.setOnClickListener {
            rawIndex = position
            notifyDataSetChanged()
            context.setDeliveryOptionId(data?.get(position)?.deliveryOptionId)
        }

    }

    override fun getItemCount(): Int {
        return data?.size!!
    }
}