package com.example.secret.booklist60;

import android.app.Application;

import com.example.secret.booklist60.utils.DemoMessageHandler;

import cn.bmob.newim.BmobIM;



/**
 * 一开始进入的application，初始化bmob
 */
public class MyApplication extends Application{

    private static MyApplication INSTANCE;
    public static MyApplication INSTANCE(){
        return INSTANCE;
    }
    private void setInstance(MyApplication app) {
        setBmobIMApplication(app);
    }
    private static void setBmobIMApplication(MyApplication a) {
        MyApplication.INSTANCE = a;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        //im初始化
        BmobIM.init(this);
        //注册消息接收器
        BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));

        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        com.igexin.sdk.PushManager.getInstance().initialize(this.getApplicationContext(), com.example.secret.booklist60.push.DemoPushService.class);
// com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        com.igexin.sdk.PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), com.example.secret.booklist60.push.DemoIntentService.class);
    }
}
