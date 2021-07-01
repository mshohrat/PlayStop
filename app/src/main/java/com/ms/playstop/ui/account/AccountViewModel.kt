package com.ms.playstop.ui.account

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.model.Profile
import com.ms.playstop.network.base.ApiServiceGenerator
import com.orhanobut.hawk.Hawk

class AccountViewModel : ViewModel() {

    val account: MutableLiveData<Profile?> = MutableLiveData()

    init {
        loadAccountData()
        fetchAccountData()
    }

    fun loadAccountData() {
        if(Hawk.contains(Profile.SAVE_KEY)) {
            val profile = Hawk.get(Profile.SAVE_KEY) as? Profile
            account.value = profile
        }
    }

    @SuppressLint("CheckResult")
    private fun fetchAccountData() {
        ApiServiceGenerator.getApiService.getUserInfo()
            ?.initSchedulers()
            ?.subscribe({
                it?.user?.let { user ->
                    val profile = Hawk.get<Profile?>(Profile.SAVE_KEY)
                    profile?.apply {
                        name = user.name
                        email = user.email
                        isPhoneVerified = user.isPhoneVerified
                        phone = user.phone
                        endSubscriptionDate = user.endSubscriptionDate
                        isSubscriptionExpired = user.isSubscriptionExpired
                    }
                    Hawk.put(Profile.SAVE_KEY, profile)
                    account.value = profile
                }
            },{

            })
    }

    fun logout() {
        if(Hawk.contains(Profile.SAVE_KEY)) {
            Hawk.delete(Profile.SAVE_KEY)
        }
    }
}