package com.lappenfashion.ui.faq

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainFaq
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import kotlinx.android.synthetic.main.activity_faq.*
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.android.synthetic.main.toolbar_with_back_button.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaqActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)

        initData()
    }

    private fun initData() {
        txtTitle.text = "FAQ"

        if (NetworkConnection.checkConnection(this@FaqActivity)) {
            Helper.showLoader(this@FaqActivity)
            getFaq()
        } else {
            Helper.showTost(this@FaqActivity, resources.getString(R.string.no_internet))
        }

        imgBack.setOnClickListener {
            finish()
        }
    }

    private fun getFaq() {
        var api = MyApi(this@FaqActivity)
        val requestCall: Call<ResponseMainFaq> = api.getFaq()

        requestCall.enqueue(object : Callback<ResponseMainFaq> {
            override fun onResponse(
                call: Call<ResponseMainFaq>,
                response: Response<ResponseMainFaq>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()?.result == true) {
                    recyclerviewFaq.layoutManager = LinearLayoutManager(
                        this@FaqActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    recyclerviewFaq.setHasFixedSize(true)
                    var faqAdapter = FaqAdapter(
                        this@FaqActivity,
                        response.body()?.payload!!
                    )
                    recyclerviewFaq.adapter = faqAdapter
                }
            }

            override fun onFailure(call: Call<ResponseMainFaq>, t: Throwable) {
                Helper.showTost(this@FaqActivity,t.message)
                Helper.dismissLoader()
            }

        })
    }
}