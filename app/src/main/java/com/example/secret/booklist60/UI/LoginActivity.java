package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.ExitApplication;
import com.example.secret.booklist60.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/*
   登录界面
 */
public class LoginActivity extends Activity {
    private EditText etUsername,etPw;
    private TextView tvForget;
    private ImageButton btnLogin,btnClear;
    private MyUser user;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        etUsername = (EditText) findViewById(R.id.etUsername_Login);
        etUsername.getBackground().setAlpha(50);
        etPw = (EditText) findViewById(R.id.etPW_Login);
        etPw.getBackground().setAlpha(50);
        btnLogin = (ImageButton) findViewById(R.id.btnLogin);
        tvForget = (TextView) findViewById(R.id.tvForget);
        tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,FindPwActivity.class));

            }
        });




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etUsername.getText().toString();
                String pw = etPw.getText().toString();
                user = new MyUser();
                user.setUsername(name);
                user.setPassword(pw);
                progressDialog = ProgressDialog.show(LoginActivity.this,null,"正在登录....");
                user.login(LoginActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                        toast("登录成功");
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        progressDialog.dismiss();
                        toast("登录失败："+s);
                    }
                });
            }
        });


        btnClear = (ImageButton) findViewById(R.id.btnClear);
        btnClear.getBackground().setAlpha(100);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etUsername.setText("");
            }
        });

        TextWatcher etUsernameWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(etUsername.getText().toString().trim())){
                    btnClear.setVisibility(View.VISIBLE);
                }
                else {
                    btnClear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etUsername.addTextChangedListener(etUsernameWatcher);
        ExitApplication.getInstance().addActivities(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            return true;
        }

        return false;
    }

    public void toast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
