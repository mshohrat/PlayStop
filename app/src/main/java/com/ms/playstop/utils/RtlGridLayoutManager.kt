package com.ms.playstop.utils

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class RtlGridLayoutManager(context: Context?,spanCount: Int, orientation: Int, reverseLayout: Boolean): GridLayoutManager(context,spanCount,orientation,reverseLayout) {

    override fun isLayoutRTL(): Boolean {
        return true
    }
}