package com.ms.playstop.ui.playVideo

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.SeenMovieRequest

class PlayVideoViewModel : ViewModel() {

    private var movieId : Int = -1
    private var currentMovieSeenPosition = 0L
    val savedMovieSeenPosition = MutableLiveData(0L)

    fun setMovieId(movieId: Int?) {
        movieId?.let {
            this.movieId = it
            fetchSeenMoviePosition()
        }
    }

    fun setSeenMoviePosition(position: Long) {
        this.currentMovieSeenPosition = position
    }

    @SuppressLint("CheckResult")
    fun fetchSeenMoviePosition() {
        ApiServiceGenerator.getApiService
            .getSeenMovie(movieId)
            ?.initSchedulers()
            ?.subscribe({
               it?.position?.let {
                   savedMovieSeenPosition.value = it
               }
            },{})
    }

    @SuppressLint("CheckResult")
    fun updateSeenMovie() {
        savedMovieSeenPosition.value = currentMovieSeenPosition
        ApiServiceGenerator.getApiService
            .updateSeenMovie(movieId,SeenMovieRequest(currentMovieSeenPosition))
            ?.initSchedulers()
            ?.subscribe({},{})
    }

}