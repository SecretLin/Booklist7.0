package com.example.secret.booklist60;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.secret.booklist60.DataBase.Follow_Fans;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.event.MapEvent;
import com.example.secret.booklist60.event.UpdateConversationEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import de.greenrobot.event.EventBus;

/**
 * Created by Secret on 2016/11/11.
 */

public class DemoMessageHandler extends BmobIMMessageHandler {
    Context context;

    public DemoMessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessageReceive(final MessageEvent event) {
        //当接收到服务器发来的消息时，此方法被调用
        handlerMessage(event);

    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {

        //每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
        Map<String, List<MessageEvent>> map = event.getEventMap();
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list = entry.getValue();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                handlerMessage(list.get(i));

            }


        }
    }

    public void handlerMessage(MessageEvent event) {
        BmobIMMessage msg = event.getMessage();
        if (BmobIMMessageType.getMessageTypeValue(msg.getMsgType()) == 0) {//用户自定义的消息类型，其类型值均为0
            processCustomMessage(msg, event.getFromUserInfo());
        }
        else{
            MyUser currentUser = BmobUser.getCurrentUser(context,MyUser.class);
            final BmobIMConversation conversation = event.getConversation();
            final BmobIMUserInfo info = event.getFromUserInfo();
            final String username = info.getName();
            final String title = conversation.getConversationTitle();

            if (!currentUser.isReceiveStrangerMsg()){
                BmobQuery<MyUser> userBmobQuery = new BmobQuery<>();
                userBmobQuery.addWhereEqualTo("objectId",info.getUserId());

                BmobQuery<Follow_Fans> query2 = new BmobQuery<>();
                query2.addWhereMatchesQuery("Follower","MyUser",userBmobQuery);

                BmobQuery<Follow_Fans> query1 = new BmobQuery<>();
                query1.addWhereMatchesQuery("Fans","MyUser",userBmobQuery);

                List<BmobQuery<Follow_Fans>> andList = new ArrayList<>();
                andList.add(query2);
                andList.add(query1);

                BmobQuery<Follow_Fans> andQuery = new BmobQuery<>();
                andQuery.and(andList);
                andQuery.findObjects(context, new FindListener<Follow_Fans>() {
                    @Override
                    public void onSuccess(List<Follow_Fans> list) {
                        if (list.isEmpty()){
                            return;
                        }
                        else {
                            executeMsg(conversation, info, username, title);
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });

            }
            else {
                executeMsg(conversation, info, username, title);

            }
        }




    }

    private void executeMsg(final BmobIMConversation conversation, final BmobIMUserInfo info, String username, String title) {
        if (!title.equals(username)) {
            BmobQuery<MyUser> query = new BmobQuery<>();
            query.getObject(context, info.getUserId(), new GetListener<MyUser>() {
                @Override
                public void onSuccess(final MyUser myUser) {
                    final int FINISHED = 10000001;
                    final Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == FINISHED) {
                                EventBus.getDefault().post(new UpdateConversationEvent("update"));
                                Map<String, String> map1 = new HashMap<>();
                                map1.clear();
                                map1.put("title", conversation.getConversationTitle());
                                map1.put("content", conversation.getMessages().get(0).getContent());
                                map1.put("username", info.getName());
                                map1.put("userId", info.getUserId());
                                map1.put("avatar", info.getAvatar());
                                map1.put("type","chat");
                                EventBus.getDefault().post(new MapEvent(map1));
                            }
                        }
                    };
                    new Thread() {
                        @Override
                        public void run() {
                            String name = myUser.getUsername();
                            String avatar;
                            if (myUser.getHead() == null) {
                                avatar = null;
                            } else {
                                avatar = myUser.getHead().getUrl();
                            }
                            conversation.setConversationTitle(name);
                            conversation.setConversationIcon(avatar);
                            BmobIM.getInstance().updateConversation(conversation);
                            System.out.println("after:" + conversation.getConversationTitle());
                            System.out.println("对话更新成功");
                            info.setName(name);
                            info.setAvatar(avatar);
                            BmobIM.getInstance().updateUserInfo(info);
                            handler.sendEmptyMessage(FINISHED);
                        }
                    }.start();


                }

                @Override
                public void onFailure(int i, String s) {

                }
            });

        } else {

            EventBus.getDefault().post(new UpdateConversationEvent("update"));
            Map<String, String> map1 = new HashMap<>();
            map1.clear();
            map1.put("title", conversation.getConversationTitle());
            map1.put("content", conversation.getMessages().get(0).getContent());
            map1.put("username", info.getName());
            map1.put("userId", info.getUserId());
            map1.put("avatar", info.getAvatar());
            map1.put("type","chat");
            EventBus.getDefault().post(new MapEvent(map1));
        }
    }
    //处理“有一个新粉丝”的消息
    public void processCustomMessage(BmobIMMessage message,BmobIMUserInfo info){
        String type =message.getMsgType();

        if(type.equals("add")){//接收到的添加好友的请求
            Map<String, String> map1 = new HashMap<>();
            map1.put("title","你有一个新粉丝");
            map1.put("content","");
//            map1.put("username",info.getName());
//            map1.put("userId", info.getUserId());
//            map1.put("avatar", info.getAvatar());
            map1.put("type","follow");
            EventBus.getDefault().post(new MapEvent(map1));
        }

    }
}