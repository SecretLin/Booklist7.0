package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.secret.booklist60.Adapter.BookAdapter;
import com.example.secret.booklist60.DataBase.Book;
import com.example.secret.booklist60.DataBase.Booklist;
import com.example.secret.booklist60.DataBase.History;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.DataBase.Relation_booklist;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class BookListDetailActivity extends Activity {
    private RecyclerView rv;
    private BookAdapter adapter;
    private TextView tvTitle;
    private ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_detail);



        rv = (RecyclerView) findViewById(R.id.rv);
        adapter = new BookAdapter(this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                isExist(adapter.getItem(position));

                Intent intent = new Intent(BookListDetailActivity.this, BookDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Book", adapter.getItem(position));
                intent.putExtras(bundle);


                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        Booklist booklist = (Booklist) getIntent().getSerializableExtra("Booklist");

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(booklist.getBooklistName());

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        query(booklist);
    }
    private List<Book> books = new ArrayList<>();
    private void query(Booklist booklist) {

        BmobQuery<Relation_booklist> query = new BmobQuery<>();
        query.addWhereEqualTo("booklist",new BmobPointer(booklist));
        query.include("book");
        query.order("-createdAt");
        query.findObjects(this, new FindListener<Relation_booklist>() {
            @Override
            public void onSuccess(List<Relation_booklist> list) {
                for (Relation_booklist booklist1:list){
                    books.add(booklist1.getBook());
                    System.out.println("--------------objectId:"+booklist1.getBook().getObjectId());
                }
                adapter.bindData(books);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
    String id;
    //检查历史记录中是否存在此书，若存在则删除重新添加
    public void isExist(final Book book) {
        System.out.println("isExist");
        final MyUser myUser = BmobUser.getCurrentUser(this, MyUser.class);
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
        querySum.findObjects(this, new FindListener<History>() {
            @Override
            public void onSuccess(List<History> list) {

                if (!list.isEmpty()) {
                    System.out.println(list.size());
                    System.out.println("Before:" + list.get(0).getObjectId());

                    id = list.get(0).getObjectId();
                    System.out.println("After:" + id);
                    history.delete(BookListDetailActivity.this, id, new DeleteListener() {
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
                history1.save(BookListDetailActivity.this, new SaveListener() {
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
}
