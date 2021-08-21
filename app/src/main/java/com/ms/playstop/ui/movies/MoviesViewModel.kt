package com.ms.playstop.ui.movies

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ms.playstop.R
import com.ms.playstop.model.Movie
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.ui.movies.adapter.MovieDataFactory
import com.ms.playstop.ui.movies.adapter.MovieDateSource
import com.ms.playstop.ui.movies.adapter.RequestType
import java.util.concurrent.Executors


class MoviesViewModel : ViewModel() {

    var movies : LiveData<PagedList<Movie>> = MutableLiveData()
    var moviesError : LiveData<GeneralResponse> = MutableLiveData()
    var moviesRequestState : LiveData<Int> = MutableLiveData()
    var sortItems : MutableList<Int> = mutableListOf()
    var selectedSort = Movie.SORT_DEFAULT
    var selectedSortName = MutableLiveData(R.string.sort_default)
    private var movieDataFactory : MovieDataFactory? = null
    var isYearMovies = false

    init {
        sortItems.add(R.string.sort_default)
        sortItems.add(R.string.sort_newest)
        sortItems.add(R.string.sort_oldest)
        sortItems.add(R.string.sort_imdb_score)
//        if(Hawk.contains(Movie.SORT_SAVE_KEY)) {
//            selectedSort = Hawk.get(Movie.SORT_SAVE_KEY,Movie.SORT_DEFAULT)
//        }
    }

    fun setRequestType(requestType: RequestType, requestId: Int, requestIds: IntArray) {
        if(requestType == RequestType.YEAR) {
            isYearMovies = true
        }
        movieDataFactory = MovieDataFactory(requestType,requestId,getRequestParams(),requestIds)
        movieDataFactory?.let { factory ->
            moviesError = Transformations.switchMap(factory.getMutableLiveData(),
                Function {
                    return@Function it.networkError
                }
            )
            moviesRequestState = Transformations.switchMap(factory.getMutableLiveData(),
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

            movies = LivePagedListBuilder(factory,pagedListConfig)
                .setFetchExecutor(executor)
                .build()
        }

    }

    fun refresh() {
        movies.value?.dataSource?.invalidate()
    }

    fun updateSort(menuItemId: Int?) {
        if(isYearMovies) {
            when(menuItemId) {
                R.id.action_sort_imdb_score -> {
                    selectedSort = Movie.SORT_SCORE_IMDB
                    selectedSortName.value = R.string.top_score
                }
                else -> {
                    selectedSort = Movie.SORT_DEFAULT
                    selectedSortName.value = R.string.sort_default
                }
            }
        } else {
            when(menuItemId) {
                R.id.action_sort_newest -> {
                    selectedSort = Movie.SORT_NEWEST
                    selectedSortName.value = R.string.newest
                }
                R.id.action_sort_oldest -> {
                    selectedSort = Movie.SORT_OLDEST
                    selectedSortName.value = R.string.oldest
                }
                R.id.action_sort_imdb_score -> {
                    selectedSort = Movie.SORT_SCORE_IMDB
                    selectedSortName.value = R.string.top_score
                }
                else -> {
                    selectedSort = Movie.SORT_DEFAULT
                    selectedSortName.value = R.string.sort_default
                }
            }
        }
        movieDataFactory?.updateRequestParams(getRequestParams())
        refresh()
    }

//    fun updateSort(position: Int) {
//        if(isYearMovies) {
//            selectedSort = when(position) {
//                1 -> {
//                    Movie.SORT_SCORE_IMDB
//                }
//                else -> {
//                    Movie.SORT_DEFAULT
//                }
//            }
//        } else {
//            selectedSort = when(position) {
//                1 -> {
//                    Movie.SORT_NEWEST
//                }
//                2 -> {
//                    Movie.SORT_OLDEST
//                }
//                3 -> {
//                    Movie.SORT_SCORE_IMDB
//                }
//                else -> {
//                    Movie.SORT_DEFAULT
//                }
//            }
//        }
//        movieDataFactory?.updateRequestParams(getRequestParams())
//        refresh()
//    }

    private fun getRequestParams() : Map<String,String> {
        val params = mutableMapOf<String,String>()
        params[MovieDateSource.REQUEST_PARAM_SORT] = selectedSort.toString()
        return params
    }

//    fun getSelectedSortPosition() : Int {
//        return if(isYearMovies) {
//            when(selectedSort) {
//                Movie.SORT_SCORE_IMDB -> 1
//                else -> 0
//            }
//        } else {
//            when(selectedSort) {
//                Movie.SORT_NEWEST -> 1
//                Movie.SORT_OLDEST -> 2
//                Movie.SORT_SCORE_IMDB -> 3
//                else -> 0
//            }
//        }
//    }

    fun getSelectedSortMenuItemId() : Int {
        return if(isYearMovies) {
            when(selectedSort) {
                Movie.SORT_SCORE_IMDB -> R.id.action_sort_imdb_score
                else -> R.id.action_sort_default
            }
        } else {
            when(selectedSort) {
                Movie.SORT_NEWEST -> R.id.action_sort_newest
                Movie.SORT_OLDEST -> R.id.action_sort_oldest
                Movie.SORT_SCORE_IMDB -> R.id.action_sort_imdb_score
                else -> R.id.action_sort_default
            }
        }
    }
}
