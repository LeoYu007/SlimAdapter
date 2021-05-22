package com.yu1tiao.slimadapter.core

import androidx.annotation.IntRange
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yu1tiao.slimadapter.diff.SlimDiffUtil
import me.yuu.liteadapter.core.DataOperator


typealias OnItemClickListener = (index: Int, item: Any) -> Unit
typealias OnItemLongClickListener = (index: Int, item: Any) -> Unit

/**
 * @author yu
 * @date 2018/1/14
 */
abstract class AbsAdapter<T> : RecyclerView.Adapter<ViewHolder>(), DataOperator<T> {

    @JvmField
    protected var mDataSet: MutableList<T> = ArrayList()
    protected var diffCallback: SlimDiffUtil.Callback? = null

    fun getDataSet(): List<T> {
        return mDataSet
    }

    open fun isEmpty(): Boolean = mDataSet.isNullOrEmpty()

    override fun getItem(@IntRange(from = 0) index: Int): T {
        return mDataSet[index]
    }

    override fun getItemCount(): Int {
        return mDataSet.size
    }

    override fun updateData(items: List<T>) {
        if (diffCallback != null && mDataSet.isNotEmpty()) {
            val diffResult = DiffUtil.calculateDiff(SlimDiffUtil(mDataSet, items, diffCallback!!))
            mDataSet = ArrayList(items)
            diffResult.dispatchUpdatesTo(this)
        } else {
            mDataSet.clear()
            mDataSet.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun addData(item: T) {
        mDataSet.add(item)
        notifyItemInserted(mDataSet.size)
    }

    override fun addData(@IntRange(from = 0) index: Int, item: T) {
        mDataSet.add(index, item)
        notifyItemInserted(index)
    }

    override fun addAll(items: List<T>) {
        if (items.isNotEmpty()) {
            mDataSet.addAll(items)
            notifyItemRangeInserted(mDataSet.size, items.size)
        }
    }

    override fun addAll(@IntRange(from = 0) index: Int, items: List<T>) {
        if (items.isNotEmpty()) {
            mDataSet.addAll(index, items)
            notifyItemRangeInserted(index, items.size)
        }
    }

    override fun remove(@IntRange(from = 0) index: Int) {
        mDataSet.removeAt(index)
        if (mDataSet.isEmpty()) {
            notifyDataSetChanged()
        } else {
            notifyItemRemoved(index)
        }
    }

    override fun clear() {
        mDataSet.clear()
        notifyDataSetChanged()
    }

    override fun modify(@IntRange(from = 0) index: Int, newData: T) {
        mDataSet[index] = newData
        notifyItemChanged(index)
    }

    override fun modify(index: Int, action: (T) -> Unit) {
        action(mDataSet[index])
        notifyItemChanged(index)
    }

    fun refreshData(notify: Boolean = true, block: (MutableList<T>) -> Unit) {
        block(mDataSet)
        if (notify) {
            notifyDataSetChanged()
        }
    }

}