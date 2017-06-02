package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secret.booklist60.Adapter.ReceiveCommentAdapter;
import com.example.secret.booklist60.DataBase.All_Comment;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class ReceiveComment extends Activity {
    private RecyclerView rv;
    private ReceiveCommentAdapter adapter;
    private TextView tvActionbar;
    public View sendMessage;
    public EditText etComment;
    public ImageButton btnSend;
//    public boolean isSendMessage = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_comment);

        tvActionbar = (TextView) findViewById(R.id.tvTitle);
        tvActionbar.setText("我收到的评论");

        rv = (RecyclerView) findViewById(R.id.rv);
        adapter = new ReceiveCommentAdapter(this);
        query();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        sendMessage = findViewById(R.id.layout_sendmessage);

        btnSend = (ImageButton) findViewById(R.id.btnSendMessage);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etComment.requestFocus();
                All_Comment comment = new All_Comment();
                MyUser currenUser = BmobUser.getCurrentUser(ReceiveComment.this,MyUser.class);

                All_Comment itemComment = adapter.getItem(adapter.itemPosition);
                comment.setUser1(currenUser);
                comment.setContent1(etComment.getText().toString());
                comment.setUser2(itemComment.getUser1());
                comment.setContent2(itemComment.getContent1());
                if (itemComment.getComment()!=null){
                    comment.setComment(comment.getComment());
                }
                if (itemComment.getGround()!=null){
                    comment.setGround(itemComment.getGround());
                }
                comment.save(ReceiveComment.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        toast("回复成功");
                        etComment.setText("");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        toast("回复失败,"+s);
                    }
                });
            }
        });
        etComment = (EditText) findViewById(R.id.etComment);
        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(etComment.getText().toString())) {
                    btnSend.setVisibility(View.VISIBLE);
                } else {
                    btnSend.setVisibility(View.GONE);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etComment.addTextChangedListener(textWatcher);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(3);


    }

    public void toast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    private void query() {
        MyUser currentUser = BmobUser.getCurrentUser(this,MyUser.class);
        BmobQuery<All_Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("User2",new BmobPointer(currentUser));
        query.include("User1,User2,Comment");
        query.findObjects(this, new FindListener<All_Comment>() {
            @Override
            public void onSuccess(List<All_Comment> list) {
                adapter.bindData(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}
