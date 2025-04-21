package com.example.freezecl4_zcx;

import static com.example.freezecl4_zcx.CheckActivity.user_local;
import static com.example.freezecl4_zcx.CyroModeActivity.cyro_mode;
import static com.example.freezecl4_zcx.InformationServiceActivity.userID;
import static com.example.freezecl4_zcx.StartActivity.cl2_p1;
import static com.example.freezecl4_zcx.StartActivity.cl2_p2;
import static com.example.freezecl4_zcx.StartActivity.cl2_temp1;
import static com.example.freezecl4_zcx.StartActivity.cl2_temp2;
import static com.example.freezecl4_zcx.StartActivity.cpuid;
import static com.example.freezecl4_zcx.StartActivity.cryo_flow1;
import static com.example.freezecl4_zcx.StartActivity.cryo_flow2;
import static com.example.freezecl4_zcx.StartActivity.cryo_p1;
import static com.example.freezecl4_zcx.StartActivity.cryo_p2;
import static com.example.freezecl4_zcx.StartActivity.cryo_temp1;
import static com.example.freezecl4_zcx.StartActivity.cryo_temp2;
import static com.example.freezecl4_zcx.StartActivity.version;
import static com.example.freezecl4_zcx.StartActivity.webSocketService;
import static tools.CRC16Modbus.calcCrc16Modbus;
import static tools.UIUtils.hideKeyboardAndStatusBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shawnlin.numberpicker.NumberPicker;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.Calendar;
import java.util.Objects;

import db.UploadInfoDB;
import db.UserInfoDatabase;
import entity.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.UserInfoService;
import service.api.UserInfoAPI;
import tools.AudioPlayer;
import tools.DataTypeConversion;
import tools.PermissionsDB;
import tools.SerialPortUtil;
import tools.SetDataBase;
import tools.ShowWifiErrorDialog;
import tools.entity.RfInitDatabase;
import tools.entity.RfInitDatabaseSave;

public class CyroWorkActivity extends AppCompatActivity {
    private boolean isStartCountdown1 = false;
    private boolean isStartCountdown2 = false;
    private boolean isStartPermission = false;
    private Button bt_return;
    private Button bt_work1;
    private Button bt_work2;
    private TextView iv_mode;
    private ImageView iv_hand1;
    private ImageView iv_hand2;
    private TextView tv_hand1;
    private TextView tv_hand2;
    private IndicatorSeekBar sk_p1;
    private IndicatorSeekBar sk_p2;
    private IndicatorSeekBar sk_p3;
    private IndicatorSeekBar sk_p4;
    private IndicatorSeekBar sk_p5;
    private IndicatorSeekBar sk_p6;
    private Button bt_jianP1;
    private Button bt_jianP2;
    private Button bt_jianP3;
    private Button bt_jianP4;
    private Button bt_jianP5;
    private Button bt_jianP6;
    private Button bt_jiaP1;
    private Button bt_jiaP2;
    private Button bt_jiaP3;
    private Button bt_jiaP4;
    private Button bt_jiaP5;
    private Button bt_jiaP6;
    private Button bt_set;

    private TextView tv_hand1_temp;
    private TextView tv_hand1_flow;
    private TextView tv_hand1_time;
    private TextView tv_hand1_yali;
    private TextView tv_hand2_temp;
    private TextView tv_hand2_flow;
    private TextView tv_hand2_time;
    private TextView tv_hand2_yali;
    private final SerialPortUtil serialPortUtil= new SerialPortUtil();
    private int yali1 = 10;
    private int yali2 = 10;
    private int error = 0;
    private int error1 = 0;
    private final StringBuilder initializationData = new StringBuilder();
    private int setTemp1 = -1;
    private int setTemp2 = -1;
    private int work_flag1 = 0;
    private int work_flag2 = 0;
    private Handler handler1;
    private Handler handler2;
    private Handler handler3;
    private Handler handler4;
    private Handler handler5;
    private Handler handler6;
    private boolean flag1;
    private boolean flag2;
    private boolean flag3;
    private boolean flag4;
    private boolean flag5;
    private boolean flag6;
    private int time1 = 30;
    private int time2 = 30;
    private Handler handler = new Handler();
    private Runnable sendTask;
    private boolean isSending = false;
    private int sound = 1;
    private int sound1 = 0;
    private int sound2 = 0;
    private int sound3 = 0;
    private int sound4 = 0;
    private int sound5 = 0;
    private int first_time1;
    private int first_time2;
    private int time2_ms;
    private int time1_ms;
    private int stop_time1;
    private int stop_time2;
    private Handler handler_time1;
    private Handler handler_time2;
    private Handler  handler_permission;  // 代理模式总计时：
    private Runnable runnable1;
    private Runnable runnable2;
    private Runnable runPermission;
    private boolean isRunning1 = true; // 记录倒计时是否正在运行
    private boolean isRunning2 = true; // 记录倒计时是否正在运行
    private boolean isRunningPermission  = true; //
    private int flow_error = 10;
    private int yali_error = 103;
    private Button bt_error;
    private Handler blinkHandler = new Handler();
    private Runnable blinkRunnable;
    private boolean isBlinking = false;
    private String hand1_text;
    private String hand2_text;
    private int no_hand1 = 0;
    private int no_hand2 = 0;
    private String date = "?";
    UserInfoDatabase userInfoDatabase;
    private UploadInfoDB uploadInfoDB;
    public static int hand_num = 1;//进入设置界面上面的手柄是1，下面的是2，以便区分保存
    private int identicalCounter = 0;
    private int identicalCounter1 = 0;
    private int identicalCounter2 = 0;
    private int identicalCounter3 = 0;
    private int identicalCounter4 = 0;
    private int identicalCounter5 = 0;
    private int identicalCounter6 = 0;
    private int identicalCounter7= 0;
    private NumberPicker timing_first;
    private NumberPicker timing_second;
    private LinearLayout lin_timing;
    private PermissionsDB permissionsDB;
    private int real_error = 0;
    private int isClickWork1 = 0 ; // 判断所有报警值 是否可点击 work1
    private int isClickWork2 = 0 ; // 判断所有报警值 是否可点击 work2
    private AudioPlayer audioPlayer;
    private int handle_null_num =  0 ;
    @Override
    protected void onStop() {
        stopCountdown();
        stopCountdown2();
        stopCountdown();
        audioPlayer.stopPlaying();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboardAndStatusBar(this);
        setContentView(R.layout.activity_cyro_work);
        initDatabase();
        serialPortUtil.serialPort();
        initView();
        data();
        initSend();
        handler();
        onclick();
        hand_display();
        setIntelligeet();
    }

