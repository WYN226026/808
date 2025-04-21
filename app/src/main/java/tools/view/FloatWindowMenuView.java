package tools.view;


import static com.example.freezecl4_zcx.StartActivity.cpuid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.freezecl4_zcx.R;
import com.king.zxing.util.CodeUtils;

import java.lang.reflect.Field;

import tools.windowmanager.AssistMenuWindowManager;

public class FloatWindowMenuView extends LinearLayout {
    private WindowManager windowManager;
    private WindowManager.LayoutParams mParams;
    private static  int statusBarHeight;
    private float xDownInScreen;
    //记录手指摁下时在屏幕上的纵坐标的值
    private float yDownInScreen;
    //记录手指摁下时在小悬浮窗的view上的横坐标的值
    private float xInView;
    //记录手指摁下时在小悬浮窗的view上的纵坐标的值
    private float yInView;
    //记录大悬浮窗的宽度
    public static  int viewWidth;
    //记录大悬浮窗的高度
    public static  int viewHeight;
    Context context;
    private float xInScreen;
    //记录当前手指位置在屏幕上得纵坐标
    private float yInScreen;
    //记录手指摁下时在屏幕上得横坐标的值
    Intent intent;
    private Bitmap bitmap;


    public FloatWindowMenuView(Context context,boolean type) {
        super(context);
        this.context = context;
        windowManager =(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_menu,this);
        View view = findViewById(R.id.big_window_layout);
        ImageView iv_ewm = findViewById(R.id.iv_ewm);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        initEWM();
        if(bitmap !=null){
            iv_ewm.setImageBitmap(bitmap);
        }
        intent = new Intent("floatWindowMenuView_received");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.setBackgroundColor(Color.argb(128, 255, 255, 255));
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AssistMenuWindowManager.removeBigWindow(context);
                AssistMenuWindowManager.createSmallWindow(context);
                intent.putExtra("message","close");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });


    }
    private void initEWM() {
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        bitmap  = CodeUtils.createQRCode("cpuid:"+cpuid,240,logo);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isPointInsideView(event.getX(), event.getY())) {
                    AssistMenuWindowManager.removeBigWindow(context);
                    AssistMenuWindowManager.createSmallWindow(context);
                    intent.putExtra("message","close");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
                break;
            // 其他事件的处理，例如 MOVE 和 UP 可以根据需要进行添加
        }
        return true;
    }


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
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);

    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }
    private boolean isPointInsideView(float x, float y) {
        int[] location = new int[2];
        getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        // 检查给定坐标是否在视图的范围内
        return (x >= viewX && x <= (viewX + viewWidth) &&
                y >= viewY && y <= (viewY + viewHeight));
    }
}
