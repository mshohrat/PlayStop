package com.ms.playstop.ui.otp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.R
import com.ms.playstop.extension.getErrorHttpModel
import com.ms.playstop.extension.getFirstMessage
import com.ms.playstop.extension.isValidPhoneNumber
import com.ms.playstop.model.Profile
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.*
import com.orhanobut.hawk.Hawk
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OtpViewModel : ViewModel() {

    val verifyOtp : MutableLiveData<GeneralResponse> = MutableLiveData()
    val loginOtp : MutableLiveData<Pair<GeneralResponse,Boolean>> = MutableLiveData()
    val otpError : MutableLiveData<GeneralResponse> = MutableLiveData()
    val resendCode : MutableLiveData<GeneralResponse> = MutableLiveData()
    val resendCodeError : MutableLiveData<GeneralResponse> = MutableLiveData()

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
                                        phone = phoneNumber,
                                        endSubscriptionDate = it.endSubscriptionDate,
                                        isSubscriptionExpired = it.isSubscriptionExpired
                                    )
                                    Hawk.put(Profile.SAVE_KEY, profile)
                                    loginOtp.value = GeneralResponse(messageResId = R.string.logged_in_successfully) to it.isNewUser
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
                            ?.subscribe({
                                it?.user?.let { user ->
                                    val profile = Hawk.get(Profile.SAVE_KEY) as? Profile
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

    fun resendCode() {
        when(verifyState) {
            OtpFragment.VERIFY_STATE_LOGIN -> {
                when {
                    phoneNumber.isEmpty() -> resendCodeError.value = GeneralResponse(messageResId = R.string.phone_number_can_not_be_empty)
                    phoneNumber.isValidPhoneNumber().not() -> resendCodeError.value = GeneralResponse(messageResId = R.string.entered_phone_number_is_invalid)
                    else -> {
                        ApiServiceGenerator.getApiService
                            .sendPhoneNumber(PhoneNumberRequest(phoneNumber))
                            ?.subscribeOn(Schedulers.io())
                            ?.observeOn(AndroidSchedulers.mainThread())
                            ?.subscribe({
                                it?.let {
                                    resendCode.value = GeneralResponse(messageResId = R.string.otp_sent_successfully)
                                }
                            },{
                                it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                                    resendCodeError.value = it
                                } ?: kotlin.run {
                                    resendCodeError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                                }
                            })
                    }
                }
            }
            OtpFragment.VERIFY_STATE_NONE -> {
                when {
                    phoneNumber.isEmpty() -> resendCodeError.value = GeneralResponse(messageResId = R.string.phone_number_can_not_be_empty)
                    phoneNumber.isValidPhoneNumber().not() -> resendCodeError.value = GeneralResponse(messageResId = R.string.entered_phone_number_is_invalid)
                    else -> {
                        ApiServiceGenerator.getApiService
                            .addPhoneNumber(PhoneNumberRequest(phoneNumber))
                            ?.subscribeOn(Schedulers.io())
                            ?.observeOn(AndroidSchedulers.mainThread())
                            ?.subscribe({
                                it?.let {
                                    resendCode.value = GeneralResponse(messageResId = R.string.otp_sent_successfully)
                                }
                            },{
                                it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                                    resendCodeError.value = it
                                } ?: kotlin.run {
                                    resendCodeError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                                }
                            })
                    }
                }
            }
            else -> {}
        }
    }
}