package com.lappenfashion.ui.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
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
import com.lappenfashion.data.model.ResponseMainHome


class DealsOfTheDayAdapter(
    private val context: Context,
    private val data: List<ResponseMainHome.Payload.DealsOfTheDay?>?,
    private val homeFragment: HomeFragment
) :
    RecyclerView.Adapter<DealsOfTheDayAdapter.ViewHolder>() {

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val txtSpaceName : TextView = view.findViewById(R.id.txtSpaceName)
        val imgPosterImage : ImageView = view.findViewById(R.id.imgPosterImage)
        val progressBar : SpinKitView = view.findViewById(R.id.progressBar)
        val relativeMain : RelativeLayout = view.findViewById(R.id.relativeMain)
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
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val s = data?.get(position)?.title + " &"
        var end = " more offers"
        val ss1 = SpannableString(s)
        val ss2 = SpannableString(end)
        ss1.setSpan(RelativeSizeSpan(1.5f), 0, s!!.length, 0) // set size
        holder.txtSpaceName.text = TextUtils.concat(ss1 ,ss2)

        holder.progressBar.visibility = View.VISIBLE
        Glide.with(context).load(data?.get(position)?.image)
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
            .into(holder.imgPosterImage)

        holder.relativeMain.setOnClickListener {
            homeFragment.dealsOfTheDay(data?.get(position))
        }
    }


}