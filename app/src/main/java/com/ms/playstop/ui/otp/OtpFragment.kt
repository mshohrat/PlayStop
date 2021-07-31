package com.ms.playstop.ui.otp

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.*
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.ui.completeAccount.CompleteAccountFragment
import kotlinx.android.synthetic.main.fragment_otp.*

class OtpFragment : BaseFragment() {

    companion object {
        fun newInstance() = OtpFragment()
        const val USER_PHONE = "User Phone"
        const val VERIFY_STATE = "Verify State"
        const val VERIFY_STATE_LOGIN = 2
        const val VERIFY_STATE_NONE = 0
    }

    private lateinit var viewModel: OtpViewModel
    private var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_otp, container, false)
    }

    override fun tag(): String {
        return "Otp Fragment"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OtpViewModel::class.java)
    }

    override fun onDayNightModeApplied(type: Int) {
        activity?.let { ctx ->
            view?.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorPrimary))
            otp_appbar?.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorAccentDark))
            otp_desc_tv?.setTextColor(ContextCompat.getColor(ctx,R.color.white))
            otp_pin_entry?.setTextColor(ContextCompat.getColor(ctx,R.color.white))
            otp_pin_entry?.setLineColor(ContextCompat.getColor(ctx,R.color.blue))
            otp_timer_tv?.setTextColor(ContextCompat.getColor(ctx,R.color.gray))
            otp_resend_btn?.setTextColor(ContextCompat.getColor(ctx,R.color.white))
            otp_wrong_number_btn?.setTextColor(ContextCompat.getColor(ctx,R.color.white))
            otp_btn_divider?.background = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.TRANSPARENT,ContextCompat.getColor(ctx,R.color.gray),Color.TRANSPARENT)
            )
            otp_btn?.background = LayerDrawable(
                arrayOf(MaterialShapeDrawable(ShapeAppearanceModel.builder()
                    .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.button_radius).toFloat())
                    .build()), GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                    intArrayOf(ContextCompat.getColor(ctx,R.color.purple),ContextCompat.getColor(ctx,R.color.blue))
                )))
        }
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        handleArguments()
        initViews()
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    private fun handleArguments() {
        arguments?.let {
            if(it.containsKey(USER_PHONE)) {
                viewModel.phoneNumber = it.getString(USER_PHONE,"")
            }
            if(it.containsKey(VERIFY_STATE)) {
                viewModel.verifyState = it.getInt(VERIFY_STATE, VERIFY_STATE_NONE)
            }
        }
    }

    private fun initViews() {
        otp_desc_tv?.text = String.format(getString(R.string.enter_the_otp_sent_to_phone_number_x),viewModel.phoneNumber)
        otp_pin_entry?.requestFocus()
        startTimer()
    }


    private fun subscribeToViewModel() {
        viewModel.verifyOtp.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it)
            removeLastFromParent(2)
        })
        viewModel.loginOtp.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it.first)
            removeLastFromParent(2)
            if(it.second) {
                val completeAccountFragment = CompleteAccountFragment.newInstance()
                completeAccountFragment.arguments = Bundle().apply {
                    putInt(CompleteAccountFragment.COMPLETE_ACCOUNT_STATE
                        ,CompleteAccountFragment.COMPLETE_ACCOUNT_STATE_ADD)
                }
                addToParent(completeAccountFragment)
            }
        })
        viewModel.otpError.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it)
        })

        viewModel.resendCode.observe(viewLifecycleOwner, Observer {
            showToast(it)
            startTimer()
        })

        viewModel.resendCodeError.observe(viewLifecycleOwner, Observer {
            enableResendButton()
            showToast(it)
        })
    }

    private fun startTimer() {
        timer?.cancel()
        otp_resend_btn_loading?.hide()
        otp_timer_tv?.text = "03:00"
        otp_timer_tv?.show()
        disableResendButton()
        timer = object : CountDownTimer(180000L,1000L) {

            override fun onTick(time: Long) {
                val totalTimeInSeconds = time.div(1000)
                val timeInMinuets = totalTimeInSeconds.div(60)
                val timeInExtraSeconds = totalTimeInSeconds.rem(60)
                otp_timer_tv?.text = "0".plus(timeInMinuets).plus(":").plus(if(timeInExtraSeconds < 10) "0".plus(timeInExtraSeconds) else timeInExtraSeconds)
            }

            override fun onFinish() {
                otp_timer_tv?.text = "00:00"
                enableResendButton()
                otp_timer_tv?.hide()
            }
        }
        timer?.start()
    }

    private fun subscribeToViewEvents() {
        otp_pin_entry?.setOtpCompletionListener {
            showButtonLoading()
            viewModel.verify(it?.toString())
        }
        otp_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
        otp_wrong_number_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
        otp_btn?.setOnClickListener {
            showButtonLoading()
            viewModel.verify(otp_pin_entry?.text?.toString())
        }
        otp_root?.setOnClickListener {
            it.hideSoftKeyboard()
        }

        otp_resend_btn?.setOnClickListener {
            disableResendButton()
            viewModel.resendCode()
            otp_resend_btn_loading?.show()
        }
    }

    private fun disableResendButton() {
        otp_resend_btn?.visibility = View.INVISIBLE
    }

    private fun enableResendButton() {
        otp_resend_btn?.show()
    }

    private fun showToast(response: GeneralResponse) {
        response.message?.takeIf { it.isNotEmpty() }?.let {
            Toast.makeText(activity,it, Toast.LENGTH_SHORT).show()
        } ?: kotlin.run {
            response.messageResId?.let {
                Toast.makeText(activity,it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showButtonLoading() {
        otp_btn?.text = ""
        otp_btn?.isEnabled = false
        otp_btn_loading?.show()
    }

    private fun hideButtonLoading() {
        otp_btn_loading?.hide()
        otp_btn?.setText(R.string.submit)
        otp_btn?.isEnabled = true
    }
}