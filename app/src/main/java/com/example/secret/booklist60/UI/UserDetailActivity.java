package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.Adapter.UserDongtaiAdapter;
import com.example.secret.booklist60.DataBase.Comment;
import com.example.secret.booklist60.DataBase.Ground;
import com.example.secret.booklist60.DataBase.LatestList;
import com.example.secret.booklist60.DataBase.Likes;
import com.example.secret.booklist60.utils.AddFollowerMessage;
import com.example.secret.booklist60.DataBase.Follow_Fans;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.DataBase.Shoucang;
import com.example.secret.booklist60.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Secret on 2016/11/30.
 * 用户资料界面
 */

public class UserDetailActivity extends Activity implements View.OnClickListener {
    private CircleImageView ivHead;
    private ImageView ivFollow, ivConversation;
    private TextView tvUsername, tvFollower_count, tvFans_count, tvShoucang_count, tvLatest,tvComment,tvPost;
    private int Followed = 10001;
    private int unFollowed = 10002;
    final int FINISHED = 10003;
    private LinearLayout layout_Follow, layout_Fans, layout_Shoucang;
    private UserDongtaiAdapter adapter;
    private RecyclerView rv;
    List<LatestList> latestLists = new ArrayList<>();
    List<LatestList> latestLists1 = new ArrayList<>();
    public String Username;
    private RelativeLayout rvPost,rvComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        layout_Follow = (LinearLayout) findViewById(R.id.layout_Guangzhu);
        layout_Follow.setOnClickListener(this);

        layout_Fans = (LinearLayout) findViewById(R.id.layout_Fans);
        layout_Fans.setOnClickListener(this);

        layout_Shoucang = (LinearLayout) findViewById(R.id.layout_Shoucang);
        layout_Shoucang.setOnClickListener(this);

        rvPost = (RelativeLayout) findViewById(R.id.btnOtherPost);
        rvPost.setOnClickListener(this);

        rvComment = (RelativeLayout) findViewById(R.id.btnothercomment);
        rvComment.setOnClickListener(this);

        final MyUser currentUser = BmobUser.getCurrentUser(this, MyUser.class);
        final MyUser user = (MyUser) getIntent().getSerializableExtra("user");
        Username = user.getUsername();
        ivHead = (CircleImageView) findViewById(R.id.ivHead_UserDtail);

        if (user.getHead() != null) {
            Glide.with(this).load(user.getHead().getUrl()).into(ivHead);
        }
        else if (user.getUrlHead()!=null){
            Glide.with(UserDetailActivity.this).load(user.getUrlHead()).into(ivHead);
        }
        else {

            ivHead.setImageResource(R.mipmap.head);

        }
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvUsername.setText(user.getUsername());


        tvShoucang_count = (TextView) findViewById(R.id.shoucang_count);


        tvFollower_count = (TextView) findViewById(R.id.tvFollower_count);


        tvFans_count = (TextView) findViewById(R.id.tvFans_count);

        tvComment = (TextView) findViewById(R.id.tvComment);
        tvPost = (TextView) findViewById(R.id.tvPost);
        tvLatest = (TextView) findViewById(R.id.tvLatest);
        final AssetManager mgr = getAssets();//得到AssetManager
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/msyh.ttc");//根据路径得到Typeface
        tvLatest.setTypeface(tf);//设置字体
        tvComment.setTypeface(tf);//设置字体
        tvPost.setTypeface(tf);//设置字体

        ivFollow = (ImageView) findViewById(R.id.follow);
        ivConversation = (ImageView) findViewById(R.id.ivConversation_UserDetail);
        if (currentUser.getObjectId().trim().equals(user.getObjectId().trim())){
            ivFollow.setVisibility(View.GONE);
            ivConversation.setVisibility(View.GONE);
        }



