package com.ms.playstop.ui.settings

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.getResourceFromThemeAttribute
import com.ms.playstop.model.Device
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.ui.settings.adapter.DeviceAdapter
import com.ms.playstop.ui.settings.adapter.SettingNightModeAdapter
import com.ms.playstop.utils.RemoveDeviceDialog
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.item_device_layout.view.*

class SettingsFragment : BaseFragment(), SettingNightModeAdapter.OnItemClickListener,
    DeviceAdapter.ItemClickListener {

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
    lateinit var deviceAdapter: DeviceAdapter

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
            with(ContextCompat.getColor(ctx,R.color.white)) {
                settings_connected_devices_title_tv?.setTextColor(this)
                settings_current_device_title_tv?.setTextColor(this)
                settings_other_devices_title_tv?.setTextColor(this)
                settings_current_device_layout?.device_title_tv?.setTextColor(this)
                settings_current_device_layout?.device_app_version_tv?.setTextColor(this)
            }
            with(ContextCompat.getColor(ctx,R.color.redDark)) {
                settings_terminate_other_devices?.setTextColor(this)
                settings_terminate_other_devices_loading?.setColor(this)
            }
            settings_current_device_layout?.device_bottom_divider?.setBackgroundColor(ContextCompat.getColor(ctx,R.color.grayLight))
            settings_current_device_layout?.device_iv?.setImageDrawable(AppCompatResources.getDrawable(ctx,R.drawable.ic_device))
            deviceAdapter.onDayNightModeChanged(type)
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

            settings_other_devices_group?.visibility = View.VISIBLE
            deviceAdapter = DeviceAdapter(itemClickListener = this)
            settings_other_devices_recycler?.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
            settings_other_devices_recycler?.adapter = deviceAdapter

        }
    }

    private fun subscribeToViewModel() {
        viewModel.dayNightMode.observe(viewLifecycleOwner, Observer { type ->
            adapter.setSelectedType(type)
        })
        viewModel.currentDevice.observe(viewLifecycleOwner, Observer { device ->
            settings_current_device_loading_layout?.visibility = View.GONE
            settings_current_device_layout?.visibility = View.VISIBLE
            settings_current_device_layout?.device_title_tv?.text = device.model
            settings_current_device_layout?.device_app_version_tv?.text = device.platform.plus(" ").plus(device.platformVersion)
            settings_current_device_layout?.device_last_connected_time_tv?.text = getString(R.string.online)
            activity?.let { ctx ->
                settings_current_device_layout?.device_last_connected_time_tv?.setTextColor(ContextCompat.getColor(ctx,R.color.pure_green_dark))
            }
        })
        viewModel.otherDevices.observe(viewLifecycleOwner, Observer { devices ->
            if(devices.isNotEmpty()) {
                settings_terminate_other_devices?.visibility = View.VISIBLE
                settings_terminate_other_devices?.isEnabled = true
                deviceAdapter = DeviceAdapter(devices.toMutableList(),this)
                settings_other_devices_recycler?.adapter = deviceAdapter
            } else {
                settings_other_devices_group?.visibility = View.GONE
                settings_terminate_other_devices?.visibility = View.INVISIBLE
                settings_terminate_other_devices?.isEnabled = false
                settings_terminate_other_devices_loading?.visibility = View.GONE
            }
        })
        viewModel.revokeDevice.observe(viewLifecycleOwner, Observer {
            showToast(it.second)
            deviceAdapter.removeDevice(it.first)
        })
        viewModel.revokeDeviceError.observe(viewLifecycleOwner, Observer {
            showToast(it.second)
            deviceAdapter.setLoading(it.first,false)
        })
        viewModel.revokeAllOtherDevices.observe(viewLifecycleOwner, Observer {
            showToast(it)
            deviceAdapter.clearAll()
            settings_terminate_other_devices?.visibility = View.INVISIBLE
            settings_terminate_other_devices?.isEnabled = false
            settings_terminate_other_devices_loading?.visibility = View.GONE
            settings_other_devices_group?.visibility = View.GONE
        })
        viewModel.revokeAllOtherDevicesError.observe(viewLifecycleOwner, Observer {
            showToast(it)
            settings_terminate_other_devices_loading?.visibility = View.GONE
            settings_terminate_other_devices?.visibility = View.VISIBLE
        })
    }

    private fun subscribeToViewEvents() {
        settings_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
        settings_terminate_other_devices?.setOnClickListener {
            activity?.let { ctx ->
                RemoveDeviceDialog(ctx,true,object : RemoveDeviceDialog.OnClickListener{
                    override fun onYesClick() {
                        settings_terminate_other_devices?.visibility = View.INVISIBLE
                        settings_terminate_other_devices_loading?.visibility = View.VISIBLE
                        viewModel.revokeAllOtherDevices()
                    }
                })
                .title(R.string.remove_all_devices)
                .description(R.string.are_you_sure_to_remove_these_devices)
                .show()
            }
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

    override fun onItemClick(device: Device) {
        activity?.let { ctx ->
            RemoveDeviceDialog(ctx,true,object : RemoveDeviceDialog.OnClickListener{
                override fun onYesClick() {
                    deviceAdapter.setLoading(device,true)
                    viewModel.revokeDevice(device)
                }
            })
            .title(String.format(ctx.getString(R.string.remove_device_x),device.model))
            .description(R.string.are_you_sure_to_remove_this_device)
            .show()
        }
    }

}