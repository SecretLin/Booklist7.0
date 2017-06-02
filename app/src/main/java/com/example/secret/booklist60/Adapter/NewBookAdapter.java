package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.Book;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.utils.MyShadowProperty;
import com.wangjie.shadowviewhelper.ShadowViewDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Secret on 2017/5/27.
 */

public class NewBookAdapter extends RecyclerView.Adapter {

    private List<Book> list = new ArrayList<>();
    private Context context;

    public void bindData(List<Book> list){
        this.list.clear();
        if (!list.isEmpty()){
            this.list = list;
        }
    }
    public NewBookAdapter(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewBookHolder holder = new NewBookHolder(LayoutInflater.from(context).inflate(R.layout.item_newbook,parent,false));

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewBookHolder myHolder = (NewBookHolder) holder;
        Glide.with(context).load(list.get(position).getCover()).into(myHolder.ivCover);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NewBookHolder extends RecyclerView.ViewHolder {
        private ImageView ivCover;

        public NewBookHolder(View itemView) {
            super(itemView);

            ivCover = (ImageView) itemView.findViewById(R.id.ivCover);


            MyShadowProperty sp = new MyShadowProperty()
                    .setShadowColor(0x77000000)
                    .setShadowDy(dip2px(context, 0.5f))
                    .setShadowDx(dip2px(context,0.5f))
                    .setShadowRadius(dip2px(context, 3))
                    .setShadowSide(MyShadowProperty.ALL)
                    ;
            ShadowViewDrawable sd = new ShadowViewDrawable(sp, Color.TRANSPARENT, 0, 0);
            ViewCompat.setBackground(ivCover, sd);
            ViewCompat.setLayerType(ivCover, ViewCompat.LAYER_TYPE_SOFTWARE, null);

        }
    }
    public static int dip2px(Context context, float dpValue) {
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        } catch (Throwable throwable) {
            // igonre
        }
        return 0;
    }
}
