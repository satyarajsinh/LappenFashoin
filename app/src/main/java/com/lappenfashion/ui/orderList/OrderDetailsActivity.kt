package com.lappenfashion.ui.orderList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainOrderList
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.activity_order_list.*

class OrderDetailsActivity : AppCompatActivity() {

    private var orderDetails: ResponseMainOrderList.Payload.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        initData()
        clickListener()
    }

    private fun clickListener() {
        imgBack.setOnClickListener {
            finish()
        }
    }

    private fun initData() {
        if(intent!=null){
            orderDetails = intent.getSerializableExtra("orderDetails") as ResponseMainOrderList.Payload.Data?

            recyclerProducts.layoutManager = LinearLayoutManager(
                this@OrderDetailsActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            recyclerProducts.setHasFixedSize(true)
            var orderListAdapter = OrderDetailsProductAdapter(
                this@OrderDetailsActivity,
                orderDetails!!.products
            )
            recyclerProducts.adapter = orderListAdapter

            txtTitle.text = "Order Number - "+orderDetails!!.orderId
            txtOrderStatus.text = orderDetails!!.status
            txtGrandTotal.text = "₹"+orderDetails!!.grandTotal.toString()
        }
    }
}