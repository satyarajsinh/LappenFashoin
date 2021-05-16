package com.lappenfashion.ui.categoriesDetails

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.simplemvvm.utils.Constants
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainHome
import com.lappenfashion.ui.cart.CartActivity
import com.lappenfashion.ui.products.ProductsByProductCategoryActivity
import com.lappenfashion.ui.wishlist.WishListActivity
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_categories_details.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.recyclerCategories
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
    }

    fun goToProducts(data: ResponseMainHome.Payload.Category.SubCategory.ProductCategory?) {
        var intent = Intent(this, ProductsByProductCategoryActivity::class.java)
        Prefs.putString(Constants.PREF_PRODUCT_CATEGORY_ID, data?.productCategoryId.toString())
        Prefs.putString(Constants.PREF_PRODUCT_TITLE, data?.title.toString())
        startActivity(intent)
    }

}