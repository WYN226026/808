package adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freezecl4_zcx.R;

//日历中的月份配置器
public class RCYMonthAdapter extends RecyclerView.Adapter<RCYMonthAdapter.MyViewHelder> {
    private Context context;
    private LayoutInflater inflater;
    GradientDrawable defaultRoundedBg;
    GradientDrawable roundedBgPressed;
    private int isSelectItemPosition =-1;
    private OnClickItemListener clickListener;

    public RCYMonthAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        //默认
        defaultRoundedBg = new GradientDrawable();
        defaultRoundedBg.setShape(GradientDrawable.RECTANGLE);
        defaultRoundedBg.setCornerRadius(30);
        defaultRoundedBg.setColor(Color.TRANSPARENT);

        // 创建点击后的圆角背景
        roundedBgPressed = new GradientDrawable();
        roundedBgPressed.setShape(GradientDrawable.RECTANGLE);
        roundedBgPressed.setCornerRadius(30);
        roundedBgPressed.setColor(Color.parseColor("#FDDD00"));
    }

    public void setIsSelectItemPosition(int position){
        this.isSelectItemPosition  = position;
        notifyDataSetChanged();
    }


    public void setOnClickListener(OnClickItemListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHelder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_month,parent,false);
        return new MyViewHelder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHelder holder, int position) {
        String month =getMonthAbbreviation(position+1);
        System.out.println("month"+month);
        holder.tv_month.setText(month);
        if(position==isSelectItemPosition){
            holder.itemView.setBackground(roundedBgPressed);
            holder.tv_month.setTextColor(Color.RED);
        }else{
            holder.itemView.setBackground(defaultRoundedBg);
            holder.tv_month.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return 11;
    }

    class MyViewHelder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_month;
        private OnClickItemListener onClickItemListener;
        public MyViewHelder(@NonNull View itemView ,OnClickItemListener onClickItemListener) {
            super(itemView);
            tv_month = itemView.findViewById(R.id.tv_month);
            this.onClickItemListener = onClickItemListener;
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            if(onClickItemListener !=null){
                onClickItemListener.onItemClick(view,getPosition());
            }
        }
    }

    public static String getMonthAbbreviation(int monthNumber) {
        switch (monthNumber) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return "Invalid Month";
        }
    }
}
