package com.ms.playstop.ui.categories

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.model.Category
import com.ms.playstop.model.Genre
import com.ms.playstop.model.Suggestion
import com.ms.playstop.model.Year
import com.ms.playstop.network.model.ConfigResponse
import com.orhanobut.hawk.Hawk

class CategoriesViewModel : ViewModel() {

    val categories = MutableLiveData<ArrayList<Category>>(arrayListOf())
    val genres = MutableLiveData<ArrayList<Genre>>(arrayListOf())
    val years = MutableLiveData<ArrayList<Year>>(arrayListOf())
    val loading = MutableLiveData<Boolean>(true)

    init {
        Handler().postDelayed({
            if(Hawk.contains(ConfigResponse.SAVE_KEY)) {
                val config = Hawk.get<ConfigResponse?>(ConfigResponse.SAVE_KEY)
                config?.categories?.let {
                    categories.value = categories.value?.apply {
                        this.addAll(it)
                    }
                }
                config?.genres?.let {
                    genres.value = genres.value?.apply {
                        this.addAll(it)
                    }
                }
                val yearsList = arrayListOf<Year>()
                val minYear = config?.minimumMovieYear ?: 0
                val maxYear = config?.maximumMovieYear ?: 0
                for (i in minYear .. maxYear) {
                    yearsList.add(Year(i))
                }
                yearsList.reverse()
                years.value = yearsList
            }
            loading.postValue(false)
        },1000)
    }
}
