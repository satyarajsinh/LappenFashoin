package com.lappenfashion.ui.address

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simplemvvm.utils.Constants
import com.google.gson.JsonObject
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainLogin
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.activity_products_by_product_category.*
import kotlinx.android.synthetic.main.toolbar_with_back_button.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAddressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        txtTitle.text = "Add Address"

        clickListeners()
    }

    private fun clickListeners() {

        imgBack.setOnClickListener {
            finish()
        }

        txtSave.setOnClickListener {
            if(edtName.text.toString()==""){
                edtName.error = "Field is required"
            }else if(edtMobileNumber.text.toString()==""){
                edtMobileNumber.error = "Field is required"
            }else if(edtMobileNumber.text.toString().length < 10){
                edtMobileNumber.error = "Enter valid mobile number"
            }else if(edtPincode.text.toString()==""){
                edtPincode.error = "Field is required"
            }else if(edtState.text.toString()==""){
                edtState.error = "Field is required"
            }else if(edtAddress.text.toString()==""){
                edtAddress.error = "Field is required"
            }else if(edtLocality.text.toString()==""){
                edtLocality.error = "Field is required"
            }else if(edtCity.text.toString()==""){
                edtCity.error = "Field is required"
            }else if(!radioHome.isChecked && !radioOffice.isChecked){
                Helper.showTost(this@AddAddressActivity,"Please select your address type")
            }else{
                if (NetworkConnection.checkConnection(this)) {
                    Helper.showLoader(this@AddAddressActivity)!!
                    if(radioHome.isChecked) {
                        addAddress("home")
                    }else{
                        addAddress("office")
                    }
                }else {
                    Helper.showTost(this, "No internet connection")
                }
            }
        }
    }

    private fun addAddress(type: String) {
        var api = MyApi(this)
        val requestCall: Call<ResponseMainLogin> = api.addAddress("Bearer "+ Prefs.getString(Constants.PREF_TOKEN, ""),edtName.text.toString(),edtMobileNumber.text.toString(),edtPincode.text.toString(),edtState.text.toString(),edtAddress.text.toString(),edtLocality.text.toString(),edtCity.text.toString(),type)

        requestCall.enqueue(object : Callback<ResponseMainLogin> {
            override fun onResponse(
                call: Call<ResponseMainLogin>,
                response: Response<ResponseMainLogin>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()!!.result == true) {
                    Helper.showTost(this@AddAddressActivity, response.body()!!.message!!)
                    finish()
                }
            }

            override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                Helper.dismissLoader()
            }

        })
    }
}