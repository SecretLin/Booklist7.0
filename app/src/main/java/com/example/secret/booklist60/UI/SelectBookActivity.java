package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.secret.booklist60.Adapter.BookAdapter;
import com.example.secret.booklist60.DataBase.Book;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.RecyclerItemClickListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Secret on 2016/12/13.
 */

public class SelectBookActivity extends Activity {
    private RecyclerView rv;
    private BookAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectbook);

        rv = (RecyclerView) findViewById(R.id.rv);
        adapter = new BookAdapter(this);
        query();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, rv,
                new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("book",adapter.getItem(position));
                //把返回数据存入Intent
                intent.putExtras(bundle);
                //设置返回数据
                SelectBookActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                SelectBookActivity.this.finish();
////                Intent intent = new Intent();
//                //把返回数据存入Intent
//                intent.putExtra("result", "My name is linjiqin");
//                //设置返回数据
//                SelectBookActivity.this.setResult(RESULT_OK, intent);
//                //关闭Activity
//                SelectBookActivity.this.finish();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }


    private void query() {
        final ProgressDialog progressDialog = ProgressDialog.show(this,null,"正在加载...");
        BmobQuery<Book> query = new BmobQuery<>();
        query.findObjects(this, new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> list) {
                adapter.bindData(list);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onError(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(SelectBookActivity.this,"加载失败,"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void finish(){
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(0,R.anim.push_buttom_out);
    }
}
