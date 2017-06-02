package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.Likes;
import com.example.secret.booklist60.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/24.
 */

public class LikeBookAdapter extends RecyclerView.Adapter {
    private List<Likes> list = new ArrayList<>();
    private Context context;
    public LikeBookAdapter(Context context){
        this.context = context;
    }
    public void bindData(List<Likes> list){
        this.list.clear();
        if (list != null){
            this.list.addAll(list);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LikeBookHolder holder =new LikeBookHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LikeBookHolder likeBookHolder = (LikeBookHolder) holder;
        likeBookHolder.bindData(list.get(position));
        Glide.with(context).load(list.get(position).getBook().getCover()).into(likeBookHolder.ivCover);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Likes getItem(int position){
        return list.get(position);
    }
}
