package com.ms.playstop.ui.search.filter.adapter

interface FilterItem {
    var isSelected : Boolean
    fun getFilterName() : String
    fun getFilterId() : String
}