package com.ms.playstop

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.initCustomAnimations
import com.ms.playstop.ui.splash.SplashFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
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
