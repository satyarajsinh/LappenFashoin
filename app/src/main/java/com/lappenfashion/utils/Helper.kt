package com.lappenfashion.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import com.lappenfashion.R


object Helper {

    var dialog : Dialog? = null
    fun showLoader(context: Context) {
        if(dialog!=null) {
            dialog = Dialog(context)
            dialog?.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
            dialog?.setCancelable(false)
            dialog?.setCanceledOnTouchOutside(false)
            dialog?.setContentView(R.layout.loader_view)
            dialog?.show()
            dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun dismissLoader(){
        if(dialog!=null && dialog!!.isShowing){
            dialog!!.dismiss()
        }
    }

    fun showTost(context : Context,msg : String){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
    }

}