package com.ms.playstop.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.annotation.StringRes
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textview.MaterialTextView
import com.ms.playstop.R
import kotlinx.android.synthetic.main.layout_terminate_device_dialog.*

class RemoveDeviceDialog(context: Context, isCancelable: Boolean = true, listener: OnClickListener? = null): BottomSheetDialog(context) {

    var clickListener: OnClickListener? = null

    init {
        setContentView(R.layout.layout_terminate_device_dialog)
        setCancelable(isCancelable)
        clickListener = listener
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        terminate_device_dialog_yes_btn?.setOnClickListener {
            clickListener?.onYesClick()
            dismiss()
            cancel()
        }
        terminate_device_dialog_no_btn?.setOnClickListener {
            dismiss()
            cancel()
        }
    }

    fun title(text: String): RemoveDeviceDialog {
        findViewById<MaterialTextView>(R.id.terminate_device_dialog_title_tv)?.text = text
        return this
    }

    fun title(@StringRes text: Int): RemoveDeviceDialog {
        return title(context.getString(text))
    }

    fun description(text: String): RemoveDeviceDialog {
        findViewById<MaterialTextView>(R.id.terminate_device_dialog_desc_tv)?.text = text
        return this
    }

    fun description(@StringRes text: Int): RemoveDeviceDialog {
        return description(context.getString(text))
    }

    override fun cancel() {
        super.cancel()
        clickListener = null
    }

    interface OnClickListener {
        fun onYesClick()
    }
}