package com.mathew.slimadapter.diff

class DefaultDiffCallback() : SlimDiffUtil.Callback {

    override fun areItemsTheSame(oldItem: Any?, newItem: Any?): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Any?, newItem: Any?): Boolean {
        return oldItem?.equals(newItem) ?: false
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return null
    }
}