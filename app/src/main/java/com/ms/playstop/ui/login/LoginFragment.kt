package com.ms.playstop.ui.login

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import androidx.lifecycle.ViewModelProviders
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
import com.ms.playstop.extension.*
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.ui.enrerPhoneNumber.EnterPhoneNumberFragment
import com.ms.playstop.ui.forgotPassword.ForgotPasswordFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    companion object {
        fun newInstance() = LoginFragment()
        const val TAG = "Login Fragment"
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun tag(): String {
        return TAG
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun getExitAnimation(): Animation? {
        return activity?.let { AnimationUtils.loadAnimation(it,R.anim.fade_out) }
    }

    override fun getEnterAnimation(): Animation? {
        return activity?.let { AnimationUtils.loadAnimation(it,R.anim.fade_in) }
    }

    override fun onDayNightModeApplied(type: Int) {
        activity?.let { ctx ->
            view?.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorPrimary))
            with(ContextCompat.getColor(ctx,R.color.colorAccentDark)) {
                login_appbar?.setBackgroundColor(this)
                login_email_title_tv?.setTextColor(this)
                login_password_title_tv?.setTextColor(this)
            }
            with(
                MaterialShapeDrawable(
                    ShapeAppearanceModel.builder()
                .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.background_card_radius).toFloat())
                .build()
            ).apply {
                fillColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.white_opacity_10))
            }) {
                login_email_bg_view?.background = this
                login_password_bg_view?.background = this
            }
            with(ContextCompat.getColor(ctx,R.color.gray)) {
                login_email_et?.setHintTextColor(this)
                login_password_et?.setHintTextColor(this)
            }
            with(ContextCompat.getColor(ctx,R.color.white)) {
                login_password_et?.setTextColor(this)
                login_forgot_password_btn?.setTextColor(this)
                login_email_et?.setTextColor(this)
            }
            login_btn?.background = LayerDrawable(
                arrayOf(MaterialShapeDrawable(ShapeAppearanceModel.builder()
                .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.button_radius).toFloat())
                .build()), GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                    intArrayOf(ContextCompat.getColor(ctx,R.color.purple),ContextCompat.getColor(ctx,R.color.blue))
                )))

            login_email_et?.setCompoundDrawablesRelativeWithIntrinsicBounds(AppCompatResources.getDrawable(ctx,R.drawable.ic_email),null,null,null)
            login_password_et?.setCompoundDrawablesRelativeWithIntrinsicBounds(AppCompatResources.getDrawable(ctx,R.drawable.ic_lock),null,null,null)
            login_btn_divider?.background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.TRANSPARENT,ContextCompat.getColor(ctx,R.color.gray), Color.TRANSPARENT)
            )
        }
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    private fun subscribeToViewModel() {
        viewModel.login.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(GeneralResponse(messageResId = it.second))
            if(it.first) {
                removeFromParent(this)
            } else {
                val enterPhoneNumberFragment = EnterPhoneNumberFragment.newInstance()
                val args = Bundle().apply {
                    putInt(EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE,EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE_ADD)
                }
                enterPhoneNumberFragment.arguments = args
                addToParent(enterPhoneNumberFragment)
                removeFromParent(this)
            }
        })

        viewModel.loginError.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it)
        })
    }

    private fun subscribeToViewEvents() {
        login_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }

        login_btn?.setOnClickListener {
            showButtonLoading()
            viewModel.login(
                login_email_et?.text?.toString(),
                login_password_et?.text?.toString()
            )
        }

        login_forgot_password_btn?.setOnClickListener {
            val forgotPasswordFragment = ForgotPasswordFragment.newInstance()
            addToParent(forgotPasswordFragment)
        }

        login_root?.setOnClickListener {
            it.hideSoftKeyboard()
        }
    }

    private fun showToast(response: GeneralResponse) {
        response.message?.takeIf { it.isNotEmpty() }?.let {
            Toast.makeText(activity,it,Toast.LENGTH_SHORT).show()
        } ?: kotlin.run {
            response.messageResId?.let {
                Toast.makeText(activity,it,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showButtonLoading() {
        login_btn?.text = ""
        login_btn?.isEnabled = false
        login_btn_loading?.show()
    }

    private fun hideButtonLoading() {
        login_btn_loading?.hide()
        login_btn?.setText(R.string.login)
        login_btn?.isEnabled = true
    }

    override fun handleBack(): Boolean {
        val enterPhoneNumberFragment = EnterPhoneNumberFragment.newInstance()
        val args = Bundle().apply {
            putInt(EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE, EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE_LOGIN)
        }
        enterPhoneNumberFragment.arguments = args
        addToParent(enterPhoneNumberFragment)
        removeFromParent(this)
        return true
    }
}