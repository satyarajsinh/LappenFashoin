package com.lappenfashion.ui.address

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplemvvm.utils.Constants
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainAddress
import com.lappenfashion.data.model.ResponseMainLogin
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.activity_address_listing.*
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.toolbar_with_back_button.*
import kotlinx.android.synthetic.main.toolbar_with_back_button.txtTitle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressListingActivity : AppCompatActivity() {

    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_listing)

        initData()
    }

    private fun initData() {
        imageView = findViewById(R.id.imgBack)

        txtTitle.text = "Shipping Address"

        if (NetworkConnection.checkConnection(this)) {
            Helper.showLoader(this@AddressListingActivity)!!
            getAddress()
        } else {
            Helper.showTost(this, "No internet connection")
        }

        txtAddAddress.setOnClickListener {
            var intent = Intent(this@AddressListingActivity, AddAddressActivity::class.java)
            startActivityForResult(intent,100)
        }

        txtAddress.setOnClickListener {
            var intent = Intent(this@AddressListingActivity, AddAddressActivity::class.java)
            startActivityForResult(intent,100)
        }

        imageView.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100){
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
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
                    linearAddress.visibility = View.GONE
                    recyclerAddress.visibility = View.VISIBLE
                    txtAddress.visibility = View.VISIBLE
                    recyclerAddress.layoutManager =
                        LinearLayoutManager(
                            this@AddressListingActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                    var adapter = AddressAdapter(
                        this@AddressListingActivity,
                        response.body()!!.payload as ArrayList<ResponseMainAddress.Payload?>?
                    )
                    recyclerAddress.adapter = adapter

                } else {
                    txtAddress.visibility = View.GONE
                    linearAddress.visibility = View.VISIBLE
                    recyclerAddress.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ResponseMainAddress>, t: Throwable) {
                Helper.dismissLoader()
                Helper.showTost(this@AddressListingActivity,t.message)
            }

        })
    }

    fun removeAddress(addressId: Int?) {
        if (NetworkConnection.checkConnection(this)) {
            Helper.showLoader(this@AddressListingActivity)!!

            var api = MyApi(this)
            val requestCall: Call<ResponseMainLogin> = api.deleteAddress(
                "Bearer " + Prefs.getString(
                    Constants.PREF_TOKEN, ""
                ), addressId!!
            )

            requestCall.enqueue(object : Callback<ResponseMainLogin> {
                override fun onResponse(
                    call: Call<ResponseMainLogin>,
                    response: Response<ResponseMainLogin>
                ) {
                    Helper.dismissLoader()
                    if (response.body() != null && response.body()!!.result == true) {
                        Helper.showTost(this@AddressListingActivity, response.body()!!.message!!)
                        finish()
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                    } else {
                        Helper.showTost(this@AddressListingActivity, response.body()!!.message!!)
                    }
                }

                override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                    Helper.dismissLoader()
                    Helper.showTost(this@AddressListingActivity,t.message)
                }
            })
        } else {
            Helper.showTost(this, resources.getString(R.string.no_internet))
        }

    }

    override fun onBackPressed() {

    }

    fun editAddress(address: ResponseMainAddress.Payload?) {
        var intent = Intent(this@AddressListingActivity,AddAddressActivity::class.java)
        intent.putExtra("address",address)
        startActivityForResult(intent,100)
    }
}