package com.lappenfashion.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainDeliveryOption
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_checkout.imgBack
import kotlinx.android.synthetic.main.activity_checkout.txtNext
import kotlinx.android.synthetic.main.activity_checkout_delivery_address.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class CheckoutActivity : AppCompatActivity(),
    DatePickerDialog.OnDateSetListener {

    private var deliveryOptionId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        initData()
        clickListener()
    }

    private fun initData() {
        if(NetworkConnection.checkConnection(this@CheckoutActivity)) {
            Helper.showLoader(this@CheckoutActivity)
            getDeliveryOption()
        }else{
            Helper.showTost(this@CheckoutActivity, getString(R.string.no_internet))
        }
    }

    private fun getDeliveryOption() {
        var api = MyApi(this@CheckoutActivity)
        val requestCall: Call<ResponseMainDeliveryOption> = api.getDeliveryOption()

        requestCall.enqueue(object : Callback<ResponseMainDeliveryOption> {
            override fun onResponse(
                call: Call<ResponseMainDeliveryOption>,
                response: Response<ResponseMainDeliveryOption>
            ) {
                Helper.dismissLoader()

                if (response.body() != null && response.body()?.result == true) {
                    recyclerDeliveryOption.layoutManager =
                        LinearLayoutManager(this@CheckoutActivity, LinearLayoutManager.VERTICAL, false)
                    var adapter = DeliveryOptionAdapter(
                        this@CheckoutActivity,
                        response.body()?.payload as ArrayList<ResponseMainDeliveryOption.Payload?>?
                    )
                    recyclerDeliveryOption.adapter = adapter
                } else {
                    Helper.showTost(
                        this@CheckoutActivity,
                        resources.getString(R.string.some_thing_happend_wrong)
                    )
                }
            }

            override fun onFailure(call: Call<ResponseMainDeliveryOption>, t: Throwable) {
                    Helper.dismissLoader()
            }

        })
    }

    private fun clickListener() {
        txtNext.setOnClickListener {
            if(deliveryOptionId != 0) {
                var intent =
                    Intent(this@CheckoutActivity, CheckoutDeliveryAddressActivity::class.java)
                intent.putExtra("deliveryOptionId",deliveryOptionId)
                startActivity(intent)
            }else{
                Helper.showTost(this@CheckoutActivity,"Please select your Delivery Option!")
            }
        }

        imgBack.setOnClickListener {
            finish()
        }

        txtSelectDeliverydate.setOnClickListener {
            var year : Int= Calendar.getInstance().get(Calendar.YEAR);
            var day : Int= Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            var month : Int= Calendar.getInstance().get(Calendar.MONTH);
            showDate(year, month, day, R.style.NumberPickerStyle);
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        txtSelectDeliverydate.text = year.toString()+"-"+(monthOfYear+1).toString()+"-"+dayOfMonth.toString()
    }

    private fun showDate(year: Int, monthOfYear: Int, dayOfMonth: Int, datePickerSpinner: Int) {
        SpinnerDatePickerDialogBuilder()
            .context(this@CheckoutActivity)
            .callback(this@CheckoutActivity)
            .spinnerTheme(datePickerSpinner)
            .defaultDate(year, monthOfYear, dayOfMonth)
            .build()
            .show()

        /*  val calendar: Calendar = GregorianCalendar(year, monthOfYear, dayOfMonth)
          txtBirthDate.text = simpleDateFormat!!.format(calendar.getTime())*/
    }

    fun setDeliveryOptionId(deliveryOptionId: Int?) {
        this.deliveryOptionId = deliveryOptionId!!
    }
}