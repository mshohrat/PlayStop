package com.ms.playstop.ui.splash

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ms.playstop.BuildConfig

import com.ms.playstop.R
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.navigate
import com.ms.playstop.extension.show
import com.ms.playstop.network.model.ConfigResponse
import com.ms.playstop.ui.home.HomeFragment
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
        const val TAG = "Splash Fragment"
    }

    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        initViews()
        subscribeToViewEvents()
        subscribeToViewModel()
        viewModel.fetchConfig()
    }

    private fun initViews() {
        splash_version_tv?.text = String.format(getString(R.string.version_x),BuildConfig.VERSION_NAME)
    }

    private fun subscribeToViewEvents() {
        splash_try_again_btn?.setOnClickListener {
            viewModel.fetchConfig()
            splash_try_again_btn?.hide()
            splash_progress?.show()
        }
    }

    private fun subscribeToViewModel() {
        viewModel.config.observe(viewLifecycleOwner, Observer {
            it?.let {
                Hawk.put(ConfigResponse.SAVE_KEY,it)
            }
            navigate(HomeFragment.newInstance())
        })

        viewModel.configError.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity,it?.message,Toast.LENGTH_SHORT).show()
            splash_progress?.hide()
            splash_try_again_btn?.show()
        })
    }
}
