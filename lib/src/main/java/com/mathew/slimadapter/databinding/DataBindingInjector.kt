package com.mathew.slimadapter.databinding

import androidx.databinding.DataBindingUtil
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
open class DataBindingInjector<T, B : ViewDataBinding>(layoutId: Int, private val variableId: Int) :
    ViewInjector<T>(layoutId) {

    override fun bind(holder: ViewHolder, item: T, position: Int) {
        val binding = DataBindingUtil.bind<B>(holder.itemView)
        if (binding != null) {
            if (variableId > 0) {
                binding.setVariable(variableId, item)
                binding.executePendingBindings()
            }
            onBind(binding, item, position)
        }
    }

    open fun onBind(binding: B, item: T, position: Int) {

    }
}