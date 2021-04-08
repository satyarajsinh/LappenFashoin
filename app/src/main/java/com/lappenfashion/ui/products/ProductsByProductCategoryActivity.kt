package com.lappenfashion.ui.products

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.simplemvvm.utils.Constants
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainProductsByCategory
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.utils.Helper
import com.lappenfashion.utils.SpacesItemDecoration
import kotlinx.android.synthetic.main.activity_products_by_product_category.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar_with_like_cart.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsByProductCategoryActivity : AppCompatActivity() {

    private var pageTitle: String? = ""
    private var productCategoryId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_by_product_category)

        txtTitle.visibility = View.VISIBLE

        if(intent!=null){
            productCategoryId = intent.getStringExtra("id")
            pageTitle = intent.getStringExtra("name")
            txtTitle.text = pageTitle
        }

        if (NetworkConnection.checkConnection(this)) {
            Helper.showLoader(this@ProductsByProductCategoryActivity)!!
            getProductsByProductCategory()
        }else {
            Helper.showTost(this, "No internet connection")
        }

        imgBack.setOnClickListener {
            finish()
        }
    }

    private fun getProductsByProductCategory() {
        var api = MyApi(this)
        val requestCall: Call<JsonObject> = api.getProducts(Constants.END_POINT_PRODUCTS+"?page=1&product_category_id="+productCategoryId)

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                Helper.dismissLoader()
                if (response.body() != null) {
                    val gson = Gson()
                    val productsResponse: ResponseMainProductsByCategory = gson.fromJson(
                        response.body(),
                        ResponseMainProductsByCategory::class.java
                    )

                    recyclerProductsByProductCategory.setLayoutManager(
                        GridLayoutManager(
                            this@ProductsByProductCategoryActivity,
                            2
                        )
                    )

                    var spaceIterator : SpacesItemDecoration = SpacesItemDecoration(10)
                    recyclerProductsByProductCategory.addItemDecoration(spaceIterator);
                    recyclerProductsByProductCategory.setHasFixedSize(true)

                    var productsByCategoryAdapter = ProductsByCategoryAdapter(
                        this@ProductsByProductCategoryActivity,
                        productsResponse.payload?.data
                    )
                    recyclerProductsByProductCategory.adapter = productsByCategoryAdapter

                } else {
                    Helper.showTost(this@ProductsByProductCategoryActivity, "No Data Found")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Helper.dismissLoader()
            }

        })
    }

    fun goToDetails(data: ResponseMainProductsByCategory.Payload.Data?) {
        var intent = Intent(this@ProductsByProductCategoryActivity,ProductDetailsActivity::class.java)
        intent.putExtra("productDetail",data)
        startActivity(intent)
    }
}