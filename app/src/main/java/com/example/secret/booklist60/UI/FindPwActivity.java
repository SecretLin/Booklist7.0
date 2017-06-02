package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secret.booklist60.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;

/*
   找回密码界面
 */
public class FindPwActivity extends Activity {
    EditText etEmail;
    ImageButton btnSendEmail,btnBack;
    private TextView tvActionbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        tvActionbar = (TextView) findViewById(R.id.tvTitle);
        tvActionbar.setText("找回密码");

        btnSendEmail = (ImageButton) findViewById(R.id.btnSendEmail);
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = etEmail.getText().toString();
                BmobUser.resetPasswordByEmail(FindPwActivity.this, email, new ResetPasswordByEmailListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(FindPwActivity.this,"重置密码请求成功，请到" + email + "邮箱进行密码重置操作",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(FindPwActivity.this,"重置密码请求失败：" +s,Toast.LENGTH_SHORT).show();
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

    }
}
