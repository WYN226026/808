package com.example.freezecl4_zcx;

import static com.example.freezecl4_zcx.CheckActivity.gender;
import static com.example.freezecl4_zcx.StartActivity.cpuid;
import static com.example.freezecl4_zcx.StartActivity.isStartUpLoadUserDelete;
import static com.example.freezecl4_zcx.StartActivity.webSocketService;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import adapter.OnClickItemListener;
import adapter.OnLongItemListener;
import adapter.RCY1Adapter;
import adapter.RCY2Adapter;
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
import tools.ShowWifiErrorDialog;

public class InformationServiceActivity extends AppCompatActivity {
    private Thread insertDataThread;
    private boolean isGetUserInfos = false;
    private boolean isGetUserS = false;
    private List<UserInfo> getUserInfos = new ArrayList<>();
    private List<User> getUsers = new ArrayList<>();
    Class drawable = R.drawable.class;
    Field field;
    private EditText editTextSearch;
    private ImageButton bt_edit;
    private Button bt_addInformation;
    private Button bt_return;
    private TextView data1;
    private TextView data2;
    private TextView data4;
    private TextView data6;
    private TextView data7;
    private TextView data8;
    private TextView data9;
    private TextView data10;
    private ImageView ig_p;
    private ImageView ig_p1;
    private ImageView ig_p2;
    private Button bt_cn;
    private Button bt_upload;
    public static int userID = 0;
    private TextView data_date;
    //用于修改时间查找时间的用户信息的具体id
    private  int userInfoId;
    private RecyclerView rcy1;
    private RecyclerView rcy2;
    private RCY1Adapter rcy1Adapter;
    private RCY2Adapter rcy2Adapter;
    UserInfoDatabase userInfoDatabase;
    private List<UserInfo> userInfos = new ArrayList<>();

    private List<User> users = new ArrayList<>();
    private User  nowUser = new User();

