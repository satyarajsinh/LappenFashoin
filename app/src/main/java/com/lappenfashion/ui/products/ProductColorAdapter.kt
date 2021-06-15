package com.lappenfashion.ui.products

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemvvm.utils.Constants
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainProductDetails
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import java.util.regex.Matcher
import java.util.regex.Pattern


class ProductColorAdapter() :
    RecyclerView.Adapter<ProductColorAdapter.ViewHolder>() {

    lateinit var context: ProductDetailsActivity
    lateinit var data: List<ResponseMainProductDetails.Payload.Color?>
    var colorCode: String = ""

    constructor(
        context: ProductDetailsActivity,
        data: List<ResponseMainProductDetails.Payload.Color?>?, colorcode: String
    ) : this() {
        this.context = context
        this.data = data!!
        this.colorCode = colorcode
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val linearMain: RelativeLayout = view.findViewById(R.id.linearMain)
        val cardView: CardView = view.findViewById(R.id.cardView)
        val imgCheck: ImageView = view.findViewById(R.id.imgCheck)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.row_layout_product_color,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(Prefs.getInt(Constants.PREF_SELECTED_COLOR,0) == data.get(position)?.productId) {
            /*if (data.get(position)?.flag == 1) {
                holder.imgCheck.visibility = View.VISIBLE
            } else {
                data.get(position)?.flag = 0
                holder.imgCheck.visibility = View.GONE
            }*/
            holder.imgCheck.visibility = View.VISIBLE
        }else{
            if(data.get(position)?.colorCode == colorCode){
                holder.imgCheck.visibility = View.VISIBLE
            }else{
                holder.imgCheck.visibility = View.GONE
            }
        }

        try{
            if(isValidHexaCode(data.get(position)?.colorCode!!)) {
                if (data.get(position)?.colorCode != null && data.get(position)?.colorCode != "") {
                    holder.cardView.visibility = View.VISIBLE
                    holder.cardView.setCardBackgroundColor(Color.parseColor(data.get(position)?.colorCode))
                }
            }else{
                holder.cardView.visibility = View.GONE
            }
        }catch (e : Exception){
            Helper.showTost(context,"Display color issues.")
        }


        holder.cardView.setOnClickListener {
            data.get(position)?.flag = 1
            Prefs.putInt(Constants.PREF_SELECTED_COLOR, data.get(position)?.productId!!)
            if (NetworkConnection.checkConnection(context)) {
                Helper.showLoader(context)
                context.getProductDetailsById(data.get(position)?.productId!!)
            } else {
                Helper.showTost(context, context.resources.getString(R.string.no_internet))
            }

        }
    }

    fun isValidHexaCode(colorCode : String): Boolean {
        val regex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"
        val p: Pattern = Pattern.compile(regex)
        val m: Matcher = p.matcher(colorCode)
        return m.matches()
    }


}