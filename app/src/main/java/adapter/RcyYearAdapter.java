package adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freezecl4_zcx.R;

//年份适配器
public class RcyYearAdapter extends RecyclerView.Adapter<RcyYearAdapter.MyViewHelder> {
    private Context context;
    private LayoutInflater inflater;
    GradientDrawable defaultRoundedBg;
    GradientDrawable roundedBgPressed;
    GradientDrawable greyRoundedBg;
    private int isSelectItemPosition =-1;
    private OnClickItemListener clickListener;

    public RcyYearAdapter(Context context){
        this.context =context;
        this.inflater = LayoutInflater.from(context);
        //默认
        defaultRoundedBg = new GradientDrawable();
        defaultRoundedBg.setShape(GradientDrawable.RECTANGLE);
        defaultRoundedBg.setCornerRadius(30);
        defaultRoundedBg.setColor(Color.TRANSPARENT);
        defaultRoundedBg.setStroke(2,  Color.BLACK);

        // 创建点击后的圆角背景
        roundedBgPressed = new GradientDrawable();
        roundedBgPressed.setShape(GradientDrawable.RECTANGLE);
        roundedBgPressed.setCornerRadius(30);
        roundedBgPressed.setColor(Color.parseColor("#FDDD00"));
        roundedBgPressed.setStroke(2, Color.parseColor("#FDDD00"));
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
        View view = inflater.inflate(R.layout.item_year,parent,false);
        return new MyViewHelder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHelder holder, int position) {
        int year = 1900+position;
        holder.tv_year.setText(String.valueOf(year));
        if(position==isSelectItemPosition){
            holder.itemView.setBackground(roundedBgPressed);
            holder.tv_year.setTextColor(Color.RED);
        }else{
            holder.itemView.setBackground(defaultRoundedBg);
            holder.tv_year.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return 200;
    }
    class MyViewHelder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_year;
        private LinearLayout lin_year;
        private OnClickItemListener onClickItemListener;

        public MyViewHelder(@NonNull View itemView,OnClickItemListener onClickItemListener) {
            super(itemView);
            tv_year =itemView.findViewById(R.id.tv_year);
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
}
