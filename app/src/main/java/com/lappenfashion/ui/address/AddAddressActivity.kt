package com.lappenfashion.ui.address

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lappenfashion.R
import kotlinx.android.synthetic.main.toolbar_with_back_button.*

class AddAddressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        txtTitle.text = "Add Address"
    }
}