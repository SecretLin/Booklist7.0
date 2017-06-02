package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.secret.booklist60.DataBase.All_Comment;
import com.example.secret.booklist60.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Secret on 2017/3/13.
 */

public class PinlunChildAdapter extends RecyclerView.Adapter {
    List<All_Comment> list = new ArrayList<>();
    Context context;
    public PinlunChildAdapter(Context context){
        this.context = context;
    }
    public void bindData(List<All_Comment> list){
        this.list.clear();
        if (list!=null){
            this.list = list;
        }
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PinlunChildHolder holder = new PinlunChildHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pinlunchild, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PinlunChildHolder pinlunChildHolder = (PinlunChildHolder) holder;
        pinlunChildHolder.tvReview.setText("@"+list.get(position).getUser1().getUsername());
        pinlunChildHolder.tvAuthor.setText("@"+list.get(position).getUser2().getUsername()+":");
        pinlunChildHolder.tvContent.setText(list.get(position).getContent1());
        System.out.println("pinlun:"+list.get(position).getContent2());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public All_Comment getItem(int position) {
        return list.size()!=0?list.get(position):null;
    }
}
