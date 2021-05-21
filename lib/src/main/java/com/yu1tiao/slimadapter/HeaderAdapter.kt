package com.yu1tiao.slimadapter

import android.util.SparseArray
import android.view.ViewGroup
import com.yu1tiao.slimadapter.core.AbsAdapter
import com.yu1tiao.slimadapter.core.InjectorFinder
import com.yu1tiao.slimadapter.core.ViewHolder
import com.yu1tiao.slimadapter.core.ViewInjector


/**
 * Copyright (c) 2021 北京嗨学网教育科技股份有限公司 All rights reserved.
 *
 * @author yuli
 * @date 2021/5/20
 * @description SlimAdapter
 */
class HeaderAdapter : AbsAdapter<Any>() {


    private val viewInjectors by lazy { SparseArray<ViewInjector<*>>() }
    private var injectorFinder: InjectorFinder<*>? = null

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    fun <T> register(clazz: Class<T>, injector: ViewInjector<T>) {

        viewInjectors.put(injector.layoutId, injector)
    }
}

