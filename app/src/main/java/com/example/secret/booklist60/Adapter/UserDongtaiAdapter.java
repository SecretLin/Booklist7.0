package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.LatestList;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.UI.UserDetailActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by Secret on 2017/3/14.
 */

public class UserDongtaiAdapter extends RecyclerView.Adapter {
    List<LatestList> list = new ArrayList<>();
    Context context;
    UserDetailActivity activity = new UserDetailActivity();
    public void bindData(List<LatestList> list){
        this.list.clear();
        if (list!=null){
            this.list = list;
        }

    }
    public UserDongtaiAdapter(Context context){
        this.context = context;
        activity = (UserDetailActivity) context;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserDongtaiHolder holder = new UserDongtaiHolder(LayoutInflater.from(context).inflate(R.layout.item_dongtai,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserDongtaiHolder myHoler = (UserDongtaiHolder) holder;
        MyUser currentUser = BmobUser.getCurrentUser(context,MyUser.class);
        String type = list.get(position).getType();
        if (type.equals("Ground")){
            myHoler.tvType.setText(activity.Username+"回答了问题");
        }else if (type.equals("Likes")){
            myHoler.tvType.setText(activity.Username+"点赞了");
        }else if (type.equals("Comment")){
            myHoler.tvType.setText(activity.Username+"发表了评论");
        }
        myHoler.tvContent.setText(list.get(position).getContent());
        myHoler.tvTime.setText(list.get(position).getTime());
        if (list.get(position).getBookname()!=null){
            myHoler.rvBook.setVisibility(View.VISIBLE);
            myHoler.tvBookname.setText(list.get(position).getBookname());
            myHoler.tvAuthor.setText(list.get(position).getAuthor());
            Glide.with(context).load(list.get(position).getCover()).into(myHoler.ivCover);
        }
        else {
            myHoler.rvBook.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        System.out.println("latestlist.size:"+list.size());
        return list.size();

    }

    public class UserDongtaiHolder extends RecyclerView.ViewHolder {

        private TextView tvType,tvTime,tvContent,tvBookname,tvAuthor;
        private ImageView ivCover;
        private RelativeLayout rvBook;

        public UserDongtaiHolder(View itemView) {
            super(itemView);

            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvBookname = (TextView) itemView.findViewById(R.id.tvBookname);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            ivCover = (ImageView) itemView.findViewById(R.id.ivCover);
            rvBook = (RelativeLayout) itemView.findViewById(R.id.rvBook);
        }
    }
}
