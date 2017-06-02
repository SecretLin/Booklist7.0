package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secret.booklist60.UI.MainActivity;
import com.example.secret.booklist60.utils.Flowlayout;
import com.example.secret.booklist60.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Secret on 2017/2/27.
 */

public class SelectKindsAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<String[]> kinds = new ArrayList<>();
    private List<String[]> more_kinds = new ArrayList<>();
    private List<String> kindsTitle;
    private boolean isOpen = false;
    public SelectKindsAdapter(Context context,List<String[]> kinds,List<String[]> more_kinds,List<String> kindsTitle){
        this.context = context;
        this.kinds = kinds;
        this.more_kinds = more_kinds;
        this.kindsTitle = kindsTitle;
        System.out.println("种类个数"+kinds.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SelectKindsHolder holder = new SelectKindsHolder(LayoutInflater.from(context).inflate(
                R.layout.item_selectkinds,parent,false));
        System.out.println("种类个数"+kinds.size());
        return holder;
    }
    String s,s1;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SelectKindsHolder myHolder = (SelectKindsHolder) holder;
        myHolder.tvKinds.setText(kindsTitle.get(position));
        //System.out.println("种类个数"+kinds.size());
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 30;
        lp.bottomMargin = 20;
        for (int i = 0; i<kinds.get(position).length; i ++) {
            final TextView tv = new TextView(context);

            if (i == kinds.get(position).length - 1) {
                s = kinds.get(position)[i];
                if (isOpen){
                    s1 = s + "▲";
                }
                else {
                    s1 = s + "▼";
                }
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isOpen){
                            myHolder.fl_more.setVisibility(View.VISIBLE);
                            isOpen = true;
                            s1 = s + "▲";
                            tv.setText(s1);
                            System.out.println("是否打开"+isOpen);
                        }else {
                            myHolder.fl_more.setVisibility(View.GONE);
                            isOpen = false;
                            s1 = s + "▼";
                            tv.setText(s1);
                            System.out.println("是否打开"+isOpen);
                        }

                    }
                });
            }
            else {
                final String s2 = kinds.get(position)[i];
                s1 = kinds.get(position)[i];
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(context, s2, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("page",1);
                        intent.putExtra("Kinds",s2);
                        context.startActivity(intent);
                    }
                });

            }
//
            tv.setText(s1);
            tv.setTextColor(Color.BLACK);
            //tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_bg));
            tv.setBackgroundResource(R.drawable.tv_bg);
            myHolder.fl.addView(tv, lp);
        }

        for (int i = 0; i<more_kinds.get(position).length; i ++){
            TextView tv = new TextView(context);
            final String s = more_kinds.get(position)[i];
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
                }
            });
            tv.setText(more_kinds.get(position)[i]);
            tv.setTextColor(Color.BLACK);
           // tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_bg));
            tv.setBackgroundResource(R.drawable.tv_bg);
            myHolder.fl_more.addView(tv, lp);
        }

    }

    @Override
    public int getItemCount() {
        //System.out.println("getItemCount种类个数"+kinds.size());
        return kinds.size();
    }

    public class SelectKindsHolder extends RecyclerView.ViewHolder {
        private TextView tvKinds;
        private Flowlayout fl,fl_more;
        public SelectKindsHolder(View itemView) {
            super(itemView);

            tvKinds = (TextView) itemView.findViewById(R.id.tvKinds);
            fl = (Flowlayout) itemView.findViewById(R.id.com_flowlayout);
            fl_more = (Flowlayout) itemView.findViewById(R.id.com_flowlayout_more);
        }
    }
}
