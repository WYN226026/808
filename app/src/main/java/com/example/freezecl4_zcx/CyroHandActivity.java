package com.example.freezecl4_zcx;

import static com.example.freezecl4_zcx.StartActivity.cl2_temp1;
import static com.example.freezecl4_zcx.StartActivity.cl2_temp2;
import static com.example.freezecl4_zcx.StartActivity.cryo_p1;
import static com.example.freezecl4_zcx.StartActivity.cryo_p2;
import static com.example.freezecl4_zcx.StartActivity.cl2_p1;
import static com.example.freezecl4_zcx.StartActivity.cl2_p2;
import static com.example.freezecl4_zcx.StartActivity.cryo_temp1;
import static com.example.freezecl4_zcx.StartActivity.cryo_temp2;
import static com.example.freezecl4_zcx.StartActivity.cryo_flow1;
import static com.example.freezecl4_zcx.StartActivity.cryo_flow2;
import static com.example.freezecl4_zcx.StartActivity.network;
import static com.example.freezecl4_zcx.StartActivity.version;
import static com.example.freezecl4_zcx.StartActivity.webSocketService;
import static tools.CRC16Modbus.calcCrc16Modbus;
import static tools.UIUtils.hideKeyboardAndStatusBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import tools.DataTypeConversion;
import tools.PermissionsDB;
import tools.SerialPortUtil;
import tools.ShowWifiErrorDialog;

