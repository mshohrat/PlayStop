package com.ms.playstop.ui.search.filter

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.R
import com.ms.playstop.model.*
import com.ms.playstop.network.model.ConfigResponse
import com.orhanobut.hawk.Hawk

class SearchFilterViewModel : ViewModel() {

    var categories = emptyList<Category>()
    var genres = emptyList<Genre>()
    var countries = emptyList<Country>()
    var languages = emptyList<Language>()
    var years = arrayListOf<Year>()
    var sorts = arrayListOf<Sort>()
    var types = arrayListOf<MovieType>()
    lateinit var filter : SearchFilter
    var minimumScore = 1f
    var maximumScore = 10f

    init {
        if (Hawk.contains(ConfigResponse.SAVE_KEY)) {
            val configResponse = Hawk.get(ConfigResponse.SAVE_KEY) as? ConfigResponse
            configResponse?.let {
                this@SearchFilterViewModel.categories = it.categories ?: emptyList()
                this@SearchFilterViewModel.genres = it.genres ?: emptyList()
                this@SearchFilterViewModel.countries = it.countries ?: emptyList()
                this@SearchFilterViewModel.languages = it.languages ?: emptyList()
                for (i in it.minimumMovieYear..it.maximumMovieYear) {
                    this@SearchFilterViewModel.years.add(Year(i))
                }
            }
        }
    }

    fun initTypes(context: Context?) {
        context?.let { ctx ->
            this@SearchFilterViewModel.types.add(MovieType(Movie.TYPE_ALL,ctx.getString(R.string.movie_type_all)))
            this@SearchFilterViewModel.types.add(MovieType(Movie.TYPE_MOVIE,ctx.getString(R.string.type_movie)))
            this@SearchFilterViewModel.types.add(MovieType(Movie.TYPE_SERIES,ctx.getString(R.string.type_series)))
        }
    }

    fun initSorts(context: Context?) {
        context?.let { ctx ->
            this@SearchFilterViewModel.sorts.add(Sort(Movie.SORT_DEFAULT,ctx.getString(R.string.sort_default)))
            this@SearchFilterViewModel.sorts.add(Sort(Movie.SORT_NEWEST,ctx.getString(R.string.sort_newest)))
            this@SearchFilterViewModel.sorts.add(Sort(Movie.SORT_OLDEST,ctx.getString(R.string.sort_oldest)))
            this@SearchFilterViewModel.sorts.add(Sort(Movie.SORT_SCORE_IMDB,ctx.getString(R.string.sort_imdb_score)))
        }
    }

    fun updateFilter(filter: SearchFilter) {
        this.filter = filter
    }
}