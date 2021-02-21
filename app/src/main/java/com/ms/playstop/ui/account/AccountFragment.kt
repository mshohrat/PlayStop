package com.ms.playstop.ui.account

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : BaseFragment() {

    companion object {
        fun newInstance() = AccountFragment()
        const val TAG = "Account Fragment"
    }

    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun tag(): String {
        return TAG
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    private fun subscribeToViewModel() {
        viewModel.account.observe(viewLifecycleOwner, Observer {
            it?.let {
                account_name_tv?.text = it.name
                account_email_tv?.text = it.email
                account_phone_tv?.text = it.phone
            }
        })
    }

    private fun subscribeToViewEvents() {
        account_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
        account_logout_btn?.setOnClickListener {
            viewModel.logout()
            activity?.onBackPressed()
        }
    }

    private fun restartApp() {
        activity?.recreate()
    }

}