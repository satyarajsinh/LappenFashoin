package com.lappenfashion.ui.products

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemvvm.utils.Constants
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainProductDetails
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs


class ProductReviewAdapter() :
    RecyclerView.Adapter<ProductReviewAdapter.ViewHolder>() {

    lateinit var context: ProductDetailsActivity
    lateinit var data: List<ResponseMainProductDetails.Payload.Review?>
    var productId: Int = 0

    constructor(
        context: ProductDetailsActivity,
        data: List<ResponseMainProductDetails.Payload.Review?>?, productId: Int
    ) : this() {
        this.context = context
        this.data = data!!
        this.productId = productId
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardView)
        val txtNameLetter: TextView = view.findViewById(R.id.txtNameLetter)
        val txtName: TextView = view.findViewById(R.id.txtName)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
        val txtDescription: TextView = view.findViewById(R.id.txtDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.row_layout_product_rating,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(data?.get(position)?.user != null && data?.get(position)?.user!!.name!=null && data?.get(position)?.user!!.name!=""){
            holder.txtNameLetter.text = data?.get(position)?.user!!.name!![0].toString()
        }

        if(data?.get(position)?.user != null && data?.get(position)?.user!!.name!=null && data?.get(position)?.user!!.name!=""){
            holder.txtName.text = data?.get(position)?.user!!.name!!
        }

        if(data?.get(position)?.review!=null && data?.get(position)?.review!=""){
            holder.txtDescription.text = data?.get(position)?.review
        }

        holder.ratingBar.rating = data?.get(position)?.ratting?.toFloat()!!

    }


}