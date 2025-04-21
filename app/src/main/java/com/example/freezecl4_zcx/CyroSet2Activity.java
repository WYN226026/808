package com.example.freezecl4_zcx;

import static tools.UIUtils.hideKeyboardAndStatusBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import tools.SetDataBase;
import tools.ShowWifiErrorDialog;

public class CyroSet2Activity extends AppCompatActivity {
    private Button bt_return;
    private Button bt_a;
    private Button bt_b;
    private Button bt_1;
    private Button bt_2;
    private Button bt_3;
    private Button bt_4;
    private Button bt_5;
    private Button bt_6;
    private Button bt_7;
    private Button bt_8;
    private Button bt_9;
    private Button bt_10;
    private Button bt_refresh;
    private Button bt_save;
    private EditText et_error_yali;
    private int yali = 0;
    private int ab = 1; //1选中a,2选中b
    private int num = 1;//默认选中等级1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboardAndStatusBar(this);
        setContentView(R.layout.activity_cyro_set2);
        initView();
        onclick();
    }
    private void initView(){
        bt_return = findViewById(R.id.bt_return);
        bt_a = findViewById(R.id.bt_a);
        bt_b = findViewById(R.id.bt_b);
        bt_1 = findViewById(R.id.bt_1);
        bt_2 = findViewById(R.id.bt_2);
        bt_3 = findViewById(R.id.bt_3);
        bt_4 = findViewById(R.id.bt_4);
        bt_5 = findViewById(R.id.bt_5);
        bt_6 = findViewById(R.id.bt_6);
        bt_7 = findViewById(R.id.bt_7);
        bt_8 = findViewById(R.id.bt_8);
        bt_9 = findViewById(R.id.bt_9);
        bt_10 = findViewById(R.id.bt_10);
        et_error_yali = findViewById(R.id.et_error_yali);
        bt_refresh = findViewById(R.id.bt_refresh);
        bt_save = findViewById(R.id.bt_save);

        // 设置最大输入值为 103
        et_error_yali.setFilters(new InputFilter[]{new InputFilterMinMax(0, 103)});

        SetDataBase db = new SetDataBase(CyroSet2Activity.this);
        yali =  db.getSingleColumnDataById(1,"flag4");
        et_error_yali.setText(String.valueOf(yali));
    }
    // 创建 InputFilterMinMax 类，用于限制 EditText 的输入范围
    public static class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input)) {
                    return null;
                }
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int min, int max, int input) {
            return input >= min && input <= max;
        }
    }
    private void onclick(){

        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CyroSet2Activity.this,CyroSet1Activity.class);
                startActivity(intent);
            }
        });
        bt_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ab!=1){
                    ab = 1;
                    bt_a.setBackgroundResource(R.drawable.rounded_button1);
                    bt_b.setBackgroundResource(R.drawable.rounded_button);
                }
            }
        });
        bt_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ab!=2){
                    ab = 2;
                    bt_a.setBackgroundResource(R.drawable.rounded_button);
                    bt_b.setBackgroundResource(R.drawable.rounded_button1);
                }
            }
        });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_error_yali.getText().toString().equals("")) {
                    yali = 0;
                }else {
                    yali = Integer.parseInt(et_error_yali.getText().toString());
                }
                SetDataBase db = new SetDataBase(CyroSet2Activity.this);
                db.updateSingleColumn(1,"flag"+(num+3+10*(ab-1)),yali); //保存水流报警值
            }
        });
        bt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=1;
                bt_1.setBackgroundResource(R.drawable.rounded_button1);
                bt_2.setBackgroundResource(R.drawable.rounded_button);
                bt_3.setBackgroundResource(R.drawable.rounded_button);
                bt_4.setBackgroundResource(R.drawable.rounded_button);
                bt_5.setBackgroundResource(R.drawable.rounded_button);
                bt_6.setBackgroundResource(R.drawable.rounded_button);
                bt_7.setBackgroundResource(R.drawable.rounded_button);
                bt_8.setBackgroundResource(R.drawable.rounded_button);
                bt_9.setBackgroundResource(R.drawable.rounded_button);
                bt_10.setBackgroundResource(R.drawable.rounded_button);
                SetDataBase db = new SetDataBase(CyroSet2Activity.this);
                yali =  db.getSingleColumnDataById(1,"flag"+(num+3+10*(ab-1)));
                et_error_yali.setText(String.valueOf(yali));
            }
        });
        bt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=2;
                bt_1.setBackgroundResource(R.drawable.rounded_button);
                bt_2.setBackgroundResource(R.drawable.rounded_button1);
                bt_3.setBackgroundResource(R.drawable.rounded_button);
                bt_4.setBackgroundResource(R.drawable.rounded_button);
                bt_5.setBackgroundResource(R.drawable.rounded_button);
                bt_6.setBackgroundResource(R.drawable.rounded_button);
                bt_7.setBackgroundResource(R.drawable.rounded_button);
                bt_8.setBackgroundResource(R.drawable.rounded_button);
                bt_9.setBackgroundResource(R.drawable.rounded_button);
                bt_10.setBackgroundResource(R.drawable.rounded_button);
                SetDataBase db = new SetDataBase(CyroSet2Activity.this);
                yali =  db.getSingleColumnDataById(1,"flag"+(num+3+10*(ab-1)));
                et_error_yali.setText(String.valueOf(yali));
            }
        });
        bt_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=3;
                bt_1.setBackgroundResource(R.drawable.rounded_button);
                bt_2.setBackgroundResource(R.drawable.rounded_button);
                bt_3.setBackgroundResource(R.drawable.rounded_button1);
                bt_4.setBackgroundResource(R.drawable.rounded_button);
                bt_5.setBackgroundResource(R.drawable.rounded_button);
                bt_6.setBackgroundResource(R.drawable.rounded_button);
                bt_7.setBackgroundResource(R.drawable.rounded_button);
                bt_8.setBackgroundResource(R.drawable.rounded_button);
                bt_9.setBackgroundResource(R.drawable.rounded_button);
                bt_10.setBackgroundResource(R.drawable.rounded_button);
                SetDataBase db = new SetDataBase(CyroSet2Activity.this);
                yali =  db.getSingleColumnDataById(1,"flag"+(num+3+10*(ab-1)));
                et_error_yali.setText(String.valueOf(yali));
            }
        });
        bt_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=4;
                bt_1.setBackgroundResource(R.drawable.rounded_button);
                bt_2.setBackgroundResource(R.drawable.rounded_button);
                bt_3.setBackgroundResource(R.drawable.rounded_button);
                bt_4.setBackgroundResource(R.drawable.rounded_button1);
                bt_5.setBackgroundResource(R.drawable.rounded_button);
                bt_6.setBackgroundResource(R.drawable.rounded_button);
                bt_7.setBackgroundResource(R.drawable.rounded_button);
                bt_8.setBackgroundResource(R.drawable.rounded_button);
                bt_9.setBackgroundResource(R.drawable.rounded_button);
                bt_10.setBackgroundResource(R.drawable.rounded_button);
                SetDataBase db = new SetDataBase(CyroSet2Activity.this);
                yali =  db.getSingleColumnDataById(1,"flag"+(num+3+10*(ab-1)));
                et_error_yali.setText(String.valueOf(yali));
            }
        });
        bt_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=5;
                bt_1.setBackgroundResource(R.drawable.rounded_button);
                bt_2.setBackgroundResource(R.drawable.rounded_button);
                bt_3.setBackgroundResource(R.drawable.rounded_button);
                bt_4.setBackgroundResource(R.drawable.rounded_button);
                bt_5.setBackgroundResource(R.drawable.rounded_button1);
                bt_6.setBackgroundResource(R.drawable.rounded_button);
                bt_7.setBackgroundResource(R.drawable.rounded_button);
                bt_8.setBackgroundResource(R.drawable.rounded_button);
                bt_9.setBackgroundResource(R.drawable.rounded_button);
                bt_10.setBackgroundResource(R.drawable.rounded_button);
                SetDataBase db = new SetDataBase(CyroSet2Activity.this);
                yali =  db.getSingleColumnDataById(1,"flag"+(num+3+10*(ab-1)));
                et_error_yali.setText(String.valueOf(yali));
            }
        });
        bt_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=6;
                bt_1.setBackgroundResource(R.drawable.rounded_button);
                bt_2.setBackgroundResource(R.drawable.rounded_button);
                bt_3.setBackgroundResource(R.drawable.rounded_button);
                bt_4.setBackgroundResource(R.drawable.rounded_button);
                bt_5.setBackgroundResource(R.drawable.rounded_button);
                bt_6.setBackgroundResource(R.drawable.rounded_button1);
                bt_7.setBackgroundResource(R.drawable.rounded_button);
                bt_8.setBackgroundResource(R.drawable.rounded_button);
                bt_9.setBackgroundResource(R.drawable.rounded_button);
                bt_10.setBackgroundResource(R.drawable.rounded_button);
                SetDataBase db = new SetDataBase(CyroSet2Activity.this);
                yali =  db.getSingleColumnDataById(1,"flag"+(num+3+10*(ab-1)));
                et_error_yali.setText(String.valueOf(yali));
            }
        });
        bt_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=7;
                bt_1.setBackgroundResource(R.drawable.rounded_button);
                bt_2.setBackgroundResource(R.drawable.rounded_button);
                bt_3.setBackgroundResource(R.drawable.rounded_button);
                bt_4.setBackgroundResource(R.drawable.rounded_button);
                bt_5.setBackgroundResource(R.drawable.rounded_button);
                bt_6.setBackgroundResource(R.drawable.rounded_button);
                bt_7.setBackgroundResource(R.drawable.rounded_button1);
                bt_8.setBackgroundResource(R.drawable.rounded_button);
                bt_9.setBackgroundResource(R.drawable.rounded_button);
                bt_10.setBackgroundResource(R.drawable.rounded_button);
                SetDataBase db = new SetDataBase(CyroSet2Activity.this);
                yali =  db.getSingleColumnDataById(1,"flag"+(num+3+10*(ab-1)));
                et_error_yali.setText(String.valueOf(yali));
            }
        });
        bt_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=8;
                bt_1.setBackgroundResource(R.drawable.rounded_button);
                bt_2.setBackgroundResource(R.drawable.rounded_button);
                bt_3.setBackgroundResource(R.drawable.rounded_button);
                bt_4.setBackgroundResource(R.drawable.rounded_button);
                bt_5.setBackgroundResource(R.drawable.rounded_button);
                bt_6.setBackgroundResource(R.drawable.rounded_button);
                bt_7.setBackgroundResource(R.drawable.rounded_button);
                bt_8.setBackgroundResource(R.drawable.rounded_button1);
                bt_9.setBackgroundResource(R.drawable.rounded_button);
                bt_10.setBackgroundResource(R.drawable.rounded_button);
                SetDataBase db = new SetDataBase(CyroSet2Activity.this);
                yali =  db.getSingleColumnDataById(1,"flag"+(num+3+10*(ab-1)));
                et_error_yali.setText(String.valueOf(yali));
            }
        });
        bt_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=9;
                bt_1.setBackgroundResource(R.drawable.rounded_button);
                bt_2.setBackgroundResource(R.drawable.rounded_button);
                bt_3.setBackgroundResource(R.drawable.rounded_button);
                bt_4.setBackgroundResource(R.drawable.rounded_button);
                bt_5.setBackgroundResource(R.drawable.rounded_button);
                bt_6.setBackgroundResource(R.drawable.rounded_button);
                bt_7.setBackgroundResource(R.drawable.rounded_button);
                bt_8.setBackgroundResource(R.drawable.rounded_button);
                bt_9.setBackgroundResource(R.drawable.rounded_button1);
                bt_10.setBackgroundResource(R.drawable.rounded_button);
                SetDataBase db = new SetDataBase(CyroSet2Activity.this);
                yali =  db.getSingleColumnDataById(1,"flag"+(num+3+10*(ab-1)));
                et_error_yali.setText(String.valueOf(yali));
            }
        });
        bt_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=10;
                bt_1.setBackgroundResource(R.drawable.rounded_button);
                bt_2.setBackgroundResource(R.drawable.rounded_button);
                bt_3.setBackgroundResource(R.drawable.rounded_button);
                bt_4.setBackgroundResource(R.drawable.rounded_button);
                bt_5.setBackgroundResource(R.drawable.rounded_button);
                bt_6.setBackgroundResource(R.drawable.rounded_button);
                bt_7.setBackgroundResource(R.drawable.rounded_button);
                bt_8.setBackgroundResource(R.drawable.rounded_button);
                bt_9.setBackgroundResource(R.drawable.rounded_button);
                bt_10.setBackgroundResource(R.drawable.rounded_button1);
                SetDataBase db = new SetDataBase(CyroSet2Activity.this);
                yali =  db.getSingleColumnDataById(1,"flag"+(num+3+10*(ab-1)));
                et_error_yali.setText(String.valueOf(yali));
            }
        });
    }
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
                ShowWifiErrorDialog.showWifiError(CyroSet2Activity.this,getWindowManager());
            }
        }
    };
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
}