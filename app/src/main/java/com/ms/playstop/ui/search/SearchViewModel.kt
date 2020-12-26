package com.ms.playstop.ui.search

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.extension.initSchedulers
import com.ms.playstop.model.Movie
import com.ms.playstop.network.base.ApiServiceGenerator
import com.ms.playstop.network.model.GeneralResponse

class SearchViewModel : ViewModel() {

    val searchResult : MutableLiveData<List<Movie>> = MutableLiveData()

    val searchError : MutableLiveData<GeneralResponse> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun searchMovie(query : String) {
        if(query.trim().isNotEmpty()) {
            ApiServiceGenerator.getApiService
                .searchMovie(query)
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
