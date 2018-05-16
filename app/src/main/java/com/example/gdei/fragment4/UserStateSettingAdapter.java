package com.example.gdei.fragment4;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
    String[] stateTexts = {"我的发布", "我的卖出","我的收藏","我买到的"};


    private Context context;
    private List<Integer> list;
    public UserStateSettingAdapter(Context context, List<Integer> list){
        this.list = list;
        this.context = context;
    }


    @Override
    public StateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        StateHolder holder = new StateHolder(LayoutInflater.from(context).
                inflate(R.layout.adapter_user_state, parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(StateHolder holder, int position) {
        holder.state_icon.setBackgroundResource(stateIcons[position]);
        holder.state_name.setText(stateTexts[position]);
        holder.state_num.setText(list.get(position)+"");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class StateHolder extends RecyclerView.ViewHolder{
        ImageView state_icon;
        TextView state_name, state_num;
        public StateHolder(View itemView) {
            super(itemView);
            state_icon = itemView.findViewById(R.id.state_icon);
            state_name = itemView.findViewById(R.id.state_name);
            state_num = itemView.findViewById(R.id.state_num);
        }
    }
}
