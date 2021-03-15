package com.ms.playstop.ui.account

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.addToParent
import com.ms.playstop.ui.completeAccount.CompleteAccountFragment
import com.ms.playstop.ui.enrerPhoneNumber.EnterPhoneNumberFragment
import com.ms.playstop.ui.movies.MoviesFragment
import com.ms.playstop.ui.movies.adapter.RequestType
import com.ms.playstop.ui.settings.SettingsFragment
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

        account_settings_btn?.setOnClickListener {
            addToParent(SettingsFragment.newInstance())
        }

        account_liked_movies_btn?.setOnClickListener {
            val moviesFragment = MoviesFragment.newInstance()
            moviesFragment.arguments = Bundle().apply {
                this.putInt(MoviesFragment.MOVIES_REQUEST_TYPE, RequestType.LIKES.type)
                this.putString(MoviesFragment.MOVIES_REQUEST_NAME,getString(R.string.liked_movies))
            }
            addToParent(moviesFragment)
        }

        account_name_edit_ib?.setOnClickListener {
            navigateToEditAccountFragment()
        }

        account_email_edit_ib?.setOnClickListener {
            navigateToEditAccountFragment()
        }

        account_phone_edit_ib?.setOnClickListener {
            navigateToEditPhoneFragment()
        }
    }

    private fun navigateToEditAccountFragment() {
        val completeAccountFragment = CompleteAccountFragment.newInstance()
        completeAccountFragment.arguments = Bundle().apply {
            putInt(
                CompleteAccountFragment.COMPLETE_ACCOUNT_STATE
                , CompleteAccountFragment.COMPLETE_ACCOUNT_STATE_EDIT)
        }
        addToParent(completeAccountFragment)
    }

    private fun navigateToEditPhoneFragment() {
        val enterPhoneNumberFragment = EnterPhoneNumberFragment.newInstance()
        val args = Bundle().apply {
            putInt(EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE, EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE_EDIT)
        }
        enterPhoneNumberFragment.arguments = args
        addToParent(enterPhoneNumberFragment)
    }

    private fun restartApp() {
        activity?.recreate()
    }

    override fun onSharedPreferencesChanged() {
        viewModel.loadAccountData()
    }

}