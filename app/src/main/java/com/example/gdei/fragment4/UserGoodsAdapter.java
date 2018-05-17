package com.example.gdei.fragment4;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.gdei.R;

import java.util.zip.Inflater;

/**
 * Created by SuperL on 2018/5/16.
 */

public class UserGoodsAdapter extends RecyclerView.Adapter<UserGoodsAdapter.MyBuyGoodsItemHolder> {

    Context context;
    public UserGoodsAdapter(Context context){
        this.context = context;
    }
    @Override
    public MyBuyGoodsItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyBuyGoodsItemHolder holder = new MyBuyGoodsItemHolder(LayoutInflater.from(context).
                inflate(R.layout.mybuy_goods_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyBuyGoodsItemHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 1;
    }

    class MyBuyGoodsItemHolder extends RecyclerView.ViewHolder{

        public MyBuyGoodsItemHolder(View itemView) {
            super(itemView);
        }
    }
}
