package com.ms.playstop.ui.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.addToParent
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.ui.signup.SignupFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    companion object {
        fun newInstance() = LoginFragment()
        const val TAG = "Login Fragment"
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun tag(): String {
        return TAG
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    private fun subscribeToViewModel() {
        viewModel.login.observe(viewLifecycleOwner, Observer {
            showToast(it)
            activity?.onBackPressed()
        })

        viewModel.loginError.observe(viewLifecycleOwner, Observer {
            showToast(it)
        })
    }

    private fun subscribeToViewEvents() {
        login_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }

        login_not_have_account_btn?.setOnClickListener {
            val signupFragment = SignupFragment.newInstance()
            addToParent(signupFragment)
        }

        login_btn?.setOnClickListener {
            viewModel.login(
                login_email_et?.text?.toString(),
                login_password_et?.text?.toString()
            )
        }
    }

    private fun showToast(response: GeneralResponse) {
        response.message?.takeIf { it.isNotEmpty() }?.let {
            Toast.makeText(activity,it,Toast.LENGTH_SHORT).show()
        } ?: kotlin.run {
            response.messageResId?.let {
                Toast.makeText(activity,it,Toast.LENGTH_SHORT).show()
            }
        }
    }

}