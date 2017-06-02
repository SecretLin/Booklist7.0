package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.Follow_Fans;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Secret on 2016/12/1.
 * 粉丝界面的adapter
 */

public class FansAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Follow_Fans> list = new ArrayList<>();
    private Map<Integer,MyUser> myUserMap = new HashMap<>();

    public FansAdapter(Context context){
        this.context = context;
    }

    public void bindData(List<Follow_Fans> list){
        this.list.clear();
        if (list!=null){
            this.list = list;
        }

        for (int i =0;i<list.size();i++){
            myUserMap.put(i,list.get(i).getFans());
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FansHolder holder = new FansHolder(LayoutInflater.from(context).inflate(R.layout.item_fans_follow,parent,false));

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyUser currentUser = BmobUser.getCurrentUser(context,MyUser.class);
        final FansHolder fansHolder = (FansHolder) holder;
        fansHolder.tvUsername.setText(list.get(position).getFans().getUsername());
        BmobFile head = list.get(position).getFans().getHead();
        if (head!=null){
            Glide.with(context).load(head.getUrl()).into(fansHolder.ivHead);
        }
        else if (list.get(position).getFans().getUrlHead()!=null){
            Glide.with(context).load(list.get(position).getFans().getUrlHead()).into(fansHolder.ivHead);
        }
        else {
            fansHolder.ivHead.setImageResource(R.mipmap.head);
        }

        BmobQuery<Follow_Fans> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("Follower",new BmobPointer(list.get(position).getFans()));

        BmobQuery<Follow_Fans> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("Fans",new BmobPointer(currentUser));

        List<BmobQuery<Follow_Fans>> andList = new ArrayList<>();
        andList.add(query1);
        andList.add(query2);

        final BmobQuery<Follow_Fans> andQuery = new BmobQuery<>();
        andQuery.and(andList);
        andQuery.findObjects(context, new FindListener<Follow_Fans>() {
            @Override
            public void onSuccess(List<Follow_Fans> list) {
                if (!list.isEmpty()){
                     fansHolder.ivFollow.setImageResource(R.mipmap.followed);
                }
                else {
                    fansHolder.ivFollow.setImageResource(R.mipmap.follow);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

        fansHolder.ivFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobQuery<Follow_Fans> query1 = new BmobQuery<>();
                query1.addWhereEqualTo("Follower",new BmobPointer(list.get(position).getFans()));

                BmobQuery<Follow_Fans> query2 = new BmobQuery<>();
                query2.addWhereEqualTo("Fans",new BmobPointer(currentUser));

                List<BmobQuery<Follow_Fans>> andList = new ArrayList<>();
                andList.add(query1);
                andList.add(query2);

                final BmobQuery<Follow_Fans> andQuery = new BmobQuery<>();
                andQuery.and(andList);
                andQuery.findObjects(context, new FindListener<Follow_Fans>() {
                    @Override
                    public void onSuccess(List<Follow_Fans> ffList) {
                        if (!ffList.isEmpty()){
                            fansHolder.ivFollow.setImageResource(R.mipmap.followed);
                            Follow_Fans follow_fans = new Follow_Fans();
                            follow_fans.delete(context, list.get(position).getObjectId(), new DeleteListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(context,"取消关注成功",Toast.LENGTH_SHORT).show();
                                    fansHolder.ivFollow.setImageResource(R.mipmap.follow);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(context,"取消关注失败,"+s,Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                        else {
                            MyUser currentUser = BmobUser.getCurrentUser(context,MyUser.class);
                            Follow_Fans follow_fans = new Follow_Fans();
                            follow_fans.setFollower(myUserMap.get(position));
                            follow_fans.setFans(currentUser);
                            follow_fans.save(context, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(context,"关注成功",Toast.LENGTH_SHORT).show();
                                    fansHolder.ivFollow.setImageResource(R.mipmap.followed);

                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(context,"关注失败"+s,Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return (list.isEmpty())?0:list.size();
    }

    public MyUser getItem(int position){
        return (list.isEmpty())?null:list.get(position).getFans();
    }

    public class FansHolder extends RecyclerView.ViewHolder {
        protected TextView tvUsername;
        protected CircleImageView ivHead;
        protected ImageView ivFollow;
        public FansHolder(View itemView) {
            super(itemView);

            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            ivHead = (CircleImageView) itemView.findViewById(R.id.ivHead);
            ivFollow = (ImageView) itemView.findViewById(R.id.isFollowed);
        }
    }

}
