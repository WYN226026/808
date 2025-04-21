package com.example.freezecl4_zcx;

import static com.example.freezecl4_zcx.StartActivity.network;
import static com.example.freezecl4_zcx.StartActivity.webSocketService;
import static tools.UIUtils.hideKeyboardAndStatusBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.Objects;

import tools.ShowWifiErrorDialog;
import websocket.WebSocketService;

public class CheckActivity extends AppCompatActivity {
    private ImageButton bt_log;
    private ImageButton bt_vis;
    public static int gender; // 0 man 1 woman 2 manWoman
    public static int user_local = 1;//初始化本地进入还是用户进入    1 本地    2用户    用户进入工作界面就会保存数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboardAndStatusBar(this);
        setContentView(R.layout.activity_check);
        initView();
        onclick();
    }
    // 接收WebSocketService发送的消息
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            // 处理接收到的消息
            Log.e("", "onReceive: " + message);
           if(Objects.equals(message,"Which_page")){
                sendMessageToServer("page_check");
           }
           else if (Objects.equals(message,"page_query")) {
               sendMessageToServer("page_query_ok");
               Intent intent1 = new Intent(CheckActivity.this,InformationServiceActivity.class);
               startActivity(intent1);
           }
           else if (Objects.equals(message,"page_mode")) {
               sendMessageToServer("page_mode_ok");
               Intent intent1 = new Intent(CheckActivity.this,ModeActivity.class);
               startActivity(intent1);
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
                ShowWifiErrorDialog.showWifiError(CheckActivity.this,getWindowManager());
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
    }
    private void initView(){
        bt_log = findViewById(R.id.bt_log);
        bt_vis = findViewById(R.id.bt_vis);
    }
    private void onclick(){
        bt_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_local = 2;
                Intent intent = new Intent(CheckActivity.this, InformationServiceActivity.class);
                startActivity(intent);
                sendMessageToServer("page_query");
            }
        });
        bt_vis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_local = 1;
                Intent intent = new Intent(CheckActivity.this, ModeActivity.class);
                startActivity(intent);
                sendMessageToServer("page_mode");

            }
        });
    }
    private void sendMessageToServer(String message) {
        if (webSocketService != null) {
            webSocketService.sendMessage(message);
        }
    }
}