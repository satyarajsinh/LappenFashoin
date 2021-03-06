package com.lappenfashion.ui.orderList

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.simplemvvm.utils.Constants
import com.github.ybq.android.spinkit.SpinKitView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainOrderList
import com.lappenfashion.ui.products.ProductDetailsActivity
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import java.util.regex.Matcher
import java.util.regex.Pattern


class OrderDetailsProductAdapter() :
    RecyclerView.Adapter<OrderDetailsProductAdapter.ViewHolder>() {

    lateinit var context : OrderDetailsActivity
    var data:  List<ResponseMainOrderList.Payload.Data.Product?>? = null

    constructor(
        context: OrderDetailsActivity,
        data: List<ResponseMainOrderList.Payload.Data.Product?>?
    ) : this() {
        this.context = context
        this.data = data!!
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val txtProductName : TextView = view.findViewById(R.id.txtProductName)
        val txtQty : TextView = view.findViewById(R.id.txtQty)
        val txtMrp : TextView = view.findViewById(R.id.txtMrp)
        val txtSize : TextView = view.findViewById(R.id.txtSize)
        val txtSizeOther : TextView = view.findViewById(R.id.txtSizeOther)
        val txtRating : TextView = view.findViewById(R.id.txtRating)
        val cardView : CardView = view.findViewById(R.id.txtId)
        val imgProductImage : ImageView = view.findViewById(R.id.imgProductImage)
        val progressBar: SpinKitView = view.findViewById(R.id.progressBar)
        val relativeMain : RelativeLayout = view.findViewById(R.id.relativeMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.row_layout_order_product,
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
        Glide.with(context).load(data!![position]?.mainImageName).placeholder(R.mipmap.no_image)
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
            .into(holder.imgProductImage)

        try {
            if (isValidHexaCode(data!!.get(position)?.colorCode!!)) {
                if(data!!.get(position)?.colorCode!! !=null && data!!.get(position)?.colorCode!! !="") {
                    holder.cardView.visibility = View.VISIBLE
                    holder.cardView.setCardBackgroundColor(Color.parseColor(data!!.get(position)?.colorCode!!))
                }
            } else {
                holder.cardView.visibility = View.GONE
            }
        } catch (e: Exception) {
            Helper.showTost(context, "Display color issues.")
        }

        holder.txtProductName.text = data!!.get(position)?.productName.toString()
        holder.txtQty.text = "Qty - "+data!!.get(position)?.quantity.toString()
        holder.txtMrp.text = "₹"+data!!.get(position)?.salePrice.toString()

        if(data!!.get(position)?.size_view_flag == 1) {
            holder.txtSize.visibility = View.VISIBLE
            holder.txtSizeOther.visibility = View.GONE
            holder.txtSize.text = data!!.get(position)?.size.toString()
        }else{
            holder.txtSize.visibility = View.GONE
            holder.txtSizeOther.visibility = View.VISIBLE
            holder.txtSizeOther.text = data!!.get(position)?.size.toString()
        }

        holder.txtRating.setOnClickListener {
            context.displayDialog(data!!.get(position)?.product_id)
        }

        holder.relativeMain.setOnClickListener {
            Prefs.putInt(Constants.PREF_PRODUCT_ID, data!!.get(position)?.product_id!!)
            val intent = Intent(context,ProductDetailsActivity::class.java)
            context.startActivity(intent)
        }
    }

    fun isValidHexaCode(colorCode : String): Boolean {
        val regex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"
        val p: Pattern = Pattern.compile(regex)
        val m: Matcher = p.matcher(colorCode)
        return m.matches()
    }

}