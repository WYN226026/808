package tools.view;

import static com.example.freezecl4_zcx.StartActivity.cpuid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.example.freezecl4_zcx.R;
import com.king.zxing.util.CodeUtils;

import java.lang.reflect.Field;

import tools.windowmanager.AssistMenuWindowManager;

public class AssistTouchViewLayout extends LinearLayout {
    //用于记录小悬浮窗得宽度
    public static  int viewWidth;
    //记录小悬浮窗得高度
    public static  int viewHeight;
    //记录系统状态栏得高度
    private static  int statusBarHeight;
    //用于更新小悬浮窗得位置
    private WindowManager windowManager;
    //小悬浮窗得参数
    private WindowManager.LayoutParams mParams;
    //记录当前手指位置在屏幕上得横坐标
    private float xInScreen;
    //记录当前手指位置在屏幕上得纵坐标
    private float yInScreen;
    //记录手指摁下时在屏幕上得横坐标的值
    private float xDownInScreen;
    //记录手指摁下时在屏幕上的纵坐标的值
    private float yDownInScreen;
    //记录手指摁下时在小悬浮窗的view上的横坐标的值
    private float xInView;
    //记录手指摁下时在小悬浮窗的view上的纵坐标的值
    private float yInView;
    private static final int CLICK_THRESHOLD = 10; // 10 pixels
    private boolean  isShowEWM = false;
    private ImageView iv_icon;
    private ImageView iv_ewm;
    private Bitmap bitmap;

    public AssistTouchViewLayout(Context context) {
        super(context);
        windowManager =(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_assist,this);
        View view = findViewById(R.id.small_window_layout);
        iv_icon = findViewById(R.id.iv_icon);
        iv_ewm = findViewById(R.id.iv_ewm);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        initEWM();
    }
    private void initEWM() {
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        bitmap  = CodeUtils.createQRCode("cpuid:"+cpuid,240,logo);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:

                float deltaX = Math.abs(xDownInScreen - xInScreen);
                float deltaY = Math.abs(yDownInScreen - yInScreen);
                // 判断移动距离是否在阈值内
                if (deltaX < CLICK_THRESHOLD && deltaY < CLICK_THRESHOLD) {
                    if(!isShowEWM){
                        isShowEWM = true;
                        iv_icon.setVisibility(View.GONE);
                        iv_ewm.setVisibility(View.VISIBLE);
                        iv_ewm.setImageBitmap(bitmap);
                    }else{
                        isShowEWM = false;
                        iv_icon.setVisibility(View.VISIBLE);
                        iv_ewm.setVisibility(View.GONE);
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }
    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params
     *            小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    /**
     * 打开大悬浮窗，同时关闭小悬浮窗。
     */
    private void openBigWindow() {
        AssistMenuWindowManager.createBigWindow(getContext());
        AssistMenuWindowManager.removeSmallWindow(getContext());
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }


    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

}
