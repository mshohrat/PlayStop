package com.ms.playstop.ui.completeAccount

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.R
import com.ms.playstop.extension.getErrorHttpModel
import com.ms.playstop.extension.getFirstMessage
import com.ms.playstop.extension.isValidEmail
import com.ms.playstop.model.Profile
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.network.model.InvalidCredentialsResponse
import com.ms.playstop.network.model.UpdateAccountRequest
import com.orhanobut.hawk.Hawk
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CompleteAccountViewModel : ViewModel() {

    val completeAccount : MutableLiveData<GeneralResponse> = MutableLiveData()
    val completeAccountError : MutableLiveData<GeneralResponse> = MutableLiveData()

    val account: MutableLiveData<Profile?> = MutableLiveData()

    init {
        loadAccountData()
    }

    private fun loadAccountData() {
        if(Hawk.contains(Profile.SAVE_KEY)) {
            val profile = Hawk.get(Profile.SAVE_KEY) as? Profile
            account.value = profile
        }
    }

    fun submit(name: String?,email: String?) {
        when {
            name.isNullOrEmpty() -> completeAccountError.value = GeneralResponse(messageResId = R.string.name_can_not_be_empty)
            email.isNullOrEmpty().not().and(email?.isValidEmail()?.not() == true) -> completeAccountError.value = GeneralResponse(messageResId = R.string.entered_email_is_invalid)
            else -> {
                ApiServiceGenerator.getApiService
                    .updateAccount(UpdateAccountRequest(name,email))
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        val profile = Hawk.get(Profile.SAVE_KEY) as? Profile
                        profile?.apply {
                            this.name = name
                            this.email = email
                        }
                        Hawk.put(Profile.SAVE_KEY,profile)
                        completeAccount.value = it
                    },{
                        it.getErrorHttpModel(InvalidCredentialsResponse::class.java)?.let {
                            it.getFirstMessage()?.let { message ->
                                completeAccountError.value = GeneralResponse(message)
                            }
                        } ?: kotlin.run {
                            completeAccountError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                        }
                    })
            }
        }
    }
}