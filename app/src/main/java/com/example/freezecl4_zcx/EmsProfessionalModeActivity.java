package com.example.freezecl4_zcx;

import static com.example.freezecl4_zcx.CheckActivity.user_local;
import static com.example.freezecl4_zcx.EmsModeActivity.modePI;
import static com.example.freezecl4_zcx.EmsModeActivity.sk1;
import static com.example.freezecl4_zcx.EmsModeActivity.sk2;
import static com.example.freezecl4_zcx.EmsPartActivity.left_position;
import static com.example.freezecl4_zcx.EmsPartActivity.positionData;
import static com.example.freezecl4_zcx.InformationServiceActivity.userID;
import static com.example.freezecl4_zcx.StartActivity.cl2_temp1;
import static com.example.freezecl4_zcx.StartActivity.cl2_temp2;
import static com.example.freezecl4_zcx.StartActivity.cl2_p1;
import static com.example.freezecl4_zcx.StartActivity.cl2_p2;
import static com.example.freezecl4_zcx.StartActivity.cpuid;
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
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.shawnlin.numberpicker.NumberPicker;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import db.UploadInfoDB;
import db.UserInfoDatabase;
import entity.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.UserInfoService;
import service.api.UserInfoAPI;
import tools.DataTypeConversion;
import tools.PermissionsDB;
import tools.SerialPortUtil;
import tools.SetDataBase;
import tools.ShowWifiErrorDialog;

public class EmsProfessionalModeActivity extends AppCompatActivity {
    private ImageButton bt_return;
    private ImageView bt_p1;
    private ImageView bt_p2;
    private ImageButton bt_work2;
    private NumberPicker first_picker;
    private NumberPicker second_picker;
    private IndicatorSeekBar sk_p1;
    private IndicatorSeekBar sk_p2;
    private IndicatorSeekBar sk_rf1;
    private IndicatorSeekBar sk_rf2;
    private IndicatorSeekBar sk_f1;
    private IndicatorSeekBar sk_f2;
    private IndicatorSeekBar sk_f3;
    private Button bt_jianP1;
    private Button bt_jianP2;
    private Button bt_jianRF1;
    private Button bt_jianRF2;
    private Button bt_jianF1;
    private Button bt_jianF2;
    private Button bt_jianF3;
    private Button bt_jiaP1;
    private Button bt_jiaP2;
    private Button bt_jiaRF1;
    private Button bt_jiaRF2;
    private Button bt_jiaF1;
    private Button bt_jiaF2;
    private Button bt_jiaF3;
    private int work_flag = 0;
    private int pNumFlag1 = 1;
    private int pNumFlag2 = 1;
    Field field;
    Class drawable = R.drawable.class;
    private int position_id;
    private int stop_time;
    private int first_time;
    private String date = "?";
    UserInfoDatabase userInfoDatabase;
    private UploadInfoDB uploadInfoDB;
    private int textP1 = 10;
    private int textP2 = 10;
    private int textRF1 = 0;
    private int textRF2 = 0;
    private int f1 = 30;
    private int f2 = 38;
    private int f3 = 5;
    private String time;//治疗时间
    private ImageView ig_s1;
    private ImageView ig_s2;
    private ImageView ig_s3;
    private ImageView ig_s4;
    private AnimationDrawable animation1;
    private AnimationDrawable animation2;
    private AnimationDrawable animation3;
    private AnimationDrawable animation4;
    private PermissionsDB permissionsDB;
    private boolean p12;
    private boolean rf12;
    private boolean f12;
    private boolean f22;
    private boolean f32;
    private Handler handler1;
    private Handler handler2;
    private Handler handler3;
    private Handler handler4;
    private Handler handler5;
    private Handler handler6;
    private Handler handler7;
    private Button bt_m1;
    private Button bt_m2;
    private int m_mode = 1;
    private int f_max = 50;
    private final StringBuilder initializationData = new StringBuilder();
    private final StringBuilder initializationData1 = new StringBuilder();
    private final SerialPortUtil serialPortUtil= new SerialPortUtil();
    private int temp_error = 1;
    private int sound = 1;
    private int error = 0;//手持急停
    private int error1 = 0;//磁立瘦电源报警
    private Handler handler = new Handler();
    private Runnable sendTask;
    private boolean isSending = false;
    private ImageView iv_error;
    private Handler blinkHandler = new Handler();
    private Runnable blinkRunnable;
    private boolean isBlinking = false;
    private Timer timer;
    private int remainingMinutes = 0;
    private int remainingSeconds = 0;
    private NumberPicker timing_first;
    private NumberPicker timing_second;
    private int rf1_flag1 = 0;//射频开关，   第一个手柄  默认0-关
    private int rf2_flag2 = 0; // 射频开关， 第二个手柄  默认0-关
    private boolean isTimerFirst3 = false;
    private boolean isTimerLast3 = false;
    private boolean isTimerModel = false;
    private boolean isTimer2First3 = false;
    private boolean isTimer2Last3 = false;
    private boolean isTimer2Model = false;
    private int setRFTime1 = 3 ;   // 设置 射频输出时间。  默认为前三后三 。
    private int setRFTime2 = 3 ;   // 设置 射频输出时间。  默认为前三后三 。

    private LinearLayout lin_timing;
    // 手机端使能冲突 ：
    private FirstData firstData = new FirstData();
    private boolean phoneClickFlag1 = false;
    private boolean phoneClickFlag2 = false;
    private boolean isUserTouching = false;

