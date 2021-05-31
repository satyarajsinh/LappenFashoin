package com.lappenfashion.ui.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainFilter
import com.lappenfashion.data.model.ResponseMainNotification

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    var context: NotificationActivity
    var data: List<ResponseMainNotification.Payload?>

    constructor(
        context: NotificationActivity,
        data: List<ResponseMainNotification.Payload?>
    ) {
        this.context = context
        this.data = data
    }

    class ViewHolder(view : View):RecyclerView.ViewHolder(view) {
        val txtName : TextView = view.findViewById(R.id.txtName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_layout_notification,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = data?.get(position)?.title
    }


}