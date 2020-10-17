package com.ms.playstop.ui.movies.adapter

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ms.playstop.model.Movie


class MovieDataFactory(
    private val requestType: RequestType,
    private val requestId: Int
) : DataSource.Factory<Int, Movie>() {

    private var mutableLiveData: MutableLiveData<MovieDateSource> = MutableLiveData()

    override fun create(): DataSource<Int, Movie> {
        val movieDateSource = MovieDateSource(requestType,requestId)
        mutableLiveData.postValue(movieDateSource)
        return movieDateSource
    }

    fun getMutableLiveData(): MutableLiveData<MovieDateSource> {
        return mutableLiveData
    }

}