package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.secret.booklist60.Adapter.BookListAdapter;
import com.example.secret.booklist60.Adapter.LibSearchAdapter;
import com.example.secret.booklist60.DataBase.Relation_booklist;
import com.example.secret.booklist60.DataBase.Book;
import com.example.secret.booklist60.DataBase.Booklist;
import com.example.secret.booklist60.DataBase.Comment;
import com.example.secret.booklist60.DataBase.LibInfor;
import com.example.secret.booklist60.DataBase.Likes;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.DataBase.Shoucang;
import com.example.secret.booklist60.ExitApplication;
import com.example.secret.booklist60.R;
import com.example.secret.booklist60.RecyclerItemClickListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Administrator on 2016/9/15.
 * 书的详情页
 */
public class BookDetailActivity extends Activity implements View.OnClickListener {
    TextView dtBookName, dtIntroduction_shousuo, dtIntroduction_zhankai, dtAuthor, tvCount,
            tvComment, tvUserName, tvTitle, tvTime, tvCommentCount, tvLibBookId, tvDoubanScore,
            tvConfirm, tvCancel;
    ImageView dtImagedetail, ivCommentCount;
    ImageButton ivIntroduction_more, ivIntroduction_less, btnBack, ivLib, ivLib_more;
    CircleImageView ivTouxiang;
    RelativeLayout rlPinlun;
    ImageButton btnShoucang, btnLike, btnShare;
    Likes likes = new Likes();
    Shoucang shoucang = new Shoucang();
    Book book;
    String BookName, Author, Level, coverUrl, Introduction;
    Integer count = 0;
    int finished = 0x110;
    CardView layout_lib, layout_Introduction;
    List<LibInfor> list = new ArrayList<>();
    LibSearchAdapter adapter;
    RecyclerView rv, rvBooklist;
    EditText etBooklistName;


    BookListAdapter bookListAdapter;

    LinearLayout layout_addBooklist;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklist_detail);

        query();
        //从书单页或搜索页中获取某本书
        book = (Book) getIntent().getSerializableExtra("Book");
        BookName = book.getName();
        Author = book.getAuthor();
//        Level = book.getLevel();
        coverUrl = book.getCover();
        Introduction = book.getDesc();
        if (book.getCount() != null) {
            count = book.getCount();
        } else {
            count = 0;
        }

        tvDoubanScore = (TextView) findViewById(R.id.doubanscore);

        tvDoubanScore.setText(String.valueOf(book.getScore()));
        btnShare = (ImageButton) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(this);
        layout_lib = (CardView) findViewById(R.id.layout_lib);
        dtBookName = (TextView) findViewById(R.id.BookName_Detail);
        dtIntroduction_shousuo = (TextView) findViewById(R.id.Introduction_Detail_shousuo);
//        dtLevel = (TextView) findViewById(R.id.Level_Detail);
        dtAuthor = (TextView) findViewById(R.id.Author_Detail);
        dtImagedetail = (ImageView) findViewById(R.id.Cover_Detail);
        ivIntroduction_more = (ImageButton) findViewById(R.id.ivIntroduction_more);
        ivIntroduction_less = (ImageButton) findViewById(R.id.ivIntroduction_less);
        tvCount = (TextView) findViewById(R.id.tvCount_Detail);
        dtIntroduction_zhankai = (TextView) findViewById(R.id.Introduction_Detail_zhankai);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTime = (TextView) findViewById(R.id.tvTime_booklistdetail);
        tvCommentCount = (TextView) findViewById(R.id.tvCommentCount);
        ivCommentCount = (ImageView) findViewById(R.id.ivCommentCount);
        btnShoucang = (ImageButton) findViewById(R.id.btnShoucang);
        rlPinlun = (RelativeLayout) findViewById(R.id.rlPinlun);

        btnBack = (ImageButton) findViewById(R.id.btnBack);


        btnShoucang.setOnClickListener(BookDetailActivity.this);
        rlPinlun.setOnClickListener(BookDetailActivity.this);
        btnBack.setOnClickListener(this);

        dtBookName.setText(BookName);
//        dtLevel.setText(Level);
        dtAuthor.setText(Author);
        layout_Introduction = (CardView) findViewById(R.id.layout_introduction);
        if (Introduction != null) {
            dtIntroduction_shousuo.setText("        " + Introduction);
            dtIntroduction_zhankai.setText("        " + Introduction);
        } else {
            layout_Introduction.setVisibility(View.GONE);
        }

        tvCount.setText(count.toString());
        tvTitle.setText(BookName);
        rv = (RecyclerView) findViewById(R.id.rv_lib);
