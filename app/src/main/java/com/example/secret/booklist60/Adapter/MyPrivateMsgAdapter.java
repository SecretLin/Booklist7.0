package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.Conversation;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.utils.TimeFormat;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Secret on 2016/11/5.
 * 我的私信界面的adapter
 */

public class MyPrivateMsgAdapter extends RecyclerView.Adapter {

    List<Conversation> chatList = new ArrayList<>();
    Context context;

    public MyPrivateMsgAdapter(Context context){
        this.context = context;
    }
    public void bindData(List<Conversation> chatList){
        this.chatList.clear();
        if (chatList!=null){
            this.chatList.addAll(chatList);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MessageHolder holder = new MessageHolder(LayoutInflater.from(context).inflate(R.layout.item_privatemessage,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageHolder messageHolder = (MessageHolder) holder;
        messageHolder.tvUsername.setText(chatList.get(position).getTitle());
        messageHolder.tvContent.setText(chatList.get(position).getContent());
        if (chatList.get(position).getAvatar()!=null){
            Glide.with(context).load(chatList.get(position).getAvatar()).into(messageHolder.ivHead);
        }
        else {
            messageHolder.ivHead.setBackgroundResource(R.mipmap.head);
        }
        long count = chatList.get(position).getUnReadCount();
        if (count>0){
            messageHolder.tvUnReadCount.setVisibility(View.VISIBLE);
            messageHolder.tvUnReadCount.setText(String.valueOf(count));
        }
        else {
            messageHolder.tvUnReadCount.setVisibility(View.GONE);
        }

        messageHolder.tvTime.setText(TimeFormat.getMessageTime(chatList.get(0).getTime()));


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public Conversation getItem(int position){
        return chatList.get(position);
    }

    class MessageHolder extends RecyclerView.ViewHolder{
        private CircleImageView ivHead;
        private TextView tvUsername,tvContent,tvUnReadCount,tvTime;
        public MessageHolder(View itemView) {
            super(itemView);

            ivHead = (CircleImageView) itemView.findViewById(R.id.ivHead_message);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername_message);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent_message);
            tvUnReadCount = (TextView) itemView.findViewById(R.id.unreadCount);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime_chat);
        }
    }

}
