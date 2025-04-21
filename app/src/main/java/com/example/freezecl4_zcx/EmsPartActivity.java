package com.example.freezecl4_zcx;

import static com.example.freezecl4_zcx.CheckActivity.gender;
import static com.example.freezecl4_zcx.EmsModeActivity.modePI;
import static com.example.freezecl4_zcx.StartActivity.webSocketService;
import static tools.UIUtils.hideKeyboardAndStatusBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.animation.Animator;
import android.animation.ObjectAnimator;
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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tools.PermissionsDB;
import tools.ShowWifiErrorDialog;

public class EmsPartActivity extends AppCompatActivity {
    private ImageButton bt_return;

    private ImageView imageView4;
    public static int positionData;
    private ImageButton bt_next;
    private PermissionsDB permissionsDB;
    private ImageView iv_position ;
    private ConstraintLayout lin_woman_front;
    private ConstraintLayout lin_woman_back;
    private ConstraintLayout lin_man_front;
    private ConstraintLayout lin_man_back;
    private ImageView iv_woman_glutes;
    private ImageView iv_woman_arm;
    private ImageView iv_woman_abdomen;
    private ImageView iv_woman_thigh;
    private ImageView iv_woman_calf;
    private ImageView iv_woman_back_calf;
    private ImageView iv_woman_back_thigh;
    private ImageView iv_woman_neck;
    private ImageView iv_woman_back;

    private int isFront = 0;  // 0 为 正面， 1 为反面 。
    private AnimationDrawable animation_position ;
    private Button bt_left_switch;
    private Button bt_right_switch;

    private ImageView iv_man_glutes;
    private ImageView iv_man_arm;
    private ImageView iv_man_abdomen;
    private ImageView iv_man_thigh;
    private ImageView iv_man_calf;
    private ImageView iv_man_neck;
    private ImageView iv_man_back;
    private ImageView iv_man_back_thigh;
    private ImageView iv_man_back_calf;

