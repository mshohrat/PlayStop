package com.ms.playstop.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.microsoft.appcenter.analytics.Analytics
import com.ms.playstop.extension.hideSoftKeyboard

abstract class BaseFragment : Fragment() {

    abstract fun tag() : String

    @CallSuper
    open fun onHandleDeepLink() {
        val fragments = childFragmentManager.fragments
        for (j in 0 until fragments.size){
            val f = fragments[j]
            if(f is BaseFragment && f.isVisible) {
                f.onHandleDeepLink()
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(hidden.not()) {
            onHandleDeepLink()
        }
    }

    open fun handleBack() : Boolean {
        return false
    }

    @IdRes open fun containerId() : Int {
        return 0
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onHandleDeepLink()
        Analytics.trackEvent(tag())
    }

    override fun onDestroyView() {
        view?.hideSoftKeyboard()
        super.onDestroyView()
    }
}
