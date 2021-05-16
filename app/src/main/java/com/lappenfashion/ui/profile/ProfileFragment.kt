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
import com.bumptech.glide.Glide
import com.example.simplemvvm.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainLogin
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.ui.MainActivity
import com.lappenfashion.ui.address.AddAddressActivity
import com.lappenfashion.ui.address.AddressListingActivity
import com.lappenfashion.ui.editProfile.EditProfileActivity
import com.lappenfashion.ui.orderList.OrderListActivity
import com.lappenfashion.ui.otp.OTPActivity
import com.lappenfashion.ui.wishlist.WishListActivity
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.imgProfile
import kotlinx.android.synthetic.main.fragment_profile.*
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
            displayLoginDialog()

        }

        rootView.relativeTrackOrder.setOnClickListener {
            if(Prefs.getString(Constants.PREF_IS_LOGGED_IN,"")=="1") {
                var intent = Intent(mContext, OrderListActivity::class.java)
                startActivity(intent)
            }else{
                displayLoginDialog()
            }
        }

        rootView.relativeEditProfile.setOnClickListener {
            if(Prefs.getString(Constants.PREF_IS_LOGGED_IN,"")=="1") {
                var intent = Intent(mContext, EditProfileActivity::class.java)
                startActivity(intent)
            }else{
                displayLoginDialog()
            }
        }

        rootView.relativeWishlist.setOnClickListener {
            if(Prefs.getString(Constants.PREF_IS_LOGGED_IN,"")=="1") {
                var intent = Intent(mContext, WishListActivity::class.java)
                startActivity(intent)
            }else{
                displayLoginDialog()
            }
        }

        rootView.relativeLogout.setOnClickListener {
            var count = Prefs.getInt(Constants.PREF_CART_COUNT,0)
            Prefs.clear()
            Prefs.putInt(Constants.PREF_CART_COUNT,count)
            var intent = Intent(mContext,MainActivity::class.java)
            startActivity(intent)
        }


        rootView.relativeAddress.setOnClickListener {
            if(Prefs.getString(Constants.PREF_IS_LOGGED_IN,"")=="1") {
                var intent = Intent(mContext,AddressListingActivity::class.java)
                startActivity(intent)
            }else{
                displayLoginDialog()
            }

        }
    }

    private fun displayLoginDialog() {
        val dialog = BottomSheetDialog(mContext)
        dialog.setContentView(R.layout.bottom_sheet_login)
        dialog.setCanceledOnTouchOutside(false)

        val imgClose = dialog.findViewById<View>(R.id.imgClose) as ImageView?
        val txtLogin = dialog.findViewById<View>(R.id.txtLogin) as TextView?
        val edtMobileNumber = dialog.findViewById<View>(R.id.edtPhoneNumber) as EditText?

        txtLogin!!.setOnClickListener {
            if(edtMobileNumber?.text.toString() != ""){
                if(NetworkConnection.checkConnection(mContext)) {
                    txtLogin.isEnabled = false
                    com.lappenfashion.utils.Helper.showLoader(mContext)
                    loginData(edtMobileNumber?.text.toString(),txtLogin,dialog)
                }else{
                    com.lappenfashion.utils.Helper.showTost(mContext, getString(R.string.no_internet))
                }
            }else{
                com.lappenfashion.utils.Helper.showTost(mContext,"Field is required")
            }

        }

        imgClose!!.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun loginData(mobileNumber: String, txtLogin: TextView,dialog: BottomSheetDialog) {
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
                        dialog.dismiss()
                        com.lappenfashion.utils.Helper.showTost(mContext, response.body()?.message!!)
                        var intent = Intent(mContext,OTPActivity::class.java)
                        intent.putExtra("mobile_number",mobileNumber)
                        startActivityForResult(intent,101)
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
            rootView.relativeLogout.visibility = View.VISIBLE
        }else{
            rootView.relativeProfile.visibility = View.GONE
            rootView.relativeLogout.visibility = View.GONE
            rootView.txtLogin.visibility = View.VISIBLE
        }

        if(Prefs.getString(Constants.PREF_PROFILE_PICTURE,"") != "") {
            Glide.with(mContext)
                .load(Prefs.getString(Constants.PREF_PROFILE_PICTURE, "")).into(rootView.imgProfile)
        }

        if(Prefs.getString(Constants.PREF_PROFILE_FULL_NAME,"") != "") {
            rootView.txtName.setText(Prefs.getString(Constants.PREF_PROFILE_FULL_NAME,""))
        }

        if(Prefs.getString(Constants.PREF_PROFILE_EMAIL,"") != "") {
            rootView.txtEmail.setText(Prefs.getString(Constants.PREF_PROFILE_EMAIL,""))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 101){
            initData()
        }
    }

}