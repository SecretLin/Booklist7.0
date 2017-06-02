package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.secret.booklist60.DataBase.Book;
import com.example.secret.booklist60.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Secret on 2016/9/15.
 * 这是书单页的adapter
 */
public class BookAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<Book> list = new ArrayList<>();
    public BookAdapter(Context context){
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
        BookHolder holder =new BookHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_book,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final BookHolder bookHolder = (BookHolder) holder;
        bookHolder.bindData(list.get(position));
        Glide.with(context).load(list.get(position).getCover()).
                diskCacheStrategy(DiskCacheStrategy.RESULT).into(bookHolder.ivCover);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Book getItem(int position){

        return list.get(position);
    }

}
