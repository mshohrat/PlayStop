package com.ms.playstop.ui.categories

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.button.MaterialButton
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.add
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.passHandleBackToParent
import com.ms.playstop.extension.show
import com.ms.playstop.model.Category
import com.ms.playstop.model.Genre
import com.ms.playstop.model.Suggestion
import com.ms.playstop.model.Year
import com.ms.playstop.ui.categories.adapter.ChipAdapter
import com.ms.playstop.ui.movies.MoviesFragment
import com.ms.playstop.ui.movies.adapter.RequestType
import com.ms.playstop.utils.LoadingDialog
import com.ms.playstop.utils.RtlFlexBoxLayoutManager
import kotlinx.android.synthetic.main.fragment_categories.*


class CategoriesFragment : BaseFragment() {

    companion object {
        fun newInstance() = CategoriesFragment()
    }

    private lateinit var viewModel: CategoriesViewModel
    private var loadingDialog: LoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun tag(): String {
        return "Categories Fragment"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CategoriesViewModel::class.java)
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    private fun subscribeToViewModel() {
        viewModel.categories.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()) {
                categories_category_group?.hide()
            }
            else {
                categories_category_group?.show()
                val layoutManager = FlexboxLayoutManager(activity)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_END
                layoutManager.flexWrap = FlexWrap.WRAP
                categories_category_chip_recycler?.layoutManager = layoutManager
                val adapter = ChipAdapter<Category>(it,object : ChipAdapter.OnItemClickListener<Category>{
                    override fun onItemClick(item: Category) {
                        val moviesFragment = MoviesFragment.newInstance()
                        moviesFragment.arguments = Bundle().apply {
                            this.putInt(MoviesFragment.MOVIES_REQUEST_TYPE, RequestType.CATEGORY.type)
                            this.putInt(MoviesFragment.MOVIES_REQUEST_ID,item.id)
                            this.putString(MoviesFragment.MOVIES_REQUEST_NAME,item.name)
                        }
                        add(containerId(),moviesFragment)
                    }
                })
                categories_category_chip_recycler?.adapter = adapter
//                for (category in it) {
//                    val button = createButton()
//                    button?.let {
//                        it.text = category.name
//                        categories_category_chip_group?.addView(
//                            it, ViewGroup.LayoutParams(
//                                ViewGroup.LayoutParams.WRAP_CONTENT,
//                                ViewGroup.LayoutParams.WRAP_CONTENT
//                            )
//                        )
//                        it.setOnClickListener {
//                            onItemClick(category)
//                        }
//                    }
//                }
            }
        })

        viewModel.genres.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()) {
                categories_genre_group?.hide()
            }
            else {
                categories_genre_group?.show()
                val layoutManager = FlexboxLayoutManager(activity)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_END
                layoutManager.flexWrap = FlexWrap.WRAP
                categories_genre_chip_recycler?.layoutManager = layoutManager
                val adapter = ChipAdapter<Genre>(it,object : ChipAdapter.OnItemClickListener<Genre> {
                    override fun onItemClick(item: Genre) {
                        val moviesFragment = MoviesFragment.newInstance()
                        moviesFragment.arguments = Bundle().apply {
                            this.putInt(MoviesFragment.MOVIES_REQUEST_TYPE, RequestType.GENRE.type)
                            this.putInt(MoviesFragment.MOVIES_REQUEST_ID,item.id)
                            this.putString(MoviesFragment.MOVIES_REQUEST_NAME,item.name)
                        }
                        add(containerId(),moviesFragment)
                    }
                })
                categories_genre_chip_recycler?.adapter = adapter
            }
        })

        viewModel.years.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()) {
                categories_year_group?.hide()
            }
            else {
                categories_year_group?.show()
                val layoutManager = FlexboxLayoutManager(activity)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_END
                layoutManager.flexWrap = FlexWrap.WRAP
                categories_year_chip_recycler?.layoutManager = layoutManager
                val adapter = ChipAdapter<Year>(it,object : ChipAdapter.OnItemClickListener<Year> {
                    override fun onItemClick(item: Year) {
                        val moviesFragment = MoviesFragment.newInstance()
                        moviesFragment.arguments = Bundle().apply {
                            this.putInt(MoviesFragment.MOVIES_REQUEST_TYPE, RequestType.YEAR.type)
                            this.putInt(MoviesFragment.MOVIES_REQUEST_ID,item.value)
                            this.putString(MoviesFragment.MOVIES_REQUEST_NAME,String.format(getString(R.string.year_x),item.value))
                        }
                        add(containerId(),moviesFragment)
                    }
                })
                categories_year_chip_recycler?.adapter = adapter
            }
        })
    }

    private fun subscribeToViewEvents() {
        categories_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun createButton() : MaterialButton? {
        activity?.let { ctx ->
            val button = MaterialButton(ctx)
            button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.grayLight))
            button.cornerRadius = ctx.resources.getDimensionPixelSize(R.dimen.badge_radius)
            val padding = ctx.resources.getDimensionPixelSize(R.dimen.padding_medium)
            button.setPadding(padding,padding,padding,padding)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                button.setTextAppearance(R.style.Body1_FixSize)
            } else {
                button.setTextAppearance(ctx,R.style.Body1_FixSize)
            }
            return button
        }
        return null

    }

    override fun containerId(): Int {
        return R.id.categories_frame
    }

    override fun handleBack(): Boolean {
        return passHandleBackToParent()
    }

}
