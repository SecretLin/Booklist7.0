package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.secret.booklist60.Adapter.GroundAdapter;
import com.example.secret.booklist60.DataBase.Ground;
import com.example.secret.booklist60.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class HitGroundActivity extends Activity {
    private GroundAdapter adapter;
    private RecyclerView rv;
    private TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hit_ground);

        rv = (RecyclerView) findViewById(R.id.rv);
        adapter = new GroundAdapter(this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        query();

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("热门内容");

    }

    private void query() {
        BmobQuery<Ground> query = new BmobQuery<>();
        query.order("-count");
        query.include("user,book");
        query.findObjects(this, new FindListener<Ground>() {
            @Override
            public void onSuccess(List<Ground> list) {
                adapter.bindData(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}