        ivFollow.getBackground().setAlpha(170);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == unFollowed) {
                    ivFollow.setBackgroundResource(R.mipmap.btnfollow);
                }
                if (msg.what == Followed) {
                    ivFollow.setBackgroundResource(R.mipmap.btnfollowed);
                }

            }
        };

        new Thread() {
            @Override
            public void run() {
                query(user, handler);
                queryFollower(user);
                queryFans(user);
                query_shoucang(user, handler);
            }
        }.start();

        //点击关注按钮
        ivFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentUser==null){
                    new AlertDialog.Builder(UserDetailActivity.this).setMessage("登录后才可关注哦")
                            .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(UserDetailActivity.this,LoginActivity.class));
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                    return;
                }

                BmobQuery<Follow_Fans> query = new BmobQuery<Follow_Fans>();
                query.addWhereEqualTo("Follower", user);

                BmobQuery<Follow_Fans> query1 = new BmobQuery<Follow_Fans>();
                query1.addWhereEqualTo("Fans", currentUser);

                List<BmobQuery<Follow_Fans>> andList = new ArrayList<BmobQuery<Follow_Fans>>();
                andList.add(query);
                andList.add(query1);

                BmobQuery<Follow_Fans> andQuery = new BmobQuery<Follow_Fans>();
                andQuery.and(andList);
                andQuery.findObjects(UserDetailActivity.this, new FindListener<Follow_Fans>() {
                    @Override
                    public void onSuccess(List<Follow_Fans> list) {
                        if (list.isEmpty()) {
                            saveFollower(user, currentUser);
                        } else {//已经关注了
                            Follow_Fans follow_fans = new Follow_Fans();
                            follow_fans.delete(UserDetailActivity.this, list.get(0).getObjectId(), new DeleteListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(UserDetailActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                                    ivFollow.setBackgroundResource(R.mipmap.btnfollow);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(UserDetailActivity.this, "取消关注失败，" + s, Toast.LENGTH_SHORT).show();
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
        //点击对话按钮，开启私聊

        ivConversation.getBackground().setAlpha(170);
        ivConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser==null){
                    new AlertDialog.Builder(UserDetailActivity.this).setMessage("登录后才可发送消息哦")
                            .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(UserDetailActivity.this,LoginActivity.class));
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                    return;
                }
                BmobIMUserInfo info;
                if (user.getHead() != null) {
                    info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getHead().getUrl());
                }
                else if (user.getUrlHead()!=null){
                    info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getUrlHead());
                }
                else {
                    info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), null);
                }
                BmobIM.getInstance().updateUserInfo(info);
                BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                    @Override
                    public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("c", bmobIMConversation);
                        Intent intent = new Intent(UserDetailActivity.this, ChatActivity.class);
                        intent.putExtras(bundle);
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("user", user);
                        intent.putExtras(bundle1);
                        startActivity(intent);

                    }
                });
            }
        });


        rv = (RecyclerView) findViewById(R.id.rv);
        adapter = new UserDongtaiAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(this));


        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("myUser", new BmobPointer(user));
        query.order("-createdAt");
        query.include("book");
        query.setLimit(3);
        query.findObjects(this, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                if (list != null) {
                    for (Comment comment : list) {
                        LatestList lists = new LatestList();
                        lists.setContent(comment.getCommentcontent());
                        lists.setCover(comment.getBook().getCover());
                        lists.setBookname(comment.getBook().getName());
                        lists.setAuthor(comment.getBook().getAuthor());
                        lists.setTime(comment.getCreatedAt());
                        lists.setType("Comment");
                        latestLists.add(lists);
                        System.out.println("time---->>>>>>>" + lists.getTime());
                    }
                    Collections.sort(latestLists, new sortByTime());
                    switch (latestLists.size()){
                        case 0:
                            break;
                        case 1:
                            latestLists1.add(latestLists.get(latestLists.size() - 1));
                            break;
                        case 2:
                            for (int i = 1; i <= 2; i++) {
                                System.out.println(i+"--length:"+latestLists.size());
                                latestLists1.add(latestLists.get(latestLists.size() - i));
                            }
                            break;
                        default:
                            for (int i = 1; i <= 3; i++) {
                                System.out.println(i+"--length:"+latestLists.size());
                                latestLists1.add(latestLists.get(latestLists.size() - i));
                            }
                            break;
                    }
                    adapter.bindData(latestLists1);
                    adapter.notifyDataSetChanged();
                    rv.setAdapter(adapter);
                }

            }

            @Override
            public void onError(int i, String s) {
                Log.e("Comment_Error", s);
            }
        });

        BmobQuery<Ground> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("user", new BmobPointer(user));
        BmobQuery<Ground> query4 = new BmobQuery<>();
        query4.addWhereEqualTo("type", "question");
        List<BmobQuery<Ground>> list = new ArrayList<>();
        list.add(query2);
        list.add(query4);
        BmobQuery<Ground> andQuery = new BmobQuery<>();
        andQuery.and(list);
        andQuery.order("-createdAt");
        andQuery.setLimit(3);
        andQuery.findObjects(this, new FindListener<Ground>() {
            @Override
            public void onSuccess(List<Ground> list) {
                if (list != null) {
                    for (Ground ground : list) {
                        LatestList lists = new LatestList();
                        lists.setContent(ground.getContent());
                        lists.setTime(ground.getCreatedAt());
                        lists.setGroundType(ground.getType());
                        lists.setType("Ground");
                        if (ground.getBook() != null) {
                            lists.setCover(ground.getBook().getCover());
                            lists.setBookname(ground.getBook().getName());
                            lists.setAuthor(ground.getBook().getAuthor());
                        }
                        System.out.println("time----" + lists.getTime());
                        latestLists.add(lists);
                    }
                    Collections.sort(latestLists, new sortByTime());
                    switch (latestLists.size()){
                        case 0:
                            break;
                        case 1:
                            latestLists1.add(latestLists.get(latestLists.size() - 1));
                            break;
                        case 2:
                            for (int i = 1; i <= 2; i++) {
                                System.out.println(i+"--length:"+latestLists.size());
                                latestLists1.add(latestLists.get(latestLists.size() - i));
                            }
                            break;
                        default:
                            for (int i = 1; i <= 3; i++) {
                                System.out.println(i+"--length:"+latestLists.size());
                                latestLists1.add(latestLists.get(latestLists.size() - i));
                            }
                            break;
                    }
                    adapter.bindData(latestLists1);
                    adapter.notifyDataSetChanged();
                    rv.setAdapter(adapter);
                }

            }

            @Override
            public void onError(int i, String s) {
                Log.e("Ground_Error", s);
            }
        });
