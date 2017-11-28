package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.Book;
import com.example.secret.booklist60.DataBase.Ground;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class AddShareActivity extends Activity {
    private EditText etShare;
    private ImageButton btnSelectBook,btnBack;
    private RelativeLayout layout_book;
    private ImageView ivCover;
    private TextView tvBookname, tvAuthor, tvSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_share);
        etShare = (EditText) findViewById(R.id.etQuestion);
        btnSelectBook = (ImageButton) findViewById(R.id.btnSelectBook);

        layout_book = (RelativeLayout) findViewById(R.id.layout_book);
        ivCover = (ImageView) findViewById(R.id.ivCover);
        tvBookname = (TextView) findViewById(R.id.tvBookname);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        etShare.requestFocus();
        etShare.setFocusable(true);


        btnSelectBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddShareActivity.this, SelectBookActivity.class);

                startActivityForResult(intent, 1);
                AddShareActivity.this.overridePendingTransition(R.anim.push_bottom_in, 0);
            }
        });


        tvSend = (TextView) findViewById(R.id.tvSend);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etShare.getText().toString().trim())) {
                    return;
                }
                MyUser currentUser = BmobUser.getCurrentUser(AddShareActivity.this, MyUser.class);
                Ground ground = new Ground();
                ground.setUser(currentUser);
                ground.setType("share");
                ground.setContent(etShare.getText().toString());
                ground.save(AddShareActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(AddShareActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(AddShareActivity.this, "发送失败，" + s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==-1){
            final Book book = (Book) data.getExtras().getSerializable("book");

            layout_book.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load(book.getCover()).into(ivCover);
            tvBookname.setText(book.getName());
            tvAuthor.setText(book.getAuthor());
//        String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
//        System.out.println(result+"................................");

            tvSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(etShare.getText().toString().trim())) {
                        return;
                    }
                    MyUser currentUser = BmobUser.getCurrentUser(AddShareActivity.this, MyUser.class);
                    Ground ground = new Ground();
                    ground.setUser(currentUser);
                    ground.setType("share");
                    ground.setContent(etShare.getText().toString());
                    ground.setBook(book);
                    ground.save(AddShareActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            finish();
                            Toast.makeText(AddShareActivity.this, "发送成功", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(AddShareActivity.this, "发送失败，" + s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }

    }

}
