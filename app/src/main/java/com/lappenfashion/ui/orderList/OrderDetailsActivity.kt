package com.lappenfashion.ui.orderList

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplemvvm.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainOrderCanceled
import com.lappenfashion.data.model.ResponseMainOrderList
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.toolbar_with_drawer.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class OrderDetailsActivity : AppCompatActivity() {

    private var orderCancel: Int = 0
    private var cancelOrderStatus: String = ""
    private var orderDetails: ResponseMainOrderList.Payload.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        initData()
        clickListener()
    }

    private fun clickListener() {
        imgBack.setOnClickListener {
            finish()
        }

        txtCancelOrder.setOnClickListener {
            displaySortBottomSheet()
        }
    }

    private fun displaySortBottomSheet() {
        val dialog = BottomSheetDialog(this@OrderDetailsActivity)
        dialog.setContentView(R.layout.bottom_sheet_order_status)

        var flag = 0

        var txtStatus1 = dialog.findViewById<TextView>(R.id.txtStatus1)
        var txtStatus2 = dialog.findViewById<TextView>(R.id.txtStatus2)
        var txtStatus3 = dialog.findViewById<TextView>(R.id.txtStatus3)
        var txtStatus4 = dialog.findViewById<TextView>(R.id.txtStatus4)
        var txtStatus5 = dialog.findViewById<TextView>(R.id.txtStatus5)
        var edtDescription = dialog.findViewById<EditText>(R.id.edtDescription)
        var txtApply = dialog.findViewById<TextView>(R.id.txtApply)

        txtApply?.setOnClickListener {
            if(cancelOrderStatus != "" && edtDescription?.text.toString()!="") {
                dialog.dismiss()
                if (NetworkConnection.checkConnection(this@OrderDetailsActivity)) {
                    Helper.showLoader(this@OrderDetailsActivity)
                    if(txtCancelOrder.text.toString() == "Cancel Order") {
                        cancelOrder(edtDescription?.text.toString(),"cancelled")
                    }else{
                        cancelOrder(edtDescription?.text.toString(),"returned")
                    }
                } else {
                    Helper.showTost(this@OrderDetailsActivity, "No internet connection")
                }
            }else{
                if(cancelOrderStatus == "") {
                    Helper.showTost(this@OrderDetailsActivity, "Please select your option")
                }else{
                    Helper.showTost(this@OrderDetailsActivity, "Please enter description")
                }
            }
        }

        txtStatus1?.setOnClickListener {
            cancelOrderStatus = txtStatus1.text.toString()
            txtStatus1?.setBackgroundColor(resources.getColor(R.color.background))
            txtStatus2?.setBackgroundColor(resources.getColor(R.color.white))
            txtStatus3?.setBackgroundColor(resources.getColor(R.color.white))
            txtStatus4?.setBackgroundColor(resources.getColor(R.color.white))
            txtStatus5?.setBackgroundColor(resources.getColor(R.color.white))
        }

        txtStatus2?.setOnClickListener {
            cancelOrderStatus = txtStatus2.text.toString()
            txtStatus1?.setBackgroundColor(resources.getColor(R.color.white))
            txtStatus2?.setBackgroundColor(resources.getColor(R.color.background))
            txtStatus3?.setBackgroundColor(resources.getColor(R.color.white))
            txtStatus4?.setBackgroundColor(resources.getColor(R.color.white))
            txtStatus5?.setBackgroundColor(resources.getColor(R.color.white))
        }

        txtStatus3?.setOnClickListener {
            cancelOrderStatus = txtStatus3.text.toString()
            txtStatus1?.setBackgroundColor(resources.getColor(R.color.white))
            txtStatus2?.setBackgroundColor(resources.getColor(R.color.white))
            txtStatus3?.setBackgroundColor(resources.getColor(R.color.background))
            txtStatus4?.setBackgroundColor(resources.getColor(R.color.white))
            txtStatus5?.setBackgroundColor(resources.getColor(R.color.white))
        }

        txtStatus4?.setOnClickListener {
            cancelOrderStatus = txtStatus4.text.toString()
            txtStatus1?.setBackgroundColor(resources.getColor(R.color.white))
            txtStatus2?.setBackgroundColor(resources.getColor(R.color.white))
            txtStatus3?.setBackgroundColor(resources.getColor(R.color.white))
            txtStatus4?.setBackgroundColor(resources.getColor(R.color.background))
            txtStatus5?.setBackgroundColor(resources.getColor(R.color.white))
        }

        txtStatus5?.setOnClickListener {
            cancelOrderStatus = txtStatus5.text.toString()
            txtStatus1?.setBackgroundColor(resources.getColor(R.color.white))
            txtStatus2?.setBackgroundColor(resources.getColor(R.color.white))
            txtStatus3?.setBackgroundColor(resources.getColor(R.color.white))
            txtStatus4?.setBackgroundColor(resources.getColor(R.color.white))
            txtStatus5?.setBackgroundColor(resources.getColor(R.color.background))
        }

        dialog.show()
        Helper.dismissLoader()
    }

    private fun cancelOrder(description: String,orderStatus: String) {
        var api = MyApi(this@OrderDetailsActivity)
        val requestCall: Call<ResponseMainOrderCanceled> = api.cancelOrder("Bearer " +Prefs.getString(Constants.PREF_TOKEN,""),orderStatus,orderDetails?.orderId.toString(),description,cancelOrderStatus)

        requestCall.enqueue(object : Callback<ResponseMainOrderCanceled> {
            override fun onResponse(
                call: Call<ResponseMainOrderCanceled>,
                response: Response<ResponseMainOrderCanceled>
            ) {
                Helper.dismissLoader()
                if (response.body() != null) {
                    Helper.showTost(this@OrderDetailsActivity,response.body()!!.message)
                    finish()
                }
            }

            override fun onFailure(call: Call<ResponseMainOrderCanceled>, t: Throwable) {
                Helper.dismissLoader()
                Helper.showTost(this@OrderDetailsActivity,t.message)
            }

        })
    }


    private fun initData() {
        if(intent!=null){
            orderDetails = intent.getSerializableExtra("orderDetails") as ResponseMainOrderList.Payload.Data?


            recyclerProducts.layoutManager = LinearLayoutManager(
                this@OrderDetailsActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            recyclerProducts.setHasFixedSize(true)
            var orderListAdapter = OrderDetailsProductAdapter(
                this@OrderDetailsActivity,
                orderDetails!!.products
            )
            recyclerProducts.adapter = orderListAdapter

            if(orderDetails?.orderStatusDetail!!.size>0) {
                recyclerOrderStatus.layoutManager = LinearLayoutManager(
                    this@OrderDetailsActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                recyclerOrderStatus.setHasFixedSize(true)
                var orderStatusAdapter = OrderStatusListAdapter(
                    this@OrderDetailsActivity,
                    orderDetails!!.orderStatusDetail!!
                )
                recyclerOrderStatus.adapter = orderStatusAdapter
            }

            if(orderDetails?.orderStatusDetail!!.size > 0){
                for(i in 0 until orderDetails?.orderStatusDetail!!.size){
                    if(orderDetails?.orderStatusDetail!![i]?.status == "delivered"){
                        orderCancel = 1
                    }
                }
            }

            if(orderCancel == 1){
                txtCancelOrder.text = "Return Order"
            }

            if(orderDetails?.is_return == 1){
                txtCancelOrder.visibility = View.VISIBLE
            }else{
                txtCancelOrder.visibility = View.GONE
            }

            txtTitle.text = "Order Number - "+orderDetails!!.orderId
            txtOrderStatus.text = orderDetails!!.status
            txtTotaAmount.text = "₹"+orderDetails!!.grandTotal.toString()
            txtListPrice.text = "₹"+orderDetails!!.list_price.toString()
            txtSellingPrice.text = "₹"+orderDetails!!.selling_price.toString()
            txtExtraDiscount.text = "₹"+(orderDetails!!.list_price!! - orderDetails!!.selling_price!!)
            txtCouponDiscount.text = "₹"+orderDetails!!.extra_discount
            txtShippingCharge.text = "₹"+orderDetails!!.shipping_fee
        }
    }

    fun displayDialog(orderProductId: Int?) {
        var dialog = Dialog(this@OrderDetailsActivity); // Context, this, etc.
        dialog.setContentView(R.layout.rating_dialog);

        var ratingBar = dialog.findViewById<RatingBar>(R.id.ratingBar)
        var edtDescription = dialog.findViewById<EditText>(R.id.edtDescription)
        var txtCancel = dialog.findViewById<TextView>(R.id.txtCancel)
        var txtSubmit = dialog.findViewById<TextView>(R.id.txtSubmit)

        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        lp.width = ((getResources().getDisplayMetrics().widthPixels * 0.90).roundToInt())
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.getWindow()?.setAttributes(lp)
        dialog.getWindow()?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.getWindow()?.setDimAmount(0.5f)

        txtCancel.setOnClickListener {
            dialog.dismiss()
        }

        txtSubmit.setOnClickListener {
            var rating = ratingBar.rating.toString()
            if (NetworkConnection.checkConnection(this@OrderDetailsActivity)) {
                Helper.showLoader(this@OrderDetailsActivity)
                addRating(orderProductId,rating,edtDescription.text.toString())
            } else {
                Helper.showTost(this@OrderDetailsActivity, "No internet connection")
            }
            dialog.dismiss()
        }

        dialog.show();
    }

    private fun addRating(orderProductId: Int?, rating: String, desc: String) {
        var api = MyApi(this@OrderDetailsActivity)
        val requestCall: Call<ResponseMainOrderCanceled> = api.applyRating("Bearer " +Prefs.getString(Constants.PREF_TOKEN,""),orderProductId.toString(),rating,desc)

        requestCall.enqueue(object : Callback<ResponseMainOrderCanceled> {
            override fun onResponse(
                call: Call<ResponseMainOrderCanceled>,
                response: Response<ResponseMainOrderCanceled>
            ) {
                Helper.dismissLoader()
                if (response.body() != null) {
                    Helper.showTost(this@OrderDetailsActivity,response.body()!!.message)
                    finish()
                }
            }

            override fun onFailure(call: Call<ResponseMainOrderCanceled>, t: Throwable) {
                Helper.dismissLoader()
                Helper.showTost(this@OrderDetailsActivity,t.message)
            }

        })
    }
}