package com.lappenfashion.ui.wishlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.simplemvvm.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainCartNew
import com.lappenfashion.data.model.ResponseMainLogin
import com.lappenfashion.data.model.ResponseMainWishList
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.ui.MainActivity
import com.lappenfashion.ui.cart.CartActivity
import com.lappenfashion.ui.otp.OTPActivity
import com.lappenfashion.utils.Helper
import com.lappenfashion.utils.SpacesItemDecoration
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.imgBack
import kotlinx.android.synthetic.main.activity_wishlist.*
import kotlinx.android.synthetic.main.activity_wishlist.txtCartCount
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.toolbar_with_like_cart.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishListActivity : AppCompatActivity() {


    private lateinit var adapter: WishlistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)

        initData()
        clicklisteners()
    }

    override fun onResume() {
        super.onResume()
        if(Prefs.getInt(Constants.PREF_CART_COUNT,0) == 0){
            txtCartCount.visibility = View.GONE
        }else{
            txtCartCount.visibility = View.VISIBLE
            txtCartCount.text = Prefs.getInt(Constants.PREF_CART_COUNT,0).toString()
        }

    }
    private fun initData() {

        if (Prefs.getString(Constants.PREF_IS_LOGGED_IN, "") == "1") {
            if (NetworkConnection.checkConnection(this@WishListActivity)) {
                Helper.showLoader(this@WishListActivity)
                getWishList()
            } else {
                Helper.showTost(this@WishListActivity, resources.getString(R.string.no_internet))
            }
        }else{
            relativeNoData.visibility = View.VISIBLE
            recyclerWishList.visibility = View.GONE
        }

    }

    private fun getWishList() {
        var api = MyApi(this@WishListActivity)
        val requestCall: Call<ResponseMainWishList> = api.getWishList(
            "Bearer " + Prefs.getString(
                Constants.PREF_TOKEN, ""
            )
        )

        requestCall.enqueue(object : Callback<ResponseMainWishList> {
            override fun onResponse(
                call: Call<ResponseMainWishList>,
                response: Response<ResponseMainWishList>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()!!.payload?.size!! > 0) {
                    relativeNoData.visibility = View.GONE
                    recyclerWishList.visibility = View.VISIBLE
                    recyclerWishList.layoutManager = GridLayoutManager(this@WishListActivity, 2)
                    var spaceIterator1 = SpacesItemDecoration(10)
                    recyclerWishList.addItemDecoration(spaceIterator1);
                    adapter = WishlistAdapter(
                        this@WishListActivity,
                        response.body()!!.payload as ArrayList<ResponseMainWishList.Payload?>?
                    )
                    recyclerWishList.adapter = adapter
                } else {
                    relativeNoData.visibility = View.VISIBLE
                    recyclerWishList.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ResponseMainWishList>, t: Throwable) {
                Helper.dismissLoader()
            }

        })
    }

    private fun clicklisteners() {
        imgBack.setOnClickListener {
            finish()
        }

        imgBag.setOnClickListener {
            var intent = Intent(this@WishListActivity, CartActivity::class.java)
            startActivity(intent)
        }

        txtStartShopping.setOnClickListener {
            finish()
        }
    }

    fun removeFromWishList(data: ResponseMainWishList.Payload?, position: Int) {
        if (NetworkConnection.checkConnection(this@WishListActivity)) {
            Helper.showLoader(this@WishListActivity)
            var api = MyApi(this@WishListActivity)
            val requestCall: Call<ResponseMainLogin> = api.deleteWishList(
                "Bearer " + Prefs.getString(
                    Constants.PREF_TOKEN, ""
                ), data?.wishListId!!
            )

            requestCall.enqueue(object : Callback<ResponseMainLogin> {
                override fun onResponse(
                    call: Call<ResponseMainLogin>,
                    response: Response<ResponseMainLogin>
                ) {
                    Helper.dismissLoader()
                    if (response.body() != null && response.body()!!.result == true) {
                        adapter.removeData(position)
                        Helper.showTost(this@WishListActivity, response.body()!!.message!!)
                    } else {
                        Helper.showTost(this@WishListActivity, response.body()!!.message!!)
                    }
                }

                override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                    Helper.dismissLoader()
                }

            })
        } else {
            Helper.showTost(this@WishListActivity, getString(R.string.no_internet))
        }

    }

    fun addToCart(data: ResponseMainWishList.Payload?, position: Int) {
        if(Prefs.getString(Constants.PREF_IS_LOGGED_IN,"") == "1") {
            if (NetworkConnection.checkConnection(this@WishListActivity)) {
                val array = JsonArray()
                var cartObject = JsonObject()
                cartObject.addProperty("product_id", data?.product?.productId.toString())
                cartObject.addProperty("quantity", "1")
                cartObject.addProperty("amount", data?.product?.mrp.toString())
                array.add(cartObject)

                Log.e("json Array", "json Array" + array.toString())

                Helper.showLoader(this@WishListActivity)
                var api = MyApi(this@WishListActivity)
                val requestCall: Call<ResponseMainCartNew> =
                    api.addCart("Bearer " + Prefs.getString(Constants.PREF_TOKEN, ""), array)

                requestCall.enqueue(object : Callback<ResponseMainCartNew> {
                    override fun onResponse(
                        call: Call<ResponseMainCartNew>,
                        response: Response<ResponseMainCartNew>
                    ) {
                        Helper.dismissLoader()
                        if (response.body() != null && response.body()!!.result == true) {
                            Prefs.putInt(Constants.PREF_CART_COUNT,
                                response.body()!!.payload?.cartCount!!
                            )
                            txtCartCount.visibility = View.VISIBLE
                            txtCartCount.text = response.body()!!.payload?.cartCount.toString()
                            Helper.showTost(this@WishListActivity, response.body()!!.message!!)
                        } else {
                            Helper.showTost(this@WishListActivity, "Something happend wrong, Please try again later")
                        }
                    }

                    override fun onFailure(call: Call<ResponseMainCartNew>, t: Throwable) {
                        Helper.dismissLoader()
                    }

                })
            } else {
                Helper.showTost(this@WishListActivity, getString(R.string.no_internet))
            }
        }else{
            displayLoginDialog()
        }
    }

    private fun displayLoginDialog() {
        val dialog = BottomSheetDialog(this@WishListActivity)
        dialog.setContentView(R.layout.bottom_sheet_login)
        dialog.setCanceledOnTouchOutside(false)

        val imgClose = dialog.findViewById<View>(R.id.imgClose) as ImageView?
        val txtLogin = dialog.findViewById<View>(R.id.txtLogin) as TextView?
        val edtMobileNumber = dialog.findViewById<View>(R.id.edtPhoneNumber) as EditText?

        txtLogin!!.setOnClickListener {
            if(edtMobileNumber?.text.toString() != ""){
                if(NetworkConnection.checkConnection(this@WishListActivity)) {
                    txtLogin.isEnabled = false
                    Helper.showLoader(this@WishListActivity)
                    loginData(edtMobileNumber?.text.toString(),txtLogin,dialog)
                }else{
                    Helper.showTost(this@WishListActivity, getString(R.string.no_internet))
                }
            }else{
                Helper.showTost(this@WishListActivity,"Field is required")
            }

        }

        imgClose!!.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun loginData(mobileNumber: String, txtLogin: TextView, dialog: BottomSheetDialog) {
        var api = MyApi(this@WishListActivity)
        val requestCall: Call<ResponseMainLogin> = api.login(mobileNumber)

        requestCall.enqueue(object : Callback<ResponseMainLogin> {
            override fun onResponse(
                call: Call<ResponseMainLogin>,
                response: Response<ResponseMainLogin>
            ) {
                Helper.dismissLoader()
                if (response.body() != null) {
                    if( response.body()?.result==true){
                        dialog.dismiss()
                        Helper.showTost(this@WishListActivity, response.body()?.message!!)
                        var intent = Intent(this@WishListActivity, OTPActivity::class.java)
                        intent.putExtra("mobile_number",mobileNumber)
                        startActivityForResult(intent,101)
                    }
                } else {
                    txtLogin.isEnabled = true
                    Helper.showTost(this@WishListActivity, resources.getString(R.string.some_thing_happend_wrong))
                }
            }

            override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                txtLogin.isEnabled = true
                Helper.dismissLoader()
            }

        })
    }

}