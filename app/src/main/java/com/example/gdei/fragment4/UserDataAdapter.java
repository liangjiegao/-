package com.example.gdei.fragment4;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gdei.R;

import java.util.List;

/**
 * Created by gdei on 2018/5/15.
 */

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.UDAHolder>{

    String[] itemNames = {"头像","背景图","性别","生日","常住","简介","收货地址"};
    private Context context;

    public UserDataAdapter(Context context, List list){
        this.context = context;
    }

    @Override
    public UDAHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UDAHolder holder = new UDAHolder(LayoutInflater.from(context).
                inflate(R.layout.adapter_user_date_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(UDAHolder holder, int position) {

        holder.itemName.setText(itemNames[position]);

    }

    @Override
    public int getItemCount() {
        return itemNames.length;
    }

    class UDAHolder extends RecyclerView.ViewHolder{

        private TextView itemName;
        public UDAHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
        }

    }
}
