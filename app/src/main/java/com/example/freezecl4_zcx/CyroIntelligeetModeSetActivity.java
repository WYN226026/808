package com.example.freezecl4_zcx;

import static com.example.freezecl4_zcx.CyroWorkActivity.hand_num;
import static tools.UIUtils.hideKeyboardAndStatusBar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import tools.SetDataBase;

public class CyroIntelligeetModeSetActivity extends AppCompatActivity {
    private Button bt_return;
    private Button bt_ok;
    private CheckBox cb1;
    private CheckBox cb2;
    private CheckBox cb3;
    private CheckBox cb4;
    private CheckBox cb5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboardAndStatusBar(this);
        setContentView(R.layout.activity_cyro_intelligeet_mode_set);
        initDB();
        initView();
        onclick();
    }

    private void initDB() {

    }

    private void initView(){
        bt_return = findViewById(R.id.bt_return);
        bt_ok = findViewById(R.id.bt_ok);
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);
        int mode = 0;
        if(hand_num == 1) {
            SetDataBase db = new SetDataBase(CyroIntelligeetModeSetActivity.this);
             mode = db.getSingleColumnDataById(1, "flag24");
        }else if(hand_num == 2){
            SetDataBase db = new SetDataBase(CyroIntelligeetModeSetActivity.this);
            mode = db.getSingleColumnDataById(1, "flag25");
        }
        if(mode == 1){
            cb1.setChecked(true);
            cb2.setChecked(false);
            cb3.setChecked(false);
            cb4.setChecked(false);
            cb5.setChecked(false);
        }else if(mode == 2){
            cb1.setChecked(false);
            cb2.setChecked(true);
            cb3.setChecked(false);
            cb4.setChecked(false);
            cb5.setChecked(false);
        }else if(mode == 3){
            cb1.setChecked(false);
            cb2.setChecked(false);
            cb3.setChecked(true);
            cb4.setChecked(false);
            cb5.setChecked(false);
        }else if(mode == 4){
            cb1.setChecked(false);
            cb2.setChecked(false);
            cb3.setChecked(false);
            cb4.setChecked(true);
            cb5.setChecked(false);
        }else if(mode == 5){
            cb1.setChecked(false);
            cb2.setChecked(false);
            cb3.setChecked(false);
            cb4.setChecked(false);
            cb5.setChecked(true);
        }

    }
    private void onclick(){
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CyroIntelligeetModeSetActivity.this,CyroWorkActivity.class);
                startActivity(intent);
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CyroIntelligeetModeSetActivity.this,CyroWorkActivity.class);
                startActivity(intent);
            }
        });
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb1.setChecked(true);
                    cb2.setChecked(false);
                    cb3.setChecked(false);
                    cb4.setChecked(false);
                    cb5.setChecked(false);
                    if(hand_num == 1) {
                        SetDataBase db = new SetDataBase(CyroIntelligeetModeSetActivity.this);
                        db.updateSingleColumn(1, "flag24", 1);
                    }else if(hand_num == 2){
                        SetDataBase db = new SetDataBase(CyroIntelligeetModeSetActivity.this);
                        db.updateSingleColumn(1, "flag25", 1);
                    }
                }
            }
        });
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb1.setChecked(false);
                    cb2.setChecked(true);
                    cb3.setChecked(false);
                    cb4.setChecked(false);
                    cb5.setChecked(false);
                    if(hand_num == 1) {
                        SetDataBase db = new SetDataBase(CyroIntelligeetModeSetActivity.this);
                        db.updateSingleColumn(1, "flag24", 2);
                    }else if(hand_num == 2){
                        SetDataBase db = new SetDataBase(CyroIntelligeetModeSetActivity.this);
                        db.updateSingleColumn(1, "flag25", 2);
                    }
                }
            }
        });
        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb1.setChecked(false);
                    cb2.setChecked(false);
                    cb3.setChecked(true);
                    cb4.setChecked(false);
                    cb5.setChecked(false);
                    if(hand_num == 1) {
                        SetDataBase db = new SetDataBase(CyroIntelligeetModeSetActivity.this);
                        db.updateSingleColumn(1, "flag24", 3);
                    }else if(hand_num == 2){
                        SetDataBase db = new SetDataBase(CyroIntelligeetModeSetActivity.this);
                        db.updateSingleColumn(1, "flag25", 3);
                    }
                }
            }
        });
        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb1.setChecked(false);
                    cb2.setChecked(false);
                    cb3.setChecked(false);
                    cb4.setChecked(true);
                    cb5.setChecked(false);
                    if(hand_num == 1) {
                        SetDataBase db = new SetDataBase(CyroIntelligeetModeSetActivity.this);
                        db.updateSingleColumn(1, "flag24", 4);
                    }else if(hand_num == 2){
                        SetDataBase db = new SetDataBase(CyroIntelligeetModeSetActivity.this);
                        db.updateSingleColumn(1, "flag25", 4);
                    }
                }
            }
        });
        cb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb1.setChecked(false);
                    cb2.setChecked(false);
                    cb3.setChecked(false);
                    cb4.setChecked(false);
                    cb5.setChecked(true);
                    if(hand_num == 1) {
                        SetDataBase db = new SetDataBase(CyroIntelligeetModeSetActivity.this);
                        db.updateSingleColumn(1, "flag24", 5);
                    }else if(hand_num == 2){
                        SetDataBase db = new SetDataBase(CyroIntelligeetModeSetActivity.this);
                        db.updateSingleColumn(1, "flag25", 5);
                    }
                }
            }
        });
    }
}