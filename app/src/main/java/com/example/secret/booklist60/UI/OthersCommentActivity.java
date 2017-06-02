package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secret.booklist60.Adapter.OtherCommentAdapter;
import com.example.secret.booklist60.DataBase.Comment;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

public class OthersCommentActivity extends Activity {
    private RecyclerView rv;
    private OtherCommentAdapter adapter;

    private ImageButton btnBack;
    private TextView tvActionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_comment);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvActionbar = (TextView) findViewById(R.id.tvTitle);
        tvActionbar.setText("他的评论");

        rv = (RecyclerView) findViewById(R.id.rv);
        adapter = new OtherCommentAdapter(this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        query();
    }

    private void query() {
        MyUser user = (MyUser) getIntent().getSerializableExtra("user");

        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("myUser",new BmobPointer(user));
        query.include("myUser,book");
        query.order("-createdAt");
        query.findObjects(this, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                if (list!=null){
                    adapter.bindData(list);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(OthersCommentActivity.this,"查找不到，"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
