package com.mathew.slimadapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mathew.slimadapter.core.DataOperator
import com.mathew.slimadapter.core.ViewHolder
import com.mathew.slimadapter.ex.EmptyAdapter
import com.mathew.slimadapter.ex.FooterAdapter
import com.mathew.slimadapter.ex.HeaderAdapter
import com.mathew.slimadapter.ex.LoadMoreAdapter
import com.mathew.slimadapter.ex.loadmore.DefaultLoadMoreFooter
import com.mathew.slimadapter.ex.loadmore.ILoadMoreFooter
import com.mathew.slimadapter.ex.loadmore.LoadMoreListener
import com.mathew.slimadapter.ex.loadmore.MoreLoader

/**
 * Copyright (c) 2021 $ All rights reserved.
 *
 * @author mathew
 * @date 2021/5/31
 * @description SlimAdapterWrapper，为普通的adapter增加扩展功能，包括empty、loadMore、header、footer
 */
open class SlimAdapterWrapper<T>(
    private val context: Context,
    private val contentAdapter: SlimAdapter<T>
) : DataOperator<T> by contentAdapter {

    // 这个是真正的adapter，管理所有子adapter
    private val realAdapter = ConcatAdapter()
    private var emptyAdapter: EmptyAdapter? = null
    private var loadMoreAdapter: LoadMoreAdapter? = null
    private var footerAdapter: FooterAdapter? = null
    private var headerAdapter: HeaderAdapter? = null

    init {
        realAdapter.addAdapter(contentAdapter)
    }

    fun getAdapter(): ConcatAdapter = realAdapter

    //////////////////////////////////////////////////////////////////////////////////////////////
    private var isDataObserverRegister = false
    private var isFullSpanCallbackInit = false

    private fun initEmptyViewSupport() {
        if (isDataObserverRegister) {
            return
        }
        contentAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                Log.i("SlimAdapterEx", "receive onChanged")
                doIfChange()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                Log.i("SlimAdapterEx", "receive onItemRangeChanged")
                doIfChange()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                Log.i("SlimAdapterEx", "receive onItemRangeInserted")
                doIfChange()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                Log.i("SlimAdapterEx", "receive onItemRangeRemoved")
                doIfChange()
            }

            private fun doIfChange() {
                if (contentAdapter.itemCount == 0) {
                    loadMoreAdapter?.setContentEmpty(true)
                    emptyAdapter?.show()
                } else {
                    loadMoreAdapter?.setContentEmpty(false)
                    emptyAdapter?.hide()
                }
            }
        })
        isDataObserverRegister = true
    }

    private fun initFullSpanCallback() {
        if (isFullSpanCallbackInit) {
            return
        }
        if (
            headerAdapter != null || footerAdapter != null ||
            emptyAdapter != null || loadMoreAdapter != null
        ) {
            contentAdapter.addOnAttachRecyclerViewListener(object : AttachToRecyclerViewListener {
                override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
                    setSpanSizeLookup4Grid(recyclerView)
                }

                override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
                }
            })
            contentAdapter.addOnAttachWindowListener(object : AttachToWindowListener {
                override fun onAttachedToWindow(holder: ViewHolder) {
                    setSpanSizeLookup4StaggeredGrid(holder)
                }

                override fun onDetachedFromWindow(holder: ViewHolder) {
                }
            })
            isFullSpanCallbackInit = true
        }
    }

    private fun setSpanSizeLookup4Grid(recyclerView: RecyclerView) {
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(index: Int): Int {
                    return if (isFullSpan(index)) {
                        manager.spanCount
                    } else {
                        1
                    }
                }
            }
        }
    }

    private fun setSpanSizeLookup4StaggeredGrid(holder: ViewHolder) {
        val lp = holder.itemView.layoutParams
        if (lp is StaggeredGridLayoutManager.LayoutParams) {
            val index = holder.layoutPosition
            lp.isFullSpan = isFullSpan(index)
        }
    }

    // 是否需要占据整行，用于header、footer、empty、loadMore布局使用
    protected open fun isFullSpan(index: Int): Boolean {
        val headerSize = headerAdapter?.itemCount ?: 0
        val emptySize = emptyAdapter?.itemCount ?: 0
        val footerSize = footerAdapter?.itemCount ?: 0
        val loadMoreSize = loadMoreAdapter?.itemCount ?: 0

        val totalSize = realAdapter.itemCount

        if (headerSize != 0 && index < headerSize) {
            // header
            return true
        }
        if (emptySize != 0 && index < headerSize + emptySize) {
            // empty
            return true
        }
        if (footerSize != 0 && index in (totalSize - footerSize) until totalSize) {
            // footer
            return true
        }
        if (loadMoreSize != 0 && index in (totalSize - loadMoreSize) until totalSize) {
            // loadMore
            return true
        }

        return false
    }
    //////////////////////////////////////////////////////////////////////////////////////////////

    fun addHeader(header: View) {
        if (headerAdapter == null) {
            headerAdapter = HeaderAdapter()
            realAdapter.addAdapter(0, headerAdapter!!)
            initFullSpanCallback()
        }
        headerAdapter!!.addData(header)
    }

    fun removeHeader(index: Int) {
        headerAdapter?.remove(index)
    }

    fun addFooter(footer: View) {
        if (loadMoreAdapter != null) {
            Log.w("SlimAdapterEx", "同时存在footer和loadMore，可能会冲突")
        }
        if (footerAdapter == null) {
            footerAdapter = FooterAdapter()
            realAdapter.addAdapter(footerAdapter!!)
            initFullSpanCallback()
        }
        footerAdapter!!.addData(footer)
    }

    fun removeFooter(index: Int) {
        footerAdapter?.remove(index)
    }

    fun emptyView(
        view: View,
        layoutParams: ViewGroup.LayoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
    ) {
        if (emptyAdapter == null) {
            view.layoutParams = layoutParams

            emptyAdapter = EmptyAdapter(view)
            val emptyIndex = if (headerAdapter == null) 0 else 1
            realAdapter.addAdapter(emptyIndex, emptyAdapter!!)

            initFullSpanCallback()
            initEmptyViewSupport()
        }
    }

    fun enableLoadMore(listener: () -> Unit) {
        enableLoadMore(object : LoadMoreListener {
            override fun onLoadMore() {
                listener.invoke()
            }
        })
    }

    fun enableLoadMore(
        listener: LoadMoreListener,
        footer: ILoadMoreFooter = DefaultLoadMoreFooter(context)
    ) {
        if (footerAdapter != null) {
            Log.w("SlimAdapterEx", "同时存在footer和loadMore，可能会冲突")
        }
        if (loadMoreAdapter == null) {
            val loader = MoreLoader(listener, footer)
            loadMoreAdapter = LoadMoreAdapter(loader)
            realAdapter.addAdapter(loadMoreAdapter!!)

            contentAdapter.addOnAttachRecyclerViewListener(loader)
            loader.setItemCountCallback {
                return@setItemCountCallback realAdapter.itemCount
            }

            initFullSpanCallback()
        }
    }

    fun loadMoreEnable(enable: Boolean) {
        loadMoreAdapter?.enable(enable)
    }

    fun loadMoreCompleted() {
        loadMoreAdapter?.completed()
    }

    fun loadMoreError() {
        loadMoreAdapter?.error()
    }

    fun noMore() {
        loadMoreAdapter?.noMore()
    }

    fun disableLoadMoreIfNotFullPage() {
        loadMoreAdapter?.disableLoadMoreIfNotFullPage()
    }
}