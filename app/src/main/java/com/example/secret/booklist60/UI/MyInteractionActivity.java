package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.secret.booklist60.R;
/*
   我的互动界面
 */
public class MyInteractionActivity extends Activity {
    private ImageButton btnReceiveComment,btnBack,btnLikeBooks,btnMypost;
    private TextView tvActionbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_interaction);

        tvActionbar = (TextView) findViewById(R.id.tvTitle);
        tvActionbar.setText("我的互动");

        btnReceiveComment = (ImageButton) findViewById(R.id.btnreceiveComment);
        btnReceiveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyInteractionActivity.this,ReceiveComment.class));
            }
        });

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnLikeBooks = (ImageButton) findViewById(R.id.btnlikedbook);
        btnLikeBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyInteractionActivity.this, LikeBookActivity.class));
            }
        });

        btnMypost = (ImageButton) findViewById(R.id.btnmyPost);
        btnMypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyInteractionActivity.this, MyPostActivity.class);
                intent.putExtra("isCurrentUser",true);
                startActivity(intent);

            }
        });
    }
}
