package com.lappenfashion.ui.checkout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainAddress


class CheckoutAddressAdapter : RecyclerView.Adapter<CheckoutAddressAdapter.ViewHolder> {
    lateinit var context : CheckoutDeliveryAddressActivity
    var data : ArrayList<ResponseMainAddress.Payload?>?
    var rawIndex : Int = -1

    constructor(
        context: CheckoutDeliveryAddressActivity,
        advertisementList: ArrayList<ResponseMainAddress.Payload?>?,
    ) {
        this.context = context
        this.data = advertisementList
    }

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var txtName = itemview.findViewById<TextView>(R.id.txtName)
        var txtType = itemview.findViewById<TextView>(R.id.txtType)
        var txtAddress = itemview.findViewById<TextView>(R.id.txtAddress)
        var txtLocality = itemview.findViewById<TextView>(R.id.txtLocality)
        var txtCity = itemview.findViewById<TextView>(R.id.txtCity)
        var txtPincode = itemview.findViewById<TextView>(R.id.txtPincode)
        var txtState = itemview.findViewById<TextView>(R.id.txtState)
        var txtNumber = itemview.findViewById<TextView>(R.id.txtNumber)
        var radio = itemview.findViewById<RadioButton>(R.id.radio)
        var relativeMain = itemview.findViewById<RelativeLayout>(R.id.relativeMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.row_layout_checkout_address, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(data?.get(position)?.name!=null && data?.get(position)?.name !="") {
            holder.txtName.text = data?.get(position)?.name
        }
        if(data?.get(position)?.type!=null && data?.get(position)?.type !="") {
            holder.txtType.text = data?.get(position)?.type
        }
        if(data?.get(position)?.address!=null && data?.get(position)?.address !="") {
            holder.txtAddress.text = data?.get(position)?.address
        }
        if(data?.get(position)?.localityTown!=null && data?.get(position)?.localityTown !="") {
            holder.txtLocality.text = data?.get(position)?.localityTown
        }
        if(data?.get(position)?.city!=null && data?.get(position)?.city !="") {
            holder.txtCity.text = data?.get(position)?.city + " - "
        }
        if(data?.get(position)?.pincode!=null && data?.get(position)?.pincode !="") {
            holder.txtPincode.text = data?.get(position)?.pincode
        }
        if(data?.get(position)?.state!=null && data?.get(position)?.state!="") {
            holder.txtState.text = data?.get(position)?.state + ","
        }
        if(data?.get(position)?.mobileNumber!=null && data?.get(position)?.mobileNumber!="") {
            holder.txtNumber.text = "Mobile - "+data?.get(position)?.mobileNumber
        }

        if(rawIndex == position){
            holder.radio.isChecked = true
        }else{
            holder.radio.isChecked = false
        }

        holder.relativeMain.setOnClickListener {
            rawIndex = position
            notifyDataSetChanged()
            context.setDeliveryAddressId(data?.get(position)?.addressId)
        }

        holder.radio.setOnClickListener {
            rawIndex = position
            notifyDataSetChanged()
            context.setDeliveryAddressId(data?.get(position)?.addressId)
        }


    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    fun removeData(position: Int){
        data!!.removeAt(position)
        notifyDataSetChanged()
    }
}