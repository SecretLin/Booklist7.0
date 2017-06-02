package com.example.secret.booklist60;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Secret on 2016/10/28.
 * 将所有activity收入，实现退出程序
 */

public class ExitApplication extends Application {
    private List<Activity> activities = new ArrayList<>();
    private static ExitApplication instance;

    /*
    synchronized 同步，一次只能运行一个线程
     */
    public synchronized static ExitApplication getInstance(){
        if (instance == null){
            instance = new ExitApplication();
        }
        return instance;
    }
    public void addActivities(Activity activity){
        activities.add(activity);
    }

    public void exit(){
        for (Activity activity:activities){
            if (activity!=null){
                activity.finish();
            }

        }
        System.exit(0);
        checkBrowser(getPackageName());
    }
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
    public boolean checkBrowser(String packageName) {
        if (packageName == null || "".equals(packageName))

            return false;
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(
                    packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            System.out.println("-------------获取到包名");
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
