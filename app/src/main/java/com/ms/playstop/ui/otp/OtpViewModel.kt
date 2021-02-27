package com.ms.playstop.ui.otp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.R
import com.ms.playstop.extension.getErrorHttpModel
import com.ms.playstop.extension.getFirstMessage
import com.ms.playstop.model.Profile
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.*
import com.orhanobut.hawk.Hawk
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OtpViewModel : ViewModel() {

    val verifyOtp : MutableLiveData<GeneralResponse> = MutableLiveData()
    val loginOtp : MutableLiveData<GeneralResponse> = MutableLiveData()
    val otpError : MutableLiveData<GeneralResponse> = MutableLiveData()

    var phoneNumber: String = ""
    var email: String = ""
    var password: String = ""
    var verifyState: Int = OtpFragment.VERIFY_STATE_NONE

    fun verify(code: String?) {
        when(verifyState) {
            OtpFragment.VERIFY_STATE_LOGIN -> {
                when {
                    code.isNullOrEmpty() -> otpError.value = GeneralResponse(messageResId = R.string.otp_can_not_be_empty)
                    code.length < 5 -> otpError.value = GeneralResponse(messageResId = R.string.entered_otp_is_invalid)
                    else -> {
                        ApiServiceGenerator.getApiService
                            .loginWithPhoneNumber(LoginWithPhoneNumberRequest(phoneNumber,code))
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
                                        phone = phoneNumber
                                    )
                                    Hawk.put(Profile.SAVE_KEY, profile)
                                    loginOtp.value = GeneralResponse(messageResId = R.string.logged_in_successfully)
                                } ?: kotlin.run {
                                    otpError.value = GeneralResponse(messageResId = R.string.entered_otp_is_invalid)
                                }
                            },{
                                it.getErrorHttpModel(InvalidCredentialsResponse::class.java)?.let {
                                    it.getFirstMessage()?.let { message ->
                                        otpError.value = GeneralResponse(message)
                                    }
                                } ?: kotlin.run {
                                    otpError.value = GeneralResponse(messageResId = R.string.entered_otp_is_invalid)
                                }
                            })
                    }
                }
            }
            OtpFragment.VERIFY_STATE_NONE -> {
                when {
                    code.isNullOrEmpty() -> otpError.value = GeneralResponse(messageResId = R.string.otp_can_not_be_empty)
                    code.length < 5 -> otpError.value = GeneralResponse(messageResId = R.string.entered_otp_is_invalid)
                    else -> {
                        ApiServiceGenerator.getApiService
                            .verifyPhoneNumber(VerifyPhoneNumberRequest(phoneNumber,code))
                            ?.subscribeOn(Schedulers.io())
                            ?.observeOn(AndroidSchedulers.mainThread())
                            ?.subscribe({ user ->
                                user?.let {
                                    val profile = Hawk.get<Profile?>(Profile.SAVE_KEY)
                                    profile?.apply {
                                        isPhoneVerified = true
                                        phone = user.phone
                                    }
                                    Hawk.put(Profile.SAVE_KEY,profile)
                                    verifyOtp.value = GeneralResponse(messageResId = R.string.phone_number_verified_successfully)
                                }
                            },{
                                it.getErrorHttpModel(InvalidCredentialsResponse::class.java)?.let {
                                    it.getFirstMessage()?.let { message ->
                                        otpError.value = GeneralResponse(message)
                                    }
                                } ?: kotlin.run {
                                    otpError.value = GeneralResponse(messageResId = R.string.entered_otp_is_invalid)
                                }
                            })
                    }
                }
            }
            else -> {}
        }
    }
}