package com.lappenfashion.ui.faq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainFaq


class FaqAdapter() :
    RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    lateinit var context : FaqActivity
    lateinit var data:  List<ResponseMainFaq.Payload?>

    constructor(
        context: FaqActivity,
        data: List<ResponseMainFaq.Payload?>
    ) : this() {
        this.context = context
        this.data = data!!
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val txtTitle : TextView = view.findViewById(R.id.txtTitle)
        val recyclerQuestion : RecyclerView = view.findViewById(R.id.recyclerQuestion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.row_layout_faq,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txtTitle.text = data.get(position)?.title

        holder.txtTitle.setOnClickListener {
            if(holder.recyclerQuestion.visibility == View.GONE) {
                holder.recyclerQuestion.visibility = View.VISIBLE
                holder.recyclerQuestion.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                holder.recyclerQuestion.setHasFixedSize(true)
                var orderListAdapter = QuestionsAdapter(
                    context,
                    data.get(position)?.faqQuestion
                )
                holder.recyclerQuestion.adapter = orderListAdapter
            }else{
                holder.recyclerQuestion.visibility = View.GONE
            }
        }

    }


}