package com.lappenfashion.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.simplemvvm.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainLogin
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.ui.address.AddAddressActivity
import com.lappenfashion.ui.address.AddressListingActivity
import com.lappenfashion.ui.editProfile.EditProfileActivity
import com.lappenfashion.ui.otp.OTPActivity
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.fragment_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {

    private lateinit var rootView : View
    private lateinit var mContext : Context
    private lateinit var txtName : TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_profile, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        clickListener()
    }

    private fun clickListener() {
        rootView.txtLogin.setOnClickListener {
            val dialog = BottomSheetDialog(mContext)
            dialog.setContentView(R.layout.bottom_sheet_login)
            dialog.setCanceledOnTouchOutside(false)

            val imgClose = dialog.findViewById<View>(R.id.imgClose) as ImageView?
            val txtLogin = dialog.findViewById<View>(R.id.txtLogin) as TextView?
            val edtMobileNumber = dialog.findViewById<View>(R.id.edtPhoneNumber) as EditText?
            val txtResendOtp = dialog.findViewById<View>(R.id.txtResendOtp) as TextView?

            txtLogin!!.setOnClickListener {
                if(edtMobileNumber?.text.toString() != ""){
                    if(NetworkConnection.checkConnection(mContext)) {
                        txtLogin.isEnabled = false
                        com.lappenfashion.utils.Helper.showLoader(mContext)
                        loginData(edtMobileNumber?.text.toString(),txtLogin)
                    }else{
                        com.lappenfashion.utils.Helper.showTost(mContext, getString(R.string.no_internet))
                    }
                }else{
                    com.lappenfashion.utils.Helper.showTost(mContext,"Field is required")
                }

            }

            txtResendOtp?.setOnClickListener {
                if(NetworkConnection.checkConnection(mContext)) {
                    com.lappenfashion.utils.Helper.showLoader(mContext)
                    resendOTP(edtMobileNumber?.text.toString())
                }else{
                    com.lappenfashion.utils.Helper.showTost(mContext, getString(R.string.no_internet))
                }
            }

            imgClose!!.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }

        rootView.relativeTrackOrder.setOnClickListener {
            var intent = Intent(mContext,OTPActivity::class.java)
            startActivity(intent)
        }

        rootView.relativeEditProfile.setOnClickListener {
            var intent = Intent(mContext,EditProfileActivity::class.java)
            startActivity(intent)
        }

        rootView.relativeAddress.setOnClickListener {
            var intent = Intent(mContext,AddressListingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun resendOTP(mobileNumber: String) {
        var api = MyApi(mContext)
        val requestCall: Call<ResponseMainLogin> = api.resendOTP(mobileNumber)

        requestCall.enqueue(object : Callback<ResponseMainLogin> {
            override fun onResponse(
                call: Call<ResponseMainLogin>,
                response: Response<ResponseMainLogin>
            ) {
                com.lappenfashion.utils.Helper.dismissLoader()

                if (response.body() != null) {
                    if( response.body()?.result==true){
                        com.lappenfashion.utils.Helper.showTost(mContext, response.body()?.message!!)
                        var intent = Intent(mContext,OTPActivity::class.java)
                        intent.putExtra("mobile_number",mobileNumber)
                        startActivity(intent)
                    }
                } else {
                    com.lappenfashion.utils.Helper.showTost(mContext, resources.getString(R.string.some_thing_happend_wrong))
                }
            }

            override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                com.lappenfashion.utils.Helper.dismissLoader()
            }

        })
    }

    private fun loginData(mobileNumber: String, txtLogin: TextView) {
        var api = MyApi(mContext)
        val requestCall: Call<ResponseMainLogin> = api.login(mobileNumber)

        requestCall.enqueue(object : Callback<ResponseMainLogin> {
            override fun onResponse(
                call: Call<ResponseMainLogin>,
                response: Response<ResponseMainLogin>
            ) {
                com.lappenfashion.utils.Helper.dismissLoader()

                if (response.body() != null) {
                    if( response.body()?.result==true){
                        com.lappenfashion.utils.Helper.showTost(mContext, response.body()?.message!!)
                        var intent = Intent(mContext,OTPActivity::class.java)
                        intent.putExtra("mobile_number",mobileNumber)
                        startActivity(intent)
                    }
                } else {
                    txtLogin.isEnabled = true
                    com.lappenfashion.utils.Helper.showTost(mContext, resources.getString(R.string.some_thing_happend_wrong))
                }
            }

            override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                txtLogin.isEnabled = true
                com.lappenfashion.utils.Helper.dismissLoader()
            }

        })
    }

    private fun initData() {

        if(Prefs.getString(Constants.PREF_IS_LOGGED_IN,"") == "1"){
            rootView.relativeProfile.visibility = View.VISIBLE
            rootView.txtLogin.visibility = View.GONE
        }else{
            rootView.relativeProfile.visibility = View.GONE
            rootView.txtLogin.visibility = View.VISIBLE
        }

    }

}