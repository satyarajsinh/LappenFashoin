package com.lappenfashion.utils

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.lappenfashion.R

class TranslateAnimationUtil : View.OnTouchListener{
    private lateinit var gestureDetector: GestureDetector

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(p1)
    }

    constructor(context : Context, viewAnimation : View){
        gestureDetector = GestureDetector(context,SimpleGestureDetectore(viewAnimation))
    }

    class SimpleGestureDetectore : GestureDetector.SimpleOnGestureListener {
        private lateinit var viewAnimation : View
        private var isFinishAnimation : Boolean = true

        constructor(animation : View){
            this.viewAnimation = animation
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if(distanceY > 0){
                hiddenView()
            }else{
                showView()
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        private fun hiddenView(){
            if(viewAnimation == null || viewAnimation.visibility == View.GONE){
                return
            }

            var animationDown = AnimationUtils.loadAnimation(viewAnimation.context, R.anim.move_down)
            animationDown.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {
                    viewAnimation.visibility = View.VISIBLE
                    isFinishAnimation = false
                }

                override fun onAnimationEnd(p0: Animation?) {
                    viewAnimation.visibility = View.GONE
                    isFinishAnimation = true
                }

                override fun onAnimationRepeat(p0: Animation?) {

                }

            })

            if(isFinishAnimation){
                viewAnimation.startAnimation(animationDown)
            }
        }
        private fun showView(){
            if(viewAnimation == null || viewAnimation.visibility == View.VISIBLE){
                return
            }

            var animationUp = AnimationUtils.loadAnimation(viewAnimation.context, R.anim.move_up)
            animationUp.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(p0: Animation?) {
                    viewAnimation.visibility = View.VISIBLE
                    isFinishAnimation = false
                }

                override fun onAnimationEnd(p0: Animation?) {
                    isFinishAnimation = true
                }

                override fun onAnimationRepeat(p0: Animation?) {

                }

            })

            if(isFinishAnimation){
                viewAnimation.startAnimation(animationUp)
            }
        }
    }


}