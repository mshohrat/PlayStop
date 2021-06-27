package com.ms.playstop.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ms.playstop.R
import kotlinx.android.synthetic.main.layout_review_dialog.*

class ReviewDialog(context: Context, isCancelable: Boolean = true, listener: OnVpnClickListener? = null): BottomSheetDialog(context) {

    var vpnClickListener: OnVpnClickListener? = null

    init {
        setContentView(R.layout.layout_review_dialog)
        setCancelable(isCancelable)
        vpnClickListener = listener
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        review_cancel_btn?.setOnClickListener {
            vpnClickListener?.onDiscardClick()
            dismiss()
            cancel()
        }
        review_ok_btn?.setOnClickListener {
            vpnClickListener?.onSendReviewClick()
            dismiss()
            cancel()
        }
    }

    override fun cancel() {
        super.cancel()
        vpnClickListener = null
    }

    interface OnVpnClickListener {
        fun onSendReviewClick()
        fun onDiscardClick()
    }

}