package com.ms.playstop.ui.comments.adapter

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.ms.playstop.R
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.model.Comment
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.GeneralResponse

class CommentDateSource(val movieId: Int): PageKeyedDataSource<Int, Comment>() {

    val networkError = MutableLiveData<GeneralResponse>()

    @SuppressLint("CheckResult")
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int,Comment>
    ) {
        ApiServiceGenerator.getApiService.getMovieComments(movieId,1)
            ?.initSchedulers()
            ?.subscribe({ response ->
                response?.comments?.let { comments ->
                    val nextKey = if(response.currentPage == response.totalPages) null else 2
                    callback.onResult(comments,null,nextKey)
                } ?: kotlin.run {
                    networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
                }
            },{
                networkError.postValue(GeneralResponse(messageResId = R.string.failed_in_communication_with_server))
            })
    }

    @SuppressLint("CheckResult")
    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Comment>
    ) {
        ApiServiceGenerator.getApiService.getMovieComments(movieId,params.key)
            ?.initSchedulers()
            ?.subscribe({
                it?.comments?.let { comments ->
                    val nextKey = if (params.key == it.totalPages) null else params.key+1;
                    callback.onResult(comments,nextKey)
                } ?: kotlin.run {
                }
            },{
            })
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Comment>
    ) {

    }
}