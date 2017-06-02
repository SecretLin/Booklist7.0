package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.History;
import com.example.secret.booklist60.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Secret on 2016/9/16.
 * 历史记录界面的adapter
 */
public class HistoryRecordAdapter extends RecyclerView.Adapter {
    private List<History> list = new ArrayList<>();
    private Context context;
    public HistoryRecordAdapter(Context context){
        this.context = context;
    }
    public void bindData(List<History> list){
        this.list.clear();
        if (list != null){
            this.list.addAll(list);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HistoryRecordHolder holder =new HistoryRecordHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HistoryRecordHolder historyRecordHolder = (HistoryRecordHolder) holder;
        historyRecordHolder.bindData(list.get(position));
        String coverUrl = list.get(position).getBook().getCover();
        if (coverUrl!=null){
            Glide.with(context).load(coverUrl).into(historyRecordHolder.ivCover);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public History getItem(int position){


        return list.get(position);
    }
}
