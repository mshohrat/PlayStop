package com.ms.playstop.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchFilter(
    var years: ArrayList<Year> = arrayListOf(),
    var categories: ArrayList<Category> = arrayListOf(),
    var genres: ArrayList<Genre> = arrayListOf(),
    var sort: Int = Movie.SORT_DEFAULT,
    var languages: ArrayList<Language> = arrayListOf(),
    var countries: ArrayList<Country> = arrayListOf(),
    var minimumScore: Float = 0f,
    var maximumScore: Float = 10f
): Parcelable {
    fun addYear(year: Year?) {
        year?.let {
            years.add(it)
        }
    }

    fun addYears(yearList: List<Year>?) {
        yearList.takeIf { it.isNullOrEmpty().not()}?.let {
            years.addAll(it)
        }
    }

    fun removeYear(year: Year?) {
        if(years.isNotEmpty() && years.contains(year)) {
            years.remove(year)
        }
    }

    fun removeYears(yearList: List<Year>?) {
        yearList.takeIf { it.isNullOrEmpty().not() && years.isNotEmpty() }?.let {
            years.removeAll(it)
        }
    }

    fun addCategory(category: Category?) {
        category?.let {
            categories.add(it)
        }
    }

    fun addCategories(categoryList: List<Category>?) {
        categoryList.takeIf { it.isNullOrEmpty().not() }?.let {
            categories.addAll(it)
        }
    }

    fun removeCategory(category: Category?) {
        if(categories.isNotEmpty() && categories.contains(category)) {
            categories.remove(category)
        }
    }

    fun removeCategories(categoryList: List<Category>?) {
        categoryList.takeIf { it.isNullOrEmpty().not() && categories.isNotEmpty() }?.let {
            categories.removeAll(it)
        }
    }

    fun addGenre(genre: Genre?) {
        genre?.let {
            genres.add(it)
        }
    }

    fun addGenres(genreList: List<Genre>?) {
        genreList.takeIf { it.isNullOrEmpty().not() }?.let {
            genres.addAll(it)
        }
    }

    fun removeGenre(genre: Genre?) {
        if(genres.isNotEmpty() && genres.contains(genre)) {
            genres.remove(genre)
        }
    }

    fun removeGenres(genreList: List<Genre>?) {
        genreList.takeIf { it.isNullOrEmpty().not() && genres.isNotEmpty() }?.let {
            genres.removeAll(it)
        }
    }

    fun addLanguage(language: Language?) {
        language?.let {
            languages.add(it)
        }
    }

    fun addLanguages(languageList: List<Language>?) {
        languageList.takeIf { it.isNullOrEmpty().not() }?.let {
            languages.addAll(it)
        }
    }

    fun removeLanguage(language: Language?) {
        if(languages.isNotEmpty() && languages.contains(language)) {
            languages.remove(language)
        }
    }

    fun removeLanguages(languageList: List<Language>?) {
        languageList.takeIf { it.isNullOrEmpty().not() && languages.isNotEmpty() }?.let {
            languages.removeAll(it)
        }
    }

    fun addCountry(country: Country?) {
        country?.let {
            countries.add(it)
        }
    }

    fun addCountries(countryList: List<Country>?) {
        countryList.takeIf { it.isNullOrEmpty().not() }?.let {
            countries.addAll(it)
        }
    }

    fun removeCountry(country: Country?) {
        if(countries.isNotEmpty() && countries.contains(country)) {
            countries.remove(country)
        }
    }

    fun removeCountries(countryList: List<Country>?) {
        countryList.takeIf { it.isNullOrEmpty().not() && countries.isNotEmpty() }?.let {
            countries.removeAll(it)
        }
    }

    fun setMinimumScoreValue(score: Float) {
        if(score <= maximumScore) {
            minimumScore = score
        } else {
            minimumScore = score
            maximumScore = score
        }
    }

    fun setMaximumScoreValue(score: Float) {
        if(score >= minimumScore) {
            maximumScore = score
        } else {
            minimumScore = score
            maximumScore = score
        }
    }

    fun getYearsValues(): List<Int> {
        return years.map { it.value }
    }

    fun getCategoriesIds(): List<Int> {
        return categories.map { it.id }
    }

    fun getGenresIds(): List<Int> {
        return genres.map { it.id }
    }

    fun getLanguagesIds(): List<Int> {
        return languages.map { it.id }
    }

    fun getCountriesIds(): List<Int> {
        return countries.map { it.id }
    }
}