    private void setIntelligeet() {

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
    }
    private void initSend(){
        sendCPU();
        startSending();
    }
    // 开始发送的函数
    private void startSending() {
        if (isSending) return; // 如果已经在发送，避免重复启动
        isSending = true;

        sendTask = new Runnable() {
            @Override
            public void run() {
                sendCPU1(sound); // 发送CPU1任务
                if (isSending) {
                    handler.postDelayed(this, 1000); // 每隔500ms再次执行
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



    private void sendCPU1(int sound) {
        initializationData.delete(0, initializationData.length());
        initializationData.append("AA06")
                .append(DataTypeConversion.intToHex2(sound))
                .append("0000000000000000000000");
        String crc = calcCrc16Modbus(initializationData.toString());
        String send = initializationData.append(crc).append("CC").toString();
        serialPortUtil.serial_send(send);
    }
    // 开始闪烁并设置不同的报警图片
    private void startBlinking(int alarmImageResId, String alarmText) {

//        if (isBlinking) return; // 如果已经在闪烁，避免重复启动
//
//        isBlinking = true;

        // 设置报警图片和文字
        bt_error.setBackgroundResource(alarmImageResId);
        bt_error.setText(alarmText);  // 设置报警文字

//        blinkRunnable = new Runnable() {
//            @Override
//            public void run() {
//                if (bt_error.getVisibility() == View.VISIBLE) {
//                    bt_error.setVisibility(View.GONE); // 隐藏
//                } else {
                    bt_error.setVisibility(View.VISIBLE); // 显示
//                }
                System.out.println("eeeeeeeeeeeee ");
//
//                // 每100ms 切换可见性
//                blinkHandler.postDelayed(this, 1000);
//            }
//        };

        // 开始闪烁
//        blinkHandler.post(blinkRunnable);
    }

    // 停止闪烁的函数
    private void stopBlinking() {
//        isBlinking = false;
//        if (blinkRunnable != null) {
//            blinkHandler.removeCallbacks(blinkRunnable); // 停止闪烁
            bt_error.setVisibility(View.GONE); // 确保隐藏图标
//        }
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
                            version = data_receive.substring(24, 28);
                        }
                    }
                    if (data_receive.startsWith("AAF3")) {
                        if (calcCrc16Modbus(data_receive.substring(0, 28)).equals(anObject) ) {
                            Log.e("读取cpu", "读取cpu成功 " + data_receive);
                            cryo_p1 = Integer.valueOf(data_receive.substring(4, 6), 16);
                            cryo_p2 = Integer.valueOf(data_receive.substring(6, 8), 16);
                            yali1 = Integer.valueOf(data_receive.substring(14, 16), 16);
                            yali2 = Integer.valueOf(data_receive.substring(16, 18), 16);
                            int work_flag1_ = Integer.valueOf(data_receive.substring(18, 20), 16);
                            int work_flag2_ = Integer.valueOf(data_receive.substring(20, 22), 16);
                            if(cryo_p1  == 0 || cryo_p2 == 0 ){
                                handle_null_num ++ ;
                                if(handle_null_num == 5 ){
                                    hand_display();
                                    handle_null_num = 0 ;
                                }
                            }
                            else {
                                handle_null_num = 0  ;
                                hand_display();
                            }
                            if(cryo_p1 == cryo_p2 && cryo_p1 != 0 ){
                                identicalCounter++;
                                if (identicalCounter >= 5) {
                                    sound = 200;
                                    startBlinking(R.drawable.error, getResources().getString(R.string.do_not_onstall_same_handpleces));
                                    bt_work1.setEnabled(false);
                                    bt_work2.setEnabled(false);
                                }
                            }else {
                                if(real_error==0) {
                                    sound = 0;
                                    identicalCounter = 0;
                                    stopBlinking();
                                    bt_work1.setEnabled(true);
                                    bt_work2.setEnabled(true);
                                }
                            }
                            CyroWorkActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(cryo_p1 == cryo_p2){
                                        sound = 200;
                                        real_error = 9;
                                        startBlinking(R.drawable.error,getResources().getString(R.string.do_not_onstall_same_handpleces));
                                        bt_work1.setEnabled(false);
                                        bt_work2.setEnabled(false);
                                    }else {
                                        sound = 0;
                                        real_error = 0;
                                        stopBlinking();
                                        bt_work1.setEnabled(true);
                                        bt_work2.setEnabled(true);
                                    }
                                    CyroWorkActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            sk_p1.setProgress(yali1);
                                            sk_p4.setProgress(yali2);
                                            if(work_flag1 !=work_flag1_){
                                                work_flag1 = work_flag1_ ;
                                                if(work_flag1 == 1){
                                                    bt_work1.setBackgroundResource(R.drawable.work4);
                                                    startCountdown();
                                                    // 如果 代理开启的话：
                                                    if(permissionsDB.queryFlag("flag2")==1){
                                                        startPermissionsTimer();
                                                    }
                                                }else {
                                                    stopCountdown();
                                                    bt_work1.setBackgroundResource(R.drawable.work3);
                                                    stop_time1 = first_time1 -time1_ms;
                                                    LayoutInflater inflater = getLayoutInflater();
                                                    View diaglogView = inflater.inflate(R.layout.custom_dialog_stop,null);
                                                    final AlertDialog save_diaglog = new AlertDialog.Builder(CyroWorkActivity.this)
                                                            .setView(diaglogView)
                                                            .create();
                                                    Window window =save_diaglog.getWindow();
                                                    if(window !=null){
                                                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    }
                                                    Button bt_save = diaglogView.findViewById(R.id.bt_save);
                                                    Button bt_back = diaglogView.findViewById(R.id.bt_back);
                                                    bt_save.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            date();
                                                            @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", stop_time1/60, stop_time1%60);
                                                            Log.e("stopTime",String.valueOf(time));
                                                            UserInfo userInfo = new UserInfo();
                                                            userInfo.setMachine(cpuid);
                                                            userInfo.setUserID(userID);
                                                            userInfo.setUserInfoID(userInfoDatabase.queryMaxUserInfo(String.valueOf(userID))+1);
                                                            userInfo.setWork_mode(String.valueOf(cyro_mode));
                                                            userInfo.setP12_mode(String.valueOf(yali1));
                                                            userInfo.setP34_mode(String.valueOf(setTemp1));
                                                            userInfo.setWork_date(date);
                                                            userInfo .setMode_duration(time);
                                                            userInfo.setBody_part(String.valueOf(cryo_p1));
                                                            userInfo.setPan(String.valueOf(2));//区分冷冻还是磁力，2是冷冻
                                                            if(userInfoDatabase.insertUserinfo(userInfo)>0){
                                                                userInfoAPIInsert(userInfo);
                                                            }
                                                            save_diaglog.cancel();
                                                        }
                                                    });
                                                    save_diaglog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                        @Override
                                                        public void onDismiss(DialogInterface dialog) {
                                                            hideKeyboardAndStatusBar(CyroWorkActivity.this);
                                                        }
                                                    });
                                                    bt_back.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            save_diaglog.cancel();
                                                        }
                                                    });
                                                    if(user_local==2){
                                                        save_diaglog.show();
                                                    }
                                                }
                                            }
                                           if(work_flag2 != work_flag2_){
                                               work_flag2 = work_flag2_;
                                               if(work_flag2 == 1){
                                                   startCountdown2();
                                                   if(permissionsDB.queryFlag("flag2")==1){
                                                       startPermissionsTimer();
                                                   }
                                                   bt_work2.setBackgroundResource(R.drawable.work4);
                                               }else {
                                                   stopCountdown2();
                                                   bt_work2.setBackgroundResource(R.drawable.work3);
                                                   stop_time2 = first_time2-time2_ms;
                                                   LayoutInflater inflater = getLayoutInflater();
                                                   View diaglogView = inflater.inflate(R.layout.custom_dialog_stop,null);
                                                   final AlertDialog save_diaglog = new AlertDialog.Builder(CyroWorkActivity.this)
                                                           .setView(diaglogView)
                                                           .create();
                                                   Window window =save_diaglog.getWindow();
                                                   if(window !=null){
                                                       window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                   }
                                                   Button bt_save = diaglogView.findViewById(R.id.bt_save);
                                                   Button bt_back = diaglogView.findViewById(R.id.bt_back);
                                                   bt_save.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           date();
                                                           @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", stop_time2/60, stop_time2%60);
                                                           UserInfo userInfo = new UserInfo();
                                                           userInfo.setMachine(cpuid);
                                                           userInfo.setUserID(userID);
                                                           userInfo.setUserInfoID(userInfoDatabase.queryMaxUserInfo(String.valueOf(userID))+1);
                                                           userInfo.setWork_mode(String.valueOf(cyro_mode));
                                                           userInfo.setP12_mode(String.valueOf(yali2));
                                                           userInfo.setP34_mode(String.valueOf(setTemp2));
                                                           userInfo.setWork_date(date);
                                                           userInfo.setMode_duration(time);
                                                           userInfo.setBody_part(String.valueOf(cryo_p2));
                                                           userInfo.setPan(String.valueOf(2));//区分冷冻还是磁力，2是冷冻
                                                           if(userInfoDatabase.insertUserinfo(userInfo)>0){
                                                               userInfoAPIInsert(userInfo);
                                                           }
                                                           save_diaglog.cancel();
                                                       }
                                                   });
                                                   save_diaglog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                       @Override
                                                       public void onDismiss(DialogInterface dialog) {
                                                           hideKeyboardAndStatusBar(CyroWorkActivity.this);
                                                       }
                                                   });
                                                   bt_back.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           save_diaglog.cancel();
                                                       }
                                                   });
                                                   if(user_local==2){
                                                       save_diaglog.show();
                                                   }
                                               }
                                           }
                                        }
                                    });

                                }
                            });
                        }
