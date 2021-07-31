package com.ms.playstop.ui.settings

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.ui.settings.adapter.SettingNightModeAdapter
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment(), SettingNightModeAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = SettingsFragment()
        const val SETTING_DAY_NIGHT_MODE_KEY = "Setting Day Night Mode"
        const val SETTING_DAY_NIGHT_MODE_HANDLED_KEY = "Setting Day Night Mode Handled"
        fun initDarkModeFromSetting() {
            if(Hawk.contains(SETTING_DAY_NIGHT_MODE_KEY)) {
                val type = Hawk.get<Int>(SETTING_DAY_NIGHT_MODE_KEY)
                when(type) {
                    SettingNightModeAdapter.TYPE_NIGHT_MODE -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    SettingNightModeAdapter.TYPE_DAY_NIGHT_MODE_AUTO -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                    else -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
            } else {
                Hawk.put(SETTING_DAY_NIGHT_MODE_KEY,SettingNightModeAdapter.TYPE_DAY_MODE)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private lateinit var viewModel: SettingsViewModel
    lateinit var adapter: SettingNightModeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onDayNightModeApplied(type: Int) {
        super.onDayNightModeApplied(type)
        activity?.let { ctx ->
            view?.setBackgroundColor(ContextCompat.getColor(ctx, R.color.colorPrimary))
            settings_appbar?.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorAccentDark))
            settings_night_mode_recycler?.adapter = settings_night_mode_recycler?.adapter
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                settings_night_mode_title_tv?.setTextAppearance(ctx,ctx.getResourceFromThemeAttribute(R.attr.textAppearanceHeadline4,R.style.Headline4_FixSize))
            } else {
                settings_night_mode_title_tv?.setTextAppearance(ctx,ctx.getResourceFromThemeAttribute(R.attr.textAppearanceHeadline4,R.style.Headline4_FixSize))
            }
        }
    }

    override fun tag(): String {
        return "Settings Fragment"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun onViewLoaded() {
        super.onViewLoaded()
        initViews()
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    private fun initViews() {
        activity?.let { ctx ->
            adapter = SettingNightModeAdapter(this)
            val layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
            settings_night_mode_recycler?.layoutManager = layoutManager
            settings_night_mode_recycler?.adapter = adapter
        }
    }

    private fun subscribeToViewModel() {
        viewModel.dayNightMode.observe(viewLifecycleOwner, Observer { type ->
            adapter.setSelectedType(type)
        })
    }

    private fun subscribeToViewEvents() {
        settings_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onItemClick(position: Int, type: Int) {
        viewModel.updateDayNightMode(type)
        when(type) {
            SettingNightModeAdapter.TYPE_NIGHT_MODE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            SettingNightModeAdapter.TYPE_DAY_NIGHT_MODE_AUTO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

}