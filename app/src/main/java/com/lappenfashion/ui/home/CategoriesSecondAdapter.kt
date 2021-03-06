package com.lappenfashion.ui.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.ybq.android.spinkit.SpinKitView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainHome


class CategoriesSecondAdapter(
    private val context: Context,
    private val data: List<ResponseMainHome.Payload.SubCategory?>,
    private val homeFragment: HomeFragment
) :
    RecyclerView.Adapter<CategoriesSecondAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPosterImage: ImageView = view.findViewById(R.id.imgPosterImage)
        val progressBar: SpinKitView = view.findViewById(R.id.progressBar)
        val relativeMain: RelativeLayout = view.findViewById(R.id.relativeMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.row_layout_categories_second,
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
            .into(holder.imgPosterImage)

        holder.relativeMain.setOnClickListener {
            homeFragment.subCategories(
                data?.get(position)?.productCategory!!,
                data?.get(position)?.subCategoryId!!,data?.get(position)?.category_id!!,data?.get(position)?.title!!)
        }

    }


}