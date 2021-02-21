package com.ms.playstop.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.R
import com.ms.playstop.extension.getErrorHttpModel
import com.ms.playstop.extension.getFirstMessage
import com.ms.playstop.extension.isUnauthorizedError
import com.ms.playstop.extension.isValidEmail
import com.ms.playstop.model.Profile
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.network.model.InvalidCredentialsResponse
import com.ms.playstop.network.model.LoginRequest
import com.orhanobut.hawk.Hawk
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel : ViewModel() {

    val login : MutableLiveData<Pair<Boolean,Int>> = MutableLiveData()
    val loginError : MutableLiveData<GeneralResponse> = MutableLiveData()

    fun login(email: String?,password: String?) {
        when {
            email.isNullOrEmpty() -> loginError.value = GeneralResponse(messageResId = R.string.email_can_not_be_empty)
            email.isValidEmail().not() -> loginError.value = GeneralResponse(messageResId = R.string.entered_email_is_invalid)
            password.isNullOrEmpty() -> loginError.value = GeneralResponse(messageResId = R.string.password_can_not_be_empty)
            else -> {
                ApiServiceGenerator.getApiService
                    .login(LoginRequest(email,password))
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        it?.let {
                            val profile = Profile(
                                it.name,
                                it.email,
                                token = it.token,
                                refreshToken = it.refreshToken,
                                expiresIn = it.expiresIn,
                                isActive = it.isUserActive,
                                isPhoneVerified = it.isPhoneVerified,
                                isEmailVerified = it.isEmailVerified,
                                phone = it.phone
                            )
                            Hawk.put(Profile.SAVE_KEY,profile)
                            login.value = it.isPhoneVerified to R.string.logged_in_successfully
                        }
                    },{
                        it.getErrorHttpModel(InvalidCredentialsResponse::class.java)?.let {
                            it.getFirstMessage()?.let { message ->
                                loginError.value = GeneralResponse(message)
                            }
                        } ?: kotlin.run {
                            if(it.isUnauthorizedError()) {
                                loginError.value = GeneralResponse(messageResId = R.string.email_or_password_is_incorrect)
                            } else {
                                loginError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                            }
                        }
                    })
            }
        }
    }
}