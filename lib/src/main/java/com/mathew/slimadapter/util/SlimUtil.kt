package com.mathew.slimadapter.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*

internal object SlimUtil {


    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun findLastCompletelyVisibleItemPosition(layoutManager: RecyclerView.LayoutManager): Int {
        return when (layoutManager) {
            is GridLayoutManager -> {
                layoutManager.findLastCompletelyVisibleItemPosition()
            }
            is StaggeredGridLayoutManager -> {
                val into = IntArray(layoutManager.spanCount)
                layoutManager.findLastCompletelyVisibleItemPositions(into)
                findMax(into)
            }
            else -> {
                (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            }
        }
    }

    private fun findMax(lastPositions: IntArray): Int {
        var max = lastPositions[0]
        for (value in lastPositions) {
            if (value > max) {
                max = value
            }
        }
        return max
    }

    /**
     * 根据adapter的方向，为header和footer生成LayoutParams
     *
     * @param orientation
     * @param view
     * @return
     */
    fun generateLayoutParamsForHeaderAndFooter(
        orientation: Int,
        view: View
    ): RecyclerView.LayoutParams {
        val oldParams = view.layoutParams
        var marginLeft = 0
        var marginRight = 0
        var marginTop = 0
        var marginBottom = 0
        if (oldParams is ViewGroup.MarginLayoutParams) {
            marginLeft = oldParams.leftMargin
            marginRight = oldParams.rightMargin
            marginTop = oldParams.topMargin
            marginBottom = oldParams.bottomMargin
        }
        val width: Int
        val height: Int
        if (orientation == OrientationHelper.HORIZONTAL) {
            width =
                if (oldParams == null) ViewGroup.LayoutParams.WRAP_CONTENT else view.layoutParams.width
            height = ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height =
                if (oldParams == null) ViewGroup.LayoutParams.WRAP_CONTENT else view.layoutParams.height
        }
        val newParams = RecyclerView.LayoutParams(width, height)
        newParams.setMargins(marginLeft, marginTop, marginRight, marginBottom)
        return newParams
    }
}