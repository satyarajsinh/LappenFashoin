package com.lappenfashion.ui.category

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.ybq.android.spinkit.SpinKitView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainCategories

class CategoriesAdapter(private val context: Context,
                        private val data: List<ResponseMainCategories.Payload?>?
) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private var isClicked: Boolean = false

    class ViewHolder(view : View):RecyclerView.ViewHolder(view) {
        val imgPosterImage : ImageView = view.findViewById(R.id.imgPosterImage)
        val recyclerSubCategories : RecyclerView = view.findViewById(R.id.recyclerSubCategories)
        val progressBar : SpinKitView = view.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_layout_categories,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.progressBar.visibility = View.VISIBLE
        Glide.with(context).load(data?.get(position)?.banner)
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

        holder.imgPosterImage.setOnClickListener {
            if(!isClicked) {
                isClicked = true
                holder.recyclerSubCategories.visibility = View.VISIBLE
                holder.recyclerSubCategories.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                holder.recyclerSubCategories.setHasFixedSize(true)
                var offersAdapter = SubCategoriesCatAdapter(
                    context,
                    data?.get(position)?.subCategory
                )
                holder.recyclerSubCategories.adapter = offersAdapter
            }else{
                isClicked = false
                holder.recyclerSubCategories.visibility = View.GONE
            }
        }

    }


}