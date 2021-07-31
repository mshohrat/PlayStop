package com.ms.playstop.ui.search

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.model.Movie
import com.ms.playstop.model.SearchFilter
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.network.model.SearchMovieRequest

class SearchViewModel : ViewModel() {

    val searchResult : MutableLiveData<List<Movie>> = MutableLiveData()

    val searchError : MutableLiveData<GeneralResponse> = MutableLiveData()

    var searchFilter : SearchFilter? = null

    @SuppressLint("CheckResult")
    fun searchMovie(query : String) {
        if(query.trim().isNotEmpty()) {
            ApiServiceGenerator.getApiService
                .searchMovie(
                    SearchMovieRequest(
                        query,
                        searchFilter?.sort?.toString(),
                        searchFilter?.categories?.map { it.id },
                        searchFilter?.genres?.map { it.id },
                        searchFilter?.languages?.map { it.id },
                        searchFilter?.years?.map { it.value },
                        searchFilter?.countries?.map { it.id },
                        searchFilter?.minimumScore,
                        searchFilter?.maximumScore
                    )
                )
                ?.initSchedulers()
                ?.subscribe({
                    it?.movies?.let {
                        searchResult.value = it
                    } ?: kotlin.run {
                        searchResult.value = listOf()
                    }
                },{
                    searchError.value = GeneralResponse(it.message)
                })
        }
    }
}
