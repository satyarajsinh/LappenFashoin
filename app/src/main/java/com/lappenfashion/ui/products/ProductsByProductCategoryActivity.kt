package com.lappenfashion.ui.products

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemvvm.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainLogin
import com.lappenfashion.data.model.ResponseMainProductsByCategory
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.ui.MainActivity
import com.lappenfashion.ui.cart.CartActivity
import com.lappenfashion.ui.otp.OTPActivity
import com.lappenfashion.ui.wishlist.WishListActivity
import com.lappenfashion.utils.Helper
import com.lappenfashion.utils.SpacesItemDecoration
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_products_by_product_category.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar_with_drawer.view.*
import kotlinx.android.synthetic.main.toolbar_with_like_cart.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsByProductCategoryActivity : AppCompatActivity() {

    private lateinit var productsByCategoryAdapter: ProductsByCategoryAdapter
    private var productData: ArrayList<ResponseMainProductsByCategory.Payload.Data?>? = arrayListOf()
    private var isScrolling = false
    private var offset = 1
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var pastVisiblesItems = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_by_product_category)

        initData()
        clickListener()
    }

    override fun onBackPressed() {
        Prefs.putString(Constants.PREFS_SEARCH_STRING,"")
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        Prefs.putString(Constants.PREFS_SEARCH_STRING,"")
    }

    private fun clickListener() {
        imgBack.setOnClickListener {
            Prefs.putString(Constants.PREFS_SEARCH_STRING,"")
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

        txtShopping.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if(Prefs.getInt(Constants.PREF_CART_COUNT,0) > 0){
            txtCartCount.visibility = View.VISIBLE
            txtCartCount.text = Prefs.getInt(Constants.PREF_CART_COUNT,0).toString()
        }else{
            txtCartCount.visibility = View.GONE
        }
    }
    private fun initData() {
        txtTitle.visibility = View.VISIBLE

        txtTitle.text = Prefs.getString(Constants.PREF_PRODUCT_TITLE,"")


        if(Prefs.getInt(Constants.PREF_CART_COUNT,0) > 0){
            txtCartCount.visibility = View.VISIBLE
            txtCartCount.text = Prefs.getInt(Constants.PREF_CART_COUNT,0).toString()
        }else{
            txtCartCount.visibility = View.GONE
        }

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
            Helper.showLoader(this@ProductsByProductCategoryActivity)
        } else {
            progressBar.setVisibility(View.VISIBLE)
        }

        val requestCall: Call<JsonObject>
        var api = MyApi(this)
        if(Prefs.getString(Constants.PREFS_SEARCH_STRING,"") == ""){
            requestCall = api.getProducts(Constants.END_POINT_PRODUCTS + "?page=" + offset + "&category_id="+Prefs.getString(Constants.PREF_CATEGORY_ID,"")+"&sub_category_id="+Prefs.getString(Constants.PREF_SUB_CATEGORY_ID,"")+"&product_category_id=" + Prefs.getString(Constants.PREF_PRODUCT_CATEGORY_ID,"")+"&user_id="+Prefs.getInt(Constants.PREF_USER_ID,0).toString()+"&search=")
        }else{
            requestCall = api.getProducts(Constants.END_POINT_PRODUCTS + "?page=" + offset + "&category_id=&sub_category_id=&product_category_id=&user_id="+Prefs.getInt(Constants.PREF_USER_ID,0).toString()+"&search="+Prefs.getString(Constants.PREFS_SEARCH_STRING,""))
        }

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
                    recyclerProductsByProductCategory.visibility = View.VISIBLE
                    if(productsResponse.result == true && productsResponse.payload?.data!!.size > 0){
                        productData?.addAll(productsResponse.payload?.data!!)
                        productsByCategoryAdapter.notifyDataSetChanged()
                    }else{
                        if(b){
                            recyclerProductsByProductCategory.visibility = View.GONE
                            linearNoCart.visibility = View.VISIBLE
                        }
                    }
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
        intent.putExtra("productId", data?.productId)
        startActivity(intent)
    }

    fun addToWishList(productId: Int?, position: Int) {
        if (Prefs.getString(Constants.PREF_IS_LOGGED_IN, "") == "1") {
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
                            productsByCategoryAdapter.setWishLIst(position)
                            Helper.showTost(
                                this@ProductsByProductCategoryActivity,
                                response.body()!!.message!!
                            )
                        }else{
                            var message = Helper.getErrorBodyMessage(this@ProductsByProductCategoryActivity,response.errorBody())
                            Helper.showTost(this@ProductsByProductCategoryActivity,message)
                        }
                    }

                    override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                        Helper.dismissLoader()
                    }

                })
            } else {
                Helper.showTost(this@ProductsByProductCategoryActivity, getString(R.string.no_internet))
            }
        } else {
            displayLoginDialog()
        }
    }

    private fun displayLoginDialog() {
        val dialog = BottomSheetDialog(this@ProductsByProductCategoryActivity)
        dialog.setContentView(R.layout.bottom_sheet_login)
        dialog.setCanceledOnTouchOutside(false)

        val imgClose = dialog.findViewById<View>(R.id.imgClose) as ImageView?
        val txtLogin = dialog.findViewById<View>(R.id.txtLogin) as TextView?
        val edtMobileNumber = dialog.findViewById<View>(R.id.edtPhoneNumber) as EditText?

        txtLogin!!.setOnClickListener {
            if (edtMobileNumber?.text.toString() != "") {
                if (NetworkConnection.checkConnection(this@ProductsByProductCategoryActivity)) {
                    txtLogin.isEnabled = false
                    Helper.showLoader(this@ProductsByProductCategoryActivity)
                    loginData(edtMobileNumber?.text.toString(), txtLogin, dialog)
                } else {
                    Helper.showTost(
                        this@ProductsByProductCategoryActivity,
                        getString(R.string.no_internet)
                    )
                }
            } else {
                Helper.showTost(this@ProductsByProductCategoryActivity, "Field is required")
            }

        }


        imgClose!!.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun loginData(mobileNumber: String, txtLogin: TextView, dialog: BottomSheetDialog) {
        var api = MyApi(this@ProductsByProductCategoryActivity)
        val requestCall: Call<ResponseMainLogin> = api.login(mobileNumber)

        requestCall.enqueue(object : Callback<ResponseMainLogin> {
            override fun onResponse(
                call: Call<ResponseMainLogin>,
                response: Response<ResponseMainLogin>
            ) {
                com.lappenfashion.utils.Helper.dismissLoader()

                if (response.body() != null) {
                    if (response.body()?.result == true) {
                        dialog.dismiss()
                        com.lappenfashion.utils.Helper.showTost(
                            this@ProductsByProductCategoryActivity,
                            response.body()?.message!!
                        )
                        var intent = Intent(this@ProductsByProductCategoryActivity, OTPActivity::class.java)
                        intent.putExtra("mobile_number", mobileNumber)
                        startActivity(intent)
                    }
                } else {
                    txtLogin.isEnabled = true
                    com.lappenfashion.utils.Helper.showTost(
                        this@ProductsByProductCategoryActivity,
                        resources.getString(R.string.some_thing_happend_wrong)
                    )
                }
            }

            override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                txtLogin.isEnabled = true
                com.lappenfashion.utils.Helper.dismissLoader()
            }

        })
    }

}