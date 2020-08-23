package com.ms.playstop.ui.movieLists

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.model.Suggestion
import com.ms.playstop.model.SuggestionMovies
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.ConfigResponse
import com.ms.playstop.network.model.GetAllSuggestionsMoviesRequest
import com.orhanobut.hawk.Hawk
import java.util.concurrent.TimeUnit

class MovieListsViewModel : ViewModel() {

    val suggestions = MutableLiveData<ArrayList<Suggestion>>(arrayListOf(Suggestion(-1,"تازه‌ها")))

    val moviesList : MutableLiveData<List<SuggestionMovies>> = MutableLiveData(
        listOf()
    )

    init {
        if(Hawk.contains(ConfigResponse.SAVE_KEY)) {
            val config = Hawk.get<ConfigResponse?>(ConfigResponse.SAVE_KEY)
            config?.suggestions?.let {
                val list = ArrayList<Suggestion>()
                for (suggestion in it) {
                    suggestion?.let { s ->
                        list.add(s)
                    }
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
                ?.delay(3,TimeUnit.SECONDS)
                ?.initSchedulers()
                ?.subscribe({
                    it?.suggestionMovies?.let {
                        moviesList.value = it
                    } ?: kotlin.run {
                        //todo send error
                    }
                },{
                    //todo send error
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
