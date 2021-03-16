package com.lappenfashion.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lappenfashion.R
import com.lappenfashion.data.model.ResponseMainCategories
import com.lappenfashion.data.network.MyApi
import com.lappenfashion.data.network.NetworkConnection
import com.lappenfashion.ui.category.CategoriesAdapter
import com.lappenfashion.utils.Helper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CategoryFragment : Fragment() {

    private lateinit var rootView : View
    private lateinit var mContext : Context
    private lateinit var recyclerCategories : RecyclerView

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

        initData()
        clickListener()
        return rootView
    }

    private fun clickListener() {

    }

    private fun getCategories() {

        if (NetworkConnection.checkConnection(mContext)) {
            Helper.showLoader(mContext)

            var api = MyApi()
            val requestCall: Call<ResponseMainCategories> = api.getCategories()

            requestCall.enqueue(object : Callback<ResponseMainCategories> {
                override fun onResponse(call: Call<ResponseMainCategories>, response: Response<ResponseMainCategories>) {
                    Helper.dismissLoader()

                    if (response.body() != null) {
                        recyclerCategories.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
                        recyclerCategories.setHasFixedSize(true)
                        var adapter = CategoriesAdapter(mContext, response?.body()?.payload!!)
                        recyclerCategories.adapter = adapter
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
        recyclerCategories = rootView.findViewById(R.id.recyclerCategories)

        getCategories()
    }

}