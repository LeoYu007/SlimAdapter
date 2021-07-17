package com.mathew.slimadapter.ex

import android.util.Log
import android.view.View


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