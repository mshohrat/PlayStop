package com.ms.playstop.ui.settings

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.R
import com.ms.playstop.model.Device
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.network.model.RevokeDeviceRequest
import com.orhanobut.hawk.Hawk
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SettingsViewModel : ViewModel() {

    val dayNightMode = MutableLiveData<Int>()
    val currentDevice = MutableLiveData<Device>()
    val otherDevices = MutableLiveData<List<Device>>()
    val revokeDevice : MutableLiveData<Pair<Device,GeneralResponse>> = MutableLiveData()
    val revokeDeviceError : MutableLiveData<Pair<Device,GeneralResponse>> = MutableLiveData()
    val revokeAllOtherDevices : MutableLiveData<GeneralResponse> = MutableLiveData()
    val revokeAllOtherDevicesError : MutableLiveData<GeneralResponse> = MutableLiveData()

    init {
        if(Hawk.contains(SettingsFragment.SETTING_DAY_NIGHT_MODE_KEY)) {
            val type = Hawk.get<Int>(SettingsFragment.SETTING_DAY_NIGHT_MODE_KEY)
            dayNightMode.postValue(type)
        }
        getAllDevices()
    }

    fun updateDayNightMode(type: Int) {
        Hawk.put(SettingsFragment.SETTING_DAY_NIGHT_MODE_KEY,type)
    }

    @SuppressLint("CheckResult")
    private fun getAllDevices() {
        ApiServiceGenerator.getApiService.getDevices()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                it?.let {
                    currentDevice.postValue(it.currentDevice)
                    otherDevices.postValue(it.otherDevices)
                }
            },{

            })
    }

    @SuppressLint("CheckResult")
    fun revokeDevice(device: Device) {
        ApiServiceGenerator.getApiService.revokeDevice(RevokeDeviceRequest(device.id))
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                it?.let {
                    revokeDevice.postValue(device to GeneralResponse(messageResId = R.string.device_revoked_successfully))
                    otherDevices.postValue(otherDevices.value?.toMutableList()?.filter { it.id != device.id })
                } ?: kotlin.run {
                    revokeDeviceError.postValue(device to GeneralResponse(messageResId = R.string.revoke_device_got_error))
                }
            },{
                revokeDeviceError.postValue(device to GeneralResponse(messageResId = R.string.revoke_device_got_error))
            })
    }

    @SuppressLint("CheckResult")
    fun revokeAllOtherDevices() {
        ApiServiceGenerator.getApiService.revokeAllOtherDevices()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                it?.let {
                    revokeAllOtherDevices.postValue(GeneralResponse(messageResId = R.string.other_devices_revoked_successfully))
                } ?: kotlin.run {
                    revokeAllOtherDevicesError.postValue(GeneralResponse(messageResId = R.string.revoke_other_devices_got_error))
                }
            },{
                revokeAllOtherDevicesError.postValue(GeneralResponse(messageResId = R.string.revoke_other_devices_got_error))
            })
    }
}