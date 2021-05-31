package com.mathew.slimadapter.databinding

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mathew.slimadapter.core.ViewHolder

/**
 * Copyright (c) 2021 $ All rights reserved.
 *
 * @author mathew
 * @date 2021/5/31
 * @description DataBindingViewHolder
 */
class DataBindingViewHolder(itemView: View) : ViewHolder(itemView) {

    val binding: ViewDataBinding? = DataBindingUtil.bind(itemView)

}