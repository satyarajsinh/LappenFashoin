package com.lappenfashion.ui.checkout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lappenfashion.R
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        clickListener()
    }

    private fun clickListener() {
        txtNext.setOnClickListener {
            var intent = Intent(this@CheckoutActivity,CheckoutDeliveryAddressActivity::class.java)
            startActivity(intent)
        }

        imgBack.setOnClickListener {
            finish()
        }
    }
}