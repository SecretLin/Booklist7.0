package com.example.secret.booklist60.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.secret.booklist60.DataBase.Follow_Fans;
import com.example.secret.booklist60.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Secret on 2016/12/1.
 */

public class FansHolder extends RecyclerView.ViewHolder {
    protected TextView tvUsername;
    protected CircleImageView ivHead;
    protected ImageView ivFollow;
    public FansHolder(View itemView) {
        super(itemView);

        tvUsername = (TextView) itemView.findViewById(R.id.tvUsername_Fans);
        ivHead = (CircleImageView) itemView.findViewById(R.id.ivHead_Fans);
        ivFollow = (ImageView) itemView.findViewById(R.id.isFollowed_Fans);
    }
    public void bindData(Follow_Fans follow_fans){
        tvUsername.setText(follow_fans.getFans().getUsername());
    }
}
