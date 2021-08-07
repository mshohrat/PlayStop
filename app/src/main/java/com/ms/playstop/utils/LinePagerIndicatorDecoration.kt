package com.ms.playstop.utils

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.extension.convertDpToPixel


class LinePagerIndicatorDecoration: RecyclerView.ItemDecoration() {

    @ColorInt
    private var colorActive : Int = Color.parseColor("#0C2043")
    @ColorInt
    private var colorInactive : Int = Color.parseColor("#330D1F40")

    private val mIndicatorHeight = convertDpToPixel(24f)

    /**
     * Indicator stroke width.
     */
    private val mIndicatorStrokeWidth = convertDpToPixel(2f).toFloat()

    /**
     * Indicator width.
     */
    private val mIndicatorItemLength = convertDpToPixel(10f).toFloat()

    /**
     * Padding between indicators.
     */
    private val mIndicatorItemPadding = convertDpToPixel(6f).toFloat()

    private val mInterpolator: Interpolator = AccelerateDecelerateInterpolator()

    private val mStrokePaint: Paint = Paint()
    private val mInnerPaint: Paint = Paint()

    private var areColorsInitialized = false

    init {
        mStrokePaint.strokeCap = Paint.Cap.ROUND
        mStrokePaint.strokeWidth = mIndicatorStrokeWidth
        mStrokePaint.style = Paint.Style.STROKE
        mStrokePaint.isAntiAlias = true

        mInnerPaint.strokeCap = Paint.Cap.ROUND
        mInnerPaint.strokeWidth = mIndicatorStrokeWidth
        mInnerPaint.style = Paint.Style.FILL_AND_STROKE
        mInnerPaint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        initColors(parent.context)
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

    private fun initColors(context: Context?) {
        if(areColorsInitialized) return
        context?.let { ctx ->
            colorActive = ContextCompat.getColor(ctx, R.color.white)
            colorInactive = ContextCompat.getColor(ctx, R.color.gray_opacity_50)
            areColorsInitialized = true
        }
    }

    private fun drawInactiveIndicators(
        c: Canvas,
        indicatorStartX: Float,
        indicatorPosY: Float,
        itemCount: Int
    ) {
        mStrokePaint.color = colorInactive

        // width of item indicator including padding
        val itemWidth = mIndicatorItemLength + mIndicatorItemPadding
        val radius = mIndicatorItemLength / 2f
        var start = indicatorStartX
        for (i in 0 until itemCount) {
            // draw the line for every item
            c.drawCircle(start + radius, indicatorPosY, radius, mStrokePaint)
            start += itemWidth
        }
    }

    private fun drawHighlights(
        c: Canvas, indicatorStartX: Float, indicatorPosY: Float,
        highlightPosition: Int, progress: Float, itemCount: Int
    ) {
        Log.i("ttttt",progress.toString())
        mStrokePaint.color = colorActive
        mInnerPaint.color = colorActive

        // width of item indicator including padding
        val itemWidth = mIndicatorItemLength + mIndicatorItemPadding
        if (progress == 0f) {
            // no swipe, draw a normal indicator
            val highlightStart = indicatorStartX + (itemWidth * (itemCount - 1 - highlightPosition))
            val radius = mIndicatorItemLength / 2f
            c.drawCircle(
                highlightStart + radius, indicatorPosY,
                radius , mInnerPaint
            )
        } else {
            var highlightStart = indicatorStartX + (itemWidth * (itemCount - 1 - highlightPosition))
            val radius = mIndicatorItemLength / 2f

            // calculate partial highlight
            val partialLength = mIndicatorItemLength * progress

            // draw the cut off highlight
            c.drawCircle(
                highlightStart - radius + mIndicatorItemLength.times(1 - progress), indicatorPosY,
                radius, mInnerPaint
            )

            // draw the highlight overlapping to the next item as well
            if (highlightPosition in 0 until itemCount) {
                highlightStart = highlightStart - radius + mIndicatorItemLength.times(1 - progress) - radius
                c.drawCircle(
                    highlightStart, indicatorPosY, radius, mInnerPaint
                )
                val path = Path()
                path.moveTo(highlightStart,indicatorPosY - radius)
                path.lineTo(highlightStart + radius,indicatorPosY - radius)
                path.lineTo(highlightStart + radius,indicatorPosY + radius)
                path.lineTo(highlightStart,indicatorPosY + radius)
                path.close()
                c.drawPath(path,mInnerPaint)
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
        outRect.bottom = mIndicatorHeight
    }
}