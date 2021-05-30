package com.lappenfashion.ui.address

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.simplemvvm.utils.Constants
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainAddress
import com.lappenfashion.data.model.ResponseMainHome
import com.lappenfashion.data.model.ResponseMainLogin
import com.lappenfashion.data.model.ResponseMainZipCode
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.MyApiZipCode
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.activity_products_by_product_category.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.toolbar_with_back_button.*
import kotlinx.android.synthetic.main.toolbar_with_drawer.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddAddressActivity : AppCompatActivity() {

    private var valid: Boolean = false
    private var addressId: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        txtTitle.text = "Add Address"

        if(intent!=null){
            var data = intent.getSerializableExtra("address") as ResponseMainAddress.Payload?
            if(data!=null){
                txtSave.text = "Update"
                addressId = data?.addressId
                edtName.setText(data?.name)
                edtMobileNumber.setText(data?.mobileNumber)
                valid = true
                edtPincode.setText(data?.pincode)
                edtState.setText(data?.state)
                edtAddress.setText(data?.address)
                edtLocality.setText(data?.localityTown)
                edtCity.setText(data?.city)

                if(data?.type=="home"){
                    radioHome.isChecked = true
                }else{
                    radioOffice.isChecked = true
                }
            }
        }

        clickListeners()
    }

    private fun clickListeners() {

        imgBack.setOnClickListener {
            val intent = Intent()
            setResult(100, intent)
            finish() //finishing ac
        }

        edtPincode.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                valid = false
                if(s?.length == 6 || count == 6){
                    txtPincodeError.visibility = View.GONE
                    if (NetworkConnection.checkConnection(this@AddAddressActivity)) {
                        Helper.showLoader(this@AddAddressActivity)
                        checkZipCode(edtPincode.text.toString())
                    }else {
                        Helper.showTost(this@AddAddressActivity, "No internet connection")
                    }
                }else{
                    txtPincodeError.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        txtSave.setOnClickListener {
            if(edtName.text.toString()==""){
                edtName.error = "Field is required"
            }else if(edtMobileNumber.text.toString()==""){
                edtMobileNumber.error = "Field is required"
            }else if(edtMobileNumber.text.toString().length < 10 || edtMobileNumber.text.toString().length > 10){
                edtMobileNumber.error = "Enter valid mobile number"
            }else if(edtPincode.text.toString()=="" || edtPincode.text.toString().length != 6){
                edtPincode.error = "Field is required"
            }else if(!valid){
                edtPincode.error = "Invalid Pincode"
            }else if(edtState.text.toString()==""){
                edtState.error = "Field is required"
            }else if(edtAddress.text.toString()==""){
                edtAddress.error = "Field is required"
            }else if(edtLocality.text.toString()==""){
                edtLocality.error = "Field is required"
            }else if(edtCity.text.toString()==""){
                edtCity.error = "Field is required"
            }else if(!radioHome.isChecked && !radioOffice.isChecked){
                Helper.showTost(this@AddAddressActivity, "Please select your address type")
            }else{
                if (NetworkConnection.checkConnection(this)) {
                    Helper.showLoader(this@AddAddressActivity)!!
                    if(txtSave.text == "SAVE") {
                        if (radioHome.isChecked) {
                            addAddress("home")
                        } else {
                            addAddress("office")
                        }
                    }else{
                        if (radioHome.isChecked) {
                            updateAddress("home")
                        } else {
                            updateAddress("office")
                        }

                    }
                }else {
                    Helper.showTost(this, "No internet connection")
                }
            }
        }

        txtCancel.setOnClickListener {
            finish()
        }
    }

    private fun checkZipCode(zip: String) {
        var api = MyApiZipCode(this@AddAddressActivity)
        val requestCall: Call<ResponseMainZipCode> = api.checkPincode(Constants.END_POINT_ZIP_CODE+"?filter_codes="+zip+"&token=b88d8c379d23efae66d55d709add178d986b7dd2")

        requestCall.enqueue(object : Callback<ResponseMainZipCode> {
            override fun onResponse(
                call: Call<ResponseMainZipCode>,
                response: Response<ResponseMainZipCode>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()!!.deliveryCodes?.size!! > 0) {
                    valid = true
                    txtPincodeError.visibility = View.GONE
                    if(response.body()!!.deliveryCodes?.get(0)?.postalCode!! !=null) {
                        edtCity.setText(response.body()!!.deliveryCodes?.get(0)?.postalCode?.district!!)
                    }
                } else {
                    txtPincodeError.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ResponseMainZipCode>, t: Throwable) {
                Helper.dismissLoader()
                Helper.showTost(this@AddAddressActivity,t.message)
            }

        })
    }

    private fun updateAddress(type: String) {
        var api = MyApi(this)
        val requestCall: Call<ResponseMainLogin> = api.editAddress(
            "Bearer " + Prefs.getString(
                Constants.PREF_TOKEN,
                ""
            ), addressId!!,
            edtName.text.toString(),
            edtMobileNumber.text.toString(),
            edtPincode.text.toString(),
            edtState.text.toString(),
            edtAddress.text.toString(),
            edtLocality.text.toString(),
            edtCity.text.toString(),
            type
        )

        requestCall.enqueue(object : Callback<ResponseMainLogin> {
            override fun onResponse(
                call: Call<ResponseMainLogin>,
                response: Response<ResponseMainLogin>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()!!.result == true) {
                    Helper.showTost(this@AddAddressActivity, response.body()!!.message!!)
                    val intent = Intent()
                    setResult(100, intent)
                    finish() //finishing ac
                }
            }

            override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                Helper.dismissLoader()
                Helper.showTost(this@AddAddressActivity,t.message)
            }

        })
    }

    private fun addAddress(type: String) {
        var api = MyApi(this)
        val requestCall: Call<ResponseMainLogin> = api.addAddress(
            "Bearer " + Prefs.getString(
                Constants.PREF_TOKEN,
                ""
            ),
            edtName.text.toString(),
            edtMobileNumber.text.toString(),
            edtPincode.text.toString(),
            edtState.text.toString(),
            edtAddress.text.toString(),
            edtLocality.text.toString(),
            edtCity.text.toString(),
            type
        )

        requestCall.enqueue(object : Callback<ResponseMainLogin> {
            override fun onResponse(
                call: Call<ResponseMainLogin>,
                response: Response<ResponseMainLogin>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()!!.result == true) {
                    Helper.showTost(this@AddAddressActivity, response.body()!!.message!!)
                    val intent = Intent()
                    setResult(100, intent)
                    finish() //finishing ac
                }
            }

            override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                Helper.dismissLoader()
                Helper.showTost(this@AddAddressActivity,t.message)
            }

        })
    }

    override fun onBackPressed() {

    }
}