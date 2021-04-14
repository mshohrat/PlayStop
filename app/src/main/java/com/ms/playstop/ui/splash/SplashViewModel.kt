package com.ms.playstop.ui.splash

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.ConfigResponse
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.network.model.RegisterFbTokenRequest

class SplashViewModel : ViewModel() {

    val config = MutableLiveData<ConfigResponse?>()

    val configError = MutableLiveData<GeneralResponse?>()

    @SuppressLint("CheckResult")
    fun fetchConfig() {
        ApiServiceGenerator.getApiService.getConfig()
            ?.initSchedulers()
            ?.subscribe({
                config.value = it
            },{
                configError.value = GeneralResponse(it.message)
            })
    }

    @SuppressLint("CheckResult")
    fun registerFbToken(token: String) {
        token.takeIf { it.isNotEmpty() }?.let {
            ApiServiceGenerator.getApiService.registerFbToken(RegisterFbTokenRequest(token))
                ?.initSchedulers()
                ?.subscribe({},{})
        }
    }

}