package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.ExitApplication;
import com.example.secret.booklist60.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/*
    设置界面
    目前实现退出登录、修改密码、以及是否接收陌生人消息的功能
 */
public class SettingActivity extends Activity {
    TextView btnChangePW,btnLogout,tvActionbar;
    ImageButton btnBack;
    Switch btnPrivateMsg,btnPush;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        tvActionbar = (TextView) findViewById(R.id.tvTitle);
        tvActionbar.setText("设置");

        final MyUser currentUser = BmobUser.getCurrentUser(this,MyUser.class);

        btnLogout = (TextView) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser.logOut(SettingActivity.this);
                startActivity(new Intent(SettingActivity.this,LoginActivity.class));

            }
        });

        btnChangePW = (TextView) findViewById(R.id.btnChangePW);
        btnChangePW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,ChangePWActivity.class));

            }
        });

        btnPrivateMsg = (Switch) findViewById(R.id.switch_PrivateMsg);
        if (currentUser!=null){
            btnPrivateMsg.setChecked(currentUser.isReceiveStrangerMsg());
        }

        btnPrivateMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (currentUser!=null){
                    if (b){
                        currentUser.setReceiveStrangerMsg(true);
                        currentUser.update(SettingActivity.this, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(SettingActivity.this,"开启成功",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(SettingActivity.this,"开启失败，"+s,Toast.LENGTH_SHORT).show();
                            }
                        });
                        btnPrivateMsg.setChecked(true);
                    }
                    else {
                        btnPrivateMsg.setChecked(false);
                    }
                    System.out.println(b);
                }

            }
        });


        btnPush = (Switch) findViewById(R.id.switch_Tuisong);

        btnPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

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
}
