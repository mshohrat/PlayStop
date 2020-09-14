package com.ms.playstop.ui.categories

import android.content.res.ColorStateList
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
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
import com.ms.playstop.ui.movies.MoviesFragment
import com.ms.playstop.ui.movies.adapter.RequestType
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : BaseFragment() {

    companion object {
        fun newInstance() = CategoriesFragment()
    }

    private lateinit var viewModel: CategoriesViewModel

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
                for (category in it) {
                    val button = createButton()
                    button?.let {
                        it.text = category.name
                        categories_category_chip_group?.addView(
                            it, ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                        )
                        it.setOnClickListener {
                            onItemClick(category)
                        }
                    }
                }
            }
        })

        viewModel.genres.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()) {
                categories_genre_group?.hide()
            }
            else {
                categories_genre_group?.show()
                for (genre in it) {
                    val button = createButton()
                    button?.let {
                        it.text = genre.name
                        categories_genre_chip_group?.addView(
                            it, ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                        )
                        it.setOnClickListener {
                            onItemClick(genre)
                        }
                    }
                }
            }
        })

        viewModel.suggestions.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()) {
                categories_suggestion_group?.hide()
            }
            else {
                categories_suggestion_group?.show()
                for (suggestion in it) {
                    val button = createButton()
                    button?.let {
                        it.text = suggestion.name
                        categories_suggestion_chip_group?.addView(
                            it, ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                        )
                        it.setOnClickListener {
                            onItemClick(suggestion)
                        }
                    }
                }
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

    private fun onItemClick(category: Category) {
        val moviesFragment = MoviesFragment.newInstance()
        moviesFragment.arguments = Bundle().apply {
            this.putInt(MoviesFragment.MOVIES_REQUEST_TYPE, RequestType.CATEGORY.type)
            this.putInt(MoviesFragment.MOVIES_REQUEST_ID,category.id)
            this.putString(MoviesFragment.MOVIES_REQUEST_NAME,category.name)
        }
        add(containerId(),moviesFragment)
    }

    private fun onItemClick(genre: Genre) {
        val moviesFragment = MoviesFragment.newInstance()
        moviesFragment.arguments = Bundle().apply {
            this.putInt(MoviesFragment.MOVIES_REQUEST_TYPE, RequestType.GENRE.type)
            this.putInt(MoviesFragment.MOVIES_REQUEST_ID,genre.id)
            this.putString(MoviesFragment.MOVIES_REQUEST_NAME,genre.name)
        }
        add(containerId(),moviesFragment)
    }

    private fun onItemClick(suggestion: Suggestion) {
        val moviesFragment = MoviesFragment.newInstance()
        moviesFragment.arguments = Bundle().apply {
            this.putInt(MoviesFragment.MOVIES_REQUEST_TYPE, RequestType.SUGGESTION.type)
            this.putInt(MoviesFragment.MOVIES_REQUEST_ID,suggestion.id)
            this.putString(MoviesFragment.MOVIES_REQUEST_NAME,suggestion.name)
        }
        add(containerId(),moviesFragment)
    }

    override fun containerId(): Int {
        return R.id.categories_frame
    }

    override fun handleBack(): Boolean {
        return passHandleBackToParent()
    }

}
