package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secret.booklist60.Adapter.PinlunAdapter;
import com.example.secret.booklist60.DataBase.All_Comment;
import com.example.secret.booklist60.DataBase.Book;
import com.example.secret.booklist60.DataBase.Comment;
import com.example.secret.booklist60.DataBase.Ground;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.ExitApplication;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.RecycleViewDivider;
import com.example.secret.booklist60.RecyclerItemClickListener;
import com.example.secret.booklist60.utils.CommentMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/10/16.
 */
public class PingLunActivity extends Activity {
    PinlunAdapter adapter;
    public RecyclerView recyclerView;
    public ImageButton btnSendPinlun, btnBack, btnSharePinlun;
    public EditText etComment;
    RelativeLayout layout_Pinlun, layout_PinglunShare;
    public int who = 1;
    Comment comment;
    Switch sw_share;
    boolean isShare = false;
    public int itemPosition;
    TextView tvActionbar;

    public void onCreate(Bundle saveInstanceStated) {
        super.onCreate(saveInstanceStated);
        setContentView(R.layout.activity_pinlun);

        tvActionbar = (TextView) findViewById(R.id.tvTitle);
        tvActionbar.setText("评论");

        recyclerView = (RecyclerView) findViewById(R.id.rv_Pinlun);
        query();
        adapter = new PinlunAdapter(this);
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
        layout_Pinlun = (RelativeLayout) findViewById(R.id.layout_pinlun);

        etComment = (EditText) findViewById(R.id.etComment);
        etComment.setMaxHeight(400);


        btnSendPinlun = (ImageButton) findViewById(R.id.btnSendPinlun);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnSharePinlun = (ImageButton) findViewById(R.id.btnSharePinlun);
//

//
//
        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(etComment.getText().toString())) {
                    btnSendPinlun.setVisibility(View.VISIBLE);
                    btnSharePinlun.setVisibility(View.VISIBLE);
                } else {
                    btnSendPinlun.setVisibility(View.GONE);
                    btnSharePinlun.setVisibility(View.GONE);
                    layout_PinglunShare.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etComment.addTextChangedListener(textWatcher);
//


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ExitApplication.getInstance().addActivities(this);

//        //点击该评论，表明要回复该评论
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                itemPosition = position;
                who = 2;
                comment = adapter.getItem(position);
                etComment.requestFocus();
                etComment.setFocusable(true);
                InputMethodManager imm = (InputMethodManager) etComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                etComment.setHint("回复"+adapter.getItem(position).getMyUser().getUsername());


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        btnSharePinlun.getBackground().setAlpha(100);
        layout_PinglunShare = (RelativeLayout) findViewById(R.id.isSharePinglun);
        btnSharePinlun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_PinglunShare.setVisibility(layout_PinglunShare.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });
        sw_share = (Switch) findViewById(R.id.sw_share);

        sw_share.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isShare = true;
                }
                else {
                    isShare = false;
                }
            }
        });
        btnSendPinlun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyUser currentUser = BmobUser.getCurrentUser(PingLunActivity.this,MyUser.class);
                if (currentUser==null){
                    new AlertDialog.Builder(PingLunActivity.this).setMessage("登录后才可评论哦")
                            .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(PingLunActivity.this,LoginActivity.class));
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                    return;
                }

                if (who == 1) {//只是纯粹的发表评论
                    MyUser user = BmobUser.getCurrentUser(PingLunActivity.this, MyUser.class);
                    final Book book = (Book) getIntent().getSerializableExtra("Book");
                    String commentContent = etComment.getText().toString();
                    if (commentContent.trim().equals("")) {
                        return;
                    }
                    Comment comment = new Comment();
                    comment.setCommentcontent(commentContent);
                    comment.setBook(book);
                    comment.setMyUser(user);
                    comment.save(PingLunActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            toast("评论发表成功");

                            query();
                            if (isShare){
                                MyUser currentUser = BmobUser.getCurrentUser(PingLunActivity.this, MyUser.class);
                                Ground ground = new Ground();
                                ground.setContent(etComment.getText().toString());
                                ground.setType("comment");
                                ground.setUser(currentUser);
                                ground.setBook(book);
                                ground.save(PingLunActivity.this, new SaveListener() {
                                    @Override
                                    public void onSuccess() {
                                        toast("分享成功");
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        toast("分享失败，" + s);
                                    }
                                });
                            }
                            etComment.setText("");

                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.i("bmob", "失败：" + s);
                        }
                    });
                } else if (who == 2) {//回复第一条评论
                    MyUser review = BmobUser.getCurrentUser(PingLunActivity.this, MyUser.class);
                    final Comment commentContent = adapter.getItem(itemPosition);
                    String contentChild = etComment.getText().toString();
                    String content2 = commentContent.getCommentcontent();

                    All_Comment all_comment = new All_Comment();
                    all_comment.setUser1(review);
                    all_comment.setUser2(commentContent.getMyUser());
                    all_comment.setContent1(contentChild);
                    all_comment.setContent2(content2);
                    all_comment.setComment(commentContent);
                    all_comment.setWho(2);
                    all_comment.save(PingLunActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            toast("评论发表成功");
                            sendNewComment(commentContent.getMyUser());
                            Log.i("回复评论","成功");
                            adapter.query(itemPosition,null);
                            etComment.setText("");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.e("bmob", "失败：" + s);
                        }
                    });

                }
                else if (who == 3){//回复别人的评论
                    final MyUser review = BmobUser.getCurrentUser(PingLunActivity.this, MyUser.class);
                    Comment commentContent = adapter.getItem(itemPosition);
                    String contentChild = etComment.getText().toString();

                    All_Comment all_comment = new All_Comment();
                    all_comment.setUser1(review);
                    all_comment.setContent1(contentChild);
                    final MyUser user2 = adapter.adapterList.get(itemPosition).getItem(adapter.childPosition).getUser1();
                    all_comment.setUser2(user2);
                    all_comment.setContent2(adapter.adapterList.get(itemPosition).getItem(adapter.childPosition).getContent1());
                    all_comment.setComment(commentContent);
                    all_comment.setWho(3);
                    all_comment.save(PingLunActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            toast("评论发表成功");
                            sendNewComment(user2);
                            adapter.query(itemPosition,null);
                            etComment.setText("");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            toast("评论发表失败,"+s);
                        }
                    });

//
                }
            }
        });
    }


    public void query() {

        Book book = (Book) getIntent().getSerializableExtra("Book");
        System.out.println(book.getName());
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("book", new BmobPointer(book));
        query.include("book,myUser");
        query.order("-createdAt");
        query.findObjects(PingLunActivity.this, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                if (list != null) {
                    adapter.bindData(list);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    public void sendNewComment(MyUser myUser){
        BmobIMUserInfo info;
        if (myUser.getHead()!=null){
            info = new BmobIMUserInfo(myUser.getObjectId(),myUser.getUsername(),myUser.getHead().getUrl());
        }
        else {

            info = new BmobIMUserInfo(myUser.getObjectId(),myUser.getUsername(),null);
        }

        BmobIM.getInstance().updateUserInfo(info);
        BmobIMConversation conversation = BmobIM.getInstance().startPrivateConversation(info,true,null);
        BmobIMConversation conversation1 = BmobIMConversation.obtain(BmobIMClient.getInstance(),conversation);//创建新的会话
        CommentMessage message = new CommentMessage();
        Map<String,Object> map =new HashMap<>();

        message.setExtraMap(map);
        conversation1.sendMessage(message, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                if (e==null){
                    Toast.makeText(PingLunActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PingLunActivity.this,"评论失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}
