package com.ms.playstop.ui.playVideo

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.model.Movie
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.SeenMovieRequest

class PlayVideoViewModel : ViewModel() {

    private var movieId : Int = -1
    private var movieType : Int = -1
    private var currentMovieSeenPosition = 0L
    val savedMovieSeenPosition = MutableLiveData(0L)

    fun setMovieId(movieId: Int?) {
        movieId?.let {
            this.movieId = it
            fetchSeenMoviePosition()
        }
    }

    fun setMovieType(movieType: Int?) {
        movieType?.let {
            this.movieType = it
        }
    }

    fun setSeenMoviePosition(position: Long) {
        this.currentMovieSeenPosition = position
    }

    @SuppressLint("CheckResult")
    fun fetchSeenMoviePosition() {
        when(movieType) {
            Movie.TYPE_SERIES -> {
                ApiServiceGenerator.getApiService
                    .getSeenEpisode(movieId)
                    ?.initSchedulers()
                    ?.subscribe({
                        it?.position?.let {
                            savedMovieSeenPosition.value = it
                        }
                    },{})
            }
            Movie.TYPE_MOVIE -> {
                ApiServiceGenerator.getApiService
                    .getSeenMovie(movieId)
                    ?.initSchedulers()
                    ?.subscribe({
                        it?.position?.let {
                            savedMovieSeenPosition.value = it
                        }
                    },{})
            }
        }
    }

    @SuppressLint("CheckResult")
    fun updateSeenMovie() {
        when(movieType) {
            Movie.TYPE_SERIES -> {
                ApiServiceGenerator.getApiService
                    .updateSeenEpisode(movieId,SeenMovieRequest(currentMovieSeenPosition))
                    ?.initSchedulers()
                    ?.subscribe({},{})
            }
            Movie.TYPE_MOVIE -> {
                ApiServiceGenerator.getApiService
                    .updateSeenMovie(movieId,SeenMovieRequest(currentMovieSeenPosition))
                    ?.initSchedulers()
                    ?.subscribe({},{})
            }
        }
    }

}