package com.lappenfashion.ui.categoriesDetails

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.simplemvvm.utils.Constants
import com.github.ybq.android.spinkit.SpinKitView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainHome
import com.lappenfashion.ui.cart.CartActivity
import com.lappenfashion.ui.products.ProductsByProductCategoryActivity
import com.lappenfashion.ui.wishlist.WishListActivity
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_categories_details.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.recyclerCategories
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.toolbar_with_drawer.view.*
import kotlinx.android.synthetic.main.toolbar_with_like_cart.*

class CategoriesDetailsActivity : AppCompatActivity() {

    lateinit var subCategoriesData: ResponseMainHome.Payload.Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_details)

        initData()
        clickListener()

    }

    override fun onResume() {
        super.onResume()
        if (Prefs.getInt(Constants.PREF_CART_COUNT, 0) > 0) {
            txtCartCount.visibility = View.VISIBLE
            txtCartCount.text = Prefs.getInt(Constants.PREF_CART_COUNT, 0).toString()
        }else{
            txtCartCount.visibility = View.GONE
        }
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

        if (Prefs.getInt(Constants.PREF_CART_COUNT, 0) > 0) {
            txtCartCount.visibility = View.VISIBLE
            txtCartCount.text = Prefs.getInt(Constants.PREF_CART_COUNT, 0).toString()
        }else{
            txtCartCount.visibility = View.GONE
        }


        if (intent != null) {
            subCategoriesData =
                intent.getSerializableExtra("subCategories") as ResponseMainHome.Payload.Category
        }

        if (subCategoriesData.topBanner?.size!! > 0) {
            progressTopBanner.visibility = View.VISIBLE
            Glide.with(this).load(subCategoriesData.topBanner?.get(0)?.bannerImage)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressTopBanner.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressTopBanner.visibility = View.GONE
                        return false
                    }
                })
                .into(imgTopBanner)

        }

        if (subCategoriesData.bottomBanner?.size!! > 0) {
            progressBottomBanner.visibility = View.VISIBLE
            Glide.with(this).load(subCategoriesData.bottomBanner?.get(0)?.bannerImage)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBottomBanner.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBottomBanner.visibility = View.GONE
                        return false
                    }
                })
                .into(imgBottomBanner)
        }

        if (subCategoriesData != null) {
            recyclerCategories.layoutManager = LinearLayoutManager(
                this@CategoriesDetailsActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            recyclerCategories.setHasFixedSize(true)
            var offersAdapter = SubCategoriesAdapter(
                this,
                subCategoriesData.subCategory
            )
            recyclerCategories.adapter = offersAdapter
        }

        loadBanner(subCategoriesData.topBanner)
        loadBottomBanner(subCategoriesData.bottomBanner)
    }

    companion object {
        lateinit var mPager: ViewPager
        private var currentPage = 0
        private var NUM_PAGES = 0
        private var currentPageBottom = 0
        private var NUM_PAGES_BOTTOM = 0
    }

    private fun loadBanner(body: List<ResponseMainHome.Payload.Category.TopBanner?>?) {
        mPagerSubCategories!!.adapter = SlidingImageAdapter(this@CategoriesDetailsActivity, body)

        indicatorSubCategories.setViewPager(mPagerSubCategories)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicatorSubCategories.setRadius(5 * density)

        NUM_PAGES = body!!.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            mPagerSubCategories!!.setCurrentItem(currentPage++, true)
        }
        /*  val swipeTimer = Timer()
          swipeTimer.schedule(object : TimerTask() {
              override fun run() {
                  handler.post(Update)
              }
          }, 3000, 3000)*/

        // Pager listener over indicator
        indicatorSubCategories.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

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
        private val imageModelArrayList: List<ResponseMainHome.Payload.Category.TopBanner?>?
    ) : PagerAdapter() {

        private val inflater: LayoutInflater = LayoutInflater.from(mContext)

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(view: ViewGroup, position: Int): Any {
            val imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false)!!

            val imageView = imageLayout
                .findViewById(R.id.image) as ImageView

            val progressBar = imageLayout
                .findViewById(R.id.progressBar) as SpinKitView

            val txtBannerTitle = imageLayout
                .findViewById(R.id.bannerTitle) as TextView

            imageView.setImageResource(0)

            progressBar.visibility = View.VISIBLE

            Glide.with(mContext).load(imageModelArrayList?.get(position)?.bannerImage)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(imageView)

            /*imageView.setOnClickListener {
                var intent = Intent(mContext, ProductsByProductCategoryActivity::class.java)
                Prefs.putString(Constants.PREF_PRODUCT_CATEGORY_ID,imageModelArrayList?.get(position)?.categoryId.toString())
                Prefs.putString(Constants.PREF_SUB_CATEGORY_ID,"")
                Prefs.putString(Constants.PREF_PRODUCT_CATEGORY_ID,"")
                Prefs.putString(Constants.PREF_PRODUCT_TITLE,"")
                mContext.startActivity(intent)
            }
*/
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


    private fun loadBottomBanner(body: List<ResponseMainHome.Payload.Category.BottomBanner?>?) {
        mPagerBottom!!.adapter = SlidingImageBottomAdapter(this@CategoriesDetailsActivity, body)

        indicatorBottom.setViewPager(mPagerBottom)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicatorBottom.setRadius(5 * density)

        NUM_PAGES_BOTTOM = body!!.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPageBottom == NUM_PAGES_BOTTOM) {
                currentPageBottom = 0
            }
            mPagerBottom!!.setCurrentItem(currentPageBottom++, true)
        }
        /*  val swipeTimer = Timer()
          swipeTimer.schedule(object : TimerTask() {
              override fun run() {
                  handler.post(Update)
              }
          }, 3000, 3000)*/

        // Pager listener over indicator
        indicatorBottom.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPageBottom = position

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })
    }

    class SlidingImageBottomAdapter(
        private val mContext: Context,
        private val imageModelArrayList: List<ResponseMainHome.Payload.Category.BottomBanner?>?
    ) : PagerAdapter() {

        private val inflater: LayoutInflater = LayoutInflater.from(mContext)

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(view: ViewGroup, position: Int): Any {
            val imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false)!!

            val imageView = imageLayout
                .findViewById(R.id.image) as ImageView

            val progressBar = imageLayout
                .findViewById(R.id.progressBar) as SpinKitView

            val txtBannerTitle = imageLayout
                .findViewById(R.id.bannerTitle) as TextView

            imageView.setImageResource(0)

            progressBar.visibility = View.VISIBLE

            Glide.with(mContext).load(imageModelArrayList?.get(position)?.bannerImage)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(imageView)

            /*imageView.setOnClickListener {
                var intent = Intent(mContext, ProductsByProductCategoryActivity::class.java)
                Prefs.putString(Constants.PREF_PRODUCT_CATEGORY_ID,imageModelArrayList?.get(position)?.categoryId.toString())
                Prefs.putString(Constants.PREF_SUB_CATEGORY_ID,"")
                Prefs.putString(Constants.PREF_PRODUCT_CATEGORY_ID,"")
                Prefs.putString(Constants.PREF_PRODUCT_TITLE,"")
                mContext.startActivity(intent)
            }
*/
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


    fun goToProducts(data: ResponseMainHome.Payload.Category.SubCategory.ProductCategory?) {
        var intent = Intent(this, ProductsByProductCategoryActivity::class.java)
        Prefs.putString(Constants.PREF_PRODUCT_CATEGORY_ID, data?.productCategoryId.toString())
        Prefs.putString(Constants.PREF_PRODUCT_TITLE, data?.title.toString())
        startActivity(intent)
    }

}