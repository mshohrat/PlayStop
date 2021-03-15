package com.ms.playstop.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ms.playstop.R
import kotlinx.android.synthetic.main.layout_vpn_dialog.*

class VpnDialog(context: Context, isCancelable: Boolean = true): BottomSheetDialog(context) {

    var vpnClickListener: OnVpnClickListener? = null

    init {
        setContentView(R.layout.layout_vpn_dialog)
        setCancelable(isCancelable)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        vpn_btn?.setOnClickListener {
            vpnClickListener?.onGotItClick()
            dismiss()
            cancel()
        }
    }

    override fun cancel() {
        super.cancel()
        vpnClickListener = null
    }

    interface OnVpnClickListener {
        fun onGotItClick()
    }

}