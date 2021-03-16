package com.lappenfashion.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainHome
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.ui.MainActivity
import com.lappenfashion.ui.cart.CartActivity
import com.lappenfashion.ui.checkout.CheckoutActivity
import com.lappenfashion.ui.wishlist.WishListActivity
import com.lappenfashion.utils.Helper
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var mContext: Context
    private lateinit var rootView: View
    private lateinit var linearProfile : LinearLayout
    private lateinit var linearCategories : LinearLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        setUpToolBarAction()
        getCategories()

        return rootView
    }

    private fun getCategories() {

        if (NetworkConnection.checkConnection(mContext)) {
            Helper.showLoader(mContext)

            var api = MyApi()
            val requestCall: Call<ResponseMainHome> = api.getHome()

            requestCall.enqueue(object : Callback<ResponseMainHome> {
                override fun onResponse(call: Call<ResponseMainHome>, response: Response<ResponseMainHome>) {
                    Helper.dismissLoader()

                    if (response.body() != null) {
                        recyclerCategories.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                        recyclerCategories.setHasFixedSize(true)
                        var adapter = HomeAdapter(mContext, response?.body()?.payload?.categoryList!!)
                        recyclerCategories.adapter = adapter

                        recyclerDealsOftheDay.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                        recyclerDealsOftheDay.setHasFixedSize(true)
                        var adapter1 = DealsOfTheDayAdapter(mContext, response?.body()?.payload?.dealsOfTheDay!!)
                        recyclerDealsOftheDay.adapter = adapter1


                        loadBanner(response?.body()!!.payload?.exploreList!!)
                    } else {

                    }
                }

                override fun onFailure(call: Call<ResponseMainHome>, t: Throwable) {
                    Helper.dismissLoader()
                }

            })
        } else {
            Helper.showTost(mContext, "No internet connection")
        }
    }

    companion object {
        lateinit var mPager: ViewPager
        private var currentPage = 0
        private var NUM_PAGES = 0
    }

    private fun loadBanner(body: List<ResponseMainHome.Payload.Explore?>) {
        mPager!!.adapter = SlidingImageAdapter(mContext, body)

        indicator.setViewPager(mPager)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator.setRadius(5 * density)

        NUM_PAGES = body.size

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
        private val imageModelArrayList: List<ResponseMainHome.Payload.Explore?>
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

            txtBannerTitle.text = imageModelArrayList?.get(position)?.title

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

    private fun setUpToolBarAction() {
        val imgDrawer = rootView.findViewById<ImageView>(R.id.imgDrawer)
        val imgCart = rootView.findViewById<ImageView>(R.id.imgCart)
        val imgWishList = rootView.findViewById<ImageView>(R.id.imgLiked)
        val imgSearch = rootView.findViewById<ImageView>(R.id.imgSearch)

        imgDrawer.setOnClickListener {
            (mContext as MainActivity).openDrawer()
        }

        imgCart.setOnClickListener {
            var intent = Intent(mContext,CartActivity::class.java)
            startActivity(intent)
        }

        imgSearch.setOnClickListener {
            var intent = Intent(mContext,CheckoutActivity::class.java)
            startActivity(intent)
        }

        imgWishList.setOnClickListener {
            var intent = Intent(mContext,WishListActivity::class.java)
            startActivity(intent)
        }

    }

}