package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.Book;
import com.example.secret.booklist60.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Secret on 2016/12/13.
 */

public class SelectbookAdapter extends RecyclerView.Adapter {
    List<Book> list = new ArrayList<>();
    Context context;
    public SelectbookAdapter(Context context){
        this.context = context;
    }
    public void bindData(List<Book> list){
        this.list.clear();
        if (list!=null){
            this.list = list;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SelectbookHolder holder = new SelectbookHolder(LayoutInflater.from(context).inflate(R.layout.item_selectbook,
                parent,false));

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SelectbookHolder selectbookHolder = (SelectbookHolder) holder;
        Glide.with(context).load(list.get(position).getCover()).into(selectbookHolder.ivCover);
        selectbookHolder.tvBookname.setText(list.get(position).getName());
        selectbookHolder.tvAuthor.setText(list.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Book getItem(int position){
        return list.get(position);
    }
    class SelectbookHolder extends RecyclerView.ViewHolder {
        private ImageView ivCover;
        private TextView tvBookname,tvAuthor;
        public SelectbookHolder(View itemView) {
            super(itemView);

            ivCover = (ImageView) itemView.findViewById(R.id.ivCover);
            tvBookname = (TextView) itemView.findViewById(R.id.tvBookname);
            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
        }
    }
}
