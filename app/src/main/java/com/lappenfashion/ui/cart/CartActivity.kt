package com.lappenfashion.ui.cart

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemvvm.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lappenfashion.R
import com.lappenfashion.data.model.*
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.sqlitedb.DBManager
import com.lappenfashion.ui.MainActivity
import com.lappenfashion.ui.checkout.CheckoutActivity
import com.lappenfashion.ui.otp.OTPActivity
import com.lappenfashion.ui.wishlist.WishListActivity
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.imgBack
import kotlinx.android.synthetic.main.activity_wishlist.*
import kotlinx.android.synthetic.main.toolbar_with_like_cart.*
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

        /*if (Prefs.getString(Constants.PREF_IS_LOGGED_IN, "") == "1") {
            if (NetworkConnection.checkConnection(this@CartActivity)) {
                getCart()
            } else {
                Helper.showTost(this@CartActivity, getString(R.string.no_internet))
            }
        }*/
        getCart()
    }

    private fun getCart() {
        if (Prefs.getString(Constants.PREF_IS_LOGGED_IN, "") == "1") {
            if (NetworkConnection.checkConnection(this@CartActivity)) {
                Helper.showLoader(this@CartActivity)
                addCart("1")
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

                    for (i in 0 until response.body()!!.payload?.cartList?.size!!) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            var localCart = ResponseMainLocalCart(
                                0,
                                response.body()!!.payload?.cartList?.get(i)?.product?.mainImageName!!,
                                response.body()!!.payload?.cartList?.get(i)?.product?.productId!!.toString(),
                                response.body()!!.payload?.cartList?.get(i)?.quantity.toString(),
                                response.body()!!.payload?.cartList?.get(i)?.product?.productName!!,
                                response.body()!!.payload?.cartList?.get(i)?.product?.mrp!!.toString(),
                                response.body()!!.payload?.cartList?.get(i)?.product?.size!!,
                                response.body()!!.payload?.cartList?.get(i)?.product?.colorCode!!
                            )
                            dbManager.insertCart(localCart)

                        }, 2000)
                    }

                    var count = Prefs.getInt(Constants.PREF_CART_COUNT, response.body()!!.payload?.cartCount!!)
                    Prefs.putInt(Constants.PREF_CART_COUNT, count)

                    txtTotal.text = "₹" + response.body()!!.payload?.totalCartAmount.toString()
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
                if (NetworkConnection.checkConnection(this@CartActivity)) {
                    Helper.showLoader(this@CartActivity)
                    checkOutCart()
                } else {
                    Helper.showTost(this@CartActivity, getString(R.string.no_internet))
                }
            } else {
                displayLoginDialog()
            }
        }

        imgLikedCart.setOnClickListener {
            var intent = Intent(this@CartActivity, WishListActivity::class.java)
            startActivity(intent)
        }

        txtShopping.setOnClickListener {
            var intent = Intent(this@CartActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun addCart(s: String) {
        val cartItem = dbManager.fetchCart()

        val array = JsonArray()
        if (cartItem.size > 0) {
            for (i in 0 until cartItem.size) {
                var cartObject = JsonObject()
                cartObject.addProperty("product_id", cartItem.get(i).productId)
                cartObject.addProperty("quantity", cartItem.get(i).cartQty)
                cartObject.addProperty("amount", cartItem.get(i).cartAmount)
                array.add(cartObject)
                dbManager.deleteCart(cartItem.get(i).cartId.toLong())
            }

            Log.e("json Array", "json Array" + array.toString())
            var api = MyApi(this@CartActivity)
            val requestCall: Call<ResponseMainCartNew> = api.addCart(
                "Bearer " + Prefs.getString(
                    Constants.PREF_TOKEN, ""
                ), array
            )

            requestCall.enqueue(object : Callback<ResponseMainCartNew> {
                override fun onResponse(
                    call: Call<ResponseMainCartNew>,
                    response: Response<ResponseMainCartNew>
                ) {
                    Helper.dismissLoader()

                    if (response.body() != null && response.body()?.result == true) {
                        Helper.showLoader(this@CartActivity)
                        getCartItem()
                    } else {
                        Helper.showTost(
                            this@CartActivity,
                            resources.getString(R.string.some_thing_happend_wrong)
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseMainCartNew>, t: Throwable) {
                    Helper.dismissLoader()
                }

            })
        } else {
            Helper.dismissLoader()
            Helper.showLoader(this@CartActivity)
            getCartItem()
        }


    }

    private fun checkOutCart() {
        var api = MyApi(this@CartActivity)
        val requestCall: Call<ResponseMainCheckoutCart> =
            api.checkoutCart("Bearer " + Prefs.getString(Constants.PREF_TOKEN, ""))

        requestCall.enqueue(object : Callback<ResponseMainCheckoutCart> {
            override fun onResponse(
                call: Call<ResponseMainCheckoutCart>,
                response: Response<ResponseMainCheckoutCart>
            ) {

                if (response.body() != null && response.body()?.result == true) {
                    if (response.body()?.payload?.size!! > 0) {
                        displayOutOfStock(response.body()?.payload!!)
                    } else {
                        Helper.dismissLoader()
                        var intent = Intent(this@CartActivity, CheckoutActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    Helper.dismissLoader()
                    Helper.showTost(
                        this@CartActivity,
                        resources.getString(R.string.some_thing_happend_wrong)
                    )
                }
            }

            override fun onFailure(call: Call<ResponseMainCheckoutCart>, t: Throwable) {
                Helper.dismissLoader()
            }

        })
    }

    private fun displayOutOfStock(payload: List<ResponseMainCheckoutCart.Payload?>) {
        val dialog = BottomSheetDialog(this@CartActivity)
        dialog.setContentView(R.layout.bottom_sheet_out_of_stock)
        dialog.setCanceledOnTouchOutside(false)

        val txtRemove = dialog.findViewById<View>(R.id.txtRemove) as TextView?
        val txtCancel = dialog.findViewById<View>(R.id.txtCancel) as TextView?
        val recyclerOutOfStock = dialog.findViewById<View>(R.id.recyclerOutOfStock) as RecyclerView?

        recyclerOutOfStock?.layoutManager =
            LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
        var adapter = CheckoutCartAdapter(
            this@CartActivity,
            payload
        )
        recyclerOutOfStock?.adapter = adapter

        txtRemove!!.setOnClickListener {
            if (NetworkConnection.checkConnection(this@CartActivity)) {
                Helper.showLoader(this@CartActivity)
                removeOutOfStockProducts(payload)
            } else {
                Helper.showTost(this@CartActivity, getString(R.string.no_internet))
            }
        }

        txtCancel!!.setOnClickListener { dialog.dismiss() }
        dialog.show()
        Helper.dismissLoader()
    }

    private fun removeOutOfStockProducts(payload: List<ResponseMainCheckoutCart.Payload?>) {

        var list = arrayListOf<String>()

        for (i in 0 until payload.size) {
            list.add(payload[i]?.productId.toString())
        }

        val commaSeperatedString = list.joinToString(separator = ",") { it -> "\'${it}\'" }
//        var tring = commaSeperatedString.replaceAll("^\"|\"$", "");

        val result = commaSeperatedString.removeSurrounding("'", "'").split(",").map { it.toInt() }
        val result2 = result.toString().removeSurrounding("[", "]").split(",").map { it.toInt() }
        var api = MyApi(this@CartActivity)
        val requestCall: Call<ResponseMainDeleteCheckoutCart> =
            api.deleteOutOfStockCart(
                "Bearer " + Prefs.getString(Constants.PREF_TOKEN, ""),
                result2
            )

        requestCall.enqueue(object : Callback<ResponseMainDeleteCheckoutCart> {
            override fun onResponse(
                call: Call<ResponseMainDeleteCheckoutCart>,
                response: Response<ResponseMainDeleteCheckoutCart>
            ) {
                if (response.body() != null && response.body()?.result == true) {
                    val cartItem = dbManager.fetchCart()
                    if (cartItem.size > 0) {
                        for (i in 0 until cartItem.size) {
                            dbManager.deleteCart(cartItem.get(i).cartId.toLong())
                        }
                    }
                    Helper.showTost(this@CartActivity, response.body()?.message)
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                } else {
                    Helper.dismissLoader()
                    Helper.showTost(
                        this@CartActivity,
                        resources.getString(R.string.some_thing_happend_wrong)
                    )
                }
            }

            override fun onFailure(call: Call<ResponseMainDeleteCheckoutCart>, t: Throwable) {
                Helper.dismissLoader()
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

        txtLogin!!.setOnClickListener {
            if (edtMobileNumber?.text.toString() != "") {
                if (NetworkConnection.checkConnection(this@CartActivity)) {
                    txtLogin.isEnabled = false
                    Helper.showLoader(this@CartActivity)
                    loginData(edtMobileNumber?.text.toString(), txtLogin, dialog)
                } else {
                    Helper.showTost(
                        this@CartActivity,
                        getString(R.string.no_internet)
                    )
                }
            } else {
                Helper.showTost(this@CartActivity, "Field is required")
            }

        }

        imgClose!!.setOnClickListener { dialog.dismiss() }
        dialog.show()
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
                        startActivityForResult(intent,200)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 200){
            getCart()
        }
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
        if (Prefs.getInt(Constants.PREF_CART_COUNT, 0) > 0) {
            var count = Prefs.getInt(Constants.PREF_CART_COUNT, 0) - 1
            Prefs.putInt(Constants.PREF_CART_COUNT, count)
        }
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
            ), get?.cartId!!, qty
        )

        requestCall.enqueue(object : Callback<ResponseMainCartNew> {
            override fun onResponse(
                call: Call<ResponseMainCartNew>,
                response: Response<ResponseMainCartNew>
            ) {
                Helper.dismissLoader()
                if (response.body()?.result == true) {
                    dbManager.deleteCart(get?.cartId!!.toLong())
                }
                if (response.body() != null && response.body()!!.payload?.cartList?.size!! > 0) {
                    txtTotal.text = "₹" + response.body()!!.payload?.totalCartAmount.toString()
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

                val cartItem = dbManager.fetchCart()
                if (cartItem.size > 0) {
                    for (i in 0 until cartItem.size) {
                        dbManager.deleteCart(cartItem.get(i).cartId.toLong())
                    }
                }

                if (response.body() != null && response.body()!!.payload?.cartList?.size!! > 0) {
                    txtTotal.text = "₹" + response.body()!!.payload?.totalCartAmount.toString()
                    Prefs.putInt(Constants.PREF_CART_COUNT,response.body()!!.payload?.cartCount!!)
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
                    Prefs.putInt(Constants.PREF_CART_COUNT, 0)
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