package com.lappenfashion.ui.checkout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lappenfashion.R
import kotlinx.android.synthetic.main.activity_checkout_delivery_address.*

class CheckoutDeliveryAddressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_delivery_address)

        imgBack.setOnClickListener {
            finish()
        }

        txtBack.setOnClickListener {
            finish()
        }
    }
}