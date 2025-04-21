package com.example.freezecl4_zcx;

import static tools.CRC16Modbus.calcCrc16Modbus;
import static tools.UIUtils.hideKeyboardAndStatusBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import tools.DataTypeConversion;
import tools.SerialPortUtil;
import tools.SetDataBase;
import tools.ShowWifiErrorDialog;

public class CyroSet1Activity extends AppCompatActivity {
    private volatile boolean isRunning = true; // 控制线程是否运行的标志位
    private final Object lock = new Object();
    private Thread F7thread;
    private Button bt_return;
    private Button bt_sub;
    private Button bt_add;
    private Button bt_a1;
    private Button bt_a2;
    private Button bt_a3;
    private Button bt_a4;
    private Button bt_b1;
    private Button bt_b2;
    private Button bt_b3;
    private Button bt_b4;
    private TextView tv_flow;
    private TextView tv_a1;
    private TextView tv_a2;
    private TextView tv_a3;
    private TextView tv_a4;
    private TextView tv_b1;
    private TextView tv_b2;
    private TextView tv_b3;
    private TextView tv_b4;
    private Button bt_work;
    private Button bt_next;
    private Button bt_save;
    private int flow = 30;
    private final StringBuilder initializationData = new StringBuilder();
    private int a_num = 0;
    private int b_num = 0;
    private final SerialPortUtil serialPortUtil= new SerialPortUtil();
    private int work_flag = 0;
    private int yali1 = 0;
    private int yali2 = 0;
    private Handler handler;
    private boolean flag;

    @Override
    protected void onStop() {
        super.onStop();
        pauseF7(); // 暂停
    }

