package com.ms.playstop

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.initCustomAnimations
import com.ms.playstop.model.*
import com.ms.playstop.ui.splash.SplashFragment
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    var deepLink: DeepLink? = null
    val hasDeepLink: Boolean
    get() {
        return deepLink != null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            w.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.fragments.size.takeIf { it == 0 }.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame,
                    SplashFragment.newInstance())
                .commit()
        }
        handleDeepLink(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
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
                    } else {
                        null
                    }
                    if (scheme != null) {
                        val slashSeparated = rest.split("/".toRegex()).toTypedArray()
                        if (slashSeparated.isNotEmpty()) {
                            val hostString = slashSeparated[0]
                            val host = if (hostString == Host.PlayStop.type) {
                                Host.PlayStop
                            } else {
                                null
                            }
                            if (host != null) {
                                when (host) {
                                    Host.PlayStop -> {
                                        if (slashSeparated.size > 1) {
                                            val path1String = slashSeparated[1]
                                            deepLink = DeepLink(
                                                scheme,
                                                host,
                                                extractPath(path1String)
                                            )
                                        }
                                    }
                                    else -> {
                                    }
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

    private fun extractPath(pathString: String?): Path? {
        pathString?.takeIf { it.isNotEmpty() }?.toLowerCase(Locale.getDefault())?.let {
            if(it.startsWith(PathType.Open.type)) {
                val valueString = it.replaceFirst(PathType.Open.type,"").replaceFirst("/","")
                try {
                    val value = valueString.toInt()
                    return Path(PathType.Movie,value)
                } catch (e: Exception) {
                    e.printStackTrace()
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            } else {
                return null
            }
        } ?: kotlin.run {
            return null
        }
        return null
    }

    companion object {
        var INSTANCE : MainActivity? = null
        fun doRestart() {
            //INSTANCE?.reloadSideMenu()
            //INSTANCE?.findNavController(R.id.main_content_nav_host)?.navigate(R.id.pop_to_splash)
        }
    }

    override fun onBackPressed() {
        val fragments = ArrayList<Fragment>()
        val baseFragments = supportFragmentManager.fragments
        baseFragments.takeIf { it.isNotEmpty() }?.let {
            for (i in 0 until it.size){
                val fragment = it[i]
                if(fragment is BaseFragment && fragment.isVisible) {
                    fragments.add(fragment)
                    if(hasNestedFragments(fragment)) {
                        putFragments(fragment, fragments)
                    }
                }
            }
            fragments.reverse()
            if(fragments.size == 1) {
                if(fragments[0] is BaseFragment && (fragments[0] as BaseFragment).handleBack()) {
                    return
                } else {
                    super.onBackPressed()
                }
            } else {
                for (f in fragments) {
                    if(f is BaseFragment && f.handleBack()) {
                        break
                    } else {
                        f.parentFragment?.childFragmentManager?.beginTransaction()
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

    private fun hasNestedFragments(fragment: Fragment) : Boolean {
        return fragment.childFragmentManager.fragments.isNotEmpty()
    }

    private fun putFragments(fragment: Fragment,fragmentList: MutableList<Fragment>) {
        val fragments = fragment.childFragmentManager.fragments
        for (j in 0 until fragments.size){
            val f = fragments[j]
            if(f is BaseFragment && f.isVisible) {
                fragmentList.add(f)
                if(hasNestedFragments(f)) {
                    putFragments(f, fragmentList)
                }
            }
        }
    }
}
