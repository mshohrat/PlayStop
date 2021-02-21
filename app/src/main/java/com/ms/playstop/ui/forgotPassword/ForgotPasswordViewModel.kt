package com.ms.playstop.ui.forgotPassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.R
import com.ms.playstop.extension.isValidEmail
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.network.model.ResetPasswordRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ForgotPasswordViewModel : ViewModel() {

    val resetPassword : MutableLiveData<GeneralResponse> = MutableLiveData()
    val resetPasswordError : MutableLiveData<GeneralResponse> = MutableLiveData()

    fun resetPassword(email: String?) {
        when {
            email.isNullOrEmpty() -> resetPasswordError.value = GeneralResponse(messageResId = R.string.email_can_not_be_empty)
            email.isValidEmail().not() -> resetPasswordError.value = GeneralResponse(messageResId = R.string.entered_email_is_invalid)
            else -> {
                ApiServiceGenerator.getApiService
                    .resetPassword(ResetPasswordRequest(email))
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        resetPassword.postValue(it)
                    },{
                        resetPasswordError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                    })
            }
        }
    }
}