package com.lappenfashion.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lappenfashion.R


class ProfileFragment : Fragment() {

    private lateinit var rootView : View
    private lateinit var mContext : Context
    private lateinit var txtName : TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_profile, container, false)

        initData()
        clickListener()
        return rootView
    }

    private fun clickListener() {
        txtName.setOnClickListener {
            val dialog = BottomSheetDialog(mContext)
            dialog.setContentView(R.layout.bottom_sheet_login)
            dialog.setCanceledOnTouchOutside(false)

            val imgClose = dialog.findViewById<View>(R.id.imgClose) as ImageView?
            imgClose!!.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }
    }

    private fun initData() {
        txtName = rootView.findViewById(R.id.txtName)
    }

}