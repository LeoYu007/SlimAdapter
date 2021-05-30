package com.mathew.slimadapter.advenced

import com.mathew.slimadapter.SlimAdapter
import com.mathew.slimadapter.entity.SelectableItem

/**
 * 多选列表
 */
open class MultiSelectAdapter<T : SelectableItem> : SlimAdapter<T>() {

    /**
     * 全选
     */
    open fun selectAll() {
        mDataSet.forEach {
            it.isSelected = true
        }
        notifyDataSetChanged()
    }

    /**
     * 反选
     */
    open fun invertSelect() {
        mDataSet.forEach {
            it.isSelected = !it.isSelected
        }
        notifyDataSetChanged()
    }

    open fun toggle(index: Int) {
        modify(index) {
            it.isSelected = !it.isSelected
        }
    }

}