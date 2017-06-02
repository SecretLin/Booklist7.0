package com.example.secret.booklist60.UI.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.secret.booklist60.Adapter.BookAdapter;
import com.example.secret.booklist60.DataBase.Book;
import com.example.secret.booklist60.DataBase.History;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.RecyclerItemClickListener;
import com.example.secret.booklist60.UI.BookListDetailActivity;
import com.example.secret.booklist60.UI.SearchActivity;
import com.example.secret.booklist60.UI.SelectKindsActivity;
import com.example.secret.booklist60.org.afinal.simplecache.ACache;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Secret on 2016/8/27.
 * 书单排列（书城）
 */
public class BookFragment extends Fragment implements View.OnClickListener {
    RecyclerView rv;
    LinearLayoutManager layoutManager;
    BookAdapter adapter;
    ImageButton btnLevelSort, btnCountSort, btnKinds,btnSearch;
    String id;
    SwipeRefreshLayout refreshLayout;

    ACache aCache;
    JSONArray arrays = null;
    public ProgressDialog progressDialog;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_booklist, container, false);
        init(view);
        progressDialog = ProgressDialog.show(getContext(),null,"正在加载....");
        return view;
    }

    private void init(View view) {
        btnLevelSort = (ImageButton) view.findViewById(R.id.btnScoreSort);
        btnCountSort = (ImageButton) view.findViewById(R.id.btnCountSort);
        btnSearch = (ImageButton) view.findViewById(R.id.btnSearch);
        btnKinds = (ImageButton) view.findViewById(R.id.kinds);
        btnKinds.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnLevelSort.setOnClickListener(this);
        btnCountSort.setOnClickListener(this);

        query("-score");//根据豆瓣评分高低排列

        rv = (RecyclerView) view.findViewById(R.id.rv);
        adapter = new BookAdapter(getContext());
        rv.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        rv.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                isExist(adapter.getItem(position));

                Intent intent = new Intent(getContext(), BookListDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Book", adapter.getItem(position));
                intent.putExtras(bundle);


                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                getItemPositionAndBack();
            }
        });


//        aCache = ACache.get(getContext());
        //下拉刷新
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                query("-score");
                btnLevelSort.setBackgroundResource(R.mipmap.score_pressed);
                btnCountSort.setBackgroundResource(R.mipmap.tuijian);
                arrays = null;
                refreshLayout.setRefreshing(false);

            }
        });



    }


    int lastOffset,lastPosition;
    private void getItemPositionAndBack() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
        //获取可视的第一个view
        View topView = layoutManager.getChildAt(0);
        if(topView != null) {
            //获取与该view的顶部的偏移量
            lastOffset = topView.getTop();
            //得到该View的数组位置
            lastPosition = layoutManager.getPosition(topView);
        }
        System.out.println("scroll position:"+lastPosition+","+lastOffset);
    }

    /**
     * 让RecyclerView滚动到指定位置
     */
    private void scrollToPosition() {
        if(rv.getLayoutManager() != null && lastPosition >= 0) {
            ((LinearLayoutManager) rv.getLayoutManager()).scrollToPositionWithOffset(lastPosition, lastOffset);
        }
        System.out.println("scroll:"+lastPosition+","+lastOffset);
    }

    //从数据库中获取数据
    public void query(String order) {

        if (Bmob.getConnectTimeout()>15){
            progressDialog.dismiss();
            System.out.println("-------------无法连接到网络----------");
            Toast.makeText(getContext(),"无法连接到网络",Toast.LENGTH_LONG).show();
            arrays = null;
        }
        System.out.println("-------------------getConnectTimeout:"+Bmob.getConnectTimeout());

        //根据不同的分类选择获取不同数据
        String kinds = getActivity().getIntent().getStringExtra("Kinds");
        if (kinds != null) {
            queryByKinds(order,kinds);
        }
        //默认情况下的排序
        else {
            try{
                BmobQuery<Book> query = new BmobQuery<>();

                //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
                boolean isCache = query.hasCachedResult(getContext(),Book.class);
                System.out.println("------------isCache:"+isCache);
                if(isCache){
                    query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
                }else{
                    query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
                }
                query.order(order);
                query.findObjects(getActivity(), new FindListener<Book>() {
                    @Override
                    public void onSuccess(List<Book> list) {

                        adapter.bindData(list);
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });

            }catch (Exception ex){
                System.out.println("----------ex:"+ex.getMessage());
            }

        }
    }

    //属于前端种类的排序
    public void queryByKinds(String order,String type) {
        BmobQuery<Book> query = new BmobQuery<>();
        query.order(order);
        query.addWhereEqualTo("type", type);
        query.findObjects(getActivity(), new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> list) {
                adapter.bindData(list);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onError(int i, String s) {
                Log.e("error:", s);
            }
        });

    }

    //检查历史记录中是否存在此书，若存在则删除重新添加
    public void isExist(final Book book) {
        System.out.println("isExist");
        final MyUser myUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        final History history = new History();

        BmobQuery<History> query = new BmobQuery<>();
        query.addWhereEqualTo("myUser", new BmobPointer(myUser));

        BmobQuery<History> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("book", new BmobPointer(book));

        List<BmobQuery<History>> andQuerys = new ArrayList<BmobQuery<History>>();
        andQuerys.add(query);
        andQuerys.add(query1);

        BmobQuery<History> querySum = new BmobQuery<>();
        querySum.and(andQuerys);
        querySum.findObjects(getActivity(), new FindListener<History>() {
            @Override
            public void onSuccess(List<History> list) {

                if (!list.isEmpty()) {
                    System.out.println(list.size());
                    System.out.println("Before:" + list.get(0).getObjectId());

                    id = list.get(0).getObjectId();
                    System.out.println("After:" + id);
                    history.delete(getActivity(), id, new DeleteListener() {
                        @Override
                        public void onSuccess() {
                            Log.i("Delete", "Succeed!");

                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.e("Error:", s);
                        }
                    });

                }
                History history1 = new History();
                history1.setMyUser(myUser);
                history1.setBook(book);
                history1.save(getActivity(), new SaveListener() {
                    @Override
                    public void onSuccess() {
                        System.out.println("添加到历史记录成功");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        System.out.println("添加到历史记录失败，" + s);
                    }
                });
            }


            @Override
            public void onError(int i, String s) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //根据豆瓣评分排序
            case R.id.btnScoreSort:
                arrays = null;
                progressDialog = ProgressDialog.show(getContext(),null,"正在加载....");
                query("-score");
                btnLevelSort.setBackgroundResource(R.mipmap.score_pressed);
                btnCountSort.setBackgroundResource(R.mipmap.tuijian);
                break;
            //根据推荐数量排序
            case R.id.btnCountSort:
                arrays = null;
                progressDialog = ProgressDialog.show(getContext(),null,"正在加载....");
                query("-count");
                btnLevelSort.setBackgroundResource(R.mipmap.score);
                btnCountSort.setBackgroundResource(R.mipmap.tuijian_pressed);
                break;
            //搜索
            case R.id.btnSearch:

                startActivity(new Intent(getContext(), SearchActivity.class));

                break;
            //选择种类
            case R.id.kinds:

                startActivity(new Intent(getContext(), SelectKindsActivity.class));

                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        init(getView());
        query("-score");
        scrollToPosition();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //aCache.clear();
        BmobQuery.clearAllCachedResults(getContext());
    }

}

