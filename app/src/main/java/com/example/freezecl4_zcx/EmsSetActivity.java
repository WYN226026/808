package com.example.freezecl4_zcx;

import static com.example.freezecl4_zcx.StartActivity.cl2_p1;
import static com.example.freezecl4_zcx.StartActivity.cl2_p2;
import static com.example.freezecl4_zcx.StartActivity.cl2_temp1;
import static com.example.freezecl4_zcx.StartActivity.cl2_temp2;
import static com.example.freezecl4_zcx.StartActivity.version;
import static tools.CRC16Modbus.calcCrc16Modbus;
import static tools.SerialPortUtil.serialPortManager;
import static tools.UIUtils.hideKeyboardAndStatusBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tools.DataTypeConversion;
import tools.EwmDB;
import tools.PermissionsDB;
import tools.SerialPortUtil;
import tools.SetDataBase;
import tools.ShowWifiErrorDialog;
import tools.windowmanager.AssistMenuWindowManager;
import update.UpdateChecker;

public class EmsSetActivity extends AppCompatActivity {
    private final StringBuilder initializationData = new StringBuilder();

    private boolean up = false;
    private Button bt_update_hex;
    private Button bt_return;
    private Button bt_sound;
    private Button bt_temp_error;
    private TextView tv_temp_t1;
    private TextView tv_temp_t2;
    private int temp_error = 1;//1默认开
    private int sound = 1;//1默认开
    private Button bt_ok;
    private final SerialPortUtil serialPortUtil= new SerialPortUtil();
    private int p12v= 0;
    private int p12rfv= 0;
    private int rf= 0;
    private TextView tv_version;
    private Button bt_set;
    private PermissionsDB permissionsDB;
    private Button bt_update;
    private ProgressBar progressBar;
    private TextView downloadPercentage;
    private Button installButton;
    private AlertDialog downloadDialog;
    private EwmDB ewmDB;
    private Button bt_ewm;
    private Button bt_next;
    private boolean isDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboardAndStatusBar(this);
        setContentView(R.layout.activity_ems_set);
        serialPortUtil.serialPort();
        initView();
        ewmDB = new EwmDB(this);
        permissionsDB = PermissionsDB.getInstance(this);
        permissionsDB.openReadDB();
        permissionsDB.openWriteDB();

        EwmDB.getInstance(this);
        ewmDB.openReadDB();
        ewmDB.openWriteDB();

        if(ewmDB.getFlag()>0){
            bt_ewm.setBackgroundResource(R.drawable.open_ewm);
        }else{
            bt_ewm.setBackgroundResource(R.drawable.close_ewma);
        }
        data();
        sendCPU();
        onclick();
    }
    private final BroadcastReceiver netWorKReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            if("wifiAvailable".equals(message)){
                inset();
                ShowWifiErrorDialog.hideWifiError();
            }
            else if ("wifiUnavailable".equals(message)){
                recovery();
                ShowWifiErrorDialog.showWifiError(EmsSetActivity.this,getWindowManager());
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
    private void inset() {
        //取消一进入页面弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏底部工具栏
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(netWorKReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter1 = new IntentFilter("network_message_received");
        LocalBroadcastManager.getInstance(this).registerReceiver(netWorKReceiver,intentFilter1);
    }

    private void sendCPU(){
        String data1 = "AA04000000000000000000000000";
        String data  = calcCrc16Modbus("AA04000000000000000000000000");
        serialPortUtil.serial_send(data1+data+"CC");
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
                            temp_error = Integer.valueOf(data_receive.substring(14, 16), 16);
                        } else {
                            sendCPU();
                        }
                    }
                    if (data_receive.startsWith("AAF5")) {
                        if (calcCrc16Modbus(data_receive.substring(0, 28)).equals(anObject)) {
                            p12v = Integer.valueOf(data_receive.substring(4, 8), 16);
                            p12rfv = Integer.valueOf(data_receive.substring(8, 12), 16);
                            rf = Integer.valueOf(data_receive.substring(12, 14), 16);
                            temp_error = Integer.valueOf(data_receive.substring(14, 16), 16);
                        }
                    }
                    if(data_receive.startsWith("B1")){
                        //擦除
                        serialPortUtil.serial_send("0A");//DataTypeConversion.intToHex2(bao)
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                serialPortUtil.serial_send("C1");
                            }
                        }).start();
                    }
                    if(data_receive.startsWith("C1")){ //C1
                        sendBinFile();
                    }
                    if(data_receive.startsWith("CC")){
                        up = false;
                        serialPortUtil.serial_send("A1");
                        Intent intent = new Intent(EmsSetActivity.this, StartActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };
    }
    @SuppressLint("SetTextI18n")
    private void initView(){
        bt_update_hex = findViewById(R.id.bt_update_hex);
        bt_return = findViewById(R.id.bt_return);
        bt_temp_error = findViewById(R.id.bt_temp_error);
        bt_sound = findViewById(R.id.bt_sound);
        tv_temp_t1 = findViewById(R.id.tv_temp_t1);
        tv_temp_t2 = findViewById(R.id.tv_temp_t2);
        bt_ok = findViewById(R.id.bt_ok);
        tv_version = findViewById(R.id.tv_version);
        bt_set = findViewById(R.id.bt_set);
        bt_update = findViewById(R.id.bt_update);
        bt_ewm = findViewById(R.id.bt_ewm);
        bt_next = findViewById(R.id.bt_next);

        tv_temp_t1.setText(String.valueOf(cl2_temp1) + "℃");
        tv_temp_t2.setText(String.valueOf(cl2_temp2) + "℃");

        SetDataBase db = new SetDataBase(EmsSetActivity.this);
        temp_error =  db.getSingleColumnDataById(1,"flag1");
        sound = db.getSingleColumnDataById(1,"flag2");

        if(temp_error == 1){
            bt_temp_error.setBackgroundResource(R.drawable.bt_on);
        }else if(temp_error == 0){
            bt_temp_error.setBackgroundResource(R.drawable.bt_off);
        }
        if(sound == 1){
            bt_sound.setBackgroundResource(R.drawable.bt_on);
        }else if(sound == 0){
            bt_sound.setBackgroundResource(R.drawable.bt_off);
        }

        tv_version.setText(String.valueOf(version));

    }
    private void onclick(){
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmsSetActivity.this, EmsSet2Activity.class);
                startActivity(intent);
            }
        });
        bt_update_hex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPasswordDialog();
            }
        });
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmsSetActivity.this,EmsModeActivity.class);
                startActivity(intent);
            }
        });
