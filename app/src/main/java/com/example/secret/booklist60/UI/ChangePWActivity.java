package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secret.booklist60.ExitApplication;
import com.example.secret.booklist60.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
/*
   修改密码的界面
 */
public class ChangePWActivity extends Activity {
    EditText etOldPW,etNewPW, etNewPWCon;
    ImageButton btnQueren,btnBack;
    TextView tvActionbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        tvActionbar = (TextView) findViewById(R.id.tvTitle);
        tvActionbar.setText("修改密码");

        etOldPW = (EditText) findViewById(R.id.etOldPW);
        etNewPW = (EditText) findViewById(R.id.etNewPW);
        etNewPWCon = (EditText) findViewById(R.id.etNewPWCon);



        btnQueren = (ImageButton) findViewById(R.id.btnQueren);
        btnQueren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String NewPW = etNewPW.getText().toString();
                String NewPWCon = etNewPWCon.getText().toString();
                String oldPW = etOldPW.getText().toString();

                if (NewPW.equals(NewPWCon)) {
                    if (NewPW.length()<6 || NewPW.length()>10){
                        toast("密码长度不符");
                        return;
                    }
                    BmobUser.updateCurrentUserPassword(ChangePWActivity.this, oldPW, NewPW, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            toast("修改密码成功！");
                            new AlertDialog.Builder(ChangePWActivity.this).setMessage("密码已修改，需重新登录").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    BmobUser.logOut(ChangePWActivity.this);
                                    startActivity(new Intent(ChangePWActivity.this,LoginActivity.class));

                                }
                            }).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            toast("修改失败："+s);
                        }
                    });
                }
                else {
                    toast("密码不一致");
                }

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
    public void toast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
