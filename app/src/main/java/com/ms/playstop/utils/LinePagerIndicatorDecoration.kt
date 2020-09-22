package com.ms.playstop.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.extension.convertDpToPixel


class LinePagerIndicatorDecoration: RecyclerView.ItemDecoration() {

    private val colorActive = Color.parseColor("#0C2043")
    private val colorInactive = Color.parseColor("#330D2040")

    private val mIndicatorHeight = convertDpToPixel(16f)

    /**
     * Indicator stroke width.
     */
    private val mIndicatorStrokeWidth = convertDpToPixel(2f).toFloat()

    /**
     * Indicator width.
     */
    private val mIndicatorItemLength = convertDpToPixel(16f).toFloat()

    /**
     * Padding between indicators.
     */
    private val mIndicatorItemPadding = convertDpToPixel(4f).toFloat()

    private val mInterpolator: Interpolator = AccelerateDecelerateInterpolator()

    private val mPaint: Paint = Paint()

    init {
        mPaint.strokeCap = Paint.Cap.ROUND;
        mPaint.strokeWidth = mIndicatorStrokeWidth;
        mPaint.style = Paint.Style.STROKE;
        mPaint.isAntiAlias = true;
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val itemCount: Int = parent.adapter?.itemCount ?: 0

        // center horizontally, calculate width and subtract half from center

        // center horizontally, calculate width and subtract half from center
        val totalLength = mIndicatorItemLength * itemCount
        val paddingBetweenItems =
            Math.max(0, itemCount - 1) * mIndicatorItemPadding
        val indicatorTotalWidth = totalLength + paddingBetweenItems
        val indicatorStartX: Float = (parent.width - indicatorTotalWidth) / 2f

        // center vertically in the allotted space

        // center vertically in the allotted space
        val indicatorPosY: Float = parent.height - mIndicatorHeight / 2f

        drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount)


        // find active page (which should be highlighted)


        // find active page (which should be highlighted)
        val layoutManager =
            parent.layoutManager as? LinearLayoutManager
        val activePosition = layoutManager?.findFirstVisibleItemPosition() ?: -1
        if (activePosition == RecyclerView.NO_POSITION) {
            return
        }

        // find offset of active page (if the user is scrolling)

        // find offset of active page (if the user is scrolling)
        val activeChild: View? = layoutManager?.findViewByPosition(activePosition)
        val left: Int = activeChild?.left ?: 0
        val width: Int = activeChild?.width ?: 0

        // on swipe the active item will be positioned from [-width, 0]
        // interpolate offset for smooth animation

        // on swipe the active item will be positioned from [-width, 0]
        // interpolate offset for smooth animation
        val progress =
            mInterpolator.getInterpolation(left / width.toFloat())

        drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount)
    }

    private fun drawInactiveIndicators(
        c: Canvas,
        indicatorStartX: Float,
        indicatorPosY: Float,
        itemCount: Int
    ) {
        mPaint.color = colorInactive

        // width of item indicator including padding
        val itemWidth = mIndicatorItemLength + mIndicatorItemPadding
        var start = indicatorStartX
        for (i in 0 until itemCount) {
            // draw the line for every item
            c.drawLine(start, indicatorPosY, start + mIndicatorItemLength, indicatorPosY, mPaint)
            start += itemWidth
        }
    }

    private fun drawHighlights(
        c: Canvas, indicatorStartX: Float, indicatorPosY: Float,
        highlightPosition: Int, progress: Float, itemCount: Int
    ) {
        mPaint.color = colorActive

        // width of item indicator including padding
        val itemWidth = mIndicatorItemLength + mIndicatorItemPadding
        if (progress == 0f) {
            // no swipe, draw a normal indicator
            val highlightStart = indicatorStartX + (itemWidth * (itemCount - 1 - highlightPosition))
            c.drawLine(
                highlightStart, indicatorPosY,
                highlightStart + mIndicatorItemLength , indicatorPosY, mPaint
            )
        } else {
            var highlightStart = indicatorStartX + (itemWidth * (itemCount - 1 - highlightPosition))
            // calculate partial highlight
            val partialLength = mIndicatorItemLength * progress

            // draw the cut off highlight
            c.drawLine(
                highlightStart, indicatorPosY,
                highlightStart + mIndicatorItemLength - partialLength, indicatorPosY, mPaint
            )

            // draw the highlight overlapping to the next item as well
            if (highlightPosition in 0 until itemCount) {
                highlightStart -= itemWidth
                c.drawLine(
                    highlightStart + mIndicatorItemLength - partialLength, indicatorPosY,
                    highlightStart + mIndicatorItemLength, indicatorPosY, mPaint
                )
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = mIndicatorHeight;
    }
}