package com.example.secret.booklist60.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secret.booklist60.DataBase.MyUser;
import com.example.secret.booklist60.ExitApplication;
import com.example.secret.booklist60.R;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.UpdateListener;
import thirdParty.Constants;
import thirdParty.NetUtils;

/**
 * Created by Secret on 2016/9/13.
 * 首页
 */
public class HomeActivity extends Activity {
    Button btnRegist,btnLogin;
    ImageView ivQQ,ivSina;
    TextView tvSkipLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        //登录按钮
        btnLogin = (Button) findViewById(R.id.btnLogin_Home);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 startActivity(new Intent(HomeActivity.this,LoginActivity.class));

            }
        });
        //注册按钮
        btnRegist = (Button) findViewById(R.id.btnRegist_Home);
        btnRegist.getBackground().mutate().setAlpha(50);
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,RegistActivity.class));
            }
        });
        //获取当前用户，下次打开该app就可以直接登录
        MyUser currentUser = BmobUser.getCurrentUser(this,MyUser.class);
        if (currentUser!=null){
            startActivity(new Intent(HomeActivity.this,MainActivity.class));
            finish();
        }

//        /*
//        以下为第三方账号登录
//         */
//        ShareSDK.initSDK(HomeActivity.this);
////        EventBus.getDefault().register(this);
//
        ivQQ = (ImageView) findViewById(R.id.ivQQlogin);
        //使用qq登录
        ivQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  qqAuthorize();
//                ShareSDKUtils.Login(QQ.NAME);


            }
        });
//
//        ivSina = (ImageView) findViewById(R.id.ivSinaLogin);
//        ivSina.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ShareSDKUtils.Login(SinaWeibo.NAME);
//            }
//        });

        tvSkipLogin = (TextView) findViewById(R.id.tvSkipLogin);
        tvSkipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
            }
        });

        ExitApplication.getInstance().addActivities(this);
    }

    /**
     * @method loginWithAuth

     * @exception
     */
    public void loginWithAuth(final BmobUser.BmobThirdUserAuth authInfo,final String token,final String expires,final String openId){
        BmobUser.loginWithAuthData(HomeActivity.this, authInfo, new OtherLoginListener() {

            @Override
            public void onSuccess(JSONObject userAuth) {
                // TODO Auto-generated method stub
                Log.i("smile",authInfo.getSnsType()+"登陆成功返回:"+userAuth);




                new Thread() {
                    @Override
                    public void run() {
                        Map<String, String> params = new HashMap<String, String>();
                        String token1 = token;
                        String openId1 = openId;

                        MyUser currentUser = BmobUser.getCurrentUser(HomeActivity.this,MyUser.class);
                        String username = NetUtils.getUsername("https://graph.qq.com/user/get_user_info?"+"access_token="+token
                                +"&openid="+openId+"&oauth_consumer_key="+"1105898198");
                        String head = NetUtils.getHeadUrl("https://graph.qq.com/user/get_user_info?"+"access_token="+token
                                +"&openid="+openId+"&oauth_consumer_key="+"1105898198");
                        System.out.println("QQ的个人信息：" + username);
                        currentUser.setUsername(username);
                        currentUser.setUrlHead(head);
                        currentUser.update(HomeActivity.this, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                System.out.println("成功");
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                System.out.println("失败，"+s);
                            }
                        });


                    }

                }.start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
//                        intent.putExtra("json", userAuth.toString());
//                        intent.putExtra("from", authInfo.getSnsType());
                intent.putExtra("token",token);
//                        intent.putExtra("expires",expires);
                intent.putExtra("openId",openId);
//                        System.out.println("--------------user:"+userAuth);
                startActivity(intent);
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                toast("第三方登陆失败："+msg);
            }

        });
    }
    public static Tencent mTencent;
    private void qqAuthorize(){
        if(mTencent==null){
            mTencent = Tencent.createInstance(Constants.QQ_APP_ID, getApplicationContext());
        }
        System.out.println("-------------tencent:"+mTencent);
        mTencent.logout(HomeActivity.this);
        mTencent.login(this, "all", new IUiListener() {

            @Override
            public void onComplete(Object arg0) {
                // TODO Auto-generated method stub
                if(arg0!=null){
                    JSONObject jsonObject = (JSONObject) arg0;
                    try {
                        String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
                        String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
                        String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
                        BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ,token, expires,openId);
                        loginWithAuth(authInfo,token,expires,openId);
                    } catch (JSONException e) {
                    }
                }

            }

            @Override
            public void onError(UiError arg0) {
                // TODO Auto-generated method stub
                toast("QQ授权出错："+arg0.errorCode+"--"+arg0.errorDetail);
            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                toast("取消qq授权");
            }
        });













    }


    private void toast(String msg){
        Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
    }


    /*
    双击退出程序
     */
    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click(); //调用双击退出函数
        }
        return false;
    }
    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            ExitApplication.getInstance().exit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);

    }
}
