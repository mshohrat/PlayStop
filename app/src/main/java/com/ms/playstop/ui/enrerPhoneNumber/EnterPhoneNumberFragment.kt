package com.ms.playstop.ui.enrerPhoneNumber

import android.content.res.ColorStateList
import android.graphics.Color
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
import com.ms.playstop.extension.*
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.ui.login.LoginFragment
import com.ms.playstop.ui.mainAccount.MainAccountFragment
import com.ms.playstop.ui.otp.OtpFragment
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*

class EnterPhoneNumberFragment : BaseFragment() {

    companion object {
        fun newInstance() = EnterPhoneNumberFragment()
        const val ENTER_PHONE_NUMBER_STATE = "Enter Phone Number State"
        const val ENTER_PHONE_NUMBER_STATE_ADD = 101
        const val ENTER_PHONE_NUMBER_STATE_LOGIN = 102
        const val ENTER_PHONE_NUMBER_STATE_EDIT = 103
        const val TAG = "Enter Phone Number Fragment"
    }

    private lateinit var viewModel: EnterPhoneNumberViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_enter_phone_number, container, false)
    }

    override fun tag(): String {
        return TAG
    }

    override fun getExitAnimation(): Animation? {
        return if(arguments?.containsKey(ENTER_PHONE_NUMBER_STATE) == true && arguments?.getInt(ENTER_PHONE_NUMBER_STATE) == ENTER_PHONE_NUMBER_STATE_EDIT) {
            activity?.let { AnimationUtils.loadAnimation(it,R.anim.slide_out_right) }
        } else {
            activity?.let { AnimationUtils.loadAnimation(it, R.anim.fade_out) }
        }
    }

    override fun getEnterAnimation(): Animation? {
        return if(arguments?.containsKey(ENTER_PHONE_NUMBER_STATE) == true && arguments?.getInt(ENTER_PHONE_NUMBER_STATE) == ENTER_PHONE_NUMBER_STATE_EDIT) {
            activity?.let { AnimationUtils.loadAnimation(it,R.anim.slide_in_right) }
        } else {
            activity?.let { AnimationUtils.loadAnimation(it, R.anim.fade_in) }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EnterPhoneNumberViewModel::class.java)
    }

    override fun onDayNightModeApplied(type: Int) {
        activity?.let { ctx ->
            view?.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorPrimary))
            with(ContextCompat.getColor(ctx,R.color.colorAccentDark)){
                enter_phone_number_appbar?.setBackgroundColor(this)
                enter_phone_number_phone_title_tv?.setTextColor(this)
            }
            with(ContextCompat.getColor(ctx,R.color.white)){
                enter_phone_number_phone_et?.setTextColor(this)
                enter_phone_number_desc_tv?.setTextColor(this)
                enter_phone_number_login_with_email_btn?.setTextColor(this)
            }
            enter_phone_number_btn?.background = LayerDrawable(
                arrayOf(
                    MaterialShapeDrawable(
                        ShapeAppearanceModel.builder()
                    .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.button_radius).toFloat())
                    .build()), GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    intArrayOf(ContextCompat.getColor(ctx,R.color.purple),ContextCompat.getColor(ctx,R.color.blue))
                )
            ))
            enter_phone_number_phone_et?.setHintTextColor(ContextCompat.getColor(ctx,R.color.gray))
            enter_phone_number_phone_bg_view?.background = MaterialShapeDrawable(
                ShapeAppearanceModel.builder()
                    .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.background_card_radius).toFloat())
                    .build()
            ).apply {
                fillColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.white_opacity_10))
            }
            enter_phone_number_phone_et?.setCompoundDrawablesRelativeWithIntrinsicBounds(AppCompatResources.getDrawable(ctx,R.drawable.ic_phone),null,null,null)
            enter_phone_number_btn_divider?.background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.TRANSPARENT,ContextCompat.getColor(ctx,R.color.gray), Color.TRANSPARENT)
            )
        }
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        handleArguments()
        initViews()
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    private fun handleArguments() {
        arguments?.let {
            if(it.containsKey(ENTER_PHONE_NUMBER_STATE)) {
                viewModel.state = it.getInt(ENTER_PHONE_NUMBER_STATE, ENTER_PHONE_NUMBER_STATE_LOGIN)
            }
        }
    }

    private fun initViews() {
        when(viewModel.state) {
            ENTER_PHONE_NUMBER_STATE_LOGIN -> {
                enter_phone_number_toolbar_name_tv?.text = getString(R.string.login_to_account)
                enter_phone_number_btn_divider?.show()
                enter_phone_number_login_with_email_btn?.show()
            }
            ENTER_PHONE_NUMBER_STATE_ADD -> {
                enter_phone_number_toolbar_name_tv?.text = getString(R.string.add_phone_number)
                enter_phone_number_btn_divider?.hide()
                enter_phone_number_login_with_email_btn?.hide()
            }
            else -> {
                enter_phone_number_toolbar_name_tv?.text = getString(R.string.edit_phone_number)
                enter_phone_number_btn_divider?.hide()
                enter_phone_number_login_with_email_btn?.hide()
            }
        }
    }

    private fun subscribeToViewModel() {
        viewModel.login.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(GeneralResponse(messageResId = it.second))
            val otpFragment = OtpFragment.newInstance()
            otpFragment.arguments = Bundle().apply { 
                putInt(OtpFragment.VERIFY_STATE,OtpFragment.VERIFY_STATE_LOGIN)
                putString(OtpFragment.USER_PHONE,it.first)
            }
            addToParent(otpFragment)
        })
        viewModel.loginError.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it)
        })
        
        viewModel.verify.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(GeneralResponse(messageResId = it.second))
            val otpFragment = OtpFragment.newInstance()
            otpFragment.arguments = Bundle().apply {
                putInt(OtpFragment.VERIFY_STATE,OtpFragment.VERIFY_STATE_NONE)
                putString(OtpFragment.USER_PHONE,it.first)
            }
            addToParent(otpFragment)
        })
        viewModel.verifyError.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it)
        })
    }

    private fun subscribeToViewEvents() {
        enter_phone_number_btn?.setOnClickListener {
            showButtonLoading()
            viewModel.submit(enter_phone_number_phone_et?.text?.toString())
        }
        enter_phone_number_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
        enter_phone_number_root?.setOnClickListener {
            it.hideSoftKeyboard()
        }
        enter_phone_number_login_with_email_btn?.setOnClickListener {
            addToParent(LoginFragment.newInstance())
            removeFromParent(this)
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

    private fun pushBack() : Boolean {
        return when(viewModel.state) {
            ENTER_PHONE_NUMBER_STATE_LOGIN -> {
                addToParent(LoginFragment.newInstance())
                removeFromParent(this)
                true
            }
            else -> {
                false
            }
        }
    }

    private fun showButtonLoading() {
        enter_phone_number_btn?.text = ""
        enter_phone_number_btn?.isEnabled = false
        enter_phone_number_btn_loading?.show()
    }

    private fun hideButtonLoading() {
        enter_phone_number_btn_loading?.hide()
        enter_phone_number_btn?.setText(R.string.submit)
        enter_phone_number_btn?.isEnabled = true
    }

    override fun handleBack(): Boolean {
        return when(viewModel.state) {
            ENTER_PHONE_NUMBER_STATE_LOGIN -> {
                parentFragment?.takeIf { it is MainAccountFragment }?.let {
                    passHandleBackToParent()
                } ?: kotlin.run {
                    super.handleBack()
                }
            }
            else -> super.handleBack()
        }

    }

}