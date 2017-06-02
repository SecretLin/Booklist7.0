package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.secret.booklist60.DataBase.Booklist;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.DataBase.Relation_booklist;
import com.example.secret.booklist60.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Secret on 2017/5/31.
 */

public class BookListAdapter extends RecyclerView.Adapter {

    private List<Booklist> list = new ArrayList<>();
    private Context context;

    public BookListAdapter(Context context){
        this.context = context;
    }
    public void bindData(List<Booklist> list){
        this.list.clear();
        if (!list.isEmpty()){
            this.list = list;
        }




    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        BookListHolder bookListHolder = new BookListHolder(LayoutInflater.from(context).inflate(R.layout.item_booklist,parent,false));


        return bookListHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final BookListHolder myHolder = (BookListHolder) holder;
        myHolder.tvBookname.setText(list.get(position).getBooklistName());

        MyUser currentUser = BmobUser.getCurrentUser(context,MyUser.class);
        BmobQuery<Relation_booklist> query = new BmobQuery<>();
        query.addWhereEqualTo("myUser",currentUser);

        BmobQuery<Relation_booklist> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("booklistName",list.get(position).getBooklistName());

        BmobQuery<Relation_booklist> query2 = new BmobQuery<>();
        query2.addWhereExists("book");

        List<BmobQuery<Relation_booklist>> queries = new ArrayList<>();
        queries.add(query);
        queries.add(query1);
        queries.add(query2);


        BmobQuery<Relation_booklist> andQurey = new BmobQuery<>();
        andQurey.and(queries);

        andQurey.findObjects(context, new FindListener<Relation_booklist>() {
            @Override
            public void onSuccess(List<Relation_booklist> list) {
                if (!list.isEmpty()){
                    myHolder.tvBookCount.setText(list.size()+"本");
                }

            }

            @Override
            public void onError(int i, String s) {
                System.out.println("--------booklistError:"+s);
            }
        });

//        if (list.get(position).getBookCount()!=null){
//            myHolder.tvBookCount.setText(list.get(position).getBookCount()+"本");
//        }
//        else {
//            myHolder.tvBookCount.setText("0本");
//        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Booklist getItem(int position){
        return list.get(position);
    }

    class BookListHolder extends RecyclerView.ViewHolder {
        private TextView tvBookname,tvBookCount;

        public BookListHolder(View itemView) {
            super(itemView);

            tvBookname = (TextView) itemView.findViewById(R.id.tvBookname);
            tvBookCount = (TextView) itemView.findViewById(R.id.tvBookCount);
        }
    }
}