public class CyroHandActivity extends AppCompatActivity {
    private ImageButton bt_return;
    private ImageButton bt_set;
    private Button bt_hand1;
    private Button bt_hand2;
    private ImageView iv_hand1;
    private ImageView iv_hand2;
    private final SerialPortUtil serialPortUtil= new SerialPortUtil();
    private Button bt_error;
    private Handler blinkHandler = new Handler();
    private Runnable blinkRunnable;
    private boolean isBlinking = false;
    private int identicalCounter = 0;
    private int identicalCounter1 = 0;
    private int identicalCounter2 = 0;
    private PermissionsDB permissionsDB;
    private volatile boolean isRunning = false;
    private int handle_null_number = 0  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboardAndStatusBar(this);
        setContentView(R.layout.activity_cyro_hand);
        initDB();
        serialPortUtil.serialPort();
        initView();
        data();
        sendCPU();
        onclick();
    }
    private void initDB() {
        permissionsDB = PermissionsDB.getInstance(this);
        permissionsDB.openWriteDB();
        permissionsDB.openReadDB();
    }
    private void showError(String error){
        SpannableString spannableString = new SpannableString(error);
        ForegroundColorSpan redColorSpan = new ForegroundColorSpan(Color.RED);
        spannableString.setSpan(redColorSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置文本字号大小（例如，20sp）
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(100, true); // 第二个参数为true表示单位为sp
        spannableString.setSpan(sizeSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(spannableString)
                .setCancelable(true)
                .create();

        // 设置 AlertDialog 背景为透明
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        alertDialog.show();
        // 获取 AlertDialog 的 TextView 并设置文本居中
        TextView messageView = alertDialog.findViewById(android.R.id.message);
        if (messageView != null) {
            messageView.setGravity(Gravity.CENTER);
        }
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });
        // 三秒后自动消失
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        }, 3000); // 3000 毫秒等于 3 秒
    }
    private void initView(){
        bt_return = findViewById(R.id.bt_return);
        bt_set = findViewById(R.id.bt_set);
        bt_hand1 = findViewById(R.id.bt_hand1);
        bt_hand2 = findViewById(R.id.bt_hand2);
        iv_hand1 = findViewById(R.id.iv_hand1);
        iv_hand2 = findViewById(R.id.iv_hand2);
        bt_error = findViewById(R.id.bt_error);
        bt_error.setVisibility(View.GONE);
        hand_display();
    }
    private void sendCPU(){
        String data1 = "AA01000000000000000000000000";
        String data  = calcCrc16Modbus("AA01000000000000000000000000");
        serialPortUtil.serial_send(data1+data+"CC");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String data1 = "AA01000000000000000000000000";
                String data  = calcCrc16Modbus("AA01000000000000000000000000");
                serialPortUtil.serial_send(data1+data+"CC");
            }
        },300);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String data1 = "AA01000000000000000000000000";
                String data  = calcCrc16Modbus("AA01000000000000000000000000");
                serialPortUtil.serial_send(data1+data+"CC");
            }
        },600);

    }
    private void startHandleThread() {
        // 如果线程正在运行，直接返回
        if (isRunning) {
            System.out.println("线程正在运行，拒绝重复启动");
            return;
        }

        // 启动新线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    isRunning = true; // 标记线程开始运行
                    String data1 = "AA01000000000000000000000000";
                    for (int i = 0; i < 5; i++) {
                        // 计算CRC16
                        String crcData = calcCrc16Modbus(data1);
                        // 拼接完整指令
                        String fullCommand = data1 + crcData + "CC";
                        // 发送指令
                        serialPortUtil.serial_send(fullCommand);

                        // 打印调试信息（可选）
                        System.out.println("发送第 " + (i + 1) + " 次: " + fullCommand);

                        // 休眠200毫秒（最后一次不休眠）
                        if (i < 4) {
                            Thread.sleep(200);
                        }
                    }
                } catch (InterruptedException e) {
                    // 处理线程中断异常
                    Thread.currentThread().interrupt();
                    System.out.println("线程被中断");
                } finally {
                    isRunning = false; // 标记线程结束
                }
            }
        }).start();
    }
    private void data() {
        StartActivity.myHandler = new Handler(Looper.getMainLooper()) {
            @SuppressLint({"HandlerLeak", "DefaultLocale"})
            @Override
            public void handleMessage(@Nullable Message msg) {
                super.handleMessage(msg);
                if (msg != null && msg.obj.toString().length() == 34) {
                    String data_receive = msg.obj.toString();
                    final String anObject = data_receive.substring(28, 32);
                    Log.e("", "wl: " + data_receive);

                    if (data_receive.startsWith("AAF1")) {
                        if (calcCrc16Modbus(data_receive.substring(0, 28)).equals(anObject)) {
                            Log.e("读取cpu", "读取cpu成功 " + data_receive);
                            cl2_p1 = Integer.valueOf(data_receive.substring(4, 6), 16);
                            cl2_p2 = Integer.valueOf(data_receive.substring(6, 8), 16);
                            cl2_temp1 = Integer.valueOf(data_receive.substring(8, 10), 16);
                            cl2_temp2 = Integer.valueOf(data_receive.substring(10, 12), 16);
                            cryo_p1 = Integer.valueOf(data_receive.substring(12, 14), 16);
                            cryo_p2 = Integer.valueOf(data_receive.substring(14, 16), 16);
                            cryo_temp1 = Integer.valueOf(data_receive.substring(16, 18), 16);
                            cryo_temp2 = Integer.valueOf(data_receive.substring(18, 20), 16);
                            cryo_flow1 = Integer.valueOf(data_receive.substring(20, 22), 16);
                            cryo_flow2 = Integer.valueOf(data_receive.substring(22, 24), 16);
                            version = String.valueOf(Integer.valueOf(data_receive.substring(24, 28),16));
                            //03
                            // 03
                            // 1A
                            // 09
                            // 04
                            // 02
                            // 0000120003F44225CC
                            if(cryo_p1 == 0  || cryo_p2 == 0 ){
                                startHandleThread();
                                handle_null_number ++ ;
                                if(handle_null_number == 5 ){
                                    hand_display();
                                    handle_null_number = 0 ;
                                }
                            }
                            else{
                                hand_display();
                            }
                            if(cryo_p1 == cryo_p2){
                                identicalCounter++;
                                if(identicalCounter>5) {
                                    startBlinking(R.drawable.error);
                                    bt_hand1.setEnabled(false);
                                    bt_hand2.setEnabled(false);
                                }
                            }else {
                                identicalCounter=0;
                                stopBlinking();
                                bt_hand1.setEnabled(true);
                                bt_hand2.setEnabled(true);
                            }
                        } else {

                        }
                    }
                }
            }
        };
    }
    // 开始闪烁并设置不同的报警图片
    private void startBlinking(int alarmImageResId) {
        if (isBlinking) return; // 如果已经在闪烁，避免重复启动
        isBlinking = true;

        bt_error.setBackgroundResource(alarmImageResId); // 设置报警图片

        blinkRunnable = new Runnable() {
            @Override
            public void run() {
                if (bt_error.getVisibility() == View.VISIBLE) {
                    bt_error.setVisibility(View.GONE); // 隐藏
                } else {
                    bt_error.setVisibility(View.VISIBLE); // 显示
                }

                // 每100ms 切换可见性
                blinkHandler.postDelayed(this, 100);
            }
        };

        blinkHandler.post(blinkRunnable); // 立即开始闪烁
    }

    // 停止闪烁的函数
    private void stopBlinking() {
        isBlinking = false;
        if (blinkRunnable != null) {
            blinkHandler.removeCallbacks(blinkRunnable); // 停止闪烁
            bt_error.setVisibility(View.GONE); // 确保隐藏图标
        }
    }
    private void onclick(){
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CyroHandActivity.this,ModeActivity.class);
                startActivity(intent);
            }
        });
        bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CyroHandActivity.this,CyroSet1Activity.class);
                startActivity(intent);
            }
        });
        bt_hand1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CyroHandActivity.this,CyroModeActivity.class);
