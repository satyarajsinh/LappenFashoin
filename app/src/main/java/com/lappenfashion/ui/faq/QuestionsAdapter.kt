package com.lappenfashion.ui.faq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainFaq
import com.lappenfashion.ui.orderList.OrderListAdapter
import kotlinx.android.synthetic.main.activity_order_list.*


class QuestionsAdapter() :
    RecyclerView.Adapter<QuestionsAdapter.ViewHolder>() {

    lateinit var context : FaqActivity
    lateinit var data: List<ResponseMainFaq.Payload.FaqQuestion?>

    constructor(
        context: FaqActivity,
        data: List<ResponseMainFaq.Payload.FaqQuestion?>?
    ) : this() {
        this.context = context
        this.data = data!!
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val txtTitle : TextView = view.findViewById(R.id.txtTitle)
        val txtAns : TextView = view.findViewById(R.id.txtAns)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.row_layout_question,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(data.get(position)?.que!=null) {
            holder.txtTitle.text = data.get(position)?.que
        }

        if(data.get(position)?.ans!=null) {
            holder.txtAns.text = data.get(position)?.ans
        }


    }


}