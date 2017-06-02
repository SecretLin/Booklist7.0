package com.example.secret.booklist60.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.secret.booklist60.R;

/**
 * Created by Administrator on 2016/12/7.
 */

public class PinlunChildHolder extends RecyclerView.ViewHolder {
    TextView tvReview,tvReply,tvAuthor,tvContent;
    public PinlunChildHolder(View itemView){
        super(itemView);
        tvReview = (TextView)itemView.findViewById(R.id.tv_reviewName);
        tvReply = (TextView) itemView.findViewById(R.id.tv_reply);
        tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
        tvContent = (TextView)itemView.findViewById(R.id.tv_Content);
    }
}
