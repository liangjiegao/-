package com.example.gdei.fragment4;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gdei.R;

import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by SuperL on 2018/5/16.
 */

public class UserGoodsAdapter extends RecyclerView.Adapter<UserGoodsAdapter.MyBuyGoodsItemHolder> {
    private static final String TAG = "UserGoodsAdapter";
    Context context;
    List<Map<String, String>> list;
    public UserGoodsAdapter(Context context, List<Map<String, String>> list){
        this.context = context;
        this.list = list;
        Log.i(TAG, "UserGoodsAdapter: "+list.size());
    }
    @Override
    public MyBuyGoodsItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyBuyGoodsItemHolder holder = new MyBuyGoodsItemHolder(LayoutInflater.from(context).
                inflate(R.layout.mybuy_goods_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyBuyGoodsItemHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: goodsName"+list.get(position).get("goodsName").toString());
        holder.mybuy_goods_name.setText(list.get(position).get("goodsName").toString());
        holder.mybuy_goods_price.setText(list.get(position).get("goodsPrice").toString());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyBuyGoodsItemHolder extends RecyclerView.ViewHolder{
        TextView mybuy_goods_name , mybuy_goods_price;

        public MyBuyGoodsItemHolder(View itemView) {
            super(itemView);
            mybuy_goods_name = itemView.findViewById(R.id.mybuy_goods_name);
            mybuy_goods_price = itemView.findViewById(R.id.mybuy_goods_price);
        }
    }
}
