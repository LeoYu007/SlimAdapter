package com.mathew.slimadapter.sample;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mathew.slimadapter.sample.entity.OnePiece;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yu
 * @date 2019/4/15
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        initView();
        loadData();
    }


    protected void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(initLayoutManager());
        recyclerView.setAdapter(createAdapter());
    }

    protected RecyclerView.LayoutManager initLayoutManager() {
        return new LinearLayoutManager(this);
    }

    protected int getLayoutId() {
        return R.layout.layout_list;
    }

    protected void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    protected abstract RecyclerView.Adapter<? extends RecyclerView.ViewHolder> createAdapter();

    protected List<OnePiece> loadData() {
        List<OnePiece> data = new ArrayList<>();
        data.add(new OnePiece("我是要做海贼王的男人", R.mipmap.ic_lufei, false));
        data.add(new OnePiece("路痴路痴路痴", R.mipmap.ic_suolong, false));
        data.add(new OnePiece("haha~~", R.mipmap.ic_big2, true));
        data.add(new OnePiece("色河童色河童色河童", R.mipmap.ic_shanzhi, false));
        data.add(new OnePiece("我是要做海贼王的男人", R.mipmap.ic_lufei, false));
        data.add(new OnePiece("haha~~~", R.mipmap.ic_big3, true));
        data.add(new OnePiece("路痴路痴路痴", R.mipmap.ic_suolong, false));
        data.add(new OnePiece("haha~", R.mipmap.ic_big1, true));
        data.add(new OnePiece("色河童色河童色河童", R.mipmap.ic_shanzhi, false));
        data.add(new OnePiece("haha~", R.mipmap.ic_big1, true));
        return data;
    }
}
