package com.lappenfashion.ui.otp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simplemvvm.utils.Constants
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainLogin
import com.lappenfashion.data.model.ResponseMainVerifyOtp
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.ui.MainActivity
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_otp.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OTPActivity : AppCompatActivity() {

    private var mobileNumber: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        if(intent!=null){
            mobileNumber = intent.getStringExtra("mobile_number")!!
        }

        imgBack.setOnClickListener {
            finish()
        }

        txtResendOtp.setOnClickListener {
            resendOTP()
        }

        txtLogin.setOnClickListener {
            if(NetworkConnection.checkConnection(this)) {
                com.lappenfashion.utils.Helper.showLoader(this@OTPActivity)
                if(et_otp.text.toString()!="" && et_otp.text.toString().length == 6) {
                    com.lappenfashion.utils.Helper.showLoader(this@OTPActivity)
                    verifyOTP(mobileNumber,et_otp.text.toString())
                }else{
                    com.lappenfashion.utils.Helper.showTost(this, "Please enter valid OTP")
                }
            }else{
                com.lappenfashion.utils.Helper.showTost(this, getString(R.string.no_internet))
            }
        }

    }

    private fun resendOTP() {
        var api = MyApi(this@OTPActivity)
        val requestCall: Call<ResponseMainLogin> = api.resendOTP(mobileNumber)

        requestCall.enqueue(object : Callback<ResponseMainLogin> {
            override fun onResponse(
                call: Call<ResponseMainLogin>,
                response: Response<ResponseMainLogin>
            ) {
                Helper.dismissLoader()

                if (response.body() != null) {
                    if (response.body()?.result == true) {
                        Helper.showTost(
                            this@OTPActivity,
                            response.body()?.message!!
                        )
                    }
                } else {
                    Helper.showTost(
                        this@OTPActivity,
                        resources.getString(R.string.some_thing_happend_wrong)
                    )
                }
            }

            override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                Helper.dismissLoader()
            }

        })
    }

    private fun verifyOTP(mobileNumber: String, otp: String) {
        var api = MyApi(this)
        val requestCall: Call<ResponseMainVerifyOtp> = api.verifyOtp(mobileNumber,otp)
        requestCall.enqueue(object : Callback<ResponseMainVerifyOtp> {
            override fun onResponse(
                call: Call<ResponseMainVerifyOtp>,
                response: Response<ResponseMainVerifyOtp>
            ) {
                Helper.dismissLoader()

                if (response.body() != null) {
                    if(response.body()?.result==true){
                        Prefs.putString(Constants.PREF_TOKEN,response.body()?.payload?.accessToken)
                        Prefs.putString(Constants.PREF_PROFILE_MOBILE_NUMBER,response.body()?.payload?.mobileNumber)
                        Prefs.putInt(Constants.PREF_USER_ID,response.body()?.payload?.userId!!)
                        Prefs.putString(Constants.PREF_IS_LOGGED_IN,"1")
                        finish()
                    }
                } else {
                    com.lappenfashion.utils.Helper.showTost(this@OTPActivity, resources.getString(R.string.some_thing_happend_wrong))
                }
            }

            override fun onFailure(call: Call<ResponseMainVerifyOtp>, t: Throwable) {
                com.lappenfashion.utils.Helper.dismissLoader()
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Helper.dismissLoader()
    }
}