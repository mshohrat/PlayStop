package com.ms.playstop.ui.requestMovie

import android.content.res.ColorStateList
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.show
import com.ms.playstop.network.model.GeneralResponse
import kotlinx.android.synthetic.main.fragment_request_movie.*

class RequestMovieFragment : BaseFragment() {

    companion object {
        fun newInstance() = RequestMovieFragment()
        const val TAG = "Request Movie Fragment"
    }

    private lateinit var viewModel: RequestMovieViewModel

    override fun tag(): String {
        return TAG
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_request_movie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RequestMovieViewModel::class.java)
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    private fun subscribeToViewModel() {
        viewModel.submitRequest.observe(viewLifecycleOwner, {
            hideLoading()
            request_movie_text_et?.text = null
            showToast(it)
        })
        viewModel.submitRequestError.observe(viewLifecycleOwner, {
            hideLoading()
            showToast(it)
        })
    }

    private fun subscribeToViewEvents() {
        request_movie_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
        request_movie_submit_btn?.setOnClickListener {
            showLoading()
            viewModel.submit(request_movie_text_et?.text?.toString())
        }
    }

    private fun showToast(response: GeneralResponse) {
        response.message?.takeIf { it.isNotEmpty() }?.let {
            Toast.makeText(activity,it, Toast.LENGTH_SHORT).show()
        } ?: kotlin.run {
            response.messageResId?.let {
                Toast.makeText(activity,it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading() {
        request_movie_submit_btn?.text = null
        request_movie_submit_btn?.isClickable = false
        request_movie_progress?.show()
    }

    private fun hideLoading() {
        request_movie_progress?.hide()
        request_movie_submit_btn?.setText(R.string.submit)
        request_movie_submit_btn?.isClickable = true
    }

    override fun onDayNightModeApplied(type: Int) {
        activity?.let { ctx ->
            request_movie_appbar?.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorAccentDark))
            with(ContextCompat.getColor(ctx,R.color.colorPrimary)) {
                view?.setBackgroundColor(this)
                request_movie_submit_btn?.setTextColor(this)
                request_movie_progress?.setColor(this)
            }
            with(ContextCompat.getColor(ctx,R.color.white)) {
                request_movie_description_tv?.setTextColor(this)
                request_movie_text_et?.setTextColor(this)
            }
            request_movie_text_et?.setHintTextColor(ContextCompat.getColor(ctx,R.color.white_opacity_50))
            request_movie_submit_btn?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.colorAccent))
            request_movie_text_et?.background = MaterialShapeDrawable(
                ShapeAppearanceModel.builder()
                    .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.background_card_radius).toFloat())
                    .build()
            ).apply {
                fillColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.white_opacity_10))
            }
        }
    }
}