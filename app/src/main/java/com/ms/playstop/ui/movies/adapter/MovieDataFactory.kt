package com.ms.playstop.ui.movies.adapter

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ms.playstop.model.Movie


class MovieDataFactory(
    private val requestType: RequestType,
    private val requestId: Int
) : DataSource.Factory<Int, Movie>() {

    private var movieDataSource: MovieDateSource = MovieDateSource(requestType,requestId)
    private var mutableLiveData: MutableLiveData<MovieDateSource> = MutableLiveData()

    override fun create(): DataSource<Int, Movie> {
        mutableLiveData.postValue(movieDataSource)
        return movieDataSource
    }

    fun getMutableLiveData(): MutableLiveData<MovieDateSource> {
        return mutableLiveData
    }

}