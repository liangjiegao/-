package com.example.gdei.fragment4;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gdei.MyContext;
import com.example.gdei.R;

import java.util.List;

/**
 * Created by gdei on 2018/5/11.
 */

public class UserStateSettingAdapter extends RecyclerView.Adapter<UserStateSettingAdapter.StateHolder> {

    int[] stateIcons = {R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round,
                          R.mipmap.ic_launcher_round};
    String[] stateTexts;
    private Context context;
    private List<Integer> list;
    private MyGoodsStateItemListener stateItemListener;
    public UserStateSettingAdapter(Context context, List<Integer> list,  String[] stateTexts,MyGoodsStateItemListener listener){
        this.list = list;
        this.context = context;
        this.stateItemListener = listener;
        this.stateTexts = stateTexts;
    }


    @Override
    public StateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        StateHolder holder = new StateHolder(LayoutInflater.from(context).
                inflate(R.layout.adapter_user_state, parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(StateHolder holder, final int position) {
        holder.state_icon.setBackgroundResource(stateIcons[position]);
        holder.state_name.setText(stateTexts[position]);
        holder.state_num.setText(list.get(position)+"");
        holder.state_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateItemListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class StateHolder extends RecyclerView.ViewHolder{
        ImageView state_icon;
        TextView state_name, state_num;
        CardView state_body;
        public StateHolder(View itemView) {
            super(itemView);
            state_body = itemView.findViewById(R.id.state_body);
            state_icon = itemView.findViewById(R.id.state_icon);
            state_name = itemView.findViewById(R.id.state_name);
            state_num = itemView.findViewById(R.id.state_num);
        }
    }
    interface MyGoodsStateItemListener{
        //点击 我的发布， 我的卖出等 项
        void onClick(int pos);
    }
}
