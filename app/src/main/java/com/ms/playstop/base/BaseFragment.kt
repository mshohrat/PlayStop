package com.ms.playstop.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.microsoft.appcenter.analytics.Analytics
import com.ms.playstop.R
import com.ms.playstop.extension.hideSoftKeyboard
import com.ms.playstop.extension.setStatusBarColor
import com.ms.playstop.utils.OnDayNightModeChangeListener

abstract class BaseFragment : Fragment(), OnDayNightModeChangeListener {

    abstract fun tag() : String

    private val SHARED_PREFERENCES_TAG = "Hawk2"
    private val sharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
        onSharedPreferencesChanged(key)
    }
    private var isActivityCreated = false
    private var isViewLoadedCalled = false

    open fun stickyChildrenCount() : Int {
        return 0
    }

    open fun onChildIsRemoving(removingChild: Fragment) {
        if(hasChild()) {
            getLastSkipOneChild()?.takeIf { it is BaseFragment }?.let {
                (it as BaseFragment).onSetStatusBarColor()
            } ?: kotlin.run {
                onSetStatusBarColor()
            }
        } else {
            onSetStatusBarColor()
        }
    }

    protected fun getLastSkipOneChild() : Fragment? {
        return childFragmentManager.fragments.takeIf { it.size > 1  }?.let {
            it[it.lastIndex - 1]
        }
    }

    protected fun hasChild() : Boolean {
        return childFragmentManager.fragments.isNotEmpty()
    }

    protected open fun onSetStatusBarColor() {
        activity?.let { ctx ->
            ctx.setStatusBarColor(ContextCompat.getColor(ctx, R.color.colorAccentDark))
        }
    }

    protected open fun onSharedPreferencesChanged(key: String) {

    }

    protected open fun getEnterAnimation() : Animation? {
        return null
    }

    protected open fun getExitAnimation() : Animation? {
        return null
    }

    protected open fun isEnterAnimationEnabled() : Boolean {
        return true
    }

    protected open fun isExitAnimationEnabled() : Boolean {
        return true
    }

    override fun onDayNightModeApplied(type: Int) {
        onSetStatusBarColor()
    }

    @CallSuper
    protected open fun onViewLoaded() {
        isViewLoadedCalled = true
        onHandleDeepLink()
        Analytics.trackEvent(tag())
        activity?.getSharedPreferences(SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE)?.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
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
            onSetStatusBarColor()
            if(hasChild()) {
                callOnSetStatusBarColorForChildren()
            }
        }
    }

    private fun callOnSetStatusBarColorForChildren() {
        for (fragment in childFragmentManager.fragments){
            if(fragment is BaseFragment) {
                (fragment as BaseFragment).onSetStatusBarColor()
            }
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
        isActivityCreated = true
        onSetStatusBarColor()
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        activity?.let { ctx ->
            if(enter){
                if(isEnterAnimationEnabled()) {
                    val animation = getEnterAnimation() ?: AnimationUtils.loadAnimation(ctx, R.anim.slide_in_right)
                    animation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {

                        }

                        override fun onAnimationEnd(animation: Animation?) {
                            if (isActivityCreated && isViewLoadedCalled.not()) {
                                onViewLoaded()
                            }
                        }

                        override fun onAnimationRepeat(animation: Animation?) {

                        }

                    })
                    return animation
                } else {
                    if (isActivityCreated && isViewLoadedCalled.not()) {
                        onViewLoaded()
                    }
                    return null
                }
            } else {
                return if(isExitAnimationEnabled()) {
                    getExitAnimation() ?: AnimationUtils.loadAnimation(ctx, R.anim.slide_out_right)
                } else {
                    null
                }
            }
        } ?: kotlin.run {
            return null
        }
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
