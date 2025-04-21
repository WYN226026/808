package com.example.freezecl4_zcx;

import static tools.CRC16Modbus.calcCrc16Modbus;
import static tools.DataTypeConversion.bytesToHexString;
import static tools.UIUtils.hideKeyboardAndStatusBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.design.mclibrary.LauncherDesigner;
import com.hzmct.enjoysdk.api.EnjoySDK;
import com.hzmct.enjoysdk.transform.EnjoyApiNotSupportException;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import db.UploadInfoDB;
import db.UserInfoDatabase;
import entity.UploadUserInfo;
import entity.User;
import entity.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.UserInfoService;
import service.UserService;
import service.api.UserAPI;
import service.api.UserInfoAPI;
import tools.DatabaseHelper;
import tools.EwmDB;
import tools.NetworkChangeReceiver;
import tools.SerialPortUtil;
import tools.ShowWifiErrorDialog;
import tools.network.NetworkStatusManager;
import websocket.WebSocketService;

public class StartActivity extends AppCompatActivity implements NetworkChangeReceiver.NetworkChangeListener {
    public static  int workip = 0;
    private ImageView ig_start_bg;
    public static SoundPool soundPool;
    public static int soundID;
    private boolean key = false;
    private final SerialPortUtil serialPortUtil= new SerialPortUtil();
    public static Handler myHandler;
    public static byte[] mBuffer = null;
    public static WebSocketService webSocketService;
    public static WebSocketService.LocalBinder binder;
    public static boolean network = false;
    private ImageView animationImageView;

    public  static String cpuid = "0";
    private static  int REQUEST_READ_STORAGE =123;
    public static boolean web_H =false;
    private NetworkChangeReceiver networkChangeReceiver;
    private UploadInfoDB uploadInfoDB;
    private UserInfoDatabase userInfoDatabase;
    Thread upLoadUserInsertInsertThread;// 上传线程先增加后删除
    Thread upLoadUserInfoInsertThread;// 上传线程先增加后删除
    Thread upLoadUserDeleteThread;// 上传线程先增加后删除
    Thread upLoadUserInfoDeleteThread;// 上传线程先增加后删除
    public boolean isSendFinishUser= false;// 上传标志位。
    public boolean isSendFinishUserInfo = false;// 上传标志位。
    private boolean isSendFinishUserInsert = false;// 上传标志位。
    public static boolean isSendFinishUserinfoInsert = false;// 上传标志位。
    //控制删除线程的开启和关闭。
    public static boolean isStartUpLoadUserDelete = false;
    private boolean isStartUpLoadUserInfoDelete = false;
    public static int cl2_p1 = 3;
    public static int cl2_p2 = 3;
    public static int cl2_temp1 = 0;
    public static int cl2_temp2 = 0;
    public static int cryo_p1 = 0;
    public static int cryo_p2 = 0;
    public static int cryo_temp1 = 0;
    public static int cryo_temp2 = 0;
    public static int cryo_flow1 = 0;
    public static int cryo_flow2 = 0;

