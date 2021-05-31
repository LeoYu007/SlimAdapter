package com.mathew.slimadapter.core

/**
 * @author yu.
 * @date 2018/1/12
 */
interface DataOperator<T> {
    fun getItem(index: Int): T
    fun addData(item: T)
    fun addData(index: Int, item: T)
    fun addAll(items: List<T>)
    fun addAll(index: Int, items: List<T>)
    fun remove(index: Int)
    fun modify(index: Int, newData: T)
    fun modify(index: Int, action: (T) -> Unit)
    fun setNewData(items: List<T>)
    fun refreshDataSet(autoNotify: Boolean = true, block: (MutableList<T>) -> Unit)
    fun clear()
}