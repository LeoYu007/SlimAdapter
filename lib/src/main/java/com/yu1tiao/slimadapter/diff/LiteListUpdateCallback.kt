package com.yu1tiao.slimadapter.diff

import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView

class LiteListUpdateCallback(
    private val mAdapter: RecyclerView.Adapter<*>
) : ListUpdateCallback {

    override fun onInserted(position: Int, count: Int) {
        mAdapter.notifyItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        mAdapter.notifyItemRangeRemoved(position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        mAdapter.notifyItemMoved(fromPosition, toPosition)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        mAdapter.notifyItemRangeChanged(position, count, payload)
    }

}