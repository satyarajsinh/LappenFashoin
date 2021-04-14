package com.lappenfashion.ui.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplemvvm.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainCartNew
import com.lappenfashion.data.model.ResponseMainLocalCart
import com.lappenfashion.data.model.ResponseMainLogin
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.sqlitedb.DBManager
import com.lappenfashion.ui.MainActivity
import com.lappenfashion.ui.checkout.CheckoutActivity
import com.lappenfashion.ui.otp.OTPActivity
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.imgBack
import kotlinx.android.synthetic.main.activity_wishlist.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {

    private var totalAmount = 0
    private lateinit var dbManager: DBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        initData()
        clickListeners()
    }

    private fun initData() {
        dbManager = DBManager(this@CartActivity)
        dbManager.open()
    }

    private fun getCart() {
        if (Prefs.getString(Constants.PREF_IS_LOGGED_IN, "") == "1") {
            if (NetworkConnection.checkConnection(this@CartActivity)) {
                Helper.showLoader(this@CartActivity)
                getCartItem()
            } else {
                Helper.showTost(this@CartActivity, resources.getString(R.string.no_internet))
            }
        } else {

            val cartItem = dbManager.fetchCart()
            if (cartItem.size > 0) {
                for (i in 0 until cartItem.size) {
                    totalAmount += (cartItem.get(i).cartQty.toInt() * cartItem.get(i).cartAmount.toInt())
                }
                txtTotal.text = "₹" + totalAmount.toString()
                relativeBottom.visibility = View.VISIBLE
                recyclerCart.visibility = View.GONE
                recyclerLocalCart.visibility = View.VISIBLE
                linearNoCart.visibility = View.GONE
                recyclerLocalCart.layoutManager =
                    LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
                var adapter = LocalCartAdapter(
                    this@CartActivity,
                    cartItem
                )
                recyclerLocalCart.adapter = adapter
            } else {
                recyclerCart.visibility = View.GONE
                recyclerLocalCart.visibility = View.GONE
                relativeBottom.visibility = View.GONE
                linearNoCart.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getCart()
    }

    private fun getCartItem() {
        var api = MyApi(this@CartActivity)
        val requestCall: Call<ResponseMainCartNew> = api.getCart(
            "Bearer " + Prefs.getString(
                Constants.PREF_TOKEN, ""
            )
        )

        requestCall.enqueue(object : Callback<ResponseMainCartNew> {
            override fun onResponse(
                call: Call<ResponseMainCartNew>,
                response: Response<ResponseMainCartNew>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()!!.payload?.cartList?.size!! > 0) {
                    txtTotal.text = "₹" +response.body()!!.payload?.totalCartAmount.toString()
                    relativeBottom.visibility = View.VISIBLE
                    recyclerCart.visibility = View.VISIBLE
                    recyclerLocalCart.visibility = View.GONE
                    linearNoCart.visibility = View.GONE
                    recyclerCart.layoutManager =
                        LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
                    var adapter = CartAdapter(
                        this@CartActivity,
                        response.body()!!.payload?.cartList as ArrayList<ResponseMainCartNew.Payload.Cart?>?
                    )
                    recyclerCart.adapter = adapter
                } else {
                    linearNoCart.visibility = View.VISIBLE
                    relativeBottom.visibility = View.GONE
                    recyclerCart.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ResponseMainCartNew>, t: Throwable) {
                Helper.dismissLoader()
            }

        })
    }

    private fun clickListeners() {
        imgBack.setOnClickListener {
            finish()
        }

        txtCheckout.setOnClickListener {
            if (Prefs.getString(Constants.PREF_IS_LOGGED_IN, "") == "1") {
                if(NetworkConnection.checkConnection(this@CartActivity)) {
                    Helper.showLoader(this@CartActivity)
                    addCart()
                }else{
                    Helper.showTost(this@CartActivity, getString(R.string.no_internet))
                }
            } else {
                displayLoginDialog()
            }
        }

        txtShopping.setOnClickListener {
            var intent = Intent(this@CartActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun addCart() {
        val cartItem = dbManager.fetchCart()

        val array = JsonArray()
        if (cartItem.size > 0) {
            for(i in 0 until cartItem.size){
                var cartObject = JsonObject()
                cartObject.addProperty("product_id",cartItem.get(i).productId)
                cartObject.addProperty("quantity",cartItem.get(i).cartQty)
                cartObject.addProperty("amount",cartItem.get(i).cartAmount)
                array.add(cartObject)
            }
        }

        Log.e("json Array","json Array"+array.toString())
        var api = MyApi(this@CartActivity)
        val requestCall: Call<ResponseMainCartNew> = api.addCart("Bearer "+ Prefs.getString(
            Constants.PREF_TOKEN, ""),array)

        requestCall.enqueue(object : Callback<ResponseMainCartNew> {
            override fun onResponse(
                call: Call<ResponseMainCartNew>,
                response: Response<ResponseMainCartNew>
            ) {
                Helper.dismissLoader()

                if (response.body() != null && response.body()?.result == true) {
                    var intent = Intent(this@CartActivity, CheckoutActivity::class.java)
                    startActivity(intent)
                } else {
                    Helper.showTost(
                        this@CartActivity,
                        resources.getString(R.string.some_thing_happend_wrong)
                    )
                }
            }

            override fun onFailure(call: Call<ResponseMainCartNew>, t: Throwable) {
                com.lappenfashion.utils.Helper.dismissLoader()
            }

        })
    }

    private fun displayLoginDialog() {
        val dialog = BottomSheetDialog(this@CartActivity)
        dialog.setContentView(R.layout.bottom_sheet_login)
        dialog.setCanceledOnTouchOutside(false)

        val imgClose = dialog.findViewById<View>(R.id.imgClose) as ImageView?
        val txtLogin = dialog.findViewById<View>(R.id.txtLogin) as TextView?
        val edtMobileNumber = dialog.findViewById<View>(R.id.edtPhoneNumber) as EditText?
        val txtResendOtp = dialog.findViewById<View>(R.id.txtResendOtp) as TextView?

        txtLogin!!.setOnClickListener {
            if (edtMobileNumber?.text.toString() != "") {
                if (NetworkConnection.checkConnection(this@CartActivity)) {
                    txtLogin.isEnabled = false
                    com.lappenfashion.utils.Helper.showLoader(this@CartActivity)
                    loginData(edtMobileNumber?.text.toString(), txtLogin,dialog)
                } else {
                    com.lappenfashion.utils.Helper.showTost(
                        this@CartActivity,
                        getString(R.string.no_internet)
                    )
                }
            } else {
                com.lappenfashion.utils.Helper.showTost(this@CartActivity, "Field is required")
            }

        }

        txtResendOtp?.setOnClickListener {
            if (NetworkConnection.checkConnection(this@CartActivity)) {
                com.lappenfashion.utils.Helper.showLoader(this@CartActivity)
                resendOTP(edtMobileNumber?.text.toString())
            } else {
                com.lappenfashion.utils.Helper.showTost(
                    this@CartActivity,
                    getString(R.string.no_internet)
                )
            }
        }

        imgClose!!.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun resendOTP(mobileNumber: String) {
        var api = MyApi(this@CartActivity)
        val requestCall: Call<ResponseMainLogin> = api.resendOTP(mobileNumber)

        requestCall.enqueue(object : Callback<ResponseMainLogin> {
            override fun onResponse(
                call: Call<ResponseMainLogin>,
                response: Response<ResponseMainLogin>
            ) {
                com.lappenfashion.utils.Helper.dismissLoader()

                if (response.body() != null) {
                    if (response.body()?.result == true) {
                        com.lappenfashion.utils.Helper.showTost(
                            this@CartActivity,
                            response.body()?.message!!
                        )
                        var intent = Intent(this@CartActivity, OTPActivity::class.java)
                        intent.putExtra("mobile_number", mobileNumber)
                        startActivity(intent)
                    }
                } else {
                    com.lappenfashion.utils.Helper.showTost(
                        this@CartActivity,
                        resources.getString(R.string.some_thing_happend_wrong)
                    )
                }
            }

            override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                com.lappenfashion.utils.Helper.dismissLoader()
            }

        })
    }

    private fun loginData(mobileNumber: String, txtLogin: TextView, dialog: BottomSheetDialog) {
        var api = MyApi(this@CartActivity)
        val requestCall: Call<ResponseMainLogin> = api.login(mobileNumber)

        requestCall.enqueue(object : Callback<ResponseMainLogin> {
            override fun onResponse(
                call: Call<ResponseMainLogin>,
                response: Response<ResponseMainLogin>
            ) {
                com.lappenfashion.utils.Helper.dismissLoader()

                if (response.body() != null) {
                    if (response.body()?.result == true) {
                        dialog.dismiss()
                        com.lappenfashion.utils.Helper.showTost(
                            this@CartActivity,
                            response.body()?.message!!
                        )
                        var intent = Intent(this@CartActivity, OTPActivity::class.java)
                        intent.putExtra("mobile_number", mobileNumber)
                        startActivity(intent)
                    }
                } else {
                    txtLogin.isEnabled = true
                    com.lappenfashion.utils.Helper.showTost(
                        this@CartActivity,
                        resources.getString(R.string.some_thing_happend_wrong)
                    )
                }
            }

            override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                txtLogin.isEnabled = true
                com.lappenfashion.utils.Helper.dismissLoader()
            }

        })
    }

    fun updateQuantity(qty1: ResponseMainLocalCart, qty: Int) {
        dbManager.updateCart(qty1.cartId, qty)
        val cartItem = dbManager.fetchCart()
        if (cartItem.size > 0) {
            totalAmount = 0
            for (i in 0 until cartItem.size) {
                totalAmount += (cartItem.get(i).cartQty.toInt() * cartItem.get(i).cartAmount.toInt())
            }
            txtTotal.text = "₹" + totalAmount.toString()
        }
    }

    fun removeProduct(responseMainLocalCart: ResponseMainLocalCart) {
        dbManager.deleteCart(responseMainLocalCart.cartId.toLong())
        val cartItem = dbManager.fetchCart()
        if (cartItem.size == 0) {
            recyclerCart.visibility = View.GONE
            recyclerLocalCart.visibility = View.GONE
            relativeBottom.visibility = View.GONE
            linearNoCart.visibility = View.VISIBLE
        } else {
            totalAmount = 0
            for (i in 0 until cartItem.size) {
                totalAmount += (cartItem.get(i).cartQty.toInt() * cartItem.get(i).cartAmount.toInt())
            }
            txtTotal.text = "₹" + totalAmount.toString()
        }
    }

    fun updateCart(get: ResponseMainCartNew.Payload.Cart?, qty: String) {
        var api = MyApi(this@CartActivity)
        val requestCall: Call<ResponseMainCartNew> = api.updateCart(
            "Bearer " + Prefs.getString(
                Constants.PREF_TOKEN, ""
            ), get?.cartId!!,qty
        )

        requestCall.enqueue(object : Callback<ResponseMainCartNew> {
            override fun onResponse(
                call: Call<ResponseMainCartNew>,
                response: Response<ResponseMainCartNew>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()!!.payload?.cartList?.size!! > 0) {
                    txtTotal.text = "₹" +response.body()!!.payload?.totalCartAmount.toString()
                    relativeBottom.visibility = View.VISIBLE
                    recyclerCart.visibility = View.VISIBLE
                    recyclerLocalCart.visibility = View.GONE
                    linearNoCart.visibility = View.GONE
                    recyclerCart.layoutManager =
                        LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
                    var adapter = CartAdapter(
                        this@CartActivity,
                        response.body()!!.payload?.cartList as ArrayList<ResponseMainCartNew.Payload.Cart?>?
                    )
                    recyclerCart.adapter = adapter
                } else {
                    linearNoCart.visibility = View.VISIBLE
                    relativeBottom.visibility = View.GONE
                    recyclerCart.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ResponseMainCartNew>, t: Throwable) {
                Helper.dismissLoader()
            }

        })
    }

    fun removeCart(cart: ResponseMainCartNew.Payload.Cart?) {
        var api = MyApi(this@CartActivity)
        val requestCall: Call<ResponseMainCartNew> = api.deleteCart(
            "Bearer " + Prefs.getString(
                Constants.PREF_TOKEN, ""
            ), cart?.cartId!!
        )

        requestCall.enqueue(object : Callback<ResponseMainCartNew> {
            override fun onResponse(
                call: Call<ResponseMainCartNew>,
                response: Response<ResponseMainCartNew>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()!!.payload?.cartList?.size!! > 0) {
                    txtTotal.text = "₹" +response.body()!!.payload?.totalCartAmount.toString()
                    relativeBottom.visibility = View.VISIBLE
                    recyclerCart.visibility = View.VISIBLE
                    recyclerLocalCart.visibility = View.GONE
                    linearNoCart.visibility = View.GONE
                    recyclerCart.layoutManager =
                        LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
                    var adapter = CartAdapter(
                        this@CartActivity,
                        response.body()!!.payload?.cartList as ArrayList<ResponseMainCartNew.Payload.Cart?>?
                    )
                    recyclerCart.adapter = adapter
                } else {
                    linearNoCart.visibility = View.VISIBLE
                    relativeBottom.visibility = View.GONE
                    recyclerCart.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ResponseMainCartNew>, t: Throwable) {
                Helper.dismissLoader()
            }

        })
    }
}