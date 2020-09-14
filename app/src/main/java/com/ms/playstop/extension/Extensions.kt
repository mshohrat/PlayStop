package com.ms.playstop.extension

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.transition.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.ms.playstop.App
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.model.Profile
import com.ms.playstop.network.model.InvalidCredentialsResponse
import com.ms.playstop.ui.home.HomeFragment
import com.orhanobut.hawk.Hawk
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import kotlin.math.roundToInt

const val TRANSITION_NAME = "Transition Name"

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
    try {
        if (replace) {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_frame, destination)
                ?.commit()
        } else {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.add(R.id.main_frame, destination)
                ?.commit()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        FirebaseCrashlytics.getInstance().recordException(e)
    }
}

fun Fragment.addOrShow(destination: Fragment) {
    try {
        var done = false
        if (destination is BaseFragment) {
            val f = childFragmentManager.findFragmentByTag(destination.tag())
            if (f != null) {
                for (fragment in childFragmentManager.fragments) {
                    if (fragment.tag == destination.tag()) {
                        childFragmentManager.beginTransaction().initCustomAnimations()
                            .show(fragment).commitAllowingStateLoss()
                        done = true
                    } else {
                        childFragmentManager.beginTransaction().hide(fragment)
                            .commitAllowingStateLoss()
                        done = true
                    }
                }
            } else {
                for (fragment in childFragmentManager.fragments) {
                    childFragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss()
                }
                if (destination.isStateSaved) {
                    childFragmentManager.beginTransaction()
                        .initCustomAnimations()
                        .add(R.id.home_frame, destination, destination.tag())
                        .commitAllowingStateLoss()
                    done = true
                } else {
                    childFragmentManager.beginTransaction()
                        .initCustomAnimations()
                        .add(R.id.home_frame, destination, destination.tag())
                        .commit()
                    done = true
                }
            }
        } else {
            if (destination.isStateSaved) {
                childFragmentManager.beginTransaction()
                    .initCustomAnimations()
                    .add(R.id.home_frame, destination)
                    .commitAllowingStateLoss()
                done = true
            } else {
                childFragmentManager.beginTransaction()
                    .initCustomAnimations()
                    .add(R.id.home_frame, destination)
                    .commit()
                done = true
            }
        }

        if (done && this is HomeFragment) {
            this.selectTabBy(destination)
        }

    } catch (e: Exception) {
        e.printStackTrace()
        FirebaseCrashlytics.getInstance().recordException(e)
    }
}

fun Fragment.add(@IdRes containerId: Int,destination: Fragment,transitionElement: View? = null) {
    try {
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
    } catch (e: Exception) {
        e.printStackTrace()
        FirebaseCrashlytics.getInstance().recordException(e)
    }

}

fun convertDpToPixel(dp: Float): Int {
    return (dp * (Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

fun convertPixelToDp(px: Float): Int {
    return (px / (Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

fun widthOfDevice(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun heightOfDevice(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
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

fun isUserLoggedIn(): Boolean {
    return if (Hawk.contains(Profile.SAVE_KEY)) {
        val profile = Hawk.get<Profile?>(Profile.SAVE_KEY)
        profile != null
    } else {
        false
    }
}

fun isDeviceOnline(): Boolean {
    val cm = App.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT < 23) {
        cm?.activeNetworkInfo?.let {
            return it.isConnected
        }
        return false

    } else {
        cm?.activeNetwork?.let {
            cm.getNetworkCapabilities(it)?.let { networkCapabilities ->
                return if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) && networkCapabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    true
                } else {
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                }

            }
        }
        return false
    }
}

fun CharSequence.isValidEmail() : Boolean {
    return !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
val Fragment.isGooglePlayServicesAvailable: Boolean
    get() {
        try {
            val status = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(activity)
            if (status == ConnectionResult.SUCCESS) {
                return true
            }
        } catch (e: Exception) {
        }
        return false
    }

fun <T> Throwable.getErrorHttpModel(type: Class<T>) : T? {
    return if(this is HttpException){
        val body = this.response()?.errorBody()
        if(body != null ){
            try {
                Gson().fromJson(body.string(),type)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
    else {
        null
    }
}

fun Throwable.isUnauthorizedError() : Boolean {
    return if(this is HttpException) {
        this.code() == 401
    }
    else {
        false
    }
}

fun InvalidCredentialsResponse.getFirstMessage() : String? {
    invalidCredentialsData?.let { errors ->
        return when {
            errors.nameErrors?.isNotEmpty() == true -> {
                errors.nameErrors[0]
            }
            errors.emailErrors?.isNotEmpty() == true -> {
                errors.emailErrors[0]
            }
            errors.passwordErrors?.isNotEmpty() == true -> {
                errors.passwordErrors[0]
            }
            else -> {
                null
            }
        }
    } ?: kotlin.run {
        return null
    }
}

fun Activity.updateStatusBarColor(@ColorRes colorId: Int, isStatusBarFontDark: Boolean = false) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, colorId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val lFlags = window.decorView.systemUiVisibility
            // Update the SystemUiVisibility depending on whether we want a Light or Dark theme.
            window.decorView.systemUiVisibility =
                if (isStatusBarFontDark) lFlags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() else lFlags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}