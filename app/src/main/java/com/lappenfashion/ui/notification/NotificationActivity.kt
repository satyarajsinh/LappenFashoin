package com.lappenfashion.ui.notification

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplemvvm.utils.Constants
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainNotification
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.activity_product_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        if (NetworkConnection.checkConnection(this@NotificationActivity)) {
            Helper.showLoader(this@NotificationActivity)
            getNotification()
        }else {
            Helper.showTost(this@NotificationActivity, "No internet connection")
        }

        imgBack.setOnClickListener {
            finish()
        }
    }

    private fun getNotification() {
        var api = MyApi(this@NotificationActivity)
        val requestCall: Call<ResponseMainNotification> = api.getNotification(
            "Bearer " + Prefs.getString(
                Constants.PREF_TOKEN, ""))

        requestCall.enqueue(object : Callback<ResponseMainNotification> {
            override fun onResponse(
                call: Call<ResponseMainNotification>,
                response: Response<ResponseMainNotification>
            ) {
                Helper.dismissLoader()

                if (response.body() != null && response.body()?.result == true && response.body()?.payload?.size!! > 0) {
                    txtNoData.visibility = View.GONE
                    recyclerNotification.layoutManager = LinearLayoutManager(
                        this@NotificationActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    recyclerNotification.setHasFixedSize(true)
                    var notificationAdapter = NotificationAdapter(
                        this@NotificationActivity,
                        response.body()?.payload!!
                    )
                    recyclerNotification.adapter = notificationAdapter
                } else {
                    txtNoData.visibility = View.VISIBLE
                    Helper.showTost(
                        this@NotificationActivity,
                        resources.getString(R.string.some_thing_happend_wrong)
                    )
                }
            }

            override fun onFailure(call: Call<ResponseMainNotification>, t: Throwable) {
                Helper.dismissLoader()
                txtNoData.visibility = View.VISIBLE
                Helper.showTost(this@NotificationActivity, t.message)
            }

        })
    }
}