package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.All_Comment;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.UI.ReceiveComment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Secret on 2016/12/14.
 */

public class ReceiveCommentAdapter extends RecyclerView.Adapter {
    private List<All_Comment> list = new ArrayList<>();
    private Context context;
    ReceiveComment receiveComment = new ReceiveComment() ;
    public int itemPosition;
    public ReceiveCommentAdapter(Context context){
        this.context = context;
        receiveComment = (ReceiveComment) context;
    }
    public void bindData(List<All_Comment> list){
        this.list.clear();
        if (list!=null){
            this.list = list;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReceiveCommentHolder holder = new ReceiveCommentHolder(LayoutInflater.from(context).inflate(
                R.layout.item_receivecomment, parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ReceiveCommentHolder commentHolder = (ReceiveCommentHolder) holder;
        if (list.get(position).getUser1().getHead()!=null){
            Glide.with(context).load(list.get(position).getUser1().getHead().getUrl()).into(commentHolder.ivHead);

        }
        else if (list.get(position).getUser1().getUrlHead()!=null){
            Glide.with(context).load(list.get(position).getUser1().getUrlHead()).into(commentHolder.ivHead);
        }
        else {
            commentHolder.ivHead.setImageResource(R.mipmap.head);
        }
        commentHolder.tvUsername.setText(list.get(position).getUser1().getUsername());
        commentHolder.tvContent.setText(list.get(position).getContent1());
        commentHolder.tvTime.setText(list.get(position).getCreatedAt());
        commentHolder.tvContent_me.setText(list.get(position).getContent2());

        commentHolder.btnSendBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveComment.sendMessage.setVisibility(View.VISIBLE);
                itemPosition = position;
            }
        });

    }


    public All_Comment getItem(int position){
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ReceiveCommentHolder extends RecyclerView.ViewHolder{
        private CircleImageView ivHead;
        private TextView tvUsername,tvContent,tvTime,tvContent_me;
        private Button btnSendBack;
        public ReceiveCommentHolder(View itemView) {
            super(itemView);
            ivHead = (CircleImageView) itemView.findViewById(R.id.ivHead);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvContent_me = (TextView) itemView.findViewById(R.id.tvContent_Me);
            btnSendBack = (Button) itemView.findViewById(R.id.btnSendBack);
        }
    }
}
