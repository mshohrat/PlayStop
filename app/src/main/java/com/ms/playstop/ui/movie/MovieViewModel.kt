package com.ms.playstop.ui.movie

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.R
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.model.Movie
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.network.model.PostCommentRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MovieViewModel : ViewModel() {

    val movie : MutableLiveData<Movie?> = MutableLiveData()
    val movieError : MutableLiveData<GeneralResponse> = MutableLiveData()
    val sendComment : MutableLiveData<GeneralResponse> = MutableLiveData()
    val sendCommentError : MutableLiveData<GeneralResponse> = MutableLiveData()
    val likeMovie : MutableLiveData<GeneralResponse> = MutableLiveData()
    val likeMovieError : MutableLiveData<GeneralResponse> = MutableLiveData()
    val similarMovies : MutableLiveData<List<Movie>> = MutableLiveData()
    val similarMoviesError : MutableLiveData<GeneralResponse> = MutableLiveData()
    private var movieId: Int = 0

    @SuppressLint("CheckResult")
    fun fetchMovie(movieId: Int) {
        this.movieId = movieId
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

    fun refresh() {
        fetchMovie(movieId)
    }

    fun sendComment(text: String?) {
        when {
            text.isNullOrEmpty() -> sendCommentError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
            else -> {
                ApiServiceGenerator.getApiService
                    .postComment(PostCommentRequest(text),movieId)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        it?.let {
                            sendComment.value = GeneralResponse(messageResId = R.string.comment_posted_successfully)
                        } ?: kotlin.run {
                            sendCommentError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                        }
                    },{
                        sendCommentError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                    })
            }
        }
    }

    @SuppressLint("CheckResult")
    fun likeOrDislikeMovie() {
        movie.value?.let {
            if(it.isLiked) {
                movie.postValue(it.apply { isLiked = false })
                ApiServiceGenerator.getApiService
                    .dislikeMovie(movieId)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({ response ->
                        response?.let { res ->
                            likeMovie.value = res
                            movie.postValue(it.apply { isLiked = false })
                        } ?: kotlin.run {
                            likeMovieError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                            movie.postValue(it.apply { isLiked = true })
                        }
                    },{ throwable ->
                        likeMovieError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                        movie.postValue(it.apply { isLiked = true })
                    })
            } else {
                movie.postValue(it.apply { isLiked = true })
                ApiServiceGenerator.getApiService
                    .likeMovie(movieId)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({ response ->
                        response?.let { res ->
                            likeMovie.value = res
                            movie.postValue(it.apply { isLiked = true })
                        } ?: kotlin.run {
                            likeMovieError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                            movie.postValue(it.apply { isLiked = false })
                        }
                    },{ throwable ->
                        likeMovieError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                        movie.postValue(it.apply { isLiked = false })
                    })
            }
        }

    }

    @SuppressLint("CheckResult")
    fun fetchSimilarMovies() {
        if(movieId == 0) {
            similarMoviesError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
        }
        ApiServiceGenerator.getApiService
            .getSimilarMovies(movieId)
            ?.initSchedulers()
            ?.subscribe({
                it?.let {
                    similarMovies.value = it.movies
                } ?: kotlin.run {
                    movieError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                }
            },{
                movieError.value = GeneralResponse(it.message)
            })
    }
}
