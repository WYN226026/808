package tools;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

import com.example.freezecl4_zcx.R;
public class ShowWifiErrorDialog {
    static AlertDialog alertDialog;
    static int showError = 0 ;
    public static void showWifiError(Context context,WindowManager windowManager){
        if (showError == 1) {
            // 如果对话框已经显示，则不重复显示
            return;
        }
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_wifi_error, null);
        // 设置自定义布局到对话框
        builder.setView(dialogView);
        // 创建并显示对话框
        alertDialog = builder.create();
        // 获取对话框窗口的视图
        View dialogWindow = alertDialog.getWindow().getDecorView();
        // 设置动画背景
        dialogWindow.setBackgroundResource(R.drawable.warn_wifi_updown);
        // 启动帧动画
        AnimationDrawable animationDrawable = (AnimationDrawable) dialogWindow.getBackground();
        if (animationDrawable != null) {
            animationDrawable.start();
        } else {
            throw new IllegalStateException("Background is not an AnimationDrawable");
        }

        // 显示对话框
        alertDialog.setCancelable(false); // 禁止通过返回键取消
        alertDialog.setCanceledOnTouchOutside(false); // 禁止点击外部取消
        alertDialog.show();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        layoutParams.width = (int) (width * 0.5);
        layoutParams.height = (int) (height * 0.07);
        alertDialog.getWindow().setAttributes(layoutParams);
        // 标记对话框已显示
        showError = 1;
    }
    public static  void hideWifiError(){
        if(alertDialog != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    System.out.println("wifiError :::  隐藏");
                    showError = 0 ;
                }
            });
        }
    }
}
