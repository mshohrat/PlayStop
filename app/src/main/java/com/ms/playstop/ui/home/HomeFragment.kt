package com.ms.playstop.ui.home

import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.*
import com.ms.playstop.ui.categories.CategoriesFragment
import com.ms.playstop.ui.mainAccount.MainAccountFragment
import com.ms.playstop.ui.movieLists.MovieListsFragment
import com.ms.playstop.ui.search.SearchFragment
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance() = HomeFragment()
        const val TAG = "Home Fragment"
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onSetStatusBarColor() {
        activity?.let { ctx ->
            ctx.setStatusBarColor(ContextCompat.getColor(ctx, R.color.colorPrimary))
        }
    }
    
    override fun tag(): String {
        return ""
    }

    override fun isEnterAnimationEnabled(): Boolean {
        return false
    }

    override fun isExitAnimationEnabled(): Boolean {
        return false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //activity?.updateStatusBarColor(R.color.purple)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        initViews()
        subscribeToViewEvents()
    }

    override fun onDayNightModeApplied(type: Int) {
        super.onDayNightModeApplied(type)
        activity?.let { ctx ->
            //ctx.setStatusBarColor(ContextCompat.getColor(ctx,R.color.colorPrimary))
            view?.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorPrimary))
            val white = ContextCompat.getColor(ctx,R.color.white)
            val purple = ContextCompat.getColor(ctx,R.color.purple_new)
            home_tab_layout?.setTabTextColors(white,purple)
            home_tab_layout?.setSelectedTabIndicatorColor(purple)
            home_tab_layout?.tabRippleColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.gray))
            home_tab_layout?.tabIconTint = ColorStateList.valueOf(white)
            home_tab_layout?.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorPrimaryLightDarkOpacity50))
            home_tab_layout?.selectedTabPosition?.let { position ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    home_tab_layout?.getTabAt(position)?.icon?.setColorFilter(BlendModeColorFilter(purple,BlendMode.SRC_IN))
                } else {
                    home_tab_layout?.getTabAt(position)?.icon?.setColorFilter(purple, PorterDuff.Mode.SRC_IN)
                }
            }
        }
    }

    private fun initViews() {
        home_tab_layout?.let {
            it.addTab(
                it.newTab()
                    .setText(R.string.account)
                    .setIcon(R.drawable.ic_avatar)
            )
            it.addTab(
                it.newTab()
                    .setText(R.string.category)
                    .setIcon(R.drawable.ic_category)
            )
            it.addTab(
                it.newTab()
                    .setText(R.string.search)
                    .setIcon(R.drawable.ic_search)
            )
            it.addTab(
                it.newTab()
                    .setText(R.string.home)
                    .setIcon(R.drawable.ic_movies)
            )
        }
    }

    private fun subscribeToViewEvents() {
        home_tab_layout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab?) {
                handleReselectedTab(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.icon?.colorFilter = null
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                handleSelectedTab(tab)
                activity?.let { ctx ->
                    val tabIconColor = ContextCompat.getColor(ctx, R.color.purple_new)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        tab?.icon?.setColorFilter(BlendModeColorFilter(tabIconColor,BlendMode.SRC_IN))
                    } else {
                        tab?.icon?.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
                    }
                }
            }

        })

        home_tab_layout?.getTabAt(3)?.select()
    }

    fun handleSelectedTab(tab: TabLayout.Tab?) {
        tab?.let {
            val destination = when(it.position) {
                0 -> {
                    MainAccountFragment.newInstance()
                }
                1 -> {
                    CategoriesFragment.newInstance()
                }
                2 -> {
                    SearchFragment.newInstance()
                }
                3 -> {
                    MovieListsFragment.newInstance()
                }
                else -> {
                    null
                }
            }
            destination?.let {
                addOrShow(it)
            }
        }
    }

    fun handleReselectedTab(tab: TabLayout.Tab?) {
        if(childFragmentManager.fragments.isEmpty()) {
            return
        }
        tab?.let {
            val destination = when(it.position) {
                0 -> childFragmentManager.findFragmentByTag(MainAccountFragment.TAG)
                1 -> childFragmentManager.findFragmentByTag(CategoriesFragment.TAG)
                2 -> childFragmentManager.findFragmentByTag(SearchFragment.TAG)
                3 -> childFragmentManager.findFragmentByTag(MovieListsFragment.TAG)
                else -> null
            }
            destination?.removeAllChildren()
        }
    }

    fun selectTabBy(destination: Fragment) {
        when (destination) {
            is MovieListsFragment -> home_tab_layout?.getTabAt(3)?.select()
            is SearchFragment -> home_tab_layout?.getTabAt(2)?.select()
            is CategoriesFragment -> home_tab_layout?.getTabAt(1)?.select()
            is MainAccountFragment -> home_tab_layout?.getTabAt(0)?.select()
            else -> {}
        }
    }

    override fun handleBack(): Boolean {
        if(home_tab_layout?.selectedTabPosition == 0 || home_tab_layout?.selectedTabPosition == 1 || home_tab_layout?.selectedTabPosition == 2) {
            home_tab_layout?.getTabAt(3)?.select()
        }
        else {
            activity?.finish()
        }
        return true
    }

    override fun containerId(): Int {
        return R.id.home_frame
    }

    override fun onHandleDeepLink() {
        super.onHandleDeepLink()
//        activity?.takeIf { it is MainActivity }?.let { act ->
//            (act as MainActivity).deepLink?.takeIf {
//                it.scheme == Scheme.Http
//                        && it.host == Host.PlayStopApp
//                        && it.path1?.pathType == PathType.Open}?.let {
//                home_tab_layout?.getTabAt(2)?.select()
//            }
//            act.deepLink?.takeIf {
//                it.scheme == Scheme.Http
//                        && it.host == Host.PlayStop
//                        && it.path1?.pathType == PathType.Search}?.let {
//                home_tab_layout?.getTabAt(0)?.select()
//            }
//        }
    }

}
