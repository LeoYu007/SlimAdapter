package com.mathew.slimadapter.ex

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mathew.slimadapter.AttachToRecyclerViewListener
import com.mathew.slimadapter.AttachToWindowListener
import com.mathew.slimadapter.SlimAdapter
import com.mathew.slimadapter.ex.loadmore.MoreLoader
import com.mathew.slimadapter.core.DataOperator
import com.mathew.slimadapter.core.ViewHolder

/**
 * 为了一个普通的adapter增加 header、footer、empty、loadMore的功能
 * 实际上是构建了一个ConcatAdapter，header、footer、empty分别是3个不同的子adapter
 */
class SlimAdapterHelper<T>(
    val contentAdapter: SlimAdapter<T>,
    private val headers: Array<View>? = null,
    private val footers: Array<View>? = null,
    private val moreLoader: MoreLoader? = null,
    private val emptyView: View? = null,
    emptyViewHeight: Int = ViewGroup.LayoutParams.MATCH_PARENT
) : DataOperator<T> by contentAdapter {

    private lateinit var realAdapter: ConcatAdapter
    private var emptyAdapter: EmptyAdapter? = null
    private var footerAdapter: FooterAdapter? = null
    private var headerAdapter: HeaderAdapter? = null

    init {
        emptyView?.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, emptyViewHeight
        )
    }

    fun loadMoreEnable(enable: Boolean) {
        moreLoader?.enable = enable
    }

    fun loadMoreCompleted() {
        moreLoader?.loadMoreCompleted()
    }

    fun loadMoreError() {
        moreLoader?.loadMoreError()
    }

    fun noMore() {
        moreLoader?.noMore()
    }

    /**
     * 此方法在第一次更新数据后可以选择性调用
     */
    fun disableIfNotFullPage() {
        moreLoader?.disableIfNotFullPage()
    }

    fun build(): ConcatAdapter {
        if (!footers.isNullOrEmpty() && moreLoader != null) {
            throw RuntimeException("不能同时添加footer和loadMore")
        }

        realAdapter = ConcatAdapter(contentAdapter)

        if (!headers.isNullOrEmpty()) {
            realAdapter.addHeader(*headers)
        }
        if (!footers.isNullOrEmpty()) {
            realAdapter.addFooter(*footers)
        }
        if (moreLoader != null) {
            realAdapter.enableLoadMore(moreLoader)
            contentAdapter.addOnAttachRecyclerViewListener(moreLoader)
            moreLoader.setItemCountCallback {
                return@setItemCountCallback realAdapter.itemCount
            }
        }

        emptyAdapter = if (emptyView != null) {
            EmptyAdapter(emptyView)
        } else {
            null
        }
        emptyAdapter?.let {
            val emptyIndex = if (headers.isNullOrEmpty()) 0 else 1
            realAdapter.addAdapter(emptyIndex, it)
        }

        if (
            !headers.isNullOrEmpty() || !footers.isNullOrEmpty() ||
            emptyView != null || moreLoader != null
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
                    moreLoader?.isEmpty = true
                    emptyAdapter?.show()
                } else {
                    moreLoader?.isEmpty = false
                    emptyAdapter?.hide()
                }
//                moreLoader?.disableIfNotFullPage()
            }
        })

        return realAdapter
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

    private fun isFullSpan(index: Int): Boolean {
        val headerSize = headerAdapter?.itemCount ?: 0
        val emptySize = emptyAdapter?.itemCount ?: 0
        val footerSize = footerAdapter?.itemCount ?: 0

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

        return false
    }

    private fun ConcatAdapter.addHeader(vararg view: View): ConcatAdapter {

        headerAdapter = if (adapters.isNotEmpty() && adapters[0] is HeaderAdapter) {
            adapters[0] as HeaderAdapter
        } else {
            val headerAdapter = HeaderAdapter()
            this.addAdapter(0, headerAdapter)
            headerAdapter
        }

        headerAdapter?.addAll(view.toList())

        return this
    }

    private fun ConcatAdapter.addFooter(vararg view: View): ConcatAdapter {

        footerAdapter = if (adapters.isNotEmpty() && adapters[adapters.size - 1] is FooterAdapter) {
            adapters[adapters.size - 1] as FooterAdapter
        } else {
            val footerAdapter = FooterAdapter()
            this.addAdapter(footerAdapter)
            footerAdapter
        }

        footerAdapter?.addAll(view.toList())

        return this
    }

    private fun ConcatAdapter.enableLoadMore(loader: MoreLoader): ConcatAdapter {
        return addFooter(loader.loadMoreFooterView)
    }

}