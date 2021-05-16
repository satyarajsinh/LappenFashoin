package com.lappenfashion.ui.products

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.simplemvvm.utils.Constants
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lappenfashion.R
import com.lappenfashion.data.model.*
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.sqlitedb.DBManager
import com.lappenfashion.ui.MainActivity
import com.lappenfashion.ui.cart.CartActivity
import com.lappenfashion.ui.wishlist.WishListActivity
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.indicator
import kotlinx.android.synthetic.main.fragment_home.linearViewPager
import kotlinx.android.synthetic.main.fragment_home.mPager
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.toolbar_with_like_cart.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var productData: ResponseMainProductDetails.Payload
    private lateinit var dbManager: DBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        initData()
        clickListener()
        imgBack.setOnClickListener {
            finish()
        }
    }

    private fun clickListener() {
        linearAddToBag.setOnClickListener {
            if (Prefs.getString(Constants.PREF_IS_LOGGED_IN, "") == "1") {
                addToCartMain(productData)
            } else {
                addToCart()
            }
        }

        imgCart.setOnClickListener {
            var intent = Intent(this@ProductDetailsActivity, CartActivity::class.java)
            startActivity(intent)
        }

        imgLiked.setOnClickListener {
            var intent = Intent(this@ProductDetailsActivity, WishListActivity::class.java)
            startActivity(intent)
        }

        txtGoToHome.setOnClickListener {
            var intent = Intent(this@ProductDetailsActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun addToCartMain(data: ResponseMainProductDetails.Payload?) {
        if (NetworkConnection.checkConnection(this@ProductDetailsActivity)) {
            val array = JsonArray()
            var cartObject = JsonObject()
            cartObject.addProperty("product_id", data?.productId.toString())
            cartObject.addProperty("quantity", "1")
            cartObject.addProperty("amount", data?.salePrice.toString())
            array.add(cartObject)

            Log.e("json Array", "json Array" + array.toString())

            Helper.showLoader(this@ProductDetailsActivity)
            var api = MyApi(this@ProductDetailsActivity)
            val requestCall: Call<ResponseMainCartNew> =
                api.addCart("Bearer " + Prefs.getString(Constants.PREF_TOKEN, ""), array)

            requestCall.enqueue(object : Callback<ResponseMainCartNew> {
                override fun onResponse(
                    call: Call<ResponseMainCartNew>,
                    response: Response<ResponseMainCartNew>
                ) {
                    Helper.dismissLoader()
                    if (response.body() != null && response.body()!!.result == true) {
                        Prefs.putInt(
                            Constants.PREF_CART_COUNT,
                            response.body()!!.payload?.cartCount!!
                        )
                        txtCartCount.visibility = View.VISIBLE
                        txtCartCount.text = response.body()!!.payload?.cartCount.toString()
                        Helper.showTost(this@ProductDetailsActivity, response.body()!!.message!!)
                    } else {
                        Helper.showTost(
                            this@ProductDetailsActivity,
                            "Something happend wrong, Please try again later"
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseMainCartNew>, t: Throwable) {
                    Helper.dismissLoader()
                }

            })
        } else {
            Helper.showTost(this@ProductDetailsActivity, getString(R.string.no_internet))
        }
    }

    private fun addToCart() {
        dbManager = DBManager(this)
        dbManager.open()

        Helper.showLoader(this@ProductDetailsActivity)
        Handler(Looper.getMainLooper()).postDelayed({
            var localCart = ResponseMainLocalCart(
                0,
                productData?.mainImageName!!,
                productData?.productId!!.toString(),
                "1",
                productData?.productName!!,
                productData?.salePrice!!.toString(),productData?.size!!.toString(),productData.colorCode.toString()
            )
            dbManager.insertCart(localCart)

            var count = Prefs.getInt(Constants.PREF_CART_COUNT, 0) + 1
            txtCartCount.visibility = View.VISIBLE
            txtCartCount.text = count.toString()
            Prefs.putInt(Constants.PREF_CART_COUNT, count)

            Helper.showTost(this@ProductDetailsActivity, "Product added to cart")
            Helper.dismissLoader()
        }, 2000)

    }

    private fun initData() {
        Prefs.putInt(Constants.PREF_SELECTED_COLOR,0)
        if (intent != null) {
            var productId = intent.getIntExtra("productId", 0)
            if (NetworkConnection.checkConnection(this@ProductDetailsActivity)) {
                Helper.showLoader(this@ProductDetailsActivity)
                getProductDetailsById(productId)
            } else {
                Helper.showTost(
                    this@ProductDetailsActivity,
                    resources.getString(R.string.no_internet)
                )
            }
        }

        if(Prefs.getInt(Constants.PREF_CART_COUNT, 0) == 0){
            txtCartCount.visibility = View.GONE
        }else{
            txtCartCount.visibility = View.VISIBLE
            txtCartCount.text = Prefs.getInt(Constants.PREF_CART_COUNT, 0).toString()
        }
    }

    fun getProductDetailsById(productId: Int) {
        Log.e("product id","product id"+productId.toString())
        Log.e("user id","userid"+Prefs.getInt(
            Constants.PREF_USER_ID,
            0
        ).toString())

        var api = MyApi(this@ProductDetailsActivity)
        val requestCall : Call<ResponseMainProductDetails> = api.getProductDetails(
            Constants.END_POINT_PRODUCTS + "/" + productId + "?user_id" + Prefs.putInt(
                Constants.PREF_USER_ID,
                0
            )
        )

        requestCall.enqueue(object : Callback<ResponseMainProductDetails> {
            override fun onResponse(
                call: Call<ResponseMainProductDetails>,
                response: Response<ResponseMainProductDetails>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()?.payload != null) {
                    coordinator.visibility = View.VISIBLE
                    linearBottom.visibility = View.VISIBLE
                    linearNoData.visibility = View.GONE
                    productData = response.body()?.payload!!
                    if (response.body()?.payload!!.image != null && response.body()?.payload?.image?.size!! > 0) {
                        linearViewPager.visibility = View.VISIBLE
                        loadBanner(response.body()?.payload!!.image)
                    } else {
                        linearViewPager.visibility = View.GONE
                    }

                    txtProductTitle.text = response.body()?.payload!!.productName
                    txtProductMrp.text = "₹"+response.body()?.payload!!.mrp.toString()
                    txtProductMrp.setPaintFlags(txtProductMrp.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                    txtProductSalePrice.text = "₹"+response.body()?.payload!!.salePrice.toString()
                    txtSizeFit.text = response.body()?.payload!!.brandFit.toString()
                    txtMaterialCare.text = response.body()?.payload!!.material.toString()
                    txtStyleNote.text = response.body()?.payload!!.pocketStyle.toString()

                    /*try {
                        txtColor.setBackgroundColor(Color.parseColor(productData?.color!!))
                    } catch (e: Exception) {

                    }*/

                    txtDetails.text = response.body()?.payload!!.description

                    recyclerSize.layoutManager = LinearLayoutManager(
                        this@ProductDetailsActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    recyclerSize.setHasFixedSize(true)
                    var productSizeAdapter = ProductSizeAdapter(
                        this@ProductDetailsActivity,
                        response.body()?.payload!!.sizeList as List<ResponseMainProductDetails.Payload.Size>,
                        productId
                    )
                    recyclerSize.adapter = productSizeAdapter


                    recyclerColor.layoutManager = LinearLayoutManager(
                        this@ProductDetailsActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    recyclerColor.setHasFixedSize(true)

                    var colorList : ArrayList<ResponseMainProductDetails.Payload.Color?> = arrayListOf()

                    var flag = 0
                    for(i in response.body()?.payload!!.colorList!!.indices){
                        if(colorList.size > 0){
                            for(j in colorList.indices){
                                if(colorList.get(j)?.color == response.body()?.payload!!.colorList!!.get(i)!!.color){
                                    flag = 0
                                    break
                                }else{
                                    flag = 1
                                }
                            }
                            if(flag == 1){
                                colorList.add(response.body()?.payload!!.colorList!![i])
                            }
                        }else{
                            colorList.add(response.body()?.payload!!.colorList!![i])
                        }
                    }

                    var productColorAdapter = ProductColorAdapter(
                        this@ProductDetailsActivity,
                        colorList,productId
                    )
                    recyclerColor.adapter = productColorAdapter
                } else {
                    coordinator.visibility = View.GONE
                    linearBottom.visibility = View.GONE
                    linearNoData.visibility = View.VISIBLE
                }

            }

            override fun onFailure(call: Call<ResponseMainProductDetails>, t: Throwable) {
                txtNoDataFound.setText(resources.getString(R.string.no_data_found))
                linearNoData.visibility = View.VISIBLE
                Helper.dismissLoader()
            }

        })
    }

    companion object {
        lateinit var mPager: ViewPager
        private var currentPage = 0
        private var NUM_PAGES = 0
    }

    private fun loadBanner(body: List<ResponseMainProductDetails.Payload.Image?>?) {
        mPager!!.adapter = SlidingImageAdapter(this@ProductDetailsActivity, body)

        indicator.setViewPager(mPager)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator.setRadius(5 * density)

        NUM_PAGES = body?.size!!

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            mPager!!.setCurrentItem(currentPage++, true)
        }
        /*  val swipeTimer = Timer()
          swipeTimer.schedule(object : TimerTask() {
              override fun run() {
                  handler.post(Update)
              }
          }, 3000, 3000)*/

        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPage = position

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })
    }

    class SlidingImageAdapter(
        private val mContext: Context,
        private val imageModelArrayList: List<ResponseMainProductDetails.Payload.Image?>?
    ) : PagerAdapter() {

        private val inflater: LayoutInflater = LayoutInflater.from(mContext)

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(view: ViewGroup, position: Int): Any {
            val imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false)!!

            val imageView = imageLayout
                .findViewById(R.id.image) as ImageView

            val txtBannerTitle = imageLayout
                .findViewById(R.id.bannerTitle) as TextView

            imageView.setImageResource(0)

            Glide.with(mContext)
                .load(imageModelArrayList?.get(position)?.image)
                .into(imageView)

//            txtBannerTitle.text = imageModelArrayList?.get(position)?.image

            view.addView(imageLayout, 0)

            return imageLayout
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return imageModelArrayList?.size!!
        }
    }
}