    public static String version = "0.0";
    private EwmDB ewmDB;
    public static EnjoySDK enjoySDK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboardAndStatusBar(this);
        setContentView(R.layout.activity_start);
        NetworkStatusManager networkStatusManager = new NetworkStatusManager(this);
        networkStatusManager.startNetworkStatusChecking();
        try {
            initSecure();
            setCpuid(); //开始时将cpuid放在sharePreferce中
            if(!"com.example.freezecl4_zcx".equals(enjoySDK.getHomePackage())){
                enjoySDK.setHomePackage("com.example.freezecl4_zcx");
            }
        } catch (EnjoyApiNotSupportException e) {
            throw new RuntimeException(e);
        }
        dbInit();
        workip = 2;
        //初始化网络监听
        loginReceiver();
        serialPortUtil.serialPort();
        Listener_function();
        data();
        shakeHands();
        initView();
        LauncherDesigner launcherDesigner = new LauncherDesigner(this);
        Log.e("isLauncher",String.valueOf(launcherDesigner.setLauncher("com.example.freezecl4_zcx")));
        Log.e("Launcher_name",launcherDesigner.getLauncher());
        getSuspendedPermissions();
        openSuspendedWindow();
    }
    private void initSecure() throws EnjoyApiNotSupportException {
        //把该应用设置为安全应用。
        enjoySDK = new EnjoySDK(this);
        enjoySDK.setSecurePasswd("Abc12345","Abc12345");
        enjoySDK.registSafeProgram("Abc12345");
    }
    private void setCpuid() throws EnjoyApiNotSupportException {
        //通过在设置安全密码把当前应用注册为系统安全应用
        cpuid =  enjoySDK.getCpuSerial();//获取设置唯一识别码（cpu串号）
    }

    private void getSuspendedPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (! Settings.canDrawOverlays(StartActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,10);
            }
        }else{
            System.out.println("sdk is "+Build.VERSION.SDK_INT);
        }
    }
    private void openSuspendedWindow() {
        if(ewmDB.getFlag()==1){
            System.out.println("显示 ewmDB");
            Intent intent = new Intent(StartActivity.this, FloatWindowService.class);
            startService(intent);
        }
    }
    private void dbInit() {
        uploadInfoDB = UploadInfoDB.getInstance(this);
        uploadInfoDB.openReadDB();
        uploadInfoDB.openWriteDB();

        userInfoDatabase = UserInfoDatabase.getInstance(this);
        userInfoDatabase.openReadDB();
        userInfoDatabase.openWriteDB();

        ewmDB = EwmDB.getInstance(this);
        ewmDB.openReadDB();
        ewmDB.openWriteDB();
    }

    private void loginReceiver() {
        networkChangeReceiver = new NetworkChangeReceiver(this);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            binder = (WebSocketService.LocalBinder) service;
            webSocketService = binder.getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName className) {
            webSocketService = null;
        }
    };
    private void sendMessageToServer(String message) {
        if (webSocketService != null) {
            webSocketService.sendMessage(message);
        }
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
                ShowWifiErrorDialog.showWifiError(StartActivity.this,getWindowManager());
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
    // 接收WebSocketService发送的消息
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        // 处理接收到的消息
        Log.e("", "onReceive: " + message);
        if(Objects.equals(message, "Successfully_connected")){
            network = true;
        }else if(Objects.equals(message,"Which_page")){
            sendMessageToServer("page_start");
        }else if(Objects.equals(message,"jump_start")){
            sendMessageToServer("jump_start_ok");
            Intent intent1 = new Intent(StartActivity.this,CheckActivity.class);
            startActivity(intent1);
        }else if ("Heartbeat".equals(message)){
            web_H = true;
        }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, WebSocketService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

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
    }

    private void initView(){
        ig_start_bg = findViewById(R.id.ig_start_bg);
        animationImageView = findViewById(R.id.animationImageView);
        requestStoragePermission();
        ig_start_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, CheckActivity.class);
                startActivity(intent);
            }
        });
    }
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
        } else {
            loadImages();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImages();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void loadImages() {
        AnimationDrawable animationDrawable = new AnimationDrawable();

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media.DATA + " like ? and (" +
                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                MediaStore.Images.Media.MIME_TYPE + "=?)";
        String[] selectionArgs = new String[]{"%Pictures%", "image/png", "image/gif", "image/jpeg"};

        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        boolean foundImages = false;
        boolean foundAnim = false;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                Log.e("path",path);
                if (path.endsWith(".png") || path.endsWith(".jpg")||path.equals(".bmp")) {
                    String [] path_Block = path.split("/");         //通过split截取获取文件名
                    String [] path_Block_Block = path_Block[path_Block.length-1].split("\\.");//去掉扩展名。"."为特殊字符需要转义字符 \\
                    String LogoName = path_Block_Block[0];
                    Drawable drawable = Drawable.createFromPath(path);
                    if (drawable != null&&"logo".equals(LogoName.toLowerCase())) {//添加判断只有他的名字为logo时才会替换。
                        foundImages = true;
                        animationDrawable.addFrame(drawable, 200);
                    }
                } else if (path.endsWith(".gif")) {
                    try {
                        String [] path_Block = path.split("/");
                        String [] path_Block_Block = path_Block[path_Block.length-1].split("\\.");
                        String animName = path_Block_Block[0];
                        Log.e("animName",animName);
                        if("anim".equals(animName.toLowerCase())){                         //动画的名字设置成anim
                            foundAnim = true;
                            pl.droidsonroids.gif.GifDrawable gifDrawable = new pl.droidsonroids.gif.GifDrawable(new File(path));
                            ig_start_bg.setImageDrawable(gifDrawable);
                            gifDrawable.setLoopCount(1);  // 设置播放次数为1
                            gifDrawable.start();
                            break;  // 只显示第一个 GIF 文件
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            cursor.close();
        }

        if (!(foundImages||foundAnim)) {
            pl.droidsonroids.gif.GifDrawable gifDrawable1 = null;
            try {
                gifDrawable1 = new pl.droidsonroids.gif.GifDrawable(getResources(), R.drawable.logo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ig_start_bg.setBackground(gifDrawable1);
            gifDrawable1.setLoopCount(1);  // 设置播放次数为1
            gifDrawable1.start();
            return; // Exit the method if no images found.
        }
        if(foundImages){
            animationDrawable.setOneShot(false);
            ig_start_bg.setImageDrawable(animationDrawable);
            animationDrawable.start();
        }
    }
    private void data(){
        myHandler = new Handler(Looper.getMainLooper()) {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@Nullable Message msg) {
                super.handleMessage(msg);
                if (msg != null && msg.obj.toString().length() == 34) {
                    Log.e("", "mode   " + msg.obj.toString());
                    String data_receive = msg.obj.toString();
                    final String anObject = data_receive.substring(28, 32);
                    //握手
                    if (data_receive.startsWith("AAF1")) {
                        if(calcCrc16Modbus(data_receive.substring(0,28)).equals(anObject)) {
                            key = true;
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
                            version = String.valueOf(Integer.valueOf(data_receive.substring(24, 28),16)) ;
                            System.out.println("version"+version);
                        }else {
                            shakeHands();
                        }
                    }
                }
            }
        };
    }
    private void shakeHands(){
        String data1 = "AA01000000000000000000000000";
        String data  = calcCrc16Modbus("AA01000000000000000000000000");
        serialPortUtil.serial_send(data1+data+"CC");
    }
    private void Listener_function(){
        serialPortUtil.setOnDataReceiveListener(buffer -> {
            mBuffer = buffer;
            Message msg = myHandler.obtainMessage();
            msg.obj = bytesToHexString(mBuffer,mBuffer.length);
            myHandler.sendMessage(msg);
        });
    }

    @SuppressLint("NewApi")
    private void initSound() {
        soundPool = new SoundPool.Builder().build();
        soundID = soundPool.load(this, R.raw.starting_up, 1);
    }

    //连接到网络时
    @Override
    public void onNetworkConnected() {
        System.out.println("网络已连接");
        //遍历痕迹表
        loginOnCreate();
        if(webSocketService != null){
            try {
                Thread.sleep(1500);
                //重新连接到服务器中。
                webSocketService.afreshOpen();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //    上传用户治疗信息 删除痕迹。 直接上传全部。删除痕迹（服务器中直接全部删除）
    void userInfoDelete(String machine,List<UploadUserInfo> userInfos){
        UserInfoAPI userInfoAPI = UserInfoService.getUserinfoAPI();
        Call<Long> longCall = userInfoAPI.batchDelete(machine, userInfos);
        longCall.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                long res = response.body();
                if(res ==1){
                    isSendFinishUserInfo = true;
                    uploadInfoDB.deleteAllUploadUserInfoDelete();
                }
                System.out.println("api upload userinfoDelete 成功" + res);
            }
            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                System.out.println("api upload userinfoDelete  失败"+t.getMessage());
            }
        });
    }
    //  上传 用户治疗信息 添加信息 或修改 痕迹 。 全部上传
    void userinfoInsertORUpdate(List<UserInfo> userInfos){
        UserInfoAPI userInfoAPI = UserInfoService.getUserinfoAPI();
        Call<Long> insertCall = userInfoAPI.batchInsertORUpdate(userInfos);
        insertCall.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                long res = response.body();
                if(res ==1){
                    isSendFinishUserinfoInsert = true;
                    uploadInfoDB.deleteAllUploadUserInfoInsert();
                }
                System.out.println("api  upload userinfoInsert 成功" + res);
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                System.out.println("api upload userInfoInsert 失败"+t.getMessage());
            }
        });
    }
    //上传 用户信息删除痕迹 全部上传
    void userDelete(String machine ,List<String> userIDs){
        UserAPI userAPI = UserService.getUserAPI();
        Call<Long> longCall = userAPI.batchDelete(machine, userIDs);
        longCall.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                long res = response.body();
                if(res ==1){
                    isSendFinishUser = true;
                    uploadInfoDB.deleteAllUploadUserDelete();
                }
                System.out.println("api upload userDelete 成功"+res);
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                System.out.println("api upload userDelete 失败"+t.getMessage());
            }
        });
    }
    //上传用户信息 添加痕迹    全部上传
    void userInsertORUpdate(List<User> users){
        UserAPI userAPI = UserService.getUserAPI();
        Call<Long> longCall = userAPI.batchInsertORUpdate(users);
        longCall.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                long res  = response.body();
                if(res == 1){
                    System.out.println("api upload userInsert 成功 res    " +res);
                    isSendFinishUserInsert = true;
                    uploadInfoDB.deleteAllUploadUserInsert();
                }
                System.out.println("api upload userInsert 成功" +res);
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                System.out.println("api upload userInsert 失败"+t.getMessage());
            }
        });
    }

    //连接到网络遍历痕迹数据库中的缩有痕迹表
    private void loginOnCreate(){
        UserAPI userAPI = UserService.getUserAPI();
        //上传服务器 在 服务器判断  有没有这个表。 如果有 则 修改登录日期和登录信息 。
        //如果没有则创建这个表
        Call<Long> longCall = userAPI.loginMachine(cpuid,"1");

        //遍历痕迹 信息。 通过痕迹 信息 。来查询具体的添加的数据信息
        List<UploadUserInfo> userInfoIDs = uploadInfoDB.queryAllUserInfoUploadInsert();
        List<String> userIDs = uploadInfoDB.queryAllUserUploadInsert();
        List<String> usersDelete = uploadInfoDB.queryAllUserUploadDelete();
        List<UploadUserInfo> userInfoDelete = uploadInfoDB.queryAllUserInfoUploadInsertDelete();
        System.out.println("userInfoUpload insert :"+userIDs);
        System.out.println("userInfoUpload delete :"+usersDelete);
        System.out.println("userUpload  insert : "+userInfoIDs);
        System.out.println("userUpload  delete : "+userInfoDelete);
        //查询出来的用户信息， 和用户治疗信息
        List<User> users = userInfoDatabase.batchQueryUser(userIDs);
        List<UserInfo>userInfos = userInfoDatabase.batchQueryUserInfo(userInfoIDs);
        System.out.println("user upload  insert : "+users);
        System.out.println("userinfo upload  insert :"+ userInfos);
        // 如果调用 loginMachine 接口成功后则： 遍历痕迹信息。
        longCall.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                Long insertedId = response.body();
                Log.e("insertedID", String.valueOf(insertedId));
                isSendFinishUser = false;
                isSendFinishUserInfo = false;
                isSendFinishUserinfoInsert = false;
                isSendFinishUserInsert = false;
                startUpLoadUserInsert(users);
                startUpLoadUserInfoInsert(userInfos);

                // 只有添加成功缩有的 用户信息  后才会上传删除 痕迹
                startUpLoadUserDelete(usersDelete);
                // 只有添加成功左右 用户治疗信息后 才会上传删除 痕迹
                startUpLoadUserInfoDelete(userInfoDelete);
            }
            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.e("api", "请求失败：" + t.getMessage());
            }
        });
    }
    //开始上传用户添加信息
    void startUpLoadUserInsert(List<User> users){
        upLoadUserInsertInsertThread = new Thread(new Runnable() {
            @Override
            public void run() {
                userInsertORUpdate(users);
            }
        });
        upLoadUserInsertInsertThread.start();
    }
    //开始上传用户治疗 添加信息
    void startUpLoadUserInfoInsert(List<UserInfo> userInfos){
        upLoadUserInfoInsertThread = new Thread(new Runnable() {
            @Override
            public void run() {
                userinfoInsertORUpdate(userInfos);
            }
        });
        upLoadUserInfoInsertThread.start();
    }
    //当用户添加信息上传完后开始上传删除信息
    void startUpLoadUserDelete(List<String>userID){
        upLoadUserDeleteThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStartUpLoadUserDelete){
                    if (isSendFinishUserInsert){
                        System.out.println("startUpLoadUserDelete is start ");
                        userDelete(cpuid,userID);
                        isStartUpLoadUserDelete = true;
                    }
                }
            }
        });
        upLoadUserDeleteThread.start();
    }
    //当用户治疗信息上传完后开始上传删除信息
    void startUpLoadUserInfoDelete(List<UploadUserInfo>userInfos){
        upLoadUserInfoDeleteThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStartUpLoadUserInfoDelete){
                    if (isSendFinishUserinfoInsert){
                        userInfoDelete(cpuid,userInfos);
                        isStartUpLoadUserInfoDelete = true;
                    }
                }
            }
        });
        upLoadUserInfoDeleteThread.start();
    }
}