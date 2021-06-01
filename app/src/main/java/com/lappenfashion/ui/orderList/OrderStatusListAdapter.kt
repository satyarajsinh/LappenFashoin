package com.lappenfashion.ui.orderList

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainOrderList
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class OrderStatusListAdapter() :
    RecyclerView.Adapter<OrderStatusListAdapter.ViewHolder>() {

    lateinit var context: OrderDetailsActivity
    lateinit var data: List<ResponseMainOrderList.Payload.Data.OrderStatusDetail?>

    constructor(
        context: OrderDetailsActivity,
        data: List<ResponseMainOrderList.Payload.Data.OrderStatusDetail?>
    ) : this() {
        this.context = context
        this.data = data!!
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardView)
        val view: View = view.findViewById(R.id.view)
        val txtOrderStatus: TextView = view.findViewById(R.id.txtOrderStatus)
        val txtOrderDate: TextView = view.findViewById(R.id.txtOrderDate)
        val txtOrderDetails: TextView = view.findViewById(R.id.txtOrderDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.row_layout_order_status,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data.get(position)?.status != null) {
            if (data.get(position)?.status == "cancelled") {
                holder.cardView.setCardBackgroundColor(Color.RED)
                holder.view.setBackgroundColor(Color.RED)
            } else {
                holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.green))
                holder.view.setBackgroundColor(context.resources.getColor(R.color.green))
            }
            holder.txtOrderStatus.text = data.get(position)?.display_status
        }

        if (data.get(position)?.createdAt != null) {
            holder.txtOrderDate.text = parseDateToddMMyyyy(data.get(position)?.createdAt)
        }

        if (data.get(position)?.description != null) {
            holder.txtOrderDetails.text = data.get(position)?.description
        }
    }

    fun parseDateToddMMyyyy(time: String?): String? {
        val inputPattern = "yyyy-MM-dd HH:mm:ss"
        val outputPattern = "dd/MMM/yyyy"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

}