//                        isGetAAF3 = true;
                    }
                    if (data_receive.startsWith("AAF6")) {
                        if (calcCrc16Modbus(data_receive.substring(0, 28)).equals(anObject)) {
                            Log.e("读取cpu", "读取cpu成功 " + data_receive);
                            int yali11 = Integer.valueOf(data_receive.substring(8, 10), 16);
                            int yali22 = Integer.valueOf(data_receive.substring(10, 12), 16);
                            cryo_temp1 = Integer.valueOf(data_receive.substring(12, 14), 16);
                            cryo_temp2 = Integer.valueOf(data_receive.substring(14, 16), 16);
                            cryo_flow1 = Integer.valueOf(data_receive.substring(16, 18), 16);
                            cryo_flow2 = Integer.valueOf(data_receive.substring(18, 20), 16);
                            no_hand1 = Integer.valueOf(data_receive.substring(20, 22), 16);
                            no_hand2 = Integer.valueOf(data_receive.substring(22, 24), 16);
                            error = Integer.valueOf(data_receive.substring(24, 26), 16);
                            System.out.println("solve  get  "+ cryo_temp1 +"  "+ cryo_temp2 +"  "+ cryo_flow1 +"   "+ cryo_flow2 +"   "+ no_hand1 +"   "+ no_hand2 + "   "+ error);
                            CyroWorkActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int a = 0;
                                    int b = 0;
                                    if (cryo_temp1 > 127) {
                                        a = cryo_temp1 - 256;  // 转换为负数
                                    } else {
                                        a = cryo_temp1;        // 保持为正数
                                    }

                                    if (cryo_temp2 > 127) {
                                        b = cryo_temp2 - 256;  // 转换为负数
                                    } else {
                                        b = cryo_temp2;        // 保持为正数
                                    }
                                    tv_hand1_yali.setText(String.valueOf(yali11));
                                    tv_hand2_yali.setText(String.valueOf(yali22));
                                    tv_hand1_temp.setText(String.valueOf(a));
                                    tv_hand2_temp.setText(String.valueOf(b));
                                    tv_hand1_flow.setText(String.valueOf(cryo_flow1));
                                    tv_hand2_flow.setText(String.valueOf(cryo_flow2));

                                    if(yali11>yali_error){
                                        real_error = 1;
                                        solveError();
                                    }
                                    if(yali2 > yali_error){
                                        real_error = 2;
                                        solveError();
                                    }
                                    if(cryo_flow1 < flow_error){
                                        real_error = 3;
                                        solveError();
                                    }
                                    if(cryo_flow2 < flow_error){
                                        real_error = 4;
                                        solveError();
                                    }
                                    if(no_hand1 == 1){
                                        real_error = 5;
                                        solveError();
                                    }
                                    if(no_hand2 == 1){
                                        real_error = 6;
                                        solveError();
                                    }
                                    if(no_hand2 ==1 && no_hand1 == 1){
                                        identicalCounter7 ++;
                                        if(identicalCounter7 >=5){
                                            identicalCounter7 = 0 ;
                                            real_error = 8;
                                            solveError();
                                        }
                                    }
                                    if(error == 1){
                                        real_error = 7;
                                        solveError();
                                    }
                                    // 单独判断两个工作键是否可以
                                    if(no_hand1 == 1 || cryo_flow1 <flow_error||error ==1 ){
                                        isClickWork1 = 1 ;
                                    }
                                    else{
                                        isClickWork1 = 0 ;
                                    }
                                    if(no_hand2 == 1 || cryo_flow2<flow_error || error ==1 ){
                                        isClickWork2 = 1 ;
                                    }
                                    else{
                                        isClickWork2 = 0 ;
                                    }

                                    if((!(yali11>yali_error))&&(!(yali2 > yali_error))&&(!(cryo_flow1 < flow_error))&&(!(cryo_flow2 < flow_error))&&(no_hand1==0)&&(no_hand2==0)&&(error==0)&&(real_error!=9)){
                                        real_error=0;
                                        solveError();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        };
    }
    private void solveError(){
        if(real_error!= 0) {
            if (real_error == 1) {
                // 开始循环播放
                Log.e("", "solveError1: "  );
                identicalCounter1++;
                if (identicalCounter1 > 5) {
                    audioPlayer.startLooping(R.raw.error);
                    sound = 200;
                    hand_num(cl2_p1);
                    startBlinking(R.drawable.error, hand1_text + getResources().getString(R.string.negative_pressure_is_too_large));
                    bt_work1.setEnabled(false);
                    bt_work1.setBackgroundResource(R.drawable.work3);
                    stopCountdown();
                    work_flag1 = 0;
                    sendCPU();
                    if (work_flag2 == 0) {
                        stopPermission();
                    }
                }
            }
            if (real_error == 2) {
                Log.e("", "solveError2: "  );
                identicalCounter2++;
                if (identicalCounter2 > 5) {
                    audioPlayer.startLooping(R.raw.error);
                    sound = 200;
                    hand_num(cl2_p2);
                    startBlinking(R.drawable.error, hand2_text + getResources().getString(R.string.negative_pressure_is_too_large));
                    bt_work2.setEnabled(false);
                    bt_work2.setBackgroundResource(R.drawable.work3);
                    stopCountdown2();
                    work_flag2 = 0;
                    sendCPU();
                    if (work_flag1 == 0) {
                        stopPermission();
                    }
                }
            }
            if(real_error == 3){
                Log.e("", "solveError3: "  );
                identicalCounter3++;
                if(identicalCounter3 >5) {
                    audioPlayer.startLooping(R.raw.error);
                    sound = 200;
                    hand_num(cl2_p1);
                    startBlinking(R.drawable.error, hand1_text + getResources().getString(R.string.waterflow_is_too_slow));
                    bt_work1.setEnabled(false);
                    bt_work1.setBackgroundResource(R.drawable.work3);
                    stopCountdown();
                    work_flag1 = 0;
                    sendCPU();
                    if (work_flag2 == 0) {
                        stopPermission();
                    }
                }
            }
            if(real_error == 4){
                Log.e("", "solveError4: "  );
                identicalCounter4++;
                if(identicalCounter4>5) {
                    audioPlayer.startLooping(R.raw.error);
                    sound = 200;
                    hand_num(cl2_p2);
                    startBlinking(R.drawable.error, hand2_text + getResources().getString(R.string.waterflow_is_too_slow));
                    bt_work2.setEnabled(false);
                    bt_work2.setBackgroundResource(R.drawable.work3);
                    stopCountdown2();
                    work_flag2 = 0;
                    sendCPU();
                    if (work_flag1 == 0) {
                        stopPermission();
                    }
                }
            }
            if(real_error == 5){
                Log.e("", "solveError5: "  );
                identicalCounter5++;
                if(identicalCounter5>5) {
//                    audioPlayer.startLooping(R.raw.error);
                    sound = 200;
                    iv_hand1.setBackgroundResource(R.drawable.error1);
                    bt_work1.setEnabled(false);
                    bt_work1.setBackgroundResource(R.drawable.work3);
                    stopCountdown();
                    work_flag1 = 0;
                    sendCPU();
                    if (work_flag2 == 0) {
                        stopPermission();
                    }
                }
            }
            if(real_error == 6){
                Log.e("", "solveError6: "  );
                identicalCounter6++;
                if(identicalCounter6>5) {
//                    audioPlayer.startLooping(R.raw.error);
                    sound = 200;
                    iv_hand2.setBackgroundResource(R.drawable.error1);
                    bt_work2.setEnabled(false);
                    bt_work2.setBackgroundResource(R.drawable.work3);
                    stopCountdown2();
                    work_flag2 = 0;
                    sendCPU();
                    if (work_flag1 == 0) {
                        stopPermission();
                    }
                }
            }
            if(real_error == 7){
                audioPlayer.startLooping(R.raw.error);
                Log.e("", "solveError7: "  );
                sound = 200;
                bt_work2.setEnabled(false);
                bt_work1.setEnabled(false);
                startBlinking(R.drawable.error_scjt,"");
                bt_work1.setBackgroundResource(R.drawable.work3);
                bt_work2.setBackgroundResource(R.drawable.work3);
                stopCountdown();
                stopCountdown2();
                work_flag1 = 0;
                work_flag2 = 0;
                sendCPU();
                stopPermission();
            }
            if (real_error == 8) // 左右手柄都没插
            {
                audioPlayer.startLooping(R.raw.error);
                sound = 200;
            }
            if(real_error == 9){
                sound = 200;

                startBlinking(R.drawable.error,getResources().getString(R.string.do_not_onstall_same_handpleces));

                bt_work1.setBackgroundResource(R.drawable.work3);
                bt_work2.setBackgroundResource(R.drawable.work3);
                bt_work1.setEnabled(false);
                bt_work2.setEnabled(false);
                work_flag1 = 0;
                work_flag2 = 0;
                stopCountdown();
                stopCountdown2();
                sendCPU();
                stopPermission();
            }
        }else {
            // 停止播放
            audioPlayer.stopPlaying();
            Log.e("", "solveError999: " );
            sound = 0;
            identicalCounter1 = 0;
            identicalCounter2 = 0;
            identicalCounter3 = 0;
            identicalCounter4 = 0;
            identicalCounter5 = 0;
            identicalCounter6 = 0;
            identicalCounter7 = 0;
            sound1 = 0;
            if(work_flag1 != 1){
                setEnable_T_1();
            }
            if(work_flag2 != 1){
                setEnable_T_2();
            }
            stopBlinking();
            bt_work1.setEnabled(true);
            bt_work2.setEnabled(true);
            hand_display();
        }
    }
    private void initView(){
        bt_return = findViewById(R.id.bt_return);
        iv_mode = findViewById(R.id.iv_mode);
        bt_work1 = findViewById(R.id.bt_work1);
        bt_work2 = findViewById(R.id.bt_work2);
        iv_hand1 = findViewById(R.id.iv_hand1);
        iv_hand2 = findViewById(R.id.iv_hand2);
        tv_hand1 = findViewById(R.id.tv_hand1);
        tv_hand2 = findViewById(R.id.tv_hand2);
        sk_p1 = findViewById(R.id.sk_p1);
        sk_p2 = findViewById(R.id.sk_p2);
        sk_p3 = findViewById(R.id.sk_p3);
        sk_p4 = findViewById(R.id.sk_p4);
        sk_p5 = findViewById(R.id.sk_p5);
        sk_p6 = findViewById(R.id.sk_p6);
        bt_jianP1 = findViewById(R.id.bt_jianP1);
        bt_jianP2 = findViewById(R.id.bt_jianP2);
        bt_jianP3 = findViewById(R.id.bt_jianP3);
        bt_jianP4 = findViewById(R.id.bt_jianP4);
        bt_jianP5 = findViewById(R.id.bt_jianP5);
        bt_jianP6 = findViewById(R.id.bt_jianP6);
        bt_jiaP1 = findViewById(R.id.bt_jiaP1);
        bt_jiaP2 = findViewById(R.id.bt_jiaP2);
        bt_jiaP3 = findViewById(R.id.bt_jiaP3);
        bt_jiaP4 = findViewById(R.id.bt_jiaP4);
        bt_jiaP5 = findViewById(R.id.bt_jiaP5);
        bt_jiaP6 = findViewById(R.id.bt_jiaP6);
        bt_set = findViewById(R.id.bt_set);
        tv_hand1_temp = findViewById(R.id.tv_hand1_temp);
        tv_hand1_flow = findViewById(R.id.tv_hand1_flow);
        tv_hand1_time = findViewById(R.id.tv_hand1_time);
        tv_hand1_yali = findViewById(R.id.tv_hand1_yali);
        tv_hand2_temp = findViewById(R.id.tv_hand2_temp);
        tv_hand2_flow = findViewById(R.id.tv_hand2_flow);
        tv_hand2_time = findViewById(R.id.tv_hand2_time);
        tv_hand2_yali = findViewById(R.id.tv_hand2_yali);
        bt_error = findViewById(R.id.bt_error);
        bt_error.setVisibility(View.GONE);
        // 初始化 Handler
        handler_time1 = new Handler(Looper.getMainLooper());
        handler_time2 = new Handler(Looper.getMainLooper());
        // 初始话  handler_permission；
        handler_permission = new Handler(Looper.getMainLooper());
        first_time1 = 1800;
        first_time2 = 1800;
        time1_ms = 1800;
        time2_ms = 1800;

        yali1 = 10;
        yali2 = 10;
        sk_p1.setMax(100);
        sk_p1.setMin(0);
        sk_p4.setMax(100);
        sk_p4.setMin(0);
        sk_p1.setProgress(yali1);
        sk_p4.setProgress(yali1);

        if(cyro_mode == 1){
            iv_mode.setText(getResources().getString(R.string.PROFESSIONAL_MODE));
            bt_set.setVisibility(View.GONE);
        }else if(cyro_mode == 2){
            iv_mode.setText(getResources().getString(R.string.INTELLIGEET_MODE));
            bt_set.setVisibility(View.GONE);
            SetDataBase db = new SetDataBase(CyroWorkActivity.this);
            int mode = db.getSingleColumnDataById(1,"flag24");
            int mode1 = db.getSingleColumnDataById(1,"flag25");
            System.out.println("aaaaabbbcdfradasf"+ mode + " "+ mode1 + "  " +setTemp2 + time2  );
            if(mode == 1){
                setTemp1 = 0;
                time1 = 50;
                sk_p2.setProgress(50);
                sk_p3.setProgress(0);
            }else if(mode == 2){
                setTemp1 = -3;
                time1 = 40;
                sk_p2.setProgress(40);
                sk_p3.setProgress(-3);
            }else if(mode == 3){
                setTemp1 = -5;
                time1 = 35;
                sk_p2.setProgress(35);
                sk_p3.setProgress(-5);
            }else if(mode == 4){
                setTemp1 = -6;
                time1 = 35;
                sk_p2.setProgress(35);
                sk_p3.setProgress(-6);
            }else if(mode == 5){
                setTemp1 = -8;
                time1 = 30;
                sk_p2.setProgress(30);
                sk_p3.setProgress(-8);
            }
            if(mode1 == 1){
                setTemp2 = 0;
                time2 = 50;
                sk_p5.setProgress(50);
                sk_p6.setProgress(0);
            }else if(mode1 == 2){
                setTemp2 = -3;
                time2 = 40;
                sk_p5.setProgress(40);
                sk_p6.setProgress(-3);
            }else if(mode1 == 3){
                setTemp2 = -5;
                time2 = 35;
                sk_p5.setProgress(35);
                sk_p6.setProgress(-5);
            }else if(mode1 == 4){
                setTemp2 = -6;
                time2 = 35;
                sk_p5.setProgress(35);
                sk_p6.setProgress(-6);
            }else if(mode1 == 5){
                setTemp2 = -8;
                time2 = 30;
                sk_p5.setProgress(30);
                sk_p6.setProgress(-8);
            }
        }

        sk_p1.setIndicatorTextFormat("${PROGRESS} kap");
        sk_p4.setIndicatorTextFormat("${PROGRESS} kap");
        sk_p3.setIndicatorTextFormat("${PROGRESS} ℃");
        sk_p6.setIndicatorTextFormat("${PROGRESS} ℃");
        sk_p2.setIndicatorTextFormat("${PROGRESS} min");
        sk_p5.setIndicatorTextFormat("${PROGRESS} min");

        SetDataBase db = new SetDataBase(CyroWorkActivity.this);
        flow_error = db.getSingleColumnDataById(1,"flag3");
        yali_error = db.getSingleColumnDataById(1,"flag4");

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

        audioPlayer = new AudioPlayer(this); //播放声音初始化
    }
    private void startCountdown() {
        if(!isStartCountdown1){
            isStartCountdown1 = true;
            isRunning1 = true;
            runnable1 = new Runnable() {
                @Override
                public void run() {
                    System.out.println("isRunning1 " + isRunning1 + "  " +time1_ms );
                    if(isRunning1){
                        if (time1_ms > 0 ) {
                            time1_ms--;
                            updateTextView();
                            handler_time1.postDelayed(this, 1000);
                        }else{
                            timer1Over();
                            setEnable_T_1();
                            if(work_flag2 == 0){
                                stopPermission();
                            }
                        }
                    }
                }
            };
            handler_time1.post(runnable1);
        }
    }
    private void startCountdown2() {
        if(!isStartCountdown2) {
            isStartCountdown2 = true;
            isRunning2 = true;
            runnable2 = new Runnable() {
                @Override
                public void run() {

                    if(isRunning2){
                        if (time2_ms > 0 ) {
                            time2_ms--;
                            updateTextView2();
                            handler_time2.postDelayed(this, 1000);
                        }
                        else {
                            timer2Over();
                            setEnable_T_2();
                            if(work_flag1 == 0){
                                stopPermission();
                            }
                        }
                    }
                }
            };
            handler_time2.post(runnable2);
        }
    }
    private void startPermissionsTimer(){
        if(!isStartPermission){
            isStartPermission = true;
            isRunningPermission = true;
            runPermission = new Runnable() {
                @Override
                public void run() {
                    if(isRunningPermission){
                        timing_second.setValue(timing_second.getValue()-1);
                        permissionsDB.updateFlag("timingSecond",timing_second.getValue());
                        if((timing_first.getValue() + timing_second.getValue() )>=0){
                            handler_permission.postDelayed(this, 1000);
                        }
                        // 当秒为 0 ；
                        if(timing_second.getValue() <=0){
                            // 当分不为 0 ;
                            if(timing_first.getValue() >0){
                                timing_first.setValue(timing_first.getValue() -1);
                                permissionsDB.updateFlag("timing",timing_first.getValue());
                                timing_second.setValue(60);
                            }
                            // 分为  0
                            else{

                                stopPermission();
                                timer1Over();
                                timer2Over();
                                showVerificationDialog();
                            }
                        }
                   }
                }
            };
            handler_permission.post(runPermission);
        }
    }

    // 停止倒计时方法
    private void stopPermission(){
        isRunningPermission = false;
        isStartPermission = false;
        handler_permission.removeCallbacks(runPermission);
    }
    private void stopCountdown() {
        isRunning1 = false;
        isStartCountdown1 = false;
        handler_time1.removeCallbacks(runnable1); // 停止 handler 的调用
    }
    private void stopCountdown2() {
        isRunning2 = false;
        isStartCountdown2 = false;
        handler_time2.removeCallbacks(runnable2); // 停止 handler 的调用
    }
    @SuppressLint("DefaultLocale")
    private void updateTextView() {
        tv_hand1_time.setText(String.format("Time remaining: %d seconds", time1_ms));

        int minutes = time1_ms / 60;
        int seconds = time1_ms % 60;

        // 使用 String.format 格式化为 "MM:SS"
        tv_hand1_time.setText(String.format("%02d:%02d", minutes, seconds));
    }
    @SuppressLint("DefaultLocale")
    private void updateTextView2() {
        tv_hand2_time.setText(String.format("Time remaining: %d seconds", time2));

        int minutes = time2_ms / 60;
        int seconds = time2_ms % 60;
        tv_hand2_time.setText(String.format("%02d:%02d", minutes, seconds));

    }
    @SuppressLint("ClickableViewAccessibility")
    private void onclick(){
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(work_flag1 == 0 && work_flag2 == 0) {
                    audioPlayer.stopPlaying();
                    stopSending();
                    sendCPU2();
                    Intent intent = new Intent(CyroWorkActivity.this, CyroModeActivity.class);
                    startActivity(intent);
                }
            }
        });
        iv_hand1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("aaaaaaaaaaabbbbbbbbbbcccccccc"+cyro_mode);
                if(work_flag1 == 0) {
                    if (cyro_mode == 2) {
                        hand_num = 1;
                        showIntelligentModeDialog();

                    }
                }
            }
        });
        iv_hand2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(work_flag2 == 0) {
                    if (cyro_mode == 2) {
                        hand_num = 2;
                        showIntelligentModeDialog();

                    }
                }
            }
        });
        bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CyroWorkActivity.this, CyroIntelligeetModeSetActivity.class);
                startActivity(intent);
            }
        });
        bt_work1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(work_flag1 == 0){
                        if(isClickWork1 ==0 ){
                            work_flag1 = 1;
                            time1_ms = time1*60;
                            first_time1 = time1_ms;
                            startCountdown();
                            if(permissionsDB.queryFlag("flag2")==1){
                                startPermissionsTimer();
                            }
                            bt_work1.setBackgroundResource(R.drawable.work4);
                            setEnable_F_1();
                            sendCPU();
                        }
                    }else {
                        stop_time1 = first_time1 -time1_ms;
                        work_flag1 = 0;
                        setEnable_T_1();
                        stopCountdown();
                        if(work_flag2 == 0 ){
                            stopPermission();
                        }
                        bt_work1.setBackgroundResource(R.drawable.work3);
                        sendCPU();
                        LayoutInflater inflater = getLayoutInflater();
                        View diaglogView = inflater.inflate(R.layout.custom_dialog_stop,null);
                        final AlertDialog save_diaglog = new AlertDialog.Builder(CyroWorkActivity.this)
                                .setView(diaglogView)
                                .create();
                        Window window =save_diaglog.getWindow();
                        if(window !=null){
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                        Button bt_save = diaglogView.findViewById(R.id.bt_save);
                        Button bt_back = diaglogView.findViewById(R.id.bt_back);
                        bt_save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                date();
                                @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", stop_time1/60, stop_time1%60);
                                Log.e("stopTime",String.valueOf(time));
                                UserInfo userInfo = new UserInfo();
                                userInfo.setMachine(cpuid);
                                userInfo.setUserID(userID);
                                userInfo.setUserInfoID(userInfoDatabase.queryMaxUserInfo(String.valueOf(userID))+1);
                                userInfo.setWork_mode(String.valueOf(cyro_mode));
                                userInfo.setP12_mode(String.valueOf(yali1));
                                userInfo.setP34_mode(String.valueOf(setTemp1));
                                userInfo.setWork_date(date);
                                userInfo .setMode_duration(time);
                                userInfo.setBody_part(String.valueOf(cryo_p1));
                                userInfo.setPan(String.valueOf(2));//区分冷冻还是磁力，2是冷冻
                                if(userInfoDatabase.insertUserinfo(userInfo)>0){
                                    userInfoAPIInsert(userInfo);
                                }
                                save_diaglog.cancel();
                            }
                        });
                        save_diaglog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                hideKeyboardAndStatusBar(CyroWorkActivity.this);
                            }
                        });
                        bt_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                save_diaglog.cancel();
                            }
                        });

                        if(user_local==2){
                            save_diaglog.show();
                        }
                    }
                }
        });
        bt_work2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(work_flag2 == 0 ){
                    if(isClickWork2 ==0){
                        work_flag2 = 1;
                        time2_ms = time2 * 60 ;
                        first_time2 = time2_ms;
                        startCountdown2();
                        setEnable_F_2();
                        if(permissionsDB.queryFlag("flag2")==1){
                            startPermissionsTimer();
                        }
                        bt_work2.setBackgroundResource(R.drawable.work4);
                        sendCPU();
                    }
                }else {
                    work_flag2 = 0;
                    stop_time2 = first_time2-time2_ms;
                    stopCountdown2();
                    setEnable_T_2();
                    if(work_flag1 == 0 ){
                        stopPermission();
                    }
                    bt_work2.setBackgroundResource(R.drawable.work3);
                    sendCPU();
                    LayoutInflater inflater = getLayoutInflater();
                    View diaglogView = inflater.inflate(R.layout.custom_dialog_stop,null);
                    final AlertDialog save_diaglog = new AlertDialog.Builder(CyroWorkActivity.this)
                            .setView(diaglogView)
                            .create();
                    Window window =save_diaglog.getWindow();
                    if(window !=null){
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    }
                    Button bt_save = diaglogView.findViewById(R.id.bt_save);
                    Button bt_back = diaglogView.findViewById(R.id.bt_back);
                    bt_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            date();
                            @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", stop_time2/60, stop_time2%60);
                            UserInfo userInfo = new UserInfo();
                            userInfo.setMachine(cpuid);
                            userInfo.setUserID(userID);
                            userInfo.setUserInfoID(userInfoDatabase.queryMaxUserInfo(String.valueOf(userID))+1);
                            userInfo.setWork_mode(String.valueOf(cyro_mode));
                            userInfo.setP12_mode(String.valueOf(yali2));
                            userInfo.setP34_mode(String.valueOf(setTemp2));
                            userInfo.setWork_date(date);
                            userInfo.setMode_duration(time);
                            userInfo.setBody_part(String.valueOf(cryo_p2));
                            userInfo.setPan(String.valueOf(2));//区分冷冻还是磁力，2是冷冻
                            if(userInfoDatabase.insertUserinfo(userInfo)>0){
                                userInfoAPIInsert(userInfo);
                            }
                            save_diaglog.cancel();
                        }
                    });
                    save_diaglog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            hideKeyboardAndStatusBar(CyroWorkActivity.this);
                        }
                    });
                    bt_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            save_diaglog.cancel();
                        }
                    });

                    if(user_local==2){
                        save_diaglog.show();
                    }
                }
            }
        });
        sk_p1.setOnSeekChangeListener(new OnSeekChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSeeking(SeekParams seekParams) {
//                yali1 = seekParams.progress;  // 将进度值调整为10的倍数
                int progress = seekParams.progress;
                // 设置步进为10
                progress = (progress / 10) * 10;
                yali1 = progress;
                sk_p1.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                sendCPU();
            }
        });
        sk_p2.setOnSeekChangeListener(new OnSeekChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSeeking(SeekParams seekParams) {
                time1 = seekParams.progress;
                tv_hand1_time.setText(String.valueOf(time1) + ":00");
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                time1_ms = time1 * 60;
                sendCPU();
            }
        });
        sk_p3.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                setTemp1 = seekParams.progress;
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                Log.e("", "onStopTrackingTouch: " + setTemp1 );
                sendCPU();
            }
        });
        sk_p4.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
