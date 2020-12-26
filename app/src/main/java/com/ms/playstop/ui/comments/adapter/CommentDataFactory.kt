package com.ms.playstop.ui.comments.adapter

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ms.playstop.model.Comment


class CommentDataFactory(
    private val requestId: Int
) : DataSource.Factory<Int, Comment>() {

    private var commentDataSource: CommentDateSource = CommentDateSource(requestId)
    private var mutableLiveData: MutableLiveData<CommentDateSource> = MutableLiveData()

    override fun create(): DataSource<Int, Comment> {
        mutableLiveData.postValue(commentDataSource)
        return commentDataSource
    }

    fun getMutableLiveData(): MutableLiveData<CommentDateSource> {
        return mutableLiveData
    }

}