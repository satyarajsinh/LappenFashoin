package com.lappenfashion.ui.wishlist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.simplemvvm.utils.Constants
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainLogin
import com.lappenfashion.data.model.ResponseMainWishList
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.imgBack
import kotlinx.android.synthetic.main.activity_wishlist.*
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

    private fun initData() {
        if (NetworkConnection.checkConnection(this@WishListActivity)) {
            Helper.showLoader(this@WishListActivity)
            getWishList()
        }else{
            Helper.showTost(this@WishListActivity,resources.getString(R.string.no_internet))
        }
    }

    private fun getWishList() {
        var api = MyApi(this@WishListActivity)
        val requestCall: Call<ResponseMainWishList> = api.getWishList("Bearer "+ Prefs.getString(
            Constants.PREF_TOKEN, ""))

        requestCall.enqueue(object : Callback<ResponseMainWishList> {
            override fun onResponse(
                call: Call<ResponseMainWishList>,
                response: Response<ResponseMainWishList>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()!!.payload?.size!! >0) {
                    relativeNoData.visibility = View.GONE
                    recyclerWishList.visibility = View.VISIBLE
                    recyclerWishList.layoutManager = GridLayoutManager(this@WishListActivity, 2)
                    adapter = WishlistAdapter(
                        this@WishListActivity,
                        response.body()!!.payload as ArrayList<ResponseMainWishList.Payload?>?
                    )
                    recyclerWishList.adapter = adapter
                }else{
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
    }

    fun removeFromWishList(data: ResponseMainWishList.Payload?, position: Int) {
        if (NetworkConnection.checkConnection(this@WishListActivity)) {
            Helper.showLoader(this@WishListActivity)
            var api = MyApi(this@WishListActivity)
            val requestCall: Call<ResponseMainLogin> = api.deleteWishList("Bearer "+ Prefs.getString(
                Constants.PREF_TOKEN, ""), data?.wishListId!!
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
                    }else{
                        Helper.showTost(this@WishListActivity, response.body()!!.message!!)
                    }
                }

                override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                    Helper.dismissLoader()
                }

            })
        }else{
            Helper.showTost(this@WishListActivity,getString(R.string.no_internet))
        }

    }

    fun addToCart(data: ResponseMainWishList.Payload?, position: Int) {
        if (NetworkConnection.checkConnection(this@WishListActivity)) {
            Helper.showLoader(this@WishListActivity)
            var api = MyApi(this@WishListActivity)
            val requestCall: Call<ResponseMainLogin> = api.addToCart("Bearer "+ Prefs.getString(
                Constants.PREF_TOKEN, ""), data?.product?.productId.toString(),"1",data?.product?.salePrice.toString()
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
                    }else{
                        Helper.showTost(this@WishListActivity, response.body()!!.message!!)
                    }
                }

                override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                    Helper.dismissLoader()
                }

            })
        }else{
            Helper.showTost(this@WishListActivity,getString(R.string.no_internet))
        }
    }
}