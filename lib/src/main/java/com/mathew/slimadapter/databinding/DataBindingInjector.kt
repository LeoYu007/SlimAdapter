package com.mathew.slimadapter.databinding

import androidx.databinding.ViewDataBinding
import com.mathew.slimadapter.core.ViewHolder
import com.mathew.slimadapter.core.ViewInjector

/**
 * Copyright (c) 2021 $ All rights reserved.
 *
 * @author mathew
 * @date 2021/5/31
 * @description DataBindingInjector
 */
open class DataBindingInjector<T>(layoutId: Int, private val variableId: Int) :
    ViewInjector<T>(layoutId) {

    override fun bind(holder: ViewHolder, item: T, position: Int) {
        val dViewHolder = holder as DataBindingViewHolder
        val binding = dViewHolder.binding
        if (binding != null) {
            if (variableId > 0) {
                binding.setVariable(variableId, item)
                binding.executePendingBindings()
            }
            onBind(binding, item, position)
        }
    }

    open fun onBind(binding: ViewDataBinding, item: T, position: Int) {

    }
}