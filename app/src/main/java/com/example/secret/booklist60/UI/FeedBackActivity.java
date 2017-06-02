package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secret.booklist60.Adapter.FeedBackAdapter;
import com.example.secret.booklist60.DataBase.FeedBack;
import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.ExitApplication;
import com.example.secret.booklist60.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
/*
   反馈界面，由于时间不足暂时未能实现
 */

public class FeedBackActivity extends Activity {
    private String type = null;
    private RecyclerView rv;
    private FeedBackAdapter adapter;
    private EditText etFeedback;
    private ImageButton btnSendFeedback,btnBack;
    private LinearLayoutManager layoutManager;
    private TextView tvActionbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        tvActionbar = (TextView) findViewById(R.id.tvTitle);
        tvActionbar.setText("反映");

        rv = (RecyclerView) findViewById(R.id.rv);
        adapter = new FeedBackAdapter(this);
        final MyUser currentUser = BmobUser.getCurrentUser(this, MyUser.class);
        type = getIntent().getStringExtra("type");
        if (type.equals("search")) {
            FeedBack feedBack = new FeedBack();
            feedBack.setUser(currentUser);
            feedBack.setContent("请输入你想找的书的书名和作者");
            feedBack.setType(1);
            feedBack.save(FeedBackActivity.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    System.out.println("系统发出消息：" + "成功");
                    query();
                }

                @Override
                public void onFailure(int i, String s) {
                    System.out.println("系统发出消息：" + s);
                }
            });
        } else if (type.equals("normal")) {
            FeedBack feedBack = new FeedBack();
            feedBack.setUser(currentUser);
            feedBack.setContent("你好，请把你想反馈的问题在此处反馈，我们将会对您给出的反馈给出回应，" +
                    "并给出解决方案。");
            feedBack.setType(1);
            feedBack.save(FeedBackActivity.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    System.out.println("系统发出消息：" + "成功");
                    query();
                }

                @Override
                public void onFailure(int i, String s) {
                    System.out.println("系统发出消息：" + s);
                }
            });
        }

        rv.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        etFeedback = (EditText) findViewById(R.id.etComment);
        etFeedback.setMaxHeight(400);

        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(etFeedback.getText().toString())){
                    btnSendFeedback.setVisibility(View.VISIBLE);
                }
                else {
                    btnSendFeedback.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etFeedback.addTextChangedListener(textWatcher);
        etFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutManager.scrollToPositionWithOffset(adapter.getItemCount()-1,0);
            }
        });        btnSendFeedback = (ImageButton) findViewById(R.id.btnSendPinlun);
        btnSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FeedBack feedBack = new FeedBack();
                feedBack.setContent(etFeedback.getText().toString());
                feedBack.setType(2);
                feedBack.setUser(currentUser);
                feedBack.save(FeedBackActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        etFeedback.setText("");
                        adapter.addFeedback(feedBack);
                        final FeedBack feedBack1 = new FeedBack();
                        feedBack1.setUser(currentUser);
                        feedBack1.setContent("感谢您的反馈，您反馈的内容已记录。");
                        feedBack1.setType(1);
                        feedBack1.save(FeedBackActivity.this, new SaveListener() {
                            @Override
                            public void onSuccess() {
                                System.out.println("系统发出消息：" + "成功");
                                adapter.addFeedback(feedBack1);
                                layoutManager.scrollToPositionWithOffset(adapter.getItemCount()-1,0);
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                System.out.println("系统发出消息：" + s);
                            }
                        });


                    }

                    @Override
                    public void onFailure(int i, String s) {
                        toast("反馈失败,"+s);
                    }
                });
            }
        });


        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ExitApplication.getInstance().addActivities(this);
    }

    private void query() {
        final ProgressDialog progressDialog = ProgressDialog.show(this,null,"加载中...");
        final MyUser currentUser = BmobUser.getCurrentUser(this, MyUser.class);
        BmobQuery<FeedBack> query = new BmobQuery<>();
        query.include("");
        query.addWhereEqualTo("user",new BmobPointer(currentUser));
        query.findObjects(this, new FindListener<FeedBack>() {
            @Override
            public void onSuccess(List<FeedBack> list) {
                adapter.bindData(list);
                adapter.notifyDataSetChanged();
                layoutManager.scrollToPositionWithOffset(list.size()-1,0);
                progressDialog.dismiss();
            }

            @Override
            public void onError(int i, String s) {
                progressDialog.dismiss();
                toast("加载失败,"+s);
            }
        });
    }
    private void toast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

}
