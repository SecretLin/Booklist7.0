package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.secret.booklist60.DataBase.LibInfor;
import com.example.secret.booklist60.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Secret on 2016/12/7.
 * 书详情页中，馆藏记录的adapter
 */

public class LibSearchAdapter extends RecyclerView.Adapter {
    Context context;
    List<LibInfor> list = new ArrayList<>();
    public static int length = 1;

    public LibSearchAdapter(Context context) {
        this.context = context;
    }

    public void bindData(List<LibInfor> list) {
        this.list.clear();
        if (list != null) {
            this.list = list;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LibSearchHolder holder = new LibSearchHolder(LayoutInflater.from(context).inflate(R.layout.item_libsearch, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        System.out.println("p"+position);
        LibSearchHolder holder1 = (LibSearchHolder) holder;


        if (position > 0) {
            if (list.get(position).getLocation().equals(list.get(position - 1).getLocation())) {
                holder1.tvLib.setText("");
                holder1.ivLib.setVisibility(View.GONE);
            } else {
                holder1.tvLib.setText(list.get(position).getLocation());
            }
        }else {
            holder1.tvLib.setText(list.get(position).getLocation());
        }
        holder1.tvStatus.setText(list.get(position).getStatus().trim());

//            System.out.println("booklistdetail:"+list.get(position).getLocation());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class LibSearchHolder extends RecyclerView.ViewHolder {
        private TextView tvLib, tvStatus;
        private ImageView ivLib;

        public LibSearchHolder(View itemView) {
            super(itemView);

            tvLib = (TextView) itemView.findViewById(R.id.tvLib);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            ivLib = (ImageView) itemView.findViewById(R.id.ivLibicon);
        }
    }
}
