package com.ms.playstop.ui.account

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.*
import com.ms.playstop.ui.completeAccount.CompleteAccountFragment
import com.ms.playstop.ui.enrerPhoneNumber.EnterPhoneNumberFragment
import com.ms.playstop.ui.movies.MoviesFragment
import com.ms.playstop.ui.movies.adapter.RequestType
import com.ms.playstop.ui.payment.PaymentFragment
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
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    private fun subscribeToViewModel() {
        viewModel.account.observe(viewLifecycleOwner, Observer {
            it?.let {
                account_name_tv?.text = it.name
                account_email_tv?.text = it.email
                account_phone_tv?.text = it.phone
                if(isSubscriptionEnabled()) {
                    account_subscription_group?.show()
                    if(it.isSubscriptionExpired == true) {
                        activity?.let {
                            account_subscription_status_tv?.setTextColor(ContextCompat.getColor(it,R.color.pure_orange_dark))
                        }
                        account_subscription_status_tv?.setText(R.string.disabled)
                        account_end_subscription_date_tv?.text = ""
                    } else {
                        activity?.let {
                            account_subscription_status_tv?.setTextColor(ContextCompat.getColor(it,R.color.pure_green_dark))
                        }
                        account_subscription_status_tv?.setText(R.string.enabled)
                        //account_end_subscription_date_tv?.text = String.format(getString(R.string.until_date_x),getJalaliDate(it.endSubscriptionDate))
                    }
                } else {
                    account_subscription_group?.hide()
                }
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

        account_subscription_purchase_btn?.setOnClickListener {
            navigateToPaymentFragment()
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

    private fun navigateToPaymentFragment() {
        val paymentFragment = PaymentFragment.newInstance()
        addToParent(paymentFragment)
    }

    private fun restartApp() {
        activity?.recreate()
    }

    override fun onSharedPreferencesChanged() {
        viewModel.loadAccountData()
    }

}