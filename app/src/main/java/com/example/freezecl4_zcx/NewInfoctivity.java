package com.example.freezecl4_zcx;

import static com.example.freezecl4_zcx.InformationServiceActivity.formatDateString;
import static com.example.freezecl4_zcx.StartActivity.cpuid;
import static com.example.freezecl4_zcx.StartActivity.webSocketService;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Calendar;
import java.util.Objects;

import db.UploadInfoDB;
import db.UserInfoDatabase;
import entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.UserService;
import service.api.UserAPI;
import tools.ShowWifiErrorDialog;

public class NewInfoctivity extends AppCompatActivity {
    private int optionID;
    private CheckBox cb_man;
    private CheckBox cb_woman;
    private Button bt_return;
    private Button bt_next;
    private TextView tv_id_edit;
    private EditText et_firstName_edit;
    private EditText et_lastName_edit;
    private EditText et_age_edit;
    private EditText et_great_date_edit;
    private int man_woman = 0;//0男1女
    public static String year = "2023";
    public static String month = "1";
    public static String day = "1";
    public static String systemTime = "01/01/2023";
    public static String systemTimeNum = "01012023";
    private Cursor cursor;
    private int count = 0; //DBHelper_user_custom 数据总量
    private ImageView iv_background;
    private EditText et_phone;
    private EditText et_e_mail;
    private UserInfoDatabase  userInfoDatabase;
    int currentID  = 0;
    private ImageButton bt_date;
    private UploadInfoDB uploadInfoDB = null;

    private Boolean isSendE_mail = true;
    private Boolean isSendPhone = true;
    private Boolean isSendDate = true;

