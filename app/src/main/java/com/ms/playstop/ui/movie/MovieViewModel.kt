package com.ms.playstop.ui.movie

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.R
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.model.Movie
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.GeneralResponse

class MovieViewModel : ViewModel() {

    val movie : MutableLiveData<Movie?> = MutableLiveData()

    val movieError : MutableLiveData<GeneralResponse> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun fetchMovie(movieId: Int) {
        ApiServiceGenerator.getApiService
            .getMovie(movieId)
            ?.initSchedulers()
            ?.subscribe({
                it?.let {
                    movie.value = it.movie
                } ?: kotlin.run {
                    movieError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                }
            },{
                movieError.value = GeneralResponse(it.message)
            })
    }
}
