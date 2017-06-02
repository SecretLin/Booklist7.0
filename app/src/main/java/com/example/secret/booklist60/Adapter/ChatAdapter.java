package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.utils.TimeFormat;
import com.example.secret.booklist60.UI.UserDetailActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Secret on 2016/11/4.
 * 聊天界面的adapter
 */

public class ChatAdapter extends RecyclerView.Adapter {
    List<BmobIMMessage> chatList = new ArrayList<>();
    Context context;
    //文本
    private final int TYPE_RECEIVER_TXT = 0;
    private final int TYPE_SEND_TXT = 1;

    public ChatAdapter(Context context) {

        this.context = context;

    }

    public void bindData(List<BmobIMMessage> chatList) {
        this.chatList.clear();
        if (chatList != null) {
            this.chatList.addAll(chatList);
            notifyDataSetChanged();
        }
    }

    public void addMessage(BmobIMMessage msg) {

        chatList.addAll(Arrays.asList(msg));
        notifyDataSetChanged();
    }

    public int getItemPosition(BmobIMMessage message) {
        for (int i = this.getItemCount(); i > 0; i--) {
            if (message.equals(this.getItem(i))) {
                return i;
            }
        }
        return 0;
    }

    public Object getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == TYPE_SEND_TXT) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_me, parent, false);
            Chat_Me_Holder meHolder = new Chat_Me_Holder(view);


            return meHolder;

        } else if (viewType == TYPE_RECEIVER_TXT) {

            View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_other, parent, false);
            Chat_Friend_Holder friendHolder = new Chat_Friend_Holder(view1);


            return friendHolder;
        }
        return null;
    }

    public String getUserId() {
        String id = BmobUser.getCurrentUser(context).getObjectId();
        return id;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BmobIMMessage message = chatList.get(position);
        final MyUser currentUser = BmobUser.getCurrentUser(context, MyUser.class);
        if (holder instanceof Chat_Me_Holder) {
            Chat_Me_Holder meHolder = (Chat_Me_Holder) holder;
            if (chatList != null) {
                meHolder.tvContent.setText(message.getContent());
                if (currentUser.getHead() != null) {
                    Glide.with(context).load(currentUser.getHead().getUrl()).into(meHolder.ivHead);
                }else if(currentUser.getUrlHead()!=null){
                    Glide.with(context).load(currentUser.getUrlHead()).into(meHolder.ivHead);
                }
                else {
                    meHolder.ivHead.setBackgroundResource(R.mipmap.head);
                }
            }
            meHolder.ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, UserDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", currentUser);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            showTime(position, meHolder.tvTime);
        } else if (holder instanceof Chat_Friend_Holder) {
            final BmobIMUserInfo info1 = BmobIM.getInstance().getUserInfo(message.getFromId());
            Chat_Friend_Holder friendHolder = (Chat_Friend_Holder) holder;
            if (chatList != null) {
                friendHolder.tvContent.setText(message.getContent());
                if (info1.getAvatar() != null) {
                    Glide.with(context).load(info1.getAvatar()).into(friendHolder.ivHead);
                }
            }
            friendHolder.ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                    query.addWhereEqualTo("username", info1.getName());
                    query.findObjects(context, new FindListener<MyUser>() {
                        @Override
                        public void onSuccess(List<MyUser> list) {
                            if (!list.isEmpty()) {
                                Intent intent = new Intent(context, UserDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("user", list.get(0));
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        }

                        @Override
                        public void onError(int i, String s) {

                        }
                    });
                }
            });

            showTime(position, friendHolder.tvTime);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        BmobIMMessage message = chatList.get(position);
        if (message.getMsgType().equals(BmobIMMessageType.TEXT.getType())) {
            return chatList.get(position).getFromId().equals(getUserId()) ? TYPE_SEND_TXT : TYPE_RECEIVER_TXT;
        }
        return 0;
    }


    class Chat_Me_Holder extends RecyclerView.ViewHolder {
        private CircleImageView ivHead;
        private TextView tvContent, tvTime;

        public Chat_Me_Holder(View itemView) {
            super(itemView);
            ivHead = (CircleImageView) itemView.findViewById(R.id.ivHead_chat_me);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent_Me);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }

    }

    class Chat_Friend_Holder extends RecyclerView.ViewHolder {
        private CircleImageView ivHead;
        private TextView tvContent, tvTime;

        public Chat_Friend_Holder(View itemView) {
            super(itemView);
            ivHead = (CircleImageView) itemView.findViewById(R.id.ivHead_chat_other);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent_Friend);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime_other);
        }
    }

    public void showTime(int position, TextView tvTime) {
        if (position == 0) {
            tvTime.setVisibility(View.VISIBLE);
            tvTime.setText(TimeFormat.getChatTime(chatList.get(position).getCreateTime()));
        } else {
            long time1 = chatList.get(position).getCreateTime();
            long time2 = chatList.get(position - 1).getCreateTime();
            tvTime.setText(TimeFormat.getChatTime(chatList.get(position).getCreateTime()));//+" "+TimeFormat.getChatTime(chatList.get(position-1).getCreateTime()));
            if ((time1 - time2) > 10 * 60 * 1000) {
                tvTime.setVisibility(View.VISIBLE);
            }else{
                tvTime.setVisibility(View.GONE);
            }
        }

    }
}