    private final StringBuilder sendDataM7 = new StringBuilder();
    private final StringBuilder sendDataM8 = new StringBuilder();
    private final StringBuilder sendDataM9 = new StringBuilder();
    private SetDataBase setDB ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboardAndStatusBar(this);
        setContentView(R.layout.activity_ems_professional_mode);
        serialPortUtil.serialPort();
        Log.e("", "dataInitiasdalization: " + positionData );
        sk1 = cl2_p1;
        sk2 = cl2_p2;
        initDatabase();
        initDB();
        initView();
        dataInitialization();
        data();
        initSend();
        Log.e("", "dataInitiasdffalization1: " + pNumFlag1  + pNumFlag2 );
        Log.e("", "dataInitiasdffalization3: " + pNumFlag1  + pNumFlag2 );
        handler();
        onclick();
    }

    private void initDB() {
        setRFTime1 = setDB.getSingleColumnDataById(1,"flag26");
        setRFTime2 = setDB.getSingleColumnDataById(1,"flag27");
    }

    private void initDatabase() {
        userInfoDatabase = UserInfoDatabase.getInstance(this);
        userInfoDatabase.openWriteDB();
        userInfoDatabase.openReadDB();

        uploadInfoDB = UploadInfoDB.getInstance(this);
        uploadInfoDB.openWriteDB();
        uploadInfoDB.openReadDB();

        permissionsDB = PermissionsDB.getInstance(this);
        permissionsDB.openReadDB();
        permissionsDB.openWriteDB();

        setDB = new  SetDataBase(EmsProfessionalModeActivity.this);
    }
    // 开始闪烁并设置不同的报警图片
    private void startBlinking(int alarmImageResId) {
        if (isBlinking) return; // 如果已经在闪烁，避免重复启动
        isBlinking = true;

        iv_error.setImageResource(alarmImageResId); // 设置报警图片

        blinkRunnable = new Runnable() {
            @Override
            public void run() {
                if (iv_error.getVisibility() == View.VISIBLE) {
                    iv_error.setVisibility(View.GONE); // 隐藏
                } else {
                    iv_error.setVisibility(View.VISIBLE); // 显示
                }

                // 每100ms 切换可见性
                blinkHandler.postDelayed(this, 500);
            }
        };

        blinkHandler.post(blinkRunnable); // 立即开始闪烁
    }

    // 停止闪烁的函数
    private void stopBlinking() {
        isBlinking = false;
        if (blinkRunnable != null) {
            blinkHandler.removeCallbacks(blinkRunnable); // 停止闪烁
            iv_error.setVisibility(View.GONE); // 确保隐藏图标
        }
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
                        if(calcCrc16Modbus(data_receive.substring(0,28)).equals(anObject)) {
                            Log.e("手柄变化", "手柄变化 " + data_receive);
                            cl2_p1 = Integer.valueOf(data_receive.substring(4, 6), 16);
                            cl2_p2 = Integer.valueOf(data_receive.substring(6, 8), 16);
                            cl2_temp1 = Integer.valueOf(data_receive.substring(8, 10), 16);
                            cl2_temp2 = Integer.valueOf(data_receive.substring(10, 12), 16);
                            handUpdata();
                        }
                    }
                    if (data_receive.startsWith("AAF2")) {
                        if (calcCrc16Modbus(data_receive.substring(0, 28)).equals(anObject)) {
                            Log.e("参数初始化", "pro:参数初始化成功 " + data_receive);
                        } else {
                            sendCPU(rf1_flag1);
                        }
                    }
                    if (data_receive.startsWith("AAF6")) {
                        if (calcCrc16Modbus(data_receive.substring(0, 28)).equals(anObject)) {
                            Log.e("读取cpu", "读取cpu成功 " + data_receive);
                            cl2_temp1 = Integer.valueOf(data_receive.substring(4, 6), 16);
                            cl2_temp2 = Integer.valueOf(data_receive.substring(6, 8), 16);
                            error = Integer.valueOf(data_receive.substring(24, 26), 16);
                            error1 = Integer.valueOf(data_receive.substring(26, 28), 16);
                            EmsProfessionalModeActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (temp_error == 1){
                                        if (cl2_temp1 > 70) {
                                            stopWork();
                                            sound = 200;
                                            startBlinking(R.drawable.error_p1_temp);
                                            bt_work2.setEnabled(false);
                                            isTimerFirst3 = false;
                                            isTimerLast3 = false;
                                            isTimerModel = false;

                                            isTimer2First3 = false;
                                            isTimer2Last3 = false;
                                            isTimer2Model = false;
                                        } else {
                                            sound = 0;
                                            stopBlinking();
                                            bt_work2.setEnabled(true);
                                        }
                                        if (cl2_temp2 > 70) {
                                            stopWork();
                                            sound = 200;
                                            startBlinking(R.drawable.error_p2_temp);
                                            bt_work2.setEnabled(false);
                                            isTimerFirst3 = false;
                                            isTimerLast3 = false;
                                            isTimerModel = false;

                                            isTimer2First3 = false;
                                            isTimer2Last3 = false;
                                            isTimer2Model = false;
                                        } else {
                                            sound = 0;
                                            stopBlinking();
                                            bt_work2.setEnabled(true);
                                        }
                                    }
                                    if(error1 == 1){
                                        stopWork();
                                        sound = 200;
                                        startBlinking(R.drawable.error_power);
                                        bt_work2.setEnabled(false);
                                        isTimerFirst3 = false;
                                        isTimerLast3 = false;
                                        isTimerModel = false;

                                        isTimer2First3 = false;
                                        isTimer2Last3 = false;
                                        isTimer2Model = false;
                                    }else {
                                        sound = 0;
                                        stopBlinking();
                                        bt_work2.setEnabled(true);
                                    }
                                    if(error == 1){
                                        stopWork();
                                        sound = 200;
                                        startBlinking(R.drawable.error_scjt);
                                        bt_work2.setEnabled(false);
                                        isTimerFirst3 = false;
                                        isTimerLast3 = false;
                                        isTimerModel = false;

                                        isTimer2First3 = false;
                                        isTimer2Last3 = false;
                                        isTimer2Model = false;
                                    }else {
                                        sound = 0;
                                        stopBlinking();
                                        bt_work2.setEnabled(true);
                                    }
                                }
                            });

                        } else {
                            sendCPU1();
                        }
                    }
                }
            }
        };
    }
    private void handUpdata(){  //接收主动上传数据时更新手柄图片
        sk1 = cl2_p1;
        sk2 = cl2_p2;
        if(work_flag == 0) {
            if (sk1 <= 2) {
                pIdL(sk1 + 3 * pNumFlag1 * (work_flag + 1));
                bt_p1.setBackgroundResource(position_id);
            } else {
                bt_p1.setBackgroundResource(R.drawable.s1);
            }
            if (sk2 <= 2) {
                pIdR(sk2 + 3 * pNumFlag1 * (work_flag + 1));
                bt_p2.setBackgroundResource(position_id);
            } else {
                bt_p2.setBackgroundResource(R.drawable.k1);
            }
        }
    }
    private void initSend(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendCPU(rf1_flag1);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                startSending();
            }
        }).start();
    }
    // 开始发送的函数
    private void startSending() {
        if (isSending) return; // 如果已经在发送，避免重复启动
        isSending = true;
        sendTask = new Runnable() {
            @Override
            public void run() {
                sendCPU1(); // 发送CPU1任务
                if (isSending) {
                    handler.postDelayed(this, 500); // 每隔500ms再次执行
                }
            }
        };
        handler.post(sendTask); // 立即开始发送
    }

    // 停止发送的函数
    private void stopSending() {
        isSending = false;
        if (sendTask != null) {
            handler.removeCallbacks(sendTask); // 停止发送任务
        }
    }
    @SuppressLint("Range")
    private void dataInitialization(){
        Log.e("", "dataInitiasdffalization2: " + pNumFlag1  + pNumFlag2 );
        if(sk1 <= 2) {
            pNumFlag1 = 1;
            pIdL(sk1 + 3);
            bt_p1.setBackgroundResource(position_id);
        }else {
            pNumFlag1 = 0;
            bt_p1.setBackgroundResource(R.drawable.s1);
        }

        if(sk2 <= 2) {
            pNumFlag2 = 1;
            pIdR(sk2 + 3);
            bt_p2.setBackgroundResource(position_id);
        }else {
            pNumFlag2 = 0;
            bt_p2.setBackgroundResource(R.drawable.k1);
        }
        Log.e("", "dataInitiasdalization: " + positionData );
        switch (positionData){
            case 1:
                sk_p1.setProgress(60);
                sk_p2.setProgress(60);
                sk_rf1.setProgress(30);
                sk_rf2.setProgress(30);
                textP1 = 60;
                textP2 = 60;
                textRF1 = 30;
                textRF2 = 30;
                break;
            case 2:
                sk_p1.setProgress(50);
                sk_p2.setProgress(50);
                sk_rf1.setProgress(30);
                sk_rf2.setProgress(30);
                textP1 = 50;
                textP2 = 50;
                textRF1 = 30;
                textRF2 = 30;
                break;
            case 3:
                sk_p1.setProgress(40);
                sk_p2.setProgress(40);
                sk_rf1.setProgress(30);
                sk_rf2.setProgress(30);
                textP1 = 40;
                textP2 = 40;
                textRF1 = 30;
                textRF2 = 30;
                break;
            default:
                break;
        }
        Log.e("", "dataInitiasdalization: " + textP1 );
        Log.e("", "dataInitiasdalization: " + textP2 );

    }
    private void initView(){
        bt_return = findViewById(R.id.bt_return);
        bt_p1 = findViewById(R.id.bt_p1);
        bt_p2 = findViewById(R.id.bt_p2);
        bt_work2 = findViewById(R.id.bt_work2);
        first_picker = findViewById(R.id.first_picker);
        second_picker = findViewById(R.id.second_picker);
        sk_p1 = findViewById(R.id.sk_p1);
        sk_p2 = findViewById(R.id.sk_p2);
        sk_rf1 = findViewById(R.id.sk_rf1);
        sk_rf2 = findViewById(R.id.sk_rf2);
        sk_f1 = findViewById(R.id.sk_f1);
        sk_f2 = findViewById(R.id.sk_f2);
        sk_f3 = findViewById(R.id.sk_f3);
        bt_jianP1 = findViewById(R.id.bt_jianP1);
        bt_jianP2 = findViewById(R.id.bt_jianP2);
        bt_jianRF1 = findViewById(R.id.bt_jianRF1);
        bt_jianRF2 = findViewById(R.id.bt_jianRF2);
        bt_jianF1 = findViewById(R.id.bt_jianF1);
        bt_jianF2 = findViewById(R.id.bt_jianF2);
        bt_jianF3 = findViewById(R.id.bt_jianF3);
        bt_jiaP1 = findViewById(R.id.bt_jiaP1);
        bt_jiaP2 = findViewById(R.id.bt_jiaP2);
        bt_jiaRF1 = findViewById(R.id.bt_jiaRF1);
        bt_jiaRF2 = findViewById(R.id.bt_jiaRF2);
        bt_jiaF1 = findViewById(R.id.bt_jiaF1);
        bt_jiaF2 = findViewById(R.id.bt_jiaF2);
        bt_jiaF3 = findViewById(R.id.bt_jiaF3);
        ig_s1 = findViewById(R.id.ig_s1);
        ig_s2 = findViewById(R.id.ig_s2);
        ig_s3 = findViewById(R.id.ig_s3);
        ig_s4 = findViewById(R.id.ig_s4);
        bt_m1 = findViewById(R.id.bt_m1);
        bt_m2 = findViewById(R.id.bt_m2);
        iv_error = findViewById(R.id.iv_error);

        first_picker.setMaxValue(60);
        first_picker.setMinValue(0);
        second_picker.setMaxValue(60);
        second_picker.setMinValue(0);
        first_picker.setValue(30);
        second_picker.setValue(0);
        first_picker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        second_picker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        first_picker.setWrapSelectorWheel(true);
        second_picker.setWrapSelectorWheel(true);

        ig_s1.setBackgroundResource(R.drawable.animation);
        ig_s2.setBackgroundResource(R.drawable.animation);
        ig_s3.setBackgroundResource(R.drawable.animation);
        ig_s4.setBackgroundResource(R.drawable.animation);
        animation1 = (AnimationDrawable) ig_s1.getBackground();
        animation2 = (AnimationDrawable) ig_s2.getBackground();
        animation3 = (AnimationDrawable) ig_s3.getBackground();
        animation4 = (AnimationDrawable) ig_s4.getBackground();
        animation1.stop();
        animation2.stop();
        animation3.stop();
        animation4.stop();
        ig_s1.setVisibility(View.GONE);
        ig_s2.setVisibility(View.GONE);
        ig_s3.setVisibility(View.GONE);
        ig_s4.setVisibility(View.GONE);

        m_mode = 1;
        f_max = 50;
        sk_f1.setMax(f_max);
        sk_f2.setMax(f_max);
        sk_f3.setMax(f_max);

        sk_p1.setIndicatorTextFormat("${PROGRESS} %");
        sk_p2.setIndicatorTextFormat("${PROGRESS} %");
        sk_rf2.setIndicatorTextFormat("${PROGRESS} %");
        sk_rf1.setIndicatorTextFormat("${PROGRESS} %");
        sk_f1.setIndicatorTextFormat("${PROGRESS} Hz");
        sk_f2.setIndicatorTextFormat("${PROGRESS} Hz");
        sk_f3.setIndicatorTextFormat("${PROGRESS} Hz");

        timing_first = findViewById(R.id.timing_first);
        timing_second = findViewById(R.id.timing_second);
        lin_timing = findViewById(R.id.lin_timing);
        //设置为不可选中。
        timing_first.setEnabled(false);
        timing_second.setEnabled(false);

        timing_first.setSelectedTextColor(Color.RED);
        timing_second.setSelectedTextColor(Color.RED);
        if(permissionsDB.queryFlag("flag2")==1){
            System.out.println("flag2 ==- "+1);
            timing_first.setVisibility(View.VISIBLE);
            timing_second.setVisibility(View.VISIBLE);
            lin_timing.setVisibility(View.VISIBLE);
        }else{
            lin_timing.setVisibility(View.GONE);
            System.out.println("flag2 ==- "+0);
            timing_first.setVisibility(View.GONE);
            timing_second.setVisibility(View.GONE);
        }
        timing_first.setValue(permissionsDB.queryFlag("timing"));
        timing_second.setValue(permissionsDB.queryFlag("timingSecond"));

        SetDataBase db = new SetDataBase(EmsProfessionalModeActivity.this);
        temp_error =  db.getSingleColumnDataById(1,"flag1");
//        sound = db.getSingleColumnDataById(1,"flag2");

        sk1 = cl2_p1;
        sk2 = cl2_p2;

        Log.e("", "initView: "+ sound );
        Log.e("", "initView: "+ temp_error);
    }
    private void startTimer() {
        // 获取分钟和秒钟的值
        int minutes = first_picker.getValue();
        int seconds = second_picker.getValue();

        // 计算总共的毫秒数
        final long[] totalMilliseconds = {(minutes * 60 + seconds) * 1000};
        Log.e("", "onClick:333333333 " +work_flag );

        // 创建计时器
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                    // 更新剩余时间
                    remainingMinutes = (int) (totalMilliseconds[0] / 60000);
                    remainingSeconds = (int) ((totalMilliseconds[0] % 60000) / 1000);
                    totalMilliseconds[0] -= 1000;
                    // 在UI线程更新UI
                    runOnUiThread(new Runnable() {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void run() {
                            // 更新UI显示剩余时间
                            Log.e("", "onClick:4444444 " +work_flag );
                            first_picker.setValue(remainingMinutes);
                            second_picker.setValue(remainingSeconds);
                            // 处理 rf1_flag 的设置
                            int totalMinutes = minutes;
                            int remainingTotalMinutes = remainingMinutes;  // 剩余的分钟数

                            // 前三分钟 (flag 为 1)
                            if (remainingTotalMinutes > totalMinutes - setRFTime1) {
                                if (!isTimerFirst3) {
                                    rf1_flag1 = 1;
                                    isTimerFirst3 = true;
                                    sendCPU(rf1_flag1); // 发送数据
                                }
                            }
                            // 后三分钟 (flag 为 1)
                            else if (remainingTotalMinutes < setRFTime1) {
                                if (!isTimerLast3) {
                                    rf1_flag1 = 1;
                                    isTimerLast3 = true;
                                    sendCPU(rf1_flag1); // 发送数据
                                }
                            }
                            // 中间部分 (flag 为 0)
                            else {
                                if (!isTimerModel) {
                                    rf1_flag1 = 0;
                                    isTimerModel = true;
                                    sendCPU(rf1_flag1); // 发送数据
                                }
                            }

                            // 前
                            if(remainingTotalMinutes > totalMinutes -setRFTime2){
                                if(!isTimer2First3){
                                    rf2_flag2 = 1 ;
                                    isTimer2First3 = true;
                                    sendCPU(rf2_flag2);
                                }
                            }
                            // 后
                            else if (remainingTotalMinutes < setRFTime2){
                                if(!isTimer2Last3){
                                    rf2_flag2 = 1 ;
                                    isTimer2Last3 = true;
                                    sendCPU(rf2_flag2);
                                }
                            }
                            // 中
                            else {
                                if(!isTimer2Model){
                                    rf2_flag2 = 0 ;
                                    isTimer2Model = true;
                                    sendCPU(rf2_flag2);
                                }
                            }

                            if(remainingMinutes == 0 && remainingSeconds == 0){
                                first_picker.setValue(remainingMinutes);
                                second_picker.setValue(remainingSeconds);
                                if(remainingMinutes == 0 && remainingSeconds == 0){  //计时结束保存数据

                                    work_flag = 0;
                                    rf1_flag1=1;
                                    isTimerFirst3 = false;
                                    isTimerLast3 = false;
                                    isTimerModel = false;

                                    isTimer2First3 = false;
                                    isTimer2Last3 = false;
                                    isTimer2Model = false;
                                    stopTimer();
                                    if (pNumFlag1 == 1) {
                                        if (sk1 <= 2) {
                                            pIdL(sk1 + 3);
                                            bt_p1.setBackgroundResource(position_id);
                                        } else {
                                            bt_p1.setBackgroundResource(R.drawable.s1);
                                        }
                                    }
                                    if (pNumFlag2 == 1) {
                                        if (sk2 <= 2) {
                                            pIdR(sk2 + 3);
                                            bt_p2.setBackgroundResource(position_id);
                                        } else {
                                            bt_p2.setBackgroundResource(R.drawable.k1);
                                        }
                                    }
                                    bt_work2.setBackgroundResource(R.drawable.work3);
                                    sendCPU(rf1_flag1);
                                    List<View> disabledViews = Arrays.asList(sk_p1, sk_p2, sk_rf1, sk_rf2, sk_f1, sk_f2, sk_f3, first_picker,
                                            second_picker, bt_jiaP1, bt_jiaP2, bt_jiaRF1, bt_jiaRF2, bt_jianP1, bt_jianP2, bt_m1, bt_m2,
                                            bt_jianRF1, bt_jianRF2, bt_jianF1, bt_jianF2, bt_jianF3, bt_jiaF1, bt_jiaF2, bt_jiaF3,
                                            bt_return);
                                    for (View view : disabledViews) {
                                        view.setEnabled(true);
                                    }
                                    animation1.stop();
                                    animation2.stop();
                                    animation3.stop();
                                    animation4.stop();
                                    ig_s1.setVisibility(View.GONE);
                                    ig_s2.setVisibility(View.GONE);
                                    ig_s3.setVisibility(View.GONE);
                                    ig_s4.setVisibility(View.GONE);
                                    LayoutInflater inflater = getLayoutInflater();
                                    stop_time = first_time - (first_picker.getValue() * 60 + second_picker.getValue());
                                    Log.e("stop_time", String.valueOf(stop_time));
                                    View diaglogView = inflater.inflate(R.layout.custom_dialog_stop, null);
                                    final AlertDialog save_diaglog = new AlertDialog.Builder(EmsProfessionalModeActivity.this)
                                            .setView(diaglogView)
                                            .create();
                                    Window window = save_diaglog.getWindow();
                                    if (window != null) {
                                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    }
                                    Button bt_save = diaglogView.findViewById(R.id.bt_save);
                                    Button bt_back = diaglogView.findViewById(R.id.bt_back);
                                    bt_save.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            date();
                                            @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", stop_time / 60, stop_time % 60);
                                            Log.e("stopTime", String.valueOf(time));
                                            UserInfo userInfo = new UserInfo();
                                            userInfo.setMachine(cpuid);
                                            userInfo.setUserID(userID);
                                            userInfo.setUserInfoID(userInfoDatabase.queryMaxUserInfo(String.valueOf(userID)) + 1);
                                            userInfo.setWork_mode(String.valueOf(modePI));
                                            userInfo.setP12_mode(String.valueOf(textP1));
                                            userInfo.setP34_mode(String.valueOf(textP2));
                                            userInfo.setRf12_mode(String.valueOf(textRF1));
                                            userInfo.setRf34_mode(String.valueOf(textRF2));
                                            userInfo.setMode_f1(String.valueOf(f1));
                                            userInfo.setMode_f2(String.valueOf(f2));
                                            userInfo.setMode_f3(String.valueOf(f3));
                                            userInfo.setMode_options(String.valueOf(0));
                                            userInfo.setWork_date(date);
                                            userInfo.setMode_duration(time);
                                            userInfo.setBody_part(String.valueOf(left_position));
                                            userInfo.setPan("1");
                                            userInfo.setL_forehead_current("0");
                                            userInfo.setR_forehead_current("0");
                                            if (userInfoDatabase.insertUserinfo(userInfo) > 0) {
                                                userInfoAPIInsert(userInfo);
                                            }
                                            save_diaglog.cancel();
                                        }
                                    });
                                    save_diaglog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            hideKeyboardAndStatusBar(EmsProfessionalModeActivity.this);
                                        }
                                    });
                                    bt_back.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            save_diaglog.cancel();
                                        }
                                    });

                                    if (user_local == 2) {
                                        save_diaglog.show();
                                    }

