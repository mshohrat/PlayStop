package com.ms.playstop.ui.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.model.Profile
import com.orhanobut.hawk.Hawk

class AccountViewModel : ViewModel() {

    val account: MutableLiveData<Profile?> = MutableLiveData()

    init {
        if(Hawk.contains(Profile.SAVE_KEY)) {
            val profile = Hawk.get(Profile.SAVE_KEY) as? Profile
            account.value = profile
        }
    }

    fun logout() {
        if(Hawk.contains(Profile.SAVE_KEY)) {
            Hawk.delete(Profile.SAVE_KEY)
        }
    }
}