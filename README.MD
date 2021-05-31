### 一个简单的adapter封装库,fully write in kotlin

- header/footer
- empty view
- auto load more
- auto diff

### usage

```kotlin
// 简单使用
 val slimAdapter = SlimAdapter<OnePiece>().apply {
            // 每注册一个viewInjector代表一种viewType
            register(R.layout.item_normal) { holder, item, _ ->
                bindData2View(holder, item)
            }
            register(R.layout.item_big) { holder, item, _ ->
                bindData2View(holder, item)
            }

            // 多itemType必须设置injectorFinder
            injectorFinder { item, _, _ ->
                if ((item as OnePiece).isBigType) R.layout.item_big else R.layout.item_normal
            }

            itemClickListener { index, _ ->
                showToast("click position : $index")
            }
            itemLongClickListener { index, _ ->
                remove(index)
            }

            autoDiff()
        }

```
#### 更新数据的接口，SlimAdapter和扩展的SlimAdapterWrapper都实现了此接口用来更新数据
```kotlin
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
```
#### 扩展功能
- 此功能是采用ConcatAdapter实现，header、footer、empty和load more分别是单独的adapter，互不影响，也不影响内容adapter。
- 由于ConcatAdapter是final的，所以只能采用包装的方式来做多adapter的管理，使用方法如下：
```kotlin

        // 内容adapter
        val slimAdapter = ...

        val empty = View.inflate(this, R.layout.empty_view, null)
        val header1 = View.inflate(this, R.layout.item_header, null)
        val header2 = View.inflate(this, R.layout.item_header, null)
        val footer = View.inflate(this, R.layout.item_footer, null)

        // 通过SlimAdapterWrapper扩展，header、footer、empty和load more功能
        val wrapper = SlimAdapterWrapper(this, slimAdapter).apply {
            addHeader(header1)
            addHeader(header2)
            // footer和load more不能同时存在
//            addFooter(footer)
            emptyView(empty)
            enableLoadMore(object : LoadMoreListener {
                override fun onLoadMore() {
                    loadMore()
                }
            })
        }

        recyclerView.adapter = wrapper.getAdapter()
```