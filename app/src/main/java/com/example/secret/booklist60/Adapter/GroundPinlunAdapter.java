package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.All_Comment;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.UI.GroundPinLunActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/2/23.
 */

public class GroundPinlunAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<All_Comment> list =new ArrayList<>();
    GroundPinLunActivity activity = new GroundPinLunActivity();
    public GroundPinlunAdapter(Context context){
        this.context=context;
        activity = (GroundPinLunActivity) context;
    }
    public void bindData(List<All_Comment> list){
        this.list.clear();
        if(list != null){
            this.list.addAll(list);
        }
    }
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        GroundPinlunHolder holder = new GroundPinlunHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_groundpinlun,parent,false));
        return holder;
    }
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        GroundPinlunHolder groundPinlunHolder =(GroundPinlunHolder)holder;
//        groundPinlunHolder.bindData(list.get(position));
        MyUser user = list.get(position).getUser1();
        if (user.getHead()!=null){
            Glide.with(context).load(user.getHead().getUrl()).into(groundPinlunHolder.TouXiang);
        }
        groundPinlunHolder.tv_Time.setText(list.get(position).getCreatedAt());
        groundPinlunHolder.tv_pName.setText(list.get(position).getUser1().getUsername());

        groundPinlunHolder.tv_content.setText(list.get(position).getContent1());
        if (list.get(position).getWho()==2){
            groundPinlunHolder. tv_huifu.setVisibility(View.VISIBLE);
            groundPinlunHolder.tv_pName1.setVisibility(View.VISIBLE);
            groundPinlunHolder.tv_pName1.setText("@"+list.get(position).getUser2().getUsername());
        }

    }
    public int getItemCount(){
        return list.size();
    }

    public All_Comment getItem(int position){
        return list.get(position);
    }

    class GroundPinlunHolder extends RecyclerView.ViewHolder {
        CircleImageView TouXiang;
        TextView tv_pName, tv_huifu, tv_pName1,tv_content,tv_Time;
        public GroundPinlunHolder(View itemView){
            super(itemView);
            TouXiang =(CircleImageView)itemView.findViewById(R.id.ivHead);
            tv_pName=(TextView)itemView.findViewById(R.id.pName);
            tv_huifu =(TextView)itemView.findViewById(R.id.tvhuifu);
            tv_pName1 =(TextView)itemView.findViewById(R.id.pName1);
            tv_content=(TextView)itemView.findViewById(R.id.tv_content);
            tv_Time = (TextView) itemView.findViewById(R.id.tvTime);
        }
        public void bindData(final All_Comment all_comment){


        }
    }
}
