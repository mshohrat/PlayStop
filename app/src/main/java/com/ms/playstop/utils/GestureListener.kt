package com.ms.playstop.utils

import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

class GestureListener(private var onScrollOrientationListener: GestureListener.OnScrollOrientationListener? = null): GestureDetector.SimpleOnGestureListener() {

    private val Y_BUFFER = 10

    override fun onDown(e: MotionEvent?): Boolean {
        onScrollOrientationListener?.onHorizontalScroll()
        return super.onDown(e)
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if(abs(distanceX) > abs(distanceY)) {
            onScrollOrientationListener?.onHorizontalScroll()
        } else if (abs(distanceY) > Y_BUFFER) {
            onScrollOrientationListener?.onVerticalScroll()
        }
        return super.onScroll(e1, e2, distanceX, distanceY)
    }

    interface OnScrollOrientationListener {
        fun onVerticalScroll()
        fun onHorizontalScroll()
    }
}