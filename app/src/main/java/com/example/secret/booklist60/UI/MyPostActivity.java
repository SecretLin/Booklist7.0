package com.example.secret.booklist60.UI;

import android.app.Activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.secret.booklist60.Adapter.MyPostAdapter;
import com.example.secret.booklist60.DataBase.Ground;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

public class MyPostActivity extends Activity {
    private RecyclerView rv;
    private MyPostAdapter adapter;
    private TextView tvActionbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        tvActionbar = (TextView) findViewById(R.id.tvTitle);


        rv = (RecyclerView) findViewById(R.id.rv);

        boolean isCurrentUser = getIntent().getBooleanExtra("isCurrentUser",true);
        System.out.println("isCurrentUser:"+isCurrentUser);
        if (isCurrentUser){
            MyUser currentUser = BmobUser.getCurrentUser(this,MyUser.class);
            query(currentUser);
            tvActionbar.setText("我的发布");
        }
        else {
            MyUser user = (MyUser) getIntent().getSerializableExtra("user");
            query(user);
            System.out.println("isCurrentUser:"+user.getUsername());
            tvActionbar.setText("他的发布");
        }
        
        adapter = new MyPostAdapter(this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new RecycleViewDivider(this,  LinearLayoutManager.VERTICAL,
                R.drawable.decoration));
    }

    private void query(MyUser user) {

        System.out.println("query isCurrentUser:"+user.getUsername());
        BmobQuery<Ground> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user",new BmobPointer(user));

        BmobQuery<Ground> query = new BmobQuery<>();
        query.addWhereNotEqualTo("type","comment");

        List<BmobQuery<Ground>> list = new ArrayList<>();
        list.add(query);
        list.add(query1);

        BmobQuery<Ground> andQuery = new BmobQuery<>();
        andQuery.and(list);
        andQuery.order("-createdAt");
        andQuery.include("book,user");
        andQuery.findObjects(this, new FindListener<Ground>() {
            @Override
            public void onSuccess(List<Ground> list) {
                adapter.bindList(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                 Log.e("myPost.error",s);
            }
        });




    }
}
