package com.ms.playstop.ui.forgotPassword

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.hideSoftKeyboard
import com.ms.playstop.extension.show
import com.ms.playstop.network.model.GeneralResponse
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_signup.*

class ForgotPasswordFragment : BaseFragment() {

    companion object {
        fun newInstance() = ForgotPasswordFragment()
    }

    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun tag(): String {
        return "Forgot Password Fragment"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
        subscribeToViewEvents()
        subscribeToViewModel()
    }

    private fun subscribeToViewEvents() {
        forgot_password_btn?.setOnClickListener {
            showButtonLoading()
            viewModel.resetPassword(forgot_password_email_et?.text?.toString())
        }

        forgot_password_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }

        forgot_password_root?.setOnClickListener {
            it.hideSoftKeyboard()
        }
    }

    private fun subscribeToViewModel() {
        viewModel.resetPassword.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it)
            activity?.onBackPressed()
        })

        viewModel.resetPasswordError.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it)
        })
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
        forgot_password_btn?.text = ""
        forgot_password_btn?.isEnabled = false
        forgot_password_btn_loading?.show()
    }

    private fun hideButtonLoading() {
        forgot_password_btn_loading?.hide()
        forgot_password_btn?.setText(R.string.reset)
        forgot_password_btn?.isEnabled = true
    }

}