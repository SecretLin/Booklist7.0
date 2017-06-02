package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.secret.booklist60.Adapter.MyPrivateMsgAdapter;
import com.example.secret.booklist60.DataBase.Conversation;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.RecycleViewDivider;
import com.example.secret.booklist60.RecyclerItemClickListener;
import com.example.secret.booklist60.event.UpdateConversationEvent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.greenrobot.event.EventBus;

/*
   私信界面
 */
public class MyPrivateConversationActivity extends Activity {
    private MyPrivateMsgAdapter adapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView rv;
    private ImageButton btnBack;
    private TextView tvActionbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_private_conversation);

        tvActionbar = (TextView) findViewById(R.id.tvTitle);
        tvActionbar.setText("我的私信");

        rv = (RecyclerView) findViewById(R.id.rv_MPM);
        adapter = new MyPrivateMsgAdapter(this);
        layoutManager = new LinearLayoutManager(this);

        query();
        rv.setAdapter(adapter);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new RecycleViewDivider(this, LinearLayout.HORIZONTAL));
        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final Conversation c = adapter.getItem(position);
//                System.out.println(c.getTitle());
                BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                query.addWhereEqualTo("username", c.getTitle());
                query.findObjects(MyPrivateConversationActivity.this, new FindListener<MyUser>() {
                    @Override
                    public void onSuccess(List<MyUser> list) {
//                          System.out.println(list.size());
                        BmobIMUserInfo info = new BmobIMUserInfo(list.get(0).getObjectId(), c.getTitle(), c.getAvatar());
                        BmobIM.getInstance().updateUserInfo(info);
//                        System.out.println(info.getUser());
                        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                            @Override
                            public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("c", bmobIMConversation);
                                Intent intent = new Intent(MyPrivateConversationActivity.this,ChatActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void onEventMainThread(UpdateConversationEvent event) {
        if (event.getMsg() == "update") {
            query();
        }
    }
    public void query() {
        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
        List<Conversation> conversationList = new ArrayList<>();
        conversationList.clear();
        if (list != null && list.size() > 0) {
            System.out.println(list.get(0).getUpdateTime());
            for (BmobIMConversation item : list) {
                Conversation conversation = new Conversation();
                List<BmobIMMessage> messages = item.getMessages();
                if (messages.get(0).getMsgType().equals("add")){
                    item.deleteMessage(messages.get(0));
                    query();
                }
                else {
                    conversation.setTitle(item.getConversationTitle());
                    conversation.setAvatar(item.getConversationIcon());
                    conversation.setTime(messages.get(0).getCreateTime());
                    conversation.setUnReadCount(BmobIM.getInstance().getUnReadCount(item.getConversationId()));
                    if (item.getMessages().size() > 0) {
                        conversation.setContent(item.getMessages().get(0).getContent());
                        conversationList.add(conversation);
                    }
                }

            }

        }
        adapter.bindData(conversationList);
        adapter.notifyDataSetChanged();
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

    @Override
    public void onResume() {
        super.onResume();
        query();

    }
}
