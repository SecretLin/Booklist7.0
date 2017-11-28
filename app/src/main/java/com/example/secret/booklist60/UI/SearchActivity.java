package com.example.secret.booklist60.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.secret.booklist60.Adapter.SearchAdapter;
import com.example.secret.booklist60.DataBase.Book;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.ExitApplication;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
/*
    搜索书本界面
 */

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    LinearLayoutManager layout;
    RecyclerView rv;
    SearchAdapter adapter;
    TextView tvFeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        rv = (RecyclerView) findViewById(R.id.rv_Search);
        adapter = new SearchAdapter(this);
        rv.setAdapter(adapter);
        layout = new LinearLayoutManager(this);
        rv.setLayoutManager(layout);

        rv.addOnItemTouchListener(new RecyclerItemClickListener(SearchActivity.this, rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view,int position) {

                Intent intent = new Intent(SearchActivity.this, BookDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Book",adapter.getItem(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        tvFeedback = (TextView) findViewById(R.id.tvFankui);
        tvFeedback.setVisibility(View.GONE);

        ExitApplication.getInstance().addActivities(this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        item.expandActionView();


        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {

                        BmobQuery<Book> query = new BmobQuery<>();
                        query.findObjects(SearchActivity.this, new FindListener<Book>() {
                            @Override
                            public void onSuccess(List<Book> list) {
                                adapter.setFilter(list);
                            }

                            @Override
                            public void onError(int i, String s) {
                                System.out.println("error:"+s);
                            }
                        });
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        //按了搜索后，会再列表最下面显示"反馈"
        tvFeedback.setVisibility(View.VISIBLE);
        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUser currentUser = BmobUser.getCurrentUser(SearchActivity.this,MyUser.class);
                if (currentUser==null){
                    new AlertDialog.Builder(SearchActivity.this).setMessage("登录后才可反映哦")
                            .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(SearchActivity.this,LoginActivity.class));
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                    return;
                }
                Intent intent = new Intent(SearchActivity.this,FeedBackActivity.class);
                intent.putExtra("type","search");
                startActivity(intent);
            }
        });
        return true;
    }
    // 用户输入字符时激发该方法
    @Override
    public boolean onQueryTextChange(final String s) {

        if (!TextUtils.isEmpty(s)){
            final List<Book> filteredDataList;
            filteredDataList=new ArrayList<>();
            BmobQuery<Book> query = new BmobQuery<>();
//            query.addWhereMatches("name",s);
            query.findObjects(SearchActivity.this, new FindListener<Book>() {
                @Override
                public void onSuccess(List<Book> list) {
                    for (Book book :list){

                        String name = book.getName();
                        if (name.toLowerCase().contains(s.toLowerCase())){
                            filteredDataList.add(book);
                        }


                    }
                    System.out.println("Count:"+filteredDataList.size());
                    adapter.setFilter(filteredDataList);
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }

        return true;
    }

}
