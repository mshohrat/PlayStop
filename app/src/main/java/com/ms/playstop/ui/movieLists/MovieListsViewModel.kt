package com.ms.playstop.ui.movieLists

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.R
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.model.*
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.ConfigResponse
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.network.model.GetAllSuggestionsMoviesRequest
import com.orhanobut.hawk.Hawk

class MovieListsViewModel : ViewModel() {

    val suggestions = MutableLiveData<ArrayList<Suggestion>>(arrayListOf())

    val moviesError  = MutableLiveData<GeneralResponse>()

    val moviesList : MutableLiveData<List<SuggestionMovies>> = MutableLiveData(
        listOf()
    )

    val moviesExtraList : MutableLiveData<List<GenresSuggestionMovies>> = MutableLiveData(
        listOf()
    )

    var isAllExtraMoviesFetched = false

    val specialMoviesList : MutableLiveData<ArrayList<Movie>?> = MutableLiveData(
        null
    )

    var moviesPage = 1

    init {
        if(Hawk.contains(ConfigResponse.SAVE_KEY)) {
            val config = Hawk.get(ConfigResponse.SAVE_KEY) as? ConfigResponse
            val l = ArrayList<SuggestionMovies>()
            config?.suggestions?.let {
                val list = ArrayList<Suggestion>()
                for (suggestion in it) {
                    list.add(suggestion)
                }
                suggestions.value = suggestions.value?.apply {
                    this.addAll(list)
                }
                suggestions.value?.let {
                    for (s in it) {
                        l.add(SuggestionMovies(s, listOf()))
                    }
                }
            }
            config?.specialsName?.let {
                specialMoviesList.value = arrayListOf()
                l.add(0,SuggestionMovies(Suggestion(-2, it), listOf()))
            } ?: kotlin.run {
                specialMoviesList.value = null
            }
            moviesList.value = l
        }
        fetchMovies()
    }

    @SuppressLint("CheckResult")
    fun fetchMovies(fresh: Boolean = false) {
        if(fresh) {
            isAllExtraMoviesFetched = false
            moviesPage = 1
        }
        if(moviesPage == 1 || fresh) {
            suggestions.value?.let {
                val suggestionIds = arrayListOf<Int>()
                for (suggestion in it) {
                    suggestionIds.add(suggestion.id)
                }
                ApiServiceGenerator.getApiService
                    .getAllSuggestionsMovies(
                        GetAllSuggestionsMoviesRequest(
                            suggestionIds
                        )
                    )
                    ?.initSchedulers()
                    ?.subscribe({ response ->
                        var specials: SuggestionMovies? = null
                        response?.specialMovies?.takeIf { it.movies.size > 5 }?.let {
                            specials = SuggestionMovies(Suggestion(-2, it.name), it.movies)
                        }
                        response?.suggestionMovies?.apply {
                            specials?.let { s ->
                                this.add(0,s)
                            }
                        }?.let {
                            moviesList.value = it
                            moviesPage++
                        } ?: kotlin.run {
                            moviesError.value =
                                GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                        }
                        response?.specialMovies?.movies?.takeIf { it.size > 8 }?.let {
                            specialMoviesList.value = ArrayList(it.take(8))
                        } ?: kotlin.run {
                            response?.specialMovies?.movies?.takeIf { it.isNotEmpty() }?.let {
                                specialMoviesList.value = it
                            } ?: kotlin.run {

                            }
                        }
                    }, {
                        moviesError.value =
                            GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                    })
            }
        } else {
            if(isAllExtraMoviesFetched.not()) {
                ApiServiceGenerator.getApiService
                    .getAllGenresSuggestionsMovies(moviesPage)
                    ?.initSchedulers()
                    ?.subscribe({ response ->
                        response?.genresSuggestionMovies?.takeIf { it.isNotEmpty() }?.let {
                            moviesExtraList.value = it
                            if (response.currentPage < response.totalPages) {
                                moviesPage++
                            } else {
                                isAllExtraMoviesFetched = true
                            }
                        }
                    }, {
                        isAllExtraMoviesFetched = true
                    })
            }
        }
    }

    fun refresh() {
        fetchMovies(true)
    }

    @SuppressLint("CheckResult")
    fun fetchMovies(suggestion: Suggestion) {
//        if(suggestion.id == -1) {
//            ApiServiceGenerator.getApiService
//                .getLastMovies(1)
//                ?.initSchedulers()
//                ?.subscribe({
//                    it?.movies?.let {
//                        moviesList.value = moviesList.value?.apply {
//                            this[0] = suggestion to it
//                        }
//                    }
//                },{
//
//                })
//        } else {
//            ApiServiceGenerator.getApiService
//                .getSuggestionMovies(1, suggestion.id)
//                ?.initSchedulers()
//                ?.subscribe({
//                    it?.movies?.let {
//                        moviesList.value = moviesList.value?.apply {
//                            this.add(suggestion to it)
//                        }
//                    }
//                }, {
//
//                })
//        }
    }
}