//                                    if(user_local ==2) {
//                                        stop_time = first_time - (first_picker.getValue() * 60 + second_picker.getValue());
//                                        date();
//                                        time = String.format("%02d:%02d", stop_time / 60, stop_time % 60);
//                                        UserInfo userInfo = new UserInfo();
//                                        userInfo.setMachine(cpuid);
//                                        userInfo.setUserID(userID);
//                                        userInfo.setUserInfoID(userInfoDatabase.queryMaxUserInfo(String.valueOf(userID))+1);
//                                        userInfo.setWork_mode(String.valueOf(modePI));
//                                        userInfo.setP12_mode(String.valueOf(textP1));
//                                        userInfo.setP34_mode(String.valueOf(textP2));
//                                        userInfo.setRf12_mode(String.valueOf(textRF1));
//                                        userInfo.setRf34_mode(String.valueOf(textRF2));
//                                        userInfo.setMode_f1(String.valueOf(f1));
//                                        userInfo.setMode_f2(String.valueOf(f2));
//                                        userInfo.setMode_f3(String.valueOf(f3));
//                                        userInfo.setMode_options(String.valueOf(0));
//                                        userInfo.setWork_date(date);
//                                        userInfo.setMode_duration(String.valueOf(time));
//                                        userInfo.setBody_part(String.valueOf(left_position));
//                                        userInfo.setPan("1");
//                                        if(userInfoDatabase.insertUserinfo(userInfo)>0){
//                                            userInfoAPIInsert(userInfo);
//                                        }
//                                    }
                                    if(permissionsDB.queryFlag("flag2")==1){
                                        timing_second.setValue(timing_second.getValue()-1);
                                        permissionsDB.updateFlag("timingSecond",timing_second.getValue());
                                        // 当秒为 0
                                        if(timing_second.getValue() <=0){
                                            // 分钟不为 0 时。
                                            if(timing_first.getValue() >0){
                                                timing_first.setValue(timing_first.getValue() -1 );
                                                permissionsDB.updateFlag("timing", timing_first.getValue());
                                                timing_second.setValue(60);
                                            }else{
                                                if(user_local ==2){
                                                    stop_time = first_time - (first_picker.getValue() * 60 + second_picker.getValue());
                                                    date();
                                                    time = String.format("%02d:%02d", stop_time / 60, stop_time % 60);
                                                    UserInfo userInfo = new UserInfo();
                                                    userInfo.setMachine(cpuid);
                                                    userInfo.setUserID(userID);
                                                    userInfo.setUserInfoID(userInfoDatabase.queryMaxUserInfo(String.valueOf(userID))+1);
                                                    userInfo.setWork_mode(String.valueOf(modePI));
                                                    userInfo.setP12_mode(String.valueOf(textP1));
                                                    userInfo.setP34_mode(String.valueOf(textP2));
                                                    userInfo.setRf12_mode(String.valueOf(textRF1));
                                                    userInfo.setRf34_mode(String.valueOf(textRF2));
                                                    userInfo.setMode_f1(String.valueOf(f1));
                                                    userInfo.setMode_f2(String.valueOf(f2));
                                                    userInfo.setMode_f3(String.valueOf(f3));
                                                    userInfo.setMode_options(String.valueOf(0));
                                                    userInfo.setWork_date(date);
                                                    userInfo.setMode_duration(String.valueOf(time));
                                                    userInfo.setBody_part(String.valueOf(left_position));
                                                    userInfo.setPan("1");
                                                    if(userInfoDatabase.insertUserinfo(userInfo)>0){
                                                        userInfoAPIInsert(userInfo);
                                                    }
                                                }
                                                first_picker.setEnabled(true);
                                                second_picker.setEnabled(true);
                                                work_flag = 0;
                                                isTimerFirst3 = false;
                                                isTimerLast3 = false;
                                                isTimerModel = false;

                                                isTimer2First3 = false;
                                                isTimer2Last3 = false;
                                                isTimer2Model = false;
                                                sendCPU(rf1_flag1);
                                                sendMessageToServer("bt_work3"+work_flag);
                                                bt_work2.setBackgroundResource(R.drawable.work3);
                                                // 计时结束
                                                stopTimer();
                                                showVerificationDialog();
                                            }
                                        }

                                    }

                                }
                            }
                        }
                    });
            }
        }, 0, 1000); // 每隔1秒执行一次
    }
    void stopWork(){
            work_flag = 0;
            stopTimer();
            if (pNumFlag1 == 1) {
                if (sk1 <= 2) {
                    pIdL(sk1 + 3);
                    bt_p1.setBackgroundResource(position_id);
                } else {
                    bt_p1.setBackgroundResource(R.drawable.s1);
                }
            }
            if (pNumFlag2 == 1) {
                if (sk2 <= 2) {
                    pIdR(sk2 + 3);
                    bt_p2.setBackgroundResource(position_id);
                } else {
                    bt_p2.setBackgroundResource(R.drawable.k1);
                }
            }
            bt_work2.setBackgroundResource(R.drawable.work3);
            sendCPU(rf1_flag1);
            List<View> disabledViews = Arrays.asList(sk_p1, sk_p2, sk_rf1, sk_rf2, sk_f1, sk_f2, sk_f3, first_picker,
                    second_picker, bt_jiaP1, bt_jiaP2, bt_jiaRF1, bt_jiaRF2, bt_jianP1, bt_jianP2, bt_m1, bt_m2,
                    bt_jianRF1, bt_jianRF2, bt_jianF1, bt_jianF2, bt_jianF3, bt_jiaF1, bt_jiaF2, bt_jiaF3,
                    bt_return);
            for (View view : disabledViews) {
                view.setEnabled(true);
            }
            animation1.stop();
            animation2.stop();
            animation3.stop();
            animation4.stop();
            ig_s1.setVisibility(View.GONE);
            ig_s2.setVisibility(View.GONE);
            ig_s3.setVisibility(View.GONE);
            ig_s4.setVisibility(View.GONE);
            if(user_local ==2) {
                stop_time = first_time - (first_picker.getValue() * 60 + second_picker.getValue());
                date();
                time = String.format("%02d:%02d", stop_time / 60, stop_time % 60);
                UserInfo userInfo = new UserInfo();
                userInfo.setMachine(cpuid);
                userInfo.setUserID(userID);
                userInfo.setUserInfoID(userInfoDatabase.queryMaxUserInfo(String.valueOf(userID))+1);
                userInfo.setWork_mode(String.valueOf(modePI));
                userInfo.setP12_mode(String.valueOf(textP1));
                userInfo.setP34_mode(String.valueOf(textP2));
                userInfo.setRf12_mode(String.valueOf(textRF1));
                userInfo.setRf34_mode(String.valueOf(textRF2));
                userInfo.setMode_f1(String.valueOf(f1));
                userInfo.setMode_f2(String.valueOf(f2));
                userInfo.setMode_f3(String.valueOf(f3));
                userInfo.setMode_options(String.valueOf(0));
                userInfo.setWork_date(date);
                userInfo.setMode_duration(String.valueOf(time));
                userInfo.setBody_part(String.valueOf(left_position));
                userInfo.setPan("1");
                if(userInfoDatabase.insertUserinfo(userInfo)>0){
                    userInfoAPIInsert(userInfo);
                }
            }
            if(permissionsDB.queryFlag("flag2")==1){
                timing_second.setValue(timing_second.getValue()-1);
                permissionsDB.updateFlag("timingSecond",timing_second.getValue());
                // 当秒为 0
                if(timing_second.getValue() <=0){
                    // 分钟不为 0 时。
                    if(timing_first.getValue() >0){
                        timing_first.setValue(timing_first.getValue() -1 );
                        permissionsDB.updateFlag("timing", timing_first.getValue());
                        timing_second.setValue(60);
                    }else{
                        if(user_local ==2){
                            stop_time = first_time - (first_picker.getValue() * 60 + second_picker.getValue());
                            date();
                            time = String.format("%02d:%02d", stop_time / 60, stop_time % 60);
                            UserInfo userInfo = new UserInfo();
                            userInfo.setMachine(cpuid);
                            userInfo.setUserID(userID);
                            userInfo.setUserInfoID(userInfoDatabase.queryMaxUserInfo(String.valueOf(userID))+1);
                            userInfo.setWork_mode(String.valueOf(modePI));
                            userInfo.setP12_mode(String.valueOf(textP1));
                            userInfo.setP34_mode(String.valueOf(textP2));
                            userInfo.setRf12_mode(String.valueOf(textRF1));
                            userInfo.setRf34_mode(String.valueOf(textRF2));
                            userInfo.setMode_f1(String.valueOf(f1));
                            userInfo.setMode_f2(String.valueOf(f2));
                            userInfo.setMode_f3(String.valueOf(f3));
                            userInfo.setMode_options(String.valueOf(0));
                            userInfo.setWork_date(date);
                            userInfo.setMode_duration(String.valueOf(time));
                            userInfo.setBody_part(String.valueOf(left_position));
                            userInfo.setPan("1");
                            if(userInfoDatabase.insertUserinfo(userInfo)>0){
                                userInfoAPIInsert(userInfo);
                            }
                        }
                        first_picker.setEnabled(true);
                        second_picker.setEnabled(true);
                        work_flag = 0;
                        sendCPU(rf1_flag1);
                        sendMessageToServer("bt_work3"+work_flag);
                        bt_work2.setBackgroundResource(R.drawable.work3);
                        // 计时结束
                        stopTimer();
                        showVerificationDialog();
                    }
                }

            }
    }
    private void showVerificationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = "Please reset shots number with mobile App";
        SpannableString spannableString = new SpannableString(message);
        ForegroundColorSpan redColorSpan = new ForegroundColorSpan(Color.RED);
        spannableString.setSpan(redColorSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setMessage(spannableString);

        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                sendMessageToServer("page_faceMenWoman");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bt_return.performClick();
                    }
                });
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                hideKeyboardAndStatusBar(EmsProfessionalModeActivity.this);
                sendMessageToServer("page_faceMenWoman");
                bt_return.performClick();
            }
        })  ;
        builder.show();
    }
    private void sendMessageToServer(String message) {
        if (webSocketService != null) {
            webSocketService.sendMessage(message);
        }
    }
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    @SuppressLint({"ClickableViewAccessibility", "DefaultLocale"})
    private void onclick(){
        first_picker.setFormatter(value -> String.format("%02d",value));//把数字显示都换成两位数，不足前面补0
        second_picker.setFormatter(value -> String.format("%02d",value));
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSending();
                sendMessageToServer("jump_part_m");
                Intent intent = new Intent(EmsProfessionalModeActivity.this,EmsPartActivity.class);
                startActivity(intent);
            }
        });
        bt_m1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m_mode!=1) {
                    m_mode = 1;
                    f_max = 50;
                    sendMessageToServer("m_click_m"+1);
                    sk_f1.setMax(f_max);
                    sk_f2.setMax(f_max);
                    sk_f3.setMax(f_max);
                    bt_m1.setBackgroundResource(R.drawable.bt_m1_down);
                    bt_m2.setBackgroundResource(R.drawable.bt_m2_up);
                }
            }
        });
        bt_m2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m_mode!=2) {
                    m_mode = 2;
                    f_max = 150 ;
                    sendMessageToServer("m_click_m"+2);
                    sk_f1.setMax(f_max);
                    sk_f2.setMax(f_max);
                    sk_f3.setMax(f_max);
                    bt_m1.setBackgroundResource(R.drawable.bt_m1_up);
                    bt_m2.setBackgroundResource(R.drawable.bt_m2_dwon);
                }
            }
        });
        bt_p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sk1 <= 2){
                    if(work_flag == 0){
                        if(pNumFlag1 == 0){
                            pNumFlag1 = 1;
                            pIdL(sk1 + 3);
                            bt_p1.setBackgroundResource(position_id);
                        }
                        else {
                            pNumFlag1 = 0;
                            pIdL(sk1);
                            bt_p1.setBackgroundResource(position_id);
                        }
                        sendM8();

                    }
                }
            }
        });
        bt_p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sk2 <= 2){
                    if(work_flag == 0){
                        if(pNumFlag2 == 0 ){
                            pNumFlag2 = 1;
                            pIdR(sk2 + 3);
                            bt_p2.setBackgroundResource(position_id);
                        }
                        else {
                            pNumFlag2 = 0;
                            pIdR(sk2);
                            bt_p2.setBackgroundResource(position_id);
                        }
                        sendM8();

                    }
                }
            }
        });

        bt_work2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((sk1 <= 2 && pNumFlag1 == 1) || (sk2 <= 2 && pNumFlag2 == 1))&&(first_picker.getValue() * 60 + second_picker.getValue())>0) {
                    if(work_flag == 0) {
                        first_time = first_picker.getValue() * 60 + second_picker.getValue();
                            if (sk1 <= 2) {
                                pIdL(sk1 + 6);
                                bt_p1.setBackgroundResource(position_id);
                            } else {
                                bt_p1.setBackgroundResource(R.drawable.s1);
                            }
                            if (sk2 <= 2) {
                                pIdR(sk2 + 6);
                                bt_p2.setBackgroundResource(position_id);
                            } else {
                                bt_p2.setBackgroundResource(R.drawable.k1);
                            }
                            if (pNumFlag1 == 0) {
                                pIdL(sk1);
                                bt_p1.setBackgroundResource(position_id);

                            }
                            if (pNumFlag2 == 0) {
                                pIdR(sk2);
                                bt_p2.setBackgroundResource(position_id);
                            }
                            work_flag = 1 ;
                            rf1_flag1 = 1 ;
                            rf2_flag2 = 1 ;
                            startTimer();
                        bt_work2.setBackgroundResource(R.drawable.work4);
                            List<View> disabledViews = Arrays.asList(sk_p1, sk_p2, sk_rf1, sk_rf2, sk_f1, sk_f2, sk_f3, first_picker,
                                    second_picker, bt_jiaP1, bt_jiaP2, bt_jiaRF1, bt_jiaRF2, bt_jianP1, bt_jianP2, bt_m1, bt_m2,
                                    bt_jianRF1, bt_jianRF2, bt_jianF1, bt_jianF2, bt_jianF3, bt_jiaF1, bt_jiaF2, bt_jiaF3,
                                    bt_return);
                            for (View view : disabledViews) {
                                view.setEnabled(false);
                            }
                            if (sk1 <= 2 && sk1 > 0) {
                                ig_s1.setVisibility(View.GONE);
                                ig_s2.setVisibility(View.VISIBLE);
                                animation1.stop();
                                animation2.start();
                            } else if (sk1 == 0) {
                                ig_s1.setVisibility(View.VISIBLE);
                                ig_s2.setVisibility(View.GONE);
                                animation1.start();
                                animation2.stop();
                            }
                            if (sk2 <= 2 && sk2 > 0) {
                                ig_s3.setVisibility(View.GONE);
                                ig_s4.setVisibility(View.VISIBLE);
                                animation3.stop();
                                animation4.start();
                            } else if (sk2 == 0) {
                                ig_s3.setVisibility(View.VISIBLE);
                                ig_s4.setVisibility(View.GONE);
                                animation3.start();
                                animation4.stop();
                            }
                            if (pNumFlag1 == 0) {
                                ig_s1.setVisibility(View.GONE);
                                ig_s2.setVisibility(View.GONE);
                            }
                            if (pNumFlag2 == 0) {
                                ig_s3.setVisibility(View.GONE);
                                ig_s4.setVisibility(View.GONE);
                            }
                        } else {
                        rf1_flag1 = 1 ;
                        rf2_flag2 = 1 ;
                        work_flag = 0;
                        isTimerFirst3 = false;
                        isTimerLast3 = false;
                        isTimerModel = false;

                        isTimer2First3 = false;
                        isTimer2Last3 = false;
                        isTimer2Model = false;
                        stopTimer();
                        sendCPU(rf1_flag1);
                        if (pNumFlag1 == 1) {
                                if (sk1 <= 2) {
                                    pIdL(sk1 + 3);
                                    bt_p1.setBackgroundResource(position_id);
                                } else {
                                    bt_p1.setBackgroundResource(R.drawable.s1);
                                }
                            }
                            if (pNumFlag2 == 1) {
                                if (sk2 <= 2) {
                                    pIdR(sk2 + 3);
                                    bt_p2.setBackgroundResource(position_id);
                                } else {
                                    bt_p2.setBackgroundResource(R.drawable.k1);
                                }
                            }
                            bt_work2.setBackgroundResource(R.drawable.work3);
                            LayoutInflater inflater = getLayoutInflater();
                            stop_time = first_time - (first_picker.getValue() * 60 + second_picker.getValue());
                            Log.e("stop_time", String.valueOf(stop_time));
                            View diaglogView = inflater.inflate(R.layout.custom_dialog_stop, null);
                            final AlertDialog save_diaglog = new AlertDialog.Builder(EmsProfessionalModeActivity.this)
                                    .setView(diaglogView)
                                    .create();
                            Window window = save_diaglog.getWindow();
                            if (window != null) {
                                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            }
                            Button bt_save = diaglogView.findViewById(R.id.bt_save);
                            Button bt_back = diaglogView.findViewById(R.id.bt_back);
                            bt_save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    date();
                                    @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", stop_time / 60, stop_time % 60);
                                    Log.e("stopTime", String.valueOf(time));
                                    UserInfo userInfo = new UserInfo();
                                    userInfo.setMachine(cpuid);
                                    userInfo.setUserID(userID);
                                    userInfo.setUserInfoID(userInfoDatabase.queryMaxUserInfo(String.valueOf(userID)) + 1);
                                    userInfo.setWork_mode(String.valueOf(modePI));
                                    userInfo.setP12_mode(String.valueOf(textP1));
                                    userInfo.setP34_mode(String.valueOf(textP2));
                                    userInfo.setRf12_mode(String.valueOf(textRF1));
                                    userInfo.setRf34_mode(String.valueOf(textRF2));
                                    userInfo.setMode_f1(String.valueOf(f1));
                                    userInfo.setMode_f2(String.valueOf(f2));
                                    userInfo.setMode_f3(String.valueOf(f3));
                                    userInfo.setMode_options(String.valueOf(0));
                                    userInfo.setWork_date(date);
                                    userInfo.setMode_duration(time);
                                    userInfo.setBody_part(String.valueOf(left_position));
                                    userInfo.setPan("1");
                                    userInfo.setL_forehead_current("0");
                                    userInfo.setR_forehead_current("0");
                                    if (userInfoDatabase.insertUserinfo(userInfo) > 0) {
                                        userInfoAPIInsert(userInfo);
                                    }
                                    save_diaglog.cancel();
                                }
                            });
                            save_diaglog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    hideKeyboardAndStatusBar(EmsProfessionalModeActivity.this);
                                }
                            });
                            bt_back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    save_diaglog.cancel();
                                }
                            });

                            if (user_local == 2) {
                                save_diaglog.show();
                            }
                            List<View> enabledViews = Arrays.asList(sk_p1, sk_p2, sk_rf1, sk_rf2, sk_f1, sk_f2, sk_f3,
                                    first_picker, second_picker, bt_jiaP1, bt_jiaP2, bt_jiaRF1, bt_jiaRF2, bt_m1, bt_m2,
                                    bt_jianP1, bt_jianP2, bt_jianRF1, bt_jianRF2, bt_jianF1, bt_jianF2, bt_jianF3,
                                    bt_jiaF1, bt_jiaF2, bt_jiaF3, bt_return);
                            for (View view : enabledViews) {
                                view.setEnabled(true);
                            }
                            animation1.stop();
                            animation2.stop();
                            animation3.stop();
                            animation4.stop();
                            ig_s1.setVisibility(View.GONE);
                            ig_s2.setVisibility(View.GONE);
                            ig_s3.setVisibility(View.GONE);
                            ig_s4.setVisibility(View.GONE);
                        }
                    if(!phoneClickFlag1){
                        sendM8();
                    }
                    phoneClickFlag1 = false;
                    }
            }
        });
        sk_p1.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                textP1 = seekParams.progress;

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                if(isUserTouching){
                    sendCPU(rf1_flag1);
                    sendM8();
                }
                isUserTouching = false;

            }
        });
        sk_p2.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                textP2 = seekParams.progress;

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                if(isUserTouching){
                    sendCPU(rf1_flag1);
                    sendM8();
                }
                isUserTouching = false;

            }
        });
        sk_rf1.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                textRF1 = seekParams.progress;

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                if(isUserTouching){
                    sendCPU(rf1_flag1);
                    sendM8();
                }
                isUserTouching = false;

            }
        });
        sk_rf2.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                textRF2 = seekParams.progress;

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                if(isUserTouching){
                    sendCPU(rf1_flag1);
                    sendM8();
                }
                isUserTouching = false;

            }
        });
        sk_f1.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                f1 = seekParams.progress;

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                if(isUserTouching){
                    sendCPU(rf1_flag1);
                    sendM8();
                }
                isUserTouching = false;

            }
        });
        sk_f2.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                f2 = seekParams.progress;

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                if(isUserTouching){
                    sendCPU(rf1_flag1);
                    sendM8();
                }
                isUserTouching = false;

            }
        });
        sk_f3.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                f3 = seekParams.progress;

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                if(isUserTouching){
                    sendCPU(rf1_flag1);
                    sendM8();
                }
                isUserTouching = false;

            }
        });
        bt_jiaP1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jiaP1.setBackgroundResource(R.drawable.bt_add_down);
                    p12 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (p12){
                                if (System.currentTimeMillis() > 100) {
                                    textP1++;
                                    if(textP1 > 100){
                                        textP1 = 100;
                                    }
                                    Message msg = handler1.obtainMessage();
                                    msg.arg1 = textP1;
                                    handler1.sendMessage(msg);
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
                    bt_jiaP1.setBackgroundResource(R.drawable.bt_add_up);
                    p12 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    p12 = false;
                }
                return true;
            }
        });
        bt_jiaP2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jiaP2.setBackgroundResource(R.drawable.bt_add_down);
                    p12 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (p12){
                                if (System.currentTimeMillis() > 100) {
                                    textP2++;
                                    if(textP2 > 100){
                                        textP2 = 100;
                                    }
                                    Message msg = handler6.obtainMessage();
                                    msg.arg1 = textP2;
                                    handler6.sendMessage(msg);
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
                    bt_jiaP2.setBackgroundResource(R.drawable.bt_add_up);
                    p12 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    p12 = false;
                }
                return true;
            }
        });
        bt_jiaRF1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jiaRF1.setBackgroundResource(R.drawable.bt_add_down);
                    rf12 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (rf12){
                                if (System.currentTimeMillis() > 100) {
                                    textRF1++;
                                    if(textRF1 > 100){
                                        textRF1 = 100;
                                    }
                                    Message msg = handler2.obtainMessage();
                                    msg.arg1 = textRF1;
                                    handler2.sendMessage(msg);
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
                    bt_jiaRF1.setBackgroundResource(R.drawable.bt_add_up);
                    rf12 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    rf12 = false;
                }
                return true;
            }
        });
        bt_jiaRF2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jiaRF2.setBackgroundResource(R.drawable.bt_add_down);
                    rf12 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (rf12){
                                if (System.currentTimeMillis() > 100) {
                                    textRF2++;
                                    if(textRF2 > 100){
                                        textRF2 = 100;
                                    }
                                    Message msg = handler7.obtainMessage();
                                    msg.arg1 = textRF2;
                                    handler7.sendMessage(msg);
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
                    bt_jiaRF2.setBackgroundResource(R.drawable.bt_add_up);
                    rf12 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    rf12 = false;
                }
                return true;
            }
        });
        bt_jianP1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jianP1.setBackgroundResource(R.drawable.bt_sub_down);
                    p12 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (p12){
                                if (System.currentTimeMillis() > 100) {
                                    textP1--;
                                    if(textP1 < 1){
                                        textP1 = 1;
                                    }
                                    Message msg = handler1.obtainMessage();
                                    msg.arg1 = textP1;
                                    handler1.sendMessage(msg);
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
                    bt_jianP1.setBackgroundResource(R.drawable.bt_sub_up);
                    p12 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    p12 = false;
                }
                return true;
            }
        });
        bt_jianP2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jianP2.setBackgroundResource(R.drawable.bt_sub_down);
                    p12 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (p12){
                                if (System.currentTimeMillis() > 100) {
                                    textP2--;
                                    if(textP2 < 1){
                                        textP2 = 1;
                                    }
                                    Message msg = handler6.obtainMessage();
                                    msg.arg1 = textP2;
                                    handler6.sendMessage(msg);
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
                    bt_jianP2.setBackgroundResource(R.drawable.bt_sub_up);
                    p12 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    p12 = false;
                }
                return true;
            }
        });
        bt_jianRF1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jianRF1.setBackgroundResource(R.drawable.bt_sub_down);
                    rf12 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (rf12){
                                if (System.currentTimeMillis() > 100) {
                                    textRF1--;
                                    if(textRF1 < 1){
                                        textRF1 = 0;
                                    }
                                    Message msg = handler2.obtainMessage();
                                    msg.arg1 = textRF1;
                                    handler2.sendMessage(msg);
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
                    bt_jianRF1.setBackgroundResource(R.drawable.bt_sub_up);
                    rf12 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    rf12 = false;
                }
                return true;
            }
        });
        bt_jianRF2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jianRF2.setBackgroundResource(R.drawable.bt_sub_down);
                    rf12 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (rf12){
                                if (System.currentTimeMillis() > 100) {
                                    textRF2--;
                                    if(textRF2 < 1){
                                        textRF2 = 0;
                                    }
                                    Message msg = handler7.obtainMessage();
                                    msg.arg1 = textRF2;
                                    handler7.sendMessage(msg);
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
                    bt_jianRF2.setBackgroundResource(R.drawable.bt_sub_up);
                    rf12 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    rf12 = false;
                }
                return true;
            }
        });
        bt_jiaF1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jiaF1.setBackgroundResource(R.drawable.bt_add_down);
                    f12 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (f12){
                                if (System.currentTimeMillis() > 100) {
                                    f1++;
                                    if(f1 > f_max){
                                        f1 = f_max;
                                    }
                                    Message msg = handler3.obtainMessage();
                                    msg.arg1 = f1;
                                    handler3.sendMessage(msg);
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
                    bt_jiaF1.setBackgroundResource(R.drawable.bt_add_up);
                    f12 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    f12 = false;
                }
                return true;
            }
        });
        bt_jiaF2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jiaF2.setBackgroundResource(R.drawable.bt_add_down);
                    f22 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (f22){
                                if (System.currentTimeMillis() > 100) {
                                    f2++;
                                    if(f2 > f_max){
                                        f2 = f_max;
                                    }
                                    Message msg = handler4.obtainMessage();
                                    msg.arg1 = f2;
                                    handler4.sendMessage(msg);
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
                    bt_jiaF2.setBackgroundResource(R.drawable.bt_add_up);
                    f22 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    f22 = false;
                }
                return true;
            }
        });
        bt_jiaF3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jiaF3.setBackgroundResource(R.drawable.bt_add_down);
                    f32 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (f32){
                                if (System.currentTimeMillis() > 100) {
                                    f3++;
                                    if(f3 > f_max){
                                        f3 = f_max;
                                    }
                                    Message msg = handler5.obtainMessage();
                                    msg.arg1 = f3;
                                    handler5.sendMessage(msg);
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
                    bt_jiaF3.setBackgroundResource(R.drawable.bt_add_up);
                    f32 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    f32 = false;
                }
                return true;
            }
        });
        bt_jianF1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jianF1.setBackgroundResource(R.drawable.bt_sub_down);
                    f12 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (f12){
                                if (System.currentTimeMillis() > 100) {
                                    f1--;
                                    if(f1 < 1){
                                        f1 = 1;
                                    }
                                    Message msg = handler3.obtainMessage();
                                    msg.arg1 = f1;
                                    handler3.sendMessage(msg);
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
                    bt_jianF1.setBackgroundResource(R.drawable.bt_sub_up);
                    f12 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    f12 = false;
                }
                return true;
            }
        });
        bt_jianF2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jianF2.setBackgroundResource(R.drawable.bt_sub_down);
                    f22 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (f22){
                                if (System.currentTimeMillis() > 100) {
                                    f2--;
                                    if(f2 < 1){
                                        f2 = 1;
                                    }
                                    Message msg = handler4.obtainMessage();
                                    msg.arg1 = f2;
                                    handler4.sendMessage(msg);
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
                    bt_jianF2.setBackgroundResource(R.drawable.bt_sub_up);
                    f22 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    f22 = false;
                }
                return true;
            }
        });
        bt_jianF3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jianF3.setBackgroundResource(R.drawable.bt_sub_down);
                    f32 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (f32){
                                if (System.currentTimeMillis() > 100) {
                                    f3--;
                                    if(f3 < 1){
                                        f3 = 1;
                                    }
                                    Message msg = handler5.obtainMessage();
                                    msg.arg1 = f3;
                                    handler5.sendMessage(msg);
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
                    bt_jianF3.setBackgroundResource(R.drawable.bt_sub_up);
                    f32 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    f32 = false;
                }
                return true;
            }
        });
    }
    private void sendCPU(int rf1) {
        initializationData.delete(0, initializationData.length());
        initializationData.append("AA02")
                .append(DataTypeConversion.intToHex2(textP1))
                .append(DataTypeConversion.intToHex2(textP2))
                .append(DataTypeConversion.intToHex2(f1))
                .append(DataTypeConversion.intToHex2(f2))
                .append(DataTypeConversion.intToHex2(f3))
                .append(DataTypeConversion.intToHex2(textRF1))
                .append(DataTypeConversion.intToHex2(textRF2));

        if (pNumFlag1 == 1 && pNumFlag2 != 1) {
            initializationData.append(DataTypeConversion.intToHex2(work_flag))
                    .append(DataTypeConversion.intToHex2(0));
        }
        else if (pNumFlag1 != 1 && pNumFlag2 == 1) {
            initializationData.append(DataTypeConversion.intToHex2(0))
                    .append(DataTypeConversion.intToHex2(work_flag));
        }
        else if (pNumFlag1 == 1 && pNumFlag2 == 1) {
            initializationData.append(DataTypeConversion.intToHex2(work_flag))
                    .append(DataTypeConversion.intToHex2(work_flag));
        }
        initializationData.append(DataTypeConversion.intToHex2(work_flag==0?0:(pNumFlag2 == 1?rf1:0)));   // 射频反了 1 - 2  ， 2 -1
        initializationData.append(DataTypeConversion.intToHex2(work_flag==0?0:(pNumFlag1 == 1?rf1:0)));   // 射频反了 1 - 2  ， 2 -1
        initializationData.append("00");

        String crc = calcCrc16Modbus(initializationData.toString());
        String send = initializationData.append(crc).append("cc").toString();
        serialPortUtil.serial_send(send);
    }

    private void sendCPU1() {
        initializationData.delete(0, initializationData.length());
        initializationData.append("AA06")
                .append(DataTypeConversion.intToHex2(sound))
                .append("0000000000000000000000");
        Log.e("", "sendCPU1: " + DataTypeConversion.intToHex2(sound) );

        Log.e("", "sendCPU1: " + initializationData.toString() );
        String crc = calcCrc16Modbus(initializationData.toString());
//        String crc = calcCrc16Modbus(initializationData.toString()).replace("0x", "");
        Log.e("", "sendCPU1: " + crc );
        String send = initializationData.append(crc).append("cc").toString();
        serialPortUtil.serial_send(send);
    }

    private void handler(){
        handler1 = new Handler(msg -> {
            sk_p1.setProgress(textP1);
            sendCPU(rf1_flag1);
            sendM8();
            return true;
        });
        handler2 = new Handler(msg -> {
            sk_rf1.setProgress(textRF1);
            sendCPU(rf1_flag1);
            sendM8();
            return true;
        });
        handler3 = new Handler(msg -> {
            sk_f1.setProgress(f1);
            sendCPU(rf1_flag1);
            sendM8();
            return true;
        });
        handler4 = new Handler(msg -> {
            sk_f2.setProgress(f2);
            sendCPU(rf1_flag1);
            sendM8();
            return true;
        });
        handler5 = new Handler(msg -> {
            sk_f3.setProgress(f3);
            sendCPU(rf1_flag1);
            sendM8();
            return true;
        });
        handler6 = new Handler(msg -> {
            sk_p2.setProgress(textP2);
            sendCPU(rf1_flag1);
            sendM8();
            return true;
        });
        handler7 = new Handler(msg -> {
            sk_rf2.setProgress(textRF2);
            sendCPU(rf1_flag1);
            sendM8();
            return true;
        });
    }
    private int pIdL(int a){
        try {
            field = drawable.getField("s" + a);
            position_id = field.getInt(field.getName());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return a;
    }
    private int pIdR(int a){
        try {
            field = drawable.getField("k" + a);
            position_id = field.getInt(field.getName());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return a;
    }
    private void date(){ //获取当前系统日期
        String year;
        String month;
        String day;
        Calendar calendar = Calendar.getInstance();
        year = String.valueOf(Integer.valueOf(calendar.get(Calendar.YEAR)));
        month = String.valueOf(Integer.valueOf(calendar.get(Calendar.MONTH)+1));
        if(month.length() < 2){
            month = "0" + month;
        }
        day = String.valueOf(Integer.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        if(day.length() < 2){
            day = "0" + day;
        }
        date =  day +'/' +  month + '/' + year;
        //systemTimeNum = day + month + year;
        Log.e("", "date: " + day + month + year );

    }
    //向服务器保存用户的工作信息。
    void userInfoAPIInsert(UserInfo userInfo){
        UserInfoAPI userInfoAPI = UserInfoService.getUserinfoAPI();
        Call<Long> longCall = userInfoAPI.addUserInfo(userInfo);
        longCall.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                //保存到服务器中后服务器返回的值
                long userInfoInsertFlag = response.body();
                System.out.println("API   : userInfo insert finish : "+userInfoInsertFlag );
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                //如果失败则记录添加痕迹。将添加痕迹记录到uploadInfoDB 的表中。 只记录用户ID，和userinfo 的ID .等待下次连接到网络时遍历这个表。
                uploadInfoDB.insertUserInfoInsert(String.valueOf(userInfo.getUserID()),String.valueOf(userInfo.getUserInfoID()));
            }
        });
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
                ShowWifiErrorDialog.showWifiError(EmsProfessionalModeActivity.this,getWindowManager());
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
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            System.out.println("EmsProfessionMode " + message);
            if(message.equals("page_part_p")){
                if(work_flag==0) {
                    serialPortUtil.serial_send("AA09000000000000000000000000CC9C63");
                    stopSending();
                    sendMessageToServer("jump_part_m");
                    Intent intent1 = new Intent(EmsProfessionalModeActivity.this, EmsPartActivity.class);
                    startActivity(intent1);
                }
            }
            else if(message.startsWith("AAP8")){ // 参数调整
                textP1 = Integer.valueOf(message.substring(4,6),16);
                textP2 = Integer.valueOf(message.substring(6,8),16);
                textRF1 = Integer.valueOf(message.substring(8,10),16);
                textRF2 = Integer.valueOf(message.substring(10,12),16);
                f1 = Integer.valueOf(message.substring(12,14),16);
                f2 = Integer.valueOf(message.substring(14,16),16);
                f3 = Integer.valueOf(message.substring(16,18),16);
                int Flag1 = Integer.valueOf(message.substring(18,20),16);
                int Flag2 = Integer.valueOf(message.substring(20,22),16);
                int workFlag1 = Integer.valueOf(message.substring(22,24),16);
                int workFlag2 = Integer.valueOf(message.substring(24,26),16);
                int firstTimer1 = Integer.valueOf(message.substring(26,28),16);
                int secondTimer1 = Integer.valueOf(message.substring(28,30),16);
                int firstTimer2 = Integer.valueOf(message.substring(30,32),16);
                int secondTimer2 = Integer.valueOf(message.substring(32,34),16);
                // 点击 冲突标志位 ：
                phoneClickFlag1 = true;
                phoneClickFlag2 = true;
                System.out.println("secondTimer1 " + secondTimer2);
                // 使能
                if(Flag1 != pNumFlag1||Flag2 != pNumFlag2){
                    if(sk1 <= 2){
                        if(work_flag == 0){
                            if(pNumFlag1 == 0){
                                pNumFlag1 = 1;
                                pIdL(sk1 + 3);
                                bt_p1.setBackgroundResource(position_id);
                                pIdR(sk2 + 3);
                                bt_p2.setBackgroundResource(position_id);
                            }
                            else {
                                pNumFlag1 = 0;
                                pIdL(sk1);
                                bt_p1.setBackgroundResource(position_id);
                                pIdR(sk2);
                                bt_p2.setBackgroundResource(position_id);
                            }

                        }
                    }
                }
                // 设置时间，只有再暂停时才会调整
                if(work_flag ==0){
                    first_picker.setValue(firstTimer1);
                    second_picker.setValue(secondTimer1);
                }

                // 工作
                if(workFlag1 != work_flag){
                    phoneClickFlag1 = true;
                    bt_work2.performClick();
                }

                sk_p1.setProgress(textP1);
                sk_p2.setProgress(textP2);
                sk_rf1.setProgress(textRF1);
                sk_rf2.setProgress(textRF2);
                sk_f1.setProgress(f1);
                sk_f2.setProgress(f2);
                sk_f3.setProgress(f3);
            }
            else if ("getInitData".equals(message)){ // 手机端进入界面后初始化 数据 ：
                sendM8();
            }
            else if (message.startsWith("p_click_m")){
                m_mode = Integer.valueOf(message.substring("p_click_m".length()));
                if(m_mode ==1){
                    f_max = 50;
                    sk_f1.setMax(f_max);
                    sk_f2.setMax(f_max);
                    sk_f3.setMax(f_max);
                    bt_m1.setBackgroundResource(R.drawable.bt_m1_down);
                    bt_m2.setBackgroundResource(R.drawable.bt_m2_up);
                }
                else {
                    m_mode = 2;
                    f_max = 150 ;
                    sk_f1.setMax(f_max);
                    sk_f2.setMax(f_max);
                    sk_f3.setMax(f_max);
                    bt_m1.setBackgroundResource(R.drawable.bt_m1_up);
                    bt_m2.setBackgroundResource(R.drawable.bt_m2_dwon);
                }
            }
            else if ("Which_page".equals(message)){
                sendMessageToServer("page_work_pm");
            }
            else if ("Forced_shutdown".equals(message)){
                work_flag = 0;
                sendCPU(rf1_flag1); // 发送数据
                bt_return.performClick();
            }
        }
    };
    private void sendTimerM7(){
        // timer1 :
        // 手机端  开始工作时才会发送  发送工作剩余时间
        sendDataM7.delete(0,sendDataM7.length());
        sendDataM7.append("AAM7")
                .append(DataTypeConversion.intToHex2(first_picker.getValue()))
                .append(DataTypeConversion.intToHex2(second_picker.getValue()));
        sendMessageToServer(String.valueOf(sendDataM7));
    }

    private void sendM8(){
        //手机端 参数调整
        sendDataM8.delete(0,sendDataM8.length());
        sendDataM8.append("AAM8")
                .append(DataTypeConversion.intToHex2(textP1))//wwwww
                .append(DataTypeConversion.intToHex2(textP2))
                .append(DataTypeConversion.intToHex2(textRF1))
                .append(DataTypeConversion.intToHex2(textRF2))
                .append(DataTypeConversion.intToHex2(f1))
                .append(DataTypeConversion.intToHex2(f2))
                .append(DataTypeConversion.intToHex2(f3))
                .append(DataTypeConversion.intToHex2(pNumFlag1))
                .append(DataTypeConversion.intToHex2(pNumFlag2))
                .append(DataTypeConversion.intToHex2(work_flag))
                .append(DataTypeConversion.intToHex2(0))
                .append(DataTypeConversion.intToHex2(first_picker.getValue()))
                .append(DataTypeConversion.intToHex2(second_picker.getValue()))
                .append(DataTypeConversion.intToHex2(0))
                .append(DataTypeConversion.intToHex2(0))
                .append(DataTypeConversion.intToHex2(sk1))
                .append(DataTypeConversion.intToHex2(sk2))
                .append(DataTypeConversion.intToHex2(0))
                .append(DataTypeConversion.intToHex2(0))
                .append("00CC");
        System.out.println("发送了 AAM8"+ sendDataM8);
        sendMessageToServer(String.valueOf(sendDataM8));
    }
    private void sendHandleM9(){
        // 手机端 手柄
        sendDataM9.delete(0,sendDataM9.length());
        sendDataM9.append("AAM9")
                .append(DataTypeConversion.intToHex2(sk1))
                .append(DataTypeConversion.intToHex2(sk2))
                .append(DataTypeConversion.intToHex2(0))
                .append(DataTypeConversion.intToHex2(0))
                .append(DataTypeConversion.intToHex2(error))
                .append("00CC");
        System.out.println("发送了 AAM9 " + sendDataM9);
        sendMessageToServer(String.valueOf(sendDataM9));
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
    private class  FirstData{
        int first_sk1 ;
        int first_sk2 ;
        int first_sk3 ;
        int first_sk4 ;
        int first_error ;
        public int getFirst_sk1() {
            return first_sk1;
        }

        public void setFirst_sk1(int first_sk1) {
            this.first_sk1 = first_sk1;
        }

        public int getFirst_sk2() {
            return first_sk2;
        }

        public void setFirst_sk2(int first_sk2) {
            this.first_sk2 = first_sk2;
        }

        public int getFirst_sk3() {
            return first_sk3;
        }

        public void setFirst_sk3(int first_sk3) {
            this.first_sk3 = first_sk3;
        }

        public int getFirst_sk4() {
            return first_sk4;
        }

        public void setFirst_sk4(int first_sk4) {
            this.first_sk4 = first_sk4;
        }

        public int getFirst_error() {
            return first_error;
        }

        public void setFirst_error(int first_error) {
            this.first_error = first_error;
        }

        public FirstData(int first_sk1, int first_sk2, int first_sk3, int first_sk4,int first_error) {
            this.first_sk1 = first_sk1;
            this.first_sk2 = first_sk2;
            this.first_sk3 = first_sk3;
            this.first_sk4 = first_sk4;
            this.first_error = first_error;
        }
        public FirstData(){

        }
        public boolean equals(@Nullable FirstData firstData) {
            return this.first_sk1 == firstData.first_sk1 &&
                    this.first_sk2 == firstData.first_sk2 &&
                    this.first_sk3 == firstData.first_sk3 &&
                    this.first_sk4 == firstData.first_sk4 &&
                    this.first_error == firstData.first_error;
        }
    }
}