package com.example.secret.booklist60.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.secret.booklist60.DataBase.Comment;
import com.example.secret.booklist60.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Secret on 2016/10/27.
 */

public class PinlunHolder extends RecyclerView.ViewHolder {
    private TextView tvUsername,tvComment,tvTime;
    protected CircleImageView ivTouxiang;
    RecyclerView ChildRecycler;
    private List<Comment> list = new ArrayList<>();

    public PinlunHolder(View itemView) {
        super(itemView);
        tvUsername = (TextView) itemView.findViewById(R.id.tvUsername_Pinlun);
        tvComment = (TextView) itemView.findViewById(R.id.tvComment);
        tvTime = (TextView) itemView.findViewById(R.id.tvTime);

        ivTouxiang = (CircleImageView) itemView.findViewById(R.id.ivTouxiang_Pinlun);
        ChildRecycler = (RecyclerView) itemView.findViewById(R.id.rv_child);

    }
    public void bindData(Comment comment){
        tvUsername.setText(comment.getMyUser().getUsername());
        tvComment.setText(comment.getCommentcontent());
        tvTime.setText(comment.getCreatedAt());
    }
}