//
        BmobQuery<Likes> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("myUser", new BmobPointer(user));
        BmobQuery<Likes> query3 = new BmobQuery<>();
        query3.addWhereExists("book");
        List<BmobQuery<Likes>> list1 = new ArrayList<>();
        list1.add(query1);
        list1.add(query3);
        BmobQuery<Likes> andQuery1 = new BmobQuery<>();
        andQuery1.and(list1);
        andQuery1.order("-createdAt");
        andQuery1.setLimit(3);
        andQuery1.findObjects(this, new FindListener<Likes>() {
            @Override
            public void onSuccess(List<Likes> list) {
                if (list != null) {
                    for (Likes likes : list) {
                        LatestList lists = new LatestList();
                        lists.setLike(likes.isLike());
                        lists.setType("Likes");
                        lists.setTime(likes.getCreatedAt());
                        lists.setCover(likes.getBook().getCover());
                        lists.setBookname(likes.getBook().getName());
                        lists.setAuthor(likes.getBook().getAuthor());
                        latestLists.add(lists);
                    }
                    Collections.sort(latestLists, new sortByTime());
                    switch (latestLists.size()){
                        case 0:
                            break;
                        case 1:
                            latestLists1.add(latestLists.get(latestLists.size() - 1));
                            break;
                        case 2:
                            for (int i = 1; i <= 2; i++) {
                                System.out.println(i+"--length:"+latestLists.size());
                                latestLists1.add(latestLists.get(latestLists.size() - i));
                            }
                            break;
                        default:
                            for (int i = 1; i <= 3; i++) {
                                System.out.println(i+"--length:"+latestLists.size());
                                latestLists1.add(latestLists.get(latestLists.size() - i));
                            }
                            break;
                    }
                    adapter.bindData(latestLists1);
                    adapter.notifyDataSetChanged();
                    rv.setAdapter(adapter);
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });



    }





    class sortByTime implements Comparator<LatestList> {

        @Override
        public int compare(LatestList o1, LatestList o2) {
            long t1, t2;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date d1 = format.parse(o1.getTime());
                t1 = d1.getTime();

                Date d2 = format.parse(o2.getTime());
                t2 = d2.getTime();
                if (t1 < t2) {
                    return -1;
                } else if (t1 > t2) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return 1;
        }
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        final MyUser user = (MyUser) getIntent().getSerializableExtra("user");

        switch (v.getId()) {

            case R.id.layout_Guangzhu:
                Intent intent = new Intent(UserDetailActivity.this,FollowActivity.class);
                intent.putExtra("isCurrentUser",false);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",user);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.layout_Fans:
                Intent intent1 = new Intent(UserDetailActivity.this,FansActivity.class);
                intent1.putExtra("isCurrentUser",false);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("user",user);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;
            case R.id.layout_Shoucang:
//                Intent intent2 = new Intent(UserDetailActivity.this,ShoucangActivity.class);
//                intent2.putExtra("isCurrentUser",false);
//                Bundle bundle2 = new Bundle();
//                bundle2.putSerializable("user",user);
//                intent2.putExtras(bundle2);
//                startActivity(intent2);
                break;

            case R.id.btnOtherPost:
                Intent intent3 = new Intent(UserDetailActivity.this,MyPostActivity.class);
                intent3.putExtra("isCurrentUser",false);
                Bundle bundle3 = new Bundle();
                bundle3.putSerializable("user",user);
                intent3.putExtras(bundle3);
                startActivity(intent3);
                break;
            case R.id.btnothercomment:
                Intent intent4 = new Intent(UserDetailActivity.this,OthersCommentActivity.class);
                Bundle bundle4 = new Bundle();
                bundle4.putSerializable("user",user);
                intent4.putExtras(bundle4);
                startActivity(intent4);
                break;
        }
    }


    //先找找该用户是否已经关注了，然后再通过handler来设置是否关注的图标
    private void query(MyUser user, final Handler handler) {
        MyUser currentUser = BmobUser.getCurrentUser(this, MyUser.class);
        BmobQuery<Follow_Fans> query = new BmobQuery<Follow_Fans>();
        query.addWhereEqualTo("Follower", user);

        BmobQuery<Follow_Fans> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("Fans", currentUser);

        List<BmobQuery<Follow_Fans>> andList = new ArrayList<>();
        andList.add(query);
        andList.add(query1);

        BmobQuery<Follow_Fans> andQuery = new BmobQuery<>();
        andQuery.and(andList);
        andQuery.findObjects(UserDetailActivity.this, new FindListener<Follow_Fans>() {
            @Override
            public void onSuccess(List<Follow_Fans> list) {
                if (list.isEmpty()) {
                    handler.sendEmptyMessage(unFollowed);
                } else {
                    handler.sendEmptyMessage(Followed);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    //获取关注的人数
    public void queryFollower(MyUser user) {
        BmobQuery<Follow_Fans> query = new BmobQuery<>();
        query.addWhereEqualTo("Fans", new BmobPointer(user));
        query.findObjects(this, new FindListener<Follow_Fans>() {
            @Override
            public void onSuccess(List<Follow_Fans> list) {
                tvFollower_count.setText(String.valueOf(list.size()));

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    //获取粉丝的人数
    public void queryFans(MyUser user) {
        BmobQuery<Follow_Fans> query = new BmobQuery<>();
        query.addWhereEqualTo("Follower", new BmobPointer(user));
        query.findObjects(this, new FindListener<Follow_Fans>() {
            @Override
            public void onSuccess(List<Follow_Fans> list) {
                tvFans_count.setText(String.valueOf(list.size()));


            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    //获取收藏的数目
    private void query_shoucang(MyUser user, final Handler handler) {
        BmobQuery<Shoucang> query = new BmobQuery<>();
        query.addWhereEqualTo("myUser", new BmobPointer(user));
        query.findObjects(this, new FindListener<Shoucang>() {
            @Override
            public void onSuccess(List<Shoucang> list) {
                //显示个人中心中收藏的数量
                tvShoucang_count.setText(String.valueOf(list.size()));

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


    //关注成功
    private void saveFollower(final MyUser user, MyUser currentUser) {
        Follow_Fans follow_fans = new Follow_Fans();
        follow_fans.setFollower(user);
        follow_fans.setFans(currentUser);
        follow_fans.save(UserDetailActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                sendFollowMessage(user);
                ivFollow.setBackgroundResource(R.mipmap.btnfollowed);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserDetailActivity.this, "关注失败" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }


    //发送“有新粉丝”的消息给对方
    public void sendFollowMessage(MyUser myUser) {
        MyUser currentUser = BmobUser.getCurrentUser(this, MyUser.class);
        BmobIMUserInfo info;
        if (myUser.getHead() != null) {
            info = new BmobIMUserInfo(myUser.getObjectId(), myUser.getUsername(), myUser.getHead().getUrl());
        } else {

            info = new BmobIMUserInfo(myUser.getObjectId(), myUser.getUsername(), null);
        }

        BmobIM.getInstance().updateUserInfo(info);
        BmobIMConversation conversation = BmobIM.getInstance().startPrivateConversation(info, true, null);
        BmobIMConversation conversation1 = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversation);//创建新的会话
        AddFollowerMessage message = new AddFollowerMessage();
        Map<String, Object> map = new HashMap<>();
        map.put("name", currentUser.getUsername());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
        if (currentUser.getHead() != null) {
            map.put("avatar", currentUser.getHead().getUrl());//发送者的头像
        } else {
            map.put("avatar", null);
        }
        map.put("uid", currentUser.getObjectId());//发送者的uid
        message.setExtraMap(map);
        conversation1.sendMessage(message, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                if (e == null) {
                    Toast.makeText(UserDetailActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserDetailActivity.this, "关注失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
