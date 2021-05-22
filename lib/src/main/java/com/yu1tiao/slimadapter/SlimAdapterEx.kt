package com.yu1tiao.slimadapter

import android.view.View
import androidx.recyclerview.widget.ConcatAdapter


fun ConcatAdapter.addHeader(view: View): ConcatAdapter {

    if (adapters.isNotEmpty() && adapters[0] is FullSpanAdapter) {
        adapters[0] as FullSpanAdapter
    } else {
        val headerAdapter = FullSpanAdapter()
        this.addAdapter(0, headerAdapter)
        headerAdapter
    }.addData(view)

    return this
}

fun ConcatAdapter.addFooter(view: View): ConcatAdapter {

    if (adapters.isNotEmpty() && adapters[adapters.size - 1] is FooterAdapter) {
        adapters[adapters.size - 1] as FooterAdapter
    } else {
        val footerAdapter = FooterAdapter()
        this.addAdapter(footerAdapter)
        footerAdapter
    }.addData(view)

    return this
}