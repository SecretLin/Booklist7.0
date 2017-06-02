package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.All_Comment;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.UI.MoreCommentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Secret on 2017/3/16.
 */

public class MoreCommentAdapter  extends RecyclerView.Adapter {
    private Context context;
    private List<All_Comment> list =new ArrayList<>();
    MoreCommentActivity activity = new MoreCommentActivity();
    public MoreCommentAdapter(Context context){
        this.context=context;
        activity = (MoreCommentActivity) context;
    }
    public void bindData(List<All_Comment> list){
        this.list.clear();
        if(list != null){
            this.list.addAll(list);
        }
    }
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
       MoreCommentHolder holder = new MoreCommentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_groundpinlun,parent,false));
        return holder;
    }
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        MoreCommentHolder myHolder = (MoreCommentHolder) holder;
        MyUser user = list.get(position).getUser1();
        if (user.getHead()!=null){
            Glide.with(context).load(user.getHead().getUrl()).into(myHolder.TouXiang);
        }
        myHolder.tv_Time.setText(list.get(position).getCreatedAt());
        myHolder.tv_pName.setText(list.get(position).getUser1().getUsername());

        myHolder.tv_content.setText(list.get(position).getContent1());
        if (list.get(position).getWho()==2 || list.get(position).getWho()==3){
            myHolder. tv_huifu.setVisibility(View.VISIBLE);
            myHolder.tv_pName1.setVisibility(View.VISIBLE);
            myHolder.tv_pName1.setText("@"+list.get(position).getUser2().getUsername());
        }

    }
    public int getItemCount(){
        return list.size();
    }

    public All_Comment getItem(int position){
        return list.get(position);
    }

    class MoreCommentHolder extends RecyclerView.ViewHolder {
        ImageView TouXiang;
        TextView tv_pName, tv_huifu, tv_pName1,tv_content,tv_Time;
        public MoreCommentHolder(View itemView){
            super(itemView);
            TouXiang =(ImageView)itemView.findViewById(R.id.ivHead);
            tv_pName=(TextView)itemView.findViewById(R.id.pName);
            tv_huifu =(TextView)itemView.findViewById(R.id.tvhuifu);
            tv_pName1 =(TextView)itemView.findViewById(R.id.pName1);
            tv_content=(TextView)itemView.findViewById(R.id.tv_content);
            tv_Time = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }
}
