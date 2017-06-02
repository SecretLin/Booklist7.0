package com.example.secret.booklist60.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.secret.booklist60.DataBase.Book;
import com.example.secret.booklist60.R;

/**
 * Created by Secret on 2016/9/15.
 */
public class BookHolder extends RecyclerView.ViewHolder {
    private TextView tvBookname,tvAuthor,tvLevel,tvCount;
    public ImageView ivCover;

    public BookHolder(View itemView) {
        super(itemView);
        tvBookname = (TextView) itemView.findViewById(R.id.tvBookname);
        tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
        tvCount = (TextView) itemView.findViewById(R.id.tvCount);
        ivCover = (ImageView) itemView.findViewById(R.id.ivCover);

    }
    public void bindData(Book book){
        tvBookname.setText(book.getName());
        tvAuthor.setText(book.getAuthor());
        if (book.getCount()!=null){
            tvCount.setText(book.getCount().toString());
        }
        else {
            tvCount.setText("0");
        }


    }
}
