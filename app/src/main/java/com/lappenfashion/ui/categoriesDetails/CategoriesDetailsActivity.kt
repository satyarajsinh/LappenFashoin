package com.lappenfashion.ui.categoriesDetails

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainHome
import com.lappenfashion.ui.products.ProductsByProductCategoryActivity
import kotlinx.android.synthetic.main.activity_categories_details.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.recyclerCategories
import kotlinx.android.synthetic.main.toolbar_with_like_cart.*

class CategoriesDetailsActivity : AppCompatActivity() {


    lateinit var subCategoriesData: ResponseMainHome.Payload.Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_details)

        initData()

        imgBack.setOnClickListener {
            finish()
        }
    }

    private fun initData() {
        if (intent != null) {
            subCategoriesData =
                intent.getSerializableExtra("subCategories") as ResponseMainHome.Payload.Category
        }

        if (subCategoriesData.topBanner?.size!! > 0) {
            Glide.with(this).load(subCategoriesData.topBanner?.get(0)?.bannerImage)
                .into(imgTopBanner)
        }

        if (subCategoriesData.bottomBanner?.size!! > 0) {
            Glide.with(this).load(subCategoriesData.bottomBanner?.get(0)?.bannerImage)
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
        var intent = Intent(this,ProductsByProductCategoryActivity::class.java)
        intent.putExtra("id",data?.productCategoryId.toString())
        intent.putExtra("name",data?.title.toString())
        startActivity(intent)
    }

}