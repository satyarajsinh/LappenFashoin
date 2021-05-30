package com.lappenfashion.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.simplemvvm.utils.Constants
import com.github.ybq.android.spinkit.SpinKitView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainHome
import com.lappenfashion.ui.MainActivity
import com.lappenfashion.ui.products.ProductDetailsActivity
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_product_details.*


class TrendingAdapter(
    private val context: Context,
    private val data: List<ResponseMainHome.Payload.Trending?>?,
    private val activity: MainActivity?
) :
    RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val imgTrendingPoster : ImageView = view.findViewById(R.id.imgTrendingPoster)
        val txtDiscountPrice : TextView = view.findViewById(R.id.txtDiscountPrice)
        val txtOrignalPrice : TextView = view.findViewById(R.id.txtOrignalPrice)
        val progressBar : SpinKitView = view.findViewById(R.id.progressBar)
        val linearMain : LinearLayout = view.findViewById(R.id.linearMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.row_layout_trending,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.progressBar.visibility = View.VISIBLE
        Glide.with(context).load(data?.get(position)?.image).placeholder(R.mipmap.no_image)
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
            .into(holder.imgTrendingPoster)

        holder.txtDiscountPrice.text = "₹"+data?.get(position)?.salePrice
        holder.txtOrignalPrice.text = "₹"+data?.get(position)?.price
        holder.txtOrignalPrice.setPaintFlags(holder.txtOrignalPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)

        holder.linearMain.setOnClickListener {
            Prefs.putInt(Constants.PREF_PRODUCT_ID,data?.get(position)?.id!!)
            val intent = Intent(context,ProductDetailsActivity::class.java)
            context.startActivity(intent)
        }
    }


}