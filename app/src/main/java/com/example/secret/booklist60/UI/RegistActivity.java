package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.ExitApplication;
import com.example.secret.booklist60.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.listener.SaveListener;
/*
   注册界面
 */
public class RegistActivity extends Activity {
    private EditText etUsername,etPW,etConPW,etEmail;
    private ImageButton btnRegist,btnClear;
    private MyUser user;
    private int length;
    String username,pw,email;
    private TextView tvPWStrong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        etUsername = (EditText) findViewById(R.id.etUsername_Regist);
        etPW = (EditText) findViewById(R.id.etPW_Regist);
        etConPW = (EditText) findViewById(R.id.etConPW_Regist);
        etEmail = (EditText) findViewById(R.id.etEmail_Regist);
        etUsername.getBackground().setAlpha(50);
        etConPW.getBackground().setAlpha(50);
        etPW.getBackground().setAlpha(50);
        etEmail.getBackground().setAlpha(50);

        tvPWStrong = (TextView) findViewById(R.id.tvPWStrong);

        btnRegist = (ImageButton) findViewById(R.id.btnRegist);


        user = new MyUser();
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = etUsername.getText().toString();
                pw = etPW.getText().toString();
                email = etEmail.getText().toString();
                length =pw.length();
                System.out.println(length);
                isValuable();
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

        ExitApplication.getInstance().addActivities(this);

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
        //设置密码强度
        final TextWatcher etPWWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(etPW.getText().toString())){
                    //只有纯数字或纯英文
                    String weak = "([a-zA-Z0-9])";
                    Pattern weakP = Pattern.compile(weak);
                    Matcher weakM = weakP.matcher(etPW.getText().toString());
                    if (weakM.find()){
                        tvPWStrong.setText("弱");

                    }
                    //数字跟英文结合
                    String num="([0-9])";
                    String eng = "([a-zA-Z])";
                    Pattern midiumP = Pattern.compile(num);
                    Pattern midiumP1 = Pattern.compile(eng);
                    Matcher midiumM = midiumP.matcher(etPW.getText().toString());
                    Matcher midiumM1 = midiumP1.matcher(etPW.getText().toString());
                    String zifu = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                    Pattern strongP = Pattern.compile(zifu);
                    Matcher strongM = strongP.matcher(etPW.getText().toString());
                    if (midiumM.find() && midiumM1.find()){
                        if (strongM.find()){
                            //数字英文符合结合
                            tvPWStrong.setText("强");
                        }
                        else {
                            tvPWStrong.setText("中");
                        }

                    }
                    tvPWStrong.setVisibility(View.VISIBLE);
                }
                else {
                    tvPWStrong.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etPW.addTextChangedListener(etPWWatcher);

    }

    private void isValuable() {

        String regEx="([a-zA-Z0-9])";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(username);
        if( !m.find()){
            toast("用户名只能由字母大小写和数字组成");
            return;
        }
        if (length<6 || length>10){
            toast("密码长度不符");
            return;
        }
        if (!pw.equals(etConPW.getText().toString())){
            toast("密码不一致");
            return;
        }
        user.setUsername(username);
        user.setPassword(pw);
        user.setEmail(email);
        user.setReceiveStrangerMsg(false);
        user.signUp(RegistActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("注册成功");
                startActivity(new Intent(RegistActivity.this,LoginActivity.class));
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                toast("注册失败："+s);
            }
        });
    }

    public void toast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
