package com.example.freezecl4_zcx;

import static tools.CRC16Modbus.calcCrc16Modbus;
import static tools.UIUtils.hideKeyboardAndStatusBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import tools.DataTypeConversion;
import tools.SerialPortUtil;
import tools.SetDataBase;
import tools.ShowWifiErrorDialog;

public class EmsSet2Activity extends AppCompatActivity {
    private Button bt_return;
    private Button bt_save;
    private IndicatorSeekBar sk_t1;
    private IndicatorSeekBar sk_t2;
//    private IndicatorSeekBar sk_rf_duty_cycle;
    private int t1 = 3;
    private int t2 = 3;
    private final SerialPortUtil serialPortUtil= new SerialPortUtil();
    private int p12v= 0;
    private int p12rfv= 0;
    private int rf= 0;
    private int pv = 200;
    private int rfv= 380;
    private final StringBuilder initializationData = new StringBuilder();
    private int error_temp = 70;
    private IndicatorSeekBar sk_rf_out;
    private Button bt_wifi;
    private int wifi_error = 0 ;// 默认为开
    private TextView tv_wifi;
    private Button bt_set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboardAndStatusBar(this);
        setContentView(R.layout.activity_ems_set2);
        initView();
        data();
        sendCPU();
        onclick();
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
                ShowWifiErrorDialog.showWifiError(EmsSet2Activity.this,getWindowManager());
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
    private void sendCPU(){
        String data1 = "AA04000000000000000000000000";
        String data  = calcCrc16Modbus("AA04000000000000000000000000");
        serialPortUtil.serial_send(data1+data+"CC");
    }
    private void sendCPU1(){
        initializationData.delete(0, initializationData.length());
        initializationData.append("AA05")
                .append(DataTypeConversion.intToHex4(0))
                .append(DataTypeConversion.intToHex4(rfv))
                .append(DataTypeConversion.intToHex2(rf/2))
                .append(DataTypeConversion.intToHex2(70))
                .append("000000000000");
        String crc = calcCrc16Modbus(initializationData.toString());
        String send = initializationData.append(crc).append("cc").toString();
        serialPortUtil.serial_send(send);
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
                    if (data_receive.startsWith("AAF4")) {
                        if (calcCrc16Modbus(data_receive.substring(0, 28)).equals(anObject)) {
                            Log.e("磁立瘦设置读取", "磁立瘦设置读取成功 " + data_receive);
                            p12v = Integer.valueOf(data_receive.substring(4, 8), 16);
                            p12rfv = Integer.valueOf(data_receive.substring(8, 12), 16);
                            rf = Integer.valueOf(data_receive.substring(12, 14), 16);
                            error_temp = Integer.valueOf(data_receive.substring(14, 16), 16);
                        }
                    }
                    if (data_receive.startsWith("AAF5")) {
                        Log.e("磁立瘦设置读取", "磁立瘦设置读取成功 " + data_receive);

                    }

                }
            }
        };
    }
    private void initView(){
        bt_return = findViewById(R.id.bt_return);
        bt_save = findViewById(R.id.bt_save);
        sk_t1 = findViewById(R.id.sk_t1);
        sk_t2 = findViewById(R.id.sk_t2);
        sk_rf_out = findViewById(R.id.sk_rf_out);
        sk_t1.setIndicatorTextFormat("${PROGRESS} ");
        sk_t2.setIndicatorTextFormat("${PROGRESS} ");
        sk_rf_out.setIndicatorTextFormat("${PROGRESS} ");
        bt_wifi = findViewById(R.id.bt_wifi);
        tv_wifi  = findViewById(R.id.tv_wifi);
        bt_set = findViewById(R.id.bt_set);
        SetDataBase db = new SetDataBase(EmsSet2Activity.this);
        t1 =  db.getSingleColumnDataById(1,"flag26");
        t2 =  db.getSingleColumnDataById(1,"flag27");
        rf =  db.getSingleColumnDataById(1,"flag28");
        pv =  db.getSingleColumnDataById(1,"flag29");
        rfv =  db.getSingleColumnDataById(1,"flag30");
        sk_t1.setProgress(t1);
        sk_t2.setProgress(t2);
        sk_rf_out.setProgress(rfv);
        wifi_error = db.getSingleColumnDataById(1,"flag31");
        if(wifi_error == 1 ){
            bt_wifi.setBackgroundResource(R.drawable.bt_on);
        }
        else {
            bt_wifi.setBackgroundResource(R.drawable.bt_off);
        }
    }
    private void onclick(){
        bt_set.setOnClickListener(new View.OnClickListener() {
            // 定义变量来跟踪点击次数和上次点击的时间
            private int clickCount = 0;
            private long lastClickTime = 0;
            private final int REQUIRED_CLICK_COUNT = 5;
            private final int CLICK_DURATION = 3000; // 3 seconds
            @Override
            public void onClick(View v) {
                long currentTime = System.currentTimeMillis();

                // 如果两次点击之间的时间大于3秒，重置点击次数
                if (currentTime - lastClickTime > CLICK_DURATION) {
                    clickCount = 0;
                }

                // 更新上次点击时间
                lastClickTime = currentTime;

                // 增加点击次数
                clickCount++;
                if (clickCount == REQUIRED_CLICK_COUNT) {
                    clickCount = 0; // Reset for next series of clicks
                    // 扩充密码输入布局
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.password_input_layout, null);
                    final EditText etPassword = dialogView.findViewById(R.id.et_password);
                    Button btnConfirm = dialogView.findViewById(R.id.bt_confirm);

                    // Create
                    final AlertDialog passwordInputDialog = new AlertDialog.Builder(EmsSet2Activity.this)
                            .setView(dialogView)
                            .create();

                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (etPassword.getText().toString().equals("QH2023")) {
                                passwordInputDialog.dismiss();
                                tv_wifi.setVisibility(View.VISIBLE);
                                bt_wifi.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(EmsSet2Activity.this,"Wrong password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    passwordInputDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            hideKeyboardAndStatusBar(EmsSet2Activity.this);
                        }
                    });
                    passwordInputDialog.show();
                }
            }
        });

        bt_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wifi_error == 1){
                    wifi_error = 0;
                    bt_wifi.setBackgroundResource(R.drawable.bt_off);
                }
                else {
                    wifi_error = 1;
                    bt_wifi.setBackgroundResource(R.drawable.bt_on);
                }
            }
        });

        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmsSet2Activity.this,EmsSetActivity.class);
                startActivity(intent);
            }
        });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCPU1();
                SetDataBase db = new SetDataBase(EmsSet2Activity.this);
                db.updateSingleColumn(1,"flag31",wifi_error);
            }
        });
        sk_t1.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                t1 = seekParams.progress;

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

                SetDataBase db = new SetDataBase(EmsSet2Activity.this);
                db.updateSingleColumn(1,"flag26",t1);
            }
        });
        sk_t2.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                t2 = seekParams.progress;

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

                SetDataBase db = new SetDataBase(EmsSet2Activity.this);
                db.updateSingleColumn(1,"flag27",t2);
            }
        });
//        sk_rf_duty_cycle.setOnSeekChangeListener(new OnSeekChangeListener() {
//            @Override
//            public void onSeeking(SeekParams seekParams) {
//                rf = seekParams.progress;
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
//                sendCPU1();
//                SetDataBase db = new SetDataBase(EmsSet2Activity.this);
//                db.updateSingleColumn(1,"flag28",rf);
//            }
//        });
//        sk_v.setOnSeekChangeListener(new OnSeekChangeListener() {
//            @Override
//            public void onSeeking(SeekParams seekParams) {
//                pv = seekParams.progress;
//            }
//
//            @Override
//            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
//                sendCPU1();
//                SetDataBase db = new SetDataBase(EmsSet2Activity.this);
//                db.updateSingleColumn(1,"flag29",pv);
//            }
//        });
        sk_rf_out.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                rfv = seekParams.progress;
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                sendCPU1();
                SetDataBase db = new SetDataBase(EmsSet2Activity.this);
                db.updateSingleColumn(1,"flag30",rfv);
            }
        });
    }
}