package com.lappenfashion.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplemvvm.utils.Constants
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainCart
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.ui.checkout.CheckoutActivity
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.imgBack
import kotlinx.android.synthetic.main.activity_wishlist.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        initData()
        clickListeners()
    }

    private fun initData() {
        if (NetworkConnection.checkConnection(this@CartActivity)) {
            Helper.showLoader(this@CartActivity)
            getCartItem()
        }else{
            Helper.showTost(this@CartActivity,resources.getString(R.string.no_internet))
        }
    }

    private fun getCartItem() {
        var api = MyApi(this@CartActivity)
        val requestCall: Call<ResponseMainCart> = api.getCart("Bearer "+ Prefs.getString(
            Constants.PREF_TOKEN, ""))

        requestCall.enqueue(object : Callback<ResponseMainCart> {
            override fun onResponse(
                call: Call<ResponseMainCart>,
                response: Response<ResponseMainCart>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()!!.payload?.size!! >0) {
                    recyclerCart.layoutManager =
                        LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
                    var adapter = CartAdapter(
                        this@CartActivity,
                        response.body()!!.payload as ArrayList<ResponseMainCart.Payload?>?
                    )
                    recyclerCart.adapter = adapter
                }else{
                    relativeNoData.visibility = View.VISIBLE
                    recyclerWishList.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ResponseMainCart>, t: Throwable) {
                Helper.dismissLoader()
            }

        })
    }

    private fun clickListeners() {
        imgBack.setOnClickListener {
            finish()
        }

        txtCheckout.setOnClickListener {
            var intent = Intent(this@CartActivity,CheckoutActivity::class.java)
            startActivity(intent)
        }
    }
}