package com.example.freezecl4_zcx;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.shawnlin.numberpicker.NumberPicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adapter.OnClickItemListener;
import adapter.RCYMonthAdapter;
import adapter.RcyYearAdapter;


public class CalendarActivity extends AppCompatActivity {
    private ImageView bt_year;
    private ImageView bt_month;
    private RecyclerView rcy_year;
    private RcyYearAdapter rcyYearAdapter;
    private MaterialCalendarView calendar;
    private ImageView bt_back;
    private RecyclerView rcy_month;
    private RCYMonthAdapter rcyMonthAdapter;
    private ImageButton bt_edit;
    private int selectYear = 1900;
    private LinearLayout lin_edit;
    private LinearLayout lin_calendar;
    public static int flag =0;
    public LinearLayout lin_rcy_month;
    Intent intent ;
    long currentDate= -1 ;
    String[] year_month_day ;
    private TextView tv_month;
    Dialog dialog;
    String[] day_data = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    String[] moon_data = {"01","02","03","04","05","06","07","08","09","10","11","12"};
    String edit_current_data;
    CalendarDay today;
    private  AlertDialog progressDialog;
    private Handler handler = new Handler();


    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        },0);
    }

    private void loadData() {
        currentDate = convertStringToTimestamp(getIntent().getStringExtra("date"));
        year_month_day = getYearMonthDay(getIntent().getStringExtra("date"));
        //创建calendar 对象。
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTimeInMillis(currentDate);
        // 创建 CalendarDay 对象
        today = CalendarDay.from(calendarDate.get(Calendar.YEAR), calendarDate.get(Calendar.MONTH) + 1, calendarDate.get(Calendar.DAY_OF_MONTH));
        System.out.println("today"+today);
        intent = new Intent("Calendar");
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialong_calendar);
        if(dialog.getWindow()!=null){
            dialog.getWindow().setBackgroundDrawableResource(R.color.touming);
        }
        dialog.show();
        // 检查 compactCalendarView 是否为空
        bt_year = dialog.findViewById(R.id.bt_year);
        bt_month = dialog.findViewById(R.id.bt_month);
        rcy_year = dialog.findViewById(R.id.rcy_year);
        calendar = dialog.findViewById(R.id.calendar);
        bt_back = dialog.findViewById(R.id.bt_back);
        rcy_month = dialog.findViewById(R.id.rcy_month);
        lin_calendar = dialog.findViewById(R.id.lin_calendar);
        lin_edit = dialog.findViewById(R.id.lin_edit);
        bt_edit  = dialog.findViewById(R.id.bt_edit);
        lin_rcy_month = dialog.findViewById(R.id.lin_rcy_month);
        tv_month = dialog.findViewById(R.id.tv_month);

        if(currentDate!=-1){
            calendar.setDateSelected(today,true);
            calendar.setCurrentDate(today,false);
        }
        rcyYearAdapter = new RcyYearAdapter(this);
        rcyMonthAdapter = new RCYMonthAdapter(this);

        rcy_year.setAdapter(rcyYearAdapter);
        rcy_month.setAdapter(rcyMonthAdapter);

        rcy_year.setLayoutManager(new GridLayoutManager(CalendarActivity.this,4));
        rcy_month.setLayoutManager(new GridLayoutManager(CalendarActivity.this,3));
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_calendar.setVisibility(View.GONE);
                lin_edit.setVisibility(View.VISIBLE);
                edit_current_data = year_month_day[0] +"/"+ year_month_day[1] +"/" + year_month_day[2];
            }
        });

        bt_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_back.setVisibility(View.VISIBLE);
                // 获取选中的 CalendarDay 对象
                CalendarDay selectedDate = calendar.getSelectedDate();
                long selectDateMillis=0;
                if (selectedDate != null) {
                    // 创建 Calendar 实例并设置为选中的日期
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(selectedDate.getYear(), selectedDate.getMonth() - 1, selectedDate.getDay());  // 月份从 0 开始
                    // 将 Calendar 转换为 Date
                    Date date = calendar.getTime();
                    // 获取 Date 对象的时间戳（毫秒数）
                    selectDateMillis = date.getTime();
                    // 现在 selectDateMillis 包含选中日期的毫秒数
                    System.out.println("Selected date in millis: " + selectDateMillis);
                } else {
                    System.out.println("No date selected.");
                }
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(selectDateMillis);
                int selectYear = calendar1.get(Calendar.YEAR);
                int clickRCYYearItem = selectYear-1900;
                System.out.println("calenderView"+clickRCYYearItem);
                if(clickRCYYearItem>=0){
                    rcy_year.scrollToPosition(clickRCYYearItem);
                    rcyYearAdapter.setIsSelectItemPosition(clickRCYYearItem);
                }
                calendar.setVisibility(View.GONE);
                rcy_year.setVisibility(View.VISIBLE);
                lin_rcy_month.setVisibility(View.GONE);
                bt_year.setBackgroundResource(R.drawable.year_black);
                bt_month.setBackgroundResource(R.drawable.mon_gray);
                bt_back.setBackgroundResource(R.drawable.bt_head_back);
            }
        });
        bt_month.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onClick(View view) {
                bt_back.setVisibility(View.VISIBLE);
                long selectDateMillis=0;
                CalendarDay selectedDate = calendar.getSelectedDate();
                if (selectedDate != null) {
                    // 创建 Calendar 实例并设置为选中的日期
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(selectedDate.getYear(), selectedDate.getMonth() - 1, selectedDate.getDay());  // 月份从 0 开始
                    // 将 Calendar 转换为 Date
                    Date date = calendar.getTime();
                    // 获取 Date 对象的时间戳（毫秒数）
                    selectDateMillis = date.getTime();
                    // 现在 selectDateMillis 包含选中日期的毫秒数
                    System.out.println("Selected date in millis: " + selectDateMillis);
                } else {
                    System.out.println("No date selected.");
                }
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(selectDateMillis);

                int selectMonth = calendar1.get(Calendar.MONTH);
                selectYear = calendar1.get(Calendar.YEAR);
                int ClickRcyMonthItem = selectMonth;
                System.out.println("bt_month"+ClickRcyMonthItem);
                if(ClickRcyMonthItem>=0){
                    rcy_month.scrollToPosition(ClickRcyMonthItem);
                    rcyMonthAdapter.setIsSelectItemPosition(ClickRcyMonthItem);
                }
                //再tv_month中显示当前的月份。
                tv_month.setText(String.format("%02d",ClickRcyMonthItem+1)+"  "+"month");
                calendar.setVisibility(View.GONE);
                lin_rcy_month.setVisibility(View.VISIBLE);
                rcy_year.setVisibility(View.GONE);
                bt_year.setBackgroundResource(R.drawable.year_gray);
                bt_month.setBackgroundResource(R.drawable.mon_black);
                bt_back.setBackgroundResource(R.drawable.bt_head_back);
            }
        });
        rcyYearAdapter.setOnClickListener(new OnClickItemListener() {
            @Override
            public void onItemClick(View v, int position) {
                int year = 1900+position;
                calendar.clearSelection();
                calendar.setDateSelected(getCalendarDayForFirstDayOfYear(year),true);
                calendar.setCurrentDate(getCalendarDayForFirstDayOfYear(year),false);
                calendar.setVisibility(View.VISIBLE);
                rcy_year.setVisibility(View.GONE);
                bt_year.setBackgroundResource(R.drawable.year_gray);
                bt_back.setBackground(null);
            }
        });
        rcyMonthAdapter.setOnClickListener(new OnClickItemListener() {
            @Override
            public void onItemClick(View v, int position) {
                int month = position+1;
                calendar.clearSelection();
                calendar.setDateSelected(getCalendarDayForFirstDayOfMonth(selectYear,month),true);
                calendar.setCurrentDate(getCalendarDayForFirstDayOfMonth(selectYear,month),false);
                calendar.setVisibility(View.VISIBLE);
                lin_rcy_month.setVisibility(View.GONE);
                bt_month.setBackgroundResource(R.drawable.mon_gray);
                bt_back.setBackground(null);
            }
        });
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                int year = date.getYear();
                int month = date.getMonth();
                int day = date.getDay();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        intent.putExtra("message", year + "/" + month + "/" + day);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        System.out.println("message " + year + "/" + month + "/" + day);
                    }
                },500);
                dialog.dismiss();
                finish();

            }
        });
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.setVisibility(View.VISIBLE);
                rcy_year.setVisibility(View.GONE);
                lin_rcy_month.setVisibility(View.GONE);
                bt_year.setBackgroundResource(R.drawable.year_gray);
                bt_month.setBackgroundResource(R.drawable.mon_gray);
                bt_back.setBackground(null);
            }
        });

        ImageView bt_calender = dialog.findViewById(R.id.bt_backCalendar);
        ImageButton bt_sure = dialog.findViewById(R.id.bt_sure);
        ImageButton ib_refresh = dialog.findViewById(R.id.bt_refresh);
        NumberPicker np_day = dialog.findViewById(R.id.np_day);
        NumberPicker np_moon = dialog.findViewById(R.id.np_moon);
        NumberPicker np_year = dialog.findViewById(R.id.np_year);

        np_day.setMinValue(1);
        np_day.setMaxValue(day_data.length);
        np_day.setDisplayedValues(day_data);

        np_moon.setMaxValue(1);
        np_moon.setMaxValue(moon_data.length);
        np_moon.setDisplayedValues(moon_data);

        np_year.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.valueOf(value);
            }
        });
        np_year.setMaxValue(2099);
        np_year.setMinValue(1900);
        np_day.setValue(Integer.valueOf(year_month_day[2]));
        np_moon.setValue(Integer.valueOf(year_month_day[1]));
        np_year.setValue(Integer.valueOf(year_month_day[0]));
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("message",edit_current_data);
                        System.out.println("edit_current_data"+edit_current_data);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }
                },500);
                dialog.dismiss();
                finish();


            }
        });

        np_day .setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                edit_current_data = np_year.getValue()+"/"+moon_data[np_moon.getValue()-1]+"/"+day_data[np_day.getValue()-1];

            }
        });
        np_moon.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                edit_current_data = np_year.getValue()+"/"+moon_data[np_moon.getValue()-1]+"/"+day_data[np_day.getValue()-1];

            }
        });
        np_year.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                edit_current_data = np_year.getValue()+"/"+moon_data[np_moon.getValue()-1]+"/"+day_data[np_day.getValue()-1];

            }
        });
        ib_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                np_day.setValue(Integer.valueOf(year_month_day[2]));
                np_moon.setValue(Integer.valueOf(year_month_day[1]));
                np_year.setValue(Integer.valueOf(year_month_day[0]));
                edit_current_data = np_year.getValue()+"/"+moon_data[np_moon.getValue()-1]+"/"+day_data[np_day.getValue()-1];
            }
        });
        bt_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_edit.setVisibility(View.GONE);
                lin_calendar.setVisibility(View.VISIBLE);
                calendar.clearSelection();
                calendar.setDateSelected(CalendarDay.from(np_year.getValue(),np_moon.getValue(),np_day.getValue()),true);
                calendar.setCurrentDate(CalendarDay.from(np_year.getValue(),np_moon.getValue(),np_day.getValue()),false);

            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        });
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        inset();
        super.onCreate(savedInstanceState);
        showProgressDialog();
    }

    private void showProgressDialog() {
        dismissProgressDialog();
        // 创建ProgressDialog实例
        LayoutInflater inflater = getLayoutInflater();
        View progressView = inflater.inflate(R.layout.loading_dialog,null);
        progressDialog = new AlertDialog.Builder(CalendarActivity.this)
                .setView(progressView)
                .create();
        Window window = progressDialog.getWindow();

        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.show();
        //取消任何队列任务。

    }
    private void dismissProgressDialog() {
        // 判断ProgressDialog是否存在并且正在显示，然后关闭它
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    public static CalendarDay getCalendarDayForFirstDayOfYear(int year) {
        System.out.println("year"+year);
        return CalendarDay.from(year, Calendar.JANUARY + 1, 1);
    }

    public static CalendarDay getCalendarDayForFirstDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // 月份从 0 开始，所以要减去 1
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return CalendarDay.from(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }
    public static boolean isValidDate(String date) {
        // 定义日期格式的正则表达式
        String regex = "\\d{4}/(0?[1-9]|1[0-2])/(0?[1-9]|[1-2]\\d|3[01])";
        // 或者为yyyy/m/d这种格式
        String regex2 = "\\d{4}/[1-9]\\d?/[1-9]\\d?";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        Pattern pattern2 = Pattern.compile(regex2);
        // 创建匹配器对象
        Matcher matcher = pattern.matcher(date);
        Matcher matcher2 = pattern2.matcher(date);
        // 如果匹配成功，则进行额外的日期范围验证
        if (matcher.matches() || matcher2.matches()) {
            String[] parts = date.split("/");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            // 验证年份在 1900 到 2100 之间
            if (year < 1900 || year > 2100) {
                return false;
            }
            // 验证月份在 1 到 12 之间
            if (month < 1 || month > 12) {
                return false;
            }
            // 验证日期在 1 到 31 之间
            if (day < 1 || day > 31) {
                return false;
            }
            return true; // 所有条件都通过，返回true
        }
        return false; // 如果没有匹配成功，返回false
    }
    class DateTextWatcher implements TextWatcher {
        private EditText et_year;
        private EditText et_month;
        private EditText et_day;
        private TextView error_1;
        private TextView error_2;
        private TextView error_3;
        public DateTextWatcher(EditText et_year,  EditText et_month, EditText et_day,TextView error_1,TextView error_2,TextView error_3){
            this.et_day = et_day;
            this.et_year = et_year;
            this.et_month = et_month;
            this.error_1 = error_1;
            this.error_2 =error_2;
            this.error_3 = error_3;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String date =et_year.getText().toString()+"/"+et_month.getText().toString()+"/"+et_day.getText().toString();
            if(CalendarActivity.isValidDate(date)){
                error_1.setText("");
                error_2.setText("");
                error_3.setText("");
                flag =1;
            }
            else{
                error_1.setText("Invalid format");
                error_2.setText("use: yyyy/mm/dd");
                error_3.setText("Example: 2024/01/01");
                flag =0;
            }
        }
    }

    //转化为时间戳（毫秒）
    public static long convertStringToTimestamp(String dateString) {
        try {
            SimpleDateFormat dateFormat;
            if (dateString.indexOf("/") == 2) {
                dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            } else {
                dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            }
            Date date = dateFormat.parse(dateString);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static String[] getYearMonthDay(String dateString) {
        String[] result = new String[3];
        try {
            SimpleDateFormat dateFormat;
            if (dateString.indexOf("/") == 2) {
                dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            } else {
                dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            }
            Date date = dateFormat.parse(dateString);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            result[0] = String.valueOf(calendar.get(Calendar.YEAR));
            result[1] = String.format("%02d",calendar.get(Calendar.MONTH)+1);
            result[2] = String.format("%02d",calendar.get(Calendar.DAY_OF_MONTH));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
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
}
