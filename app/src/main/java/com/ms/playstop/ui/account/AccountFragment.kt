package com.ms.playstop.ui.account

import android.content.res.ColorStateList
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.Observer
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.*
import com.ms.playstop.ui.completeAccount.CompleteAccountFragment
import com.ms.playstop.ui.enrerPhoneNumber.EnterPhoneNumberFragment
import com.ms.playstop.ui.mainAccount.MainAccountFragment
import com.ms.playstop.ui.movies.MoviesFragment
import com.ms.playstop.ui.movies.adapter.RequestType
import com.ms.playstop.ui.payment.PaymentFragment
import com.ms.playstop.ui.settings.SettingsFragment
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : BaseFragment() {

    companion object {
        fun newInstance() = AccountFragment()
        const val TAG = "Account Fragment"
    }

    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun tag(): String {
        return TAG
    }

    override fun getEnterAnimation(): Animation? {
        return activity?.let { AnimationUtils.loadAnimation(it,R.anim.fade_in) } ?: kotlin.run { null }
    }

    override fun getExitAnimation(): Animation? {
        return activity?.let { AnimationUtils.loadAnimation(it,R.anim.fade_out) } ?: kotlin.run { null }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    override fun onDayNightModeApplied(type: Int) {
        super.onDayNightModeApplied(type)
        activity?.let { ctx ->

            with(ContextCompat.getColor(ctx,R.color.colorAccent)){
                //account_liked_movies_btn?.backgroundTintList = ColorStateList.valueOf(this)
                //account_logout_btn?.setTextColor(this)
            }

            account_avatar_iv?.let { iv ->
                ImageViewCompat.setImageTintList(iv,ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.colorAccentDarkOpacity50)))
            }

            with(ContextCompat.getColor(ctx, R.color.colorPrimary)){
                view?.setBackgroundColor(this)
                //account_liked_movies_btn?.setTextColor(this)
                account_bottom_view?.setBackgroundColor(this)
            }

            with(ContextCompat.getColor(ctx,R.color.white)){
                account_name_tv?.setTextColor(this)
                account_email_tv?.setTextColor(this)
                account_phone_tv?.setTextColor(this)
                account_subscription_title_tv?.setTextColor(this)
                account_liked_movies_btn?.setTextColor(this)
                account_subscription_purchase_btn?.setTextColor(this)
                account_settings_btn?.setTextColor(this)
            }

            account_email_tv?.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,AppCompatResources.getDrawable(ctx,R.drawable.ic_email_16dp),null)
            account_phone_tv?.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,AppCompatResources.getDrawable(ctx,R.drawable.ic_phone_16dp),null)

            with(ContextCompat.getColor(ctx,R.color.colorAccentDark)) {
                account_appbar?.setBackgroundColor(this)
            }

            account_end_subscription_date_tv?.setTextColor(ContextCompat.getColor(ctx,R.color.white_opacity_50))

            with(ContextCompat.getColor(ctx,R.color.colorAccentOpacity20)) {
                account_purchase_divider?.setBackgroundColor(this)
                account_top_divider?.setBackgroundColor(this)
                account_bottom_divider.setBackgroundColor(this)
            }

            with(ContextCompat.getColor(ctx,R.color.blue)){
                account_liked_movies_btn?.iconTint = ColorStateList.valueOf(this)
                account_subscription_purchase_btn?.iconTint = ColorStateList.valueOf(this)
                account_settings_btn?.iconTint = ColorStateList.valueOf(this)
            }

            account_logout_btn?.rippleColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.white_opacity_10))

            with(ContextCompat.getColor(ctx,R.color.blue_opacity20)) {
                account_liked_movies_btn?.rippleColor = ColorStateList.valueOf(this)
                account_subscription_purchase_btn?.rippleColor = ColorStateList.valueOf(this)
                account_settings_btn?.rippleColor = ColorStateList.valueOf(this)
            }

            with(MaterialShapeDrawable(ShapeAppearanceModel.builder()
                .setAllCornerSizes(ctx.resources.getDimensionPixelSize(R.dimen.background_card_radius).toFloat())
                .build()
            ).apply {
                fillColor = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.white_opacity_10))
            }) {

            }

        }
    }

    private fun subscribeToViewModel() {
        viewModel.account.observe(viewLifecycleOwner, Observer {
            it?.let {
                account_name_tv?.text = it.name
                account_email_tv?.text = it.email
                account_phone_tv?.text = it.phone
                if(isSubscriptionEnabled()) {
                    account_subscription_group?.show()
                    if(it.isSubscriptionExpired == true) {
                        activity?.let {
                            account_subscription_status_tv?.setTextColor(ContextCompat.getColor(it,R.color.pure_orange_dark))
                        }
                        account_end_subscription_date_tv?.hide()
                        account_subscription_status_tv?.setText(R.string.disabled)
                        account_end_subscription_date_tv?.text = ""
                    } else {
                        activity?.let {
                            account_subscription_status_tv?.setTextColor(ContextCompat.getColor(it,R.color.pure_green_dark))
                        }
                        account_end_subscription_date_tv?.show()
                        account_subscription_status_tv?.setText(R.string.enabled)
                        account_end_subscription_date_tv?.text = String.format(getString(R.string.until_date_x),getJalaliDate(it.endSubscriptionDate))
                    }
                } else {
                    account_end_subscription_date_tv?.hide()
                    account_subscription_group?.hide()
                }
            }
        })
    }

    private fun subscribeToViewEvents() {
        account_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
        account_logout_btn?.setOnClickListener {
            viewModel.logout()
//            parentFragment?.takeIf { it is MainAccountFragment }?.let {
//                (it as MainAccountFragment).handleAccountRemoved()
//            }
        }

        account_settings_btn?.setOnClickListener {
            addToParent(SettingsFragment.newInstance())
        }

        account_liked_movies_btn?.setOnClickListener {
            val moviesFragment = MoviesFragment.newInstance()
            moviesFragment.arguments = Bundle().apply {
                this.putInt(MoviesFragment.MOVIES_REQUEST_TYPE, RequestType.LIKES.type)
                this.putString(MoviesFragment.MOVIES_REQUEST_NAME,getString(R.string.liked_movies))
            }
            addToParent(moviesFragment)
        }

        account_edit_btn?.setOnClickListener {
            showEditProfileDialog()
        }

        account_subscription_purchase_btn?.setOnClickListener {
            navigateToPaymentFragment()
        }
    }

    private fun navigateToEditAccountFragment() {
        val completeAccountFragment = CompleteAccountFragment.newInstance()
        completeAccountFragment.arguments = Bundle().apply {
            putInt(
                CompleteAccountFragment.COMPLETE_ACCOUNT_STATE
                , CompleteAccountFragment.COMPLETE_ACCOUNT_STATE_EDIT)
        }
        addToParent(completeAccountFragment)
    }

    private fun navigateToEditPhoneFragment() {
        val enterPhoneNumberFragment = EnterPhoneNumberFragment.newInstance()
        val args = Bundle().apply {
            putInt(EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE, EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE_EDIT)
        }
        enterPhoneNumberFragment.arguments = args
        addToParent(enterPhoneNumberFragment)
    }

    private fun navigateToPaymentFragment() {
        val paymentFragment = PaymentFragment.newInstance()
        addToParent(paymentFragment)
    }

    private fun showEditProfileDialog() {
        activity?.let { ctx ->

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                PopupMenu(ctx, account_toolbar, Gravity.START,R.attr.popupMenuStyle ,0)
            } else {
                PopupMenu(ctx, account_toolbar)
            }.apply {
                setOnMenuItemClickListener { item ->
                    when(item.itemId) {
                        R.id.action_edit_info -> {
                            navigateToEditAccountFragment()
                            true
                        }
                        R.id.action_edit_phone -> {
                            navigateToEditPhoneFragment()
                            true
                        }
                        else -> false
                    }
                }
                inflate(R.menu.menu_edit_profile)
                show()
            }
        }
    }

    override fun onSharedPreferencesChanged() {
        viewModel.loadAccountData()
    }

    override fun handleBack(): Boolean {
        parentFragment?.takeIf { it is MainAccountFragment }?.let {
            return passHandleBackToParent()
        } ?: kotlin.run {
            return super.handleBack()
        }
    }

}