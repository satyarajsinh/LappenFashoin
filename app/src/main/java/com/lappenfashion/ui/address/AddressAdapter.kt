package com.lappenfashion.ui.address

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainAddress
import com.lappenfashion.data.model.ResponseMainWishList


class AddressAdapter : RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    lateinit var context : AddressListingActivity
    var data : ArrayList<ResponseMainAddress.Payload?>?

    constructor(
        context: AddressListingActivity,
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
        var txtEdit = itemview.findViewById<TextView>(R.id.txtEdit)
        var txtRemove = itemview.findViewById<TextView>(R.id.txtRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.row_layout_address, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = data?.get(position)?.name
        holder.txtType.text = data?.get(position)?.type
        holder.txtAddress.text = data?.get(position)?.address
        holder.txtLocality.text = data?.get(position)?.localityTown
        holder.txtCity.text = data?.get(position)?.city + " - "
        holder.txtPincode.text = data?.get(position)?.pincode
        holder.txtState.text = data?.get(position)?.state
        holder.txtNumber.text = "Mobile - "+data?.get(position)?.mobileNumber
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    fun removeData(position: Int){
        data!!.removeAt(position)
        notifyDataSetChanged()
    }
}