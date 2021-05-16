package com.lappenfashion.ui.orderList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        holder.txtId.text = "Order Number : "+data.get(position)?.orderId.toString()
        holder.txtOrderStatus.text = "Status :  "+data.get(position)?.status.toString()
    }


}