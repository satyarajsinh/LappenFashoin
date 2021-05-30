package com.lappenfashion.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplemvvm.utils.Constants
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainAddress
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.ui.address.AddAddressActivity
import com.lappenfashion.ui.placeOrder.PlaceOrderActivity
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_address_listing.*
import kotlinx.android.synthetic.main.activity_checkout_delivery_address.*
import kotlinx.android.synthetic.main.activity_checkout_delivery_address.imgBack
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.toolbar_with_back_button.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CheckoutDeliveryAddressActivity : AppCompatActivity(){

    private var addressId: Int = 0
    private var deliveryOptionId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_delivery_address)

        initData()
        clickListener()

    }

    private fun clickListener() {
        imgBack.setOnClickListener {
            finish()
        }

        txtBack.setOnClickListener {
            finish()
        }

        txtNext.setOnClickListener {
            if(addressId!=0 && deliveryOptionId!=0){
                var intent = Intent(this@CheckoutDeliveryAddressActivity, PlaceOrderActivity::class.java)
                intent.putExtra("deliveryOptionId",deliveryOptionId)
                intent.putExtra("addressId",addressId)
                startActivityForResult(intent,100)
            }else{
                Helper.showTost(this@CheckoutDeliveryAddressActivity,"Please select your Delivery Address!")
            }
        }

        txtAddAddressCheckout.setOnClickListener {
            var intent = Intent(this@CheckoutDeliveryAddressActivity, AddAddressActivity::class.java)
            startActivityForResult(intent,100)
        }

        txtAddAddressCheckoutText.setOnClickListener {
            var intent = Intent(this@CheckoutDeliveryAddressActivity, AddAddressActivity::class.java)
            startActivityForResult(intent,100)
        }


    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100){
            initData()
        }
    }

    private fun initData() {

        if(intent!=null){
            deliveryOptionId = intent.getIntExtra("deliveryOptionId",0)
        }

        if (NetworkConnection.checkConnection(this)) {
            Helper.showLoader(this@CheckoutDeliveryAddressActivity)!!
            getAddress()
        } else {
            Helper.showTost(this, "No internet connection")
        }
    }

    private fun getAddress() {
        var api = MyApi(this)
        val requestCall: Call<ResponseMainAddress> = api.getAddress(
            "Bearer " + Prefs.getString(
                Constants.PREF_TOKEN, ""
            )
        )

        requestCall.enqueue(object : Callback<ResponseMainAddress> {
            override fun onResponse(
                call: Call<ResponseMainAddress>,
                response: Response<ResponseMainAddress>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()!!.result == true && response.body()!!.payload?.size!! > 0) {

                    linearAddressCheckout.visibility = View.GONE
                    recyclerDeliveryAddress.visibility = View.VISIBLE
                    txtAddAddressCheckoutText.visibility = View.VISIBLE
                    recyclerDeliveryAddress.layoutManager =
                        LinearLayoutManager(
                            this@CheckoutDeliveryAddressActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                    var adapter = CheckoutAddressAdapter(
                        this@CheckoutDeliveryAddressActivity,
                        response.body()!!.payload as ArrayList<ResponseMainAddress.Payload?>?
                    )
                    recyclerDeliveryAddress.adapter = adapter

                } else {
                    txtAddAddressCheckoutText.visibility = View.GONE
                    linearAddressCheckout.visibility = View.VISIBLE
                    recyclerDeliveryAddress.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ResponseMainAddress>, t: Throwable) {
                Helper.dismissLoader()
                Helper.showTost(this@CheckoutDeliveryAddressActivity,t.message)
            }

        })
    }

    fun setDeliveryAddressId(addressId: Int?) {
        this.addressId = addressId!!
    }
}