package com.ms.playstop.utils

import android.content.Context
import com.google.android.flexbox.FlexboxLayoutManager

class RtlFlexBoxLayoutManager(context: Context?): FlexboxLayoutManager(context) {

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }
}