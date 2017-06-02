package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.FeedBack;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.utils.TimeFormat;
import com.example.secret.booklist60.UI.UserDetailActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Secret on 2016/12/12.
 */

public class FeedBackAdapter extends RecyclerView.Adapter {
    private List<FeedBack> list = new ArrayList<>();
    Context context;
    int SYSTEM_TYPE = 1;
    int USER_TYPE = 2;

    public FeedBackAdapter(Context context) {
        this.context = context;
    }

    public void bindData(List<FeedBack> list) {
        this.list.clear();
        if (list != null) {
            this.list = list;
        }
    }
    public void addFeedback(FeedBack feedBack) {

        list.addAll(Arrays.asList(feedBack));

        notifyDataSetChanged();

    }
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == SYSTEM_TYPE) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_system_feedback, parent, false);
            FeedBackAdapter.Feedback_Systemt systemHolder = new FeedBackAdapter.Feedback_Systemt(view);


            return systemHolder;
        } else if (viewType == USER_TYPE) {

            View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_feedback, parent, false);
            FeedBackAdapter.Feedback_User userHolder = new FeedBackAdapter.Feedback_User(view1);


            return userHolder;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final FeedBack feedBack = list.get(position);
        final MyUser currentUser = BmobUser.getCurrentUser(context, MyUser.class);
        if (holder instanceof FeedBackAdapter.Feedback_Systemt) {
            FeedBackAdapter.Feedback_Systemt systemtHolder = (FeedBackAdapter.Feedback_Systemt) holder;
            if (list != null) {
                systemtHolder.tvContent.setText(feedBack.getContent());
                systemtHolder.ivHead.setImageResource(R.mipmap.icon);
            }
            systemtHolder.ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, UserDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", currentUser);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            showTime(position, systemtHolder.tvTime);
        } else if (holder instanceof FeedBackAdapter.Feedback_User) {

            FeedBackAdapter.Feedback_User userHolder = (FeedBackAdapter.Feedback_User) holder;
            if (list != null) {
                userHolder.tvContent.setText(feedBack.getContent());

                if (feedBack.getUser().getHead() != null) {
                    Glide.with(context).load(feedBack.getUser().getHead().getUrl()).into(userHolder.ivHead);
                }
                else if (feedBack.getUser().getUrlHead()!=null){
                    Glide.with(context).load(feedBack.getUser().getUrlHead()).into(userHolder.ivHead);
                }
                else {
                    userHolder.ivHead.setImageResource(R.mipmap.head);
                }
            }
            userHolder.ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                    query.addWhereEqualTo("username", feedBack.getUser().getUsername());
                    query.findObjects(context, new FindListener<MyUser>() {
                        @Override
                        public void onSuccess(List<MyUser> list) {
                            if (!list.isEmpty()) {
                                Intent intent = new Intent(context, UserDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("user", list.get(0));
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        }

                        @Override
                        public void onError(int i, String s) {

                        }
                    });
                }
            });

            showTime(position, userHolder.tvTime);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        FeedBack feedBack = list.get(position);
        return feedBack.getType() == SYSTEM_TYPE ? SYSTEM_TYPE : USER_TYPE;
    }


    class Feedback_Systemt extends RecyclerView.ViewHolder {
        private CircleImageView ivHead;
        private TextView tvContent, tvTime;

        public Feedback_Systemt(View itemView) {
            super(itemView);
            ivHead = (CircleImageView) itemView.findViewById(R.id.ivHead_system_feedback);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent_system_feedback);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime_system_feedback);
        }

    }

    class Feedback_User extends RecyclerView.ViewHolder {
        private CircleImageView ivHead;
        private TextView tvContent, tvTime;

        public Feedback_User(View itemView) {
            super(itemView);
            ivHead = (CircleImageView) itemView.findViewById(R.id.ivHead_user_feedback);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent_user_feedback);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }

    public static final String Format_YearMonDay_Time = "yyyy-MM-dd HH:mm";

    public void showTime(int position, TextView tvTime) {
        SimpleDateFormat format = new SimpleDateFormat(Format_YearMonDay_Time);
        if (position == 0) {
            tvTime.setVisibility(View.VISIBLE);
            try {
                Date d = format.parse(list.get(position).getCreatedAt());
                tvTime.setText(TimeFormat.getChatTime(d.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            try {
                Date d1 = format.parse(list.get(position).getCreatedAt());
                Date d2 = format.parse(list.get(position-1).getCreatedAt());
                long time1 = d1.getTime();
                long time2 = d2.getTime();
                tvTime.setText(TimeFormat.getChatTime(time1));
                if ((time1 - time2) > 10 * 60 * 1000) {
                    tvTime.setVisibility(View.VISIBLE);

                }else {
                    tvTime.setVisibility(View.GONE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }
}
