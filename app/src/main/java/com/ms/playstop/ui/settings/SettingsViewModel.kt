package com.ms.playstop.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orhanobut.hawk.Hawk

class SettingsViewModel : ViewModel() {

    val dayNightMode = MutableLiveData<Int>()

    init {
        if(Hawk.contains(SettingsFragment.SETTING_DAY_NIGHT_MODE_KEY)) {
            val type = Hawk.get<Int>(SettingsFragment.SETTING_DAY_NIGHT_MODE_KEY)
            dayNightMode.postValue(type)
        }
    }

    fun updateDayNightMode(type: Int) {
        Hawk.put(SettingsFragment.SETTING_DAY_NIGHT_MODE_KEY,type)
    }
}