package com.ms.playstop.ui.splash

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.Observer
import com.google.firebase.iid.FirebaseInstanceId
import com.ms.playstop.BuildConfig

import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.navigate
import com.ms.playstop.extension.setStatusBarColor
import com.ms.playstop.extension.show
import com.ms.playstop.model.Profile
import com.ms.playstop.network.model.ConfigResponse
import com.ms.playstop.ui.home.HomeFragment
import com.ms.playstop.ui.settings.adapter.SettingNightModeAdapter
import com.ms.playstop.utils.UpdateDialog
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.android.synthetic.main.layout_splash_logo.*

class SplashFragment : BaseFragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private lateinit var viewModel: SplashViewModel
    private var updateDialog: UpdateDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun isEnterAnimationEnabled(): Boolean {
        return false
    }

    override fun getExitAnimation(): Animation? {
        return activity?.let { AnimationUtils.loadAnimation(it,R.anim.fade_out) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    override fun onSetStatusBarColor() {
        activity?.let { ctx ->
            ctx.setStatusBarColor(ContextCompat.getColor(ctx, R.color.colorPrimary))
        }
    }

    override fun onDayNightModeApplied(type: Int) {
        activity?.let { ctx ->
            with(ContextCompat.getColor(ctx,R.color.colorPrimary)){
                view?.setBackgroundColor(this)
                splash_try_again_btn?.setTextColor(this)
            }
            splash_version_tv?.setTextColor(ContextCompat.getColor(ctx,R.color.white))
            with(ContextCompat.getColor(ctx,R.color.purple_new)){
                splash_app_name_tv?.setTextColor(this)
                splash_desc_tv?.setTextColor(this)
            }
            splash_logo_iv?.let { iv ->
                if(type == SettingNightModeAdapter.TYPE_DAY_MODE) {
                    ImageViewCompat.setImageTintList(iv, null)
                } else if(type == SettingNightModeAdapter.TYPE_NIGHT_MODE) {
                    ImageViewCompat.setImageTintList(iv, ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.purple_new)))
                }
            }
            splash_progress?.setColor(ContextCompat.getColor(ctx,R.color.white))
        }
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        initViews()
        subscribeToViewEvents()
        subscribeToViewModel()
//        if(isVpnActive().not()) {
//            (activity as? MainActivity?)?.dismissVpnDialog()
//        }
//        else {
//            (activity as? MainActivity?)?.showVpnDialog()
//        }
        viewModel.fetchConfig()
    }

    private fun initViews() {
        splash_version_tv?.text = String.format(getString(R.string.version_x),BuildConfig.VERSION_NAME)
    }

    private fun subscribeToViewEvents() {
        splash_try_again_btn?.setOnClickListener {
//            if(isVpnActive().not()) {
//                (activity as? MainActivity?)?.dismissVpnDialog()
                viewModel.fetchConfig()
                splash_try_again_btn?.hide()
                splash_progress?.show()
//            }
//            else {
//                (activity as? MainActivity?)?.showVpnDialog()
//            }
        }
    }

    private fun subscribeToViewModel() {
        viewModel.config.observe(viewLifecycleOwner, Observer {
            it?.let {
                Hawk.put(ConfigResponse.SAVE_KEY,it)
            }
            it?.user?.let { user ->
                val profile = Hawk.get(Profile.SAVE_KEY) as? Profile
                profile?.apply {
                    name = user.name
                    email = user.email
                    isPhoneVerified = user.isPhoneVerified
                    phone = user.phone
                    endSubscriptionDate = user.endSubscriptionDate
                    isSubscriptionExpired = user.isSubscriptionExpired
                }
                Hawk.put(Profile.SAVE_KEY,profile)
            }
            if(it?.requiresToken == true) {
                FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceId ->
                    val token = instanceId.token
                    viewModel.registerFbToken(token)
                    handleConfigDataFetched(it)
                }
                FirebaseInstanceId.getInstance().instanceId.addOnFailureListener { instanceId ->
                    handleConfigDataFetched(it)
                }
            } else {
                handleConfigDataFetched(it)
            }
        })

        viewModel.configError.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity,R.string.failed_in_communication_with_server,Toast.LENGTH_SHORT).show()
            splash_progress?.hide()
            splash_try_again_btn?.show()
        })
    }

    private fun handleConfigDataFetched(configResponse: ConfigResponse?) {
        configResponse?.updateApp?.let {
            when {
                it.minVersion > BuildConfig.VERSION_CODE -> {
                    showUpdateDialog(true)
                }
                it.lastVersion > BuildConfig.VERSION_CODE -> {
                    showUpdateDialog(false)
                }
                else -> {
                    navigate(HomeFragment.newInstance())
                }
            }
        } ?: kotlin.run {
            navigate(HomeFragment.newInstance())
        }
    }

    private fun showUpdateDialog(isForce: Boolean = false) {
        if(updateDialog?.isShowing == true) {
            return
        }
        activity?.takeIf { it.isFinishing.not() }?.let { ctx ->
            updateDialog = UpdateDialog(ctx,isForce.not())
            updateDialog?.updateClickListener = object : UpdateDialog.OnUpdateClickListener {
                override fun onDownloadClick() {
                    dismissUpdateDialog()
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(BuildConfig.DOWNLOAD_URL)
                    activity?.startActivity(intent)
                    Handler(Looper.getMainLooper()).postDelayed({
                        activity?.finish()
                    },2000)
                }

                override fun onCancelClick() {
                    navigate(HomeFragment.newInstance())
                }

            }
            updateDialog?.show()
        }

    }

    private fun dismissUpdateDialog() {
        updateDialog?.takeIf { it.isShowing }?.dismiss()
        updateDialog?.cancel()
        updateDialog = null
    }

    override fun tag(): String {
        return "Splash Fragment"
    }
}
