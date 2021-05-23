package com.lappenfashion.ui.orderList

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplemvvm.utils.Constants
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainOrderList
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.toolbar_with_back_button.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        initData()
    }

    private fun initData() {
        txtTitle.text = "Orders"

        if (NetworkConnection.checkConnection(this@OrderListActivity)) {
            Helper.showLoader(this@OrderListActivity)
            getOrders()
        } else {
            Helper.showTost(this@OrderListActivity, resources.getString(R.string.no_internet))
        }

        imgBack.setOnClickListener {
            finish()
        }
    }

    private fun getOrders() {
        var api = MyApi(this@OrderListActivity)
        val requestCall: Call<ResponseMainOrderList> = api.getOrders("Bearer " + Prefs.getString(
            Constants.PREF_TOKEN, ""
        ))

        requestCall.enqueue(object : Callback<ResponseMainOrderList> {
            override fun onResponse(
                call: Call<ResponseMainOrderList>,
                response: Response<ResponseMainOrderList>
            ) {
                if (response.body() != null && response.body()?.result == true && response.body()?.payload!=null && response.body()?.payload?.data?.size!! > 0) {
                    recyclerOrderList.layoutManager = LinearLayoutManager(
                        this@OrderListActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    recyclerOrderList.setHasFixedSize(true)
                    var orderListAdapter = OrderListAdapter(
                        this@OrderListActivity,
                        response.body()?.payload?.data!!
                    )
                    recyclerOrderList.adapter = orderListAdapter
                } else {
                    Helper.showTost(this@OrderListActivity, "No Data Found")
                }
                Helper.dismissLoader()
            }

            override fun onFailure(call: Call<ResponseMainOrderList>, t: Throwable) {
                Helper.dismissLoader()
            }

        })
    }

    fun goToOrderDetails(get: ResponseMainOrderList.Payload.Data) {
        var intent = Intent(this@OrderListActivity,OrderDetailsActivity::class.java)
        intent.putExtra("orderDetails",get)
        startActivity(intent)
    }
}