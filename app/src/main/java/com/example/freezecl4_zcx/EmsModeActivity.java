package com.example.freezecl4_zcx;

import static com.example.freezecl4_zcx.StartActivity.webSocketService;
import static tools.UIUtils.hideKeyboardAndStatusBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import tools.ShowWifiErrorDialog;

public class EmsModeActivity extends AppCompatActivity {
    private Button bt_set;
    private Button bt_return;
    private Button bt_PROFESSIONAL_MODE;
    private Button bt_INTELLIGEET_MODE;
    public static int sk1 = 3;
    public static int sk2 = 3;
    public static int modePI = 0;
    private Button bt_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboardAndStatusBar(this);
        setContentView(R.layout.activity_ems_mode);
        initView();
        onclick();
    }
    private void initView(){
        bt_set = findViewById(R.id.bt_set);
        bt_return = findViewById(R.id.bt_return);
        bt_PROFESSIONAL_MODE = findViewById(R.id.bt_PROFESSIONAL_MODE);
        bt_INTELLIGEET_MODE = findViewById(R.id.bt_INTELLIGEET_MODE);
        bt_next = findViewById(R.id.bt_next);
    }
    private void onclick(){
        bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmsModeActivity.this,EmsSetActivity.class);
                startActivity(intent);
            }
        });

        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmsModeActivity.this,ModeActivity.class);
                startActivity(intent);
            }
        });
        bt_PROFESSIONAL_MODE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modePI = 1;
                Intent intent = new Intent(EmsModeActivity.this,EmsPartActivity.class);
                startActivity(intent);
            }
        });
        bt_INTELLIGEET_MODE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modePI = 2;
                Intent intent = new Intent(EmsModeActivity.this,EmsPartActivity.class);
                startActivity(intent);
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
                ShowWifiErrorDialog.showWifiError(EmsModeActivity.this,getWindowManager());
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
            System.out.println("page  EmsModeActivity get  "+ message);
            if("page_mode".equals(message)){
                sendMessageToServer("page_mode_ok");
                modePI = 1;
                Intent intent1 = new Intent(EmsModeActivity.this,ModeActivity.class);
                startActivity(intent1);
            }
            else if ("page_gender_p".equals(message)){
                sendMessageToServer("page_gender_p_ok");
                modePI = 2 ;
                Intent intent1 = new Intent(EmsModeActivity.this,EmsPartActivity.class);
                startActivity(intent1);
            }
            else if ("page_gender_i".equals(message)){
                sendMessageToServer("page_gender_i_ok");
                Intent intent1 = new Intent(EmsModeActivity.this,EmsPartActivity.class);
                startActivity(intent1);
            }
            else if ("Which_page".equals(message)){
                sendMessageToServer("page_ems_mode");
            }
        }
    };
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
}