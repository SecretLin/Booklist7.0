package com.example.secret.booklist60.utils;

import android.util.Log;

import com.example.secret.booklist60.event.MapEvent;
import com.example.secret.booklist60.event.RefreshEvent;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import de.greenrobot.event.EventBus;

/**
 * Created by Secret on 2017/2/5.
 */

public class ShareSDKUtils {
    int MSG_USERID_FOUND = 1;
    public static void Login(String name) {
        System.out.println("login ing..");
        Platform mPlatform = ShareSDK.getPlatform(name);
        authorize(mPlatform);
//        mPlatform.setPlatformActionListener(mPlatformActionListener);
//       // mPlatform.authorize();//单独授权,OnComplete返回的hashmap是空的
//
//        mPlatform.showUser(null);//授权并获取用户信息

    }

    static public void authorize(Platform plat) {
//        if (plat == null) {
//            popupOthers();
//            return;
//        }
//判断指定平台是否已经完成授权
        if(plat.isAuthValid()) {
            String userId = plat.getDb().getUserId();
            if (userId != null) {
//                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
//                login(plat.getName(), userId, null);
//                Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                Map<String,String> map = new HashMap<>();
                map.put("username",plat.getDb().getUserName());
                map.put("id",plat.getDb().getUserId());
                map.put("avatar",plat.getDb().getUserIcon());
                EventBus.getDefault().post(new MapEvent(map));
                return;
            }
        }
        plat.setPlatformActionListener(mPlatformActionListener);
        // true不使用SSO授权，false使用SSO授权
//        plat.SSOSetting(true);
        //获取用户资料
        plat.showUser(null);
    }

    public static PlatformActionListener mPlatformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            Log.i("onComplete", "登录成功");
            Log.i("openid", platform.getDb().getUserId());//拿到登录后的openid
            Log.i("username", platform.getDb().getUserName());//拿到登录用户的昵称

            EventBus.getDefault().post(new RefreshEvent(platform.getDb().getUserName()));

    }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Log.e("onError", throwable.toString() + "");

            Log.e("onError", "登录失败" + throwable.toString());

        }

        @Override
        public void onCancel(Platform platform, int i) {

            Log.e("onCancel", "登录取消");

        }
    };
}
