package com.lappenfashion.ui.products

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemvvm.utils.Constants
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainLogin
import com.lappenfashion.data.model.ResponseMainProductsByCategory
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.ui.cart.CartActivity
import com.lappenfashion.ui.wishlist.WishListActivity
import com.lappenfashion.utils.Helper
import com.lappenfashion.utils.SpacesItemDecoration
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_products_by_product_category.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar_with_like_cart.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsByProductCategoryActivity : AppCompatActivity() {

    private lateinit var productsByCategoryAdapter: ProductsByCategoryAdapter
    private var productData: ArrayList<ResponseMainProductsByCategory.Payload.Data?>? = arrayListOf()
    private var isScrolling = false
    private var offset = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var pastVisiblesItems = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_by_product_category)

        initData()
        clickListener()
    }

    private fun clickListener() {
        imgBack.setOnClickListener {
            finish()
        }

        imgLiked.setOnClickListener {
            var intent = Intent(this, WishListActivity::class.java)
            startActivity(intent)
        }

        imgCart.setOnClickListener {
            var intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initData() {
        txtTitle.visibility = View.VISIBLE

        txtTitle.text = Prefs.getString(Constants.PREF_PRODUCT_TITLE,"")
        if (NetworkConnection.checkConnection(this)) {
            getProductsByProductCategory(offset, true)
            recyclerProductsByProductCategory.setLayoutManager(
                GridLayoutManager(
                    this@ProductsByProductCategoryActivity,
                    2
                )
            )
            var spaceIterator : SpacesItemDecoration = SpacesItemDecoration(10)
            recyclerProductsByProductCategory.addItemDecoration(spaceIterator);
            recyclerProductsByProductCategory.setHasFixedSize(true)

            productsByCategoryAdapter = ProductsByCategoryAdapter(
                this@ProductsByProductCategoryActivity,
                productData
            )
            recyclerProductsByProductCategory.adapter = productsByCategoryAdapter
            onLoadMoreProduct(recyclerProductsByProductCategory)
        }else {
            Helper.showTost(this, "No internet connection")
        }
    }

    private fun onLoadMoreProduct(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    visibleItemCount = recyclerView.layoutManager!!.childCount
                    totalItemCount = recyclerView.layoutManager!!.itemCount
                    pastVisiblesItems =
                        (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
                    if (isScrolling && visibleItemCount + pastVisiblesItems >= totalItemCount) {
                        isScrolling = false
                        offset = offset + 1
                        getProductsByProductCategory(offset, false)
                    }
                }
            }
        })
    }

    private fun getProductsByProductCategory(offset: Int, b: Boolean) {
        if (b) {
            Helper.showLoader(this)
        } else {
            progressBar.setVisibility(View.VISIBLE)
        }

        var api = MyApi(this)
        val requestCall: Call<JsonObject> = api.getProducts(Constants.END_POINT_PRODUCTS + "?page=" + offset + "&category_id="+Prefs.getString(Constants.PREF_CATEGORY_ID,"")+"sub_category_id="+Prefs.getString(Constants.PREF_SUB_CATEGORY_ID,"")+"product_category_id=" + Prefs.getString(Constants.PREF_PRODUCT_CATEGORY_ID,""))

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
                    productData?.addAll(productsResponse.payload?.data!!)
                    productsByCategoryAdapter.notifyDataSetChanged()
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
        var intent = Intent(
            this@ProductsByProductCategoryActivity,
            ProductDetailsActivity::class.java
        )
        intent.putExtra("productDetail", data)
        startActivity(intent)
    }

    fun addToWishList(productId: Int?) {
        if (NetworkConnection.checkConnection(this@ProductsByProductCategoryActivity)) {
            Helper.showLoader(this@ProductsByProductCategoryActivity)
            var api = MyApi(this)
            val requestCall: Call<ResponseMainLogin> =
                api.addToWishList(
                    "Bearer " + Prefs.getString(Constants.PREF_TOKEN, ""),
                    productId.toString()
                )

            requestCall.enqueue(object : Callback<ResponseMainLogin> {
                override fun onResponse(
                    call: Call<ResponseMainLogin>,
                    response: Response<ResponseMainLogin>
                ) {
                    Helper.dismissLoader()
                    if (response.body() != null && response.body()!!.result == true) {
                        Helper.showTost(
                            this@ProductsByProductCategoryActivity,
                            response.body()!!.message!!
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                    Helper.dismissLoader()
                }

            })
        }else{
            Helper.showTost(this@ProductsByProductCategoryActivity, getString(R.string.no_internet))
        }
    }
}