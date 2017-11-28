package com.example.secret.booklist60.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.All_Comment;
import com.example.secret.booklist60.DataBase.Ground;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.UI.BookDetailActivity;
import com.example.secret.booklist60.utils.MyShadowProperty;
import com.example.secret.booklist60.utils.MyShadowViewDrawable;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.secret.booklist60.Adapter.NewBookAdapter.dip2px;

/**
 * Created by Secret on 2016/12/13.
 */

public class GroundAdapter extends RecyclerView.Adapter {
    private List<Ground> list = new ArrayList<>();
    private Context context;
    public GroundAdapter(Context context){
        this.context = context;
    }
    public void bindData(List<Ground> list){
        this.list.clear();
        if (list!=null)
        this.list = list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GroundHolder holder = new GroundHolder(LayoutInflater.from(context).inflate(R.layout.item_ground,parent,false));

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final GroundHolder groundHolder = (GroundHolder) holder;
        if (list.get(position).getUser()!=null){
            if (list.get(position).getUser().getHead()!=null){
                Glide.with(context).load(list.get(position).getUser().getHead().getUrl()).into(groundHolder.ivHead);
            }
            else if (list.get(position).getUser().getUrlHead()!=null){
                Glide.with(context).load(list.get(position).getUser().getUrlHead()).into(groundHolder.ivHead);
            }
            else{
                groundHolder.ivHead.setImageResource(R.mipmap.head);
            }
            groundHolder.tvUsername.setText(list.get(position).getUser().getUsername());
            groundHolder.tvContent.setText(list.get(position).getContent());
            groundHolder.tvTime.setText(list.get(position).getCreatedAt());
            BmobQuery<All_Comment> query = new BmobQuery<>();
            query.addWhereEqualTo("ground",new BmobPointer(list.get(position)));
            query.findObjects(context, new FindListener<All_Comment>() {
                @Override
                public void onSuccess(List<All_Comment> list) {
                    if (list.isEmpty()){
                        groundHolder.tvCommentCount.setText("(0)");
                    }else {
                        groundHolder.tvCommentCount.setText("("+list.size()+")");
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });

            if (list.get(position).getBook()!=null){
                groundHolder.layout_book.setVisibility(View.VISIBLE);
                Glide.with(context).load(list.get(position).getBook().getCover()).into(groundHolder.ivCover);
                groundHolder.tvBookname.setText(list.get(position).getBook().getName());
                groundHolder.tvAuthor.setText(list.get(position).getBook().getAuthor());
            }
            else {
                groundHolder.layout_book.setVisibility(View.GONE);
            }

            groundHolder.layout_book.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP){
                        groundHolder.layout_book.getParent().requestDisallowInterceptTouchEvent(true);
                        final Intent intent = new Intent(context,BookDetailActivity.class);
                        final Bundle bundle = new Bundle();
                        bundle.putSerializable("Book",list.get(position).getBook());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }

                    return false;
                }
            });
        }

    }



    @Override
    public int getItemCount() {
        System.out.println("-------size:"+list.size());
        return list.size();

    }

    public Ground getItem(int position){ return list.get(position);}

    class GroundHolder extends RecyclerView.ViewHolder {
        private CircleImageView ivHead;
        private RelativeLayout layout_book;
        private TextView tvUsername,tvBookname,tvAuthor,tvTime,tvCommentCount,tvContent;
        private ImageView ivCover;
        private RelativeLayout rlGround;

        public GroundHolder(View itemView) {
            super(itemView);

            ivHead = (CircleImageView) itemView.findViewById(R.id.ivHead);
            ivCover = (ImageView) itemView.findViewById(R.id.ivCover);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvBookname = (TextView) itemView.findViewById(R.id.tvBookname);
            layout_book = (RelativeLayout) itemView.findViewById(R.id.layout_book);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvCommentCount = (TextView) itemView.findViewById(R.id.tvCommentCount);

            rlGround = (RelativeLayout) itemView.findViewById(R.id.rlGround);

            MyShadowProperty sp = new MyShadowProperty()
                    .setShadowColor(Color.argb(255,238,238,238))
                    .setShadowDy(dip2px(context, 0.5f))
                    .setShadowRadius(dip2px(context, 2))
                    .setShadowSide(MyShadowProperty.BOTTOM | MyShadowProperty.TOP);
            MyShadowViewDrawable sd = new MyShadowViewDrawable(sp, Color.WHITE, 0, 0);
            ViewCompat.setBackground(rlGround, sd);
            ViewCompat.setLayerType(rlGround, ViewCompat.LAYER_TYPE_SOFTWARE, null);

        }
    }
}
