package com.ms.playstop.ui.movies.adapter

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.ms.playstop.R
import com.ms.playstop.extension.getErrorHttpModel
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.model.Movie
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.network.model.GenresSuggestionMoviesRequest

class MovieDateSource(val requestType: RequestType, val requestId: Int, var requestParams: Map<String,String>? = null, var requestIds: IntArray = intArrayOf()): PageKeyedDataSource<Int, Movie>() {

    companion object {
        const val STATE_LOADING = 100
        const val STATE_SUCCESS = 101
        const val STATE_ERROR = 102
        const val REQUEST_PARAM_SORT = "sort"
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
                    ApiServiceGenerator.getApiService.getSpecialMovies(
                        1,
                        getSortRequestParam()
                    )
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
                            it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                                networkError.postValue(it)
                            } ?: kotlin.run {
                                networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                            }
                        })
                }
                else if(requestId == -1) {
                    requestState.postValue(STATE_LOADING)
                    ApiServiceGenerator.getApiService.getMovies(
                        1,
                        getSortRequestParam()
                    )
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
                            it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                                networkError.postValue(it)
                            } ?: kotlin.run {
                                networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                            }
                        })
                } else {
                    requestState.postValue(STATE_LOADING)
                    ApiServiceGenerator.getApiService.getSuggestionMovies(
                        1,
                        requestId,
                        getSortRequestParam()
                    )
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
                            it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                                networkError.postValue(it)
                            } ?: kotlin.run {
                                networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                            }
                        })
                }
            }
            RequestType.CATEGORY -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getCategoryMovies(
                    1,
                    requestId,
                    getSortRequestParam()
                )
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
                        it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                            networkError.postValue(it)
                        } ?: kotlin.run {
                            networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                        }
                    })
            }
            RequestType.GENRE -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getGenreMovies(
                    1,
                    requestId,
                    getSortRequestParam()
                )
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
                        it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                            networkError.postValue(it)
                        } ?: kotlin.run {
                            networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                        }
                    })
            }
            RequestType.SPECIAL -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getSpecialMovies(
                    1,
                    getSortRequestParam()
                )
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
                        it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                            networkError.postValue(it)
                        } ?: kotlin.run {
                            networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                        }
                    })
            }
            RequestType.YEAR -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getYearMovies(
                    requestId,
                    1,
                    getSortRequestParam()
                )
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
                        it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                            networkError.postValue(it)
                        } ?: kotlin.run {
                            networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                        }
                    })
            }
            RequestType.LIKES -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getLikedMovies(
                    1,
                    getSortRequestParam()
                )
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
                        it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                            networkError.postValue(it)
                        } ?: kotlin.run {
                            networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                        }
                    })
            }
            RequestType.ACTOR -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getActorMovies(
                    1,
                    requestId,
                    getSortRequestParam()
                )
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
                        it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                            networkError.postValue(it)
                        } ?: kotlin.run {
                            networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                        }
                    })
            }
            RequestType.DIRECTOR -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getDirectorMovies(
                    1,
                    requestId,
                    getSortRequestParam()
                )
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
                        it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                            networkError.postValue(it)
                        } ?: kotlin.run {
                            networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                        }
                    })
            }
            RequestType.WRITER -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getWriterMovies(
                    1,
                    requestId,
                    getSortRequestParam()
                )
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
                        it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                            networkError.postValue(it)
                        } ?: kotlin.run {
                            networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                        }
                    })
            }
            RequestType.GENRES_SUGGESTION -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getGenresSuggestionMovies(
                    1,
                    GenresSuggestionMoviesRequest(
                        requestIds.toList(),
                        getSortRequestParam()
                    )
                )
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
                        it.getErrorHttpModel(GeneralResponse::class.java)?.let {
                            networkError.postValue(it)
                        } ?: kotlin.run {
                            networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                        }
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
                    ApiServiceGenerator.getApiService.getSpecialMovies(
                        params.key,
                        getSortRequestParam()
                    )
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
                    ApiServiceGenerator.getApiService.getMovies(
                        params.key,
                        getSortRequestParam()
                    )
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
                    ApiServiceGenerator.getApiService.getSuggestionMovies(
                        params.key,
                        requestId,
                        getSortRequestParam()
                    )
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
                ApiServiceGenerator.getApiService.getCategoryMovies(
                    params.key,
                    requestId,
                    getSortRequestParam()
                )
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
                ApiServiceGenerator.getApiService.getGenreMovies(
                    params.key,
                    requestId,
                    getSortRequestParam()
                )
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
                ApiServiceGenerator.getApiService.getSpecialMovies(
                    params.key,
                    getSortRequestParam()
                )
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
                ApiServiceGenerator.getApiService.getYearMovies(
                    requestId,
                    params.key,
                    getSortRequestParam()
                )
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
            RequestType.LIKES -> {
                ApiServiceGenerator.getApiService.getLikedMovies(
                    params.key,
                    getSortRequestParam()
                )
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
            RequestType.ACTOR -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getActorMovies(
                    params.key,
                    requestId,
                    getSortRequestParam()
                )
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
            RequestType.DIRECTOR -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getDirectorMovies(
                    params.key,
                    requestId,
                    getSortRequestParam()
                )
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
            RequestType.WRITER -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getWriterMovies(
                    params.key,
                    requestId,
                    getSortRequestParam()
                )
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
            RequestType.GENRES_SUGGESTION -> {
                requestState.postValue(STATE_LOADING)
                ApiServiceGenerator.getApiService.getGenresSuggestionMovies(
                    params.key,
                    GenresSuggestionMoviesRequest(
                        requestIds.toList(),
                        getSortRequestParam()
                    )
                )
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

    private fun getSortRequestParam(): String? {
        return if (requestParams?.containsKey(REQUEST_PARAM_SORT) == true)
            requestParams?.get(REQUEST_PARAM_SORT)
        else
            null
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Movie>
    ) {

    }
}