    @Override
    protected void onStart() {
        super.onStart();
        resumeF7();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboardAndStatusBar(this);
        setContentView(R.layout.activity_cyro_set1);
        serialPortUtil.serialPort();
        initView();
        data();
        handler();
        onclick();
        sendF7();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter1 = new IntentFilter("network_message_receiver");
        LocalBroadcastManager.getInstance(this).registerReceiver(networkReceiver,intentFilter1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(networkReceiver);
    }

    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            if("wifiAvailable".equals(message)){
                inset();
                ShowWifiErrorDialog.hideWifiError();
            }
            else if ("wifiUnavailable".equals(message)){
                recovery();
                ShowWifiErrorDialog.showWifiError(CyroSet1Activity.this,getWindowManager());
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
    private void initView(){
        bt_return = findViewById(R.id.bt_return);
        bt_sub = findViewById(R.id.bt_sub);
        bt_add = findViewById(R.id.bt_add);
        bt_a1 = findViewById(R.id.bt_a1);
        bt_a2 = findViewById(R.id.bt_a2);
        bt_a3 = findViewById(R.id.bt_a3);
        bt_a4 = findViewById(R.id.bt_a4);
        bt_b1 = findViewById(R.id.bt_b1);
        bt_b2 = findViewById(R.id.bt_b2);
        bt_b3 = findViewById(R.id.bt_b3);
        bt_b4 = findViewById(R.id.bt_b4);
        tv_flow = findViewById(R.id.tv_flow);
        tv_a1 = findViewById(R.id.tv_a1);
        tv_a2 = findViewById(R.id.tv_a2);
        tv_a3 = findViewById(R.id.tv_a3);
        tv_a4 = findViewById(R.id.tv_a4);
        tv_b1 = findViewById(R.id.tv_b1);
        tv_b2 = findViewById(R.id.tv_b2);
        tv_b3 = findViewById(R.id.tv_b3);
        tv_b4 = findViewById(R.id.tv_b4);
        bt_work = findViewById(R.id.bt_work);
        bt_next = findViewById(R.id.bt_next);
        bt_save = findViewById(R.id.bt_save);

        SetDataBase db = new SetDataBase(CyroSet1Activity.this);
        flow =  db.getSingleColumnDataById(1,"flag3");
        tv_flow.setText(String.valueOf(flow));
    }
    private void data(){
        StartActivity.myHandler = new Handler(Looper.getMainLooper()) {
            @SuppressLint({"HandlerLeak", "DefaultLocale"})
            @Override
            public void handleMessage(@Nullable Message msg) {
                super.handleMessage(msg);
                if (msg != null && msg.obj.toString().length() == 34) {
                    String data_receive = msg.obj.toString();
                    final String anObject = data_receive.substring(28, 32);
                    Log.e("", "wl: " + data_receive);

                    if (data_receive.startsWith("AAF7")) {
                        if (calcCrc16Modbus(data_receive.substring(0, 28)).equals(anObject)) {
                            Log.e("冷冻设置读取", "冷冻设置读取成功 " + data_receive);
                            yali1 = Integer.valueOf(data_receive.substring(6, 8), 16);
                            yali2 = Integer.valueOf(data_receive.substring(10, 12), 16);
                            if(a_num == 1){
                                tv_a1.setText(String.valueOf(yali1));
                            }else if(a_num == 2){
                                tv_a2.setText(String.valueOf(yali1));
                            }else if(a_num == 3){
                                tv_a3.setText(String.valueOf(yali1));
                            }else if(a_num == 4){
                                tv_a4.setText(String.valueOf(yali1));
                            }
                            if(b_num == 1){
                                tv_b1.setText(String.valueOf(yali2));
                            }else if(b_num == 2){
                                tv_b2.setText(String.valueOf(yali2));
                            }else if(b_num == 3){
                                tv_b3.setText(String.valueOf(yali2));
                            }else if(b_num == 4){
                                tv_b4.setText(String.valueOf(yali2));
                            }
                        }
                    }

                }
            }
        };
    }
    private void sendF7() {
        F7thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("","AAF8发送成功");
                while (!Thread.currentThread().isInterrupted()) {
                    synchronized (lock) {
                        while (!isRunning) {
                            try {
                                lock.wait(); // 等待被唤醒
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt(); // 设置线程的中断状态为true
                            }
                        }
                    }
                    Log.e("warn2","AA07发送成功");
                    sendCPU();
                    try {
                        Thread.sleep(500); // 每次发送后等待500毫秒
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // 设置线程的中断状态为true
                    }
                }
            }
        });
        F7thread.start();
    }
    // 暂停线程
    private void pauseF7() {
        isRunning = false;
    }
    // 继续线程
    private void resumeF7() {
        isRunning = true;
        synchronized (lock) {
            lock.notify(); // 唤醒线程
        }
    }
    private void sendCPU(){
        initializationData.delete(0, initializationData.length());
        initializationData.append("AA07")
                .append(DataTypeConversion.intToHex2(a_num))
                .append("00")
                .append(DataTypeConversion.intToHex2(b_num))
                .append("000000")
                .append(DataTypeConversion.intToHex2(work_flag))
                .append("0000000000");
        String crc = calcCrc16Modbus(initializationData.toString());
        String send = initializationData.append(crc).append("cc").toString();
        serialPortUtil.serial_send(send);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void onclick(){
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CyroSet1Activity.this,CyroHandActivity.class);
                startActivity(intent);
            }
        });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDataBase db = new SetDataBase(CyroSet1Activity.this);
                db.updateSingleColumn(1,"flag3",flow); //保存水流报警值
            }
        });
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CyroSet1Activity.this, CyroSet2Activity.class);
                startActivity(intent);
            }
        });
        bt_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(work_flag == 0){
                    work_flag = 1;
                    bt_work.setBackgroundResource(R.drawable.work4);
                }else {
                    work_flag = 0;
                    bt_work.setBackgroundResource(R.drawable.work3);
                }
            }
        });
        bt_sub.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_sub.setBackgroundResource(R.drawable.bt_sub_down);
                    flag = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (flag){
                                if (System.currentTimeMillis() > 100) {
                                    flow--;
                                    if(flow <= 0){
                                        flow = 0;
                                    }
                                    Message msg = handler.obtainMessage();
                                    msg.arg1 = flow;
                                    handler.sendMessage(msg);
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                    a.start();
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    bt_sub.setBackgroundResource(R.drawable.bt_sub_up);
                    flag = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    flag = false;
                }
                return true;
            }
        });
        bt_add.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_add.setBackgroundResource(R.drawable.bt_add_down);
                    flag = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (flag){
                                if (System.currentTimeMillis() > 100) {
                                    flow++;
                                    if(flow >= 20){
                                        flow = 20;
                                    }
                                    Message msg = handler.obtainMessage();
                                    msg.arg1 = flow;
                                    handler.sendMessage(msg);
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                    a.start();
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    bt_add.setBackgroundResource(R.drawable.bt_add_up);
                    flag = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    flag = false;
                }
                return true;
            }
        });
        bt_a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a_num = 1;
                bt_a1.setBackgroundResource(R.drawable.bt_a1_down);
                bt_a2.setBackgroundResource(R.drawable.bt_a2_up);
                bt_a3.setBackgroundResource(R.drawable.bt_a3_up);
                bt_a4.setBackgroundResource(R.drawable.bt_a4_up);

            }
        });

        bt_a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a_num = 2;
                bt_a1.setBackgroundResource(R.drawable.bt_a1_up);
                bt_a2.setBackgroundResource(R.drawable.bt_a2_down);
                bt_a3.setBackgroundResource(R.drawable.bt_a3_up);
                bt_a4.setBackgroundResource(R.drawable.bt_a4_up);

            }
        });
        bt_a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a_num = 3;
                bt_a1.setBackgroundResource(R.drawable.bt_a1_up);
                bt_a2.setBackgroundResource(R.drawable.bt_a2_up);
                bt_a3.setBackgroundResource(R.drawable.bt_a3_down);
                bt_a4.setBackgroundResource(R.drawable.bt_a4_up);

            }
        });
        bt_a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a_num = 4;
                bt_a1.setBackgroundResource(R.drawable.bt_a1_up);
                bt_a2.setBackgroundResource(R.drawable.bt_a2_up);
                bt_a3.setBackgroundResource(R.drawable.bt_a3_up);
                bt_a4.setBackgroundResource(R.drawable.bt_a4_down);

            }
        });
        bt_b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_num = 1;
                bt_b1.setBackgroundResource(R.drawable.bt_b1_down);
                bt_b2.setBackgroundResource(R.drawable.bt_b2_up);
                bt_b3.setBackgroundResource(R.drawable.bt_b3_up);
                bt_b4.setBackgroundResource(R.drawable.bt_b4_up);

            }
        });
        bt_b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_num = 2;
                bt_b1.setBackgroundResource(R.drawable.bt_b1_up);
                bt_b2.setBackgroundResource(R.drawable.bt_b2_down);
                bt_b3.setBackgroundResource(R.drawable.bt_b3_up);
                bt_b4.setBackgroundResource(R.drawable.bt_b4_up);

            }
        });
        bt_b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_num = 3;
                bt_b1.setBackgroundResource(R.drawable.bt_b1_up);
                bt_b2.setBackgroundResource(R.drawable.bt_b2_up);
                bt_b3.setBackgroundResource(R.drawable.bt_b3_down);
                bt_b4.setBackgroundResource(R.drawable.bt_b4_up);

            }
        });
        bt_b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_num = 4;
                bt_b1.setBackgroundResource(R.drawable.bt_b1_up);
                bt_b2.setBackgroundResource(R.drawable.bt_b2_up);
                bt_b3.setBackgroundResource(R.drawable.bt_b3_up);
                bt_b4.setBackgroundResource(R.drawable.bt_b4_down);
            }
        });
    }
    private void handler(){
        handler = new Handler(msg -> {
            tv_flow.setText(String.valueOf(flow));
            return true;
        });
    }
}