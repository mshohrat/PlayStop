package com.ms.playstop.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.ms.playstop.R
import kotlinx.android.synthetic.main.layout_vpn_dialog.*

class VpnDialog(context: Context, isCancelable: Boolean = true): Dialog(context) {

    init {
        setContentView(R.layout.layout_vpn_dialog)
        setCancelable(isCancelable)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        vpn_btn?.setOnClickListener {
            dismiss()
            cancel()
        }
    }

}