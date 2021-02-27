package com.ms.playstop.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.microsoft.appcenter.analytics.Analytics
import com.ms.playstop.extension.hideSoftKeyboard

abstract class BaseFragment : Fragment() {

    abstract fun tag() : String

    private val SHARED_PREFERENCES_TAG = "Hawk2"
    private val sharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { preferences, key -> onSharedPreferencesChanged() }

    protected open fun onSharedPreferencesChanged() {

    }

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
        activity?.getSharedPreferences(SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE)?.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }

    override fun onDestroyView() {
        view?.hideSoftKeyboard()
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.getSharedPreferences(SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE)?.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }
}