//二维码开关按钮。
        bt_ewm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ewmDB.getFlag()>0){
                    ewmDB.updateTable(0);
                    bt_ewm.setBackgroundResource(R.drawable.close_ewma);
                    //初始化视图。 打开 服务监听。
                    AssistMenuWindowManager.removeBigWindow(getApplicationContext());
                    AssistMenuWindowManager.removeSmallWindow(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);
                    getApplication().stopService(intent);
                }else{
                    ewmDB.updateTable(1);
                    bt_ewm.setBackgroundResource(R.drawable.open_ewm);
                    // 关闭 服务监听：
                    Intent intent = new Intent(EmsSetActivity.this, FloatWindowService.class);
                    startService(intent);

                }
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDataBase db = new SetDataBase(EmsSetActivity.this);
                db.updateSingleColumn(1, "flag1", temp_error);
                db.updateSingleColumn(1, "flag2", sound);
                sendAA05();
            }
        });
        bt_temp_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(temp_error == 1){
                    temp_error = 0;
                    bt_temp_error.setBackgroundResource(R.drawable.bt_off);
                }else {
                    temp_error = 1;
                    bt_temp_error.setBackgroundResource(R.drawable.bt_on);
                }
            }
        });
        bt_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound == 1){
                    sound = 0;
                    bt_sound.setBackgroundResource(R.drawable.bt_off);
                }else {
                    sound = 1;
                    bt_sound.setBackgroundResource(R.drawable.bt_on);
                }
            }
        });
        // 设置代理模式。
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
                    final AlertDialog passwordInputDialog = new AlertDialog.Builder(EmsSetActivity.this)
                            .setView(dialogView)
                            .create();

                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (etPassword.getText().toString().equals("QH2023")) {
                                passwordInputDialog.dismiss();

                                // Inflate the action buttons layout
                                View actionDialogView = inflater.inflate(R.layout.action_buttons_layout, null);
                                Button btnOpen = actionDialogView.findViewById(R.id.btn_open);
                                Button btnClose = actionDialogView.findViewById(R.id.btn_close);

                                // Create second AlertDialog
                                final AlertDialog actionDialog = new AlertDialog.Builder(EmsSetActivity.this)
                                        .setView(actionDialogView)
                                        .create();

                                btnOpen.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        permissionsDB.updateFlag("flag2",1);
                                        permissionsDB.updateFlag("timing",49999);
                                        permissionsDB.updateFlag("timingSecond",60);
                                        actionDialog.dismiss();
                                    }
                                });

                                btnClose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        permissionsDB.updateFlag("flag2",0);
                                        permissionsDB.updateFlag("timing",49999);
                                        permissionsDB.updateFlag("timingSecond",60);
                                        actionDialog.dismiss();
                                    }
                                });
                                actionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        hideKeyboardAndStatusBar(EmsSetActivity.this);
                                    }
                                });
                                actionDialog.show();
                            } else {
                                Toast.makeText(EmsSetActivity.this,"Wrong password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    passwordInputDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            hideKeyboardAndStatusBar(EmsSetActivity.this);
                        }
                    });
                    passwordInputDialog.show();
                }
            }
        });

        bt_update.setOnClickListener(new View.OnClickListener() {
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
                    clickCount = 0;
                    checkForUpdate();//检查更新
                }
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void checkForUpdate() {
        try {
            String currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                    new AlertDialog.Builder(this)
                            .setMessage(getResources().getString(R.string.newversion))
                            .setPositiveButton(getString(R.string.yes), (dialog, which) -> downloadApkFile())  // 假设downloadApkFile()是你的下载方法
                            .setNegativeButton(getString(R.string.no), null)
                            .show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("SetTextI18n")
    private void downloadApkFile() {
        isDownloading = true; // 开始下载，设置状态
        showDownloadDialog();
        String apkUrl = UpdateChecker.APK_DOWNLOAD_URL;
        Executors.newSingleThreadExecutor().execute(() -> {
            File apkFile = new File(getExternalFilesDir(null), "downloaded_app.apk");
            if (apkFile.exists()) {
                apkFile.delete(); // 删除旧文件
            }
            long downloaded = 0;
            Log.d("DownloadApk", "Start downloading. Already downloaded: " + downloaded + " bytes");

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(apkUrl)
                        .addHeader("Range", "bytes=" + downloaded + "-")
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    OutputStream outputStream = new FileOutputStream(apkFile, true); // 追加模式
                    long total = response.body().contentLength() + downloaded;
                    Log.d("DownloadApk", "Total size to download: " + total + " bytes");

                    byte[] buffer = new byte[2048];
                    int len;
                    while ((len = inputStream.read(buffer)) > 0) {
                        if (!isDownloading) {
                            outputStream.flush();
                            outputStream.close();
                            inputStream.close();
                            Log.d("DownloadApk", "Download cancelled");
                            return;
                        }
                        outputStream.write(buffer, 0, len);
                        downloaded += len;
                        int progress = (int) (downloaded * 100 / total);
                        Log.d("DownloadApk", "Downloading progress: " + progress + "%");
                        runOnUiThread(() -> {
                            progressBar.setProgress(progress);
                            downloadPercentage.setText(progress + "%");
                        });
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                    Log.d("DownloadApk", "Download completed");
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        installButton.setVisibility(View.VISIBLE);
                    });
                    isDownloading = false; // 下载完成，重置状态
                }
            } catch (IOException e) {
                Log.e("DownloadApk", "IOException during download", e);
                isDownloading = false;
                downloadDialog.dismiss();
            } catch (Exception e) {
                Log.e("DownloadApk", "Exception during download", e);
                isDownloading = false;
            }
        });
    }
    private void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_download, null);
        builder.setView(dialogView);

        progressBar = dialogView.findViewById(R.id.progressBar);
        downloadPercentage = dialogView.findViewById(R.id.downloadPercentage); // 获取引用
        installButton = dialogView.findViewById(R.id.button_install);
        installButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!getPackageManager().canRequestPackageInstalls()) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                                .setData(Uri.parse(String.format("package:%s", getPackageName())));
                        startActivityForResult(intent, 1234); // 1234 是一个请求码，你可以随意定义
                    } else {
                        // 调用安装 APK 的方法
                        installApk();
                    }
                } else {
                    // 如果是低于 Android 8.0 版本，直接调用安装 APK 的方法
                    installApk();
                }

            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            if (resultCode == RESULT_OK) {
                // 用户授予了权限，继续安装 APK
                installApk();
            } else {
                // 权限被拒绝，处理逻辑
                Toast.makeText(this, "Permission denied to install APK", Toast.LENGTH_SHORT).show();
            }
        }
    }

        private void installApk() { //7.0及以上安装方法
        File apkFile = new File(getExternalFilesDir(null), "downloaded_app.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri apkUri;

        // 对于Android 7.0及以上版本，使用FileProvider获取Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = getApplicationContext().getPackageName() + ".provider";
            apkUri = FileProvider.getUriForFile(this, authority, apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        } else {
            apkUri = Uri.fromFile(apkFile);
        }

        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void showPasswordDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.password_dialog);

        final EditText passwordEditText = dialog.findViewById(R.id.password_edit_text);
        Button okButton = dialog.findViewById(R.id.ok_button);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPassword = passwordEditText.getText().toString();
                if ("666888".equals(enteredPassword)) {
                    // 密码正确，进行跳转
                    updateAPP();
                    dialog.dismiss();
                } else {
                    // 密码错误，提示用户
                    Toast.makeText(getApplicationContext(), "wrong password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void updateAPP(){
        // 获取外部存储的根目录
        File sdCardRoot = Environment.getExternalStorageDirectory();
        // 获取Download目录
        File directory = new File(sdCardRoot, "Download");
        // 筛选出所有以 .bin 结尾的文件
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".bin"));
        flieBao();
        if (files != null && files.length > 0) {
                // 存在 .bin 文件，执行原来的操作
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        serialPortUtil.serial_send("aaD1000000000000000000000000BB3CCC");
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        serialPortUtil.serial_send("C1");
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sendBinFile();
                            }
                        });

                    }
                }).start();
        } else {
            // 不存在 .bin 文件，提示用户
            Toast.makeText(this, "No. bin file available", Toast.LENGTH_SHORT).show();
        }
    }
    private void flieBao(){
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File directory = new File(sdCardRoot, "Download");
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".bin"));
        if (files != null && files.length > 0) {
            final File file = files[0];
            final long totalSize = file.length();
            if((int) (totalSize / 1024) == 0) {
            }else {
            }
        }
    }
    public void sendBinFile() {
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File directory = new File(sdCardRoot, "Download");
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".bin"));

        if (files != null && files.length > 0) {
            final File file = files[0];
            final long totalSize = file.length();
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Sending files");
            progressDialog.show();

            new Thread(() -> {
                boolean success = false;
                long totalSent = 0; // 跟踪已发送的字节数
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    Log.d("", "开始发送文件，总大小: " + totalSize + "字节");
                    while ((bytesRead = fis.read(buffer)) != -1 && !Thread.currentThread().isInterrupted()) {
                        success = sendWithRetry(buffer, bytesRead, totalSent);
                        if (success) {
                            totalSent += bytesRead; // 增加已发送的字节数
                            int progress = (int) ((totalSent * 100) / totalSize);
                            long finalTotalSent = totalSent;
                            runOnUiThread(() -> {
                                progressDialog.setProgress(progress);
                                if (finalTotalSent == totalSize) {
                                    progressDialog.setTitle("Sending completed");
                                }
                            });
                        } else {
                            runOnUiThread(() -> showErrorDialog("Sending file failed"));
                            break;
                        }
                    }
                } catch (IOException e) {
                    runOnUiThread(() -> showErrorDialog("Error reading file: " + e.getMessage()));
                } finally {
                    runOnUiThread(() -> progressDialog.dismiss());
                }

                if (success && totalSent == totalSize) { // 检查已发送的字节数是否等于文件总字节数
                    runOnUiThread(() -> Toast.makeText(this, "Sending completed", Toast.LENGTH_SHORT).show());

                } else if (success) {
                    runOnUiThread(() -> showErrorDialog("Sending file completed, but byte count mismatch"));
                }
            }).start();
        } else {
            showErrorDialog("No file ending in. bin found");
        }
    }
    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("错误");
        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private String bytesToHex(byte[] bytes, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(length, 16); i++) {  // 仅转换前16字节
            sb.append(String.format("%02X ", bytes[i])+"\n");
        }
        return sb.toString().trim();
    }

    private boolean sendWithRetry(byte[] buffer, int actualBytesRead, long totalSent) throws IOException {
        boolean sent = false;
        int retries = 5; // 最大重试次数
        long delayBetweenRetries = 200; // 重试之间的延时，单位毫秒

        for (int i = 0; i < retries; i++) {
            try {
                // 打印将要发送的数据（为了避免日志太长，这里只打印前16字节）
                String dataToSend = bytesToHex(buffer, actualBytesRead);
                Log.d("", "尝试发送数据 (尝试次数: " + (i + 1) + "): " + dataToSend);

                serialPortManager.sendPacket(Arrays.copyOf(buffer, actualBytesRead)); // 发送实际读取的字节数
                sent = true; // 成功发送后标记为成功
                Log.d("", "发送成功");
                Thread.sleep(delayBetweenRetries); // 等待指定的延时
                break;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Log.e("", "发送过程被中断");
                break;
            }
        }
        return sent;
    }
    private void sendAA05(){
        initializationData.delete(0, initializationData.length());
        initializationData.append("AA02")
                .append(DataTypeConversion.intToHex4(450))
                .append(DataTypeConversion.intToHex4(290))
                .append(DataTypeConversion.intToHex2(40))
                .append(DataTypeConversion.intToHex2(70))
                .append("000000000000");
        serialPortUtil.serial_send(calcCrc16Modbus(initializationData.toString())+"CC");
    }
}