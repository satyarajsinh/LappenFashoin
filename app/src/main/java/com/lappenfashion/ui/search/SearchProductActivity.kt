package com.lappenfashion.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplemvvm.utils.Constants
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainSearchProduct
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.ui.products.ProductsByProductCategoryActivity
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_search_product.*
import kotlinx.android.synthetic.main.activity_search_product.relativeNoData
import kotlinx.android.synthetic.main.activity_search_product.txtStartShopping
import kotlinx.android.synthetic.main.activity_wishlist.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.toolbar_with_drawer.view.*
import kotlinx.android.synthetic.main.toolbar_with_like_cart.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_product)

        initData()
        clickListener()
    }

    private fun clickListener() {
        imgClear.setOnClickListener {
            edtSearch.setText("")
            recyclerProductList.visibility =View.GONE
        }

        imgBackSearch.setOnClickListener {
            finish()
        }

        txtStartShopping.setOnClickListener {
            finish()
        }
    }

    private fun initData() {
        edtSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0?.length!! > 2 && p3 > 2){
                    imgClear.visibility= View.VISIBLE
                    Helper.showLoader(this@SearchProductActivity)
                    searchProduct(edtSearch.text.toString())
                }

                if(p3 == 0){
                    imgClear.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun searchProduct(search :String){
        var api = MyApi(this@SearchProductActivity)
        val requestCall: Call<ResponseMainSearchProduct> = api.searchProduct(Constants.END_POINT_SEARCH_PRODUCT+search)

        requestCall.enqueue(object : Callback<ResponseMainSearchProduct> {
            override fun onResponse(
                call: Call<ResponseMainSearchProduct>,
                response: Response<ResponseMainSearchProduct>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()!!.payload?.size!! > 0) {
                    recyclerProductList.visibility = View.VISIBLE
                    relativeNoData.visibility = View.GONE
                    recyclerProductList.layoutManager =
                        LinearLayoutManager(this@SearchProductActivity, LinearLayoutManager.VERTICAL, false)
                    var adapter = SearchProductAdapter(this@SearchProductActivity,response.body()!!.payload)
                    recyclerProductList.adapter = adapter
                }else{
                    recyclerProductList.visibility = View.GONE
                    relativeNoData.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ResponseMainSearchProduct>, t: Throwable) {
                recyclerProductList.visibility = View.GONE
                relativeNoData.visibility = View.VISIBLE
                Helper.dismissLoader()
                Helper.showTost(this@SearchProductActivity,t.message)
            }
        })
    }

    fun goToProductListing(productName: String?) {
        Prefs.putString(Constants.PREFS_SEARCH_STRING,edtSearch.text.toString())
        var intent = Intent(this@SearchProductActivity,ProductsByProductCategoryActivity::class.java)
        startActivity(intent)
    }
}