//        tvLib = (TextView) findViewById(R.id.tvLib);
//        tvLib_more = (TextView) findViewById(R.id.tvLib_more);
        adapter = new LibSearchAdapter(this);
        tvLibBookId = (TextView) findViewById(R.id.tvBookId);
        System.out.println("booklistdetail------" + book.getLibID());
        if (book.getLibID() != null) {//如果该书有图书馆的id

//            layout_lib.setVisibility(View.VISIBLE);
            //连接网络，获取借阅信息以及书号
            //getVolley();
            getLibInfor();

        }
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setNestedScrollingEnabled(false);

        //加载封面
        Glide.with(this).load(coverUrl).into(dtImagedetail);
        dtImagedetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookDetailActivity.this, ShowBigPic.class);
                intent.putExtra("url", book.getCover());
                startActivity(intent);

            }
        });
        if (Introduction != null) {
            if (Introduction.length() > 75) {
                ivIntroduction_more.setVisibility(View.VISIBLE);
            } else {
                ivIntroduction_more.setVisibility(View.GONE);
            }
        } else {
            ivIntroduction_more.setVisibility(View.GONE);
        }

        //当简介过多，会显示一个按钮，点了之后会展开全部
        ivIntroduction_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dtIntroduction_shousuo.setVisibility(View.GONE);
                dtIntroduction_zhankai.setVisibility(View.VISIBLE);
                ivIntroduction_more.setVisibility(View.GONE);
                ivIntroduction_less.setVisibility(View.VISIBLE);
            }
        });
        //点了之后收缩
        ivIntroduction_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dtIntroduction_shousuo.setVisibility(View.VISIBLE);
                dtIntroduction_zhankai.setVisibility(View.GONE);
                ivIntroduction_more.setVisibility(View.VISIBLE);
                ivIntroduction_less.setVisibility(View.GONE);
            }
        });


        //当搜索完这本书是否有点赞后，给“点赞”的按钮设置图片
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == finished) {

                    btnLike = (ImageButton) findViewById(R.id.btnLike);
                    btnLike.setOnClickListener(BookDetailActivity.this);
                }
            }
        };
        final MyUser currentUser = BmobUser.getCurrentUser(BookDetailActivity.this, MyUser.class);

        //搜索该书是否有点赞
        new Thread() {
            @Override
            public void run() {

                BmobQuery<Likes> query = new BmobQuery<>();
                query.addWhereEqualTo("myUser", new BmobPointer(currentUser));

                BmobQuery<Likes> query1 = new BmobQuery<>();
                query1.addWhereEqualTo("book", new BmobPointer(book));

                List<BmobQuery<Likes>> andQuerys = new ArrayList<BmobQuery<Likes>>();
                andQuerys.add(query);
                andQuerys.add(query1);

                BmobQuery<Likes> querySum = new BmobQuery<>();
                querySum.and(andQuerys);
                querySum.include("book.count");
                querySum.findObjects(BookDetailActivity.this, new FindListener<Likes>() {
                    @Override
                    public void onSuccess(List<Likes> list) {
                        if (list.isEmpty()) {
                            System.out.println("不存在");
                            likes.setLike(true);
                            btnLike.setBackgroundResource(R.mipmap.dislike);

                        } else {
                            System.out.println("数量：" + list.size());
                            likes.setLike(false);
                            btnLike.setBackgroundResource(R.mipmap.like);
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });

                BmobQuery<Shoucang> queryShoucang = new BmobQuery<>();

                queryShoucang.addWhereEqualTo("myUser", new BmobPointer(currentUser));

                BmobQuery<Shoucang> query1Shoucang = new BmobQuery<>();
                query1Shoucang.addWhereEqualTo("book", new BmobPointer(book));

                List<BmobQuery<Shoucang>> andQuerysShoucang = new ArrayList<BmobQuery<Shoucang>>();
                andQuerysShoucang.add(queryShoucang);
                andQuerysShoucang.add(query1Shoucang);

                BmobQuery<Shoucang> querySumShoucang = new BmobQuery<>();
                querySumShoucang.and(andQuerysShoucang);
                querySumShoucang.include("book.count");
                querySumShoucang.findObjects(BookDetailActivity.this, new FindListener<Shoucang>() {
                    @Override
                    public void onSuccess(List<Shoucang> list) {
                        if (list.size() == 0) {

                            btnShoucang.setBackgroundResource(R.mipmap.notshoucang);

                        } else {

                            btnShoucang.setBackgroundResource(R.mipmap.shoucang);
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
                handler.sendEmptyMessage(finished);
            }

        }.start();

        tvComment = (TextView) findViewById(R.id.tvcomment);
        tvUserName = (TextView) findViewById(R.id.tvUsername_Detail);
        ivTouxiang = (CircleImageView) findViewById(R.id.touxiang_Detail);
        showComment();
        ExitApplication.getInstance().addActivities(this);

        bookListAdapter = new BookListAdapter(this);


        Log.d("test", "onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        showComment();
        System.out.println("test:" + "onResume");
        Log.d("test", "onResume");
    }

    private void showComment() {
    /*
    以下为搜索评论，将最新一条评论显示在详情页
     */
        System.out.println("booklistdetail------" + "showing");
        BmobQuery<Comment> query1Comment = new BmobQuery<>();
        query1Comment.addWhereEqualTo("book", new BmobPointer(book));
        query1Comment.include("myUser,book");
        query1Comment.findObjects(BookDetailActivity.this, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                if (!list.isEmpty()) {
                    tvCommentCount.setText("(" + String.valueOf(list.size()) + ")");
                    tvUserName.setText(list.get(list.size() - 1).getMyUser().getUsername());
                    tvComment.setText(list.get(list.size() - 1).getCommentcontent());
                    tvTime.setText(list.get(list.size() - 1).getCreatedAt());
                    if (list.get(list.size() - 1).getMyUser().getHead() != null) {
                        Glide.with(BookDetailActivity.this).load(list.get(list.size() - 1).
                                getMyUser().getHead().getUrl()).into(ivTouxiang);
                    } else if (list.get(list.size() - 1).
                            getMyUser().getUrlHead() != null) {
                        Glide.with(BookDetailActivity.this).load(list.get(list.size() - 1).
                                getMyUser().getUrlHead()).into(ivTouxiang);
                    } else {
                        ivTouxiang.setImageResource(R.mipmap.head);
                    }
                    tvCommentCount.setVisibility(View.VISIBLE);
                    tvUserName.setVisibility(View.VISIBLE);
                    tvComment.setVisibility(View.VISIBLE);
                    tvTime.setVisibility(View.VISIBLE);
                    ivTouxiang.setVisibility(View.VISIBLE);
                    ivCommentCount.setVisibility(View.VISIBLE);
                } else {
                    System.out.println("booklistdetail------" + "no comment");
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


    //因为是数据库里的数据所以需要写上query方法
    public void query() {
        BmobQuery<Book> query = new BmobQuery<>();
        query.order("-createdAt");//依照数据排序时间排序
        query.findObjects(BookDetailActivity.this, new FindListener<Book>() {
            @Override
            public void onSuccess(List<Book> list) {

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        final MyUser currentUser = BmobUser.getCurrentUser(BookDetailActivity.this, MyUser.class);
        switch (view.getId()) {
            //按了“点赞”按钮后，推荐数量增加，图标变亮
            case R.id.btnLike:
                if (currentUser == null) {
                    new AlertDialog.Builder(BookDetailActivity.this).setMessage("登录后才可点赞该书哦")
                            .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(BookDetailActivity.this, LoginActivity.class));
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                    return;
                }
                BmobQuery<Likes> query = new BmobQuery<>();

                query.addWhereEqualTo("myUser", new BmobPointer(currentUser));

                BmobQuery<Likes> query1 = new BmobQuery<>();
                query1.addWhereEqualTo("book", new BmobPointer(book));

                List<BmobQuery<Likes>> andQuerys = new ArrayList<BmobQuery<Likes>>();
                andQuerys.add(query);
                andQuerys.add(query1);

                BmobQuery<Likes> querySum = new BmobQuery<>();
                querySum.and(andQuerys);
                querySum.include("book.count");
                querySum.findObjects(BookDetailActivity.this, new FindListener<Likes>() {
                    @Override
                    public void onSuccess(List<Likes> list) {
                        if (list.isEmpty()) {
                            System.out.println("不存在");

                            if (book.getCount() == null) {
                                count = 0;
                            } else {
                                count = book.getCount();
                            }
                            count++;
                            book.setCount(count);
                            book.update(BookDetailActivity.this, new UpdateListener() {
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
                            likes.setBook(book);
                            likes.save(BookDetailActivity.this, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    Log.i("Add:", "succeed!");
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Log.d("AddFailed:", s);
                                }
                            });
                            btnLike.setBackgroundResource(R.mipmap.like);
                            tvCount.setText(count.toString());
                        } else {
                            System.out.println("数量：" + list.size());
                            count = book.getCount();
                            count--;
                            book.setCount(count);
                            book.update(BookDetailActivity.this, new UpdateListener() {
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
                            likes.delete(BookDetailActivity.this, LikeId, new DeleteListener() {
                                @Override
                                public void onSuccess() {
                                    Log.i("Delete:", "succeed!");
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Log.d("DeleteFailed:", s);
                                }
                            });
                            btnLike.setBackgroundResource(R.mipmap.dislike);
                            tvCount.setText(count.toString());
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
                break;
            //点了“收藏”按钮，会将此书收藏
            case R.id.btnShoucang:

                //收藏到书单中
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                View dialog_booklist = LayoutInflater.from(this).inflate(R.layout.dialog_book, null);
                final android.support.v7.app.AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getWindow().setContentView(dialog_booklist);


                final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                System.out.println("-------------dialog:"+params.height);
//                params.height = 800;
//                dialog.getWindow().setAttributes(params);

                BmobQuery<Booklist> query2 = new BmobQuery<>();
                query2.addWhereEqualTo("myUser",new BmobPointer(currentUser));
                query2.count(this, Booklist.class, new CountListener() {
                    @Override
                    public void onSuccess(int i) {
                        if (i<=5){
                            params.height = LinearLayoutCompat.LayoutParams.WRAP_CONTENT;
                            dialog.getWindow().setAttributes(params);
                        }
                        else {
                            params.height = 800;
                            dialog.getWindow().setAttributes(params);
                        }
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });


                rvBooklist = (RecyclerView) dialog.getWindow().findViewById(R.id.rvBooklist);
                rvBooklist.setAdapter(bookListAdapter);
                rvBooklist.setLayoutManager(new LinearLayoutManager(this));

                rvBooklist.addOnItemTouchListener(new RecyclerItemClickListener(this, rvBooklist, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        MyUser currentUser = BmobUser.getCurrentUser(BookDetailActivity.this, MyUser.class);
                        Booklist booklist = bookListAdapter.getItem(position);

                        Relation_booklist relationbooklist = new Relation_booklist();
                        relationbooklist.setMyUser(currentUser);
                        relationbooklist.setBooklist(booklist);
                        relationbooklist.setBook(book);
                        relationbooklist.save(BookDetailActivity.this, new SaveListener() {
                            @Override
                            public void onSuccess() {
                                toast("添加成功");
                                dialog.dismiss();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                toast("添加失败,"+s);
                            }
                        });

//                        BB bb = new BB();
//                        bb.setMyUser(currentUser);
//                        bb.setBooklist(booklist);
////                        bb.setBook(book);
////                        bb.setS("hhhhh");
//                        System.out.println("--------------currentUser:"+currentUser.getObjectId());
//                        System.out.println("--------------booklist:"+booklist.getObjectId());
//                        System.out.println("--------------book:"+book.getObjectId());
//                        bb.save(BookDetailActivity.this, new SaveListener() {
//                            @Override
//                            public void onSuccess() {
//                                toast("添加成功");
//                                dialog.dismiss();
//                            }
//
//                            @Override
//                            public void onFailure(int i, String s) {
//                                toast("添加失败,"+s);
//                            }
//                        });
//
                    }
//
                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
                queryBooklist();

//                builder.setView(dialog_booklist);

//                builder.show();
//
                layout_addBooklist = (LinearLayout) dialog_booklist.findViewById(R.id.layout_addBooklist);
                layout_addBooklist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(BookDetailActivity.this);
                        View dialog_newbooklist = LayoutInflater.from(BookDetailActivity.this).inflate(R.layout.dialog_newbooklist, null);
                        etBooklistName = (EditText) dialog_newbooklist.findViewById(R.id.etBooklistName);

                        builder1.setView(dialog_newbooklist);
                        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyUser currentUser = BmobUser.getCurrentUser(BookDetailActivity.this, MyUser.class);
//                                Booklist booklist = new Booklist();
//                                booklist.setMyUser(currentUser);
//                                booklist.setBook(book);
//                                if (booklist.getBookCount()==null){
//                                    booklist.setBookCount(1);
//                                }
//                                else {
//                                    Integer count = booklist.getBookCount();
//                                    booklist.setBookCount(count++);
//                                }
                                Booklist booklist = new Booklist();
                                booklist.setBooklistName(etBooklistName.getText().toString());
                                booklist.setMyUser(currentUser);
                                booklist.save(BookDetailActivity.this, new SaveListener() {
                                    @Override
                                    public void onSuccess() {
                                        toast("创建成功");
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        toast("创建失败," + s);
                                    }
                                });
                            }
                        });
                        builder1.show();
                    }
                });

                break;
            //点击“评论”那一栏，将在另一个页面显示全部评论
            case R.id.rlPinlun:

                Intent intent = new Intent(BookDetailActivity.this, PingLunActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Book", book);
                intent.putExtras(bundle);

                System.out.println(book.getName());
                startActivity(intent);
                break;
            //返回
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnShare:
                if (currentUser == null) {
                    new AlertDialog.Builder(BookDetailActivity.this).setMessage("登录后才可分享该书哦")
                            .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(BookDetailActivity.this, LoginActivity.class));
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                    return;
                }
                showShare();
                break;
        }
    }
    private void queryBooklist() {
        MyUser currentUser = BmobUser.getCurrentUser(this, MyUser.class);
        BmobQuery<Booklist> query = new BmobQuery<>();
        query.addWhereEqualTo("myUser", new BmobPointer(currentUser));
        query.findObjects(this, new FindListener<Booklist>() {
            @Override
            public void onSuccess(List<Booklist> list) {
                if (list != null) {
                    bookListAdapter.bindData(list);
                    bookListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void showShare() {
        ShareSDK.initSDK(BookDetailActivity.this);
        OnekeyShare oks = new OnekeyShare();
// 关闭sso授权
        oks.disableSSOWhenAuthorize();
// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
// oks.setNotification(R.drawable.ic_launcher,
// getString(R.string.app_name));
// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("分享标题");
// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://blog.csdn.net/donkor_");

//// text是分享文本，所有平台都需要这个字段
        oks.setText("分享文本内容");
//// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//// oks.setImagePath("/sdcard/test.jpg");//
//// 确保SDcard下面存在此张图片
//        oks.setImageUrl("http://img.blog.csdn.net/20161115193036196");
//// url仅在微信（包括好友和朋友圈）中使用

        oks.setUrl("http://blog.csdn.net/donkor_");
//// comment是我对这条分享的评论，仅在人人网和QQ空间使用
////oks.setComment("");
//// site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("一本书");
//// siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.baidu.com");


        oks.setSilent(false);

//// 启动分享GUI
        oks.show(BookDetailActivity.this);

    }

    //获取馆藏记录
    String id;

    public void getLibInfor() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1000001) {
                    sort(list);
                    System.out.println("booklistdetail:" + list.size());
                    adapter.bindData(list);
                    adapter.notifyDataSetChanged();
                    tvLibBookId.setText(id);
                    layout_lib.setVisibility(View.VISIBLE);
                }

            }
        };
        new Thread() {
            @Override
            public void run() {
                Document doc;
                String url = "http://opac.lib.szu.edu.cn/opac/bookinfo.aspx?ctrlno=" + book.getLibID();
                try {

                    org.jsoup.Connection conn = Jsoup
                            .connect(url)
                            .data("query", "java")
                            .userAgent(
                                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
                    doc = conn.get();

                    Element divFound = doc.getElementById("searchnotfound");
                    if (divFound != null) {
                        return;
                    }

                    Elements div = doc.select("tbody").select("tr");
                    for (Element e : div) {
                        LibInfor libInfor = new LibInfor();
                        libInfor.setId(e.child(1).text());
                        id = e.child(1).text();
                        libInfor.setLocation(e.child(0).text());
                        libInfor.setStatus(e.child(5).text());
                        list.add(libInfor);
//                        System.out.println("booklistdetail:"+list.size());

                    }
                    handler.sendEmptyMessage(1000001);
                } catch (IOException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();

                }
            }
        }.start();


    }

    //讲得到的馆藏记录进行排序
    public void sort(List<LibInfor> list) {
        for (int i = 0; i < list.size() - 1; i++) {

            //先根据借阅信息的长度
            if (list.get(i).getStatus().length() >
                    list.get(i + 1).getStatus().length()) {
                Collections.swap(list, i, i + 1);
            }
            //在借阅信息长度相同情况下，根据图书馆馆名的长度排序
            if (list.get(i).getStatus().length() ==
                    list.get(i + 1).getStatus().length()) {
                if (list.get(i).getLocation().length() >
                        list.get(i + 1).getLocation().length()) {
                    Collections.swap(list, i, i + 1);
                }
            }

        }
    }

    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
