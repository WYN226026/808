package adapter;

import static android.graphics.Color.rgb;

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

import java.util.List;

import entity.UserInfo;
//用户信息界面中rcy2 中适配器。
public class RCY2Adapter extends RecyclerView.Adapter<RCY2Adapter.MyViewHelder>{
    private Context context;
    private List<UserInfo> userInfos;
    private LayoutInflater inflater;
    GradientDrawable defaultRoundedBg;
    GradientDrawable roundedBgPressed;
    GradientDrawable greyRoundedBg;

    private int isSelectItemPosition =-1;
    private OnClickItemListener clickListener;
    private OnLongItemListener longListener;

    public RCY2Adapter(Context context, List<UserInfo> userInfos){
        this.context = context;
        this.userInfos = userInfos;
        this.inflater = LayoutInflater.from(context);


        // 创建默认的圆角背景
        defaultRoundedBg = new GradientDrawable();
        defaultRoundedBg.setShape(GradientDrawable.RECTANGLE);
        defaultRoundedBg.setCornerRadius(25);
        defaultRoundedBg.setColor(Color.TRANSPARENT);
        defaultRoundedBg.setStroke(2, Color.parseColor("#02373B"));

        // 创建点击后的圆角背景
        roundedBgPressed = new GradientDrawable();
        roundedBgPressed.setShape(GradientDrawable.RECTANGLE);
        roundedBgPressed.setCornerRadius(25);
        roundedBgPressed.setColor(Color.parseColor("#EA5A5A"));
        roundedBgPressed.setStroke(2, Color.parseColor("#EA5A5A"));

        // 创建没有数据时的灰色背景
        greyRoundedBg = new GradientDrawable();
        greyRoundedBg.setShape(GradientDrawable.RECTANGLE);
        greyRoundedBg.setCornerRadius(25);
        greyRoundedBg.setColor(Color.TRANSPARENT);
        greyRoundedBg.setStroke(2, Color.parseColor("#02373B"));
    }

    @NonNull
    @Override
    public MyViewHelder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_rcy2,parent,false);
        return new MyViewHelder(view,clickListener,longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHelder holder, int position) {
        System.out.println("position: "+position);
        if(userInfos.size()>position){
            holder.itemView.setEnabled(true);
            System.out.println("userinfo__"+userInfos.get(position).toString());
            holder.itemView.setBackground(defaultRoundedBg);
            holder.tv_position.setTextColor(Color.parseColor("#02373B"));
            holder.tv_date.setTextColor(Color.parseColor("#02373B"));
            if(position==isSelectItemPosition){
                holder.itemView.setBackground(roundedBgPressed);
            }else{
                holder.itemView.setBackground(defaultRoundedBg);
            }
            UserInfo userInfo = userInfos.get(position);
            if(userInfo.getWork_date()==""&&userInfo.getWork_date()==null){
                holder.tv_date.setText("?");
                holder.tv_date.setTextColor(rgb(255,0,0));
            }else{
                holder.tv_date.setText(userInfo.getWork_date());
            }
        }else{
            holder.itemView.setEnabled(false);
            holder.itemView.setBackground(greyRoundedBg);
            holder.tv_position.setTextColor(Color.parseColor("#02373B"));
            holder.tv_date.setText("");
        }
        if(position>8){
            holder.tv_position.setText(String.valueOf(position+1));
        }else{
            holder.tv_position.setText("0"+String.valueOf(position+1));
        }

    }

    @Override
    public int getItemCount() {
        return 50;
    }
    public void setUserInfos(List<UserInfo> userInfos){
        this.userInfos = userInfos;
    }

    public void setOnClickListener(OnClickItemListener listener){
        this.clickListener = listener;
    }
    public void setOnLongClickListener(OnLongItemListener listener){
        this.longListener = listener;
    }
    public void setIsSelectItemPosition(int position){
        this.isSelectItemPosition = position;
        notifyDataSetChanged();
    }

    class MyViewHelder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        private TextView tv_position;
        private TextView tv_date;

        private OnClickItemListener onClickItemListener;
        private OnLongItemListener onLongItemListener;
        public MyViewHelder(@NonNull View itemView,OnClickItemListener onClickItemListener,OnLongItemListener onLongItemListener) {
            super(itemView);
            tv_position = itemView.findViewById(R.id.tv_position);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_position.setTextColor(Color.WHITE);
            tv_date.setTextColor(Color.WHITE);

            this.onClickItemListener = onClickItemListener;
            this.onLongItemListener = onLongItemListener;
            itemView.setOnClickListener(this::onClick);
            itemView.setOnLongClickListener(this::onLongClick);
        }


        @Override
        public void onClick(View view) {
            if(onClickItemListener!=null){
                onClickItemListener.onItemClick(view,getPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if(onLongItemListener!=null){
                onLongItemListener.OnItemLongClick(view,getPosition());
            }
            return false;
        }
    }
}
