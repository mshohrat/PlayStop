package com.ms.playstop.ui.enrerPhoneNumber

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.R
import com.ms.playstop.extension.*
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EnterPhoneNumberViewModel : ViewModel() {

    val verify : MutableLiveData<Pair<String?,Int>> = MutableLiveData()
    val verifyError : MutableLiveData<GeneralResponse> = MutableLiveData()

    val login : MutableLiveData<Pair<String?,Int>> = MutableLiveData()
    val loginError : MutableLiveData<GeneralResponse> = MutableLiveData()

    var state: Int = EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE_LOGIN

    fun submit(phoneNumber: String?) {
        when(state) {
            EnterPhoneNumberFragment.ENTER_PHONE_NUMBER_STATE_LOGIN -> {
                when {
                    phoneNumber.isNullOrEmpty() -> loginError.value = GeneralResponse(messageResId = R.string.phone_number_can_not_be_empty)
                    phoneNumber.isValidPhoneNumber().not() -> loginError.value = GeneralResponse(messageResId = R.string.entered_phone_number_is_invalid)
                    else -> {
                        ApiServiceGenerator.getApiService
                            .sendPhoneNumberV2(PhoneNumberRequest(phoneNumber))
                            ?.subscribeOn(Schedulers.io())
                            ?.observeOn(AndroidSchedulers.mainThread())
                            ?.subscribe({
                                it?.let {
                                    login.value = phoneNumber to R.string.otp_sent_successfully
                                }
                            },{
                                it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                                    loginError.value = it
                                } ?: kotlin.run {
                                    loginError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                                }
                            })
                    }
                }
            }
            else -> {
                when {
                    phoneNumber.isNullOrEmpty() -> verifyError.value = GeneralResponse(messageResId = R.string.phone_number_can_not_be_empty)
                    phoneNumber.isValidPhoneNumber().not() -> verifyError.value = GeneralResponse(messageResId = R.string.entered_phone_number_is_invalid)
                    else -> {
                        ApiServiceGenerator.getApiService
                            .addPhoneNumber(PhoneNumberRequest(phoneNumber))
                            ?.subscribeOn(Schedulers.io())
                            ?.observeOn(AndroidSchedulers.mainThread())
                            ?.subscribe({
                                it?.let {
                                    verify.value = phoneNumber to R.string.otp_sent_successfully
                                }
                            },{
                                it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                                    verifyError.value = it
                                } ?: kotlin.run {
                                    verifyError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                                }
                            })
                    }
                }
            }
        }
    }
}