package com.lappenfashion.ui.filter

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.LabelFormatter
import com.lappenfashion.R
import kotlinx.android.synthetic.main.activity_filter.*
import java.text.NumberFormat
import java.util.*

class FilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        slider.setLabelFormatter(object : LabelFormatter{
            override fun getFormattedValue(value: Float): String {
                /*var currencyFormat = NumberFormat.getCurrencyInstance()
                currencyFormat.currency = Currency.getInstance("₹")*/
                var currency = "₹ $value"
                return currency
            }
        })

        slider.addOnChangeListener { slider, value, fromUser ->
            Log.e("valueeeeeeeeeeeee","valueeeeeeeeeeeee"+slider.values[0])
            Log.e("valueeeeeeeeeeeee","valueeeeeeeeeeeee"+slider.values[1])

        }

    }
}