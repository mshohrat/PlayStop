package com.ms.playstop.ui.otp

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.hideSoftKeyboard
import com.ms.playstop.extension.removeLastFromParent
import com.ms.playstop.extension.show
import com.ms.playstop.network.model.GeneralResponse
import kotlinx.android.synthetic.main.fragment_otp.*
import kotlinx.android.synthetic.main.fragment_signup.*

class OtpFragment : BaseFragment() {

    companion object {
        fun newInstance() = OtpFragment()
        const val USER_PHONE = "User Phone"
        const val VERIFY_STATE = "Verify State"
        const val VERIFY_STATE_LOGIN = 2
        const val VERIFY_STATE_NONE = 0
    }

    private lateinit var viewModel: OtpViewModel

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
    }


    private fun subscribeToViewModel() {
        viewModel.verifyOtp.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it)
            removeLastFromParent(2)
        })
        viewModel.loginOtp.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it)
            removeLastFromParent(2)
        })
        viewModel.otpError.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it)
        })
    }

    private fun subscribeToViewEvents() {
        otp_pin_entry?.setOnPinEnteredListener {
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