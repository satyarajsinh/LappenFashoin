package com.lappenfashion.ui.home

import android.content.Context
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMain
import retrofit2.Response


class DealsOfTheDayAdapter(
    private val context: Context,
    private val data: Response<ResponseMain>
) :
    RecyclerView.Adapter<DealsOfTheDayAdapter.ViewHolder>() {

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val txtSpaceName : TextView = view.findViewById(R.id.txtSpaceName)
        val imgPosterImage : ImageView = view.findViewById(R.id.imgPosterImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.row_layout_deals_of_the_day,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.body()?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val s = data.body()?.get(position)?.name + " &"
        var end = " more offers"
        val ss1 = SpannableString(s)
        val ss2 = SpannableString(end)
        ss1.setSpan(RelativeSizeSpan(1.5f), 0, s!!.length, 0) // set size
        holder.txtSpaceName.text = TextUtils.concat(ss1 ,ss2)
        Glide.with(context).load(data.body()?.get(position)?.imageurl).into(holder.imgPosterImage)
    }


}