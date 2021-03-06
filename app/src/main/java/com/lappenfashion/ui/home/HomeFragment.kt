package com.lappenfashion.ui.home

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
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
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lappenfashion.R
import com.lappenfashion.`interface`.CategoriesInterface
import com.lappenfashion.data.model.ResponseMainHome
import com.lappenfashion.data.model.ResponseMainVersionUpdate
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.sqlitedb.DBManager
import com.lappenfashion.ui.MainActivity
import com.lappenfashion.ui.cart.CartActivity
import com.lappenfashion.ui.categoriesDetails.CategoriesDetailsActivity
import com.lappenfashion.ui.notification.NotificationActivity
import com.lappenfashion.ui.products.ProductsByProductCategoryActivity
import com.lappenfashion.ui.search.SearchProductActivity
import com.lappenfashion.ui.wishlist.WishListActivity
import com.lappenfashion.utils.Helper
import com.lappenfashion.utils.SpacesItemDecoration
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.toolbar_with_drawer.view.*
import kotlinx.android.synthetic.main.toolbar_with_like_cart.*
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

        Log.e("Token","Token"+ Prefs.getString(Constants.PREF_TOKEN,""))
        Log.e("user id","user id : "+ Prefs.getInt(Constants.PREF_USER_ID,0).toString())

        if(Prefs.getInt(Constants.PREF_CART_COUNT,0) > 0){
            rootView.txtCartCount.visibility = View.VISIBLE
            rootView.txtCartCount.text = Prefs.getInt(Constants.PREF_CART_COUNT,0).toString()
        }else{
            txtCartCount.visibility = View.GONE
        }

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                101
            )
        }

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
                }else {
                    Helper.showTost(mContext, "No internet connection")
                }
                Log.e("Message", "Message : " + homeResponse.message)
            } else {
                if (NetworkConnection.checkConnection(mContext)) {
                    Helper.showLoader(mContext)
                    rootView.nestedScrollView.visibility = View.GONE
                    getHomeData(localDbId)
                } else {
                    Helper.showTost(mContext, "No internet connection")
                }
            }
        }

        if (NetworkConnection.checkConnection(mContext)) {
            getVersionUpdate()
        }else {
            Helper.showTost(mContext, "No internet connection")
        }
    }

    private fun getVersionUpdate() {
        var api = MyApi(mContext)
        val requestCall: Call<ResponseMainVersionUpdate> = api.getVersionUpdate()

        requestCall.enqueue(object : Callback<ResponseMainVersionUpdate> {
            override fun onResponse(
                call: Call<ResponseMainVersionUpdate>,
                response: Response<ResponseMainVersionUpdate>
            ) {
                Helper.dismissLoader()
                if (response.body() != null && response.body()!!.result == true && response.body()!!.payload!=null) {
                    try {
                        val pInfo =
                            context!!.packageManager.getPackageInfo(context!!.packageName, 0)
                        var version = pInfo.versionCode
                        if (version != response.body()!!.payload!!.appVersion?.toInt()) {
                            showVersionUpdate()
                        }
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseMainVersionUpdate>, t: Throwable) {
                Helper.dismissLoader()
            }
        })
    }

    private fun showVersionUpdate() {
        val versionUpdateDialog = Dialog(requireContext())
        versionUpdateDialog.setCancelable(false)
        versionUpdateDialog.setContentView(R.layout.dialog_version_update)
        versionUpdateDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val txtAlertMessage = versionUpdateDialog.findViewById<TextView>(R.id.txtAlertMessage)
        val txtUpdate = versionUpdateDialog.findViewById<TextView>(R.id.txtUpdate)
        txtUpdate.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + requireContext().packageName)
                )
            )
        }
        versionUpdateDialog.show()
    }

    override fun onResume() {
        super.onResume()
        if(Prefs.getInt(Constants.PREF_CART_COUNT,0) > 0){
            rootView.txtCartCount.visibility = View.VISIBLE
            rootView.txtCartCount.text = Prefs.getInt(Constants.PREF_CART_COUNT,0).toString()
        }else{
            rootView.txtCartCount.visibility = View.GONE
        }

    }

    private fun setHomeData(homeResponse: ResponseMainHome) {

        //main categories
        if (homeResponse?.payload != null && homeResponse?.payload?.categoryList!!.size > 0) {
            if(recyclerCategories!=null) {
                recyclerCategories.visibility = View.VISIBLE
                recyclerCategories.layoutManager =
                    LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                recyclerCategories.setHasFixedSize(true)
                var adapter =
                    CategoriesAdapter(mContext, homeResponse?.payload?.categoryList!!, this)
                recyclerCategories.adapter = adapter
            }
        } else {
            if(recyclerCategories!=null) {
                recyclerCategories.visibility = View.GONE
            }
        }


        //deals of the day
        if (homeResponse?.payload != null && homeResponse?.payload?.dealsOfTheDay!!.size > 0) {
                rootView.linearDealsOfTheDay.visibility = View.VISIBLE
                rootView.recyclerDealsOftheDay.layoutManager = LinearLayoutManager(
                    mContext,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            rootView.recyclerDealsOftheDay.setHasFixedSize(true)
                var dealsOfTheDayAdapter = DealsOfTheDayAdapter(
                    mContext,
                    homeResponse?.payload?.dealsOfTheDay!!,this@HomeFragment
                )
            rootView.recyclerDealsOftheDay.adapter = dealsOfTheDayAdapter
        } else {
            rootView.linearDealsOfTheDay.visibility = View.GONE
        }

        //second categories
        rootView.recyclerCategoriesSecond.setLayoutManager(
            GridLayoutManager(
                mContext,
                2
            )
        )

        var spaceIterator : SpacesItemDecoration = SpacesItemDecoration(10)
        rootView.recyclerCategoriesSecond.addItemDecoration(spaceIterator);
        rootView.recyclerCategoriesSecond.setHasFixedSize(true)


        var categoriesSecondAdapter = CategoriesSecondAdapter(
            mContext,
            homeResponse?.payload?.subCategory!!,this@HomeFragment
        )
        rootView.recyclerCategoriesSecond.adapter = categoriesSecondAdapter


        //banners
        if (homeResponse?.payload != null && homeResponse?.payload?.exploreList!!.size > 0) {
            rootView.linearViewPager.visibility = View.VISIBLE
            loadBanner(homeResponse!!.payload?.exploreList!!)
        } else {
            rootView.linearViewPager.visibility = View.GONE
        }


        //trending images
        rootView.recyclerTrending.setLayoutManager(
            GridLayoutManager(
                mContext,
                3
            )
        )
        rootView.recyclerTrending.setHasFixedSize(true)


        var trendingAdapter = TrendingAdapter(
            mContext,
            homeResponse.payload?.trending, activity as MainActivity?
        )
        rootView.recyclerTrending.adapter = trendingAdapter


        //offer banners
        rootView.recyclerOffers.layoutManager = LinearLayoutManager(
            mContext,
            LinearLayoutManager.VERTICAL,
            false
        )
        rootView.recyclerOffers.setHasFixedSize(true)
        var offersAdapter = OffersAdapter(
            mContext,
            homeResponse.payload?.offerPoster,this@HomeFragment
        )
        rootView.recyclerOffers.adapter = offersAdapter

        //Accessories
        rootView.recyclerAccessories.setLayoutManager(
            GridLayoutManager(
                mContext,
                3
            )
        )
        var spaceIterator1 : SpacesItemDecoration = SpacesItemDecoration(10)
        rootView.recyclerAccessories.addItemDecoration(spaceIterator1);
        rootView.recyclerAccessories.setHasFixedSize(true)

        var accessoriesAdapter = AccessoriesAdapter(
            mContext,
            homeResponse.payload?.accessories,this@HomeFragment
        )
        rootView.recyclerAccessories.adapter = accessoriesAdapter

        //other banners
        rootView.recyclerOtherOffers.layoutManager = LinearLayoutManager(
            mContext,
            LinearLayoutManager.VERTICAL,
            false
        )
        rootView.recyclerOtherOffers.setHasFixedSize(true)
        var otherOffersAdapter = OtherOffersAdapter(
            mContext,
            homeResponse.payload?.otherPoster,this@HomeFragment
        )
        rootView.recyclerOtherOffers.adapter = otherOffersAdapter

    }

    private fun getHomeData(localDbId: String) {

        Log.e("Orignal user id","Orignal user id"+Prefs.getInt(Constants.PREF_USER_ID, 0).toString())
        var userId : Int = 0
        if (Prefs.getString(Constants.PREF_IS_LOGGED_IN, "") == "1") {
            userId = Prefs.getInt(Constants.PREF_USER_ID, 0)
        }else{
            userId = 0
        }
        var api = MyApi(mContext)
        val requestCall: Call<JsonObject> = api.getHome(Constants.END_POINT_HOME+"?user_id="+userId)

        requestCall.enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                Helper.dismissLoader()
                if (response.body() != null) {

                    if (localDbId != "") {
                        dbManager.delete(localDbId.toLong())
                    }

                    dbManager.insert(response.body().toString())

                    val gson = Gson()
                    val homeResponse: ResponseMainHome = gson.fromJson(
                        response.body(),
                        ResponseMainHome::class.java
                    )

                    if (Prefs.getString(Constants.PREF_IS_LOGGED_IN, "") == "1") {
                        if(homeResponse.payload?.cart_count!! > 0){
                            Prefs.putInt(Constants.PREF_CART_COUNT,homeResponse.payload?.cart_count!!)
                            rootView.txtCartCount.visibility = View.VISIBLE
                            rootView.txtCartCount.text = homeResponse.payload?.cart_count!!.toString()
                        }
                    }else{
                        if(Prefs.getInt(Constants.PREF_CART_COUNT,0) <= 0) {
                            rootView.txtCartCount.visibility = View.GONE
                        }
                    }

                    setHomeData(homeResponse)

                } else {
                    Helper.showTost(mContext, "No Data Found")
                }
                rootView.nestedScrollView.visibility = View.VISIBLE

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                rootView.nestedScrollView.visibility = View.VISIBLE
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
        rootView.mPager!!.adapter = SlidingImageAdapter(mContext, body)

        rootView.indicator.setViewPager(rootView.mPager)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        rootView.indicator.setRadius(5 * density)

        NUM_PAGES = body.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            rootView.mPager!!.setCurrentItem(currentPage++, true)
        }
        /*  val swipeTimer = Timer()
          swipeTimer.schedule(object : TimerTask() {
              override fun run() {
                  handler.post(Update)
              }
          }, 3000, 3000)*/

        // Pager listener over indicator
        rootView.indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

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

            val progressBar = imageLayout
                .findViewById(R.id.progressBar) as SpinKitView

            val txtBannerTitle = imageLayout
                .findViewById(R.id.bannerTitle) as TextView

            imageView.setImageResource(0)

            progressBar.visibility = View.VISIBLE

            Glide.with(mContext).load(imageModelArrayList?.get(position)?.image)
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

            imageView.setOnClickListener {
                var intent = Intent(mContext, ProductsByProductCategoryActivity::class.java)
                Prefs.putString(Constants.PREF_PRODUCT_CATEGORY_ID,imageModelArrayList?.get(position)?.categoryId.toString())
                Prefs.putString(Constants.PREF_SUB_CATEGORY_ID,imageModelArrayList?.get(position)?.subCategoryId.toString())
                Prefs.putString(Constants.PREF_PRODUCT_TITLE,"")
                Prefs.putString(Constants.PREF_OFFER_AMOUNT,imageModelArrayList?.get(position)?.amount.toString())
                mContext.startActivity(intent)
            }

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
        val imgNotification = rootView.findViewById<ImageView>(R.id.imgNotification)

        imgDrawer.setOnClickListener {
            (mContext as MainActivity).openDrawer()
        }

        imgCart.setOnClickListener {
            var intent = Intent(mContext, CartActivity::class.java)
            startActivity(intent)
        }

        imgNotification.setOnClickListener {
            var intent = Intent(mContext, NotificationActivity::class.java)
            startActivity(intent)
        }

        imgSearch.setOnClickListener {
            var intent = Intent(mContext, SearchProductActivity::class.java)
            startActivity(intent)
        }

        imgWishList.setOnClickListener {
            var intent = Intent(mContext, WishListActivity::class.java)
            startActivity(intent)
        }

    }

    override fun goToSubCategories(data: ResponseMainHome.Payload.Category?) {
        var intent = Intent(mContext,CategoriesDetailsActivity::class.java)
        Prefs.putString(Constants.PREF_CATEGORY_ID,data?.categoryId.toString())
        intent.putExtra("subCategories",data)
        startActivity(intent)
    }

    override fun dealsOfTheDay(get: ResponseMainHome.Payload.DealsOfTheDay?) {
        var intent = Intent(mContext, ProductsByProductCategoryActivity::class.java)
        Prefs.putString(Constants.PREF_CATEGORY_ID,get?.categoryId?.toString())
        Prefs.putString(Constants.PREF_SUB_CATEGORY_ID,get?.subCategoryId?.toString())
        Prefs.putString(Constants.PREF_PRODUCT_CATEGORY_ID,get?.productCategoryId?.toString())
        Prefs.putString(Constants.PREF_OFFER_AMOUNT,get?.amount?.toString())
        Prefs.putString(Constants.PREF_PRODUCT_TITLE,get?.title)
        mContext.startActivity(intent)
    }

    fun subCategories(productCategory:Int,subCategory:Int,categoryId:Int,title:String) {
        var intent = Intent(mContext, ProductsByProductCategoryActivity::class.java)
        Prefs.putString(Constants.PREF_CATEGORY_ID,categoryId.toString())
        Prefs.putString(Constants.PREF_SUB_CATEGORY_ID,subCategory.toString())
        Prefs.putString(Constants.PREF_PRODUCT_CATEGORY_ID,productCategory.toString())
        Prefs.putString(Constants.PREF_PRODUCT_TITLE,title)
        mContext.startActivity(intent)
    }

    fun goToAccesories(subCategory:Int,title:String) {
        var intent = Intent(mContext, ProductsByProductCategoryActivity::class.java)
        Prefs.putString(Constants.PREF_SUB_CATEGORY_ID,subCategory.toString())
        Prefs.putString(Constants.PREF_PRODUCT_TITLE,title)
        mContext.startActivity(intent)
    }
}