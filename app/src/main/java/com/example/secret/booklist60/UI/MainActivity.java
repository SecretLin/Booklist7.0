package com.example.secret.booklist60.UI;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.ExitApplication;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.UI.Fragment.BookFragment;
import com.example.secret.booklist60.UI.Fragment.GroundFragment;
import com.example.secret.booklist60.UI.Fragment.MeFragment;
import com.example.secret.booklist60.event.MapEvent;
import com.example.secret.booklist60.event.UpdateConversationEvent;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

import de.greenrobot.event.EventBus;
/*
   主要将“发现”，“书单”,“个人中心”三个fragment放在此Activity
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    ViewPager vp;
    LinearLayout l1, l2, l3;
    ImageView iv1, iv2, iv3;
    TextView tv1, tv2, tv3;
    List<Fragment> fragments;
    FragmentPagerAdapter pagerAdapter;
    Fragment fragment;
    int currentItem;
    boolean isMe = false;
    int init = 0;
    private int lastValue = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        init();
        initEvent();
        Selected(1);

        int page = getIntent().getIntExtra("page", 0);
        if (page == 1) {
            Selected(1);
        }

        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        MyUser currentUser = BmobUser.getCurrentUser(this, MyUser.class);
        if (currentUser != null) {
            BmobIM.connect(currentUser.getObjectId(), new ConnectListener() {
                @Override
                public void done(String s, BmobException e) {

                    System.out.println("connect-->" + s);

                }
            });
            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus status) {
                    //    Log.d("test","长连接状态：" + status.getMsg());
                    Toast.makeText(MainActivity.this, "长连接状态：" + status.getMsg(), Toast.LENGTH_SHORT).show();
                    if (status.getMsg().equals("connected")) {

                    }
                    EventBus.getDefault().post(new UpdateConversationEvent("update"));
                }
            });
        }


        ExitApplication.getInstance().addActivities(this);
    }

    public void onEventMainThread(MapEvent event) {

        //收到消息提醒
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker(event.getMap().get("title") + "：" + event.getMap().get("content"));
        builder.setSmallIcon(R.mipmap.icon);
        builder.setContentTitle(event.getMap().get("title"));
        builder.setContentText(event.getMap().get("content"));
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long[] v = {0, 1000};
        notification.vibrate = v;
        if (event.getMap().get("type").equals("chat")) {
            //接受到聊天信息，点击跳转聊天页面
            final Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            String username = event.getMap().get("username");
            String id = event.getMap().get("userId");
            String avatar = event.getMap().get("avatar");
            BmobIMUserInfo info = new BmobIMUserInfo(id, username, avatar);
            BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                @Override
                public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", bmobIMConversation);
                    intent.putExtras(bundle);
                }
            });

            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            notification.contentIntent = pendingIntent;
            manager.notify(1, notification);
        } else if (event.getMap().get("type").equals("follow")) {
            //接收到有新粉丝的消息，点击跳转粉丝界面
            final Intent intent1 = new Intent(MainActivity.this, FansActivity.class);
            PendingIntent pendingIntent1 = PendingIntent.getActivity(MainActivity.this, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
            notification.contentIntent = pendingIntent1;
            manager.notify(2, notification);
        } else if (event.getMap().get("type").equals("new_Comment")) {
            //接收到有新评论的消息，点击跳转到“我接收的评论”界面
            final Intent intent1 = new Intent(MainActivity.this, ReceiveComment.class);
            PendingIntent pendingIntent1 = PendingIntent.getActivity(MainActivity.this, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
            notification.contentIntent = pendingIntent1;
            manager.notify(3, notification);
        }


    }


    private void init() {
        vp = (ViewPager) findViewById(R.id.vp);
        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (LinearLayout) findViewById(R.id.l2);
        l3 = (LinearLayout) findViewById(R.id.l3);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);

        fragments = new ArrayList<>();
        Fragment groundFragment = new GroundFragment();
        Fragment booklistFragment = new BookFragment();
        Fragment meFragment = new MeFragment();
        fragments.add(groundFragment);
        fragments.add(booklistFragment);
        fragments.add(meFragment);

        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                fragment = fragments.get(position);
                System.out.println("getItem------------------------" + position);
                return fragment;
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                //super.destroyItem(container,position,object);
            }

        };

        vp.setAdapter(pagerAdapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MyUser currentUser = BmobUser.getCurrentUser(MainActivity.this,MyUser.class);
                if (currentUser==null){
                    if (position==2){
                        new AlertDialog.Builder(MainActivity.this).setMessage("登录后才可前往个人中心哦")
                                .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        vp.setCurrentItem(1);

                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                }

                currentItem = vp.getCurrentItem();
                ImagePressed(currentItem);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    isScrolling = true;
                } else {
                    isScrolling = false;
                }
            }
        });
    }

    boolean isScrolling;

    private void initEvent() {
        l1.setOnClickListener(this);
        l2.setOnClickListener(this);
        l3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.l1:
                Selected(0);
                break;
            case R.id.l2:
                Selected(1);
                break;
            case R.id.l3:

                Selected(2);
                break;
        }
    }

    public void ReSetImage() {
        iv1.setImageResource(R.mipmap.faxian);
        iv2.setImageResource(R.mipmap.booklist);
        iv3.setImageResource(R.mipmap.me);
    }

    public void Selected(int i) {

        MyUser currentUser = BmobUser.getCurrentUser(MainActivity.this, MyUser.class);
//        System.out.println("---------------------"+currentUser.getUsername());
        if (currentUser == null && i == 2) {
            new AlertDialog.Builder(MainActivity.this).setMessage("登录后才可前往个人中心哦")
                    .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));

                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
            return;
        }

        ImagePressed(i);
        vp.setCurrentItem(i);
    }

    public void ImagePressed(int i) {
        ReSetImage();
        switch (i) {
            case 0:
                iv1.setImageResource(R.mipmap.faxian_pressed);

                break;
            case 1:
                iv2.setImageResource(R.mipmap.booklist_pressed);
                break;
            case 2:
                iv3.setImageResource(R.mipmap.me_pressed);
                break;
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        BmobQuery.clearAllCachedResults(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String kinds = getIntent().getStringExtra("Kinds");
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (MeFragment.etUsername.hasFocus()) {
                MeFragment.etUsername.setVisibility(View.GONE);
                MeFragment.tvUsername.setVisibility(View.VISIBLE);
                MeFragment.btnModifyName.setVisibility(View.VISIBLE);
                return true;
            } else if (GroundFragment.bigFAB.isOpened()) {
                GroundFragment.bigFAB.close(true);
                return true;
            }
//            else if (kinds!=null){
//
//            }
            else {

                Intent home = new Intent(Intent.ACTION_MAIN);

                home.addCategory(Intent.CATEGORY_HOME);

                startActivity(home);
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);

    }

    public static Tencent mTencent;

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



}
