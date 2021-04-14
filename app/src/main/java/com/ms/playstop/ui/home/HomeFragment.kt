package com.ms.playstop.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout

import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.addOrShow
import com.ms.playstop.extension.removeAllChildren
import com.ms.playstop.ui.categories.CategoriesFragment
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

    override fun tag(): String {
        return ""
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //activity?.updateStatusBarColor(R.color.purple)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        initViews()
        subscribeToViewEvents()
    }

    private fun initViews() {
        home_tab_layout?.let {
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
        home_tab_layout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{

            override fun onTabReselected(tab: TabLayout.Tab?) {
                handleReselectedTab(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                handleSelectedTab(tab)
            }

        })

        home_tab_layout?.getTabAt(2)?.select()
    }

    fun handleSelectedTab(tab: TabLayout.Tab?) {
        tab?.let {
            val destination = when(it.position) {
                0 -> {
                    CategoriesFragment.newInstance()
                }
                1 -> {
                    SearchFragment.newInstance()
                }
                2 -> {
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
        tab?.let {
            val destination = when(it.position) {
                0 -> childFragmentManager.findFragmentByTag(CategoriesFragment.TAG)
                1 -> childFragmentManager.findFragmentByTag(SearchFragment.TAG)
                2 -> childFragmentManager.findFragmentByTag(MovieListsFragment.TAG)
                else -> null
            }
            destination?.removeAllChildren()
        }
    }

    fun selectTabBy(destination: Fragment) {
        when (destination) {
            is MovieListsFragment -> home_tab_layout?.getTabAt(2)?.select()
            is SearchFragment -> home_tab_layout?.getTabAt(1)?.select()
            is CategoriesFragment -> home_tab_layout?.getTabAt(0)?.select()
            else -> {}
        }
    }

    override fun handleBack(): Boolean {
        if(home_tab_layout?.selectedTabPosition == 0 || home_tab_layout?.selectedTabPosition == 1 ) {
            home_tab_layout?.getTabAt(2)?.select()
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
