package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.secret.booklist60.Adapter.HistoryRecordAdapter;
import com.example.secret.booklist60.DataBase.History;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.ExitApplication;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.RecyclerItemClickListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
/*
   显示全部历史记录的界面
 */

public class HistoryRecordActivity extends Activity {
    RecyclerView rv;
    LinearLayoutManager layoutManager;
    HistoryRecordAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    TextView tvActionbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_record);

        tvActionbar = (TextView) findViewById(R.id.tvTitle);
        tvActionbar.setText("历史记录");

        rv = (RecyclerView) findViewById(R.id.rv_History);
        adapter = new HistoryRecordAdapter(HistoryRecordActivity.this);
        rv.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                Intent intent = new Intent(HistoryRecordActivity.this, BookDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Book", adapter.getItem(position).getBook());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        query();
        //下拉刷新
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_History);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
                refreshLayout.setRefreshing(false);
            }
        });
        //返回键
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ExitApplication.getInstance().addActivities(this);
    }
    public void query() {
        MyUser myUser = BmobUser.getCurrentUser(HistoryRecordActivity.this,MyUser.class);

        BmobQuery<History> query = new BmobQuery<>();
        query.addWhereEqualTo("myUser",new BmobPointer(myUser));
        query.order("-createdAt");
        query.include("book");
        query.findObjects(HistoryRecordActivity.this, new FindListener<History>() {
            @Override
            public void onSuccess(List<History> list) {
                if (list.size()!=0){
                    adapter.bindData(list);
                    adapter.notifyDataSetChanged();
                }
                else {
                    rv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}
