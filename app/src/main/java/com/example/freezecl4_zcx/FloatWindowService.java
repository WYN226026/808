package com.example.freezecl4_zcx;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tools.EwmDB;
import tools.windowmanager.AssistMenuWindowManager;

/**
 * 原：检测通过内存检测应用是否进入后台
 * 现：实时检测当前界面是否是桌面
 */
public class FloatWindowService extends Service {
    private Handler handler  = new Handler();
    private Timer timer;
    private boolean isOpen = true;
    private EwmDB ewmDB;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ewmDB= new EwmDB(this);
        if(ewmDB != null){
            ewmDB.openWriteDB();
            ewmDB.openReadDB();
        }
        if(timer == null ){
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Service被终止的同时也停止定时器继续运行
        if(timer != null){
            timer.cancel();
            timer = null;
        }

    }

    // 定时器；实时检测
    class RefreshTask extends TimerTask{
        @Override
        public void run() {
            if(!isHome(getApplicationContext())&&isOpen){       //不处于桌面
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        fullWindows();
                        AssistMenuWindowManager.createSmallWindow(getApplicationContext());
                        AssistMenuWindowManager.setFullScreen(getApplicationContext(),true);
                        isOpen = false;
                    }
                });
            }
            if(isHome(getApplicationContext())){            // 处于桌面。
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        isOpen = true;
                        AssistMenuWindowManager.removeSmallWindow(getApplicationContext());
                        AssistMenuWindowManager.removeBigWindow(getApplicationContext());
                    }
                });
            }
        }
    }

    /**
     * 判断当前界面是否是桌面
     */
    private boolean isHome(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
        if (runningProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.processName.equals(context.getPackageName())) {
                    return processInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
                }
            }
        }
        return false;
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }
    private void  fullWindows(){
        // 在Service中获取当前活动的上下文
        Context context = getApplicationContext(); // 这里可以根据实际情况获取到上下文
        // 设置全屏显示
         if(context instanceof Activity){
            Activity activity = (Activity) context;
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

    }
}
