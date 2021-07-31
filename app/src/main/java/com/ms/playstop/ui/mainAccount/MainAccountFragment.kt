package com.ms.playstop.ui.mainAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.*
import com.ms.playstop.ui.account.AccountFragment
import com.ms.playstop.ui.enrerPhoneNumber.EnterPhoneNumberFragment
import com.ms.playstop.ui.otp.OtpFragment

class MainAccountFragment : BaseFragment() {

    companion object {
        fun newInstance() = MainAccountFragment()
        const val TAG = "Main Account Fragment"
    }
    override fun tag(): String {
        return TAG
    }

    override fun stickyChildrenCount(): Int {
        return 1
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun isEnterAnimationEnabled(): Boolean {
        return false
    }

    override fun isExitAnimationEnabled(): Boolean {
        return false
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        if(isUserLoggedIn()) {
            add(containerId(),AccountFragment.newInstance(),false)
        } else {
            val enterPhoneNumberFragment = EnterPhoneNumberFragment.newInstance()
            val args = Bundle().apply {
                putInt(EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE, EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE_LOGIN)
            }
            enterPhoneNumberFragment.arguments = args
            add(containerId(),enterPhoneNumberFragment,false)
        }
    }

    override fun containerId(): Int {
        return R.id.main_account_root
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_account, container, false)
    }

    override fun handleBack(): Boolean {
        return passHandleBackToParent()
    }

    fun handleAccountRemoved() {
        removeAllChildren(withAnimation = false, skipStickyChildren = false)
        if(isUserLoggedIn()) {
            add(containerId(),AccountFragment.newInstance(),false)
        } else {
            val enterPhoneNumberFragment = EnterPhoneNumberFragment.newInstance()
            val args = Bundle().apply {
                putInt(EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE, EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE_LOGIN)
            }
            enterPhoneNumberFragment.arguments = args
            add(containerId(),enterPhoneNumberFragment,false)
        }
    }

    fun handleAccountAdded() {
        removeAllChildren(withAnimation = false, skipStickyChildren = false)
        if(isUserLoggedIn()) {
            add(containerId(),AccountFragment.newInstance(),false)
        } else {
            val enterPhoneNumberFragment = EnterPhoneNumberFragment.newInstance()
            val args = Bundle().apply {
                putInt(EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE, EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE_LOGIN)
            }
            enterPhoneNumberFragment.arguments = args
            add(containerId(),enterPhoneNumberFragment,false)
        }
    }

    override fun onSharedPreferencesChanged() {
        if(isUserLoggedIn()) {
            if(hasChild(AccountFragment.TAG).not()) {
                removeAllChildren(withAnimation = false, skipStickyChildren = false)
                add(containerId(), AccountFragment.newInstance(), false)
            }
        } else {
            if(hasChild(EnterPhoneNumberFragment.TAG).not() && hasChild(OtpFragment.TAG).not()) {
                removeAllChildren(withAnimation = false, skipStickyChildren = false)
                val enterPhoneNumberFragment = EnterPhoneNumberFragment.newInstance()
                val args = Bundle().apply {
                    putInt(EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE, EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE_LOGIN)
                }
                enterPhoneNumberFragment.arguments = args
                add(containerId(),enterPhoneNumberFragment,false)
            }
        }
    }


}