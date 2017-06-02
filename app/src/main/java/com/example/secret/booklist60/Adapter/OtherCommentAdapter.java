package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.Comment;
import com.example.secret.booklist60.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Secret on 2017/3/15.
 */

public class OtherCommentAdapter extends RecyclerView.Adapter{

    private List<Comment> list = new ArrayList<>();
    private Context context;
    public OtherCommentAdapter(Context context){
        this.context = context;
    }
    public void bindData(List<Comment> list){
        this.list.clear();
        if (list!=null){
            this.list = list;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OtherCommentHolder holder = new OtherCommentHolder(LayoutInflater.from(context).inflate(R.layout.item_othercomment,parent,false));

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OtherCommentHolder myHolder = (OtherCommentHolder) holder;
        myHolder.tvUsername.setText(list.get(position).getMyUser().getUsername());
        if (list.get(position).getMyUser().getHead()!=null){
            Glide.with(context).load(list.get(position).getMyUser().getHead().getUrl()).into(myHolder.ivHead);
        }
        else if (list.get(position).getMyUser().getUrlHead()!=null){
            Glide.with(context).load(list.get(position).getMyUser().getUrlHead()).into(myHolder.ivHead);
        }
        else {
            myHolder.ivHead.setImageResource(R.mipmap.head);
        }
        myHolder.tvTime.setText(list.get(position).getCreatedAt());
        myHolder.tvContent.setText(list.get(position).getCommentcontent());
        myHolder.tvBookname.setText(list.get(position).getBook().getName());
        myHolder.tvAuthor.setText(list.get(position).getBook().getAuthor());
        Glide.with(context).load(list.get(position).getBook().getCover()).into(myHolder.ivCover);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class OtherCommentHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername,tvTime,tvBookname,tvAuthor,tvContent;
        private CircleImageView ivHead;
        private ImageView ivCover;

        public OtherCommentHolder(View itemView) {
            super(itemView);

            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvBookname = (TextView) itemView.findViewById(R.id.tvBookname);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);

            ivHead = (CircleImageView) itemView.findViewById(R.id.ivHead);
            ivCover = (ImageView) itemView.findViewById(R.id.ivCover);

        }
    }
}