    private LinearLayout lin_left_position;
    private LinearLayout lin_right_position;
    private Button bt_left_position;
    private Button bt_right_position;
    private Button bt_gender ;
    private int lr =  0 ;  // 默认为 左侧 0 ： 左  ：：  1 ：右
    public static int left_position =  11 ; // 默认值
    public static int right_position = 13 ; // 默认值
    private boolean animationControl = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboardAndStatusBar(this);
        setContentView(R.layout.activity_ems_part);
        initDB();
        initView();
        iconInit();
        onclick();
    }
    // 设置部位出现动画
    private void scaleUpAndShow(View view, long duration) {
        // 如果视图已经处于可见状态，但可能需要重置位置
        if (view.getVisibility() == View.VISIBLE) {
            // 如果已经在目标大小附近，则不做任何事
            if (view.getScaleX() > 0.9f) {
                return;
            }
            // 如果视图可见但缩放不正确，重置其状态
            view.clearAnimation();
        }

        // 重置视图状态
        view.setScaleX(0f);
        view.setScaleY(0f);
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);

        // 应用动画
        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(duration)
                .setInterpolator(new OvershootInterpolator(1.0f)) // 回弹效果
                .start();
    }

    private void iconInit() {
        // 默认为女
        gender =  1;
        left_position =  11 ;
        right_position = 13 ;
        lin_left_position.setBackgroundResource(R.drawable.bt_down_position);
        lin_right_position.setBackgroundResource(R.drawable.bt_up_position);
        bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_abdomen);
        bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_thigh);
        if(gender == 0){//男
            iv_position.setBackgroundResource(R.drawable.man_ftob);
            lin_man_front.setVisibility(View.VISIBLE);
            iv_man_abdomen.setBackgroundResource(R.drawable.iv_have_man_abdomen);
        }else if(gender == 1){//女
            iv_position.setBackgroundResource(R.drawable.woman_ftob);
            lin_woman_front.setVisibility(View.VISIBLE);
            iv_woman_abdomen.setBackgroundResource(R.drawable.iv_have_woman_abdomen);
        }
        bt_left_switch.setBackgroundResource(R.drawable.bt_left_down_switch);
        positionData = 1;
    }
    private void initDB() {
        permissionsDB = PermissionsDB.getInstance(this);
        permissionsDB.openWriteDB();
        permissionsDB.openReadDB();
    }
    private void initView(){
        lin_left_position = findViewById(R.id.lin_left_position);
        lin_right_position = findViewById(R.id.lin_right_position);
        bt_left_position = findViewById(R.id.bt_left_position);
        bt_right_position = findViewById(R.id.bt_right_position);
        bt_gender = findViewById(R.id.bt_gender);

        bt_return = findViewById(R.id.bt_return);
        imageView4 = findViewById(R.id.imageView4);
        bt_next = findViewById(R.id.bt_next);
        iv_position = findViewById(R.id.iv_position);
        lin_woman_front = findViewById(R.id.lin_woman_front);
        lin_woman_back = findViewById(R.id.lin_woman_back);

        lin_man_front = findViewById(R.id.lin_man_front);
        lin_man_back = findViewById(R.id.lin_man_back);

        iv_woman_glutes = findViewById(R.id.iv_woman_glutes);
        iv_woman_arm = findViewById(R.id.iv_woman_arm);
        iv_woman_abdomen = findViewById(R.id.iv_woman_abdomen);
        iv_woman_thigh = findViewById(R.id.iv_woman_thigh);
        iv_woman_calf = findViewById(R.id.iv_woman_calf);
        iv_woman_back_calf  = findViewById(R.id.iv_woman_back_calf);
        iv_woman_back_thigh = findViewById(R.id.iv_woman_back_thigh);
        iv_woman_neck = findViewById(R.id.iv_woman_neck);
        iv_woman_back = findViewById(R.id.iv_woman_back);

        bt_left_switch = findViewById(R.id.bt_left_switch);
        bt_right_switch = findViewById(R.id.bt_right_switch);

        iv_man_arm = findViewById(R.id.iv_man_arm);
        iv_man_abdomen = findViewById(R.id.iv_man_abdomen);
        iv_man_calf = findViewById(R.id.iv_man_calf);
        iv_man_glutes = findViewById(R.id.iv_man_glutes);
        iv_man_thigh = findViewById(R.id.iv_man_thigh);
        iv_man_neck = findViewById(R.id.iv_man_neck);
        iv_man_back = findViewById(R.id.iv_man_back);
        iv_man_back_thigh = findViewById(R.id.iv_man_back_thigh);
        iv_man_back_calf = findViewById(R.id.iv_man_back_calf);


    }
    private void onclick(){
        bt_left_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFront == 1 ){
                    bt_left_switch.setBackgroundResource(R.drawable.bt_left_down_switch);
                    bt_right_switch.setBackgroundResource(R.drawable.bt_right_switch);
                    if(gender == 0 ){
                        lin_man_back.setVisibility(View.GONE);
                    }
                    else {
                        lin_woman_back.setVisibility(View.GONE);
                    }
                    if(gender == 0){
                        iv_position.setBackgroundResource(R.drawable.man_btof);
                    }
                    else {
                        iv_position.setBackgroundResource(R.drawable.woman_btof);
                    }
                    animation_position = (AnimationDrawable) iv_position.getBackground();
                    animation_position.start();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(gender == 0 ){
                                scaleUpAndShow(lin_man_front,150);
//                                animate_show(lin_man_front);
                            }
                            else {
                                scaleUpAndShow(lin_woman_front,150);
//                                animate_show(lin_woman_front);
                            }
                        }
                    },1000);
                    isFront =  0 ;
                }
            }
        });
        bt_right_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFront == 0 ){
                    bt_right_switch.setBackgroundResource(R.drawable.bt_right_down_switch);
                    bt_left_switch.setBackgroundResource(R.drawable.bt_left_switch);
                    if(gender == 0 ){
                        lin_man_front.setVisibility(View.GONE);
                    }
                    else {
                        lin_woman_front.setVisibility(View.GONE);
                    }
                    if(gender == 0){
                        iv_position.setBackgroundResource(R.drawable.man_ftob);
                    }
                    else {
                        iv_position.setBackgroundResource(R.drawable.woman_ftob);
                    }
                    animation_position = (AnimationDrawable) iv_position.getBackground();
                    animation_position.start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(gender == 0 ){
                                scaleUpAndShow(lin_man_back,150);
                            }
                            else {
                                scaleUpAndShow(lin_woman_back,150);
                            }
                        }
                    },1000);
                    isFront = 1 ;
                }
            }
        });
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToServer("page_ems_gender_ok");
                Intent intent = new Intent(EmsPartActivity.this, EmsModeActivity.class);
                startActivity(intent);
            }
        });
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modePI == 1) {
                    sendMessageToServer("page_ems_work_p_ok");
                    Intent intent = new Intent(EmsPartActivity.this, EmsProfessionalModeActivity.class);
                    startActivity(intent);
                }else if(modePI == 2){
                    sendMessageToServer("page_ems_work_i_ok");
                    Intent intent = new Intent(EmsPartActivity.this, EmsIntelligeetModeActivity.class);
                    startActivity(intent);
                }
            }
        });
        iv_woman_arm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionData = 2 ;
                if(lr == 0 ){
                    left_position = 10 ;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 10 ;
                    switchPositionImg(right_position);
                }
                if(lr == 0){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_arm);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_arm);
                }
            }
        });
        iv_woman_abdomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lr == 0){
                    left_position = 11 ;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 11 ;
                    switchPositionImg(right_position);
                }
                positionData = 1 ;
                if(lr == 0 ){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_abdomen);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_abdomen);
                }
            }
        });
        iv_woman_glutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lr == 0 ){
                    left_position = 12;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 12 ;
                    switchPositionImg(right_position);
                }
                positionData = 1 ;
                if(lr == 0 ){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_glutes);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_glutes);
                }
            }
        });
        iv_woman_thigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lr == 0 ){
                    left_position = 13 ;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 13 ;
                    switchPositionImg(right_position);

                }
                positionData = 1 ;
                if(lr == 0 ){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_thigh);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_thigh);
                }
            }
        });
        iv_woman_calf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lr == 0 ){
                    left_position = 14 ;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 14 ;
                    switchPositionImg(right_position);
                }
                positionData = 2 ;

                if(lr == 0 ){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_calf);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_calf);
                }
            }
        });
        iv_woman_neck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lr == 0 ){
                    left_position = 15 ;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 15 ;
                    switchPositionImg(right_position);
                }
                positionData = 2 ;
                if(lr == 0 ){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_neck);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_neck);
                }
            }
        });
        iv_woman_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lr == 0 ){
                    left_position =16 ;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 16;
                    switchPositionImg(right_position);
                }
                positionData = 2 ;
                if(lr == 0 ){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_back);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_back);
                }
            }
        });
        iv_woman_back_thigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lr == 0 ){
                    left_position = 17;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 17;
                    switchPositionImg(right_position);
                }
                positionData = 2 ;
                if(lr == 0 ){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_back_thigh);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_back_thigh);
                }
            }
        });
        iv_woman_back_calf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lr == 0 ){
                    left_position = 18;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 18 ;
                    switchPositionImg(right_position);
                }
                positionData = 2 ;
                if(lr == 0){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_back_calf);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_back_calf);
                }
            }
        });


        iv_man_arm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionData = 2 ;
                if(lr == 0 ){
                    left_position = 1 ;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 1 ;
                    switchPositionImg(right_position);
                }
                if(lr == 0){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_man_arm);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_man_arm);
                }
            }
        });
        iv_man_abdomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionData = 1 ;
                if(lr == 0 ){
                    left_position = 2 ;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 2 ;
                    switchPositionImg(right_position);
                }
                if(lr == 0 ){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_man_abdomen);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_man_abdomen);
                }
            }
        });
        iv_man_glutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionData = 1 ;
                if(lr == 0 ){
                    left_position = 3 ;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 3 ;
                    switchPositionImg(right_position);
                }
                if(lr == 0 ){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_man_glutes);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_man_glutes);
                }
            }
        });
        iv_man_thigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionData = 1 ;
                if(lr == 0 ){
                    left_position = 4;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 4 ;
                    switchPositionImg(right_position);
                }
                if(lr == 0 ){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_man_thigh);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_man_thigh);
                }
            }
        });
        iv_man_calf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionData = 2 ;
                if(lr == 0 ){
                    left_position  = 5 ;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 5 ;
                    switchPositionImg(right_position);
                }
                if(lr ==  0  ) {
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_man_calf);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_man_calf);
                }
            }
        });
        iv_man_neck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lr == 0 ){
                    left_position = 6 ;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 6 ;
                    switchPositionImg(right_position);
                }
                positionData = 2 ;
                if(lr == 0 ){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_man_neck);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_man_neck);
                }
            }
        });
        iv_man_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lr == 0){
                    left_position = 7 ;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 7 ;
                    switchPositionImg(right_position);
                }
                positionData = 2 ;
                if(lr == 0 ){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_man_back);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_man_back);
                }
            }
        });
        iv_man_back_thigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lr == 0 ){
                    left_position = 8 ;
                    switchPositionImg(left_position);
                }
                else {
                    right_position =  8 ;
                    switchPositionImg(right_position);
                }
                positionData = 2 ;
                if(lr == 0 ){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_man_back_thigh);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_man_back_thigh);
                }
            }
        });
        iv_man_back_calf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lr == 0 ){
                    left_position = 9 ;
                    switchPositionImg(left_position);
                }
                else {
                    right_position = 9 ;
                    switchPositionImg(right_position);
                }
                positionData = 2 ;
                if(lr == 0 ){
                    bt_left_position.setBackgroundResource(R.drawable.iv_position_man_back_calf);
                }
                else {
                    bt_right_position.setBackgroundResource(R.drawable.iv_position_man_back_calf);
                }
            }
        });
        bt_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gender == 1 ){
                    gender = 0 ;
                    bt_gender.setBackgroundResource(R.drawable.bt_man_switch);
                    if(isFront == 0 ){
                        iv_position.setBackgroundResource(R.drawable.man_ftob);
                        lin_man_front.setVisibility(View.VISIBLE);
                        lin_woman_front.setVisibility(View.GONE);
                        left_position = left_position > 9 ? left_position - 9:left_position;
                        right_position = right_position > 9 ? right_position - 9 : right_position;
                        switchGender(left_position,0);
                        switchGender(right_position, 1);
                    }
                    else {
                        iv_position.setBackgroundResource(R.drawable.man_btof);
                        lin_man_back.setVisibility(View.VISIBLE);
                        lin_woman_back.setVisibility(View.GONE);
                        left_position = left_position > 9 ? left_position - 9 : left_position;
                        right_position = right_position > 9 ? right_position - 9 : right_position;
                        switchGender(left_position,0);
                        switchGender(right_position, 1);
                    }
                }
                else {
                    gender =  1 ;
                    bt_gender.setBackgroundResource(R.drawable.bt_woman_switch);
                    if(isFront ==  0 ){
                        iv_position.setBackgroundResource(R.drawable.woman_ftob);
                        lin_man_front.setVisibility(View.GONE);
                        lin_woman_front.setVisibility(View.VISIBLE);
                        left_position = left_position <= 9 ? left_position + 9 : left_position;
                        right_position = right_position <= 9 ? right_position + 9 : right_position;
                        switchGender(left_position,0);
                        switchGender(right_position, 1);
                    }
                    else {
                        iv_position.setBackgroundResource(R.drawable.woman_btof);
                        lin_man_back.setVisibility(View.GONE);
                        lin_woman_back.setVisibility(View.VISIBLE);
                        left_position = left_position <= 9 ? left_position + 9 : left_position ;
                        right_position = right_position <= 9 ? right_position + 9 : right_position;
                        switchGender(left_position,0);
                        switchGender(right_position, 1);
                    }
                }
                if(lr == 0 ){
                    switchPositionImg(left_position);
                }
                else{
                    switchPositionImg(right_position);
                }
                System.out.println("bt_gender   : " + left_position +"   " + right_position);
            }
        });
        bt_left_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lr = 0 ;
                lin_left_position.setBackgroundResource(R.drawable.bt_down_position);
                lin_right_position.setBackgroundResource(R.drawable.bt_up_position);
                switch_position(left_position);
            }
        });
        bt_right_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lr = 1 ;
                lin_left_position.setBackgroundResource(R.drawable.bt_up_position);
                lin_right_position.setBackgroundResource(R.drawable.bt_down_position);
                switch_position(right_position);
            }
        });
    }
    private void sendMessageToServer(String message) {
        if (webSocketService != null) {
            webSocketService.sendMessage(message);
        }
    }
    /*
        点击下方部位选择后，部位会变成当前选择的部位
     */
    private void switch_position(int position){
        if(position == 3 || position == 12 || position >5 && position <10 || position >14 ){
            System.out.println("position   ;:: " + position + "   " + isFront  + gender);

            if(isFront == 0 ){
                bt_right_switch.setBackgroundResource(R.drawable.bt_right_down_switch);
                bt_left_switch.setBackgroundResource(R.drawable.bt_left_switch);
                if(gender == 0 ){
                    lin_man_front.setVisibility(View.GONE);
                }
                else {
                    lin_woman_front.setVisibility(View.GONE);
                }
                if(gender == 0){
                    iv_position.setBackgroundResource(R.drawable.man_ftob);
                }
                else {
                    iv_position.setBackgroundResource(R.drawable.woman_ftob);
                }
                animation_position = (AnimationDrawable) iv_position.getBackground();
                animation_position.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(gender == 0 ){
                            scaleUpAndShow(lin_man_back,150);
                        }
                        else {
                            scaleUpAndShow(lin_woman_back,150);
                        }
                    }
                },1000);
                isFront = 1 ;
            }
        }
        else {
            if(isFront == 1 ){
                bt_left_switch.setBackgroundResource(R.drawable.bt_left_down_switch);
                bt_right_switch.setBackgroundResource(R.drawable.bt_right_switch);
                if(gender == 0 ){
                    lin_man_back.setVisibility(View.GONE);
                }
                else {
                    lin_woman_back.setVisibility(View.GONE);
                }
                if(gender == 0){
                    iv_position.setBackgroundResource(R.drawable.man_btof);
                }
                else {
                    iv_position.setBackgroundResource(R.drawable.woman_btof);
                }
                animation_position = (AnimationDrawable) iv_position.getBackground();
                animation_position.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(gender == 0 ){
                            scaleUpAndShow(lin_man_front,150);
                        }
                        else {
                            scaleUpAndShow(lin_woman_front,150);
                        }
                    }
                },1000);
                isFront =  0 ;
            }
        }
        switchPositionImg(position);
    }
    /*
        点击性别后，下方部位会变成相对应的性别部位。
     */
    private void switchGender(int position , int lr){
        if(position == 1 ){
            if(lr == 0 ){
                bt_left_position.setBackgroundResource(R.drawable.iv_position_man_arm);
            }
            else {
                bt_right_position.setBackgroundResource(R.drawable.iv_position_man_arm);
            }

        }
        else if (position == 2 ) {
           if(lr == 0){
               bt_left_position.setBackgroundResource(R.drawable.iv_position_man_abdomen);
           }
           else {
               bt_right_position.setBackgroundResource(R.drawable.iv_position_man_abdomen);
           }
        }
        else if(position == 3 ){
            if(lr == 0 ){
                bt_left_position.setBackgroundResource(R.drawable.iv_position_man_glutes);
            }
            else {
                bt_right_position.setBackgroundResource(R.drawable.iv_position_man_glutes);
            }
        }
        else if (position == 4 ){
            if(lr == 0 ){
                bt_left_position.setBackgroundResource(R.drawable.iv_position_man_thigh);
            }
            else {
                bt_right_position.setBackgroundResource(R.drawable.iv_position_man_thigh);
            }
        }
        else if (position == 5 ){
            if(lr == 0 ){
                bt_left_position.setBackgroundResource(R.drawable.iv_position_man_calf);
            }
            else {
                bt_right_position.setBackgroundResource(R.drawable.iv_position_man_calf);
            }
        }
        else if (position == 6 ){
            if(lr == 0 ){
                bt_left_position.setBackgroundResource(R.drawable.iv_position_man_neck);
            }
            else {
                bt_right_position.setBackgroundResource(R.drawable.iv_position_man_neck);
            }
        }
        else if (position == 7 ){
            if(lr == 0 ){
                bt_left_position.setBackgroundResource(R.drawable.iv_position_man_back);
            }
            else {
                bt_right_position.setBackgroundResource(R.drawable.iv_position_man_back);
            }
        }
        else if (position == 8){
            if(lr == 0 ){
                bt_left_position.setBackgroundResource(R.drawable.iv_position_man_back_thigh);
            }
            else {
                bt_left_position.setBackgroundResource(R.drawable.iv_position_man_back_thigh);
            }
        }
        else if (position == 9 ){
            if(lr== 0 ){
                bt_left_position.setBackgroundResource(R.drawable.iv_position_man_back_calf);
            }
            else {
                bt_right_position.setBackgroundResource(R.drawable.iv_position_man_back_calf);
            }
        }




        if(position == 10 ){
            if(lr == 0 ){
                bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_arm);
            }
            else {
                bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_arm);
            }
        }
        else if (position == 11 ) {
           if(lr == 0 ){
               bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_abdomen);
           }
           else {
               bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_abdomen);
           }
        }
        else if(position == 12 ){
            if(lr == 0 ){
                bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_glutes);
            }
            else {
                bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_glutes);
            }
        }
        else if (position == 13){
            if(lr == 0 ){
                bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_thigh);
            }
            else {
                bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_thigh);
            }
        }
        else if (position == 14 ){
            if(lr == 0 ){
                bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_calf);
            }
            else {
                bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_calf);
            }
        }
        else if (position == 15 ){
            if(lr == 0 ){
                bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_neck);
            }
            else {
                bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_neck);
            }
        }
        else if (position == 16 ){
            if(lr == 0 ){
                bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_back);
            }
            else {
                bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_back);
            }
        }
        else if (position == 17){
            if(lr == 0 ){
                bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_back_thigh);
            }
            else {
                bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_back_thigh);
            }
        }
        else if (position == 18){
            if(lr == 0 ){
                bt_left_position.setBackgroundResource(R.drawable.iv_position_woman_back_calf);
            }
            else {
                bt_right_position.setBackgroundResource(R.drawable.iv_position_woman_back_calf);
            }
        }

    }
    /*
        * 点击性别后，当前选中的部位
     */
    private void switchPositionImg(int position){
        if(position == 1 ){
            iv_man_arm.setBackgroundResource(R.drawable.iv_have_man_arm);
            iv_man_abdomen.setBackgroundResource(R.drawable.iv_man_abdomen);
            iv_man_glutes.setBackgroundResource(R.drawable.iv_man_glutes);
            iv_man_thigh.setBackgroundResource(R.drawable.iv_man_thigh);
            iv_man_calf.setBackgroundResource(R.drawable.iv_man_calf);
            iv_man_neck.setBackgroundResource(R.drawable.iv_man_neck);
            iv_man_back.setBackgroundResource(R.drawable.iv_man_back);
            iv_man_back_thigh.setBackgroundResource(R.drawable.iv_man_thigh);
            iv_man_back_calf.setBackgroundResource(R.drawable.iv_man_calf);

        }
        else if (position == 2 ) {
            iv_man_arm.setBackgroundResource(R.drawable.iv_man_arm);
            iv_man_abdomen.setBackgroundResource(R.drawable.iv_have_man_abdomen);
            iv_man_glutes.setBackgroundResource(R.drawable.iv_man_glutes);
            iv_man_thigh.setBackgroundResource(R.drawable.iv_man_thigh);
            iv_man_calf.setBackgroundResource(R.drawable.iv_man_calf);
            iv_man_neck.setBackgroundResource(R.drawable.iv_man_neck);
            iv_man_back.setBackgroundResource(R.drawable.iv_man_back);
            iv_man_back_thigh.setBackgroundResource(R.drawable.iv_man_thigh);
            iv_man_back_calf.setBackgroundResource(R.drawable.iv_man_calf);

        }
        else if(position == 3 ){
            iv_man_arm.setBackgroundResource(R.drawable.iv_man_arm);
            iv_man_abdomen.setBackgroundResource(R.drawable.iv_man_abdomen);
            iv_man_glutes.setBackgroundResource(R.drawable.iv_have_man_glutes);
            iv_man_thigh.setBackgroundResource(R.drawable.iv_man_thigh);
            iv_man_calf.setBackgroundResource(R.drawable.iv_man_calf);
            iv_man_neck.setBackgroundResource(R.drawable.iv_man_neck);
            iv_man_back.setBackgroundResource(R.drawable.iv_man_back);
            iv_man_back_thigh.setBackgroundResource(R.drawable.iv_man_thigh);
            iv_man_back_calf.setBackgroundResource(R.drawable.iv_man_calf);
        }
        else if (position == 4 ){
            iv_man_arm.setBackgroundResource(R.drawable.iv_man_arm);
            iv_man_abdomen.setBackgroundResource(R.drawable.iv_man_abdomen);
            iv_man_glutes.setBackgroundResource(R.drawable.iv_man_glutes);
            iv_man_thigh.setBackgroundResource(R.drawable.iv_have_man_thigh);
            iv_man_calf.setBackgroundResource(R.drawable.iv_man_calf);
            iv_man_back.setBackgroundResource(R.drawable.iv_man_back);
            iv_man_back_thigh.setBackgroundResource(R.drawable.iv_man_thigh);
            iv_man_back_calf.setBackgroundResource(R.drawable.iv_man_calf);
        }
        else if (position == 5 ){
            iv_man_arm.setBackgroundResource(R.drawable.iv_man_arm);
            iv_man_abdomen.setBackgroundResource(R.drawable.iv_man_abdomen);
            iv_man_glutes.setBackgroundResource(R.drawable.iv_man_glutes);
            iv_man_thigh.setBackgroundResource(R.drawable.iv_man_thigh);
            iv_man_calf.setBackgroundResource(R.drawable.iv_have_man_calf);
            iv_man_neck.setBackgroundResource(R.drawable.iv_man_neck);
            iv_man_back.setBackgroundResource(R.drawable.iv_man_back);
            iv_man_back_thigh.setBackgroundResource(R.drawable.iv_man_thigh);
            iv_man_back_calf.setBackgroundResource(R.drawable.iv_man_calf);
        }
        else if (position == 6 ){
            iv_man_arm.setBackgroundResource(R.drawable.iv_man_arm);
            iv_man_abdomen.setBackgroundResource(R.drawable.iv_man_abdomen);
            iv_man_glutes.setBackgroundResource(R.drawable.iv_man_glutes);
            iv_man_thigh.setBackgroundResource(R.drawable.iv_man_thigh);
            iv_man_calf.setBackgroundResource(R.drawable.iv_man_calf);
            iv_man_neck.setBackgroundResource(R.drawable.iv_have_man_neck);
            iv_man_back.setBackgroundResource(R.drawable.iv_man_back);
            iv_man_back_thigh.setBackgroundResource(R.drawable.iv_man_thigh);
            iv_man_back_calf.setBackgroundResource(R.drawable.iv_man_calf);
        }
        else if (position == 7){
            iv_man_arm.setBackgroundResource(R.drawable.iv_man_arm);
            iv_man_abdomen.setBackgroundResource(R.drawable.iv_man_abdomen);
            iv_man_glutes.setBackgroundResource(R.drawable.iv_man_glutes);
            iv_man_thigh.setBackgroundResource(R.drawable.iv_man_thigh);
            iv_man_calf.setBackgroundResource(R.drawable.iv_man_calf);
            iv_man_neck.setBackgroundResource(R.drawable.iv_man_neck);
            iv_man_back.setBackgroundResource(R.drawable.iv_have_man_back);
            iv_man_back_thigh.setBackgroundResource(R.drawable.iv_man_thigh);
            iv_man_back_calf.setBackgroundResource(R.drawable.iv_man_calf);
        }
        else if (position == 8  ){
            iv_man_arm.setBackgroundResource(R.drawable.iv_man_arm);
            iv_man_abdomen.setBackgroundResource(R.drawable.iv_man_abdomen);
            iv_man_glutes.setBackgroundResource(R.drawable.iv_man_glutes);
            iv_man_thigh.setBackgroundResource(R.drawable.iv_man_thigh);
            iv_man_calf.setBackgroundResource(R.drawable.iv_man_calf);
            iv_man_neck.setBackgroundResource(R.drawable.iv_man_neck);
            iv_man_back.setBackgroundResource(R.drawable.iv_man_back);
            iv_man_back_thigh.setBackgroundResource(R.drawable.iv_have_man_thigh);
            iv_man_back_calf.setBackgroundResource(R.drawable.iv_man_calf);
        }
        else if (position == 9 ){
            iv_man_arm.setBackgroundResource(R.drawable.iv_man_arm);
            iv_man_abdomen.setBackgroundResource(R.drawable.iv_man_abdomen);
            iv_man_glutes.setBackgroundResource(R.drawable.iv_man_glutes);
            iv_man_thigh.setBackgroundResource(R.drawable.iv_man_thigh);
            iv_man_calf.setBackgroundResource(R.drawable.iv_man_calf);
            iv_man_neck.setBackgroundResource(R.drawable.iv_man_neck);
            iv_man_back.setBackgroundResource(R.drawable.iv_man_back);
            iv_man_back_thigh.setBackgroundResource(R.drawable.iv_man_thigh);
            iv_man_back_calf.setBackgroundResource(R.drawable.iv_have_man_calf);
        }

        if(position == 10 ){
            iv_woman_arm.setBackgroundResource(R.drawable.iv_have_woman_arm);
            iv_woman_abdomen.setBackgroundResource(R.drawable.iv_woman_abdomen);
            iv_woman_glutes.setBackgroundResource(R.drawable.iv_woman_glutes);
            iv_woman_thigh.setBackgroundResource(R.drawable.iv_woman_thigh);
            iv_woman_calf.setBackgroundResource(R.drawable.iv_woman_calf);
            iv_woman_neck.setBackgroundResource(R.drawable.iv_woman_neck);
            iv_woman_back.setBackgroundResource(R.drawable.iv_woman_back);
            iv_woman_back_thigh.setBackgroundResource(R.drawable.iv_woman_thigh);
            iv_woman_back_calf.setBackgroundResource(R.drawable.iv_woman_calf);
        }

        else if (position == 11 ) {
            iv_woman_arm.setBackgroundResource(R.drawable.iv_woman_arm);
            iv_woman_abdomen.setBackgroundResource(R.drawable.iv_have_woman_abdomen);
            iv_woman_glutes.setBackgroundResource(R.drawable.iv_woman_glutes);
            iv_woman_thigh.setBackgroundResource(R.drawable.iv_woman_thigh);
            iv_woman_calf.setBackgroundResource(R.drawable.iv_woman_calf);
            iv_woman_neck.setBackgroundResource(R.drawable.iv_woman_neck);
            iv_woman_back.setBackgroundResource(R.drawable.iv_woman_back);
            iv_woman_back_thigh.setBackgroundResource(R.drawable.iv_woman_thigh);
            iv_woman_back_calf.setBackgroundResource(R.drawable.iv_woman_calf);
        }
        else if(position == 12 ){
            iv_woman_arm.setBackgroundResource(R.drawable.iv_woman_arm);
            iv_woman_abdomen.setBackgroundResource(R.drawable.iv_woman_abdomen);
            iv_woman_glutes.setBackgroundResource(R.drawable.iv_have_woman_glutes);
            iv_woman_thigh.setBackgroundResource(R.drawable.iv_woman_thigh);
            iv_woman_calf.setBackgroundResource(R.drawable.iv_woman_calf);
            iv_woman_neck.setBackgroundResource(R.drawable.iv_woman_neck);
            iv_woman_back.setBackgroundResource(R.drawable.iv_woman_back);
            iv_woman_back_thigh.setBackgroundResource(R.drawable.iv_woman_thigh);
            iv_woman_back_calf.setBackgroundResource(R.drawable.iv_woman_calf);
        }
        else if (position == 13 ){
            iv_woman_arm.setBackgroundResource(R.drawable.iv_woman_arm);
            iv_woman_abdomen.setBackgroundResource(R.drawable.iv_woman_abdomen);
            iv_woman_glutes.setBackgroundResource(R.drawable.iv_woman_glutes);
            iv_woman_thigh.setBackgroundResource(R.drawable.iv_have_woman_thigh);
            iv_woman_calf.setBackgroundResource(R.drawable.iv_woman_calf);
            iv_woman_neck.setBackgroundResource(R.drawable.iv_woman_neck);
            iv_woman_back.setBackgroundResource(R.drawable.iv_woman_back);
            iv_woman_back_thigh.setBackgroundResource(R.drawable.iv_woman_thigh);
            iv_woman_back_calf.setBackgroundResource(R.drawable.iv_woman_calf);
        }
        else if (position == 14 ){
            iv_woman_arm.setBackgroundResource(R.drawable.iv_woman_arm);
            iv_woman_abdomen.setBackgroundResource(R.drawable.iv_woman_abdomen);
            iv_woman_glutes.setBackgroundResource(R.drawable.iv_woman_glutes);
            iv_woman_thigh.setBackgroundResource(R.drawable.iv_woman_thigh);
            iv_woman_calf.setBackgroundResource(R.drawable.iv_have_woman_calf);
            iv_woman_neck.setBackgroundResource(R.drawable.iv_woman_neck);
            iv_woman_back.setBackgroundResource(R.drawable.iv_woman_back);
            iv_woman_back_thigh.setBackgroundResource(R.drawable.iv_woman_thigh);
            iv_woman_back_calf.setBackgroundResource(R.drawable.iv_woman_calf);
        }
        else if (position == 15 ){
            iv_woman_arm.setBackgroundResource(R.drawable.iv_woman_arm);
            iv_woman_abdomen.setBackgroundResource(R.drawable.iv_woman_abdomen);
            iv_woman_glutes.setBackgroundResource(R.drawable.iv_woman_glutes);
            iv_woman_thigh.setBackgroundResource(R.drawable.iv_woman_thigh);
            iv_woman_calf.setBackgroundResource(R.drawable.iv_woman_calf);
            iv_woman_neck.setBackgroundResource(R.drawable.iv_have_woman_neck);
            iv_woman_back.setBackgroundResource(R.drawable.iv_woman_back);
            iv_woman_back_thigh.setBackgroundResource(R.drawable.iv_woman_thigh);
            iv_woman_back_calf.setBackgroundResource(R.drawable.iv_woman_calf);
        }
        else if (position == 16 ){
            iv_woman_arm.setBackgroundResource(R.drawable.iv_woman_arm);
            iv_woman_abdomen.setBackgroundResource(R.drawable.iv_woman_abdomen);
            iv_woman_glutes.setBackgroundResource(R.drawable.iv_woman_glutes);
            iv_woman_thigh.setBackgroundResource(R.drawable.iv_woman_thigh);
            iv_woman_calf.setBackgroundResource(R.drawable.iv_woman_calf);
            iv_woman_neck.setBackgroundResource(R.drawable.iv_woman_neck);
            iv_woman_back.setBackgroundResource(R.drawable.iv_have_woman_back);
            iv_woman_back_thigh.setBackgroundResource(R.drawable.iv_woman_thigh);
            iv_woman_back_calf.setBackgroundResource(R.drawable.iv_woman_calf);
        }
        else if (position == 17){
            iv_woman_arm.setBackgroundResource(R.drawable.iv_woman_arm);
            iv_woman_abdomen.setBackgroundResource(R.drawable.iv_woman_abdomen);
            iv_woman_glutes.setBackgroundResource(R.drawable.iv_woman_glutes);
            iv_woman_thigh.setBackgroundResource(R.drawable.iv_woman_thigh);
            iv_woman_calf.setBackgroundResource(R.drawable.iv_woman_calf);
            iv_woman_neck.setBackgroundResource(R.drawable.iv_woman_neck);
            iv_woman_back.setBackgroundResource(R.drawable.iv_woman_back);
            iv_woman_back_thigh.setBackgroundResource(R.drawable.iv_have_woman_thigh);
            iv_woman_back_calf.setBackgroundResource(R.drawable.iv_woman_calf);
        }
        else if (position == 18){
            iv_woman_arm.setBackgroundResource(R.drawable.iv_woman_arm);
            iv_woman_abdomen.setBackgroundResource(R.drawable.iv_woman_abdomen);
            iv_woman_glutes.setBackgroundResource(R.drawable.iv_woman_glutes);
            iv_woman_thigh.setBackgroundResource(R.drawable.iv_woman_thigh);
            iv_woman_calf.setBackgroundResource(R.drawable.iv_woman_calf);
            iv_woman_neck.setBackgroundResource(R.drawable.iv_woman_neck);
            iv_woman_back.setBackgroundResource(R.drawable.iv_woman_back);
            iv_woman_back_thigh.setBackgroundResource(R.drawable.iv_woman_thigh);
            iv_woman_back_calf.setBackgroundResource(R.drawable.iv_have_woman_calf);
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
                ShowWifiErrorDialog.showWifiError(EmsPartActivity.this,getWindowManager());
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
            System.out.println("EmsGenderActivity " + " onReceive : " + message);
            if(message.startsWith("click_part")){ // 选择部位
                int p = Integer.valueOf(message.substring("click_part".length()));
                getPhonePart(p);
            }
            else if ("page_ems_gender".equals(message)){
                sendMessageToServer("page_ems_gender_ok");
                Intent intent1 = new Intent(EmsPartActivity.this, EmsModeActivity.class);
                startActivity(intent1);
            }
            else if ("page_ems_work_p".equals(message)){
                if(permissionsDB.queryFlag("flag1") == 0){
                    showError(getString(R.string.error1));
                    sendMessageToServer("error1");
                }
                else if (permissionsDB.queryFlag("timing") == 0 && permissionsDB.queryFlag("timingSecond") == 0){
                    showError(getString(R.string.error2));
                    sendMessageToServer("error2");
                }else {
                    if (modePI == 1) {
                        sendMessageToServer("page_ems_work_p_ok");
                        Intent intent1 = new Intent(EmsPartActivity.this, EmsProfessionalModeActivity.class);
                        startActivity(intent1);
                    } else if (modePI == 2) {
                        sendMessageToServer("page_ems_work_i_ok");
                        Intent intent1 = new Intent(EmsPartActivity.this, EmsIntelligeetModeActivity.class);
                        startActivity(intent1);
                    }
                }
            }
            else if ("page_ems_work_i".equals(message)){
                if(permissionsDB.queryFlag("flag1") == 0){
                    showError(getString(R.string.error1));
                    sendMessageToServer("error1");
                }
                else if (permissionsDB.queryFlag("timing") == 0 && permissionsDB.queryFlag("timingSecond") == 0){
                    showError(getString(R.string.error2));
                    sendMessageToServer("error2");
                }else {
                    if (modePI == 1) {
                        sendMessageToServer("page_ems_work_p_ok");
                        Intent intent1 = new Intent(EmsPartActivity.this, EmsProfessionalModeActivity.class);
                        startActivity(intent1);
                    } else if (modePI == 2) {
                        sendMessageToServer("page_ems_work_i_ok");
                        Intent intent1 = new Intent(EmsPartActivity.this, EmsIntelligeetModeActivity.class);
                        startActivity(intent1);
                    }
                }
            }
            else if ("Which_page".equals(message)){
                sendMessageToServer("jump_part_m");
            }
        }
    };
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
    private  void  getPhonePart(Integer positionData){
        if(positionData == 1 ){
            this.positionData = 1;
        }
        else if (positionData == 2 ){
            this.positionData = 1;
        }
        else if (positionData == 3 ){
            this.positionData = 1;
        }
        else if (positionData == 4 ){
            this.positionData = 1;
        }
        else if (positionData == 5 ){
            this.positionData = 2;
        }
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


//    private void animate_show(View view){
//        if(view.getVisibility() == View.GONE){
//            // 设置初始位置为屏幕底部
//            view.setVisibility(View.VISIBLE);
//            view.setTranslationY(getScreenHeight());
//            ObjectAnimator animator = ObjectAnimator.ofFloat(view,"translationY",0f);
//            animator.setDuration(1000);
//            animator.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(@NonNull Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(@NonNull Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationCancel(@NonNull Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(@NonNull Animator animator) {
//
//                }
//            });
//            animator.start();
//        }
//    }
//    private void animate_hide(View view){
//        System.out.println(" position  旋转   ：： ");
//        if(view.getVisibility() == View.VISIBLE){
//            // 如果已经可见， 则隐藏布局
//            ObjectAnimator animator = ObjectAnimator.ofFloat(view,"translationY",getScreenHeight());
//            animator.setDuration(650);
//            animator.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(@NonNull Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(@NonNull Animator animator) {
//                    view.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onAnimationCancel(@NonNull Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(@NonNull Animator animator) {
//
//                }
//            });
//            animator.start();
//        }
//
//    }
//    private int getScreenHeight() {
//        return getResources().getDisplayMetrics().heightPixels;
//    }
}