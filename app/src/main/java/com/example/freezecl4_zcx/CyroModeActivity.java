package com.example.freezecl4_zcx;

import static com.example.freezecl4_zcx.StartActivity.webSocketService;
import static tools.UIUtils.hideKeyboardAndStatusBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import tools.PermissionsDB;
import tools.ShowWifiErrorDialog;

public class CyroModeActivity extends AppCompatActivity {
    private Button bt_set;
    private Button bt_return;
    private Button bt_PROFESSIONAL_MODE;
    private Button bt_INTELLIGEET_MODE;
    public static int cyro_mode = 1;
    private PermissionsDB permissionsDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboardAndStatusBar(this);
        setContentView(R.layout.activity_cyro_mode);
        initDB();
        initView();
        onclick();
    }

    private void initDB() {
        permissionsDB = PermissionsDB.getInstance(this);
        permissionsDB.openWriteDB();
        permissionsDB.openReadDB();
    }

    private void initView(){
        bt_set = findViewById(R.id.bt_set);
        bt_return = findViewById(R.id.bt_return);
        bt_PROFESSIONAL_MODE = findViewById(R.id.bt_PROFESSIONAL_MODE);
        bt_INTELLIGEET_MODE = findViewById(R.id.bt_INTELLIGEET_MODE);
    }
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            System.out.println("page  EmsModeActivity get  "+ message);
            if("page_hand".equals(message)){
                sendMessageToServer("page_hand_ok");
                Intent intent1 = new Intent(CyroModeActivity.this,CyroHandActivity.class);
                startActivity(intent1);
            }
            else if ("page_cyro_work".equals(message)){
                sendMessageToServer("page_cyro_work_ok");
                Intent intent1 = new Intent(CyroModeActivity.this,CyroWorkActivity.class);
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
            else if ("wifiUnavailable".equals(message)){
                recovery();
                ShowWifiErrorDialog.showWifiError(CyroModeActivity.this,getWindowManager());
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
    private void onclick(){
        bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToServer("page_hand");
                Intent intent = new Intent(CyroModeActivity.this,ModeActivity.class);
                startActivity(intent);
            }
        });
        bt_PROFESSIONAL_MODE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cyro_mode = 1;

                Intent intent = new Intent(CyroModeActivity.this,CyroHandActivity.class);
                startActivity(intent);

            }
        });
        bt_INTELLIGEET_MODE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(permissionsDB.queryFlag("flag1") == 0){
//                    showError(getString(R.string.error1));
//                }
//                else if (permissionsDB.queryFlag("timing") == 0 && permissionsDB.queryFlag("timingSecond") == 0){
//                    showError(getString(R.string.error2));
//                }
//                else{
//                    cyro_mode = 2;
//                    sendMessageToServer("page_cyro_work");
//                    Intent intent = new Intent(CyroModeActivity.this, CyroWorkActivity.class);
//                    startActivity(intent);
//                }
                cyro_mode = 2;

                Intent intent = new Intent(CyroModeActivity.this, CyroHandActivity.class);
                startActivity(intent);
            }
        });
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
}