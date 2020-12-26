package com.ms.playstop.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.ms.playstop.R
import kotlinx.android.synthetic.main.layout_update_dialog.*

class UpdateDialog(context: Context,isCancelable: Boolean = true): Dialog(context) {

    var updateClickListener: OnUpdateClickListener? = null

    init {
        setContentView(R.layout.layout_update_dialog)
        setCancelable(isCancelable)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        update_cancel_btn?.setOnClickListener {
            updateClickListener?.onCancelClick()
            dismiss()
            cancel()
        }
        update_btn?.setOnClickListener {
            updateClickListener?.onDownloadClick()
        }
        update_cancel_btn?.isEnabled = isCancelable
    }

    override fun cancel() {
        super.cancel()
        updateClickListener = null
    }

    interface OnUpdateClickListener {
        fun onDownloadClick()
        fun onCancelClick()
    }
}