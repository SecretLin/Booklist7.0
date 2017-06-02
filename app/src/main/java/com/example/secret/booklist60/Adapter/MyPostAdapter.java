package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.All_Comment;
import com.example.secret.booklist60.DataBase.Ground;
import com.example.secret.booklist60.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Secret on 2017/2/28.
 */

public class MyPostAdapter extends RecyclerView.Adapter {
    private List<Ground> lists = new ArrayList<>();
    Context context;

    public MyPostAdapter(Context context){
        this.context = context;
    }
    public void bindList(List<Ground> lists){
        this.lists.clear();
        if (lists!=null){
            this.lists = lists;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyPostHolder holder = new MyPostHolder(LayoutInflater.from(context).inflate(R.layout.item_mypost,parent,false));

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyPostHolder myHolder = (MyPostHolder) holder;
//        System.out.println("onBindViewHolder----------");

        Ground ground = lists.get(position);
        myHolder.tvUsername.setText(ground.getUser().getUsername());
        myHolder.tvTime.setText(ground.getCreatedAt());
        myHolder.tvContent.setText(ground.getContent());

        Log.i("content",ground.getContent());

        if (ground.getBook()!=null){
            myHolder.tvBookname.setText(ground.getBook().getName());
            myHolder.tvAuthor.setText(ground.getBook().getAuthor());
            myHolder.layoutbook.setVisibility(View.VISIBLE);
            Glide.with(context).load(ground.getBook().getCover()).into(myHolder.ivCover);
        }


        BmobQuery<All_Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("ground",new BmobPointer(ground));
        query.findObjects(context, new FindListener<All_Comment>() {
            @Override
            public void onSuccess(List<All_Comment> list) {
                if (!list.isEmpty()){
                    myHolder.tvCount.setText(list.size()+"评论");

                }else {
                    myHolder.tvCount.setText("0评论");
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });


    }

    @Override
    public int getItemCount() {

        System.out.println("adapter---count:"+lists.size());
        return lists.size();
    }

    class MyPostHolder extends RecyclerView.ViewHolder {

        TextView tvContent,tvBookname,tvAuthor,tvTime,tvCount,tvUsername;
        RelativeLayout layoutbook;
        ImageView ivCover;

        public MyPostHolder(View itemView) {
            super(itemView);

            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvBookname = (TextView) itemView.findViewById(R.id.tvBookname);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvCount = (TextView) itemView.findViewById(R.id.tvCount);
            layoutbook = (RelativeLayout) itemView.findViewById(R.id.layout_book);
            ivCover = (ImageView) itemView.findViewById(R.id.ivCover);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        }
    }
}
