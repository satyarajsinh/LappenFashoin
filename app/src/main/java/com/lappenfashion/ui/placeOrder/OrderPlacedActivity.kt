package com.lappenfashion.ui.placeOrder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lappenfashion.R
import com.lappenfashion.ui.MainActivity
import kotlinx.android.synthetic.main.activity_order_placed.*

class OrderPlacedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)

        txtGoToHome.setOnClickListener {
            var intent = Intent(this@OrderPlacedActivity,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {

    }
}