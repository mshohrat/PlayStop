package com.ms.playstop.ui.movieLists

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.R
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.model.Movie
import com.ms.playstop.model.SpecialMovies
import com.ms.playstop.model.Suggestion
import com.ms.playstop.model.SuggestionMovies
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.ConfigResponse
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.network.model.GetAllSuggestionsMoviesRequest
import com.orhanobut.hawk.Hawk
import java.util.concurrent.TimeUnit

class MovieListsViewModel : ViewModel() {

    val suggestions = MutableLiveData<ArrayList<Suggestion>>(arrayListOf())

    val moviesError  = MutableLiveData<GeneralResponse>()

    val moviesList : MutableLiveData<List<SuggestionMovies>> = MutableLiveData(
        listOf()
    )

    val specialMoviesList : MutableLiveData<ArrayList<Movie>> = MutableLiveData(
        arrayListOf()
    )

    init {
        if(Hawk.contains(ConfigResponse.SAVE_KEY)) {
            val config = Hawk.get<ConfigResponse?>(ConfigResponse.SAVE_KEY)
            config?.suggestions?.let {
                val list = ArrayList<Suggestion>()
                for (suggestion in it) {
                    list.add(suggestion)
                }
                suggestions.value = suggestions.value?.apply {
                    this.addAll(list)
                }
                suggestions.value?.let {
                    val l = ArrayList<SuggestionMovies>()
                    for (s in it) {
                        l.add(SuggestionMovies(s, listOf()))
                    }
                    moviesList.value = l
                }
            }
        }
        fetchMovies()
    }

    fun fetchMovies() {
        suggestions.value?.let {
            val suggestionIds = arrayListOf<Int>()
            for (suggestion in it){
                suggestionIds.add(suggestion.id)
            }
            ApiServiceGenerator.getApiService
                .getAllSuggestionsMovies(GetAllSuggestionsMoviesRequest(
                    suggestionIds
                ))
                ?.initSchedulers()
                ?.subscribe({ response ->
                    var specials : SuggestionMovies? = null
                    response?.specialMovies?.takeIf { it.movies.size > 5 }?.let {
                        specials = SuggestionMovies(Suggestion(-2,it.name),it.movies)
                    }
                    response?.suggestionMovies?.apply {
                        specials?.let { s ->
                            this.add(s)
                        }
                    }?.let {
                        moviesList.value = it
                    } ?: kotlin.run {
                        moviesError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                    }
                    response?.specialMovies?.movies?.takeIf { it.size > 5 }?.let {
                        specialMoviesList.value = ArrayList(it.take(5))
                    } ?: kotlin.run {
                        response?.specialMovies?.movies?.takeIf { it.isNotEmpty() }?.let {
                            specialMoviesList.value = it.apply { this.addAll(it) }
                        } ?: kotlin.run {

                        }
                    }
                },{
                    moviesError.value = GeneralResponse(messageResId = R.string.failed_in_communication_with_server)
                })
        }

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
