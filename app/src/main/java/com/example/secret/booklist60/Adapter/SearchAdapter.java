package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.Book;
import com.example.secret.booklist60.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Secret on 2016/10/14.
 * 书城中的搜索界面的adapter
 */

public class SearchAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<Book> list = new ArrayList<>();
    public SearchAdapter(Context context){
        this.context = context;
    }
    public void bindData(List<Book> list){
        this.list.clear();
        if (list != null){
            this.list.addAll(list);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SearchHolder holder =new SearchHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchHolder searchHolder = (SearchHolder) holder;
        searchHolder.bindData(list.get(position));
        Glide.with(context).load(list.get(position).getCover()).into(searchHolder.ivCover);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Book getItem(int position){


        return list.get(position);
    }
    public void setFilter(List<Book> FilteredDataList) {
        bindData(FilteredDataList);
        notifyDataSetChanged();
    }

}
