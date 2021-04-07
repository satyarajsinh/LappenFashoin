package com.lappenfashion.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lappenfashion.R
import com.lappenfashion.`interface`.CategoriesInterface
import com.lappenfashion.data.model.ResponseMainHome
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.sqlitedb.DBManager
import com.lappenfashion.ui.MainActivity
import com.lappenfashion.ui.cart.CartActivity
import com.lappenfashion.ui.categoriesDetails.CategoriesDetailsActivity
import com.lappenfashion.ui.checkout.CheckoutActivity
import com.lappenfashion.ui.wishlist.WishListActivity
import com.lappenfashion.utils.Helper
import com.lappenfashion.utils.SpacesItemDecoration
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(),CategoriesInterface {

    private var localDbId: String = ""
    private lateinit var mContext: Context
    private lateinit var rootView: View
    private lateinit var linearProfile: LinearLayout
    private lateinit var linearCategories: LinearLayout
    private lateinit var linearTrending: LinearLayout
    private lateinit var dbManager: DBManager

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
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBarAction()
        initData()
//        getCategories(localDbId)
    }

    private fun initData() {
//        nestedScrollView.setOnTouchListener(TranslateAnimationUtil(mContext, recyclerCategories))
        dbManager = DBManager(mContext)
        dbManager.open()

        val cursor = dbManager.fetch()
        if (cursor != null) {
            cursor.moveToFirst()
            if (cursor.count > 0) {
                var resposne = cursor.getString(1)
                localDbId = cursor.getString(0)
                val jsonObject = JSONObject(resposne);

                val gson = Gson()
                val homeResponse: ResponseMainHome = gson.fromJson(
                    jsonObject.toString(),
                    ResponseMainHome::class.java
                )
                setHomeData(homeResponse)
                if (NetworkConnection.checkConnection(mContext)) {
                    getHomeData(localDbId)
                }
                Log.e("Message", "Message : " + homeResponse.message)
            } else {
                if (NetworkConnection.checkConnection(mContext)) {
                    Helper.showLoader(mContext)
                    getHomeData(localDbId)
                } else {
                    Helper.showTost(mContext, "No internet connection")
                }
            }
        }
    }

    private fun setHomeData(homeResponse: ResponseMainHome) {

        //main categories
        if (homeResponse?.payload != null && homeResponse?.payload?.categoryList!!.size > 0) {
            recyclerCategories.visibility = View.VISIBLE
            recyclerCategories.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            recyclerCategories.setHasFixedSize(true)
            var adapter =
                CategoriesAdapter(mContext, homeResponse?.payload?.categoryList!!,this)
            recyclerCategories.adapter = adapter
        } else {
            recyclerCategories.visibility = View.GONE
        }


        //deals of the day
        if (homeResponse?.payload != null && homeResponse?.payload?.dealsOfTheDay!!.size > 0) {
            linearDealsOfTheDay.visibility = View.VISIBLE
            recyclerDealsOftheDay.layoutManager = LinearLayoutManager(
                mContext,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerDealsOftheDay.setHasFixedSize(true)
            var dealsOfTheDayAdapter = DealsOfTheDayAdapter(
                mContext,
                homeResponse?.payload?.dealsOfTheDay!!
            )
            recyclerDealsOftheDay.adapter = dealsOfTheDayAdapter

        } else {
            linearDealsOfTheDay.visibility = View.GONE
        }

        //second categories
        recyclerCategoriesSecond.setLayoutManager(
            GridLayoutManager(
                mContext,
                2
            )
        )

        var spaceIterator : SpacesItemDecoration = SpacesItemDecoration(10)
        recyclerCategoriesSecond.addItemDecoration(spaceIterator);
        recyclerCategoriesSecond.setHasFixedSize(true)


        var categoriesSecondAdapter = CategoriesSecondAdapter(
            mContext,
            homeResponse?.payload?.dealsOfTheDay!!
        )
        recyclerCategoriesSecond.adapter = categoriesSecondAdapter


        //banners
        if (homeResponse?.payload != null && homeResponse?.payload?.exploreList!!.size > 0) {
            linearViewPager.visibility = View.VISIBLE
            loadBanner(homeResponse!!.payload?.exploreList!!)
        } else {
            linearViewPager.visibility = View.GONE
        }


        //trending images
        recyclerTrending.setLayoutManager(
            GridLayoutManager(
                mContext,
                3
            )
        )
        recyclerTrending.setHasFixedSize(true)


        var trendingAdapter = TrendingAdapter(
            mContext,
            homeResponse.payload?.trending
        )
        recyclerTrending.adapter = trendingAdapter


        //offer banners
        recyclerOffers.layoutManager = LinearLayoutManager(
            mContext,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerOffers.setHasFixedSize(true)
        var offersAdapter = OffersAdapter(
            mContext,
            homeResponse.payload?.offerPoster
        )
        recyclerOffers.adapter = offersAdapter

        //Accessories
        recyclerAccessories.setLayoutManager(
            GridLayoutManager(
                mContext,
                3
            )
        )
        recyclerAccessories.setHasFixedSize(true)

        var accessoriesAdapter = AccessoriesAdapter(
            mContext,
            homeResponse.payload?.accessories
        )
        recyclerAccessories.adapter = accessoriesAdapter

        //other banners
        recyclerOtherOffers.layoutManager = LinearLayoutManager(
            mContext,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerOtherOffers.setHasFixedSize(true)
        var otherOffersAdapter = OtherOffersAdapter(
            mContext,
            homeResponse.payload?.otherPoster
        )
        recyclerOtherOffers.adapter = otherOffersAdapter



    }

    private fun getHomeData(localDbId: String) {

        var api = MyApi(mContext)
        val requestCall: Call<JsonObject> = api.getHome()

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                Helper.dismissLoader()

                if (response.body() != null) {

                    if (localDbId != "") {
                        dbManager.delete(id.toLong())
                    }

                    dbManager.insert(response.body().toString())

                    val gson = Gson()
                    val homeResponse: ResponseMainHome = gson.fromJson(
                        response.body(),
                        ResponseMainHome::class.java
                    )
                    setHomeData(homeResponse)

                } else {
                    Helper.showTost(mContext, "No Data Found")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Helper.dismissLoader()
            }

        })
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
            var intent = Intent(mContext, CartActivity::class.java)
            startActivity(intent)
        }

        imgSearch.setOnClickListener {
            var intent = Intent(mContext, CheckoutActivity::class.java)
            startActivity(intent)
        }

        imgWishList.setOnClickListener {
            var intent = Intent(mContext, WishListActivity::class.java)
            startActivity(intent)
        }

    }

    override fun goToSubCategories(data: ResponseMainHome.Payload.Category?) {
        var intent = Intent(mContext,CategoriesDetailsActivity::class.java)
        intent.putExtra("subCategories",data)
        startActivity(intent)
    }

}