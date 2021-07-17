package com.mathew.slimadapter.ex

import com.mathew.slimadapter.ex.loadmore.MoreLoader

class LoadMoreAdapter(private val moreLoader: MoreLoader) : FullSpanAdapter() {
    init {
        moreLoader.enable = true
        addData(moreLoader.loadMoreFooterView)
    }

    fun enable(enable: Boolean) {
        moreLoader.enable = enable
        if (enable) {
            setNewData(listOf(moreLoader.loadMoreFooterView))
        } else {
            clear()
        }
    }

    fun completed() {
        moreLoader.loadMoreCompleted()
    }

    fun error() {
        moreLoader.loadMoreError()
    }

    fun noMore() {
        moreLoader.noMore()
    }

    fun disableLoadMoreIfNotFullPage() {
        moreLoader.disableIfNotFullPage()
    }

    fun setContentEmpty(empty: Boolean) {
        moreLoader.isContentEmpty = empty
    }
}