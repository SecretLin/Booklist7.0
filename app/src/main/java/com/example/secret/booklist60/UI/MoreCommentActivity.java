package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.Adapter.MoreCommentAdapter;
import com.example.secret.booklist60.DataBase.All_Comment;
import com.example.secret.booklist60.DataBase.Comment;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.R;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class MoreCommentActivity extends Activity {

    RecyclerView rv;
    MoreCommentAdapter adapter;
    EditText etComment;
    ImageButton btnSendPinlun;
    public int who = 1;
    TextView tvName1,tvhuifu,tvUsername,tvTime,tvGoundContent,tvBookname,tvAuthor,tvActionbar;
    All_Comment all_comment = new All_Comment();
    CircleImageView ivHead;
    ImageView ivCover;
    MyUser user2 = null;
    String content2 = null;
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_comment);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvActionbar = (TextView) findViewById(R.id.tvTitle);


        final Comment comment = (Comment) getIntent().getSerializableExtra("comment");
        //用户信息以及评论内容
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvGoundContent = (TextView) findViewById(R.id.tvContent);
        ivHead = (CircleImageView) findViewById(R.id.ivHead);
        tvUsername.setText(comment.getMyUser().getUsername());
        tvTime.setText(comment.getUpdatedAt());
        tvGoundContent.setText(comment.getCommentcontent());
        if (comment.getMyUser().getHead()!=null){
            Glide.with(this).load(comment.getMyUser().getHead().getUrl()).into(ivHead);
        }

        //书本信息
        ivCover = (ImageView) findViewById(R.id.ivCover);
        tvBookname = (TextView) findViewById(R.id.tvBookname);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        Glide.with(this).load(comment.getBook().getCover()).into(ivCover);
        tvBookname.setText(comment.getBook().getName());
        tvAuthor.setText(comment.getBook().getAuthor());


        etComment = (EditText) findViewById(R.id.etComment);
        etComment.setMaxHeight(400);
        btnSendPinlun = (ImageButton) findViewById(R.id.btnSendMessage);
        //下面的评论框设置
        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(etComment.getText().toString())){
                    btnSendPinlun.setVisibility(View.VISIBLE);

                }
                else {
                    btnSendPinlun.setVisibility(View.GONE);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etComment.addTextChangedListener(textWatcher);
        btnSendPinlun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (who == 1) {//没有回复别人，只对发现页的内容进行评论
                    MyUser user = BmobUser.getCurrentUser(MoreCommentActivity.this, MyUser.class);

                    String commentContent = etComment.getText().toString();

                    All_Comment all_comment = new All_Comment();
                    all_comment.setUser1(user);
                    all_comment.setContent1(commentContent);
                    all_comment.setUser2(comment.getMyUser());
                    all_comment.setContent2(comment.getCommentcontent());
                    all_comment.setComment(comment);
                    all_comment.setWho(1);
                    all_comment.save(MoreCommentActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            toast("评论发表成功");
                            sendNewComment(comment.getMyUser());
                            query();
                            etComment.setText("");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            toast("评论发表失败，"+s);
                        }
                    });

                }
                else if(who == 2){//回复别人的评论
                    final MyUser user = BmobUser.getCurrentUser(MoreCommentActivity.this,MyUser.class);
                    String contentscontent = etComment.getText().toString();

                    All_Comment all_comment = new All_Comment();
                    all_comment.setUser1(user);
                    all_comment.setContent1(contentscontent);
                    all_comment.setUser2(user2);
                    all_comment.setContent2(content2);
                    all_comment.setComment(comment);
                    all_comment.setWho(2);
                    all_comment.save(MoreCommentActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            toast("评论发表成功");
                            sendNewComment(user2);
                            query();
                            etComment.setText("");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            toast("评论发表失败,"+s);
                        }
                    });
                }
            }
        });

        query();
        rv = (RecyclerView) findViewById(R.id.rv);
        adapter = new MoreCommentAdapter(this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, rv,new RecyclerItemClickListener.OnItemClickListener(){
            public void onItemClick(View view, int position) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                who = 2;
                all_comment= adapter.getItem(position);
                etComment.setHint("回复"+all_comment.getUser1().getUsername());
                user2 = all_comment.getUser1();
                content2 = all_comment.getContent1();
                etComment.requestFocus();
                etComment.setFocusable(true);
                InputMethodManager imm = (InputMethodManager) etComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.item_groundpinlun,null);
        tvName1 = (TextView) layout.findViewById(R.id.pName1);
        tvhuifu =(TextView)layout.findViewById(R.id.tvhuifu);
        AssetManager mgr = getAssets();//得到AssetManager
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/msyh.ttc");//根据路径得到Typeface
        tvActionbar.setTypeface(tf);
        tvAuthor.setTypeface(tf);
        tvBookname.setTypeface(tf);
        tvGoundContent.setTypeface(tf);
        tvhuifu.setTypeface(tf);
        tvName1.setTypeface(tf);
        tvTime.setTypeface(tf);
        tvUsername.setTypeface(tf);
    }
    public void toast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
    public void query() {

        final Comment comment = (Comment) getIntent().getSerializableExtra("comment");
        BmobQuery<All_Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("comment", new BmobPointer(comment));
        // query.setLimit(3);
        query.include("comment,User2,User1");
        query.order("createdAt");
        query.findObjects(MoreCommentActivity.this, new FindListener<All_Comment>() {
            @Override
            public void onSuccess(List<All_Comment> list) {

                if (list!=null){
                    adapter.bindData(list);
                    adapter.notifyDataSetChanged();
                    tvActionbar.setText(list.size()+"条评论");
                }
                else {

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
                    toast("评论成功");
                }
                else {
                    toast("评论失败"+e.getMessage());
                }
            }
        });
    }

}
