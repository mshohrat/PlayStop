package com.ms.playstop.ui.character

import android.annotation.SuppressLint
import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ms.playstop.R
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.model.Character
import com.ms.playstop.model.Movie
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.ui.movies.adapter.MovieDataFactory
import com.ms.playstop.ui.movies.adapter.MovieDateSource
import com.ms.playstop.ui.movies.adapter.RequestType
import java.util.concurrent.Executors

class CharacterViewModel : ViewModel() {

    var movies : LiveData<PagedList<Movie>> = MutableLiveData()
    var moviesError : LiveData<GeneralResponse> = MutableLiveData()
    var characterInfo : MutableLiveData<Character?> = MutableLiveData()
    var characterInfoError : MutableLiveData<GeneralResponse> = MutableLiveData()
    var moviesRequestState : LiveData<Int> = MutableLiveData()
    var sortItems : MutableList<Int> = mutableListOf()
    var selectedSort = Movie.SORT_DEFAULT
    var selectedSortName = MutableLiveData(R.string.sort_default)
    private var movieDataFactory : MovieDataFactory? = null
    private var characterId: Int = -1
    private var requestType = RequestType.ACTOR

    init {
        sortItems.add(R.string.sort_default)
        sortItems.add(R.string.sort_newest)
        sortItems.add(R.string.sort_oldest)
        sortItems.add(R.string.sort_imdb_score)
    }

    fun setRequestType(requestType: RequestType, requestId: Int) {
        this.requestType = requestType
        this.characterId = requestId
        movieDataFactory = MovieDataFactory(requestType, requestId, getRequestParams())
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
        movieDataFactory?.updateRequestParams(getRequestParams())
        refresh()
    }

    private fun getRequestParams() : Map<String,String> {
        val params = mutableMapOf<String,String>()
        params[MovieDateSource.REQUEST_PARAM_SORT] = selectedSort.toString()
        return params
    }

    fun getSelectedSortMenuItemId() : Int {
        return when(selectedSort) {
            Movie.SORT_NEWEST -> R.id.action_sort_newest
            Movie.SORT_OLDEST -> R.id.action_sort_oldest
            Movie.SORT_SCORE_IMDB -> R.id.action_sort_imdb_score
            else -> R.id.action_sort_default
        }
    }

    @SuppressLint("CheckResult")
    fun fetchCharacterInfo() {
        when(requestType) {
            RequestType.ACTOR -> {
                ApiServiceGenerator.getApiService
                    .getActorInfo(characterId)
                    ?.initSchedulers()
                    ?.subscribe({
                        it?.let {
                            characterInfo.value = it.actor
                        } ?: kotlin.run {
                            characterInfoError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                        }
                    },{
                        characterInfoError.value = GeneralResponse(it.message)
                    })
            }
            RequestType.DIRECTOR -> {
                ApiServiceGenerator.getApiService
                    .getDirectorInfo(characterId)
                    ?.initSchedulers()
                    ?.subscribe({
                        it?.let {
                            characterInfo.value = it.director
                        } ?: kotlin.run {
                            characterInfoError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                        }
                    },{
                        characterInfoError.value = GeneralResponse(it.message)
                    })
            }
            RequestType.WRITER -> {
                ApiServiceGenerator.getApiService
                    .getWriterInfo(characterId)
                    ?.initSchedulers()
                    ?.subscribe({
                        it?.let {
                            characterInfo.value = it.writer
                        } ?: kotlin.run {
                            characterInfoError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                        }
                    },{
                        characterInfoError.value = GeneralResponse(it.message)
                    })
            }
            else -> {}
        }
    }
}