package com.ms.playstop

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.microsoft.appcenter.crashes.Crashes
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.*
import com.ms.playstop.model.*
import com.ms.playstop.ui.settings.SettingsFragment
import com.ms.playstop.ui.settings.adapter.SettingNightModeAdapter
import com.ms.playstop.ui.splash.SplashFragment
import com.ms.playstop.utils.ReviewDialog
import com.ms.playstop.utils.VpnDialog
import com.orhanobut.hawk.Hawk
import java.net.URLDecoder
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    var deepLink: DeepLink? = null
    val hasDeepLink: Boolean
    get() {
        return deepLink != null
    }
    private var vpnDialog: VpnDialog? = null
    private var reviewDialog: ReviewDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            val w = window
//            //w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//            //w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            w.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
//            w.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//            //w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary))
        supportFragmentManager.fragments.size.takeIf { it == 0 }.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame,
                    SplashFragment.newInstance())
                .commit()
        }
        handleDeepLink(intent)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val nightMode = if(Hawk.contains(SettingsFragment.SETTING_DAY_NIGHT_MODE_KEY)) {
            Hawk.get<Int>(SettingsFragment.SETTING_DAY_NIGHT_MODE_KEY)
        } else {
            SettingNightModeAdapter.TYPE_DAY_NIGHT_MODE_AUTO
        }
        val nightModeHandled = if(Hawk.contains(SettingsFragment.SETTING_DAY_NIGHT_MODE_HANDLED_KEY)) {
            Hawk.get<Int>(SettingsFragment.SETTING_DAY_NIGHT_MODE_HANDLED_KEY)
        } else {
            SettingNightModeAdapter.TYPE_DAY_NIGHT_MODE_AUTO
        }
        if(nightMode != nightModeHandled) {
            Hawk.put(SettingsFragment.SETTING_DAY_NIGHT_MODE_HANDLED_KEY,nightMode)
            callDayNightModeChangedForFragments(nightMode)
        } else{
            if(nightMode == SettingNightModeAdapter.TYPE_DAY_NIGHT_MODE_AUTO) {
                val uiMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
                if(uiMode == Configuration.UI_MODE_NIGHT_NO) {
                    callDayNightModeChangedForFragments(SettingNightModeAdapter.TYPE_DAY_MODE)
                } else if(uiMode == Configuration.UI_MODE_NIGHT_YES) {
                    callDayNightModeChangedForFragments(SettingNightModeAdapter.TYPE_NIGHT_MODE)
                }
            }
        }
    }

    private fun callDayNightModeChangedForFragments(type: Int) {
        val fragments = ArrayList<Fragment>()
        val baseFragments = supportFragmentManager.fragments
        baseFragments.takeIf { it.isNotEmpty() }?.let {
            for (i in 0 until it.size) {
                val fragment = it[i]
                if (fragment is BaseFragment) {
                    fragments.add(fragment)
                    if (hasNestedFragments(fragment)) {
                        putFragments(fragment, fragments,false)
                    }
                }
            }
        }
        fragments.reverse()
        for (fragment in fragments) {
            if(fragment is BaseFragment) {
                fragment.onDayNightModeApplied(type)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    override fun onResume() {
        super.onResume()
//        if(isVpnActive()) {
//            showVpnDialog()
//        } else {
//            dismissVpnDialog()
//        }
        handleInAppReview()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissReviewDialog()
        //dismissVpnDialog()
    }

    private fun handleDeepLink(intent: Intent?) {
        this.deepLink = null
        intent?.let {
            val linkString = it.data?.toString()
            linkString?.takeIf { it.isNotEmpty() }?.let { link ->
                val schemeSplitted = link.split(":".toRegex()).toTypedArray()
                if (schemeSplitted.isNotEmpty()) {
                    val schemeString = schemeSplitted[0]
                    val rest = schemeSplitted[1].replace("//", "")
                    val scheme = if (schemeString == Scheme.Http.type || schemeString == Scheme.Https.type) {
                        Scheme.Http
                    } else if (schemeString == Scheme.PlayStop.type) {
                        Scheme.PlayStop
                    } else {
                        null
                    }
                    if (scheme != null) {
                        val slashSeparated = rest.split("/".toRegex()).toTypedArray()
                        if (slashSeparated.isNotEmpty()) {
                            val hostString = slashSeparated[0]
                            val host = when(hostString) {
                                Host.PlayStopApp.type -> Host.PlayStopApp
                                Host.PlayStop.type -> Host.PlayStop
                                Host.Open.type -> Host.Open
                                else -> null
                            }
                            when (host) {
                                Host.PlayStopApp -> {
                                    val path1String = slashSeparated.takeIf { it.size > 1 }?.let {
                                        extractFirstPath(it[1])
                                    }
                                    val path2String = slashSeparated.takeIf { it.size > 2 }?.let {
                                        extractSecondPath(it[2])
                                    }
                                    deepLink = DeepLink(
                                        scheme,
                                        host,
                                        path1String,
                                        path2String
                                    )
                                }
                                Host.PlayStop -> {
                                    val path1String = slashSeparated.takeIf { it.size > 1 }?.let {
                                        excludeExtraFromPath(it[1])
                                    }
                                    deepLink = DeepLink(
                                        scheme,
                                        host,
                                        path1String
                                    )
                                }
                                Host.Open -> {
                                    val path1String = slashSeparated.takeIf { it.size > 1 }?.let {
                                        extractFirstPath(it[1])
                                    }
                                    deepLink = DeepLink(
                                        scheme,
                                        host,
                                        path1String
                                    )
                                }
                                else -> {
                                }
                            }
                        }
                    }

                }
            }
        }
        if(deepLink != null) {
            val baseFragments = supportFragmentManager.fragments
            baseFragments.takeIf { it.isNotEmpty() }?.let {
                for (i in 0 until it.size) {
                    val fragment = it[i]
                    if (fragment is BaseFragment && fragment.isVisible) {
                        fragment.onHandleDeepLink()
                    }
                }
            }
        }
    }

    fun consumeDeepLink() {
        deepLink = null
        intent = null
    }

    private fun extractFirstPath(pathString: String?): Path? {
        pathString?.takeIf { it.isNotEmpty() }?.toLowerCase(Locale.getDefault())?.let {
            return when{
                it.startsWith(PathType.Open.type) -> Path(PathType.Open,"")
                it.startsWith(PathType.Payment.type) -> {
                    val valueString = it.replaceFirst(PathType.Payment.type,"")
                    Path(PathType.Payment,valueString)
                }
                else -> null
            }
        } ?: kotlin.run {
            return null
        }
    }

    private fun extractSecondPath(pathString: String?): Path? {
        pathString?.takeIf { it.isNotEmpty() }?.toLowerCase(Locale.getDefault())?.let {
            return if(it.startsWith(PathType.Movie.type)) {
                val valueString = it.replaceFirst(PathType.Movie.type,"")
                Path(PathType.Movie,valueString)
            } else {
                null
            }
        } ?: kotlin.run {
            return null
        }
    }

    private fun excludeExtraFromPath(pathString: String?): Path? {
        pathString?.takeIf { it.isNotEmpty() }?.toLowerCase(Locale.getDefault())?.let {
            val valueString = URLDecoder.decode(it, "UTF-8")
                .replace("-"," ")
                .replace("دانلود","")
                .replace("فیلم","")
                .replace("سریال","")
                .convertToEnglishNumber()
                .clearYearsPhrase()
                .trim()
            return Path(PathType.Search,valueString)
        } ?: kotlin.run {
            return null
        }
    }

    private fun String.clearYearsPhrase() : String {
        var finalString = this
        for (i in 1965 .. 2021) {
            finalString = finalString.replace(i.toString(),"")
        }
        return finalString
    }

    companion object {
        var INSTANCE : MainActivity? = null
        fun doRestart() {
            //INSTANCE?.reloadSideMenu()
            //INSTANCE?.findNavController(R.id.main_content_nav_host)?.navigate(R.id.pop_to_splash)
        }
    }

    override fun onBackPressed() {
        val rootView = findViewById<ViewGroup>(android.R.id.content)
        if(rootView.isSoftKeyboardOpen()) {
            rootView.hideSoftKeyboard()
        } else {
            val fragments = ArrayList<Fragment>()
            val baseFragments = supportFragmentManager.fragments
            baseFragments.takeIf { it.isNotEmpty() }?.let {
                for (i in 0 until it.size) {
                    val fragment = it[i]
                    if (fragment is BaseFragment && fragment.isVisible) {
                        fragments.add(fragment)
                        if (hasNestedFragments(fragment)) {
                            putFragments(fragment, fragments)
                        }
                    }
                }
                fragments.reverse()
                if (fragments.size == 1) {
                    if (fragments[0] is BaseFragment && (fragments[0] as BaseFragment).handleBack()) {
                        return
                    } else {
                        super.onBackPressed()
                    }
                } else {
                    for (f in fragments) {
                        if (f is BaseFragment && f.handleBack()) {
                            break
                        } else {
                            val parent = f.parentFragment
                            parent?.takeIf { it is BaseFragment }?.let {
                                (it as BaseFragment).onChildIsRemoving(f)
                            }
                            parent?.childFragmentManager?.beginTransaction()
                                ?.initCustomAnimations()
                                ?.remove(f)
                                ?.commit()
                            break
                        }
                    }
                }
            } ?: kotlin.run {
                super.onBackPressed()
            }
        }
    }

    private fun hasNestedFragments(fragment: Fragment) : Boolean {
        return fragment.childFragmentManager.fragments.isNotEmpty()
    }

    private fun putFragments(fragment: Fragment, fragmentList: MutableList<Fragment>, onlyAddVisibleFragments: Boolean = true) {
        val fragments = fragment.childFragmentManager.fragments
        for (j in 0 until fragments.size){
            val f = fragments[j]
            if(f is BaseFragment && (f.isVisible || onlyAddVisibleFragments.not())) {
                fragmentList.add(f)
                if(hasNestedFragments(f)) {
                    putFragments(f, fragmentList)
                }
            }
        }
    }

//    fun showVpnDialog() {
//        if(vpnDialog?.isShowing == true) {
//            return
//        }
//        takeIf { isFinishing.not() }?.let { ctx ->
//            vpnDialog = VpnDialog(ctx)
//            vpnDialog?.show()
//        }
//    }
//
//    fun dismissVpnDialog() {
//        vpnDialog?.takeIf { it.isShowing }?.dismiss()
//        vpnDialog?.cancel()
//        vpnDialog = null
//    }

    private fun showReviewDialog() {
        if(reviewDialog?.isShowing == true) {
            return
        }
        takeIf { isFinishing.not() }?.let { ctx ->
            reviewDialog = ReviewDialog(ctx,listener = object : ReviewDialog.OnVpnClickListener {
                override fun onSendReviewClick() {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(
                                "https://play.google.com/store/apps/details?id=${packageName}")
                            setPackage("com.android.vending")
                        }
                        startActivity(intent)
                    } catch (e: Exception) {
                        Crashes.trackError(e)
                    }

                }

                override fun onDiscardClick() {

                }

            })
            reviewDialog?.show()
        }
    }

    private fun dismissReviewDialog() {
        reviewDialog?.takeIf { it.isShowing }?.dismiss()
        reviewDialog?.cancel()
        reviewDialog = null
    }


    private fun handleInAppReview() {
        if(Hawk.contains(Movie.IN_APP_REVIEW_SHOWN) && Hawk.get<Boolean>(Movie.IN_APP_REVIEW_SHOWN) == true) {
            return
        }
        if(Hawk.contains(Movie.WATCHED_ONLINE_TIMES) && Hawk.get<Int>(Movie.WATCHED_ONLINE_TIMES) == 2) {
            showReviewDialog()
            Hawk.put(Movie.IN_APP_REVIEW_SHOWN,true)
        }
    }
}
