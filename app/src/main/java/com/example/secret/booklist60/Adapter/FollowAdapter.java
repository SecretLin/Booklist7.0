package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
 * Created by Secret on 2016/11/30.
 * 关注界面的adapter
 */

public class FollowAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Follow_Fans> list = new ArrayList<>();
    private Map<Integer,MyUser> myUserMap = new HashMap<>();
    public FollowAdapter(Context context){
        this.context = context;
    }
    public void bindData(List<Follow_Fans> list){
        this.list.clear();
        if (list != null){
            this.list = list;
        }

        for (int i =0;i<list.size();i++){
            myUserMap.put(i,list.get(i).getFollower());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FollowHolder holder = new FollowHolder(LayoutInflater.from(context).inflate(R.layout.item_fans_follow,parent,false));

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyUser currentUser = BmobUser.getCurrentUser(context,MyUser.class);
        final FollowHolder followHolder = (FollowHolder) holder;
        followHolder.tvUsername.setText(list.get(position).getFollower().getUsername());
        BmobFile head = list.get(position).getFollower().getHead();
        if (head!=null){
            Glide.with(context).load(head.getUrl()).into(followHolder.ivHead);
        }
        else if (list.get(position).getFollower().getUrlHead()!=null){
            Glide.with(context).load(list.get(position).getFollower().getUrlHead()).into(followHolder.ivHead);
        }
        else {
            followHolder.ivHead.setImageResource(R.mipmap.head);
        }

        followHolder.isFollowed.setBackgroundResource(R.mipmap.followed);

        followHolder.isFollowed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                followHolder.isFollowed.getParent().requestDisallowInterceptTouchEvent(true);
                BmobQuery<Follow_Fans> query = new BmobQuery<Follow_Fans>();
                query.addWhereEqualTo("Follower",new BmobPointer(myUserMap.get(position)));

                BmobQuery<Follow_Fans> query1 = new BmobQuery<Follow_Fans>();
                query1.addWhereEqualTo("Fans",new BmobPointer(currentUser));

                List<BmobQuery<Follow_Fans>> andList = new ArrayList<BmobQuery<Follow_Fans>>();
                andList.add(query);
                andList.add(query1);

                BmobQuery<Follow_Fans> andQuery = new BmobQuery<Follow_Fans>();
                andQuery.and(andList);
                andQuery.findObjects(context, new FindListener<Follow_Fans>() {
                    @Override
                    public void onSuccess(List<Follow_Fans> ffList) {

                        if (!ffList.isEmpty()){
                            Follow_Fans follow_fans = new Follow_Fans();
                            follow_fans.delete(context, list.get(position).getObjectId(), new DeleteListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(context,"取消关注成功",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(context,"取消关注失败,"+s,Toast.LENGTH_SHORT).show();
                                }
                            });
                            followHolder.isFollowed.setBackgroundResource(R.mipmap.follow);

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
                                    followHolder.isFollowed.setBackgroundResource(R.mipmap.followed);
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
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return (list.size()==0)?0:list.size();
    }


    public MyUser getItem(int position){
        return (list.isEmpty())?null:list.get(position).getFollower();
    }
    public class FollowHolder extends RecyclerView.ViewHolder{

        protected CircleImageView ivHead;
        protected TextView tvUsername;
        protected ImageView isFollowed;

        public FollowHolder(View itemView) {
            super(itemView);

            ivHead = (CircleImageView) itemView.findViewById(R.id.ivHead);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            isFollowed = (ImageView) itemView.findViewById(R.id.isFollowed);

        }

    }
}
