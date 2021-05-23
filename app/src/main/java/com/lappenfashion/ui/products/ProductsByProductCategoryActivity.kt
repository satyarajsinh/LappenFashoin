package com.lappenfashion.ui.products

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemvvm.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainFilter
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
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.activity_products_by_product_category.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar_with_drawer.view.*
import kotlinx.android.synthetic.main.toolbar_with_like_cart.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.absoluteValue


class ProductsByProductCategoryActivity : AppCompatActivity() {

    private val selectedRangeValues: MutableList<Float> = arrayListOf()
    private var minPrice: Int? = 0
    private var maxPrice: Int? = 0
    private var selectedDiscount: Int = 0
    public var selectedColor: String = ""
    public var sortBy: String = ""
    public var orderBy: String = ""
    public var offerAmount: String = ""
    public var selectedSize: String = ""
    private var filterData: ResponseMainFilter.Payload? = null
    private lateinit var productsByCategoryAdapter: ProductsByCategoryAdapter
    private var productData: ArrayList<ResponseMainProductsByCategory.Payload.Data?>? =
        arrayListOf()
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
        Prefs.putString(Constants.PREFS_SEARCH_STRING, "")
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        Prefs.putString(Constants.PREFS_SEARCH_STRING, "")
    }

    private fun clickListener() {
        imgBack.setOnClickListener {
            Prefs.putString(Constants.PREFS_SEARCH_STRING, "")
            finish()
        }

        imgLiked.setOnClickListener {
            var intent = Intent(this, WishListActivity::class.java)
            startActivityForResult(intent,100)
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

        txtSort.setOnClickListener {
            displaySortBottomSheet()
        }

        txtFilter.setOnClickListener {
            displayFilter()
        }
    }

    private fun displayFilter() {

        val dialog = BottomSheetDialog(this@ProductsByProductCategoryActivity)
        dialog.setContentView(R.layout.bottom_sheet_filter)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        var recyclerSize = dialog.findViewById<RecyclerView>(R.id.recyclerSize)
        var recyclerColor = dialog.findViewById<RecyclerView>(R.id.recyclerColor)

        var txtSize = dialog.findViewById<TextView>(R.id.txtSize)
        var txtClearAll = dialog.findViewById<TextView>(R.id.txtClearAll)
        var txtColor = dialog.findViewById<TextView>(R.id.txtColor)
        var txtPrice = dialog.findViewById<TextView>(R.id.txtPrice)
        var txtDiscount = dialog.findViewById<TextView>(R.id.txtDiscount)
        var txtCancel = dialog.findViewById<TextView>(R.id.txtCancel)
        var txtApply = dialog.findViewById<TextView>(R.id.txtApply)
        var linearPrice = dialog.findViewById<LinearLayout>(R.id.linearPrice)
        var linearDiscount = dialog.findViewById<LinearLayout>(R.id.linearDiscount)
        var slider = dialog.findViewById<RangeSlider>(R.id.slider)
        var radioGroup = dialog.findViewById<RadioGroup>(R.id.discountGroup)
        var radio10Percent = dialog.findViewById<RadioButton>(R.id.radio10Percent)
        var radio20Percent = dialog.findViewById<RadioButton>(R.id.radio20Percent)
        var radio30Percent = dialog.findViewById<RadioButton>(R.id.radio30Percent)
        var radio40Percent = dialog.findViewById<RadioButton>(R.id.radio40Percent)
        var radio50Percent = dialog.findViewById<RadioButton>(R.id.radio50Percent)

        txtSize?.setBackgroundColor(Color.WHITE)
        recyclerSize!!.visibility = View.VISIBLE
        recyclerSize!!.layoutManager =
            LinearLayoutManager(
                this@ProductsByProductCategoryActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        recyclerSize.setHasFixedSize(true)
        var adapter =
            FilterSizeAdapter(this@ProductsByProductCategoryActivity, filterData?.size!!,selectedSize)
        recyclerSize.adapter = adapter

        if(selectedDiscount == 10){
            radio10Percent?.isChecked = true
        }else if(selectedDiscount == 20){
            radio20Percent?.isChecked = true
        }else if(selectedDiscount == 30){
            radio30Percent?.isChecked = true
        }else if(selectedDiscount == 40){
            radio40Percent?.isChecked = true
        }else if(selectedDiscount == 50){
            radio50Percent?.isChecked = true
        }else{

        }

        radioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                radio10Percent?.id -> {
                    Log.e("10 percent", "10 percent")
                    selectedDiscount = 10
                }
                radio20Percent?.id -> {
                    Log.e("20 percent", "20 percent")
                    selectedDiscount = 20
                }
                radio30Percent?.id -> {
                    Log.e("30 percent", "30 percent")
                    selectedDiscount = 30
                }
                radio40Percent?.id -> {
                    Log.e("40 percent", "40 percent")
                    selectedDiscount = 40
                }
                radio50Percent?.id -> {
                    Log.e("50 percent", "50 percent")
                    selectedDiscount = 50
                }
                else -> {
                    Log.e("percent", "percent")
                }
            }
        }

        txtApply?.setOnClickListener {
            dialog.dismiss()
            productData?.clear()
            offset = 1
            getProductsByProductCategory(offset, true)
        }

        txtClearAll?.setOnClickListener {
            minPrice = 0
            maxPrice = 0
            selectedDiscount = 0
            selectedColor = ""
            sortBy = ""
            orderBy = ""
            offerAmount = ""
            selectedSize = ""
            dialog.dismiss()
            productData?.clear()
            offset = 1
            getProductsByProductCategory(offset, true)
        }

        txtSize?.setOnClickListener {
            txtSize?.setBackgroundColor(resources.getColor(R.color.white))
            txtColor?.setBackgroundColor(resources.getColor(R.color.background))
            txtDiscount?.setBackgroundColor(resources.getColor(R.color.background))
            txtPrice?.setBackgroundColor(resources.getColor(R.color.background))

            recyclerColor?.visibility = View.GONE
            recyclerSize?.visibility = View.VISIBLE
            linearPrice?.visibility = View.GONE
            linearDiscount?.visibility = View.GONE

            recyclerSize!!.layoutManager =
                LinearLayoutManager(
                    this@ProductsByProductCategoryActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            recyclerSize.setHasFixedSize(true)
            var adapter =
                FilterSizeAdapter(
                    this@ProductsByProductCategoryActivity,
                    filterData?.size!!,
                    selectedSize
                )
            recyclerSize.adapter = adapter
        }

        txtColor?.setOnClickListener {
            txtSize?.setBackgroundColor(resources.getColor(R.color.background))
            txtColor?.setBackgroundColor(resources.getColor(R.color.white))
            txtDiscount?.setBackgroundColor(resources.getColor(R.color.background))
            txtPrice?.setBackgroundColor(resources.getColor(R.color.background))

            recyclerColor?.visibility = View.VISIBLE
            recyclerSize?.visibility = View.GONE
            linearPrice?.visibility = View.GONE
            linearDiscount?.visibility = View.GONE

            recyclerSize!!.visibility = View.GONE
            recyclerColor!!.visibility = View.VISIBLE
            recyclerColor!!.layoutManager =
                LinearLayoutManager(
                    this@ProductsByProductCategoryActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            recyclerColor.setHasFixedSize(true)
            var adapterColor =
                FilterColorAdapter(this@ProductsByProductCategoryActivity, filterData?.color!!,selectedColor)
            recyclerColor.adapter = adapterColor
        }

        txtPrice?.setOnClickListener {
            txtSize?.setBackgroundColor(resources.getColor(R.color.background))
            txtColor?.setBackgroundColor(resources.getColor(R.color.background))
            txtDiscount?.setBackgroundColor(resources.getColor(R.color.background))
            txtPrice?.setBackgroundColor(resources.getColor(R.color.white))

            recyclerColor?.visibility = View.GONE
            recyclerSize?.visibility = View.GONE
            linearPrice?.visibility = View.VISIBLE
            linearDiscount?.visibility = View.GONE
        }

        txtDiscount?.setOnClickListener {
            txtSize?.setBackgroundColor(resources.getColor(R.color.background))
            txtColor?.setBackgroundColor(resources.getColor(R.color.background))
            txtDiscount?.setBackgroundColor(resources.getColor(R.color.white))
            txtPrice?.setBackgroundColor(resources.getColor(R.color.background))

            recyclerColor?.visibility = View.GONE
            recyclerSize?.visibility = View.GONE
            linearPrice?.visibility = View.GONE
            linearDiscount?.visibility = View.VISIBLE
        }

        txtCancel?.setOnClickListener {
            dialog.dismiss()
        }

        slider?.setLabelFormatter(object : LabelFormatter {
            override fun getFormattedValue(value: Float): String {
                /*var currencyFormat = NumberFormat.getCurrencyInstance()
                currencyFormat.currency = Currency.getInstance("₹")*/
                var currency = "₹ $value"
                return currency
            }
        })

        if(minPrice!! > 0) {
            selectedRangeValues.clear()
            selectedRangeValues.add(minPrice!!.toFloat())
            selectedRangeValues.add(maxPrice!!.toFloat())
            slider?.setValues(selectedRangeValues)
        }

        slider?.addOnChangeListener { slider, value, fromUser ->
            minPrice = slider.values[0].absoluteValue.toInt()
            maxPrice = value.toInt()
            Log.e("min Price","min Price"+minPrice.toString())
            Log.e("max Price","max Price"+maxPrice.toString())
            Log.e("value Price","value Price"+value.toString())
        }



        dialog.show()
        Helper.dismissLoader()
    }

    private fun displaySortBottomSheet() {
        val dialog = BottomSheetDialog(this@ProductsByProductCategoryActivity)
        dialog.setContentView(R.layout.bottom_sheet_sort_by)

        var txtLow = dialog.findViewById<TextView>(R.id.txtLow)
        var txtHigh = dialog.findViewById<TextView>(R.id.txtHigh)
        var txtDiscountHigh = dialog.findViewById<TextView>(R.id.txtDiscountHigh)
        var txtDiscountLow = dialog.findViewById<TextView>(R.id.txtDiscountLow)

        txtLow?.setOnClickListener {
            orderBy = "sale_price"
            sortBy = "asc"
            productData?.clear()
            offset = 1
            getProductsByProductCategory(offset, true)
            dialog.dismiss()
        }

        txtHigh?.setOnClickListener {
            orderBy = "sale_price"
            sortBy = "desc"
            productData?.clear()
            offset = 1
            getProductsByProductCategory(offset, true)
            dialog.dismiss()
        }

        txtDiscountHigh?.setOnClickListener {
            orderBy = "discount"
            sortBy = "desc"
            productData?.clear()
            offset = 1
            getProductsByProductCategory(offset, true)
            dialog.dismiss()
        }

        txtDiscountLow?.setOnClickListener {
            orderBy = "discount"
            sortBy = "asc"
            productData?.clear()
            offset = 1
            getProductsByProductCategory(offset, true)
            dialog.dismiss()
        }

        dialog.show()
        Helper.dismissLoader()
    }

    override fun onResume() {
        super.onResume()
        if (Prefs.getInt(Constants.PREF_CART_COUNT, 0) > 0) {
            txtCartCount.visibility = View.VISIBLE
            txtCartCount.text = Prefs.getInt(Constants.PREF_CART_COUNT, 0).toString()
        } else {
            txtCartCount.visibility = View.GONE
        }
    }

    private fun initData() {
        txtTitle.visibility = View.VISIBLE

        txtTitle.text = Prefs.getString(Constants.PREF_PRODUCT_TITLE, "")


        if (Prefs.getInt(Constants.PREF_CART_COUNT, 0) > 0) {
            txtCartCount.visibility = View.VISIBLE
            txtCartCount.text = Prefs.getInt(Constants.PREF_CART_COUNT, 0).toString()
        } else {
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
            var spaceIterator: SpacesItemDecoration = SpacesItemDecoration(10)
            recyclerProductsByProductCategory.addItemDecoration(spaceIterator);
            recyclerProductsByProductCategory.setHasFixedSize(true)

            productsByCategoryAdapter = ProductsByCategoryAdapter(
                this@ProductsByProductCategoryActivity,
                productData
            )
            recyclerProductsByProductCategory.adapter = productsByCategoryAdapter
            onLoadMoreProduct(recyclerProductsByProductCategory)
        } else {
            Helper.showTost(this, "No internet connection")
        }

        if (NetworkConnection.checkConnection(this)) {
            getFilterData()
        }
    }

    private fun getFilterData() {
        var api = MyApi(this)
        val requestCall: Call<ResponseMainFilter> = api.getFilter()

        requestCall.enqueue(object : Callback<ResponseMainFilter> {
            override fun onResponse(
                call: Call<ResponseMainFilter>,
                response: Response<ResponseMainFilter>
            ) {
                if (response.body() != null && response.body()!!.result == true) {
                    filterData = response.body()!!.payload
                } else {
                    var message = Helper.getErrorBodyMessage(
                        this@ProductsByProductCategoryActivity,
                        response.errorBody()
                    )
                    Helper.showTost(this@ProductsByProductCategoryActivity, message)
                }
            }

            override fun onFailure(call: Call<ResponseMainFilter>, t: Throwable) {
                Helper.dismissLoader()
                Helper.showTost(this@ProductsByProductCategoryActivity, t.message)
            }

        })
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

        offerAmount = Prefs.getString(Constants.PREF_OFFER_AMOUNT,"")

        val requestCall: Call<JsonObject>
        var api = MyApi(this)
        if (Prefs.getString(Constants.PREFS_SEARCH_STRING, "") == "") {
            requestCall = api.getProducts(
                Constants.END_POINT_PRODUCTS + "?page=" + offset + "&category_id=" + Prefs.getString(
                    Constants.PREF_CATEGORY_ID,
                    ""
                ) + "&sub_category_id=" + Prefs.getString(
                    Constants.PREF_SUB_CATEGORY_ID,
                    ""
                ) + "&product_category_id=" + Prefs.getString(
                    Constants.PREF_PRODUCT_CATEGORY_ID,
                    ""
                ) + "&user_id=" + Prefs.getInt(Constants.PREF_USER_ID, 0)
                    .toString() + "&search=" + "&size=" + selectedSize + "&color_code=" + selectedColor + "&min_price=" + minPrice.toString() + "&max_price=" + maxPrice.toString() + "&discount=" + selectedDiscount.toString() + "&soft_by=" + orderBy + "&order_by=" + sortBy + "&offer_amount=" + offerAmount
            )
        } else {
            requestCall = api.getProducts(
                Constants.END_POINT_PRODUCTS + "?page=" + offset + "&category_id=&sub_category_id=&product_category_id=&user_id=" + Prefs.getInt(
                    Constants.PREF_USER_ID,
                    0
                ).toString() + "&search=" + Prefs.getString(Constants.PREFS_SEARCH_STRING, "")
            )
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
                    if (productsResponse.result == true && productsResponse.payload?.data!!.size > 0) {
                        productData?.addAll(productsResponse.payload?.data!!)
                        productsByCategoryAdapter.notifyDataSetChanged()
                    } else {
                        if (b) {
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
                        } else {
                            var message = Helper.getErrorBodyMessage(
                                this@ProductsByProductCategoryActivity,
                                response.errorBody()
                            )
                            Helper.showTost(this@ProductsByProductCategoryActivity, message)
                        }
                    }

                    override fun onFailure(call: Call<ResponseMainLogin>, t: Throwable) {
                        Helper.dismissLoader()
                    }

                })
            } else {
                Helper.showTost(
                    this@ProductsByProductCategoryActivity,
                    getString(R.string.no_internet)
                )
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
                        var intent =
                            Intent(this@ProductsByProductCategoryActivity, OTPActivity::class.java)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
    }
}