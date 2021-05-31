package com.mathew.slimadapter.sample

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mathew.slimadapter.SlimAdapter
import com.mathew.slimadapter.SlimAdapterWrapper
import com.mathew.slimadapter.core.ViewHolder
import com.mathew.slimadapter.ex.loadmore.LoadMoreListener
import com.mathew.slimadapter.sample.entity.OnePiece

class MainActivity : BaseActivity() {

    private lateinit var wrapper: SlimAdapterWrapper<OnePiece>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<View>(R.id.btn_clear).setOnClickListener {
            wrapper.clear()
        }
        findViewById<View>(R.id.btn_refresh).setOnClickListener {
            val data = loadData()
            wrapper.setNewData(data)
            wrapper.loadMoreCompleted()
        }

        val data = loadData()

        wrapper.setNewData(data)
    }

    override fun createAdapter(): RecyclerView.Adapter<out RecyclerView.ViewHolder> {
        val slimAdapter = SlimAdapter<OnePiece>().apply {
            register(R.layout.item_normal) { holder, item, _ ->
                bindData2View(holder, item)
            }
            register(R.layout.item_big) { holder, item, _ ->
                bindData2View(holder, item)
            }

            injectorFinder { item, _, _ ->
                if ((item as OnePiece).isBigType) R.layout.item_big else R.layout.item_normal
            }
            itemClickListener { index, _ ->
                showToast("click position : $index")
            }
            itemLongClickListener { index, _ ->
                remove(index)
            }
        }
        val empty = View.inflate(this, R.layout.empty_view, null)
        val header1 = View.inflate(this, R.layout.item_header, null)
        val header2 = View.inflate(this, R.layout.item_header, null)
        val footer = View.inflate(this, R.layout.item_footer, null)


        wrapper = SlimAdapterWrapper(this, slimAdapter).apply {
            addHeader(header1)
            addHeader(header2)
//            addFooter(footer)
            emptyView(empty)
            enableLoadMore(object : LoadMoreListener {
                override fun onLoadMore() {
                    loadMore()
                }
            })
        }
        return wrapper.getAdapter()
    }

    private fun loadMore() {
        recyclerView.postDelayed({
            when ((System.currentTimeMillis() % 5).toInt()) {
                in 1..2 -> {
                    wrapper.loadMoreError()
                }
                0 -> {
                    wrapper.noMore()
                }
                else -> {
                    val data = loadData()
                    wrapper.addAll(data)
                    wrapper.loadMoreCompleted()
                }
            }
        }, 1500)
    }

    private fun bindData2View(holder: ViewHolder, item: OnePiece) {
        holder.setText(R.id.tvDesc, item.desc)
        holder.getImageView(R.id.ivImage)?.load(item.imageRes)
    }
}