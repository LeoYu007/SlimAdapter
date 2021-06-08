package com.mathew.slimadapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.util.isNotEmpty
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mathew.slimadapter.core.*
import com.mathew.slimadapter.databinding.DataBindingInjector
import com.mathew.slimadapter.diff.DefaultDiffCallback
import com.mathew.slimadapter.diff.SlimDiffUtil


interface AttachToRecyclerViewListener {
    fun onAttachedToRecyclerView(recyclerView: RecyclerView)
    fun onDetachedFromRecyclerView(recyclerView: RecyclerView)
}

interface AttachToWindowListener {
    fun onAttachedToWindow(holder: ViewHolder)
    fun onDetachedFromWindow(holder: ViewHolder)
}

/**
 * @author yuli
 * @date 2021/5/20
 * @description SlimAdapter
 */
open class SlimAdapter<T> : AbsAdapter<T>() {

    /**
     * key: viewType (实际上直接使用的layoutId作为viewType)    value: [ViewInjector]
     */
    private val viewInjectors by lazy { SparseArray<ViewInjector<T>>() }
    private var onItemClickListener: OnItemClickListener<T>? = null
    private var onItemLongClickListener: OnItemLongClickListener<T>? = null
    private var injectorFinder: InjectorFinder? = null
    private val attachRecyclerViewListener by lazy { arrayListOf<AttachToRecyclerViewListener>() }
    private val attachWindowListener by lazy { arrayListOf<AttachToWindowListener>() }

    fun addOnAttachRecyclerViewListener(l: AttachToRecyclerViewListener) =
        attachRecyclerViewListener.add(l)

    fun addOnAttachWindowListener(l: AttachToWindowListener) = attachWindowListener.add(l)
    fun clearOnAttachWindowListener() = attachWindowListener.clear()
    fun clearOnAttachRecyclerViewListener() = attachRecyclerViewListener.clear()

    override fun getItemViewType(position: Int): Int {
        require(viewInjectors.isNotEmpty()) {
            "No view type registered."
        }

        // 注册ViewInjector时，key保存的就是layoutId
        // 如果只有1个viewType，直接返回layoutId
        // 否则通过injectorFinder获取返回的layoutId，也通过layoutId获取到对应的ViewInjector

        if (viewInjectors.size() > 1) {
            // 注册了多个ViewInjector
            require(injectorFinder != null) {
                "Multiple view types are registered. You must set a injectorFinder"
            }
            return injectorFinder!!.layoutId(getItem(position)!!, position, itemCount)
        }

        return viewInjectors.keyAt(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 这里viewType就是layoutId
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        val holder = createViewHolder(itemView, viewInjectors[viewType])

        setupItemClickListener(holder)
        setupItemLongClickListener(holder)
        return holder
    }

    protected open fun createViewHolder(itemView: View, injector: ViewInjector<*>): ViewHolder {
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        val injector = viewInjectors[viewType]
        requireNotNull(injector) {
            "viewType出错，注册多个ViewInjector时，必须注册injectorFinder并且返回正确的layoutId"
        }

        injector.bind(holder, getItem(position), position)
    }

    private fun setupItemClickListener(viewHolder: ViewHolder) {
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            onItemClickListener?.invoke(position, mDataSet[position])
        }
    }

    private fun setupItemLongClickListener(viewHolder: ViewHolder) {
        viewHolder.itemView.setOnLongClickListener {
            val position = viewHolder.bindingAdapterPosition
            onItemLongClickListener?.invoke(position, mDataSet[position])
            true
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        attachRecyclerViewListener.forEach {
            it.onDetachedFromRecyclerView(recyclerView)
        }
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        attachRecyclerViewListener.forEach {
            it.onAttachedToRecyclerView(recyclerView)
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        attachWindowListener.forEach {
            it.onAttachedToWindow(holder)
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        attachWindowListener.forEach {
            it.onDetachedFromWindow(holder)
        }
        super.onViewDetachedFromWindow(holder)
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    open fun register(injector: ViewInjector<T>) {
        // 直接使用layoutId当做viewType
        viewInjectors.put(injector.layoutId, injector)
    }

    fun register(
        @LayoutRes layoutId: Int,
        block: (holder: ViewHolder, item: T, position: Int) -> Unit
    ) {
        register(object : ViewInjector<T>(layoutId) {
            override fun bind(holder: ViewHolder, item: T, position: Int) {
                block.invoke(holder, item, position)
            }
        })
    }

    fun <B : ViewDataBinding> registerDataBind(
        @LayoutRes layoutId: Int,
        block: (binding: B, item: T, position: Int) -> Unit
    ) {
        register(object : DataBindingInjector<T, B>(layoutId, 0) {
            override fun onBind(binding: B, item: T, position: Int) {
                block(binding, item, position)
            }
        })
    }

    fun <B : ViewDataBinding> registerDataBind(
        @LayoutRes layoutId: Int,
        variableId: Int
    ) {
        register(DataBindingInjector<T, B>(layoutId, variableId))
    }

    fun injectorFinder(finder: InjectorFinder) {
        this.injectorFinder = finder
    }

    fun injectorFinder(finder: (item: Any, position: Int, itemCount: Int) -> Int) {
        this.injectorFinder = object : InjectorFinder {
            override fun layoutId(item: Any, position: Int, itemCount: Int): Int {
                return finder(item, position, itemCount)
            }
        }
    }

    fun itemClickListener(listener: OnItemClickListener<T>) {
        this.onItemClickListener = listener
    }

    fun itemLongClickListener(listener: OnItemLongClickListener<T>) {
        this.onItemLongClickListener = listener
    }

    fun autoDiff(diffCallback: SlimDiffUtil.Callback? = DefaultDiffCallback()) {
        this.diffCallback = diffCallback
    }
}

