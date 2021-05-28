package com.yu1tiao.slimadapter.sample

import android.view.View
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.yu1tiao.slimadapter.SlimAdapter
import com.yu1tiao.slimadapter.SlimAdapterEx
import com.yu1tiao.slimadapter.core.ViewHolder
import com.yu1tiao.slimadapter.loadmore.DefaultLoadMoreFooter
import com.yu1tiao.slimadapter.loadmore.LoadMoreListener
import com.yu1tiao.slimadapter.loadmore.MoreLoader
import com.yu1tiao.slimadapter.sample.entity.OnePiece

class MainActivity : BaseActivity() {
    private lateinit var adapterEx: SlimAdapterEx<OnePiece>

    override fun initPage() {
        findViewById<View>(R.id.btn_clear).setOnClickListener {
            adapterEx.clear()
        }
        findViewById<View>(R.id.btn_refresh).setOnClickListener {
            val data = loadData()
            adapterEx.updateData(data)
            adapterEx.loadMoreCompleted()
        }

        val data = loadData()
        adapterEx.updateData(data)
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

        adapterEx = SlimAdapterEx(
            slimAdapter,
            headers = arrayOf(header1, header2),
//            footers = arrayOf(footer),
            emptyView = empty,
            moreLoader = MoreLoader(object : LoadMoreListener {
                override fun onLoadMore() {
                    loadMore()
                }
            }, DefaultLoadMoreFooter(this))
        )

        return adapterEx.buildAdapter()
    }

    private fun loadMore() {
        recyclerView.postDelayed({
            when ((System.currentTimeMillis() % 5).toInt()) {
                in 1..2 -> {
                    adapterEx.loadMoreError()
                }
                0 -> {
                    adapterEx.noMore()
                }
                else -> {
                    val data = loadData()
                    adapterEx.addAll(data)
                    adapterEx.loadMoreCompleted()
                }
            }
        }, 1500)
    }

    private fun bindData2View(holder: ViewHolder, item: OnePiece) {
        holder.setText(R.id.tvDesc, item.desc)
        holder.getImageView(R.id.ivImage)?.load(item.imageRes)
    }
}