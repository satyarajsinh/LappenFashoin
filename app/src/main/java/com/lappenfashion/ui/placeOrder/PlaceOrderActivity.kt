package com.lappenfashion.ui.placeOrder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplemvvm.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainApplyCoupon
import com.lappenfashion.data.model.ResponseMainLogin
import com.lappenfashion.data.model.ResponseMainPlaceOrderView
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_checkout_delivery_address.*
import kotlinx.android.synthetic.main.activity_place_order.*
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PlaceOrderActivity : AppCompatActivity(), PaymentResultListener {

    private var totalAmountAfterCoupon: Double = 0.0
    private var couponDiscount: Int = 0
    private lateinit var placeOrderData: ResponseMainPlaceOrderView.Payload
    private var addressId: Int = 0
    private var deliveryOptionId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)

        Checkout.preload(applicationContext)

        initData()
        clickListener()
    }

    private fun clickListener() {
        imgBack.setOnClickListener {
            finish()
        }

        relativeBottom.setOnClickListener {
            displaySortBottomSheet()
        }

        txtApply.setOnClickListener {
            if(txtApply.text.toString() == "Apply") {
                if (edtCoupon.text.toString() != "") {
                    if (NetworkConnection.checkConnection(this@PlaceOrderActivity)) {
                        Helper.showLoader(this@PlaceOrderActivity)
                        applyCouponCode(edtCoupon.text.toString())
                    } else {
                        Helper.showTost(this@PlaceOrderActivity, "No internet connection")
                    }
                } else {
                    Helper.showTost(this@PlaceOrderActivity, "Please enter your coupon code")
                }
            }else{
                txtCoupon.visibility = View.GONE
                txtCouponDiscount.visibility = View.GONE
                var discount = (placeOrderData.selling_price!! * couponDiscount)/100
                txtCouponDiscount.text = "(-) ₹" + discount.toString()
                txtTotaAmount.text = "₹" +placeOrderData.total_cart_amount.toString()
                txtAmount.text = "₹" +placeOrderData.total_cart_amount.toString()
                txtError.visibility = View.GONE
                edtCoupon.setText("")
                txtApply.setText("Apply")
            }
        }
    }

    private fun applyCouponCode(coupon: String) {
        var api = MyApi(this@PlaceOrderActivity)
        val requestCall: Call<ResponseMainApplyCoupon> = api.applyCouponCode(
            "Bearer " + Prefs.getString(
                Constants.PREF_TOKEN,
                ""
            ),coupon
        )

        requestCall.enqueue(object : Callback<ResponseMainApplyCoupon> {
            override fun onResponse(
                call: Call<ResponseMainApplyCoupon>,
                response: Response<ResponseMainApplyCoupon>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()!!.result == true && response.body()!!.payload!=null) {
                    txtCoupon.visibility = View.VISIBLE
                    txtCouponDiscount.visibility = View.VISIBLE
                    couponDiscount = response.body()!!.payload?.discount!!
                    var discount = (placeOrderData.selling_price!! * response.body()!!.payload?.discount!!)/100
                    txtCouponDiscount.text = "(-) ₹" + discount.toString()
                    totalAmountAfterCoupon = placeOrderData.selling_price!! - response.body()!!.payload?.discount!!
                    txtTotaAmount.text = "₹"+(placeOrderData.selling_price!! - response.body()!!.payload?.discount!!).toString()
                    txtAmount.text = "₹"+(placeOrderData.selling_price!! - response.body()!!.payload?.discount!!).toString()
                    txtApply.text = "Remove"
                    txtError.visibility = View.VISIBLE
                    txtError.text = "Coupon applied successfully"
                } else {
                    txtError.visibility = View.VISIBLE
                    txtError.text = "Coupon not available"
                }
            }

            override fun onFailure(call: Call<ResponseMainApplyCoupon>, t: Throwable) {
                Helper.dismissLoader()
                Helper.showTost(this@PlaceOrderActivity, t.message)
            }

        })
    }

    private fun displaySortBottomSheet() {
        val dialog = BottomSheetDialog(this@PlaceOrderActivity)
        dialog.setContentView(R.layout.bottom_sheet_payment_option)

        var flag = 0

        var txtOnline = dialog.findViewById<TextView>(R.id.txtOnline)
        var txtCash = dialog.findViewById<TextView>(R.id.txtCash)
        var txtApply = dialog.findViewById<TextView>(R.id.txtApply)

        txtOnline?.setOnClickListener {
            flag = 1
            txtOnline?.setBackgroundColor(resources.getColor(R.color.background))
            txtCash?.setBackgroundColor(resources.getColor(R.color.white))
        }

        txtCash?.setOnClickListener {
            flag = 2
            txtOnline?.setBackgroundColor(resources.getColor(R.color.white))
            txtCash?.setBackgroundColor(resources.getColor(R.color.background))
        }

        txtApply?.setOnClickListener {
            if(flag == 1){
                Helper.showLoader(this@PlaceOrderActivity)
                Handler(Looper.getMainLooper()).postDelayed({
                    startPayment()
                    Helper.dismissLoader()
                }, 2000)
            }else{
                if (NetworkConnection.checkConnection(this@PlaceOrderActivity)) {
                    Helper.showLoader(this@PlaceOrderActivity)
                    placeOrder("",2)
                } else {
                    Helper.showTost(this@PlaceOrderActivity, "No internet connection")
                }

            }
        }

        dialog.show()
        Helper.dismissLoader()
    }

    private fun startPayment() {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()
        var total = 0
        if(couponDiscount > 0){
            total = totalAmountAfterCoupon.toInt() * 100
        }else{
            total = (placeOrderData.total_cart_amount!! * 100).toInt()
        }
        try {
            val options = JSONObject()
            options.put("name","Lappen Fashion")
            options.put("description","Start your payment")
            //You can omit the image option to fetch the image from dashboard
            options.put("currency","INR");
            options.put("send_sms_hash",false)
            options.put("amount",total.toString())//pass amount in currency subunits

            val prefill = JSONObject()
            prefill.put("email","lappenfashion@gmail.com")
            prefill.put("contact","999999")

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        try {
            Log.e("payment success", "payment success" + p0)
            if (NetworkConnection.checkConnection(this@PlaceOrderActivity)) {
                Helper.showLoader(this@PlaceOrderActivity)
                placeOrder(p0, 1)
            } else {
                Helper.showTost(this@PlaceOrderActivity, "No internet connection")
            }
        }catch (e: Exception){
            Log.e("onPaymentSuccess","Exception"+e.message)
        }
    }

    private fun placeOrder(paymentId: String?, flag: Int) {
        val array = JsonArray()
        for(i in 0 until placeOrderData.cartList?.size!!) {
            var cartObject = JsonObject()
            cartObject.addProperty(
                "product_id",
                placeOrderData.cartList?.get(i)?.product?.productId.toString()
            )
            cartObject.addProperty("quantity", placeOrderData.cartList?.get(i)?.quantity.toString())
            cartObject.addProperty("mrp", placeOrderData.cartList?.get(i)?.product?.mrp.toString())
            cartObject.addProperty(
                "sale_price",
                placeOrderData.cartList?.get(i)?.product?.salePrice.toString()
            )
            array.add(cartObject)
        }

        var paymentMethod : String = ""
        if(flag ==1){
            paymentMethod = "online_payment"
        }else{
            paymentMethod = "cash_on_delivery"
        }

        var api = MyApi(this@PlaceOrderActivity)
        val requestCall: Call<ResponseMainLogin> = api.placeOrder(
            "Bearer " + Prefs.getString(
                Constants.PREF_TOKEN,
                ""
            ),
            addressId.toString(),
            deliveryOptionId.toString(),
            "",
            if(couponDiscount>0) totalAmountAfterCoupon.toString() else placeOrderData.total_cart_amount.toString(),
            paymentId,
            paymentMethod,
            array.toString()
        )

        requestCall.enqueue(object : Callback<ResponseMainLogin> {
            override fun onResponse(
                call: Call<ResponseMainLogin>,
                response: Response<ResponseMainLogin>
            ) {
                if (response.body() != null && response.body()!!.result == true) {
                    Helper.showTost(this@PlaceOrderActivity, response.body()!!.message)
                    var intent = Intent(this@PlaceOrderActivity, OrderPlacedActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Helper.showTost(this@PlaceOrderActivity, "No Data Found")
                }
                Helper.dismissLoader()
            }

            override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                Helper.dismissLoader()
            }

        })
    }

    override fun onBackPressed() {

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        try{
            Log.e("onPaymentError", "onPaymentError" + p0)
        }catch (e : Exception){

        }

    }

    private fun initData() {
        txtTitle.text = "Place Order"
        if(intent!=null){
            deliveryOptionId = intent.getIntExtra("deliveryOptionId", 0)
            addressId = intent.getIntExtra("addressId", 0)
        }

        if (NetworkConnection.checkConnection(this@PlaceOrderActivity)) {
            Helper.showLoader(this@PlaceOrderActivity)
            getPlaceOrderDetails()
        } else {
            Helper.showTost(this@PlaceOrderActivity, "No internet connection")
        }
    }

    private fun getPlaceOrderDetails() {
        Log.e("Address Id","Address Id : "+addressId.toString())
        Log.e("delivery option Id","delivery option Id : "+deliveryOptionId.toString())

        var api = MyApi(this@PlaceOrderActivity)
        val requestCall: Call<ResponseMainPlaceOrderView> = api.placeOrderView(
            "Bearer " + Prefs.getString(
                Constants.PREF_TOKEN,
                ""
            ),
            Constants.END_POINT_PLACE_ORDER_VIEW + "?address_id=" + addressId.toString() + "&delivery_option_id=" + deliveryOptionId.toString()
        )

        requestCall.enqueue(object : Callback<ResponseMainPlaceOrderView> {
            override fun onResponse(
                call: Call<ResponseMainPlaceOrderView>,
                response: Response<ResponseMainPlaceOrderView>
            ) {
                if (response.body() != null && response.body()!!.result == true) {

                    if (response.body()!!.payload?.cartList?.size!! > 0) {
                        placeOrderData = response.body()!!.payload!!
                        recyclerProducts.layoutManager = LinearLayoutManager(
                            this@PlaceOrderActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        recyclerProducts.setHasFixedSize(true)
                        var PlaceOrderProductAdapter = PlaceOrderProductAdapter(
                            this@PlaceOrderActivity,
                            response.body()!!.payload?.cartList
                        )
                        recyclerProducts.adapter = PlaceOrderProductAdapter
                    }

                    if (response.body()!!.payload?.address?.name != null) {
                        txtName.text = response.body()!!.payload?.address?.name
                    }

                    if (response.body()!!.payload?.address?.type != null) {
                        txtType.text = response.body()!!.payload?.address?.type
                    }

                    if (response.body()!!.payload?.address?.address != null) {
                        txtAddress.text = response.body()!!.payload?.address?.address
                    }

                    if (response.body()!!.payload?.address?.localityTown != null) {
                        txtLocality.text = response.body()!!.payload?.address?.localityTown
                    }

                    if (response.body()!!.payload?.address?.city != null) {
                        txtCity.text = response.body()!!.payload?.address?.city + " - "
                    }

                    if (response.body()!!.payload?.address?.pincode != null) {
                        txtPincode.text = response.body()!!.payload?.address?.pincode
                    }

                    if (response.body()!!.payload?.address?.state != null) {
                        txtState.text = response.body()!!.payload?.address?.state + ", "
                    }

                    if (response.body()!!.payload?.address?.mobileNumber != null) {
                        txtNumber.text =
                            "Mobile - " + response.body()!!.payload?.address?.mobileNumber
                    }

                    if (response.body()!!.payload?.deliveryOption?.title != null) {
                        txtTitleDelivery.text = response.body()!!.payload?.deliveryOption?.title
                    }

                    if (response.body()!!.payload?.deliveryOption?.description != null) {
                        txtDescription.text = response.body()!!.payload?.deliveryOption?.description
                    }

                    txtListPrice.text = "₹" + response.body()!!.payload?.list_price.toString()
                    txtSellingPrice.text = "₹" + response.body()!!.payload?.selling_price.toString()
                    txtShippingCharge.text = "(+) ₹" + response.body()!!.payload?.shipping_fee.toString()
                    txtExtraDiscount.text = "(-) ₹" + (response.body()!!.payload?.list_price!! - response.body()!!.payload?.selling_price!!)
                    txtTotaAmount.text = "₹" + response.body()!!.payload?.total_cart_amount.toString()
                    txtAmount.text = "₹" + response.body()!!.payload?.total_cart_amount.toString()

                } else {
                    Helper.showTost(this@PlaceOrderActivity, "No Data Found")
                }
                Helper.dismissLoader()
            }

            override fun onFailure(call: Call<ResponseMainPlaceOrderView>, t: Throwable) {
                Helper.dismissLoader()
            }

        })
    }
}