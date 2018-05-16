package com.example.gdei.fragment1;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gdei.MyContext;
import com.example.gdei.R;

import java.util.List;
import java.util.Map;

/**
 * Created by gdei on 2018/5/10.
 */

public class SellerRVAdapter extends RecyclerView.Adapter<SellerRVAdapter.MyHolder> {

    private List<Map<String, String>> sellerList;
    private Context myContext;

    private ItemListener itemListener;
    public SellerRVAdapter(Context myContext, List<Map<String, String>> sellerList, ItemListener itemListener){
        this.sellerList = sellerList;
        this.myContext = myContext;
        this.itemListener = itemListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(LayoutInflater.from(myContext).
                inflate(R.layout.adapter_seller_list, parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        //for (int i = 0; i < sellerList.size(); i++){
        System.out.println(sellerList.get(position).get("username"));
        holder.adapter_sellerName.setText(sellerList.get(position).get("username"));
        holder.adapter_price.setText(sellerList.get(position).get("price"));
        holder.adapter_goodsName.setText(sellerList.get(position).get("name"));
        holder.adapter_goodsIntr.setText(sellerList.get(position).get("Intr"));
       // }
        setItemListener(holder,position);
    }

    @Override
    public int getItemCount() {
        return sellerList.size();
    }
    private void setItemListener(MyHolder mh, final int pos){
        mh.seller_list_carview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.interGoods(pos);
            }
        });
    }
    class MyHolder extends RecyclerView.ViewHolder{
        TextView adapter_sellerName, adapter_price, adapter_goodsName, adapter_goodsIntr;
        CardView seller_list_carview;
        public MyHolder(View itemView) {
            super(itemView);
            adapter_sellerName = itemView.findViewById(R.id.adapter_sellerName);
            adapter_price = itemView.findViewById(R.id.adapter_price);
            adapter_goodsName = itemView.findViewById(R.id.adapter_goodsName);
            adapter_goodsIntr = itemView.findViewById(R.id.adapter_goodsIntr);
            seller_list_carview = itemView.findViewById(R.id.seller_list_carview);
        }
    }
    interface ItemListener{
        //点击goods项
        void interGoods(int pos);
    }
}
