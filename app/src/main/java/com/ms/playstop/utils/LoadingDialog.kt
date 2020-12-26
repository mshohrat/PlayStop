package com.ms.playstop.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.ms.playstop.R

class LoadingDialog(context: Context): Dialog(context) {

    init {
        setContentView(R.layout.layout_loading_dialog)
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}