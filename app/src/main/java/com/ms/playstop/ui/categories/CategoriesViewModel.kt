package com.ms.playstop.ui.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.playstop.model.Category
import com.ms.playstop.model.Genre
import com.ms.playstop.model.Suggestion
import com.ms.playstop.network.model.ConfigResponse
import com.orhanobut.hawk.Hawk

class CategoriesViewModel : ViewModel() {

    val categories = MutableLiveData<ArrayList<Category>>(arrayListOf())
    val genres = MutableLiveData<ArrayList<Genre>>(arrayListOf())
    val suggestions = MutableLiveData<ArrayList<Suggestion>>(arrayListOf())

    init {
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
            config?.suggestions?.let {
                suggestions.value = suggestions.value?.apply {
                    this.addAll(it)
                }
            }
        }
    }
}