    private UserInfo nowuserInfo = new UserInfo();
    private int clickRCY1Item = 0;
    private UploadInfoDB uploadInfoDB;
    private boolean isClickRcy1  = true;
    private boolean isClickRcy2 = true;
    private boolean isSearch = true;
    private int clickRCY2Item= -1;
    private boolean isInsertAllDateThread = false;
    //将返回的日期。转化为 日/月/年
    private TextView data20;
    private TextView data21;
    private TextView data22;
    private TextView data23;
    private TextView data24;
    private ImageView ig_hand;
    private TextView data_date1;
    private ImageButton bt_edit1;
    private String hand1_text;
    private TextView tv_weight;
    private TextView tv_height;
    private EditText et_weight;
    private EditText et_height;
    private ImageButton ib_edit_weight;
    private ImageButton ib_edit_height;
    private TextView tv_ld_weight;
    private TextView tv_ld_height;
    private EditText et_ld_weight;
    private EditText et_ld_height;
    private ImageButton ib_edit_ld_weight;
    private ImageButton ib_edit_ld_height;
    public static String formatDateString(String dateString) {
        // 分割日期字符串
        String[] parts = dateString.split("/");

        // 提取年、月、日
        String year = parts[2];
        String month = parts[1];
        String day = parts[0];
        System.out.println("information _"+year+" " +month +"  "+day);
        // 重新格式化月份和日期，确保它们是两位数的
        month = String.format("%02d", Integer.parseInt(month));
        day = String.format("%02d", Integer.parseInt(day));
        year =String.format("%02d", Integer.parseInt(year));
        // 重新拼接日期字符串
        return  year + "/" + month + "/" + day ;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inset();
        setContentView(R.layout.activity_information_service);
        userID = 0;
        initDataBases();
        initView();
        initRCY();
        onclick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(CandlerMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(networkReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter1 = new IntentFilter("websocket_message_received");
        LocalBroadcastManager.getInstance(this).registerReceiver(getMessageReceiver,intentFilter1);
        IntentFilter intentFilter2 = new IntentFilter("Calendar");
        LocalBroadcastManager.getInstance(this).registerReceiver(CandlerMessageReceiver,intentFilter2);
        IntentFilter intentFilter11 = new IntentFilter("network_message_received");
        LocalBroadcastManager.getInstance(this).registerReceiver(networkReceiver,intentFilter11);
    }
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
                ShowWifiErrorDialog.showWifiError(InformationServiceActivity.this,getWindowManager());
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

    private final  BroadcastReceiver CandlerMessageReceiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String  message = intent.getStringExtra("message");
            if(userInfoDatabase.updateUserInfoCreateDate(String.valueOf(nowuserInfo.getUserID()),String.valueOf(nowuserInfo.getUserInfoID()),formatDateString(message))>0){
                nowuserInfo.setWork_date(formatDateString(message));
                userInfoAPIUpdate(nowuserInfo);
                Toast.makeText(InformationServiceActivity.this, "Successfully modified", Toast.LENGTH_SHORT).show();
            }
            rcy1.scrollToPosition(clickRCY1Item);
            rcy1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    RecyclerView.ViewHolder viewHolder = rcy1.findViewHolderForAdapterPosition(clickRCY1Item);
                    if (viewHolder != null) {
                        viewHolder.itemView.performClick();
                    }
                }
            }, 100);
            set1();
        }
    };
    private final BroadcastReceiver getMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            // 处理接收到的消息
            Log.e("", "Query: " + message );
            if(Objects.equals(message, "Which_page")){
                sendMessageToServer("page_query");
            }
            else if("page_information".equals(message)){
                Intent intent1 = new Intent(InformationServiceActivity.this, NewInfoctivity.class);
                startActivity(intent1);
            }
            else if("page_modeSelect".equals(message)){
                gender = nowUser.isGender()?0:1;
                Intent intent1 = new Intent(InformationServiceActivity.this, ModeActivity.class);
                startActivity(intent1);
            }
            else if("page_check".equals(message)){
                Intent intent1 = new Intent(InformationServiceActivity.this,CheckActivity.class);
                startActivity(intent1);
            }
            else if(message.startsWith("rcy1Click")){
                isClickRcy1 = false;
                clickRCY1Item =Integer.parseInt(message.substring("rcy1Click".length()));
                if(users.size()>=clickRCY1Item){
                    rcy1.scrollToPosition(clickRCY1Item);
                    rcy1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerView.ViewHolder viewHolder = rcy1.findViewHolderForAdapterPosition(clickRCY1Item);
                            if (viewHolder != null) {
                                viewHolder.itemView.performClick();
                            }
                        }
                    }, 100);
                }
            }
            else if (message.startsWith("rcy2Click")){
                isClickRcy2 = false;
                clickRCY2Item = Integer.parseInt(message.substring("rcy2Click".length()));
                System.out.println("rcy2Click "+clickRCY2Item);
                if(userInfos.size()>=clickRCY2Item){
                    rcy2.scrollToPosition(clickRCY2Item);
                    rcy2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerView.ViewHolder viewHolder = rcy2.findViewHolderForAdapterPosition(clickRCY2Item);
                            if(viewHolder != null){
                                viewHolder.itemView.performClick();
                            }
                        }
                    },100);
                }
            }
            else if (message.startsWith("delete")){
                int position = Integer.parseInt(message.substring("delete".length()));
                System.out.println("delete users " +position+"   ");
                if(userInfoDatabase.deleteUser(String.valueOf(position))>0){
                    System.out.println("delete usersDB "+nowUser.getUserID());
                    userAPIDelete(cpuid,String.valueOf(position));
                }else{
                    System.out.println("delete false");
                }
                refRCY1();
                set1();
            }
            ///update
            else if (message.startsWith("update")){
                int userid = Integer.parseInt(message.substring("update".length()));
                System.out.println("udpate  = "+userid);
                sendMessageToServer("page_information");
                Intent intent1 = new Intent(InformationServiceActivity.this, NewInfoctivity.class);
                intent1.putExtra("userID",userid);
                intent1.putExtra("positionID",clickRCY1Item+1);
                startActivity(intent1);
            }
            else if (message.startsWith("search")){
                isSearch = false;
                String name = message.substring("search".length());
                editTextSearch.setText(name);
            }
            else if (message.startsWith("UserinfoDelete")){
                // 找到 "userid" 的位置
                int userIdIndex = message.indexOf("userid");
                // 找到 "infoid" 的位置
                int infoIdIndex = message.indexOf("infoid");
                // 截取 "userid" 后面的部分作为 userid
                String userid = message.substring(userIdIndex + "userid".length(), infoIdIndex);
                // 截取 "infoid" 后面的部分作为 infoid
                String infoid = message.substring(infoIdIndex + "infoid".length());
                // 输出结果
                System.out.println("userid: " + userid);
                System.out.println("infoid: " + infoid);
                if(userInfoDatabase.deleteUserInfo(userid,infoid)>0){
                    userInfoAPIDelete(cpuid,userid,infoid);
                }
                rcy1.scrollToPosition(clickRCY1Item);
                rcy1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView.ViewHolder viewHolder = rcy1.findViewHolderForAdapterPosition(clickRCY1Item);
                        if (viewHolder != null) {
                            viewHolder.itemView.performClick();
                        }
                    }
                }, 100);
                set1();

            }
            else if (message.startsWith("uptime")){
                int timeIdIndex = message.indexOf("time");
                int userIdIndex = message.indexOf("userid");
                int infoIdIndex = message.indexOf("infoid");
                String time = message.substring(timeIdIndex+"timetime".length(),userIdIndex);
                String userid = message.substring(userIdIndex+"userid".length(),infoIdIndex);
                String infoid = message.substring(infoIdIndex+"infoid".length());
                System.out.println("  time"+time+"  userid"+userid+"  infoid"+infoid);
                UserInfo userInfo = userInfoDatabase.queryOneUserInfo(String.valueOf(userid),String.valueOf(infoid));
                System.out.println("uptime "+userInfo);
                userInfo.setWork_date(time);
                if(userInfoDatabase.updateUserInfoCreateDate(userid,infoid,time)>0){
                    userInfoAPIUpdate(userInfo);
                    Toast.makeText(InformationServiceActivity.this,getResources().getString(R.string.success), Toast.LENGTH_SHORT).show();
                    rcy1.scrollToPosition(clickRCY1Item);
                    rcy1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerView.ViewHolder viewHolder = rcy1.findViewHolderForAdapterPosition(clickRCY1Item);
                            if (viewHolder != null) {
                                viewHolder.itemView.performClick();
                            }
                        }
                    }, 100);
                }
            }
        }
    };
    // 刷新该界面。恢复初始状态
    private void refRCY1(){
        users = userInfoDatabase.queryAllUser();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rcy1Adapter.setUsers(users);
                rcy1Adapter.setIsSelectItemPosition(-1);
                set1();
                rcy2.setVisibility(View.GONE);
            }
        });
    }
    //  远程同步
    private void onclick() {
        bt_edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String getTime = nowuserInfo.getWork_date();
                 Intent intent = new Intent(InformationServiceActivity.this, CalendarActivity.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 intent.putExtra("date",getTime);
                 startActivity(intent);
            }
        });
        ib_edit_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_weight.getText().toString().isEmpty()){
                    userInfoDatabase.updateInfoWeight(et_weight.getText().toString(),String.valueOf(nowuserInfo.getUserID()),String.valueOf(nowuserInfo.getUserInfoID()));
                }
            }
        });
        ib_edit_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_height.getText().toString().isEmpty()){
                    userInfoDatabase.updateInfoHeight(et_height.getText().toString(),String.valueOf(nowuserInfo.getUserID()),String.valueOf(nowuserInfo.getUserInfoID()));
                }
            }
        });
        ib_edit_ld_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_ld_weight.getText().toString().isEmpty()){
                    userInfoDatabase.updateInfoWeight(et_weight.getText().toString(),String.valueOf(nowuserInfo.getUserInfoID()),String.valueOf(nowuserInfo.getUserInfoID()));
                }
            }
        });
        ib_edit_ld_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_ld_height.getText().toString().isEmpty()){
                    userInfoDatabase.updateInfoHeight(et_height.getText().toString(),String.valueOf(nowuserInfo.getUserInfoID()),String.valueOf(nowuserInfo.getUserInfoID()));
                }
            }
        });
        bt_upload.setOnClickListener(new View.OnClickListener() {
            private int clickCount = 0;
            private long lastClickTime = 0;
            private final int REQUIRED_CLICK_COUNT = 5;
            private final int CLICK_DURATION = 3000; // 3 seconds
            @Override
            public void onClick(View v) {
                long currentTime = System.currentTimeMillis();
                // 如果两次点击之间的时间大于3秒，重置点击次数
                System.out.println("upload is click ");
                if (currentTime - lastClickTime > CLICK_DURATION) {
                    clickCount = 0;
                }
                // 更新上次点击时间
                lastClickTime = currentTime;
                // 增加点击次数
                clickCount++;
                if (clickCount == REQUIRED_CLICK_COUNT) {
                    new AlertDialog.Builder(InformationServiceActivity.this)
                            .setTitle(getResources().getString(R.string.synchron))
                            .setMessage(getResources().getString(R.string.overwiteCurrentData))
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //如果目前没有数据上传中则可以进行数据同步
                                    if(isStartUpLoadUserDelete){
                                        //等待下面服务器中两种数据都传输完后开始保存(线程)
                                        insertAllDateThread();
                                        //请求 用户信息数据
                                        userAPISelect(cpuid);
                                        //请求用户治疗信息数据
                                        userInfoAPISelect(cpuid);
                                    }
                                }
                            }).show();

                }
            }
        });
        bt_addInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToServer("page_newUser");
                Intent intent = new Intent(InformationServiceActivity.this,NewInfoctivity.class);
                startActivity(intent);
            }
        });
        //rcy1单机
        rcy1Adapter.setItemListener(new OnClickItemListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(View v, int position) {
                if(isClickRcy1){
                    sendMessageToServer("rcy1Click"+position);
                }
                isClickRcy1 = true;
                rcy1Adapter.setIsSelectItemPosition(position);
                rcy2.setVisibility(View.VISIBLE);
                userInfos = userInfoDatabase.queryOneUserTOUserInfo(String.valueOf(users.get(position).getUserID()));
                nowUser =  users.get(position);
                userID = users.get(position).getUserID();
                gender = nowUser.isGender() ? 1: 0;
                rcy2Adapter.setUserInfos(userInfos);
                rcy2Adapter.notifyDataSetChanged();
                rcy2Adapter.setIsSelectItemPosition(-1);
                set1();
                clickRCY1Item = position;
            }
        });
        //rcy2单机
        rcy2Adapter.setOnClickListener(new OnClickItemListener() {
            @Override
            public void onItemClick(View v, int position) {
                if(isClickRcy2){
                    sendMessageToServer("rcy2Click"+position);
                }
                isClickRcy2 = true;
                rcy2Adapter.setIsSelectItemPosition(position);

                nowuserInfo = userInfos.get(position);
                String mode = nowuserInfo.getMachine();
                Log.e("", "onItemClick: " + mode );
                if(mode.equals("1")) {
                    set2();
                    setDataTOSet2();
                }else if(mode.equals("2")){
                    set3();
                    setDataTOSet4();
                }
            }
        });
        bt_cn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userID !=0){
                    gender = nowUser.isGender()?0:1;
                    sendMessageToServer("page_modeSelect");
                    Intent intent = new Intent(InformationServiceActivity.this, ModeActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(InformationServiceActivity.this,getResources().getString(R.string.selectUser), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //搜索
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(isSearch){
                    sendMessageToServer("search"+s.toString());
                }
                isSearch = true;
                String query = s.toString();
                users = userInfoDatabase.queryUser(query);
                rcy1Adapter.setUsers(users);
                rcy1Adapter.notifyDataSetChanged();
                rcy1Adapter.setIsSelectItemPosition(-1);
            }
        });
        //rcy1的onLongClick 事件
        rcy1Adapter.setOnLongItemListener(new OnLongItemListener() {
            @Override
            public void OnItemLongClick(View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InformationServiceActivity.this);
                builder.setMessage(getResources().getString(R.string.select_action));
                builder.setNegativeButton(getResources().getString(R.string.modify_data), (dialog, which) -> {
                    sendMessageToServer("update_user");
                    Intent intent = new Intent(InformationServiceActivity.this,NewInfoctivity.class);
                    intent.putExtra("userID",users.get(position).getUserID());
                    intent.putExtra("positionID",position+1);
                    startActivity(intent);
                });
                builder.setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(userInfoDatabase.deleteUser(String.valueOf(users.get(position).getUserID()))>0){
                            userAPIDelete(cpuid,String.valueOf(users.get(position).getUserID()));
                        }
                        refRcy1();
                        set1();
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        inset();
                    }
                });
                alertDialog.show();
            }
        });
        //rcy2 OnLongClick
        rcy2Adapter.setOnLongClickListener(new OnLongItemListener() {
            @Override
            public void OnItemLongClick(View view, int position) {
                AlertDialog.Builder alertDialog =new AlertDialog.Builder(InformationServiceActivity.this);
                alertDialog.setMessage(getResources().getString(R.string.confirm_delete));
                alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除用户治疗信息。（并上传服务器）
                        if(userInfoDatabase.deleteUserInfo(String.valueOf(nowUser.getUserID()),String.valueOf(userInfos.get(position).getUserInfoID()))>0){
                            userInfoAPIDelete(cpuid,String.valueOf(userInfos.get(position).getUserID()),String.valueOf(userInfos.get(position).getUserInfoID()));
                        }
                        rcy1.scrollToPosition(clickRCY1Item);
                        rcy1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                RecyclerView.ViewHolder viewHolder = rcy1.findViewHolderForAdapterPosition(clickRCY1Item);
                                if (viewHolder != null) {
                                    viewHolder.itemView.performClick();
                                }
                            }
                        }, 100);
                        set1();
                        rcy2Adapter.setIsSelectItemPosition(-1);
                    }
                });
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        inset();
                    }
                });
                alertDialog.show();
            }
        });
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToServer("page_check");
                Intent intent = new Intent(InformationServiceActivity.this,CheckActivity.class);
                startActivity(intent);
            }
        });

        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getTime = nowuserInfo.getWork_date();
                Intent intent = new Intent(InformationServiceActivity.this, CalendarActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("date",getTime);
                startActivity(intent);
            }
        });

    }
    void refRcy1(){
        users = userInfoDatabase.queryAllUser();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rcy1Adapter.setUsers(users);
                rcy1Adapter.notifyDataSetChanged();
                set1();
                rcy2.setVisibility(View.GONE);
            }
        });
    }

    private void initDataBases() {
        userInfoDatabase = UserInfoDatabase.getInstance(this);
        userInfoDatabase.openReadDB();
        userInfoDatabase.openWriteDB();
        users = userInfoDatabase.queryAllUser();

        uploadInfoDB = UploadInfoDB.getInstance(this);
        uploadInfoDB.openWriteDB();
        uploadInfoDB.openReadDB();
    }


    private void initRCY() {
        rcy1Adapter = new RCY1Adapter(this,users);
        rcy1.setAdapter(rcy1Adapter);
        rcy1.setLayoutManager(new LinearLayoutManager(this));
        rcy2Adapter = new RCY2Adapter(this,userInfos);
        rcy2.setAdapter(rcy2Adapter);
        rcy2.setLayoutManager(new GridLayoutManager(this,4));
        set1();
        rcy2.setVisibility(View.GONE);
    }

    private void initView(){
        bt_upload = findViewById(R.id.bt_upload);
        rcy1  = findViewById(R.id.rcy1);
        rcy2  = findViewById(R.id.rcy2);
        editTextSearch = findViewById(R.id.editTextSearch);
        bt_edit = findViewById(R.id.bt_edit);
        bt_addInformation = findViewById(R.id.bt_addInformation);
        bt_return = findViewById(R.id.bt_return);
        data1 = findViewById(R.id.data1);
        data2 = findViewById(R.id.data2);
        data4 = findViewById(R.id.data4);
        data6 = findViewById(R.id.data6);
        data7 = findViewById(R.id.data7);
        data8 = findViewById(R.id.data8);
        data9 = findViewById(R.id.data9);
        data10 = findViewById(R.id.data10);
        data20 = findViewById(R.id.data20);
        data21 = findViewById(R.id.data21);
        data22 = findViewById(R.id.data22);
        data23 = findViewById(R.id.data23);
        data24 = findViewById(R.id.data24);
        bt_edit1 = findViewById(R.id.bt_edit1);
        data_date1 = findViewById(R.id.data_date1);
        ig_hand = findViewById(R.id.ig_hand);
        ig_p = findViewById(R.id.ig_p);

        ig_p1 = findViewById(R.id.ig_p1);
        ig_p2 = findViewById(R.id.ig_p2);

        bt_cn = findViewById(R.id.bt_cn);
        data_date = findViewById(R.id.data_date);
        tv_weight = findViewById(R.id.tv_weight);
        tv_height = findViewById(R.id.tv_height);
        et_weight = findViewById(R.id.et_weight);
        et_height = findViewById(R.id.et_height);
        ib_edit_weight = findViewById(R.id.bt_edit_weight);
        ib_edit_height = findViewById(R.id.bt_edit_height);

        tv_ld_height = findViewById(R.id.tv_ld_height);
        tv_ld_weight = findViewById(R.id.tv_ld_weight);

        et_ld_height = findViewById(R.id.et_ld_height);
        et_ld_weight = findViewById(R.id.et_ld_weight);

        ib_edit_ld_weight = findViewById(R.id.bt_edit_ld_weight);
        ib_edit_ld_height = findViewById(R.id.bt_edit_ld_height);

        set1();
    }
    private void set3(){//冷冻
        bt_edit.setVisibility(View.GONE);
        data_date.setVisibility(View.GONE);
        data1.setVisibility(View.GONE);
        data2.setVisibility(View.GONE);
        data4.setVisibility(View.GONE);
        data6.setVisibility(View.GONE);
        data7.setVisibility(View.GONE);
        data8.setVisibility(View.GONE);
        data10.setVisibility(View.GONE);
        ig_p.setVisibility(View.VISIBLE);
        ig_p1.setVisibility(View.GONE);
        ig_p2.setVisibility(View.GONE);
        tv_weight.setVisibility(View.GONE);
        tv_height.setVisibility(View.GONE);
        et_weight.setVisibility(View.GONE);
        et_height.setVisibility(View.GONE);
        ib_edit_weight.setVisibility(View.GONE);
        ib_edit_height.setVisibility(View.GONE);
        data9.setVisibility(View.GONE);

        tv_ld_weight.setVisibility(View.VISIBLE);
        tv_ld_height.setVisibility(View.VISIBLE);

        et_ld_weight.setVisibility(View.VISIBLE);
        et_ld_height.setVisibility(View.VISIBLE);

        ib_edit_ld_weight.setVisibility(View.VISIBLE);
        ib_edit_ld_height.setVisibility(View.VISIBLE);

        bt_edit1.setVisibility(View.VISIBLE);
        data_date1.setVisibility(View.VISIBLE);
        data20.setVisibility(View.VISIBLE);
        data21.setVisibility(View.VISIBLE);
        data22.setVisibility(View.VISIBLE);
        data23.setVisibility(View.VISIBLE);
        data24.setVisibility(View.VISIBLE);
        ig_hand.setVisibility(View.VISIBLE);
    }
    private void set2(){ //磁力
        bt_edit.setVisibility(View.VISIBLE);
        data_date.setVisibility(View.VISIBLE);
        data1.setVisibility(View.VISIBLE);
        data2.setVisibility(View.VISIBLE);
        data4.setVisibility(View.VISIBLE);
        data6.setVisibility(View.VISIBLE);
        data7.setVisibility(View.VISIBLE);
        data8.setVisibility(View.VISIBLE);
        data10.setVisibility(View.VISIBLE);
        ig_p.setVisibility(View.VISIBLE);

        ig_p1.setVisibility(View.VISIBLE);
        ig_p2.setVisibility(View.VISIBLE);

        tv_weight.setVisibility(View.VISIBLE);
        tv_height.setVisibility(View.VISIBLE);

        ib_edit_weight.setVisibility(View.VISIBLE);
        data_date1.setVisibility(View.GONE);
        ib_edit_height.setVisibility(View.VISIBLE);
        bt_edit1.setVisibility(View.GONE);
        data20.setVisibility(View.GONE);
        data21.setVisibility(View.GONE);
        data22.setVisibility(View.GONE);
        data23.setVisibility(View.GONE);
        data24.setVisibility(View.GONE);
        ig_hand.setVisibility(View.GONE);
        et_ld_weight.setVisibility(View.GONE);
        et_ld_height.setVisibility(View.GONE);
        ib_edit_ld_weight.setVisibility(View.GONE);
        ib_edit_ld_height.setVisibility(View.GONE);
        tv_ld_weight.setVisibility(View.GONE);
        tv_ld_height.setVisibility(View.GONE);
        et_weight.setVisibility(View.VISIBLE);
        et_height.setVisibility(View.VISIBLE);
    }
    private void set1(){
        bt_edit.setVisibility(View.GONE);
        data_date.setVisibility(View.GONE);
        data1.setVisibility(View.GONE);
        data2.setVisibility(View.GONE);
        data4.setVisibility(View.GONE);
        data6.setVisibility(View.GONE);
        data7.setVisibility(View.GONE);
        data8.setVisibility(View.GONE);
        data10.setVisibility(View.GONE);
        ig_p.setVisibility(View.GONE);

        ig_p1.setVisibility(View.GONE);
        ig_p2.setVisibility(View.GONE);
        data9.setVisibility(View.GONE);
        bt_edit1.setVisibility(View.GONE);
        data_date1.setVisibility(View.GONE);
        data20.setVisibility(View.GONE);
        data21.setVisibility(View.GONE);
        data22.setVisibility(View.GONE);
        data23.setVisibility(View.GONE);
        data24.setVisibility(View.GONE);
        ig_hand.setVisibility(View.GONE);
        tv_weight.setVisibility(View.GONE);
        tv_height.setVisibility(View.GONE);
        tv_ld_weight.setVisibility(View.GONE);
        tv_ld_height.setVisibility(View.GONE);
        et_weight.setVisibility(View.GONE);
        et_height.setVisibility(View.GONE);
        et_ld_weight.setVisibility(View.GONE);
        et_ld_height.setVisibility(View.GONE);
        ib_edit_height.setVisibility(View.GONE);
        ib_edit_weight.setVisibility(View.GONE);
        ib_edit_ld_height.setVisibility(View.GONE);
        ib_edit_ld_weight.setVisibility(View.GONE);
    }
    //填充数据
    @SuppressLint("SetTextI18n")
    private void setDataTOSet2() {
        String j = "";
        data1.setText("");
        data6.setText("");
        data7.setText("");
        data8.setText("");
        if("1".equals(nowuserInfo.getWork_mode())){
            data1.setText(getResources().getString(R.string.PROFESSIONAL_MODE));
            data6.setText("F1: "+nowuserInfo.getMode_f1()+" HZ");
            data7.setText("F2: "+nowuserInfo.getMode_f2()+" HZ");
            data8.setText("F3: "+nowuserInfo.getMode_f3()+" HZ");
            data9.setVisibility(View.GONE);

        }
        else if("2".equals(nowuserInfo.getWork_mode())){
            data1.setText(getResources().getString(R.string.INTELLIGEET_MODE));
            if("1".equals(nowuserInfo.getMode_options()) ){
                j = getResources().getString(R.string.HIIT);
            }else if("2".equals(nowuserInfo.getMode_options())){
                j = getResources().getString(R.string.STRENGTH);
            }else if("3".equals(nowuserInfo.getMode_options())){
                j = getResources().getString(R.string.MUSCLE_BUILDING);
            }else if ("4".equals(nowuserInfo.getMode_options())){
                j = getResources().getString(R.string.SHAPE_UP);
            }
            else if("5".equals(nowuserInfo.getMode_options())){
                j = getResources().getString(R.string.FAT_BURNING);
            }else if("6".equals(nowuserInfo.getMode_options())){
                j = getResources().getString(R.string.RECOVERY_MODE);
            }else {
                j = "";
            }
        }
        data9.setVisibility(View.VISIBLE);
        data9.setText(j);
        data2.setText("P1: "+nowuserInfo.getP12_mode() +"%"+" / P2: "+nowuserInfo.getP34_mode()+"%");
        data4.setText("RF1: "+nowuserInfo.getRf12_mode() +"%"+" / RF2: "+nowuserInfo.getRf34_mode()+"%");
        data10.setText(nowuserInfo.getMode_duration());
        data_date.setText(nowuserInfo.getWork_date());
        et_weight.setText(nowuserInfo.getL_forehead_current());
        et_height.setText(nowuserInfo.getR_forehead_current());
        try {
            field = drawable.getField("position_" +nowuserInfo.getBody_part());
            int a = field.getInt(field.getName());
            ig_p1.setBackgroundResource(a);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            field = drawable.getField("position_" +nowuserInfo.getBody_part_());
            int a = field.getInt(field.getName());
            ig_p2.setBackgroundResource(a);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    @SuppressLint("SetTextI18n")
    private void setDataTOSet4() {  //冷冻
        if("1".equals(nowuserInfo.getWork_mode())){ //p mode
            data20.setText(getResources().getString(R.string.PROFESSIONAL_MODE));
        }
        else if("2".equals(nowuserInfo.getWork_mode())){ //i mode
            data20.setText(getResources().getString(R.string.INTELLIGEET_MODE));
        }
        et_ld_weight.setText(String.valueOf(nowuserInfo.getL_forehead_current()));
        et_ld_height.setText(String.valueOf(nowuserInfo.getR_forehead_current()));
        data21.setText("Vacuum: " +nowuserInfo.getP12_mode() + " kap");
        data22.setText("Temp: "+nowuserInfo.getP34_mode() + " ℃");
        int num = Integer.parseInt(nowuserInfo.getBody_part()); //手柄编号
        hand_num(num);
        data24.setText(String.valueOf(hand1_text));
        data23.setText("Time: "+nowuserInfo.getMode_duration());//时间
        data_date.setText(nowuserInfo.getWork_date());//riqi
        data_date1.setText(nowuserInfo.getWork_date());
    }
    private void hand_num(int hand){
        switch (hand){
            case 1:
                hand1_text = "A1";
                ig_hand.setBackgroundResource(R.drawable.a);
                break;
            case 2:
                hand1_text = "B1";
                ig_hand.setBackgroundResource(R.drawable.b);
                break;
            case 3:
                hand1_text = "C1";
                ig_hand.setBackgroundResource(R.drawable.c);
                break;
            case 4:
                hand1_text = "B2";
                ig_hand.setBackgroundResource(R.drawable.b);
                break;
            case 5:
                hand1_text = "A2";
                ig_hand.setBackgroundResource(R.drawable.a);
                break;
            case 6:
                hand1_text = "C2";
                ig_hand.setBackgroundResource(R.drawable.c);
                break;
            case 7:
                hand1_text = "D1";
                ig_hand.setBackgroundResource(R.drawable.d);
                break;
            case 8:
                hand1_text = "D2";
                ig_hand.setBackgroundResource(R.drawable.d);
                break;
            case 9:
                hand1_text = "A3";
                ig_hand.setBackgroundResource(R.drawable.a);
                break;
            case 10:
                hand1_text = "B3";
                ig_hand.setBackgroundResource(R.drawable.b);
                break;
            case 11:
                hand1_text = "C3";
                ig_hand.setBackgroundResource(R.drawable.c);
                break;
            case 12:
                hand1_text = "B4";
                ig_hand.setBackgroundResource(R.drawable.b);
                break;
            case 13:
                hand1_text = "A4";
                ig_hand.setBackgroundResource(R.drawable.a);
                break;
            case 14:
                hand1_text = "C4";
                ig_hand.setBackgroundResource(R.drawable.c);
                break;
            case 15:
                hand1_text = "D3";
                ig_hand.setBackgroundResource(R.drawable.d);
                break;
            case 16:
                hand1_text = "D4";
                ig_hand.setBackgroundResource(R.drawable.d);
                break;
            default:
                hand1_text = "?";
                ig_hand.setBackgroundResource(R.drawable.error1);
                break;
        }
    }
    private void inset() {
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
    //在服务器中。删除用户信息
    void userAPIDelete(String machine,String userID){
        //预添加 删除痕迹
        // 添加  删除痕迹 ,  如果在  添加痕迹 表中 存在该删除该添加痕迹 ,一个用户信息 的添加痕迹，一个用户治疗信息的添加痕迹。
        if(uploadInfoDB.insertUserDelete(String.valueOf(userID))>0){
            System.out.println("添加成功aaabbbccc");
            uploadInfoDB.deleteUserInsert(String.valueOf(userID));
            uploadInfoDB.deleteUserInfoDeleteUser(String.valueOf(userID));
        }else{
            uploadInfoDB.deleteUserInsert(String.valueOf(userID));
            uploadInfoDB.deleteUserInfoDeleteUser(String.valueOf(userID));
        }
        UserAPI userAPI = UserService.getUserAPI();
        Call<Long> longCall = userAPI.deleteUserOne(machine, userID);
        longCall.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                Long deleteUser = response.body();
                //删除修改痕迹。
                uploadInfoDB.deleteUserDelete(String.valueOf(userID));
                sendMessageToServer("refresh");
                System.out.println("API ： delete user one finish : "+ deleteUser);
            }
            @Override
            public void onFailure(Call<Long> call, Throwable t) {

            }
        });
    }
    //用户治疗信息的删除
    void userInfoAPIDelete(String machine,String userID,String userInfoID){
        //预添加删除痕迹
        // 添加  删除痕迹 ,  如果在  添加痕迹 表中 存在该删除该添加痕迹
        if(uploadInfoDB.insertUserInfoDelete(userID,userInfoID)>0){
            //添加删除痕迹
            uploadInfoDB.deleteUserInfoInsert(userID,userInfoID);
        }
        UserInfoAPI userInfoAPI = UserInfoService.getUserinfoAPI();
        Call<Long> longCall = userInfoAPI.deleteUserInfoOne(machine, userID, userInfoID);
        longCall.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                Long deleteUserinfo = response.body();
                System.out.println("API : delete userInfo one finish : "+deleteUserinfo );
                //删除    删除痕迹
                uploadInfoDB.deleteUserInfoDelete(userID,userInfoID);
                //向手机端发送finishDelete
                //手机端接收到后重新获取选中用户的治疗信息
                sendMessageToServer("finishDelete");
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                System.out.println("服务器删除失败，记录操作痕迹");
            }
        });
    }
    //用户治疗信息日期修改(后台当userID和infoID 一样时则为修改。如果不一样则添加)
    void userInfoAPIUpdate(UserInfo userInfo){
        //预添加修改记录。
        uploadInfoDB.insertUserInfoInsert(String.valueOf(userInfo.getUserID()),String.valueOf(userInfo.getUserInfoID()));
        //!!设置 cpuid (再后端来判断需要用到那个表)
        userInfo.setMachine(cpuid);

        UserInfoAPI userInfoAPI = UserInfoService.getUserinfoAPI();
        Call<Long> longCall = userInfoAPI.updateUserInfo(userInfo);
        longCall.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                Long updateUserInfo = response.body();
                //到这一步说明修改成功则删除预添加痕迹
                uploadInfoDB.deleteUserInfoInsert(String.valueOf(userInfo.getUserID()),String.valueOf(userInfo.getUserInfoID()));
                System.out.println("API : update userInfo on finish : "+updateUserInfo);
            }
            @Override
            public void onFailure(Call<Long> call, Throwable t) {
            }
        });
    }
    //获取服务器中的所有用户信息
    void userAPISelect(String machine){
        UserAPI userAPI = UserService.getUserAPI();
        Call<List<User>> listCall = userAPI.selectUserAll(machine);
        listCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    getUsers = response.body();
                    if(getUsers.isEmpty()){
                        getUsers = new ArrayList<>();
                    }
                    isGetUserS = true;
                    System.out.println("userAPISelect :"+getUsers);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
            }
        });
    }
    //获取服务器中的缩有用户治疗信息
    void userInfoAPISelect(String machine){
        UserInfoAPI userInfoAPI = UserInfoService.getUserinfoAPI();
        Call<List<UserInfo>>listCall = userInfoAPI.selectAllUserInfo(machine);
        listCall.enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                if(response.isSuccessful()){
                    getUserInfos = response.body();
                    if(getUserInfos.isEmpty()){
                        getUsers = new ArrayList<>();
                    }
                    isGetUserInfos = true;
                    System.out.println("userInfoAPISelect :"+getUserInfos);
                }
            }

            @Override
            public void onFailure(Call<List<UserInfo>> call, Throwable t) {

            }
        });
    }

    //获取到服务器中所有数据后保存到本地数据库中。
    private void insertAllDateThread(){
        insertDataThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //线程结束阀
                while (!isInsertAllDateThread){
                    //只有当都获取到两种数据后。则进入。。
                    if(isGetUserInfos && isGetUserS){
                        //定义事务。直接全部保存两种数据
                        userInfoDatabase.insertUsersInfoAndUsersAll(getUsers,getUserInfos);
                        //保存成功后恢复初始状态
                        refRCY1();
                        //线程关闭
                        isInsertAllDateThread = true;
                    }
                }
            }
        });
        insertDataThread.start();
    }

}