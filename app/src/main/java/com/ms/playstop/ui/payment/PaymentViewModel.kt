package com.ms.playstop.ui.payment

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.microsoft.appcenter.crashes.Crashes
import com.ms.playstop.R
import com.ms.playstop.extension.getErrorHttpModel
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.model.Product
import com.ms.playstop.model.Profile
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.network.model.VerifyPaymentRequest
import com.orhanobut.hawk.Hawk
import java.util.concurrent.TimeUnit

class PaymentViewModel : ViewModel() {

    val products: MutableLiveData<List<Product>> = MutableLiveData()
    val productsError: MutableLiveData<GeneralResponse> = MutableLiveData()
    val paymentUrl: MutableLiveData<Pair<Int,String>> = MutableLiveData()
    val paymentUrlError: MutableLiveData<GeneralResponse> = MutableLiveData()
    val verifyPayment: MutableLiveData<GeneralResponse> = MutableLiveData()
    val verifyPaymentError: MutableLiveData<GeneralResponse> = MutableLiveData()

    init {
        fetchProducts()
    }

    @SuppressLint("CheckResult")
    fun fetchProducts() {
        ApiServiceGenerator.getApiService.getProducts()
            ?.initSchedulers()
            ?.subscribe({
                it?.let {
                    products.value = it.products
                } ?: kotlin.run {
                    productsError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                }
            },{
                it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                    productsError.postValue(it)
                } ?: kotlin.run {
                    productsError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                }
            })
    }

    @SuppressLint("CheckResult")
    fun startPayment(position: Int,productId: Int) {
        ApiServiceGenerator.getApiService.startPayment(productId)
            ?.initSchedulers()
            ?.subscribe({
                it?.paymentUrl?.takeIf { it.isNotEmpty() }?.let {
                    paymentUrl.value = position to it
                } ?: kotlin.run {
                    paymentUrlError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                }
            },{
                paymentUrlError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
            })
    }

    @SuppressLint("CheckResult")
    fun verifyPayment(transactionId: String) {
        ApiServiceGenerator.getApiService.verifyPayment(VerifyPaymentRequest(transactionId))
            ?.initSchedulers()
            ?.subscribe({
                it?.let {
                    fetchUserInfo()
                } ?: kotlin.run {
                    verifyPaymentError.postValue(GeneralResponse(messageResId = R.string.payment_was_not_successful))
                }
            },{
                it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                    verifyPaymentError.postValue(it)
                } ?: kotlin.run {
                    verifyPaymentError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                }
            })
    }

    @SuppressLint("CheckResult")
    private fun fetchUserInfo() {
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
                    }
                    Hawk.put(Profile.SAVE_KEY, profile)
                }
                verifyPayment.value = GeneralResponse(messageResId = R.string.payment_was_successful)
            },{
                verifyPayment.value = GeneralResponse(messageResId = R.string.payment_was_successful)
            })
    }

}