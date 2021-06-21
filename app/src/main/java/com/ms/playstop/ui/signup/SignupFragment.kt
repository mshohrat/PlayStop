package com.ms.playstop.ui.signup

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.*
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.ui.enrerPhoneNumber.EnterPhoneNumberFragment
import com.ms.playstop.ui.login.LoginFragment
import kotlinx.android.synthetic.main.fragment_signup.*

class SignupFragment : BaseFragment() {

    companion object {
        fun newInstance() = SignupFragment()
        const val TAG = "Signup Fragment"
    }

    private lateinit var viewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun tag(): String {
        return TAG
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignupViewModel::class.java)
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    private fun subscribeToViewModel() {
        viewModel.signup.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it)
            val enterPhoneNumberFragment = EnterPhoneNumberFragment.newInstance()
            val args = Bundle().apply {
                putInt(EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE,EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE_ADD)
            }
            enterPhoneNumberFragment.arguments = args
            addToParent(enterPhoneNumberFragment)
            removeFromParent(this)
        })

        viewModel.signupError.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it)
        })
    }

    private fun subscribeToViewEvents() {
        signup_back_btn?.setOnClickListener {
            if(it.isSoftKeyboardOpen()) {
                it.hideSoftKeyboard()
            }
            addToParent(LoginFragment.newInstance())
            removeFromParent(this)
        }

        signup_have_account_btn?.setOnClickListener {
            if(it.isSoftKeyboardOpen()) {
                it.hideSoftKeyboard()
            }
            addToParent(LoginFragment.newInstance())
            removeFromParent(this)
        }

        signup_btn?.setOnClickListener {
            showButtonLoading()
            viewModel.signup(
                signup_name_et?.text?.toString(),
                signup_email_et?.text?.toString(),
                signup_password_et?.text?.toString(),
                signup_repeat_password_et?.text?.toString()
            )
        }

        signup_root?.setOnClickListener {
            signup_name_et?.hideSoftKeyboard()
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

    override fun handleBack(): Boolean {
        addToParent(LoginFragment.newInstance())
        removeFromParent(this)
        return true
    }

    private fun showButtonLoading() {
        signup_btn?.text = ""
        signup_btn?.isEnabled = false
        signup_btn_loading?.show()
    }

    private fun hideButtonLoading() {
        signup_btn_loading?.hide()
        signup_btn?.setText(R.string.submit)
        signup_btn?.isEnabled = true
    }

}