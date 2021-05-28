package com.yu1tiao.slimadapter.ex

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yu1tiao.slimadapter.core.AbsAdapter
import com.yu1tiao.slimadapter.core.ViewHolder
import com.yu1tiao.slimadapter.util.SlimUtil


interface AttachListener {
    fun onAttachedToRecyclerView(recyclerView: RecyclerView)
}

interface DetachListener {
    fun onDetachedFromRecyclerView(recyclerView: RecyclerView)
}

/**
 * @author yuli
 * @date 2021/5/20
 * @description SlimAdapter
 */
open class FullSpanAdapter : AbsAdapter<View>() {

    private var mOrientation = 0
    var attachListener: AttachListener? = null
    var detachListener: DetachListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        getItem(viewType).apply {
            layoutParams = SlimUtil.generateLayoutParamsForHeaderAndFooter(mOrientation, this)
        }
        return ViewHolder(getItem(viewType))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        detachListener?.onDetachedFromRecyclerView(recyclerView)
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        attachListener?.onAttachedToRecyclerView(recyclerView)
        initOrientation(recyclerView.layoutManager)
        setSpanSizeLookup4Grid(recyclerView)
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        setSpanSizeLookup4StaggeredGrid(holder)
    }

    private fun setSpanSizeLookup4StaggeredGrid(holder: ViewHolder) {
        val lp = holder.itemView.layoutParams
        if (lp is StaggeredGridLayoutManager.LayoutParams) {
            lp.isFullSpan = true
        }
    }

    private fun setSpanSizeLookup4Grid(recyclerView: RecyclerView) {
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(index: Int): Int {
                    return manager.spanCount
                }
            }
        }
    }

    private fun initOrientation(manager: RecyclerView.LayoutManager?) {
        if (manager is LinearLayoutManager) {
            mOrientation = manager.orientation
        } else if (manager is StaggeredGridLayoutManager) {
            mOrientation = manager.orientation
        }
    }
}

class HeaderAdapter : FullSpanAdapter()
class FooterAdapter : FullSpanAdapter()
class EmptyAdapter : FullSpanAdapter()