package com.mathew.slimadapter.ex

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mathew.slimadapter.core.AbsAdapter
import com.mathew.slimadapter.core.ViewHolder
import com.mathew.slimadapter.ex.loadmore.MoreLoader
import com.mathew.slimadapter.util.SlimUtil


/**
 * @author yuli
 * @date 2021/5/20
 */
open class FullSpanAdapter : AbsAdapter<View>() {

    private var mOrientation = RecyclerView.VERTICAL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = getItem(viewType).apply {
            layoutParams = SlimUtil.generateLayoutParamsForHeaderAndFooter(mOrientation, this)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        initOrientation(recyclerView.layoutManager)
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

class EmptyAdapter(private val emptyView: View, private var errorView: View?) : FullSpanAdapter() {

    fun show() {
        setNewData(listOf(emptyView))
    }

    fun showError(block: ((View) -> Unit)? = null) {
        errorView?.let {
            block?.invoke(it)
            setNewData(listOf(it))
        } ?: kotlin.run {
            Log.w("EmptyAdapter", "error view is null")
        }
    }

    fun hide() {
        clear()
    }
}