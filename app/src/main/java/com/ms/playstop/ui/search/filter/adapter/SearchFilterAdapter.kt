package com.ms.playstop.ui.search.filter.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider
import com.ms.playstop.R
import com.ms.playstop.extension.convertToPersianNumber
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.show
import com.ms.playstop.model.*
import com.ms.playstop.utils.DayNightModeAwareAdapter
import com.ms.playstop.utils.DayNightModeAwareViewHolder
import kotlinx.android.synthetic.main.item_search_filter_checkable.view.*
import kotlinx.android.synthetic.main.item_search_filter_header.view.*
import kotlinx.android.synthetic.main.item_search_filter_slider.view.*

class SearchFilterAdapter(
    private val context: Context,
    private val categories : List<Category>?,
    private val genres : List<Genre>?,
    private val sorts : List<Sort>?,
    private val languages : List<Language>?,
    private val years : List<Year>?,
    private val countries : List<Country>?,
    private val types : List<MovieType>?,
    private var searchFilter: SearchFilter,
    private val listener: OnDataChangedListener? = null
) : RecyclerView.Adapter<SearchFilterAdapter.ViewHolder>(), DayNightModeAwareAdapter {

    private val FILTER_TYPES_TITLE = R.string.filter_types_title
    private val FILTER_CATEGORIES_TITLE = R.string.filter_categories_title
    private val FILTER_GENRES_TITLE = R.string.filter_genres_title
    private val FILTER_SORT_TITLE = R.string.filter_sort_title
    private val FILTER_LANGUAGES_TITLE = R.string.filter_languages_title
    private val FILTER_YEARS_TITLE = R.string.filter_years_title
    private val FILTER_SCORE_TITLE = R.string.filter_score_title
    private val FILTER_COUNTRIES_TITLE = R.string.filter_countries_title

    private val dataList : MutableList<Pair<Int,FilterItem>> = mutableListOf()

    private var recyclerView: RecyclerView? = null
    private val boundViewHolders = mutableListOf<ViewHolder>()

    companion object {
        const val TYPE_HEADER = 200
        const val TYPE_ITEM_CHECKBOX = 210
        const val TYPE_ITEM_RADIO = 211
        const val TYPE_ITEM_SLIDER = 212
    }

    init {
        var selectedTypePos = arrayListOf<FilterItem>()
        types.takeIf { it.isNullOrEmpty().not() }?.let {
            for(i in it.indices) {
                if(it[i].type == Movie.TYPE_ALL) {
                    selectedTypePos = arrayListOf(it[i])
                    break
                }
            }
            dataList.add(TYPE_HEADER to SearchFilterHeaderListItem(context.getString(FILTER_TYPES_TITLE),ArrayList(it),selectedTypePos,false))
        }
        categories.takeIf { it.isNullOrEmpty().not() }?.let {
            dataList.add(TYPE_HEADER to SearchFilterHeaderListItem(context.getString(FILTER_CATEGORIES_TITLE),ArrayList(it),arrayListOf()))
        }
        genres.takeIf { it.isNullOrEmpty().not() }?.let {
            dataList.add(TYPE_HEADER to SearchFilterHeaderListItem(context.getString(FILTER_GENRES_TITLE),ArrayList(it),arrayListOf()))
        }
        var selectedSortPos = arrayListOf<FilterItem>()
        sorts.takeIf { it.isNullOrEmpty().not() }?.let {
            for(i in it.indices) {
                if(it[i].type == Movie.SORT_DEFAULT) {
                    selectedSortPos = arrayListOf(it[i])
                    break
                }
            }
            dataList.add(TYPE_HEADER to SearchFilterHeaderListItem(context.getString(FILTER_SORT_TITLE),ArrayList(it),selectedSortPos,false))
        }
        languages.takeIf { it.isNullOrEmpty().not() }?.let {
            dataList.add(TYPE_HEADER to SearchFilterHeaderListItem(context.getString(FILTER_LANGUAGES_TITLE),ArrayList(it),arrayListOf()))
        }
        years.takeIf { it.isNullOrEmpty().not() }?.let {
            dataList.add(TYPE_HEADER to SearchFilterHeaderListItem(context.getString(FILTER_YEARS_TITLE),ArrayList(it),arrayListOf()))
        }
        dataList.add(TYPE_HEADER to SearchFilterHeaderSliderItem(context.getString(FILTER_SCORE_TITLE), 0f to 10f,0f to 10f))
        countries.takeIf { it.isNullOrEmpty().not() }?.let {
            dataList.add(TYPE_HEADER to SearchFilterHeaderListItem(context.getString(FILTER_COUNTRIES_TITLE),ArrayList(it),arrayListOf()))
        }
        if((searchFilter == SearchFilter()).not()) {
            updateDataBasedOnSearchFilter()
            notifyDataSetChanged()
        }
    }

    fun clear() {
        searchFilter = SearchFilter()
        updateDataBasedOnSearchFilter()
        notifyDataSetChanged()
        notifyListener()
    }

    fun updateDataBasedOnSearchFilter() {
//        val removingChildren = hashMapOf<Int,FilterItem>()
//        for (item in dataList) {
//            if(item.first == TYPE_HEADER) {
//                removingChildren[dataList.indexOf(item)] = item.second
//            } else if (item.first == TYPE_ITEM_CHECKBOX || item.first == TYPE_ITEM_RADIO) {
//                item.second.isSelected = false
//            } else if (item.first == TYPE_ITEM_SLIDER) {
//                (item.second as SearchFilterSliderChild).selectedValues = searchFilter.minimumScore to searchFilter.maximumScore
//            }
//        }
        val newData = arrayListOf<Pair<Int,FilterItem>>()
        for (item in dataList) {
            if(item.first == TYPE_HEADER) {
                when(item.second) {
                    is SearchFilterHeaderListItem -> {
                        if((item.second as SearchFilterHeaderListItem).children.isNotEmpty()) {
                            val hasMultipleChoices = (item.second as SearchFilterHeaderListItem).hasMultipleChoices
                            when((item.second as SearchFilterHeaderListItem).children[0]) {
                                is Category -> {
                                    newData.add(TYPE_HEADER to SearchFilterHeaderListItem(
                                        context.getString(FILTER_CATEGORIES_TITLE),
                                        ArrayList(categories ?: arrayListOf()),
                                        ArrayList(searchFilter.categories),
                                        isSelected = item.second.isSelected))
                                    if(item.second.isSelected) {
                                        newData.addAll(ArrayList(categories ?: arrayListOf()).map {
                                            (if(hasMultipleChoices) TYPE_ITEM_CHECKBOX else TYPE_ITEM_RADIO) to (it.apply {
                                                isSelected = (item.second as SearchFilterHeaderListItem).selectedPositions.map { it.getFilterId() }.contains(it.getFilterId())
                                            })
                                        })
                                    }
                                }
                                is Genre -> {
                                    newData.add(TYPE_HEADER to SearchFilterHeaderListItem(
                                        context.getString(FILTER_GENRES_TITLE),
                                        ArrayList(genres ?: arrayListOf()),
                                        ArrayList(searchFilter.genres),
                                        isSelected = item.second.isSelected))
                                    if(item.second.isSelected) {
                                        newData.addAll(ArrayList(genres ?: arrayListOf()).map {
                                            (if(hasMultipleChoices) TYPE_ITEM_CHECKBOX else TYPE_ITEM_RADIO) to (it.apply {
                                                isSelected = (item.second as SearchFilterHeaderListItem).selectedPositions.map { it.getFilterId() }.contains(it.getFilterId())
                                            })
                                        })
                                    }
                                }
                                is Sort -> {
                                    newData.add(TYPE_HEADER to SearchFilterHeaderListItem(
                                        context.getString(FILTER_SORT_TITLE),
                                        ArrayList(sorts ?: arrayListOf()),
                                        ArrayList((sorts ?: arrayListOf()).filter { it.type == searchFilter.sort }),
                                        isSelected = item.second.isSelected))
                                    if(item.second.isSelected) {
                                        newData.addAll(ArrayList(sorts ?: arrayListOf()).map {
                                            (if(hasMultipleChoices) TYPE_ITEM_CHECKBOX else TYPE_ITEM_RADIO) to (it.apply {
                                                isSelected = (item.second as SearchFilterHeaderListItem).selectedPositions.map { it.getFilterId() }.contains(it.getFilterId())
                                            })
                                        })
                                    }
                                }
                                is MovieType -> {
                                    newData.add(TYPE_HEADER to SearchFilterHeaderListItem(
                                        context.getString(FILTER_TYPES_TITLE),
                                        ArrayList(types ?: arrayListOf()),
                                        ArrayList((types ?: arrayListOf()).filter { it.type == searchFilter.type }),
                                        isSelected = item.second.isSelected))
                                    if(item.second.isSelected) {
                                        newData.addAll(ArrayList(types ?: arrayListOf()).map {
                                            (if(hasMultipleChoices) TYPE_ITEM_CHECKBOX else TYPE_ITEM_RADIO) to (it.apply {
                                                isSelected = (item.second as SearchFilterHeaderListItem).selectedPositions.map { it.getFilterId() }.contains(it.getFilterId())
                                            })
                                        })
                                    }
                                }
                                is Language -> {
                                    newData.add(TYPE_HEADER to SearchFilterHeaderListItem(
                                        context.getString(FILTER_LANGUAGES_TITLE),
                                        ArrayList(languages ?: arrayListOf()),
                                        ArrayList(searchFilter.languages),
                                        isSelected = item.second.isSelected))
                                    if(item.second.isSelected) {
                                        newData.addAll(ArrayList(languages ?: arrayListOf()).map {
                                            (if(hasMultipleChoices) TYPE_ITEM_CHECKBOX else TYPE_ITEM_RADIO) to (it.apply {
                                                isSelected = (item.second as SearchFilterHeaderListItem).selectedPositions.map { it.getFilterId() }.contains(it.getFilterId())
                                            })
                                        })
                                    }
                                }
                                is Year -> {
                                    newData.add(TYPE_HEADER to SearchFilterHeaderListItem(
                                        context.getString(FILTER_YEARS_TITLE),
                                        ArrayList(years ?: arrayListOf()),
                                        ArrayList(searchFilter.years),
                                        isSelected = item.second.isSelected))
                                    if(item.second.isSelected) {
                                        newData.addAll(ArrayList(years ?: arrayListOf()).map {
                                            (if(hasMultipleChoices) TYPE_ITEM_CHECKBOX else TYPE_ITEM_RADIO) to (it.apply {
                                                isSelected = (item.second as SearchFilterHeaderListItem).selectedPositions.map { it.getFilterId() }.contains(it.getFilterId())
                                            })
                                        })
                                    }
                                }
                                is Country -> {
                                    newData.add(TYPE_HEADER to SearchFilterHeaderListItem(
                                        context.getString(FILTER_COUNTRIES_TITLE),
                                        ArrayList(countries ?: arrayListOf()),
                                        ArrayList(searchFilter.countries),
                                        isSelected = item.second.isSelected))
                                    if(item.second.isSelected) {
                                        newData.addAll(ArrayList(countries ?: arrayListOf()).map {
                                            (if(hasMultipleChoices) TYPE_ITEM_CHECKBOX else TYPE_ITEM_RADIO) to (it.apply {
                                                isSelected = (item.second as SearchFilterHeaderListItem).selectedPositions.map { it.getFilterId() }.contains(it.getFilterId())
                                            })
                                        })
                                    }
                                }
                            }
                        }
                    }
                    is SearchFilterHeaderSliderItem -> {
                        newData.add(TYPE_HEADER to SearchFilterHeaderSliderItem(
                            context.getString(FILTER_SCORE_TITLE),
                            0f to 10f,
                            searchFilter.minimumScore to searchFilter.maximumScore,
                            isSelected = item.second.isSelected))
                        if(item.second.isSelected) {
                            newData.add(TYPE_ITEM_SLIDER to SearchFilterSliderChild(
                                0f to 10f,
                                searchFilter.minimumScore to searchFilter.maximumScore
                            ))
                        }
                    }
                    else -> {}
                }
            }
        }
        dataList.clear()
        dataList.addAll(newData)
//        for (removedItem in removingChildren.entries) {
//            if(removedItem.value is SearchFilterHeaderListItem && (removedItem.value as SearchFilterHeaderListItem).children.isNotEmpty()){
//                when {
//                    (removedItem.value as SearchFilterHeaderListItem).children[0] is Category -> {
//                        dataList[removedItem.key] = TYPE_HEADER to SearchFilterHeaderListItem(context.getString(FILTER_CATEGORIES_TITLE),ArrayList(categories ?: arrayListOf()),ArrayList(searchFilter.categories),isSelected = removedItem.value.isSelected)
//                    }
//                    (removedItem.value as SearchFilterHeaderListItem).children[0] is Genre -> {
//                        dataList[removedItem.key] = TYPE_HEADER to SearchFilterHeaderListItem(context.getString(FILTER_GENRES_TITLE),ArrayList(genres ?: arrayListOf()),ArrayList(searchFilter.genres),isSelected = removedItem.value.isSelected)
//                    }
//                    (removedItem.value as SearchFilterHeaderListItem).children[0] is Sort -> {
//                        var selectedSortPos = arrayListOf<FilterItem>()
//                        sorts.takeIf { it.isNullOrEmpty().not() }?.let {
//                            for(i in it.indices) {
//                                if(it[i].type == searchFilter.sort) {
//                                    selectedSortPos = arrayListOf(it[i])
//                                    break
//                                }
//                            }
//                        }
//                        dataList[removedItem.key] = TYPE_HEADER to SearchFilterHeaderListItem(context.getString(FILTER_SORT_TITLE),ArrayList(sorts ?: arrayListOf()),selectedSortPos,false,isSelected = removedItem.value.isSelected)
//                    }
//                    (removedItem.value as SearchFilterHeaderListItem).children[0] is Language -> {
//                        dataList[removedItem.key] = TYPE_HEADER to SearchFilterHeaderListItem(context.getString(FILTER_LANGUAGES_TITLE),ArrayList(languages ?: arrayListOf()),ArrayList(searchFilter.languages),isSelected = removedItem.value.isSelected)
//                    }
//                    (removedItem.value as SearchFilterHeaderListItem).children[0] is Year -> {
//                        dataList[removedItem.key] = TYPE_HEADER to SearchFilterHeaderListItem(context.getString(FILTER_YEARS_TITLE),ArrayList(years ?: arrayListOf()),ArrayList(searchFilter.years),isSelected = removedItem.value.isSelected)
//                    }
//                    (removedItem.value as SearchFilterHeaderListItem).children[0] is Country -> {
//                        dataList[removedItem.key] = TYPE_HEADER to SearchFilterHeaderListItem(context.getString(FILTER_COUNTRIES_TITLE),ArrayList(countries ?: arrayListOf()),ArrayList(searchFilter.countries),isSelected = removedItem.value.isSelected)
//                    }
//
//                }
//            }
//            else if(removedItem.value is SearchFilterHeaderSliderItem){
//                dataList[removedItem.key] = TYPE_HEADER to SearchFilterHeaderSliderItem(context.getString(FILTER_SCORE_TITLE),0f to 10f,searchFilter.minimumScore to searchFilter.maximumScore,isSelected = removedItem.value.isSelected)
//            }
//
//        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
        this.boundViewHolders.clear()
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        boundViewHolders.takeIf { it.contains(holder) }?.remove(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            TYPE_HEADER -> {
                HeaderItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_filter_header,parent,false))
            }
            TYPE_ITEM_SLIDER -> {
                SliderItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_filter_slider,parent,false))
            }
            TYPE_ITEM_RADIO -> {
                RadioListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_filter_checkable,parent,false))
            }
            TYPE_ITEM_CHECKBOX -> {
                CheckListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_filter_checkable,parent,false))
            }
            else -> {
                ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_filter_header,parent,false))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = dataList[position]
        return when(item.first) {
            TYPE_HEADER -> TYPE_HEADER
            else -> {
                when(item.second) {
                    is Sort -> TYPE_ITEM_RADIO
                    is MovieType -> TYPE_ITEM_RADIO
                    is SearchFilterSliderChild -> TYPE_ITEM_SLIDER
                    else -> TYPE_ITEM_CHECKBOX
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(position,item)
        boundViewHolders.takeIf { it.contains(holder).not() }?.add(holder)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    open class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), DayNightModeAwareViewHolder {
        val rootView  = itemView
        open fun bind(position: Int,item: Pair<Int,FilterItem>) {}
        override fun onDayNightModeChanged(type: Int) {

        }
    }

    inner class HeaderItemViewHolder(itemView: View) : ViewHolder(itemView) {
        val titleTv = itemView.item_search_filter_header_title_tv
        var arrowIv = itemView.item_search_filter_header_arrow_iv
        override fun bind(position: Int, item: Pair<Int,FilterItem>) {
            when(item.second) {
                is SearchFilterHeaderSliderItem -> {
                    val sliderHeaderItem = item.second as SearchFilterHeaderSliderItem
                    titleTv?.text = sliderHeaderItem.getFilterName()
                    if(sliderHeaderItem.isSelected) {
                        arrowIv?.rotation = 90f
                    } else {
                        arrowIv?.rotation = 270f
                    }
                    rootView.setOnClickListener {
                        if(sliderHeaderItem.isSelected) {
                            updateData(item.apply { second.isSelected = false },false)
                            removeDataFromListAndUpdateItem(item,position + 1,position + 1)
                        } else {
                            updateData(item.apply { second.isSelected = true },false)
                            val newData = TYPE_ITEM_SLIDER to SearchFilterSliderChild(sliderHeaderItem.baseValues,sliderHeaderItem.selectedValues)
                            addData(position + 1, arrayListOf(newData))
                        }
                    }
                }
                else -> {
                    val headerListItem = item.second as SearchFilterHeaderListItem
                    titleTv?.text = headerListItem.getFilterName()
                    if(headerListItem.isSelected) {
                        arrowIv?.rotation = 90f
                    } else {
                        arrowIv?.rotation = 270f
                    }
                    rootView.setOnClickListener {
                        if(headerListItem.isSelected) {
                            updateData(item.apply { second.isSelected = false },false)
                            removeDataFromListAndUpdateItem(item,position + 1,position + headerListItem.children.size)
                        } else {
                            updateData(item.apply { second.isSelected = true },false)
                            val newData = arrayListOf<Pair<Int,FilterItem>>()
                            for (filter in headerListItem.children){
                                newData.add((if(headerListItem.hasMultipleChoices) TYPE_ITEM_CHECKBOX else TYPE_ITEM_RADIO) to (filter.apply { isSelected = headerListItem.selectedPositions.map { it.getFilterId() }.contains(filter.getFilterId()) }))
                            }
                            addData(position + 1,newData)
                        }
                    }
                }
            }
        }

        override fun onDayNightModeChanged(type: Int) {
            rootView.context?.let { ctx ->
                (rootView as MaterialCardView).setCardBackgroundColor(ContextCompat.getColor(ctx,R.color.colorPrimaryDark))
                with(ContextCompat.getColor(ctx,R.color.white)) {
                    (rootView as MaterialCardView).setStrokeColor(ColorStateList.valueOf(this))
                    arrowIv?.let { iv ->
                        ImageViewCompat.setImageTintList(iv,ColorStateList.valueOf(this))
                    }
                    titleTv?.setTextColor(this)
                }
            }
        }
    }

    private fun removeDataFromListAndUpdateItem(
        item: Pair<Int, FilterItem>,
        firstPosition: Int,
        secondPosition: Int
    ) {
        if(item.second is SearchFilterHeaderListItem) {
            if(dataList.isNotEmpty()) {
                val removingItems = arrayListOf<Pair<Int,FilterItem>>()
                for (i in dataList.indices) {
                    if(i in firstPosition..secondPosition) {
                        removingItems.add(dataList[i])
                    }
                }
                if(removingItems.isNotEmpty()) {
                    (item.second as SearchFilterHeaderListItem).selectedPositions = ArrayList(removingItems.map { it.second }.filter { it.isSelected })
                    updateData(item,false)
                    dataList.removeAll(removingItems)
                    notifyDataSetChanged()
                }
            }
        } else if(item.second is SearchFilterHeaderSliderItem) {
            if(dataList.isNotEmpty()) {
                val removingItems = arrayListOf<Pair<Int,FilterItem>>()
                for (i in dataList.indices) {
                    if(i in firstPosition..secondPosition) {
                        removingItems.add(dataList[i])
                    }
                }
                if(removingItems.isNotEmpty()) {
                    val removedItem = dataList[firstPosition]
                    val parentItem = item.second as SearchFilterHeaderSliderItem
                    if(removedItem.second is SearchFilterSliderChild) {
                        val removedFilter = removedItem.second as SearchFilterSliderChild
                        parentItem.baseValues = removedFilter.baseValues
                        parentItem.selectedValues = removedFilter.selectedValues
                    }
                    dataList[dataList.indexOf(item)] = item.first to parentItem
                    dataList.removeAll(removingItems)
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun addData(position: Int, items: ArrayList<Pair<Int, FilterItem>>) {
        if(dataList.size >= position) {
            dataList.addAll(position,items)
            notifyDataSetChanged()
        }
    }

    inner class SliderItemViewHolder(itemView: View) : ViewHolder(itemView) {
        val slider = itemView.item_search_filter_slider
        var sliderToValueTv = itemView.item_search_filter_slider_to_value_tv
        var sliderFromValueTv = itemView.item_search_filter_slider_from_value_tv
        override fun bind(position: Int, item: Pair<Int,FilterItem>) {
            slider?.valueFrom = (item.second as SearchFilterSliderChild).baseValues.first
            slider?.valueTo = (item.second as SearchFilterSliderChild).baseValues.second
            slider?.setValues((item.second as SearchFilterSliderChild).selectedValues.first,(item.second as SearchFilterSliderChild).selectedValues.second)
            sliderFromValueTv?.text = (item.second as SearchFilterSliderChild).selectedValues.first.toString()
            sliderToValueTv?.text = (item.second as SearchFilterSliderChild).selectedValues.second.toString()
            slider?.stepSize = 0.1f
            slider?.setLabelFormatter(LabelFormatter {
                return@LabelFormatter it.toString().convertToPersianNumber()
            })
            slider?.addOnChangeListener(RangeSlider.OnChangeListener { slider, value, fromUser ->
                if(fromUser) {
                    val minValue = slider.values.firstOrNull() ?: 0f
                    val maxValue = if(slider.values.size > 1) slider.values[1] else 10f
                    (item.second as SearchFilterSliderChild).selectedValues = minValue to maxValue
                    sliderFromValueTv?.text = minValue.toString()
                    sliderToValueTv?.text = maxValue.toString()
                    updateData(item,false)
                }
            })
        }

        override fun onDayNightModeChanged(type: Int) {
            rootView.context?.let { ctx ->
                rootView.setBackgroundColor(ContextCompat.getColor(ctx,R.color.colorPrimaryLightDark))
                with(ContextCompat.getColor(ctx,R.color.white)){
                    sliderFromValueTv?.setTextColor(this)
                    sliderToValueTv?.setTextColor(this)
                }
                slider?.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.colorAccent))
                slider?.trackInactiveTintList = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.colorAccentOpacity20))
                slider?.trackActiveTintList = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.colorAccentDark))
            }
        }
    }

    inner class CheckListItemViewHolder(itemView: View) : ViewHolder(itemView) {
        val nameTv = itemView.item_search_filter_checkable_name_tv
        val checkBox = itemView.item_search_filter_checkable_checkbox
        val radiobtn = itemView.item_search_filter_checkable_radio
        val divider = itemView.item_search_filter_checkable_divider
        override fun bind(position: Int, item: Pair<Int,FilterItem>) {
            if(position > 0 && dataList[position - 1].first == TYPE_HEADER) {
                divider?.hide()
            } else {
                divider?.show()
            }
            radiobtn?.hide()
            checkBox?.show()
            checkBox?.isChecked = item.second.isSelected
            nameTv?.text = item.second.getFilterName()
            rootView.setOnClickListener {
                checkBox?.let { cb ->
                    cb.isChecked = !cb.isChecked
                }
                updateData(item.apply { second.isSelected = !second.isSelected },false)
            }

        }

        override fun onDayNightModeChanged(type: Int) {
            rootView.context?.let { ctx ->
                (rootView as MaterialCardView).setCardBackgroundColor(ContextCompat.getColor(ctx,R.color.colorPrimaryLightDark))
                with(ContextCompat.getColor(ctx,R.color.colorAccent)){
                    checkBox?.let { cb ->
                        CompoundButtonCompat.setButtonTintList(cb,ColorStateList.valueOf(this))
                    }
                    radiobtn?.let { rb ->
                        CompoundButtonCompat.setButtonTintList(rb,ColorStateList.valueOf(this))
                    }
                    nameTv?.setTextColor(ContextCompat.getColor(ctx,R.color.grayLight))
                    divider?.setBackgroundColor(this)
                }
            }
        }
    }

    inner class RadioListItemViewHolder(itemView: View) : ViewHolder(itemView) {
        val nameTv = itemView.item_search_filter_checkable_name_tv
        val checkBox = itemView.item_search_filter_checkable_checkbox
        val radiobtn = itemView.item_search_filter_checkable_radio
        val divider = itemView.item_search_filter_checkable_divider
        override fun bind(position: Int, item: Pair<Int,FilterItem>) {
            if(position > 0 && dataList[position - 1].first == TYPE_HEADER) {
                divider?.hide()
            } else {
                divider?.show()
            }
            checkBox?.hide()
            radiobtn?.show()
            radiobtn?.isChecked = item.second.isSelected
            nameTv?.text = item.second.getFilterName()
            rootView.setOnClickListener {
                radiobtn?.takeIf { it.isChecked.not() }?.let { rb ->
                    rb.isChecked = true
                    uncheckOtherRadioButtons(position,item.second)
                    updateData(item.apply { second.isSelected = true },true)
                }
            }

        }

        private fun uncheckOtherRadioButtons(position: Int,filterItem: FilterItem) {
            when(filterItem) {
                is Sort -> {
                    dataList.filter { it.second is Sort }.forEach {
                        if(dataList.indexOf(it) != position) {
                            it.second.isSelected = false
                        }
                    }
                }
                is MovieType -> {
                    dataList.filter { it.second is MovieType }.forEach {
                        if(dataList.indexOf(it) != position) {
                            it.second.isSelected = false
                        }
                    }
                }
            }
        }

        override fun onDayNightModeChanged(type: Int) {
            rootView.context?.let { ctx ->
                (rootView as MaterialCardView).setCardBackgroundColor(ContextCompat.getColor(ctx,R.color.colorPrimaryLightDark))
                with(ContextCompat.getColor(ctx,R.color.colorAccent)){
                    checkBox?.let { cb ->
                        CompoundButtonCompat.setButtonTintList(cb,ColorStateList.valueOf(this))
                    }
                    radiobtn?.let { rb ->
                        CompoundButtonCompat.setButtonTintList(rb,ColorStateList.valueOf(this))
                    }
                    nameTv?.setTextColor(ContextCompat.getColor(ctx,R.color.grayLight))
                    divider?.setBackgroundColor(this)
                }
            }
        }
    }

    private fun updateData(item: Pair<Int,FilterItem>, notify: Boolean = true) {
        if(dataList.contains(item)) {
            val position = dataList.indexOf(item)
            dataList[position] = item
            if (notify) {
                notifyDataSetChanged()
            }
            notifyListener(position)
        }
    }

    interface OnDataChangedListener {
        fun onDataChanged(filter: SearchFilter)
    }

    private fun notifyListener(position: Int = -1) {
        listener?.let { listener ->
            if(dataList.isNotEmpty()) {
                if(position >= 0 && dataList.size > position) {
                    val filter = dataList[position].second
                    updateSearchFilterBasedOnFilter(filter)
                } else {
                    for (data in dataList) {
                        val filter = data.second
                        updateSearchFilterBasedOnFilter(filter)
                    }
                }
                listener.onDataChanged(searchFilter)
            } else {
                searchFilter = SearchFilter()
                listener.onDataChanged(searchFilter)
            }
        }
    }

    private fun updateSearchFilterBasedOnFilter(filter: FilterItem) {
        when(filter) {
            is Category -> {
                if(filter.isSelected) {
                    if(searchFilter.categories.map { it.getFilterId() }.contains(filter.getFilterId()).not()) {
                        searchFilter.categories.add(filter)
                    }
                } else {
                    if(searchFilter.categories.map { it.getFilterId() }.contains(filter.getFilterId())) {
                        searchFilter.categories.removeAt(searchFilter.categories.map { it.getFilterId() }.indexOf(filter.getFilterId()))
                    }
                }
            }
            is Genre -> {
                if(filter.isSelected) {
                    if(searchFilter.genres.map { it.getFilterId() }.contains(filter.getFilterId()).not()) {
                        searchFilter.genres.add(filter)
                    }
                } else {
                    if(searchFilter.genres.map { it.getFilterId() }.contains(filter.getFilterId())) {
                        searchFilter.genres.removeAt(searchFilter.genres.map { it.getFilterId() }.indexOf(filter.getFilterId()))
                    }
                }
            }
            is Year -> {
                if(filter.isSelected) {
                    if(searchFilter.years.map { it.getFilterId() }.contains(filter.getFilterId()).not()) {
                        searchFilter.years.add(filter)
                    }
                } else {
                    if(searchFilter.years.map { it.getFilterId() }.contains(filter.getFilterId())) {
                        searchFilter.years.removeAt(searchFilter.years.map { it.getFilterId() }.indexOf(filter.getFilterId()))
                    }
                }
            }
            is Language -> {
                if(filter.isSelected) {
                    if(searchFilter.languages.map { it.getFilterId() }.contains(filter.getFilterId()).not()) {
                        searchFilter.languages.add(filter)
                    }
                } else {
                    if(searchFilter.languages.map { it.getFilterId() }.contains(filter.getFilterId())) {
                        searchFilter.languages.removeAt(searchFilter.languages.map { it.getFilterId() }.indexOf(filter.getFilterId()))
                    }
                }
            }
            is Country -> {
                if(filter.isSelected) {
                    if(searchFilter.countries.map { it.getFilterId() }.contains(filter.getFilterId()).not()) {
                        searchFilter.countries.add(filter)
                    }
                } else {
                    if(searchFilter.countries.map { it.getFilterId() }.contains(filter.getFilterId())) {
                        searchFilter.countries.removeAt(searchFilter.countries.map { it.getFilterId() }.indexOf(filter.getFilterId()))
                    }
                }
            }
            is Sort -> {
                if(filter.isSelected) {
                    if(searchFilter.sort != filter.type) {
                        searchFilter.sort = filter.type
                    }
                }
            }
            is MovieType -> {
                if(filter.isSelected) {
                    if(searchFilter.type != filter.type) {
                        searchFilter.type = filter.type
                    }
                }
            }
            is SearchFilterSliderChild -> {
                searchFilter.minimumScore = filter.selectedValues.first
                searchFilter.maximumScore = filter.selectedValues.second
            }
            is SearchFilterHeaderSliderItem -> {
                searchFilter.minimumScore = filter.selectedValues.first
                searchFilter.maximumScore = filter.selectedValues.second
            }
            is SearchFilterHeaderListItem -> {
                if(filter.isSelected.not()) {
                    for (i in filter.children) {
                        if (filter.selectedPositions.map { it.getFilterId() }.contains(i.getFilterId())) {
                            when (i) {
                                is Category -> {
                                    if (i.isSelected) {
                                        if (searchFilter.categories.map { it.getFilterId() }.contains(i.getFilterId()).not()) {
                                            searchFilter.categories.add(i)
                                        }
                                    } else {
                                        if (searchFilter.categories.map { it.getFilterId() }.contains(i.getFilterId())) {
                                            searchFilter.categories.removeAt(searchFilter.categories.map { it.getFilterId() }.indexOf(i.getFilterId()))
                                        }
                                    }
                                }
                                is Genre -> {
                                    if (i.isSelected) {
                                        if (searchFilter.genres.map { it.getFilterId() }.contains(i.getFilterId()).not()) {
                                            searchFilter.genres.add(i)
                                        }
                                    } else {
                                        if (searchFilter.genres.map { it.getFilterId() }.contains(i.getFilterId())) {
                                            searchFilter.genres.removeAt(searchFilter.genres.map { it.getFilterId() }.indexOf(i.getFilterId()))
                                        }
                                    }
                                }
                                is Year -> {
                                    if (i.isSelected) {
                                        if (searchFilter.years.map { it.getFilterId() }.contains(i.getFilterId()).not()) {
                                            searchFilter.years.add(i)
                                        }
                                    } else {
                                        if (searchFilter.years.map { it.getFilterId() }.contains(i.getFilterId())) {
                                            searchFilter.years.removeAt(searchFilter.years.map { it.getFilterId() }.indexOf(i.getFilterId()))
                                        }
                                    }
                                }
                                is Language -> {
                                    if (i.isSelected) {
                                        if (searchFilter.languages.map { it.getFilterId() }.contains(i.getFilterId()).not()) {
                                            searchFilter.languages.add(i)
                                        }
                                    } else {
                                        if (searchFilter.languages.map { it.getFilterId() }.contains(i.getFilterId())) {
                                            searchFilter.languages.removeAt(searchFilter.languages.map { it.getFilterId() }.indexOf(i.getFilterId()))
                                        }
                                    }
                                }
                                is Country -> {
                                    if (i.isSelected) {
                                        if (searchFilter.countries.map { it.getFilterId() }.contains(i.getFilterId()).not()) {
                                            searchFilter.countries.add(i)
                                        }
                                    } else {
                                        if (searchFilter.countries.map { it.getFilterId() }.contains(i.getFilterId())) {
                                            searchFilter.countries.removeAt(searchFilter.countries.map { it.getFilterId() }.indexOf(i.getFilterId()))
                                        }
                                    }
                                }
                                is Sort -> {
                                    if (i.isSelected) {
                                        if (searchFilter.sort != i.type) {
                                            searchFilter.sort = i.type
                                        }
                                    }
                                }
                                is MovieType -> {
                                    if (i.isSelected) {
                                        if (searchFilter.type != i.type) {
                                            searchFilter.type = i.type
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDayNightModeChanged(type: Int) {
        for (item in boundViewHolders) {
            item.onDayNightModeChanged(type)
        }
    }
}