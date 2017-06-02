package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.secret.booklist60.DataBase.Likes;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.ExitApplication;
import com.example.secret.booklist60.Adapter.LikeBookAdapter;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/2/24.
 */

public class LikeBookActivity extends Activity {
    RecyclerView rv;
    LinearLayoutManager layoutManager;
    LikeBookAdapter adapter;
    ImageButton btnBack;
    TextView tvActionbar;
    boolean isOthers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);

        tvActionbar = (TextView) findViewById(R.id.tvTitle);
        tvActionbar.setText("赞过的书");

        rv = (RecyclerView) findViewById(R.id.rv_Shoucang);
        adapter = new LikeBookAdapter(LikeBookActivity.this);
        rv.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                Intent intent = new Intent(LikeBookActivity.this, BookListDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Book", adapter.getItem(position).getBook());
                intent.putExtras(bundle);


                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        isOthers = getIntent().getBooleanExtra("isOthers",false);
        if (isOthers){
            MyUser myUser = (MyUser) getIntent().getSerializableExtra("user");
            isQuery(myUser);
        }else {
            MyUser myUser = BmobUser.getCurrentUser(this,MyUser.class);
            isQuery(myUser);
        }

        ExitApplication.getInstance().addActivities(this);
    }
    //查询书本，如果点赞为当前用户的，则获取出来
    //查询条件为：查询当前用户所点赞的，要注意去掉评论，因为点赞的数据库包括喜欢的评论和喜欢的书。
    public void isQuery(MyUser myUser) {
        //MyUser myUser = BmobUser.getCurrentUser(this,MyUser.class);

        BmobQuery<Likes> query = new BmobQuery<>();
        query.addWhereEqualTo("myUser",new BmobPointer(myUser));

        BmobQuery<Likes> query1 = new BmobQuery<>();
        query.addWhereDoesNotExists("comment");

        List<BmobQuery<Likes>> queryList = new ArrayList<>();
        queryList.add(query);
        queryList.add(query1);

        BmobQuery<Likes> andQuery = new BmobQuery<>();
        andQuery.and(queryList);
        andQuery.order("-createdAt");
        andQuery.include("book");
        andQuery.findObjects(LikeBookActivity.this, new FindListener<Likes>() {
            @Override
            public void onSuccess(List<Likes> list) {
                if (list.size()!=0){
                    adapter.bindData(list);
                    adapter.notifyDataSetChanged();
                    Log.i("count",String.valueOf(list.size()));
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
