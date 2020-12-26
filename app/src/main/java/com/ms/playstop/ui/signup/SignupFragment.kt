package com.ms.playstop.ui.signup

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
import kotlinx.android.synthetic.main.fragment_login.*
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
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    private fun subscribeToViewModel() {
        viewModel.signup.observe(viewLifecycleOwner, Observer {
            showToast(it)
            activity?.onBackPressed()
        })

        viewModel.signupError.observe(viewLifecycleOwner, Observer {
            showToast(it)
        })
    }

    private fun subscribeToViewEvents() {
        signup_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }

        signup_have_account_btn?.setOnClickListener {
            activity?.onBackPressed()
        }

        signup_btn?.setOnClickListener {
            viewModel.signup(
                signup_name_et?.text?.toString(),
                signup_email_et?.text?.toString(),
                signup_password_et?.text?.toString(),
                signup_repeat_password_et?.text?.toString()
            )
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

}