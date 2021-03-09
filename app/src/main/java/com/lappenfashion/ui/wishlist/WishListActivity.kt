package com.lappenfashion.ui.wishlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseCart
import com.lappenfashion.data.model.ResponseWishlist
import com.lappenfashion.ui.cart.CartAdapter
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.imgBack
import kotlinx.android.synthetic.main.activity_cart.recyclerCart
import kotlinx.android.synthetic.main.activity_wishlist.*

class WishListActivity : AppCompatActivity() {

    private var wishList : ArrayList<ResponseWishlist> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)

        clicklisteners()

        var list1 = ResponseWishlist("https://rukminim1.flixcart.com/image/880/1056/kf1fo280hlty2aw-0/t-shirt/w/s/e/-original-imafdfvvr8hqdu65.jpeg?q=50","Scarlet Headphone","Best product","1100")
        var list2 = ResponseWishlist("https://rukminim1.flixcart.com/image/580/696/kjom6q80-0/t-shirt/f/j/b/l-aff-1048black-adiba-fashion-factory-original-imafz6uenneexbf7.jpeg?q=50","Apple Watch","Best product","5000")
        var list3 = ResponseWishlist("https://rukminim1.flixcart.com/image/580/696/kg9qbgw0-0/t-shirt/c/0/7/s-shp395402-shapphr-original-imafwjx7tnbqqqhz.jpeg?q=50","Adidas Perfume","Best product","50")

        wishList.add(list1)
        wishList.add(list2)
        wishList.add(list3)

        recyclerWishList.layoutManager = GridLayoutManager(this@WishListActivity, 2)
        var adapter = WishlistAdapter(
            this@WishListActivity,
            wishList
        )
        recyclerWishList.adapter = adapter

    }

    private fun clicklisteners() {
        imgBack.setOnClickListener {
            finish()
        }
    }
}