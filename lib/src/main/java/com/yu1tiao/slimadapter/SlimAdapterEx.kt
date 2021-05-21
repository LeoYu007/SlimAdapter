package com.yu1tiao.slimadapter

import androidx.recyclerview.widget.ConcatAdapter
import com.yu1tiao.slimadapter.core.InjectorFinder
import com.yu1tiao.slimadapter.core.ViewInjector


fun ConcatAdapter.header(
    headers: List<ViewInjector<Any>>,
    injectorFinder: InjectorFinder? = null
): ConcatAdapter {

    if (headers.isNullOrEmpty()) {
        return this
    }

    if (headers.size > 1 && injectorFinder == null) {
        throw RuntimeException("多个ViewInjector必须同时传入InjectorFinder")
    }

    if (adapters.isNotEmpty() && adapters[0] is HeaderAdapter) {
        val headerAdapter = adapters[0] as HeaderAdapter
        headers.forEach {
            headerAdapter.register(it)
        }
    } else {
        this.addAdapter(0, HeaderAdapter(headers, injectorFinder))
    }

    return this
}

fun ConcatAdapter.footer(
    footer: List<ViewInjector<Any>>,
    injectorFinder: InjectorFinder? = null
): ConcatAdapter {

    if (footer.isNullOrEmpty()) {
        return this
    }

    if (footer.size > 1 && injectorFinder == null) {
        throw RuntimeException("多个ViewInjector必须同时传入InjectorFinder")
    }

    if (adapters.isNotEmpty() && adapters[adapters.size - 1] is FooterAdapter) {
        val footerAdapter = adapters[0] as FooterAdapter
        footer.forEach {
            footerAdapter.register(it)
        }
    } else {
        this.addAdapter(FooterAdapter(footer, injectorFinder))
    }
    return this
}


fun ConcatAdapter.updateHeaderData(headerIndex: Int, obj: Any) {
    if (adapters.isEmpty() || adapters[0] !is HeaderAdapter) {
        return
    }

    val headerAdapter = adapters[0] as HeaderAdapter
    headerAdapter.modify(headerIndex, obj)
}

fun ConcatAdapter.setHeaderData(data: List<Any>) {
    if (adapters.isEmpty() || adapters[0] !is HeaderAdapter) {
        return
    }

    val headerAdapter = adapters[0] as HeaderAdapter
    headerAdapter.updateData(data)
}

fun ConcatAdapter.updateFooterData(footerIndex: Int, obj: Any) {
    if (adapters.isEmpty() || adapters[adapters.size - 1] !is FooterAdapter) {
        return
    }

    val footerAdapter = adapters[adapters.size - 1] as FooterAdapter
    footerAdapter.modify(footerIndex, obj)
}

fun ConcatAdapter.setFooterData(data: List<Any>) {
    if (adapters.isEmpty() || adapters[adapters.size - 1] !is FooterAdapter) {
        return
    }

    val footerAdapter = adapters[adapters.size - 1] as FooterAdapter
    footerAdapter.updateData(data)
}