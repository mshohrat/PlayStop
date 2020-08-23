package com.ms.playstop.extension

import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.ui.comment.CommentsFragment
import com.ms.playstop.ui.home.HomeFragment
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlin.math.roundToInt

fun <T> Single<T>?.initSchedulers() : Single<T>? {
    return this?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun Fragment.navigate(destination: Fragment,replace: Boolean = true) {
    if(replace) {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.main_frame, destination)
            ?.commit()
    }
    else {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.add(R.id.main_frame, destination)
            ?.commit()
    }
}

fun Fragment.addOrShow(destination: Fragment) {
    var done = false
    if(destination is BaseFragment) {
        val f = childFragmentManager.findFragmentByTag(destination.tag())
        if(f != null) {
            for (fragment in childFragmentManager.fragments) {
                if(fragment.tag == destination.tag()) {
                    childFragmentManager.beginTransaction().initCustomAnimations().show(fragment).commitAllowingStateLoss()
                    done = true
                }
                else {
                    childFragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss()
                    done = true
                }
            }
        }
        else {
            for (fragment in childFragmentManager.fragments) {
                childFragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss()
            }
            if(destination.isStateSaved) {
                childFragmentManager.beginTransaction()
                    .initCustomAnimations()
                    .add(R.id.home_frame, destination, destination.tag())
                    .commitAllowingStateLoss()
                done = true
            }
            else {
                childFragmentManager.beginTransaction()
                    .initCustomAnimations()
                    .add(R.id.home_frame, destination, destination.tag())
                    .commit()
                done = true
            }
        }
    }
    else {
        if(destination.isStateSaved) {
            childFragmentManager.beginTransaction()
                .initCustomAnimations()
                .add(R.id.home_frame, destination)
                .commitAllowingStateLoss()
            done = true
        }
        else {
            childFragmentManager.beginTransaction()
                .initCustomAnimations()
                .add(R.id.home_frame, destination)
                .commit()
            done = true
        }
    }

    if(done && this is HomeFragment) {
        this.selectTabBy(destination)
    }

}

fun Fragment.add(@IdRes containerId: Int,destination: Fragment) {
    if(destination.isStateSaved) {
        childFragmentManager.beginTransaction()
            .initCustomAnimations()
            .add(containerId, destination)
            .commitAllowingStateLoss()
    }
    else {
        childFragmentManager.beginTransaction()
            .initCustomAnimations()
            .add(containerId, destination)
            .commit()
    }
}

fun convertDpToPixel(dp: Float): Int {
    return (dp * (Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

fun convertPixelToDp(px: Float): Int {
    return (px / (Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

fun Fragment.separateLengthByTime(length: Int): String {
    val hours = length / 60
    val minuets = length % 60
    if(hours == 0) {
        activity?.getString(R.string.x_minuets)?.let {
            return String.format(it,minuets.toString())
        } ?: kotlin.run {
            return minuets.toString()
        }
    } else {
        activity?.getString(R.string.x_hours_x_minuets)?.let {
            return String.format(it,hours.toString(),minuets.toString())
        } ?: kotlin.run {
            return ((hours*60) + minuets).toString()
        }
    }
}

fun BaseFragment.passHandleBackToParent() : Boolean {
    parentFragment?.takeIf { it is BaseFragment }?.let {
        return (it as BaseFragment).handleBack()
    } ?: kotlin.run {
        return false
    }
}

fun Fragment.addToParent(destination: Fragment) {
    parentFragment?.takeIf { it is BaseFragment }?.let {
        it.add((it as BaseFragment).containerId(), destination)
    }
}

fun FragmentTransaction.initCustomAnimations() : FragmentTransaction {
    return this.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out)
}
