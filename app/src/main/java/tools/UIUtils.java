package tools;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;

/****************************
 *
 *
 *    工具类，，隐藏软键盘，，状态栏，，导航栏
 *
 *
 * ******************************/

public class UIUtils {

    public static void hideKeyboardAndStatusBar(Activity activity) {
        // 取消一进入页面弹出软键盘
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // 隐藏状态栏
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 隐藏底部工具栏
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