//                startActivity(intent);

                if(permissionsDB.queryFlag("flag1") == 0){
                    showError(getString(R.string.error1));
                }
                else if (permissionsDB.queryFlag("timing") == 0 && permissionsDB.queryFlag("timingSecond") == 0){
                    showError(getString(R.string.error2));
                }
                else {
                    sendMessageToServer("page_cyro_work");
                    Intent intent = new Intent(CyroHandActivity.this,CyroWorkActivity.class);
                    startActivity(intent);
                }
            }
        });
        bt_hand2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CyroHandActivity.this,CyroModeActivity.class);
//                startActivity(intent);

                if(permissionsDB.queryFlag("flag1") == 0){
                    showError(getString(R.string.error1));
                }
                else if (permissionsDB.queryFlag("timing") == 0 && permissionsDB.queryFlag("timingSecond") == 0){
                    showError(getString(R.string.error2));
                }
                else {
                    sendMessageToServer("page_cyro_work");
                    Intent intent = new Intent(CyroHandActivity.this,CyroWorkActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            System.out.println("page  EmsModeActivity get  "+ message);
            if("page_mode".equals(message)){
                sendMessageToServer("page_mode_ok");
                Intent intent1 = new Intent(CyroHandActivity.this,ModeActivity.class);
                startActivity(intent1);
            }
            else if ("page_cyro_mode".equals(message)){
                sendMessageToServer("page_cyro_mode_ok");
                Intent intent1 = new Intent(CyroHandActivity.this,CyroWorkActivity.class);
                startActivity(intent1);
            }
            else if ("Which_page".equals(message)){
                sendMessageToServer("page_ems_mode");
            }
        }
    };
    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            if("wifiAvailable".equals(message)){
                inset();
                ShowWifiErrorDialog.hideWifiError();
            }
            else {
                recovery();
                ShowWifiErrorDialog.showWifiError(CyroHandActivity.this,getWindowManager());
            }
        }
    };
    private void recovery(){
        // 恢复软键盘显示
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 恢复正常的系统UI显示
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;  // 恢复正常UI显示
        decorView.setSystemUiVisibility(uiOptions);

        // 使布局宽度适应屏幕
        getWindow().getDecorView().setPadding(0, 0, 0, 0); // 清除额外的内边距

        // 确保界面宽度匹配屏幕
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }
    private void inset(){
        //取消一进入页面弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏底部工具栏
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void sendMessageToServer(String message) {
        if (webSocketService != null) {
            webSocketService.sendMessage(message);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("websocket_message_received");
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,intentFilter);
        IntentFilter intentFilter1 = new IntentFilter("network_message_received");
        LocalBroadcastManager.getInstance(this).registerReceiver(networkReceiver,intentFilter1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(networkReceiver);
    }
    @SuppressLint("SetTextI18n")
    private void hand_display(){
        switch (cryo_p1){
            case 0:
                identicalCounter1++;
                if(identicalCounter1>5) {
                    iv_hand1.setBackgroundResource(R.drawable.error1);
                    bt_hand1.setText("?");
                    iv_hand1.setEnabled(false);
                }
                break;
            case 1:
                identicalCounter1=0;
                iv_hand1.setBackgroundResource(R.drawable.a);
                bt_hand1.setText("A1");
                iv_hand1.setEnabled(true);
                break;
            case 2:
                identicalCounter1=0;
                iv_hand1.setBackgroundResource(R.drawable.b);
                bt_hand1.setText("B1");
                iv_hand1.setEnabled(true);
                break;
            case 3:
                identicalCounter1=0;
                iv_hand1.setBackgroundResource(R.drawable.c);
                bt_hand1.setText("C1");
                iv_hand1.setEnabled(true);
                break;
            case 4:
                identicalCounter1=0;
                iv_hand1.setBackgroundResource(R.drawable.b);
                bt_hand1.setText("B2");
                iv_hand1.setEnabled(true);
                break;
            case 5:
                identicalCounter1=0;
                iv_hand1.setBackgroundResource(R.drawable.a);
                bt_hand1.setText("A2");
                iv_hand1.setEnabled(true);
                break;
            case 6:
                identicalCounter1=0;
                iv_hand1.setBackgroundResource(R.drawable.c);
                bt_hand1.setText("C2");
                iv_hand1.setEnabled(true);
                break;
            case 7:
                identicalCounter1=0;
                iv_hand1.setBackgroundResource(R.drawable.d);
                bt_hand1.setText("D1");
                iv_hand1.setEnabled(true);
                break;
            case 8:
                identicalCounter1=0;
                iv_hand1.setBackgroundResource(R.drawable.d);
                bt_hand1.setText("D2");
                iv_hand1.setEnabled(true);
                break;
            case 9:
                identicalCounter1=0;
                iv_hand1.setBackgroundResource(R.drawable.a);
                bt_hand1.setText("A3");
                iv_hand1.setEnabled(true);
                break;
            case 10:
                identicalCounter1=0;
                iv_hand1.setBackgroundResource(R.drawable.b);
                bt_hand1.setText("B3");
                iv_hand1.setEnabled(true);
                break;
            case 11:
                identicalCounter1=0;
                iv_hand1.setBackgroundResource(R.drawable.c);
                bt_hand1.setText("C3");
                iv_hand1.setEnabled(true);
                break;
            case 12:
                identicalCounter1=0;
                iv_hand1.setBackgroundResource(R.drawable.b);
                bt_hand1.setText("B4");
                iv_hand1.setEnabled(true);
                break;
            case 13:
                identicalCounter1=0;
                iv_hand1.setBackgroundResource(R.drawable.a);
                bt_hand1.setText("A4");
                iv_hand1.setEnabled(true);
                break;
            case 14:
                identicalCounter1=0;
                iv_hand1.setBackgroundResource(R.drawable.c);
                bt_hand1.setText("C4");
                iv_hand1.setEnabled(true);
                break;
            case 15:
                identicalCounter1=0;
                iv_hand1.setBackgroundResource(R.drawable.d);
                bt_hand1.setText("D3");
                iv_hand1.setEnabled(true);
                break;
            case 16:
                identicalCounter1=0;
                iv_hand1.setBackgroundResource(R.drawable.d);
                bt_hand1.setText("D4");
                iv_hand1.setEnabled(true);
                break;
            default:
                identicalCounter1++;
                if(identicalCounter1>5) {
                    iv_hand1.setBackgroundResource(R.drawable.error1);
                    bt_hand1.setText("?");
                    iv_hand1.setEnabled(false);
                }
                break;
        }
        switch (cryo_p2){
            case 0:
                identicalCounter2++;
                if(identicalCounter2>5) {
                    iv_hand2.setBackgroundResource(R.drawable.error1);
                    bt_hand2.setText("?");
                    iv_hand2.setEnabled(false);
                }
                break;
            case 1:
                identicalCounter2=0;
                iv_hand2.setBackgroundResource(R.drawable.a);
                bt_hand2.setText("A1");
                iv_hand2.setEnabled(true);
                break;
            case 2:
                identicalCounter2=0;
                iv_hand2.setBackgroundResource(R.drawable.b);
                bt_hand2.setText("B1");
                iv_hand2.setEnabled(true);
                break;
            case 3:
                identicalCounter2=0;
                iv_hand2.setBackgroundResource(R.drawable.c);
                bt_hand2.setText("C1");
                iv_hand2.setEnabled(true);
                break;
            case 4:
                identicalCounter2=0;
                iv_hand2.setBackgroundResource(R.drawable.b);
                bt_hand2.setText("B2");
                iv_hand2.setEnabled(true);
                break;
            case 5:
                identicalCounter2=0;
                iv_hand2.setBackgroundResource(R.drawable.a);
                bt_hand2.setText("A2");
                iv_hand2.setEnabled(true);
                break;
            case 6:
                identicalCounter2=0;
                iv_hand2.setBackgroundResource(R.drawable.c);
                bt_hand2.setText("C2");
                iv_hand2.setEnabled(true);
                break;
            case 7:
                identicalCounter2=0;
                iv_hand2.setBackgroundResource(R.drawable.d);
                bt_hand2.setText("D1");
                iv_hand2.setEnabled(true);
                break;
            case 8:
                identicalCounter2=0;
                iv_hand2.setBackgroundResource(R.drawable.d);
                bt_hand2.setText("D2");
                iv_hand2.setEnabled(true);
                break;
            case 9:
                identicalCounter2=0;
                iv_hand2.setBackgroundResource(R.drawable.a);
                bt_hand2.setText("A3");
                iv_hand2.setEnabled(true);
                break;
            case 10:
                identicalCounter2=0;
                iv_hand2.setBackgroundResource(R.drawable.b);
                bt_hand2.setText("B3");
                iv_hand2.setEnabled(true);
                break;
            case 11:
                identicalCounter2=0;
                iv_hand2.setBackgroundResource(R.drawable.c);
                bt_hand2.setText("C3");
                iv_hand2.setEnabled(true);
                break;
            case 12:
                identicalCounter2=0;
                iv_hand2.setBackgroundResource(R.drawable.b);
                bt_hand2.setText("B4");
                iv_hand2.setEnabled(true);
                break;
            case 13:
                identicalCounter2=0;
                iv_hand2.setBackgroundResource(R.drawable.a);
                bt_hand2.setText("A4");
                iv_hand2.setEnabled(true);
                break;
            case 14:
                identicalCounter2=0;
                iv_hand2.setBackgroundResource(R.drawable.c);
                bt_hand2.setText("C4");
                iv_hand2.setEnabled(true);
                break;
            case 15:
                identicalCounter2=0;
                iv_hand2.setBackgroundResource(R.drawable.d);
                bt_hand2.setText("D3");
                iv_hand2.setEnabled(true);
                break;
            case 16:
                identicalCounter2=0;
                iv_hand2.setBackgroundResource(R.drawable.d);
                bt_hand2.setText("D4");
                iv_hand2.setEnabled(true);
                break;
            default:
                identicalCounter2++;
                if(identicalCounter2>5) {
                    iv_hand2.setBackgroundResource(R.drawable.error1);
                    bt_hand2.setText("?");
                    iv_hand2.setEnabled(false);
                }
                break;
        }
    }
}