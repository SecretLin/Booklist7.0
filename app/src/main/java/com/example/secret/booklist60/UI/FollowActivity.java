package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.secret.booklist60.Adapter.FansAdapter;
import com.example.secret.booklist60.Adapter.FollowAdapter;
import com.example.secret.booklist60.DataBase.Follow_Fans;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.RecycleViewDivider;
import com.example.secret.booklist60.RecyclerItemClickListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
/*
   关注的界面
 */
public class FollowActivity extends Activity {
    public RecyclerView rv;
    private LinearLayoutManager layoutManager;
    private FollowAdapter adapter;
    ImageButton btnBack;
    TextView tvActionbar;
    boolean isCurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        tvActionbar = (TextView) findViewById(R.id.tvTitle);
        tvActionbar.setText("关注");

        rv = (RecyclerView) findViewById(R.id.rvFollow);
        layoutManager = new LinearLayoutManager(this);
        adapter = new FollowAdapter(this);

        isCurrentUser = getIntent().getBooleanExtra("isCurrentUser",false);
        if (isCurrentUser){
            MyUser currentUser = BmobUser.getCurrentUser(this,MyUser.class);
            query(currentUser);
        }else {
            MyUser user = (MyUser) getIntent().getSerializableExtra("user");
            query(user);
        }


        rv.setAdapter(adapter);
        rv.setLayoutManager(layoutManager);
        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();

                bundle.putSerializable("user",adapter.getItem(position));
                Intent intent = new Intent(FollowActivity.this,UserDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        rv.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL));
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    //查询关注的人，如果粉丝为当前用户，则将该列获取出来
    private void query(MyUser user) {
//        MyUser currentUser = BmobUser.getCurrentUser(this,MyUser.class);
        BmobQuery<Follow_Fans> query = new BmobQuery<>();
        query.include("Follower");
        query.order("-createdAt");
        query.addWhereEqualTo("Fans",new BmobPointer(user));
        query.findObjects(this, new FindListener<Follow_Fans>() {
            @Override
            public void onSuccess(List<Follow_Fans> list) {
                if (list!=null){
                    System.out.println("count:"+list.size());
                    adapter.bindData(list);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(int i, String s) {
                System.out.println("查询失败"+s);
            }
        });
    }
}
