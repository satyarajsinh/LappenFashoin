package com.lappenfashion.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemvvm.utils.Constants
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainCategories
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.ui.category.CategoriesAdapter
import com.lappenfashion.ui.products.ProductsByProductCategoryActivity
import com.lappenfashion.utils.Helper
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.fragment_category.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CategoryFragment : Fragment() {

    private lateinit var rootView : View
    private lateinit var mContext : Context
    private lateinit var recyclerCategoriesBottom : RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_category, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        clickListener()
    }

    private fun clickListener() {

    }

    private fun getCategories() {

        if (NetworkConnection.checkConnection(mContext)) {
            Helper.showLoader(mContext)

            var api = MyApi(mContext)
            val requestCall: Call<ResponseMainCategories> = api.getCategories()

            requestCall.enqueue(object : Callback<ResponseMainCategories> {
                override fun onResponse(call: Call<ResponseMainCategories>, response: Response<ResponseMainCategories>) {
                    Helper.dismissLoader()

                    if (response.body() != null) {
                        recyclerCategoriesBottom.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
                        recyclerCategoriesBottom.setHasFixedSize(true)
                        var adapter = CategoriesAdapter(mContext, response?.body()?.payload!!,this@CategoryFragment)
                        recyclerCategoriesBottom.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<ResponseMainCategories>, t: Throwable) {
                    Helper.dismissLoader()
                }

            })
        } else {
            Helper.showTost(mContext, "No internet connection")
        }
    }

    private fun initData() {
        recyclerCategoriesBottom = rootView.findViewById(R.id.recyclerCategoriesBottom)

        getCategories()
    }

    fun goToProductDetails() {
        var intent = Intent(mContext, ProductsByProductCategoryActivity::class.java)
        mContext.startActivity(intent)
    }

}