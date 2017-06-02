package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class AddQuestionActivity extends Activity {
    private EditText etQuestion;
    private ImageButton btnSelectBook,btnBack;
    private RelativeLayout layout_book;
    private ImageView ivCover;
    private TextView tvBookname, tvAuthor, tvSend,tvActionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        tvActionbar = (TextView) findViewById(R.id.tvTitle);
        tvActionbar.setText("提问");

        etQuestion = (EditText) findViewById(R.id.etQuestion);
        btnSelectBook = (ImageButton) findViewById(R.id.btnSelectBook);

        layout_book = (RelativeLayout) findViewById(R.id.layout_book);
        ivCover = (ImageView) findViewById(R.id.ivCover);
        tvBookname = (TextView) findViewById(R.id.tvBookname);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        etQuestion.requestFocus();


        btnSelectBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddQuestionActivity.this, SelectBookActivity.class);

                startActivityForResult(intent, 1);
                AddQuestionActivity.this.overridePendingTransition(R.anim.push_bottom_in, 0);
            }
        });


        tvSend = (TextView) findViewById(R.id.tvSend);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etQuestion.getText().toString().trim())){
                    return;
                }
                MyUser currentUser = BmobUser.getCurrentUser(AddQuestionActivity.this, MyUser.class);
                Ground ground = new Ground();
                ground.setUser(currentUser);
                ground.setType("question");
                ground.setContent(etQuestion.getText().toString());
                ground.save(AddQuestionActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(AddQuestionActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                        AddQuestionActivity.this.finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(AddQuestionActivity.this, "发送失败，" + s, Toast.LENGTH_SHORT).show();
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

        Log.i("ResultCode",String.valueOf(resultCode));
        Log.i("RequestCode",String.valueOf(requestCode));

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
                    if (TextUtils.isEmpty(etQuestion.getText().toString().trim())){
                        return;
                    }
                    MyUser currentUser = BmobUser.getCurrentUser(AddQuestionActivity.this, MyUser.class);
                    Ground ground = new Ground();
                    ground.setUser(currentUser);

                    ground.setContent(etQuestion.getText().toString());
                    ground.setBook(book);
                    ground.save(AddQuestionActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            finish();
                            Toast.makeText(AddQuestionActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(AddQuestionActivity.this, "发送失败，" + s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }


    }

}
