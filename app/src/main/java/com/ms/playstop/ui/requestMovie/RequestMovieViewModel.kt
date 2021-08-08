package com.ms.playstop.ui.requestMovie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.R
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.network.model.RequestMovieRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RequestMovieViewModel : ViewModel() {

    val submitRequest : MutableLiveData<GeneralResponse> = MutableLiveData()
    val submitRequestError : MutableLiveData<GeneralResponse> = MutableLiveData()

    fun submit(requestText: String?) {
        when {
            requestText.isNullOrEmpty() -> submitRequestError.value = GeneralResponse(messageResId = R.string.request_text_can_not_be_empty)
            else -> {
                ApiServiceGenerator.getApiService
                    .requestMovie(RequestMovieRequest(requestText))
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        it?.let {
                            submitRequest.value = GeneralResponse(messageResId = R.string.request_posted_successfully)
                        } ?: kotlin.run {
                            submitRequestError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                        }
                    },{
                        submitRequestError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                    })
            }
        }
    }
}