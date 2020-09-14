package com.ms.playstop.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration() : RecyclerView.ItemDecoration() {
    private var spanCount: Int = 0
    private var spacing: Int = 0
    private var includeEdge: Boolean = false

    constructor(spanCount:Int, spacing:Int, includeEdge:Boolean = false) : this()
    {
        this.spanCount = spanCount
        this.spacing = spacing
        this.includeEdge = includeEdge
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val position = parent.getChildAdapterPosition(view) // item position
        if(position == 0 && !includeEdge)
        {
            return
        }
        val column = position % spanCount // item column

        if (includeEdge) {
            outRect.right = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.left = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing*2
            }
            outRect.bottom = spacing*2 // item bottom
        } else {
            outRect.right = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
            outRect.left = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
        }
    }
}