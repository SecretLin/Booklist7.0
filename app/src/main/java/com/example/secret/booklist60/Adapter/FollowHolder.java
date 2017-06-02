package com.example.secret.booklist60.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.secret.booklist60.DataBase.Follow_Fans;
import com.example.secret.booklist60.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Secret on 2016/11/30.
 */

public class FollowHolder extends RecyclerView.ViewHolder{

    protected CircleImageView ivHead;
    protected TextView tvUsername;
    protected ImageButton isFollowed;

    public FollowHolder(View itemView) {
        super(itemView);

        ivHead = (CircleImageView) itemView.findViewById(R.id.ivHead_Follow);
        tvUsername = (TextView) itemView.findViewById(R.id.tvUsername_Follow);
        isFollowed = (ImageButton) itemView.findViewById(R.id.isFollowed);

    }
    public void bindData(Follow_Fans follow_fans){

        tvUsername.setText(follow_fans.getFollower().getUsername());

    }
}
