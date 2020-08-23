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
        return TAG
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
            it.getTabAt(0)?.select()
        }
    }

    private fun subscribeToViewEvents() {
        home_tab_layout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when(it.position) {
                        0 -> {
                            addOrShow(CategoriesFragment.newInstance())
                        }
                        1 -> {
                            addOrShow(SearchFragment.newInstance())
                        }
                        2 -> {
                            addOrShow(MovieListsFragment.newInstance())
                        }
                        else -> {}
                    }
                }
            }

        })
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
        activity?.finish()
        return true
    }

}
