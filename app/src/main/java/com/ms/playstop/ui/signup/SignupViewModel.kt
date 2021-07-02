package com.ms.playstop.ui.signup

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
import com.ms.playstop.network.model.LoginRequest
import com.ms.playstop.network.model.SignupRequest
import com.orhanobut.hawk.Hawk
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SignupViewModel : ViewModel() {

    val signup : MutableLiveData<GeneralResponse> = MutableLiveData()
    val signupError : MutableLiveData<GeneralResponse> = MutableLiveData()

    fun signup(name: String?,email: String?,password: String?,repeatPassword: String?) {
        when {
            name.isNullOrEmpty() -> signupError.value = GeneralResponse(messageResId = R.string.name_can_not_be_empty)
            email.isNullOrEmpty() -> signupError.value = GeneralResponse(messageResId = R.string.email_can_not_be_empty)
            email.isValidEmail().not() -> signupError.value = GeneralResponse(messageResId = R.string.entered_email_is_invalid)
            password.isNullOrEmpty() -> signupError.value = GeneralResponse(messageResId = R.string.password_can_not_be_empty)
            repeatPassword.isNullOrEmpty() -> signupError.value = GeneralResponse(messageResId = R.string.repeat_password_can_not_be_empty)
            (password == repeatPassword).not() -> signupError.value = GeneralResponse(messageResId = R.string.repeat_password_not_match_password)
            else -> {
                ApiServiceGenerator.getApiService
                    .signup(SignupRequest(name,email,password))
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        login(email,password)
                    },{
                        it.getErrorHttpModel(InvalidCredentialsResponse::class.java)?.let {
                            it.getFirstMessage()?.let { message ->
                                signupError.value = GeneralResponse(message)
                            }
                        } ?: kotlin.run {
                            signupError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                        }
                    })
            }
        }
    }

    fun login(email: String?,password: String?) {
        when {
            email.isNullOrEmpty() -> signup.value = GeneralResponse(messageResId = R.string.registered_successfully)
            email.isValidEmail().not() -> signup.value = GeneralResponse(messageResId = R.string.registered_successfully)
            password.isNullOrEmpty() -> signup.value = GeneralResponse(messageResId = R.string.registered_successfully)
            else -> {
                ApiServiceGenerator.getApiService
                    .login(LoginRequest(email, password))
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
                                phone = it.phone,
                                endSubscriptionDate = it.endSubscriptionDate,
                                isSubscriptionExpired = it.isSubscriptionExpired
                            )
                            Hawk.put(Profile.SAVE_KEY, profile)
                        }
                        signup.value =
                            GeneralResponse(messageResId = R.string.registered_successfully)
                    }, {
                        signup.value =
                            GeneralResponse(messageResId = R.string.registered_successfully)
                    })
            }
        }
    }
}