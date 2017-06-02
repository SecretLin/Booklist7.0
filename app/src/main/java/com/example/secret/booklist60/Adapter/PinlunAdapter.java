package com.example.secret.booklist60.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.DataBase.All_Comment;
import com.example.secret.booklist60.DataBase.Comment;
import com.example.secret.booklist60.DataBase.Likes;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.UI.MoreCommentActivity;
import com.example.secret.booklist60.UI.PingLunActivity;
import com.example.secret.booklist60.UI.UserDetailActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Secret on 2016/10/27.
 * 评论界面的adapter
 */

public class PinlunAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Comment> comments = new ArrayList<>();
    PinlunChildAdapter childAdapter; //= new PinlunChildAdapter(context);
    public List<PinlunChildAdapter> adapterList = new ArrayList<>();
    public int childPosition;

    public PinlunAdapter(Context context) {
        this.context = context;
    }

    public void bindData(List<Comment> list) {
        this.comments.clear();
        if (list != null) {
            this.comments.addAll(list);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PinlunHolder holder = new PinlunHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pinlun, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PinlunHolder pinlunHolder = (PinlunHolder) holder;

        pinlunHolder.bindData(comments.get(position));

        childAdapter = new PinlunChildAdapter(context);
        adapterList.add(childAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        //通过绑定父评论来查找子评论
        query(position, pinlunHolder);

        pinlunHolder.ChildRecycler.setAdapter(childAdapter);
        pinlunHolder.ChildRecycler.setLayoutManager(linearLayoutManager);
        pinlunHolder.ChildRecycler.setNestedScrollingEnabled(false);
        if (comments.get(position).getMyUser().getHead() != null) {
            Glide.with(context).load(comments.get(position).getMyUser().getHead().getUrl()).into(pinlunHolder.ivTouxiang);
        }
        else if (comments.get(position).getMyUser().getUrlHead()!=null){
            Glide.with(context).load(comments.get(position).getMyUser().getUrlHead()).into(pinlunHolder.ivTouxiang);
        }
        else {
            pinlunHolder.ivTouxiang.setImageResource(R.mipmap.head);
        }
        //点击头像，跳转到用户资料界面
        pinlunHolder.ivTouxiang.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pinlunHolder.ivTouxiang.getParent().requestDisallowInterceptTouchEvent(true);
                Intent intent = new Intent(context, UserDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", comments.get(position).getMyUser());
                intent.putExtras(bundle);
                context.startActivity(intent);
                return true;
            }
        });

        //点击回复他人评论
        final PingLunActivity activity = (PingLunActivity) context;
        pinlunHolder.ChildRecycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pinlunHolder.ChildRecycler.getParent().requestDisallowInterceptTouchEvent(true);
                RecyclerView rv = (RecyclerView) view;
                View childView = rv.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                childPosition = rv.getChildPosition(childView);
                Log.i("childPosition,adapter", String.valueOf(position));
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    activity.who = 3;
                    activity.itemPosition = position;
                    activity.etComment.requestFocus();
                    activity.etComment.setFocusable(true);
                    InputMethodManager imm = (InputMethodManager) activity.etComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    activity.etComment.setHint("回复" + adapterList.get(position).getItem(childPosition).getUser1().getUsername());
                }

                return false;
            }
        });

        //点赞他人的评论
        pinlunHolder.btnLike.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pinlunHolder.btnLike.getParent().requestDisallowInterceptTouchEvent(true);
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    final Comment comment = comments.get(position);
                    final MyUser currentUser = BmobUser.getCurrentUser(context, MyUser.class);
                    BmobQuery<Likes> query = new BmobQuery<>();
                    query.addWhereEqualTo("myUser", new BmobPointer(currentUser));

                    BmobQuery<Likes> query1 = new BmobQuery<>();
                    query1.addWhereEqualTo("comment", new BmobPointer(comment));

                    List<BmobQuery<Likes>> andQuerys = new ArrayList<BmobQuery<Likes>>();
                    andQuerys.add(query);
                    andQuerys.add(query1);

                    BmobQuery<Likes> querySum = new BmobQuery<>();
                    querySum.and(andQuerys);
                    querySum.include("comment.count");
                    querySum.findObjects(context, new FindListener<Likes>() {
                        @Override
                        public void onSuccess(List<Likes> list) {
                            Likes likes = new Likes();
                            Integer count;
                            if (list.isEmpty() || comment.getCount() == 0) {
                                System.out.println("不存在");
                                if (comment.getCount() == null) {
                                    count = 0;
                                } else {
                                    count = comment.getCount();
                                }
                                count++;
                                comment.setCount(count);
                                comment.update(context, new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.i("Update:", "succeed!");
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Log.d("UpdateFailed:", s);
                                    }
                                });

                                likes.setMyUser(currentUser);
                                likes.setComment(comment);
                                likes.save(context, new SaveListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.i("Add:", "succeed!");
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Log.d("AddFailed:", s);
                                    }
                                });
                                pinlunHolder.btnLike.setBackgroundResource(R.mipmap.like);
                                pinlunHolder.tvCount.setText(count.toString());
                            } else if (!list.isEmpty()) {
                                System.out.println("数量：" + list.size());
                                count = comment.getCount();
                                count--;
                                comment.setCount(count);
                                comment.update(context, new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.i("Delete:", "succeed!");
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Log.d("DeleteFailed:", s);
                                    }
                                });
                                String LikeId = list.get(0).getObjectId();
                                likes.delete(context, LikeId, new DeleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.i("Delete:", "succeed!");
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Log.d("DeleteFailed:", s);
                                    }
                                });
                                pinlunHolder.btnLike.setBackgroundResource(R.mipmap.dislike);
                                pinlunHolder.tvCount.setText(count.toString());
                            }
                        }

                        @Override
                        public void onError(int i, String s) {

                        }
                    });
                }

