package com.example.secret.booklist60.UI.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secret.booklist60.Adapter.GroundAdapter;
import com.example.secret.booklist60.Adapter.NewBookAdapter;
import com.example.secret.booklist60.DataBase.Book;
import com.example.secret.booklist60.DataBase.Follow_Fans;
import com.example.secret.booklist60.DataBase.Ground;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.UI.GroundPinLunActivity;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.RecyclerItemClickListener;
import com.example.secret.booklist60.UI.AddQuestionActivity;
import com.example.secret.booklist60.UI.AddShareActivity;
import com.example.secret.booklist60.UI.HitGroundActivity;
import com.example.secret.booklist60.UI.LoginActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Secret on 2016/8/27.
 * 这是一个发现页面
 */
public class GroundFragment extends Fragment {
    private ViewPager vp;
    private ImageView[] imageViews = null;
    private ImageView imageView = null;
    /*线程安全中的一种原子加减
       初始化what为0，代表在第一个位置
     */
    private AtomicInteger what = new AtomicInteger(0);
    private boolean isContinue = true;
    private SwipeRefreshLayout refreshLayout;

    //    private FloatingActionButton btnAdd;
    private RecyclerView rv,rvNewbook,rvTimeSort;
    private GroundAdapter adapter,TimeAdapter;
    public static FloatingActionMenu bigFAB;
    private FloatingActionButton btnQuestion,btnShare;
    private TextView tvNewbook,tvHit,tvMore;
    private NewBookAdapter newBookAdapter;
//    private SmartRefreshLayout refreshLayout;
    public int clickMoreCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ground, container, false);
        init(view);
        initViewPager(view);


        return view;
    }

    private void init(View view) {

        //新书推荐
        tvNewbook = (TextView) view.findViewById(R.id.tvNewBook);
        AssetManager mgr = getActivity().getAssets();//得到AssetManager
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/msyh.ttc");//根据路径得到Typeface
        tvNewbook.setTypeface(tf);//设置字体

        rvNewbook = (RecyclerView) view.findViewById(R.id.rvNewBook);
        newBookAdapter = new NewBookAdapter(getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvNewbook.setAdapter(newBookAdapter);
        rvNewbook.setLayoutManager(manager);
        queryNewbook();

        //热门推荐
        tvMore = (TextView) view.findViewById(R.id.tv_more);
        tvHit = (TextView) view.findViewById(R.id.tvHit);
        tvMore.setTypeface(tf);//设置字体
        tvHit.setTypeface(tf);//设置字体
        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), HitGroundActivity.class));
            }
        });

        rv = (RecyclerView) view.findViewById(R.id.rv);
        adapter = new GroundAdapter(getContext());
        query();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final Intent intent = new Intent(getActivity(),GroundPinLunActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Ground",adapter.getItem(position));
                intent.putExtras(bundle);
                Ground ground = adapter.getItem(position);
                Integer count = ground.getCount();
                if (count!=null){
                    count++;
                }else {
                    count = 1;
                }
                ground.setCount(count);
                ground.update(getActivity(), ground.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        System.out.println("更新成功--------");
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        //按时间日期关注的人排序
        rvTimeSort = (RecyclerView) view.findViewById(R.id.rvTimeSort);
        TimeAdapter = new GroundAdapter(getContext());
        rvTimeSort.setAdapter(TimeAdapter);
        rvTimeSort.setLayoutManager(new LinearLayoutManager(getContext()));
//        rvTimeSort.setRefreshing(true);
//        rvTimeSort.setFooterViewText("正在加载");
        queryDongtai();
        rvTimeSort.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final Intent intent = new Intent(getActivity(),GroundPinLunActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Ground",adapter.getItem(position));
                intent.putExtras(bundle);
                Ground ground = adapter.getItem(position);
                Integer count = ground.getCount();
                if (count!=null){
                    count++;
                }else {
                    count = 1;
                }
                ground.setCount(count);
                ground.update(getActivity(), ground.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        System.out.println("更新成功--------");
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        bigFAB = (FloatingActionMenu) view.findViewById(R.id.bigFAB);
        bigFAB.setClosedOnTouchOutside(true);
        btnQuestion = (FloatingActionButton) view.findViewById(R.id.btnQuestion);
        btnShare = (FloatingActionButton) view.findViewById(R.id.btnShare);

        final MyUser currentUser = BmobUser.getCurrentUser(getContext(),MyUser.class);

        btnQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser==null){
                    new AlertDialog.Builder(getActivity()).setMessage("登录后才可发表提问哦")
                            .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(getActivity(),LoginActivity.class));
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                    return;
                }
                startActivityForResult(new Intent(getContext(), AddQuestionActivity.class),1);
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentUser==null){
                    new AlertDialog.Builder(getActivity()).setMessage("登录后才可发表分享哦")
                            .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(getActivity(),LoginActivity.class));
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                    return;
                }
                startActivityForResult(new Intent(getContext(), AddShareActivity.class),2);
            }
        });


        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
                queryDongtai();
                queryNewbook();
                refreshLayout.setRefreshing(false);
            }
        });

    }



    private void queryNewbook() {
        BmobQuery<Book> query = new BmobQuery<>();
        query.setLimit(10);
        query.findObjects(getContext(), new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> list) {
                if (!list.isEmpty()){
                    newBookAdapter.bindData(list);
                    newBookAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int i, String s) {
                System.out.println("新书推荐出错啦！"+s);
            }
        });
    }

    private void query() {
        BmobQuery<Ground> query = new BmobQuery<>();

        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
        boolean isCache = query.hasCachedResult(getContext(),Ground.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.include("user,booklist");
        query.order("-count");
        query.setLimit(3);
        query.findObjects(getContext(), new FindListener<Ground>() {
            @Override
            public void onSuccess(List<Ground> list) {
                adapter.bindData(list);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    List<MyUser> followers = new ArrayList<>();
    List<Ground> dongtais = new ArrayList<>();
    private void queryDongtai(){
        BmobQuery<Follow_Fans> queryFollower = new BmobQuery<>();
        MyUser currentUser = BmobUser.getCurrentUser(getContext(),MyUser.class);
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
        boolean isCache = queryFollower.hasCachedResult(getContext(),Follow_Fans.class);
        if(isCache){
            queryFollower.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            queryFollower.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        queryFollower.addWhereEqualTo("Fans",new BmobPointer(currentUser));
        queryFollower.findObjects(getContext(), new FindListener<Follow_Fans>() {
            @Override
            public void onSuccess(List<Follow_Fans> list) {
                if (!list.isEmpty()){
                    for (Follow_Fans ff : list){
                        followers.add(ff.getFollower());
                    }
                    System.out.println("---------size:"+list.size());
                }
                BmobQuery<Ground> query = new BmobQuery<>();
                query.addWhereContainedIn("user",followers);
                query.include("user,booklist");
                boolean isCache = query.hasCachedResult(getContext(),Ground.class);
                if(isCache){
                    query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
                }else{
                    query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
                }
                query.findObjects(getActivity(), new FindListener<Ground>() {
                    @Override
                    public void onSuccess(List<Ground> list) {
                        dongtais.addAll(list);
                        BmobQuery<Ground> query1 = new BmobQuery<Ground>();
                        query1.order("-createdAt");
                        query1.include("user,booklist");
                        query1.findObjects(getActivity(), new FindListener<Ground>() {
                            @Override
                            public void onSuccess(List<Ground> list) {
                                dongtais.addAll(list);
                                TimeAdapter.bindData(dongtais);
                                TimeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(int i, String s) {

                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == -1){
            query();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume(){
        super.onResume();
//        query();
        if (bigFAB.isOpened()){
            bigFAB.close(true);
        }
    }
    private void initViewPager(View view) {
        vp = (ViewPager) view.findViewById(R.id.vp);
        ViewGroup group = (ViewGroup) view.findViewById(R.id.layout_point);//LinearLayout布局

        //      这里存放的是三张广告背景
        List<View> advPics = new ArrayList<View>();

        ImageView img1 = new ImageView(getContext());
        img1.setBackgroundResource(R.mipmap.activity_one);
        advPics.add(img1);

        ImageView img2 = new ImageView(getContext());
        img2.setBackgroundResource(R.mipmap.activity_two);
        advPics.add(img2);

        ImageView img3 = new ImageView(getContext());
        img3.setBackgroundResource(R.mipmap.activity_three);
        advPics.add(img3);


        //初始化三个小白点
        imageViews = new ImageView[advPics.size()];
        for (int i = 0; i < advPics.size(); i++) {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(50, 50));//三个小白点的大小
            imageView.setPadding(5, 5, 5, 5);//整个linearlayout布局上下左右相差5px
            imageViews[i] = imageView;
            if (i == 0) {
                imageViews[i]
                        .setBackgroundResource(R.mipmap.vp_point_selected);
            } else {
                imageViews[i]
                        .setBackgroundResource(R.mipmap.vp_point_unselected);
            }
            group.addView(imageViews[i]);//将三个小白点添加到LinearLayout中
        }

        vp.setAdapter(new AdvAdapter(advPics));
        vp.setOnPageChangeListener(new GuidePageChangeListener());
        vp.setOnTouchListener(new View.OnTouchListener() {
            //设置滑动事件
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        isContinue = false;//如果按下滑动，那么就不会自动滑动
                        break;
                    case MotionEvent.ACTION_UP:
                        isContinue = true;
                        break;
                    default:
                        isContinue = true;
                        break;
                }
                return false;
            }
        });
        new Thread(new Runnable() {

            @Override
            public void run() {
                //不断循环，先把获取到的传给handler，告诉它现在在第几张图，然后继续下一张图，停三秒后才显示
                while (true) {
                    if (isContinue) {
                        viewHandler.sendEmptyMessage(what.get());
                        whatOption();//对取得的值进行操作，并休眠3秒后继续执行
                    }
                }
            }

        }).start();
    }


    private void whatOption() {
        what.incrementAndGet();//加一后再取值，类似于++i
        if (what.get() > imageViews.length - 1) {//如果取得的值大于2
            what.getAndAdd(-3);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
    }

    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            vp.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }

    };

    private final class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            what.getAndSet(arg0);
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0]
                        .setBackgroundResource(R.mipmap.vp_point_selected);
                if (arg0 != i) {
                    imageViews[i]
                            .setBackgroundResource(R.mipmap.vp_point_unselected);
                }
            }

        }

    }

    private final class AdvAdapter extends PagerAdapter {
        private List<View> views = null;

        public AdvAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

    }
    public void toast(String s){
        Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
    }


}
