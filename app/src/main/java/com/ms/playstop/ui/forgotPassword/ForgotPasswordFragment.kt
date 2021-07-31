package com.ms.playstop.ui.forgotPassword

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.hideSoftKeyboard
import com.ms.playstop.extension.show
import com.ms.playstop.network.model.GeneralResponse
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signup.*

class ForgotPasswordFragment : BaseFragment() {

    companion object {
        fun newInstance() = ForgotPasswordFragment()
    }

    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun tag(): String {
        return "Forgot Password Fragment"
    }

    override fun getExitAnimation(): Animation? {
        return activity?.let { AnimationUtils.loadAnimation(it,R.anim.fade_out) }
    }

    override fun getEnterAnimation(): Animation? {
        return activity?.let { AnimationUtils.loadAnimation(it,R.anim.fade_in) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
    }

    override fun onDayNightModeApplied(type: Int) {
        activity?.let { ctx ->
            with(ContextCompat.getColor(ctx,R.color.colorPrimary)) {
                view?.setBackgroundColor(this)
                forgot_password_btn?.setTextColor(this)
            }
            with(ContextCompat.getColor(ctx,R.color.colorAccentDark)) {
                forgot_password_appbar?.setBackgroundColor(this)
                forgot_password_email_title_tv?.setTextColor(this)
            }
            forgot_password_email_bg_view?.background = MaterialShapeDrawable(
                ShapeAppearanceModel.builder()
                .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.background_card_radius).toFloat())
                .build()
            ).apply {
                fillColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.white_opacity_10))
            }
            forgot_password_email_et?.setTextColor(ContextCompat.getColor(ctx,R.color.white))
            forgot_password_email_et?.setHintTextColor(ContextCompat.getColor(ctx,R.color.gray))
            forgot_password_email_et?.setCompoundDrawablesRelativeWithIntrinsicBounds(AppCompatResources.getDrawable(ctx,R.drawable.ic_email),null,null,null)
            forgot_password_btn?.background = LayerDrawable(
                arrayOf(MaterialShapeDrawable(ShapeAppearanceModel.builder()
                    .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.button_radius).toFloat())
                    .build()), GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    intArrayOf(ContextCompat.getColor(ctx,R.color.purple),ContextCompat.getColor(ctx,R.color.blue))
                )
                ))
        }
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        subscribeToViewEvents()
        subscribeToViewModel()
    }

    private fun subscribeToViewEvents() {
        forgot_password_btn?.setOnClickListener {
            showButtonLoading()
            viewModel.resetPassword(forgot_password_email_et?.text?.toString())
        }

        forgot_password_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }

        forgot_password_root?.setOnClickListener {
            it.hideSoftKeyboard()
        }
    }

    private fun subscribeToViewModel() {
        viewModel.resetPassword.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it)
            activity?.onBackPressed()
        })

        viewModel.resetPasswordError.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it)
        })
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

    private fun showButtonLoading() {
        forgot_password_btn?.text = ""
        forgot_password_btn?.isEnabled = false
        forgot_password_btn_loading?.show()
    }

    private fun hideButtonLoading() {
        forgot_password_btn_loading?.hide()
        forgot_password_btn?.setText(R.string.reset)
        forgot_password_btn?.isEnabled = true
    }

}