    private Boolean isSendFirstName=true;
    private Boolean isSendLastName=true;
    private Boolean isSendAge=true;
    private Boolean isSendGender =  true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inset();
        setContentView(R.layout.activity_new_infoctivity);
        initDB();
        initView();
        date();
        select_db_max_id();
        onclick();

    }
    private final  BroadcastReceiver getMessageReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            et_great_date_edit.setText(formatDateString(message));
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
                ShowWifiErrorDialog.showWifiError(NewInfoctivity.this,getWindowManager());
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



    private void initDB() {
        userInfoDatabase = UserInfoDatabase.getInstance(this);
        userInfoDatabase.openWriteDB();
        userInfoDatabase.openReadDB();

        uploadInfoDB = UploadInfoDB.getInstance(this);
        uploadInfoDB.openReadDB();
        uploadInfoDB.openWriteDB();
    }

    private void initView(){
        cb_man = findViewById(R.id.cb_man);
        cb_woman = findViewById(R.id.cb_woman);
        bt_return = findViewById(R.id.bt_return);
        bt_next = findViewById(R.id.bt_next);
        tv_id_edit = findViewById(R.id.tv_id_edit);
        et_firstName_edit = findViewById(R.id.et_firstName_edit);
        et_lastName_edit = findViewById(R.id.et_lastName_edit);
        et_age_edit = findViewById(R.id.et_age_edit);
        et_great_date_edit = findViewById(R.id.et_great_date_edit);
        iv_background = findViewById(R.id.iv_background);
        cb_man.setChecked(true);
        et_phone = findViewById(R.id.et_phone_num);
        et_e_mail = findViewById(R.id.et_e_mail);
        bt_date = findViewById(R.id.bt_date);
    }
    @SuppressLint("Range")
    private void select_db_max_id(){
        int userID = getIntent().getIntExtra("userID", 0);
        if(userID>0){
            optionID = userID;
            User user = userInfoDatabase.queryUserOne(String.valueOf(userID));
            System.out.println("getUser : "+user);
            tv_id_edit.setText(String.valueOf(getIntent().getIntExtra("positionID",0)));
            et_age_edit.setText(String.valueOf(user.getAge()));
            if(user.isGender()){
                cb_man.setChecked(false);
                cb_woman.setChecked(true);
                man_woman = 1;
            }else{
                cb_man.setChecked(true);
                cb_woman.setChecked(false);
                man_woman = 0;
            }
            et_firstName_edit.setText(String.valueOf(user.getFirstName()));
            et_lastName_edit.setText(String.valueOf(user.getLastName()));
            et_phone.setText(String.valueOf(user.getPhone_num()));
            et_e_mail.setText(String.valueOf(user.getE_mail()));
            et_great_date_edit.setText(String.valueOf(user.getCreateDate()));
        }else{
            count = userInfoDatabase.contUser()+1;
            optionID= userInfoDatabase.selectUserMaxID()+1;
            System.out.println("add user id  = "+optionID);
            tv_id_edit.setText(String.valueOf(count));
            sendMessageToServer("id"+tv_id_edit.getText().toString());
        }
    }
    private void sendMessageToServer(String message) {
        if (webSocketService != null) {
            webSocketService.sendMessage(message);
        }
    }
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            // 处理接收到的消息
            Log.e("", "Query: " + message );
            if(Objects.equals(message, "Which_page")){
                sendMessageToServer("page_information");
            }
            else if("page_editQuery".equals(message)){
                Intent intent1 = new Intent(NewInfoctivity.this,CheckActivity.class);
                startActivity(intent1);
            }
            else if("page_query".equals(message)){
                Intent  intent1 = new Intent(NewInfoctivity.this,InformationServiceActivity.class);
                startActivity(intent1);
            }
            else if(message.startsWith("firstName")){
                isSendFirstName = false;
                et_firstName_edit.setText(message.substring("firstName".length()));
            }
            else if(message.startsWith("lastName")){
                isSendLastName = false;
                et_lastName_edit.setText(message.substring("lastName".length()));
            }
            else if(message.startsWith("age")){
                isSendAge = false;
                et_age_edit.setText(message.substring("age".length()));
            }
            else if (message.startsWith("date")){
                isSendDate = false;
                et_great_date_edit.setText(message.substring("date".length()));
            }
            else if (message.startsWith("phone_num")){
                isSendPhone = false;
                et_phone.setText(message.substring("phone_num".length()));
            }
            else if (message.startsWith("e_mail")){
                isSendE_mail = false;
                et_e_mail.setText(message.substring("e_mail".length()));
            }
            else if ("save_userInfo".equals(message)){
                System.out.println("save");
                bt_next.performClick();
            }
            else if(message.startsWith("man_woman")){
                isSendGender = false;
                if("1".equals(message.substring("man_woman".length()))){
                    cb_man.setChecked(true);
                }else{
                    cb_man.setChecked(false);
                }
            }
            else if ("getuserinfo".equals(message)){
                User user1 = new User();
                user1.setFirstName(!et_firstName_edit.getText().toString().isEmpty()?et_firstName_edit.getText().toString():" ");
                user1.setLastName(!et_lastName_edit.getText().toString().isEmpty()?et_lastName_edit.getText().toString():" ");
                user1.setAge(!et_age_edit.getText().toString().isEmpty()?et_age_edit.getText().toString():"  ");
                user1.setUserID(Integer.valueOf(tv_id_edit.getText().toString()));
                user1.setGender(man_woman == 1 ?true:false);
                user1.setCreateDate(!et_great_date_edit.getText().toString().isEmpty()?et_great_date_edit.getText().toString():" ");
                user1.setE_mail(!et_e_mail.getText().toString().isEmpty()?et_e_mail.getText().toString():" ");
                user1.setPhone_num(!et_phone.getText().toString().isEmpty()?et_phone.getText().toString():" ");
                sendMessageToServer("userinfo"+user1.toString());
            }
            else if ("flag_update".equals(message)){
                User user1 = new User();
                user1.setFirstName(!et_firstName_edit.getText().toString().isEmpty()?et_firstName_edit.getText().toString():" ");
                user1.setLastName(!et_lastName_edit.getText().toString().isEmpty()?et_lastName_edit.getText().toString():" ");
                user1.setAge(!et_age_edit.getText().toString().isEmpty()?et_age_edit.getText().toString():"  ");
                user1.setUserID(Integer.valueOf(tv_id_edit.getText().toString()));
                user1.setGender(man_woman == 1 ?true:false);
                user1.setCreateDate(!et_great_date_edit.getText().toString().isEmpty()?et_great_date_edit.getText().toString():" ");
                user1.setE_mail(!et_e_mail.getText().toString().isEmpty()?et_e_mail.getText().toString():" ");
                user1.setPhone_num(!et_phone.getText().toString().isEmpty()?et_phone.getText().toString():" ");
                sendMessageToServer("userinfo"+user1.toString());
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter1 = new IntentFilter("websocket_message_received");
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,intentFilter1);
        IntentFilter intentFilter21 = new IntentFilter("Calendar");
        LocalBroadcastManager.getInstance(this).registerReceiver(getMessageReceiver,intentFilter21);
        IntentFilter intentFilter11 = new IntentFilter("network_message_received");
        LocalBroadcastManager.getInstance(this).registerReceiver(networkReceiver,intentFilter11);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(networkReceiver);
    }

    private void insert_data() {
        User user = new User();
        user.setUserID(Integer.valueOf(optionID));
        user.setFirstName(et_firstName_edit.getText().toString());
        user.setLastName(et_lastName_edit.getText().toString());
        user.setAge(et_age_edit.getText().toString());
        user.setGender(man_woman ==1 ? true:false);
        user.setPhone_num(et_phone.getText().toString());
        user.setE_mail(et_e_mail.getText().toString());
        user.setCreateDate(et_great_date_edit.getText().toString());
        System.out.println("保存 ："+user.toString());
        user.setMachine(cpuid);
        if(getIntent().getIntExtra("userID",0)>0){
            long updateFlag = userInfoDatabase.updateUser(user);
            System.out.println(updateFlag);
           if(updateFlag>0){
               userAPIUpdate(user);
           }
        }else{
            if (userInfoDatabase.insertUsers(user) < 1) {
                Toast.makeText(this, getString(R.string.error_save), Toast.LENGTH_SHORT).show();
            }else{
                userApiInsert(user);
            }
        }
        sendMessageToServer("page_query");
    }
    private void onclick(){
        bt_return.setOnClickListener(v -> {
            sendMessageToServer("page_check");
            Intent intent = new Intent(NewInfoctivity.this,CheckActivity.class);
            startActivity(intent);
        });
        bt_next.setOnClickListener(v -> {
            if(tv_id_edit.getText().length() > 0 && et_firstName_edit.getText().length() > 0
            && et_lastName_edit.getText().length() > 0 && et_age_edit.getText().length() > 0
            && et_great_date_edit.getText().length() > 0){
                if(getIntent().getIntExtra("userID",0)==0){
                    if(count + 1 < 500){  //最大存储数量 500
                        insert_data();
                        sendMessageToServer("page_query");
                        Intent intent = new Intent(NewInfoctivity.this,InformationServiceActivity.class);
                        startActivity(intent);
                    }else {
                        Toast toastCenter = Toast.makeText(NewInfoctivity.this,
                                getString(R.string.userlimit),Toast.LENGTH_SHORT);
                        toastCenter.show();
                    }
                }else{
                    insert_data();
                    sendMessageToServer("page_query");
                    Intent intent = new Intent(NewInfoctivity.this,InformationServiceActivity.class);
                    startActivity(intent);
                }
            } else {
                Toast toastCenter = Toast.makeText(NewInfoctivity.this,
                        getString(R.string.stillinformation),Toast.LENGTH_SHORT);
                toastCenter.show();
            }
        });
        cb_man.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                man_woman = 0;
                cb_man.setChecked(true);
                cb_woman.setChecked(false);
            }else{
                man_woman = 1;
                cb_man.setChecked(false);
                cb_woman.setChecked(true);
            }
            if(isSendGender){
                sendMessageToServer("man_woman"+(isChecked ?1:2));
            }
            isSendGender = true;
        });
        cb_woman.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                man_woman = 1;
                cb_woman.setChecked(true);
                cb_man.setChecked(false);
            }
        });
        bt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = et_great_date_edit.getText().toString();
                Intent intent = new Intent(NewInfoctivity.this,CalendarActivity.class);
                intent .putExtra("date",time);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        et_firstName_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(isSendFirstName){
                    sendMessageToServer("firstName"+editable.toString());
                }
                isSendFirstName =true;

            }
        });

        et_lastName_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(isSendLastName){
                    sendMessageToServer("lastName"+editable.toString());
                }
                isSendLastName=true;
            }
        });
        et_age_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(isSendAge){
                    sendMessageToServer("age"+editable.toString());
                }
                isSendAge = true;
            }
        });
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(isSendPhone){
                    sendMessageToServer("phone_num"+editable.toString());
                }
                isSendPhone = true;
            }
        });
        et_e_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(isSendE_mail){
                    sendMessageToServer("e_mail"+editable.toString());
                }
                isSendE_mail = true;
            }
        });
        et_great_date_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(isSendDate){
                    sendMessageToServer("date"+editable.toString());
                }
                isSendDate = true;
            }
        });
    }
    private void date(){ //获取当前系统日期
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
        systemTime = day + '/' + month + '/' + year;
        systemTimeNum = day + month + year;
        Log.e("", "date: " + day + month + year );
        if(systemTime.charAt(2) == '/' && systemTime.charAt(5) == '/'){
            et_great_date_edit.setText(systemTime);
        }else {
            Toast toastCenter = Toast.makeText(NewInfoctivity.this,
                   getString(R.string.notwifi),
                    Toast.LENGTH_SHORT);
//            toastCenter.setGravity(Gravity.CENTER,0,0);
            toastCenter.show();
            et_great_date_edit.setText("");
        }
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
    /**
     * 点击空白位置 隐藏软键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
                inset();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            inset();
        }
    }

    void userApiInsert(User user){
        uploadInfoDB.insertUserInsert(String.valueOf(user.getUserID()));
        UserAPI userAPI = UserService.getUserAPI();
        Call<Long> insertUserCall =  userAPI.addUser(user);
        insertUserCall.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                Long insert = response.body();
                //删除操作痕迹
                uploadInfoDB.deleteUserInsert(String.valueOf(user.getUserID()));
                System.out.println("api : finish insert : "+insert );
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.e("api","请求失败"+t.getMessage());
            }
        });
    }
    void userAPIUpdate(User user){
        //预添加痕迹
        uploadInfoDB.insertUserInsert(String.valueOf(user.getUserID()));
        UserAPI userAPI = UserService.getUserAPI();
        Call<Long> updateUserCall = userAPI.updateOne(user);
        updateUserCall.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                Long update = response.body();
                //删除痕迹
                uploadInfoDB.deleteUserInsert(String.valueOf(user.getUserID()));
                System.out.println("api : finish update  : "+update);
            }
            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.e("api","请求失败"+t.getMessage());
            }
        });
    }


}