package com.yu1tiao.slimadapter.sample

import android.view.View
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.yu1tiao.slimadapter.SlimAdapter
import com.yu1tiao.slimadapter.addFooter
import com.yu1tiao.slimadapter.addHeader
import com.yu1tiao.slimadapter.core.ViewHolder
import com.yu1tiao.slimadapter.sample.entity.OnePiece

class MainActivity : BaseActivity() {
    private lateinit var adapter: ConcatAdapter
    private lateinit var slimAdapter: SlimAdapter<OnePiece>

    override fun initPage() {
        loadData()
        slimAdapter.addAll(data)
    }

    override fun createAdapter(): RecyclerView.Adapter<out RecyclerView.ViewHolder> {
        slimAdapter = SlimAdapter<OnePiece>().apply {
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
        adapter = ConcatAdapter()
        adapter.addAdapter(slimAdapter)
        val header = View.inflate(this, R.layout.item_header, null)
        val footer = View.inflate(this, R.layout.item_footer, null)
        adapter.apply {
            addHeader(header)
            addHeader(header)
            addHeader(header)
            addFooter(footer)
        }
        return adapter
    }

    private fun bindData2View(holder: ViewHolder, item: OnePiece) {
        holder.setText(R.id.tvDesc, item.desc)
        holder.getImageView(R.id.ivImage)?.load(item.imageRes)
    }
}