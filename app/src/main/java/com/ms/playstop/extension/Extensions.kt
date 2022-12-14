package com.ms.playstop.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import cab.snapp.extensions.calendar.JalaliCalendarTool
import com.bumptech.glide.Glide
import com.bumptech.glide.request.BaseRequestOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.appindexing.Action
import com.google.firebase.appindexing.FirebaseAppIndex
import com.google.firebase.appindexing.FirebaseUserActions
import com.google.firebase.appindexing.Indexable
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.microsoft.appcenter.crashes.Crashes
import com.ms.playstop.App
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.model.Profile
import com.ms.playstop.network.model.ConfigResponse
import com.ms.playstop.network.model.InvalidCredentialsResponse
import com.orhanobut.hawk.Hawk
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*
import java.util.regex.Pattern
import kotlin.math.roundToInt

const val TRANSITION_NAME = "Transition Name"
private val PHONENUMBER_PATTERN = Pattern.compile("^(09)[0-9]{9}")

fun <T> Single<T>?.initSchedulers() : Single<T>? {
    return this?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

@SuppressLint("WrongConstant")
fun View.hideSoftKeyboard() {
    try {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun View.showSoftKeyboard() {
    try {
        val imm =
            context.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        this.requestFocus()
        imm.showSoftInput(this, 0)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

fun View.isSoftKeyboardOpen(): Boolean {
    return if (parent is ViewGroup) {
        val r = Rect()
        (parent as ViewGroup).getWindowVisibleDisplayFrame(r)

        val heightDiff = (parent as ViewGroup).rootView.height - (r.bottom - r.top)
        val dp: Float = heightDiff / rootView.resources.displayMetrics.density
        val isVisible = dp > 100
        isVisible
    } else false
}

fun Fragment.navigate(destination: Fragment,replace: Boolean = true) {
    try {
        if (replace) {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_frame, destination,if(destination is BaseFragment) destination.tag() else "")
                ?.commit()
        } else {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.add(R.id.main_frame, destination,if(destination is BaseFragment) destination.tag() else "")
                ?.commit()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        FirebaseCrashlytics.getInstance().recordException(e)
        Crashes.trackError(e)
    }
}

fun Fragment.addOrShow(destination: Fragment) {
    try {
        if (destination is BaseFragment) {
            val f = childFragmentManager.findFragmentByTag(destination.tag())
            if (f != null) {
                for (fragment in childFragmentManager.fragments) {
                    if (fragment.tag == destination.tag()) {
                        if(fragment.isVisible.not()) {
                            childFragmentManager.beginTransaction().initCustomAnimations()
                                .show(fragment).commitAllowingStateLoss()
                        }
                    } else {
                        childFragmentManager.beginTransaction().hide(fragment)
                            .commitAllowingStateLoss()
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
                } else {
                    childFragmentManager.beginTransaction()
                        .initCustomAnimations()
                        .add(R.id.home_frame, destination, destination.tag())
                        .commit()
                }
            }
        } else {
            if (destination.isStateSaved) {
                childFragmentManager.beginTransaction()
                    .initCustomAnimations()
                    .add(R.id.home_frame, destination)
                    .commitAllowingStateLoss()
            } else {
                childFragmentManager.beginTransaction()
                    .initCustomAnimations()
                    .add(R.id.home_frame, destination)
                    .commit()
            }
        }

    } catch (e: Exception) {
        e.printStackTrace()
        FirebaseCrashlytics.getInstance().recordException(e)
        Crashes.trackError(e)
    }
}

fun Fragment.add(@IdRes containerId: Int,destination: Fragment,withAnimation: Boolean = true) {
    try {
        if (destination.isStateSaved) {
            childFragmentManager.beginTransaction()
                .initCustomAnimations()
                .add(containerId, destination, if(destination is BaseFragment) destination.tag() else "")
                .commitAllowingStateLoss()
        } else {
            childFragmentManager.beginTransaction()
                .initCustomAnimations()
                .add(containerId, destination, if(destination is BaseFragment) destination.tag() else "")
                .commit()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        FirebaseCrashlytics.getInstance().recordException(e)
        Crashes.trackError(e)
    }

}

fun Fragment.replace(@IdRes containerId: Int,destination: Fragment,withAnimation: Boolean = true) {
    try {
        if (destination.isStateSaved) {
            childFragmentManager.beginTransaction()
                .initCustomAnimations()
                .replace(containerId, destination, if(destination is BaseFragment) destination.tag() else "")
                .commitAllowingStateLoss()
        } else {
            childFragmentManager.beginTransaction()
                .initCustomAnimations()
                .replace(containerId, destination, if(destination is BaseFragment) destination.tag() else "")
                .commit()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        FirebaseCrashlytics.getInstance().recordException(e)
        Crashes.trackError(e)
    }

}

fun Fragment.remove(destination: Fragment,withAnimation: Boolean = true) {
    try {
        if(withAnimation) {
            if (destination.isStateSaved) {
                childFragmentManager.beginTransaction()
                    .initCustomAnimations()
                    .remove(destination)
                    .commitAllowingStateLoss()
            } else {
                childFragmentManager.beginTransaction()
                    .initCustomAnimations()
                    .remove(destination)
                    .commit()
            }
        } else {
            if (destination.isStateSaved) {
                childFragmentManager.beginTransaction()
                    .remove(destination)
                    .commitAllowingStateLoss()
            } else {
                childFragmentManager.beginTransaction()
                    .remove(destination)
                    .commit()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        FirebaseCrashlytics.getInstance().recordException(e)
        Crashes.trackError(e)
    }

}

fun Fragment.hasChild(tag: String): Boolean {
    return childFragmentManager.findFragmentByTag(tag) != null
}

fun Fragment.removeAllChildren(withAnimation: Boolean = true,skipStickyChildren: Boolean = true) {
    try {
        val fragments = childFragmentManager.fragments
        if(this is BaseFragment && skipStickyChildren) {
            if(fragments.size >= this.stickyChildrenCount()) {
                for (i in 0 until this.stickyChildrenCount()) {
                    fragments.removeFirstOrNull()
                }
            }
        }
        val size = fragments.size
        for (i in 0 .. size){
            val child = fragments[i]
            child?.let {
                if(withAnimation && i == size - 1) {
                    if (child.isStateSaved) {
                        childFragmentManager.beginTransaction()
                            .initCustomAnimations()
                            .remove(child)
                            .commitAllowingStateLoss()
                    } else {
                        childFragmentManager.beginTransaction()
                            .initCustomAnimations()
                            .remove(child)
                            .commit()
                    }
                } else {
                    if (child.isStateSaved) {
                        childFragmentManager.beginTransaction()
                            .remove(child)
                            .commitAllowingStateLoss()
                    } else {
                        childFragmentManager.beginTransaction()
                            .remove(child)
                            .commit()
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        FirebaseCrashlytics.getInstance().recordException(e)
        Crashes.trackError(e)
    }
}

private fun saveGuideShown(key: String) {
    Hawk.put(key,true)
}

fun isGuideShown(key: String) : Boolean {
    return Hawk.contains(key) && Hawk.get<Boolean>(key,false)
}

fun Fragment.showGuide(guidePair: Pair<Pair<Int,Int>,Pair<String,View?>>, function:(()->(Unit))? = null) {
    activity?.showGuide(guidePair,function)
}

fun Fragment.showSequenceGuide(guidePairList: List<Pair<Pair<Int,Int>,Pair<String,View?>>>) {
    activity?.showSequenceGuide(guidePairList)
}

fun Activity.showGuide(guidePair: Pair<Pair<Int,Int>,Pair<String,View?>>, function:(()->(Unit))? = null) {
    try {
        if(isGuideShown(guidePair.second.first)) {
            function?.invoke()
            return
        }
        guidePair.second.second?.let { target ->
            val currentVisibility = target.visibility
            if(currentVisibility != View.VISIBLE) {
                target.visibility = View.VISIBLE
            }
            GuideView.Builder(this) //Activity instance
                .setTitle(getString(guidePair.first.first)) //Any title for the bubble view
                .setContentText(getString(guidePair.first.second))
                .setTargetView(target) //View to point out
                .setDismissType(DismissType.anywhere)
                .setGravity(Gravity.auto)
                .setGuideListener {
                    saveGuideShown(guidePair.second.first)
                    target.visibility = currentVisibility
                    function?.invoke()
                }
                .build()
                .show()
        }
    } catch (ex: java.lang.Exception) {
        Crashes.trackError(ex)
    }
}

fun Activity.showSequenceGuide(guidePairList: List<Pair<Pair<Int,Int>,Pair<String,View?>>>) {
    var i = 0
    val function = object : () -> (Unit) {
        override fun invoke() {
            if(i < guidePairList.size - 1) {
                 i++
                 showGuide(guidePairList[i],this)
            }
        }
    }
    showGuide(guidePairList[i],function)
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

fun String.isFile() : Boolean {
    if(this.isEmpty() || this.contains(".").not()) {
        return false
    }
    val uri = Uri.parse(this)
    val lastSegment = uri.lastPathSegment?.toLowerCase(Locale.getDefault())
    if(lastSegment.isNullOrEmpty()) {
        return false
    }
    return lastSegment.contains("mkv",true)
            || lastSegment.contains("mp4",true)
            || lastSegment.contains("m3u8",true)
            || lastSegment.contains("srt",true)
            || lastSegment.contains("txt",true)
            || lastSegment.contains("vtt",true)
            || lastSegment.contains("ssa",true)
}

fun Fragment.addToParent(destination: Fragment) {
    parentFragment?.takeIf { it is BaseFragment }?.let {
        it.add((it as BaseFragment).containerId(), destination)
    }
}

fun Fragment.replaceInParent(destination: Fragment) {
    parentFragment?.takeIf { it is BaseFragment }?.let {
        it.replace((it as BaseFragment).containerId(), destination)
    }
}

fun Fragment.removeFromParent(destination: Fragment,withAnimation: Boolean = true) {
    parentFragment?.remove(destination,withAnimation)
}

fun Fragment.removeLastFromParent(numbers: Int = 1,skipNumbers: Int = 1) {
    val fragments = parentFragment?.childFragmentManager?.fragments
    fragments?.takeIf { it.isNotEmpty() && it.size >= numbers + skipNumbers - 1 }?.let {
        it.reverse()
        for (i in skipNumbers - 1 until numbers + skipNumbers - 1) {
            val f = fragments[i]
            parentFragment?.remove(f,i == numbers + skipNumbers - 2)
        }
    }
}

fun FragmentTransaction.initCustomAnimations() : FragmentTransaction {
    return this
}

fun isUserLoggedIn(): Boolean {
    return if (Hawk.contains(Profile.SAVE_KEY)) {
        val profile = Hawk.get(Profile.SAVE_KEY) as? Profile
        profile != null
    } else {
        false
    }
}

fun isUserPhoneVerified(): Boolean {
    return if (Hawk.contains(Profile.SAVE_KEY)) {
        val profile = Hawk.get(Profile.SAVE_KEY) as? Profile
        profile != null && profile.isPhoneVerified
    } else {
        false
    }
}

fun isUserActive(): Boolean {
    return if (Hawk.contains(Profile.SAVE_KEY)) {
        val profile = Hawk.get(Profile.SAVE_KEY) as? Profile
        profile != null && profile.isActive
    } else {
        false
    }
}

fun isSubscriptionEnabled(): Boolean {
    return if (Hawk.contains(ConfigResponse.SAVE_KEY)) {
        val config = Hawk.get(ConfigResponse.SAVE_KEY) as? ConfigResponse
        config?.features?.isSubscriptionEnabled ?: false
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

fun isVpnActive(): Boolean {
    var iface = ""
    try {
        for (networkInterface in Collections.list(NetworkInterface.getNetworkInterfaces())) {
            if (networkInterface.isUp) iface = networkInterface.name
            if (iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                return true
            }
        }
    } catch (e1: SocketException) {
        e1.printStackTrace()
    }
    return false
}

fun CharSequence.isValidEmail() : Boolean {
    return !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun CharSequence.isValidPhoneNumber() : Boolean {
    return !isNullOrEmpty() && PHONENUMBER_PATTERN.matcher(this.toString().convertToEnglishNumber()).matches()
}

fun CharSequence.isValidCharacterName(): Boolean {
    return this.isNotEmpty() && (this == "???????? ????????").not() && (this == "???????? ????????").not()
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
            errors.phoneErrors?.isNotEmpty() == true -> {
                errors.phoneErrors[0]
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

fun Activity.hasSoftKeys(): Boolean {
//    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
//        val d = windowManager.defaultDisplay
//        val realDisplayMetrics = DisplayMetrics()
//        d.getRealMetrics(realDisplayMetrics)
//        val realHeight = realDisplayMetrics.heightPixels
//        val realWidth = realDisplayMetrics.widthPixels
//        val displayMetrics = DisplayMetrics()
//        d.getMetrics(displayMetrics)
//        val displayHeight = displayMetrics.heightPixels
//        val displayWidth = displayMetrics.widthPixels
//        return realWidth - displayWidth > 0 || realHeight - displayHeight > 0
//    } else {
//        val hasMenuKey: Boolean = ViewConfiguration.get(this).hasPermanentMenuKey()
//        val hasBackKey: Boolean = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
//        return !hasMenuKey && !hasBackKey
//    }

    val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
    return id > 0 && resources.getBoolean(id)
}

fun Activity.hasNotch(): Boolean {
    val d = windowManager.defaultDisplay
    val realDisplayMetrics = DisplayMetrics()
    d.getRealMetrics(realDisplayMetrics)
    val realHeight = realDisplayMetrics.heightPixels
    val realWidth = realDisplayMetrics.widthPixels
    val displayMetrics = DisplayMetrics()
    d.getMetrics(displayMetrics)
    val displayHeight = displayMetrics.heightPixels
    val displayWidth = displayMetrics.widthPixels
    return realWidth - displayWidth > 0 || realHeight - displayHeight > 0
}

fun Activity.setStatusBarColor(
    @ColorInt color: Int
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window?.let { window ->

            // set status bar icons color to White or Black based on status bar's color
            val contrastColor = getContrastColor(color)
            setStatusBarTheme(contrastColor == Color.WHITE)

            // on APIs lower than M (basically 21 and 22), status bar's color shouldn't be white
            // so set a darker color so that icons will be visible
            val standardColor =
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && color == Color.WHITE)
                    ContextCompat.getColor(this, R.color.colorAccentDark)
                else color

            // set status bar color
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = standardColor
        }
    }
}

private fun Activity.setStatusBarTheme(isDark: Boolean = false) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        // Fetch the current flags.
        val flags = window.decorView.systemUiVisibility
        // Update the SystemUiVisibility dependening on whether we want a Light or Dark theme.
        window.decorView.systemUiVisibility =
            if (isDark) flags.and(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
            else flags.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }
}

private fun getContrastColor(@ColorInt color: Int): Int {
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)
    val lum = (((0.299 * red) + ((0.587 * green) + (0.114 * blue))))
    return if (lum > 186) Color.BLACK else Color.WHITE
}

fun String.convertToPersianNumber(): String {
    return this
        .replace("0", "??")
        .replace("1", "??")
        .replace("2", "??")
        .replace("3", "??")
        .replace("4", "??")
        .replace("5", "??")
        .replace("6", "??")
        .replace("7", "??")
        .replace("8", "??")
        .replace("9", "??")
}

fun String.convertToEnglishNumber(): String {
    return this
        .replace("??", "0")
        .replace("??", "1")
        .replace("??", "2")
        .replace("??", "3")
        .replace("??", "4")
        .replace("??", "5")
        .replace("??", "6")
        .replace("??", "7")
        .replace("??", "8")
        .replace("??", "9")
}

fun startLogMovie(movieName: String?) {
    FirebaseUserActions.getInstance().start(
        Action.Builder(Action.Builder.VIEW_ACTION).setObject(
            " ???????????? ???????? ???????? ???????????? ${movieName}",
            "//playstop.ir/????????????-????????-${movieName}/")
            .setMetadata(Action.Metadata.Builder().setUpload(true))
            .build())
}

fun endLogAndIndexMovie(movieName: String?) {
    val indexable = Indexable.Builder()
        .setName(" ???????????? ???????? ???????? ???????????? ${movieName}")
        .setUrl("//playstop.ir/????????????-????????-${movieName}/")
        .setKeywords("????????????","??????????","????????","???????? ????????????")
        .build()
    FirebaseAppIndex.getInstance().update(indexable)
    FirebaseUserActions.getInstance().end(
        Action.Builder(Action.Builder.VIEW_ACTION).setObject(
            " ???????????? ???????? ???????? ???????????? ${movieName}",
            "//playstop.ir/????????????-????????-${movieName}/")
            .setMetadata(Action.Metadata.Builder().setUpload(true))
            .build())
}

fun getJalaliDateTime(gregorianDate: String?): String {
    try {
        gregorianDate?.let {
            val splitData =
                gregorianDate.split(" ".toRegex()).toTypedArray()
            val date = splitData[0]
            val time = splitData[1]
            val splitDate = date.split("-".toRegex()).toTypedArray()
            val splitTime = time.split(":".toRegex()).toTypedArray()
            val jalaliCalendarTool =
                JalaliCalendarTool(
                    splitDate[0].toInt(),
                    splitDate[1].toInt(),
                    splitDate[2].toInt()
                )
            return jalaliCalendarTool.iranianDate + " " + splitTime[0] + ":" + splitTime[1]
        } ?: run {
            return ""
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun getJalaliDate(gregorianDate: String?): String {
    try {
        gregorianDate?.let {
            val splitData =
                gregorianDate.split(" ".toRegex()).toTypedArray()
            val date = splitData[0]
            val splitDate = date.split("-".toRegex()).toTypedArray()
            val jalaliCalendarTool =
                JalaliCalendarTool(
                    splitDate[0].toInt(),
                    splitDate[1].toInt(),
                    splitDate[2].toInt()
                )
            return jalaliCalendarTool.iranianDate
        } ?: run {
            return ""
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun Context.getResourceFromThemeAttribute(@AttrRes attributeResId: Int, defaultValue: Int): Int {
    val typedValue = TypedValue()
    return if (theme.resolveAttribute(attributeResId, typedValue, true)) {
        typedValue.resourceId
    } else defaultValue
}

fun loadImage(imageView: ImageView?, imageUrl: String?, requestOptionsList: List<BaseRequestOptions<*>>? = null, retryCount: Int = 1) {
    imageView?.let { iv ->
        imageUrl?.takeIf { it.isNotEmpty() }?.let { url ->
            iv.context?.let { ctx ->
                try {
                    val requestBuilder = Glide.with(ctx)
                        .load(url)
                    requestOptionsList?.takeIf { it.isNotEmpty() }?.let {
                        for (option in it){
                            requestBuilder.apply(option)
                        }
                    }
                    requestBuilder.into(iv)
                } catch (e: Exception) {
                    Crashes.trackError(e)
                }
            }
        }
    }
}