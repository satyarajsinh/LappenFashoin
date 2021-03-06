package com.lappenfashion.ui.otp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.simplemvvm.utils.Constants
import com.google.firebase.iid.FirebaseInstanceId
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainLogin
import com.lappenfashion.data.model.ResponseMainVerifyOtp
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.ui.editProfile.EditProfileActivity
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_otp.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OTPActivity : AppCompatActivity() {

    private var firebaseToken: String = ""
    private var mobileNumber: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        if(intent!=null){
            mobileNumber = intent.getStringExtra("mobile_number")!!
        }

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.w("TAG", "getInstanceId failed", it.exception)
                return@addOnCompleteListener
            }

            // Get new Instance ID token
            val token: String = it.result?.getToken()!!

            // Log and toast
            firebaseToken = token
            Log.e("Firebase token : ", firebaseToken)
        }

        imgBack.setOnClickListener {
            finish()
        }

        txtResendOtp.setOnClickListener {
            txtResendOtpTime.visibility = View.VISIBLE
            txtResendOtp.isClickable = false
            object : CountDownTimer(30000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    txtResendOtpTime.setText("seconds remaining: " + millisUntilFinished / 1000)
                }

                override fun onFinish() {
                    txtResendOtp.isClickable = true
                    txtResendOtpTime.visibility = View.GONE
                    txtResendOtpTime.setText("done!")
                }
            }.start()

            resendOTP()
        }

        txtLogin.setOnClickListener {
            if(NetworkConnection.checkConnection(this)) {
                Helper.showLoader(this@OTPActivity)
                if(et_otp.text.toString()!="" && et_otp.text.toString().length == 6) {
                    verifyOTP(mobileNumber,et_otp.text.toString())
                }else{
                    Helper.showTost(this, "Please enter valid OTP")
                }
            }else{
                Helper.showTost(this, getString(R.string.no_internet))
            }
        }

        txtResendOtpTime.visibility = View.VISIBLE
        txtResendOtp.isClickable = false
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                txtResendOtpTime.setText("seconds remaining: " + millisUntilFinished / 1000)
            }

            override fun onFinish() {
                txtResendOtp.isClickable = true
                txtResendOtpTime.visibility = View.GONE
                txtResendOtpTime.setText("done!")
            }
        }.start()
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
                Helper.showTost(this@OTPActivity,t.message)
            }

        })
    }

    private fun verifyOTP(mobileNumber: String, otp: String) {
        var api = MyApi(this)
        val requestCall: Call<ResponseMainVerifyOtp> = api.verifyOtp(mobileNumber,otp,firebaseToken,"android")
        requestCall.enqueue(object : Callback<ResponseMainVerifyOtp> {
            override fun onResponse(
                call: Call<ResponseMainVerifyOtp>,
                response: Response<ResponseMainVerifyOtp>
            ) {
                Helper.dismissLoader()
                if (response.body() != null) {
                    if(response.body()?.result==true){
                        Prefs.putString(Constants.PREF_TOKEN,response.body()?.payload?.accessToken)
                        Prefs.putInt(Constants.PREF_USER_ID,response.body()?.payload?.userId!!)
                        Prefs.putString(Constants.PREF_IS_LOGGED_IN,"1")
                        Prefs.putString(
                            Constants.PREF_PROFILE_PICTURE,
                            response.body()!!.payload?.image
                        )
                        Prefs.putString(
                            Constants.PREF_PROFILE_FULL_NAME,
                            response.body()!!.payload?.name
                        )
                        Prefs.putString(
                            Constants.PREF_PROFILE_MOBILE_NUMBER,
                            response.body()!!.payload?.mobileNumber
                        )
                        Prefs.putString(Constants.PREF_PROFILE_EMAIL, response.body()!!.payload?.email)
                        Prefs.putString(
                            Constants.PREF_PROFILE_DATE_OF_BIRTH,
                            response.body()!!.payload?.birthDate
                        )
                        Prefs.putString(
                            Constants.PREF_PROFILE_GENDER,
                            response.body()!!.payload?.gender
                        )
                        if(response.body()?.payload!!.is_profile_setup == 0){
                            var intent = Intent(this@OTPActivity,EditProfileActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            finish()
                        }
                    }
                } else {
                    Helper.showTost(this@OTPActivity, resources.getString(R.string.some_thing_happend_wrong))
                }
            }

            override fun onFailure(call: Call<ResponseMainVerifyOtp>, t: Throwable) {
                Helper.dismissLoader()
                Helper.showTost(this@OTPActivity,t.message)
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Helper.dismissLoader()
    }
}