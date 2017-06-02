package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secret.booklist60.Adapter.ChatAdapter;
import com.example.secret.booklist60.DataBase.Follow_Fans;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.ExitApplication;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.event.UpdateConversationEvent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.greenrobot.event.EventBus;

public class ChatActivity extends Activity {
    private RecyclerView rv;
    private ChatAdapter adapter;
    private LinearLayoutManager layoutManager;
    private TextView tvTitle;
    BmobIMConversation c;
    EditText etMessage;
    ImageButton btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
       //获取实例对话
        c = BmobIMConversation.obtain(BmobIMClient.getInstance(),
                (BmobIMConversation) getIntent().getSerializableExtra("c"));


        rv = (RecyclerView) findViewById(R.id.rv_Chat);
        adapter = new ChatAdapter(ChatActivity.this);
        rv.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        etMessage = (EditText) findViewById(R.id.etComment);
        etMessage.setMaxHeight(400);
        //设置标题
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(c.getConversationTitle());
        //加载对话记录
        query(null);

        btnSend = (ImageButton) findViewById(R.id.btnSendMessage);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(1);

        //监听发送消息的EditText,当不输入文字时，发送按钮不出现
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(etMessage.getText().toString())) {
                    btnSend.setVisibility(View.VISIBLE);
                } else {
                    btnSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etMessage.addTextChangedListener(textWatcher);
        etMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layoutManager.scrollToPositionWithOffset(adapter.getItemCount()-1,0);
                    }
                }, 100);
            }
        });


        ExitApplication.getInstance().addActivities(this);
    }

    public void onEventMainThread(UpdateConversationEvent event) {
        if (event.getMsg() == "update") {
            query(null);
        }
    }


    private void sendMessage() {
        String text = etMessage.getText().toString();
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(text);

        c.sendMessage(msg, listener);

    }

    public MessageSendListener listener = new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
        }

        @Override
        public void onStart(final BmobIMMessage msg) {
            super.onStart(msg);
            adapter.addMessage(msg);
            etMessage.setText("");

        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            adapter.notifyDataSetChanged();
            etMessage.setText("");
            if (e != null) {
                toast("send error:" + e.getMessage());
                System.out.println("send error:" + e.getMessage());
            }
            layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
        }
    };

    public void query(final BmobIMMessage msg) {
        final MyUser currentUser = BmobUser.getCurrentUser(this,MyUser.class);
        c.queryMessages(msg, 100, new MessagesQueryListener() {
            @Override
            public void done(final List<BmobIMMessage> msglist, BmobException e) {
                if (e == null) {

                    if (null != msglist && msglist.size() > 0) {
                        if (currentUser.isReceiveStrangerMsg()){//如果可以接受到陌生人的消息，那么全部消息都会加载出来
                            adapter.bindData(msglist);
                            for (BmobIMMessage message : msglist) {
                                c.updateReceiveStatus(message);

                            }

                            layoutManager.scrollToPositionWithOffset(msglist.size() - 1, 0);
                        }
                        else {//不可以接受到陌生人的消息

                            for (BmobIMMessage message:msglist){
                                BmobQuery<MyUser> userBmobQuery = new BmobQuery<MyUser>();
                                userBmobQuery.addWhereEqualTo("objectId",message.getFromId());//获取发送人的objectid
                                /*
                                以下在做查询
                                如果发送人既是粉丝又是关注者，就会把他发的消息显示出来
                                 */
                                BmobQuery<Follow_Fans> query = new BmobQuery<>();
                                query.addWhereMatchesQuery("Follower","_User",userBmobQuery);

                                BmobQuery<Follow_Fans> query1 = new BmobQuery<>();
                                query1.addWhereMatchesQuery("Fans","_User",userBmobQuery);

                                List<BmobQuery<Follow_Fans>> list = new ArrayList<>();
                                list.add(query);
                                list.add(query1);

                                BmobQuery<Follow_Fans> andQuery = new BmobQuery<>();
                                andQuery.and(list);
                                andQuery.findObjects(ChatActivity.this, new FindListener<Follow_Fans>() {
                                    @Override
                                    public void onSuccess(List<Follow_Fans> list) {
                                        if (!list.isEmpty()){
                                            adapter.bindData(msglist);
                                            for (BmobIMMessage message : msglist) {
                                                c.updateReceiveStatus(message);

                                            }

                                            layoutManager.scrollToPositionWithOffset(msglist.size() - 1, 0);
                                        }
                                    }

                                    @Override
                                    public void onError(int i, String s) {

                                    }
                                });
                            }
                        }



                    }
                } else {
                    System.out.println("msg error:" + e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(ChatActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            finish();
            return true;

        } else
            return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void toast(String txt) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }

}