//                yali2 = seekParams.progress;  // 将进度值调整为10的倍数
                int progress = seekParams.progress;
                // 设置步进为10
                progress = (progress / 10) * 10;
                yali2 = progress;
                sk_p4.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                sendCPU();
            }
        });
        sk_p5.setOnSeekChangeListener(new OnSeekChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSeeking(SeekParams seekParams) {
                time2 = seekParams.progress;
                tv_hand2_time.setText(String.valueOf(time2) + ":00");

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                time2_ms = time2 * 60;
                sendCPU();
            }
        });
        sk_p6.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                setTemp2 = seekParams.progress;

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                Log.e("", "onStopTrackingTouch: " + setTemp2 );

                sendCPU();
            }
        });
        bt_jianP1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jianP1.setBackgroundResource(R.drawable.bt_sub_down);
                    flag1 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (flag1){
                                if (System.currentTimeMillis() > 100) {
                                    yali1-=10;
                                    if(yali1 < 1){
                                        yali1 = 0;
                                    }
                                    Message msg = handler1.obtainMessage();
                                    msg.arg1 = yali1;
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
                    flag1 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    flag1 = false;
                }
                return true;
            }
        });
        bt_jianP2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jianP2.setBackgroundResource(R.drawable.bt_sub_down);
                    flag2 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (flag2){
                                if (System.currentTimeMillis() > 100) {
                                    time1--;
                                    if(time1 < 1){
                                        time1 = 1;
                                    }
                                    Message msg = handler2.obtainMessage();
                                    msg.arg1 = time1;
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
                    bt_jianP2.setBackgroundResource(R.drawable.bt_sub_up);
                    flag2 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    flag2 = false;
                }
                return true;
            }
        });
        bt_jianP3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jianP3.setBackgroundResource(R.drawable.bt_sub_down);
                    flag3 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (flag3){
                                if (System.currentTimeMillis() > 100) {
                                    setTemp1--;
                                    if(setTemp1 < -15){
                                        setTemp1 = -15;
                                    }
                                    Message msg = handler3.obtainMessage();
                                    msg.arg1 = setTemp1;
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
                    bt_jianP3.setBackgroundResource(R.drawable.bt_sub_up);
                    flag3 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    flag3 = false;
                }
                return true;
            }
        });
        bt_jianP4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jianP4.setBackgroundResource(R.drawable.bt_sub_down);
                    flag4 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (flag4){
                                if (System.currentTimeMillis() > 100) {
                                    yali2-=10;
                                    if(yali2 < 1){
                                        yali2 = 0;
                                    }
                                    Message msg = handler4.obtainMessage();
                                    msg.arg1 = yali2;
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
                    bt_jianP4.setBackgroundResource(R.drawable.bt_sub_up);
                    flag4 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    flag4 = false;
                }
                return true;
            }
        });
        bt_jianP5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jianP5.setBackgroundResource(R.drawable.bt_sub_down);
                    flag5 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (flag5){
                                if (System.currentTimeMillis() > 100) {
                                    time2--;
                                    if(time2 < 1){
                                        time2 = 1;
                                    }
                                    Message msg = handler5.obtainMessage();
                                    msg.arg1 = time2;
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
                    bt_jianP5.setBackgroundResource(R.drawable.bt_sub_up);
                    flag5 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    flag5 = false;
                }
                return true;
            }
        });
        bt_jianP6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jianP6.setBackgroundResource(R.drawable.bt_sub_down);
                    flag6 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (flag6){
                                if (System.currentTimeMillis() > 100) {
                                    setTemp2--;
                                    if(setTemp2 < -15){
                                        setTemp2 = -15;
                                    }
                                    Message msg = handler6.obtainMessage();
                                    msg.arg1 = setTemp2;
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
                    bt_jianP6.setBackgroundResource(R.drawable.bt_sub_up);
                    flag6 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    flag6 = false;
                }
                return true;
            }
        });
        bt_jiaP1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jiaP1.setBackgroundResource(R.drawable.bt_add_down);
                    flag1 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (flag1){
                                if (System.currentTimeMillis() > 100) {
                                    yali1 += 10;
                                    if(yali1 > 100){
                                        yali1 = 100;
                                    }
                                    Message msg = handler1.obtainMessage();
                                    msg.arg1 = yali1;
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
                    flag1 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    flag1 = false;
                }
                return true;
            }
        });
        bt_jiaP2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jiaP2.setBackgroundResource(R.drawable.bt_add_down);
                    flag2 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (flag2){
                                if (System.currentTimeMillis() > 100) {
                                    time1++;
                                    if(time1 > 60){
                                        time1 = 60;
                                    }
                                    Message msg = handler2.obtainMessage();
                                    msg.arg1 = time1;
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
                    bt_jiaP2.setBackgroundResource(R.drawable.bt_add_up);
                    flag2 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    flag2 = false;
                }
                return true;
            }
        });
        bt_jiaP3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jiaP3.setBackgroundResource(R.drawable.bt_add_down);
                    flag3 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (flag3){
                                if (System.currentTimeMillis() > 100) {
                                    setTemp1++;
                                    if(setTemp1 > 5){
                                        setTemp1 = 5;
                                    }
                                    Message msg = handler3.obtainMessage();
                                    msg.arg1 = setTemp1;
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
                    bt_jiaP3.setBackgroundResource(R.drawable.bt_add_up);
                    flag3 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    flag3 = false;
                }
                return true;
            }
        });
        bt_jiaP4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jiaP4.setBackgroundResource(R.drawable.bt_add_down);
                    flag4 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (flag4){
                                if (System.currentTimeMillis() > 100) {
                                    yali2+=10;
                                    if(yali2 > 100){
                                        yali2 = 100;
                                    }
                                    Message msg = handler4.obtainMessage();
                                    msg.arg1 = yali2;
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
                    bt_jiaP4.setBackgroundResource(R.drawable.bt_add_up);
                    flag4 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    flag4 = false;
                }
                return true;
            }
        });
        bt_jiaP5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jiaP5.setBackgroundResource(R.drawable.bt_add_down);
                    flag5 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (flag5){
                                if (System.currentTimeMillis() > 100) {
                                    time2++;
                                    if(time2 > 60){
                                        time2 = 60;
                                    }
                                    Message msg = handler5.obtainMessage();
                                    msg.arg1 = time2;
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
                    bt_jiaP5.setBackgroundResource(R.drawable.bt_add_up);
                    flag5 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    flag5 = false;
                }
                return true;
            }
        });
        bt_jiaP6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    bt_jiaP6.setBackgroundResource(R.drawable.bt_add_down);
                    flag6 = true;
                    Thread a = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (flag6){
                                if (System.currentTimeMillis() > 100) {
                                    setTemp2++;
                                    if(setTemp2 > 5){
                                        setTemp2 = 5;
                                    }
                                    Message msg = handler6.obtainMessage();
                                    msg.arg1 = setTemp2;
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
                    bt_jiaP6.setBackgroundResource(R.drawable.bt_add_up);
                    flag6 = false;
                }
                else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                    flag6 = false;
                }
                return true;
            }
        });
    }

    private void setEnable_F_1() {
//        sk_p2.setEnabled(false);
//        sk_p3.setEnabled(false);
//        bt_jianP2.setEnabled(false);
//        bt_jianP3.setEnabled(false);
//        bt_jiaP2.setEnabled(false);
//        bt_jiaP3.setEnabled(false);
    }
    private void setEnable_T_1(){
        sk_p1.setEnabled(true);
        bt_jianP1.setEnabled(true);
        bt_jiaP1.setEnabled(true);
//        if(cyro_mode  == 1 ){
            sk_p2.setEnabled(true);
            sk_p3.setEnabled(true);
            bt_jiaP2.setEnabled(true);
            bt_jiaP3.setEnabled(true);
            bt_jianP2.setEnabled(true);
            bt_jianP3.setEnabled(true);
//        }

    }
    private void setEnable_T_2(){
        sk_p4.setEnabled(true);
        bt_jianP4.setEnabled(true);
        bt_jiaP4.setEnabled(true);
//        if(cyro_mode == 1 ){
            sk_p5.setEnabled(true);
            sk_p6.setEnabled(true);
            bt_jianP5.setEnabled(true);
            bt_jianP6.setEnabled(true);
            bt_jianP5.setEnabled(true);
            bt_jianP6.setEnabled(true);
            bt_jiaP5.setEnabled(true);
            bt_jiaP6.setEnabled(true);
//        }

    }
    private void setEnable_F_2(){
//        sk_p5.setEnabled(false);
//        sk_p6.setEnabled(false);
//        bt_jianP5.setEnabled(false);
//        bt_jianP6.setEnabled(false);
//        bt_jiaP5.setEnabled(false);
//        bt_jiaP6.setEnabled(false);
    }

    private void handler(){
        handler1 = new Handler(msg -> {
            sk_p1.setProgress(yali1);
            sendCPU();
            return true;
        });
        handler2 = new Handler(msg -> {
            sk_p2.setProgress(time1);
            time1_ms = time1*60;
            sendCPU();

            return true;
        });
        handler3 = new Handler(msg -> {
            sk_p3.setProgress(setTemp1);
            sendCPU();

            return true;
        });
        handler4 = new Handler(msg -> {
            sk_p4.setProgress(yali2);
            sendCPU();

            return true;
        });
        handler5 = new Handler(msg -> {
            sk_p5.setProgress(time2);
            time2_ms = time2*60;
            sendCPU();

            return true;
        });
        handler6 = new Handler(msg -> {
            sk_p6.setProgress(setTemp2);
            sendCPU();

            return true;
        });
    }
    private void hand_num(int hand){
        switch (hand){
            case 1:
                hand1_text = "A1-";
                break;
            case 2:
                hand1_text = "B1-";
                break;
            case 3:
                hand1_text = "C1-";
                break;
            case 4:
                hand1_text = "B2-";
                break;
            case 5:
                hand1_text = "A2-";
                break;
            case 6:
                hand1_text = "C2-";
                break;
            case 7:
                hand1_text = "D1-";
                break;
            case 8:
                hand1_text = "D2-";
                break;
            case 9:
                hand1_text = "A3-";
                break;
            case 10:
                hand1_text = "B3-";
                break;
            case 11:
                hand1_text = "C3-";
                break;
            case 12:
                hand1_text = "B4-";
                break;
            case 13:
                hand1_text = "A4-";
                break;
            case 14:
                hand1_text = "C4-";
                break;
            case 15:
                hand1_text = "D3-";
                break;
            case 16:
                hand1_text = "D4-";
                break;
            default:
                hand1_text = "?-";
                break;
        }
    }
    @SuppressLint("SetTextI18n")
    private void hand_display() {
        switch (cryo_p1) {
            case 0:
                iv_hand1.setBackgroundResource(R.drawable.error1);
                tv_hand1.setText("?");
                bt_work1.setEnabled(false);
                break;
            case 1:
                iv_hand1.setBackgroundResource(R.drawable.a);
                tv_hand1.setText("A1");
                bt_work1.setEnabled(true);
                break;
            case 2:
                iv_hand1.setBackgroundResource(R.drawable.b);
                tv_hand1.setText("B1");
                bt_work1.setEnabled(true);
                break;
            case 3:
                iv_hand1.setBackgroundResource(R.drawable.c);
                tv_hand1.setText("C1");
                bt_work1.setEnabled(true);
                break;
            case 4:
                iv_hand1.setBackgroundResource(R.drawable.b);
                tv_hand1.setText("B2");
                bt_work1.setEnabled(true);
                break;
            case 5:
                iv_hand1.setBackgroundResource(R.drawable.a);
                tv_hand1.setText("A2");
                bt_work1.setEnabled(true);
                break;
            case 6:
                iv_hand1.setBackgroundResource(R.drawable.c);
                tv_hand1.setText("C2");
                bt_work1.setEnabled(true);
                break;
            case 7:
                iv_hand1.setBackgroundResource(R.drawable.d);
                tv_hand1.setText("D1");
                bt_work1.setEnabled(true);
                break;
            case 8:
                iv_hand1.setBackgroundResource(R.drawable.d);
                tv_hand1.setText("D2");
                bt_work1.setEnabled(true);
                break;
            case 9:
                iv_hand1.setBackgroundResource(R.drawable.a);
                tv_hand1.setText("A3");
                bt_work1.setEnabled(true);
                break;
            case 10:
                iv_hand1.setBackgroundResource(R.drawable.b);
                tv_hand1.setText("B3");
                bt_work1.setEnabled(true);
                break;
            case 11:
                iv_hand1.setBackgroundResource(R.drawable.c);
                tv_hand1.setText("C3");
                bt_work1.setEnabled(true);
                break;
            case 12:
                iv_hand1.setBackgroundResource(R.drawable.b);
                tv_hand1.setText("B4");
                bt_work1.setEnabled(true);
                break;
            case 13:
                iv_hand1.setBackgroundResource(R.drawable.a);
                tv_hand1.setText("A4");
                bt_work1.setEnabled(true);
                break;
            case 14:
                iv_hand1.setBackgroundResource(R.drawable.c);
                tv_hand1.setText("C4");
                bt_work1.setEnabled(true);
                break;
            case 15:
                iv_hand1.setBackgroundResource(R.drawable.d);
                tv_hand1.setText("D3");
                bt_work1.setEnabled(true);
                break;
            case 16:
                iv_hand1.setBackgroundResource(R.drawable.d);
                tv_hand1.setText("D4");
                bt_work1.setEnabled(true);
                break;
            default:
                iv_hand1.setBackgroundResource(R.drawable.error1);
                tv_hand1.setText("?");
                bt_work1.setEnabled(false);
                break;
        }
        switch (cryo_p2) {
            case 0:
                iv_hand2.setBackgroundResource(R.drawable.error1);
                tv_hand2.setText("?");
                bt_work2.setEnabled(false);
                break;
            case 1:
                iv_hand2.setBackgroundResource(R.drawable.a);
                tv_hand2.setText("A1");
                bt_work2.setEnabled(true);
                break;
            case 2:
                iv_hand2.setBackgroundResource(R.drawable.b);
                tv_hand2.setText("B1");
                bt_work2.setEnabled(true);
                break;
            case 3:
                iv_hand2.setBackgroundResource(R.drawable.c);
                tv_hand2.setText("C1");
                bt_work2.setEnabled(true);
                break;
            case 4:
                iv_hand2.setBackgroundResource(R.drawable.b);
                tv_hand2.setText("B2");
                bt_work2.setEnabled(true);
                break;
            case 5:
                iv_hand2.setBackgroundResource(R.drawable.a);
                tv_hand2.setText("A2");
                bt_work2.setEnabled(true);
                break;
            case 6:
                iv_hand2.setBackgroundResource(R.drawable.c);
                tv_hand2.setText("C2");
                bt_work2.setEnabled(true);
                break;
            case 7:
                iv_hand2.setBackgroundResource(R.drawable.d);
                tv_hand2.setText("D1");
                bt_work2.setEnabled(true);
                break;
            case 8:
                iv_hand2.setBackgroundResource(R.drawable.d);
                tv_hand2.setText("D2");
                bt_work2.setEnabled(true);
                break;
            case 9:
                iv_hand2.setBackgroundResource(R.drawable.a);
                tv_hand2.setText("A3");
                bt_work2.setEnabled(true);
                break;
            case 10:
                iv_hand2.setBackgroundResource(R.drawable.b);
                tv_hand2.setText("B3");
                bt_work2.setEnabled(true);
                break;
            case 11:
                iv_hand2.setBackgroundResource(R.drawable.c);
                tv_hand2.setText("C3");
                bt_work2.setEnabled(true);
                break;
            case 12:
                iv_hand2.setBackgroundResource(R.drawable.b);
                tv_hand2.setText("B4");
                bt_work2.setEnabled(true);
                break;
            case 13:
                iv_hand2.setBackgroundResource(R.drawable.a);
                tv_hand2.setText("A4");
                bt_work2.setEnabled(true);
                break;
            case 14:
                iv_hand2.setBackgroundResource(R.drawable.c);
                tv_hand2.setText("C4");
                bt_work2.setEnabled(true);
                break;
            case 15:
                iv_hand2.setBackgroundResource(R.drawable.d);
                tv_hand2.setText("D3");
                bt_work2.setEnabled(true);
                break;
            case 16:
                iv_hand2.setBackgroundResource(R.drawable.d);
                tv_hand2.setText("D4");
                bt_work2.setEnabled(true);
                break;
            default:
                iv_hand2.setBackgroundResource(R.drawable.error1);
                tv_hand2.setText("?");
                bt_work2.setEnabled(false);
                break;
        }
    }
    private void sendCPU(){
        int a = 0;
        int b = 0;
        if(setTemp1 < 0){
            a = setTemp1 + 256;
        }else {
            a = setTemp1;
        }
        if(setTemp2 < 0){
            b = setTemp2 + 256;
        }else {
            b = setTemp2;
        }
        initializationData.delete(0, initializationData.length());
        initializationData.append("AA03")
                .append(DataTypeConversion.intToHex2(cryo_p1))
                .append(DataTypeConversion.intToHex2(cryo_p2))
                .append(DataTypeConversion.intToHex2(a))
                .append(DataTypeConversion.intToHex2(b))
                .append(DataTypeConversion.intToHex2(1))
                .append(DataTypeConversion.intToHex2(yali1))
                .append(DataTypeConversion.intToHex2(yali2))
                .append(DataTypeConversion.intToHex2(work_flag1))
                .append(DataTypeConversion.intToHex2(work_flag2))
                .append("000000");
//        isGetAAF3 = false;
        String crc = calcCrc16Modbus(initializationData.toString());
        String send = initializationData.append(crc).append("cc").toString();
        serialPortUtil.serial_send(send);
    }
    private void sendCPU2(){ //退出屏幕断电
        int a = 0;
        int b = 0;
        if(setTemp1 < 0){
            a = setTemp1 + 256;
        }else {
            a = setTemp1;
        }
        if(setTemp2 < 0){
            b = setTemp2 + 256;
        }else {
            b = setTemp2;
        }
        initializationData.delete(0, initializationData.length());
        initializationData.append("AA03")
                .append(DataTypeConversion.intToHex2(cryo_p1))
                .append(DataTypeConversion.intToHex2(cryo_p2))
                .append(DataTypeConversion.intToHex2(a))
                .append(DataTypeConversion.intToHex2(b))
                .append(DataTypeConversion.intToHex2(0))
                .append(DataTypeConversion.intToHex2(yali1))
                .append(DataTypeConversion.intToHex2(yali2))
                .append(DataTypeConversion.intToHex2(work_flag1))
                .append(DataTypeConversion.intToHex2(work_flag2))
                .append("000000");
        String crc = calcCrc16Modbus(initializationData.toString());
        String send = initializationData.append(crc).append("cc").toString();
        serialPortUtil.serial_send(send);

    }

    private void timer2Over(){
        work_flag2 = 0;
        stop_time2 = first_time2-time2_ms;
        stopCountdown2();
        bt_work2.setBackgroundResource(R.drawable.work3);
        sendCPU();
        LayoutInflater inflater = getLayoutInflater();
        View diaglogView = inflater.inflate(R.layout.custom_dialog_stop,null);
        final AlertDialog save_diaglog = new AlertDialog.Builder(CyroWorkActivity.this)
                .setView(diaglogView)
                .create();
        Window window =save_diaglog.getWindow();
        if(window !=null){
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        Button bt_save = diaglogView.findViewById(R.id.bt_save);
        Button bt_back = diaglogView.findViewById(R.id.bt_back);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date();
                @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", stop_time2/60, stop_time2%60);
                Log.e("", "onClidsfsdfck: " + time );
                Log.e("stopTime",String.valueOf(time));
                UserInfo userInfo = new UserInfo();
                userInfo.setMachine(cpuid);
                userInfo.setUserID(userID);
                userInfo.setUserInfoID(userInfoDatabase.queryMaxUserInfo(String.valueOf(userID))+1);
                userInfo.setWork_mode(String.valueOf(cyro_mode));
                userInfo.setP12_mode(String.valueOf(yali2));
                userInfo.setP34_mode(String.valueOf(setTemp2));
                userInfo.setWork_date(date);
                userInfo.setMode_duration(time);
                userInfo.setBody_part(String.valueOf(cryo_p2));
                userInfo.setPan(String.valueOf(2));//区分冷冻还是磁力，2是冷冻
                if(userInfoDatabase.insertUserinfo(userInfo)>0){
                    userInfoAPIInsert(userInfo);
                }
                save_diaglog.cancel();
            }
        });
        save_diaglog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hideKeyboardAndStatusBar(CyroWorkActivity.this);
            }
        });
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_diaglog.cancel();
            }
        });

        if(user_local==2){
            save_diaglog.show();
        }
    }
    private void timer1Over(){
        stop_time1 = first_time1 -time1_ms;
        work_flag1 = 0;
        stopCountdown();
        bt_work1.setBackgroundResource(R.drawable.work3);
        sendCPU();
        LayoutInflater inflater = getLayoutInflater();
        View diaglogView = inflater.inflate(R.layout.custom_dialog_stop,null);
        final AlertDialog save_diaglog = new AlertDialog.Builder(CyroWorkActivity.this)
                .setView(diaglogView)
                .create();
        Window window =save_diaglog.getWindow();
        if(window !=null){
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        Button bt_save = diaglogView.findViewById(R.id.bt_save);
        Button bt_back = diaglogView.findViewById(R.id.bt_back);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date();
                @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", stop_time1/60, stop_time1%60);
                Log.e("stopTime",String.valueOf(time));
                UserInfo userInfo = new UserInfo();
                userInfo.setMachine(cpuid);
                userInfo.setUserID(userID);
                userInfo.setUserInfoID(userInfoDatabase.queryMaxUserInfo(String.valueOf(userID))+1);
                userInfo.setWork_mode(String.valueOf(cyro_mode));
                userInfo.setP12_mode(String.valueOf(yali1));
                userInfo.setP34_mode(String.valueOf(setTemp1));
                userInfo.setWork_date(date);
                userInfo .setMode_duration(time);
                userInfo.setBody_part(String.valueOf(cryo_p1));
                userInfo.setPan(String.valueOf(2));//区分冷冻还是磁力，2是冷冻
                if(userInfoDatabase.insertUserinfo(userInfo)>0){
                    userInfoAPIInsert(userInfo);
                }
                save_diaglog.cancel();
            }
        });
        save_diaglog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hideKeyboardAndStatusBar(CyroWorkActivity.this);
            }
        });
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_diaglog.cancel();
            }
        });

        if(user_local==2){
            save_diaglog.show();
        }
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
    private void showVerificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = "Please use the mobile app to reset the number of shots.";
        SpannableString spannableString = new SpannableString(message);
        ForegroundColorSpan redColorSpan = new ForegroundColorSpan(Color.RED);
        spannableString.setSpan(redColorSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setMessage(spannableString);

        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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
                hideKeyboardAndStatusBar(CyroWorkActivity.this);
                bt_return.performClick();
            }
        });
        builder.show();
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
                if(work_flag1 == 1 ){
                    work_flag1 =  0 ;
                    setEnable_T_1();
                    bt_work1.setBackgroundResource(R.drawable.work3);
                    sendCPU();
                    if(work_flag2 == 0 ){
                        stopPermission();
                    }
                }
                if(work_flag2 == 1 ){
                    work_flag2 = 0  ;
                    setEnable_T_2();
                    bt_work2.setBackgroundResource(R.drawable.work3);
                    sendCPU();
                    if(work_flag1 == 0 ){
                        stopPermission();
                    }
                }
                recovery();
                ShowWifiErrorDialog.showWifiError(CyroWorkActivity.this,getWindowManager());
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
            // 处理接收到的消息
            Log.e("", "onReceive: " + message);
            if(Objects.equals(message,"Which_page")){
                sendMessageToServer("page_check");
            }
            else if (Objects.equals(message,"page_cyro_mode")) {
                sendMessageToServer("page_cyro_mode_ok");
                Intent intent1 = new Intent(CyroWorkActivity.this,InformationServiceActivity.class);
                startActivity(intent1);
            }
            else if (message.startsWith("AA03")) {
                yali1 = Integer.valueOf(message.substring(4, 6), 16);
                yali2 = Integer.valueOf(message.substring(6, 8), 16);
                time1 = Integer.valueOf(message.substring(8, 10), 16);
                time2 = Integer.valueOf(message.substring(10, 12), 16);
                setTemp1 = Integer.valueOf(message.substring(12, 14), 16);
                setTemp2 = Integer.valueOf(message.substring(14, 16), 16);
                work_flag1 = Integer.valueOf(message.substring(16, 18), 16);
                work_flag2 = Integer.valueOf(message.substring(18, 20), 16);
                CyroWorkActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sk_p1.setProgress(yali1);
                        sk_p2.setProgress(time1);
                        sk_p3.setProgress(setTemp1);
                        sk_p4.setProgress(yali2);
                        sk_p5.setProgress(time2);
                        sk_p6.setProgress(setTemp2);
                        if(work_flag1 == 0){
                            bt_work1.setBackgroundResource(R.drawable.work3);
                            sendCPU();
                        }else if(work_flag1 == 1){
                            bt_work1.setBackgroundResource(R.drawable.work4);
                            sendCPU();
                        }
                        if(work_flag2 == 0){
                            bt_work2.setBackgroundResource(R.drawable.work3);
                            sendCPU();
                        }else if(work_flag2 == 1){
                            bt_work2.setBackgroundResource(R.drawable.work4);
                            sendCPU();
                        }
                    }
                });
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("websocket_message_received");
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, intentFilter);
        IntentFilter intentFilter1 = new IntentFilter("network_message_received");
        LocalBroadcastManager.getInstance(this).registerReceiver(networkReceiver,intentFilter1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(networkReceiver);
        audioPlayer.stopPlaying();

    }

    private void sendMessageToServer(String message) {
        if (webSocketService != null) {
            webSocketService.sendMessage(message);
        }
    }
    private void showIntelligentModeDialog() {
        // 创建一个 Dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_cyro_intelligeet_mode_set); // 使用您的现有布局
        dialog.setCancelable(true);

        // 初始化控件
        Button bt_return = dialog.findViewById(R.id.bt_return);
        Button bt_ok = dialog.findViewById(R.id.bt_ok);
        CheckBox cb1 = dialog.findViewById(R.id.cb1);
        CheckBox cb2 = dialog.findViewById(R.id.cb2);
        CheckBox cb3 = dialog.findViewById(R.id.cb3);
        CheckBox cb4 = dialog.findViewById(R.id.cb4);
        CheckBox cb5 = dialog.findViewById(R.id.cb5);

        // 获取数据库实例
        SetDataBase db = new SetDataBase(this);

        // 初始化复选框状态
        int mode = 0;
        if (hand_num == 1) {
            mode = db.getSingleColumnDataById(1, "flag24");
        } else if (hand_num == 2) {
            mode = db.getSingleColumnDataById(1, "flag25");
        }

        // 根据 mode 设置复选框
        cb1.setChecked(mode == 1);
        cb2.setChecked(mode == 2);
        cb3.setChecked(mode == 3);
        cb4.setChecked(mode == 4);
        cb5.setChecked(mode == 5);

        // 设置返回按钮点击事件
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // 关闭弹窗
            }
        });

        // 设置确定按钮点击事件
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDataBase db = new SetDataBase(CyroWorkActivity.this);
                int mode = db.getSingleColumnDataById(1,"flag24");
                int mode1 = db.getSingleColumnDataById(1,"flag25");
                if(mode == 1){
                    setTemp1 = 0;
                    time1 = 50;
                    sk_p2.setProgress(50);
                    sk_p3.setProgress(0);
                }else if(mode == 2){
                    setTemp1 = -3;
                    time1 = 40;
                    sk_p2.setProgress(40);
                    sk_p3.setProgress(-3);
                }else if(mode == 3){
                    setTemp1 = -5;
                    time1 = 35;
                    sk_p2.setProgress(35);
                    sk_p3.setProgress(-5);
                }else if(mode == 4){
                    setTemp1 = -6;
                    time1 = 35;
                    sk_p2.setProgress(35);
                    sk_p3.setProgress(-6);
                }else if(mode == 5){
                    setTemp1 = -8;
                    time1 = 30;
                    sk_p2.setProgress(30);
                    sk_p3.setProgress(-8);
                }
                if(mode1 == 1){
                    setTemp2 = 0;
                    time2 = 50;
                    sk_p5.setProgress(50);
                    sk_p6.setProgress(0);
                }else if(mode1 == 2){
                    setTemp2 = -3;
                    time2 = 40;
                    sk_p5.setProgress(40);
                    sk_p6.setProgress(-3);
                }else if(mode1 == 3){
                    setTemp2 = -5;
                    time2 = 35;
                    sk_p5.setProgress(35);
                    sk_p6.setProgress(-5);
                }else if(mode1 == 4){
                    setTemp2 = -6;
                    time2 = 35;
                    sk_p5.setProgress(35);
                    sk_p6.setProgress(-6);
                }else if(mode1 == 5){
                    setTemp2 = -8;
                    time2 = 30;
                    sk_p5.setProgress(30);
                    sk_p6.setProgress(-8);
                }
                dialog.dismiss(); // 关闭弹窗
            }
        });

        // 设置复选框的监听器
        CompoundButton.OnCheckedChangeListener checkBoxListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 取消其他复选框的选中状态
                    cb1.setChecked(buttonView == cb1);
                    cb2.setChecked(buttonView == cb2);
                    cb3.setChecked(buttonView == cb3);
                    cb4.setChecked(buttonView == cb4);
                    cb5.setChecked(buttonView == cb5);

                    // 更新数据库中的 mode 值
                    int selectedMode = 0;
                    if (buttonView == cb1) selectedMode = 1;
                    else if (buttonView == cb2) selectedMode = 2;
                    else if (buttonView == cb3) selectedMode = 3;
                    else if (buttonView == cb4) selectedMode = 4;
                    else if (buttonView == cb5) selectedMode = 5;

                    if (hand_num == 1) {
                        db.updateSingleColumn(1, "flag24", selectedMode);
                    } else if (hand_num == 2) {
                        db.updateSingleColumn(1, "flag25", selectedMode);
                    }
                }
            }
        };

        // 为复选框设置监听器
        cb1.setOnCheckedChangeListener(checkBoxListener);
        cb2.setOnCheckedChangeListener(checkBoxListener);
        cb3.setOnCheckedChangeListener(checkBoxListener);
        cb4.setOnCheckedChangeListener(checkBoxListener);
        cb5.setOnCheckedChangeListener(checkBoxListener);

        // 显示 Dialog
        dialog.show();
    }

}