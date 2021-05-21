package com.yu1tiao.slimadapter

import android.view.View
import androidx.recyclerview.widget.ConcatAdapter


/**
 * @author yuli
 * @date 2021/5/20
 * @description SlimAdapter
 */
open class SlimAdapterEx<T> : SlimAdapter<T>() {

    private val concatAdapter = ConcatAdapter()
    private val contentAdapter = SlimAdapter<T>()


}


fun ConcatAdapter.header(headerBlock: () -> List<View>): ConcatAdapter {
    val headers = headerBlock()
    if (headers.isNullOrEmpty()) {
        return this
    }
    if (adapters.isEmpty()) {
        this.addAdapter(0, HeaderAdapter())
    } else if (adapters[0] is HeaderAdapter) {
        val headerAdapter = adapters[0] as HeaderAdapter



    }
    return this
}