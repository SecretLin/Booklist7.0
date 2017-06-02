package com.example.secret.booklist60.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.secret.booklist60.DataBase.Likes;
import com.example.secret.booklist60.R;

/**
 * Created by Administrator on 2017/2/24.
 */

public class LikeBookHolder extends RecyclerView.ViewHolder {
    private TextView tvBookname,tvAuthor;
    public ImageView ivCover;
    public LikeBookHolder(View itemView) {
        super(itemView);
        tvBookname = (TextView) itemView.findViewById(R.id.tvBookname);
        tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
        ivCover = (ImageView) itemView.findViewById(R.id.ivCover);
    }
    public void bindData(Likes likes){
        tvBookname.setText(likes.getBook().getName());
        tvAuthor.setText(likes.getBook().getAuthor());

    }
}
