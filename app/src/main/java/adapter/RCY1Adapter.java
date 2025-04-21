package adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freezecl4_zcx.R;

import java.util.List;

import entity.User;
//用户信息界面中 rcy1的适配器
public class RCY1Adapter extends RecyclerView.Adapter<RCY1Adapter.MyViewHelder> {

    public OnClickItemListener itemListener;
    public OnLongItemListener onLongItemListener;
    private int isSelectItemPosition =-1; //是否被选中。
    private Context context;
    private List<User> users;
    private LayoutInflater inflater;
    public void setItemListener(OnClickItemListener itemListener) {
        this.itemListener = itemListener;
    }
    public void setOnLongItemListener(OnLongItemListener onLongItemListener){
        this.onLongItemListener = onLongItemListener;
    }
    public RCY1Adapter(Context context , List<User> users){
        this.context = context;
        this.users = users;
        inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHelder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.information_list,parent,false);
        return new MyViewHelder(view,itemListener,onLongItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHelder holder, int position) {
        User user = users.get(position);
        int showID = position +1;
        if(showID>=10){
            holder.tv_id.setText(showID+".");
        }else{
            holder.tv_id.setText("0"+showID+".");
        }
        holder.tv_age.setText(user.getAge());
        String name = user.getFirstName()+"  "+user.getLastName();
        name = TextUtils.ellipsize(name,holder.tv_name.getPaint(),dp2px(130),TextUtils.TruncateAt.END).toString();
        holder.tv_name.setText(name);
        if(user.isGender()){
            holder.iv_man_woman.setBackgroundResource(R.drawable.checkbox_woman);
        }else{
            holder.iv_man_woman.setBackgroundResource(R.drawable.checkbox_man);
        }
        if(position==isSelectItemPosition){
            holder.itemView.setBackgroundResource(R.drawable.list_line);
        }else {
            holder.itemView.setBackgroundResource(R.color.touMing);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public  void setIsSelectItemPosition(int postion){
        this.isSelectItemPosition=postion;
        notifyDataSetChanged();
    }
    public void setUsers(List<User> users) {
        this.users = users;
    }
    class MyViewHelder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        private TextView tv_id;
        private TextView tv_name;
        private TextView tv_age;
        private ImageView iv_man_woman;
        private OnClickItemListener listener;
        private OnLongItemListener onLongItemListener;
        public MyViewHelder(@NonNull View itemView,OnClickItemListener onClickItemListener,OnLongItemListener onLongItemListener) {
            super(itemView);
            tv_id = itemView.findViewById(R.id.tv_id);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_age = itemView.findViewById(R.id.tv_age);
            iv_man_woman = itemView.findViewById(R.id.iv_wm);
            this.onLongItemListener = onLongItemListener;
            listener = onClickItemListener;
            itemView.setOnLongClickListener(this::onLongClick);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            if(listener!=null){
                listener.onItemClick(view,getPosition());
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
    public static int dp2px(float dpValue) {
        return (int) (0.5F + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }
}
