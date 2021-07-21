### 一个简单的adapter封装库,fully write in kotlin

- multi item type
- header/footer
- empty/error view
- auto load more
- auto diff

### usage

#### 简单使用
```kotlin
   val adapter = SlimAdapter<String>().apply {
        // 每注册一个viewInjector代表一种viewType
        register(R.layout.item_normal) { holder, item, _ ->
            bindData2View(holder, item)
        }
        // 也支持DataBinding
        registerDataBind(R.layout.item_big, BR.item)

        // 如果是多itemType必须设置injectorFinder
        // 根据item返回viewType（实际上adapter内部viewType就是布局的layoutId，所以直接返回layoutId就行）
        injectorFinder { item, _, _ ->
            if ((item as OnePiece).isBigType) R.layout.item_big else R.layout.item_normal
        }

        itemClickListener { index, _ ->
            showToast("click position : $index")
        }
        itemLongClickListener { index, _ ->
            remove(index)
        }

        // diff
        autoDiff()
   }

   recyclerView.adapter = adapter
```
#### 高级功能

- 包含功能：header、footer、empty（error实际上也是一种特殊的empty）和load more
- 此功能是采用ConcatAdapter实现，各功能分别是单独的adapter，互不影响，也不影响内容adapter。
- 由于ConcatAdapter是final的，所以只能采用包装的方式来做多adapter的管理，返回一个SlimAdapterWrapper对象

```kotlin

    // 内容adapter
    val slimAdapter = ...

    // 通过SlimAdapterWrapper扩展，header、footer、empty和load more功能
    val wrapper = slimAdapter.exWrapper(context) {

        addHeader(header1)
        addHeader(header2)
        // footer和load more不能同时存在
        // addFooter(footer)

        // empty将自动触发，不需要手动显示，errorView显示可以手动使用wrapper.showErrorView显示
        emptyView(empty, errorView)

        // enableLoadMore时可以传入自定义footer
        enableLoadMore(object : LoadMoreListener {
            override fun onLoadMore() {
                loadMore()
            }
        })
    }

    recyclerView.adapter = wrapper.getAdapter()

```
### 重要： SlimAdapter和扩展的SlimAdapterWrapper都实现了DataOperator接口用来更新数据，如使用了高级功能，请直接使用SlimAdapterWrapper来更新数据

#### DataOperator接口参考
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