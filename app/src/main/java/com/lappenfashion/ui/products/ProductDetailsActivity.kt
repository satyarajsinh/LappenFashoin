package com.lappenfashion.ui.products

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainLocalCart
import com.lappenfashion.data.model.ResponseMainProductsByCategory
import com.lappenfashion.sqlitedb.DBManager
import com.lappenfashion.ui.cart.CartActivity
import com.lappenfashion.ui.wishlist.WishListActivity
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.indicator
import kotlinx.android.synthetic.main.fragment_home.linearViewPager
import kotlinx.android.synthetic.main.fragment_home.mPager
import kotlinx.android.synthetic.main.toolbar_with_like_cart.*

class ProductDetailsActivity : AppCompatActivity(){

    private var productData: ResponseMainProductsByCategory.Payload.Data? = null
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
            if(Prefs.getString(Constants.PREF_IS_LOGGED_IN,"") == "1"){

            }else{
                addToCart()
            }
        }

        imgCart.setOnClickListener {
            var intent = Intent(this@ProductDetailsActivity,CartActivity::class.java)
            startActivity(intent)
        }

        imgLiked.setOnClickListener {
            var intent = Intent(this@ProductDetailsActivity,WishListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addToCart() {
        dbManager = DBManager(this)
        dbManager.open()

        Helper.showLoader(this@ProductDetailsActivity)
        Handler(Looper.getMainLooper()).postDelayed({
            var localCart = ResponseMainLocalCart(0,
                productData?.mainImageName!!, productData?.productId!!.toString(),"1",productData?.productName!!,productData?.mrp!!.toString())
            dbManager.insertCart(localCart)
            Helper.dismissLoader()
        }, 2000)

    }

    private fun initData() {
        if(intent!=null){
            productData = intent.getSerializableExtra("productDetail") as ResponseMainProductsByCategory.Payload.Data?
        }

        if (productData?.image != null && productData?.image?.size!! > 0) {
            linearViewPager.visibility = View.VISIBLE
            loadBanner(productData?.image)
        } else {
            linearViewPager.visibility = View.GONE
        }

        txtProductTitle.text = productData?.productName
        try {
            txtColor.setBackgroundColor(Color.parseColor(productData?.color!!))
        }catch (e : Exception){

        }

        txtDetails.text = productData?.description


        recyclerSize.layoutManager = LinearLayoutManager(
            this@ProductDetailsActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerSize.setHasFixedSize(true)
        var productSizeAdapter = ProductSizeAdapter(
            this@ProductDetailsActivity,
            productData?.size
        )
        recyclerSize.adapter = productSizeAdapter

    }

    companion object {
        lateinit var mPager: ViewPager
        private var currentPage = 0
        private var NUM_PAGES = 0
    }

    private fun loadBanner(body: List<ResponseMainProductsByCategory.Payload.Data.Image?>?) {
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
        private val imageModelArrayList: List<ResponseMainProductsByCategory.Payload.Data.Image?>?
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

            imageView.scaleType = ImageView.ScaleType.FIT_XY

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