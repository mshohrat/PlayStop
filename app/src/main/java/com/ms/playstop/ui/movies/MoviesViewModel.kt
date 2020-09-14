package com.ms.playstop.ui.movies

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ms.playstop.model.Movie
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.ui.movies.adapter.MovieDataFactory
import com.ms.playstop.ui.movies.adapter.RequestType
import java.util.concurrent.Executors


class MoviesViewModel : ViewModel() {

    var movies : LiveData<PagedList<Movie>> = MutableLiveData()
    var moviesError : LiveData<GeneralResponse> = MutableLiveData()
    var moviesRequestState : LiveData<Int> = MutableLiveData()

    fun setRequestType(requestType: RequestType,requestId: Int) {
        val movieDataFactory = MovieDataFactory(requestType,requestId)
        moviesError = Transformations.switchMap(movieDataFactory.getMutableLiveData(),
            Function {
                return@Function it.networkError
            }
        )
        moviesRequestState = Transformations.switchMap(movieDataFactory.getMutableLiveData(),
            Function {
                return@Function it.requestState
            }
        )


        val executor = Executors.newFixedThreadPool(5);
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(15)
            .setPageSize(10)
            .build()

        movies = LivePagedListBuilder(movieDataFactory,pagedListConfig)
            .setFetchExecutor(executor)
            .build()
    }
}
