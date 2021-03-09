package com.lappenfashion.ui.cart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseCart
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity() {

    private var cartList : ArrayList<ResponseCart> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        var list1 = ResponseCart("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MXx8cHJvZHVjdHxlbnwwfHwwfA%3D%3D&ixlib=rb-1.2.1&w=1000&q=80","Scarlet Headphone","$1100")
        var list2 = ResponseCart("https://images.unsplash.com/photo-1523275335684-37898b6baf30?ixid=MXwxMjA3fDB8MHxzZWFyY2h8M3x8cHJvZHVjdHxlbnwwfHwwfA%3D%3D&ixlib=rb-1.2.1&w=1000&q=80","Apple Watch","$5000")
        var list3 = ResponseCart("https://ca.binus.ac.id/files/2014/05/Chandra-Barli-160126941.jpg","Adidas Perfume","$50")

        cartList.add(list1)
        cartList.add(list2)
        cartList.add(list3)

        recyclerCart.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        var adapter = CartAdapter(
            this@CartActivity,
            cartList
        )
        recyclerCart.adapter = adapter

        clickListeners()
    }

    private fun clickListeners() {
        imgBack.setOnClickListener {
            finish()
        }
    }
}