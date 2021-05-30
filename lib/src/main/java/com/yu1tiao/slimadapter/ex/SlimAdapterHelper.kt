package com.yu1tiao.slimadapter.ex

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yu1tiao.slimadapter.SlimAdapter
import com.yu1tiao.slimadapter.ex.loadmore.MoreLoader
import me.yuu.liteadapter.core.DataOperator

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
        }

        val emptyAdapter = if (emptyView != null) {
            EmptyAdapter().apply {
                addData(emptyView)
            }
        } else {
            null
        }
        val emptyIndex = if (headers.isNullOrEmpty()) 1 else 2

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
                    emptyAdapter?.let {
                        realAdapter.addAdapter(emptyIndex, it)
                    }
                } else {
                    moreLoader?.isEmpty = false
                    emptyAdapter?.let {
                        realAdapter.removeAdapter(it)
                    }
                }
//                moreLoader?.disableIfNotFullPage()
            }
        })

        return realAdapter
    }

    private fun ConcatAdapter.addHeader(vararg view: View): ConcatAdapter {

        if (adapters.isNotEmpty() && adapters[0] is HeaderAdapter) {
            adapters[0] as HeaderAdapter
        } else {
            val headerAdapter = FullSpanAdapter()
            this.addAdapter(0, headerAdapter)
            headerAdapter
        }.addAll(view.toList())

        return this
    }

    private fun ConcatAdapter.addFooter(vararg view: View): ConcatAdapter {

        if (adapters.isNotEmpty() && adapters[adapters.size - 1] is FooterAdapter) {
            adapters[adapters.size - 1] as FooterAdapter
        } else {
            val footerAdapter = FooterAdapter()
            this.addAdapter(footerAdapter)
            footerAdapter
        }.addAll(view.toList())

        return this
    }

    private fun ConcatAdapter.enableLoadMore(loader: MoreLoader): ConcatAdapter {

        val footerAdapter =
            if (adapters.isNotEmpty() && adapters[adapters.size - 1] is FooterAdapter) {
                adapters[adapters.size - 1] as FooterAdapter
            } else {
                val footerAdapter = FooterAdapter()
                this.addAdapter(footerAdapter)
                footerAdapter
            }

        footerAdapter.addData(loader.loadMoreFooterView)

        footerAdapter.attachListener = loader
        footerAdapter.detachListener = loader
        loader.setItemCountCallback {
            return@setItemCountCallback itemCount
        }

        return this
    }

}