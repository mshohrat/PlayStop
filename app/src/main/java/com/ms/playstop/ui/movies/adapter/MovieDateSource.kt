package com.ms.playstop.ui.movies.adapter

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.ms.playstop.R
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.model.Movie
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.GeneralResponse
import java.util.concurrent.TimeUnit

class MovieDateSource(val requestType: RequestType, val requestId: Int): PageKeyedDataSource<Int, Movie>() {

    companion object {
        const val STATE_LOADING = 100
        const val STATE_SUCCESS = 101
        const val STATE_ERROR = 102
    }

    val requestState = MutableLiveData<Int>()
    val networkError = MutableLiveData<GeneralResponse>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int,Movie>
    ) {
        when(requestType) {
            RequestType.SUGGESTION -> {
                if(requestId == -2) {
                    requestState.postValue(STATE_LOADING)
                    ApiServiceGenerator.getApiService.getSpecialMovies(1)
                        ?.initSchedulers()
                        ?.subscribe({
                            val nextKey = if(it?.currentPage == it?.totalPages) null else 2
                            it?.movies?.let {
                                requestState.postValue(STATE_SUCCESS)
                                callback.onResult(it,null,nextKey)
                            } ?: kotlin.run {
                                requestState.postValue(STATE_ERROR)
                                networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                            }
                        },{
                            requestState.postValue(STATE_ERROR)
                            networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                        })
                }
                else if(requestId == -1) {
                    requestState.postValue(STATE_LOADING)
                    ApiServiceGenerator.getApiService.getLastMovies(1)
                        ?.initSchedulers()
                        ?.subscribe({
                            val nextKey = if(it?.currentPage == it?.totalPages) null else 2
                            it?.movies?.let {
                                requestState.postValue(STATE_SUCCESS)
                                callback.onResult(it,null,nextKey)
                            } ?: kotlin.run {
                                requestState.postValue(STATE_ERROR)
                                networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                            }
                        },{
                            requestState.postValue(STATE_ERROR)
                            networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                        })
                } else {
                    requestState.postValue(STATE_LOADING)
                    ApiServiceGenerator.getApiService.getSuggestionMovies(1,requestId)
                        ?.initSchedulers()
                        ?.subscribe({
                            val nextKey = if(it?.currentPage == it?.totalPages) null else 2
                            it?.movies?.let {
                                requestState.postValue(STATE_SUCCESS)
                                callback.onResult(it,null,nextKey)
                            } ?: kotlin.run {
                                requestState.postValue(STATE_ERROR)
                                networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                            }
                        },{
                            requestState.postValue(STATE_ERROR)
                            networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                        })
                }
            }
            RequestType.CATEGORY -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getCategoryMovies(1,requestId)
                    ?.initSchedulers()
                    ?.subscribe({
                        val nextKey = if(it?.currentPage == it?.totalPages) null else 2
                        it?.movies?.let {
                            requestState.postValue(STATE_SUCCESS)
                            callback.onResult(it,null,nextKey)
                        } ?: kotlin.run {
                            requestState.postValue(STATE_ERROR)
                            networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                        }
                    },{
                        requestState.postValue(STATE_ERROR)
                        networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                    })
            }
            RequestType.GENRE -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getGenreMovies(1,requestId)
                    ?.initSchedulers()
                    ?.subscribe({
                        val nextKey = if(it?.currentPage == it?.totalPages) null else 2
                        it?.movies?.let {
                            requestState.postValue(STATE_SUCCESS)
                            callback.onResult(it,null,nextKey)
                        } ?: kotlin.run {
                            requestState.postValue(STATE_ERROR)
                            networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                        }
                    },{
                        requestState.postValue(STATE_ERROR)
                        networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                    })
            }
            RequestType.SPECIAL -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getSpecialMovies(1)
                    ?.initSchedulers()
                    ?.subscribe({
                        val nextKey = if(it?.currentPage == it?.totalPages) null else 2
                        it?.movies?.let {
                            requestState.postValue(STATE_SUCCESS)
                            callback.onResult(it,null,nextKey)
                        } ?: kotlin.run {
                            requestState.postValue(STATE_ERROR)
                            networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                        }
                    },{
                        requestState.postValue(STATE_ERROR)
                        networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                    })
            }
            RequestType.YEAR -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getYearMovies(requestId,1)
                    ?.initSchedulers()
                    ?.subscribe({
                        val nextKey = if(it?.currentPage == it?.totalPages) null else 2
                        it?.movies?.let {
                            requestState.postValue(STATE_SUCCESS)
                            callback.onResult(it,null,nextKey)
                        } ?: kotlin.run {
                            requestState.postValue(STATE_ERROR)
                            networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                        }
                    },{
                        requestState.postValue(STATE_ERROR)
                        networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                    })
            }
            else -> {}
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Movie>
    ) {
        when(requestType) {
            RequestType.SUGGESTION -> {
                if(requestId == -2) {
                    ApiServiceGenerator.getApiService.getSpecialMovies(params.key)
                        ?.initSchedulers()
                        ?.subscribe({
                            it?.movies?.let { movies ->
                                val nextKey = if (params.key == it.totalPages) null else params.key+1;
                                callback.onResult(movies,nextKey)
                            } ?: kotlin.run {
                            }
                        },{
                        })
                }
                else if(requestId == -1) {
                    ApiServiceGenerator.getApiService.getLastMovies(params.key)
                        ?.initSchedulers()
                        ?.subscribe({
                            it?.movies?.let { movies ->
                                val nextKey = if (params.key == it.totalPages) null else params.key+1;
                                callback.onResult(movies,nextKey)
                            } ?: kotlin.run {
                            }
                        },{
                        })
                } else {
                    ApiServiceGenerator.getApiService.getSuggestionMovies(params.key,requestId)
                        ?.initSchedulers()
                        ?.subscribe({
                            it?.movies?.let { movies ->
                                val nextKey = if (params.key == it.totalPages) null else params.key+1;
                                callback.onResult(movies,nextKey)
                            } ?: kotlin.run {
                            }
                        },{
                        })
                }
            }
            RequestType.CATEGORY -> {
                ApiServiceGenerator.getApiService.getCategoryMovies(params.key,requestId)
                    ?.initSchedulers()
                    ?.subscribe({
                        it?.movies?.let { movies ->
                            val nextKey = if (params.key == it.totalPages) null else params.key+1;
                            callback.onResult(movies,nextKey)
                        } ?: kotlin.run {
                        }
                    },{
                    })
            }
            RequestType.GENRE -> {
                ApiServiceGenerator.getApiService.getGenreMovies(params.key,requestId)
                    ?.initSchedulers()
                    ?.subscribe({
                        it?.movies?.let { movies ->
                            val nextKey = if (params.key == it.totalPages) null else params.key+1;
                            callback.onResult(movies,nextKey)
                        } ?: kotlin.run {
                        }
                    },{
                    })
            }
            RequestType.SPECIAL -> {
                ApiServiceGenerator.getApiService.getSpecialMovies(params.key)
                    ?.initSchedulers()
                    ?.subscribe({
                        it?.movies?.let { movies ->
                            val nextKey = if (params.key == it.totalPages) null else params.key+1;
                            callback.onResult(movies,nextKey)
                        } ?: kotlin.run {
                        }
                    },{
                    })
            }
            RequestType.YEAR -> {
                ApiServiceGenerator.getApiService.getYearMovies(requestId,params.key)
                    ?.initSchedulers()
                    ?.subscribe({
                        it?.movies?.let { movies ->
                            val nextKey = if (params.key == it.totalPages) null else params.key+1;
                            callback.onResult(movies,nextKey)
                        } ?: kotlin.run {
                        }
                    },{
                    })
            }
            else -> {}
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Movie>
    ) {

    }
}