//
                return false;
            }
        });





    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public Comment getItem(int position) {
        return comments.get(position);
    }
    int count;
    public void query(final int position, final PinlunHolder pinlunHolder) {
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "正在加载....");
        Comment comment = comments.get(position);
        BmobQuery<All_Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("comment", new BmobPointer(comment));
        query.include("User1,User2,comment");
        query.order("createdAt");
        query.findObjects(context, new FindListener<All_Comment>() {
            @Override
            public void onSuccess(final List<All_Comment> list) {
                if (list != null) {
                    adapterList.get(position).bindData(list);
                    if (list.size() > 3) {
                        count = list.size();
                        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) pinlunHolder.ChildRecycler.getLayoutParams();
                        linearParams.height = 310;
                        pinlunHolder.ChildRecycler.setLayoutParams(linearParams);
                        pinlunHolder.tvMore.setVisibility(View.VISIBLE);
                        pinlunHolder.tvMore.setText("共"+count+"条评论");
                        pinlunHolder.tvMore.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                pinlunHolder.tvMore.getParent().requestDisallowInterceptTouchEvent(true);

                                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                    Intent intent = new Intent(context, MoreCommentActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("comment",comments.get(position));
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }
                                return true;
                            }
                        });
                    }


                }
                progressDialog.dismiss();

            }

            @Override
            public void onError(int i, String s) {
                // Log.d("testss","data=onerror"+s);
                progressDialog.dismiss();
                Toast.makeText(context,"获取评论失败，"+s,Toast.LENGTH_SHORT).show();

            }
        });
    }

    public class PinlunHolder extends RecyclerView.ViewHolder {
        protected TextView tvUsername, tvComment, tvTime, tvCount, tvMore;
        protected CircleImageView ivTouxiang;
        protected ImageButton btnLike;
        public RecyclerView ChildRecycler;
        private List<Comment> list = new ArrayList<>();
        private RelativeLayout layout;

        public PinlunHolder(View itemView) {
            super(itemView);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername_Pinlun);
            tvComment = (TextView) itemView.findViewById(R.id.tvComment);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);

            ivTouxiang = (CircleImageView) itemView.findViewById(R.id.ivTouxiang_Pinlun);
            ChildRecycler = (RecyclerView) itemView.findViewById(R.id.rv_child);

            btnLike = (ImageButton) itemView.findViewById(R.id.ib_like);
            tvCount = (TextView) itemView.findViewById(R.id.tvlike_count);

            tvMore = (TextView) itemView.findViewById(R.id.tv_more);

            layout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
        }

        public void bindData(Comment comment) {
            tvUsername.setText(comment.getMyUser().getUsername());
            tvComment.setText(comment.getCommentcontent());
            tvTime.setText(comment.getCreatedAt());
        }
    }
}
