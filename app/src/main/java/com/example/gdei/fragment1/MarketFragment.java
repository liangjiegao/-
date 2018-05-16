package com.example.gdei.fragment1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.gdei.R;
import com.example.gdei.goodsAbstract.GoodsAbstract;
import com.example.gdei.util.BufferedReaderToString;
import com.example.gdei.util.GetPostUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gdei on 2018/5/10.
 */

public class MarketFragment extends Fragment{
    private static final String TAG = "MarketFragment";
    private View rootView;
    private RecyclerView sellerList;
    private Map<String, String> saleMsgMap;
    private List<Map<String, String>> totalList;
    private List<Map<String, String>> seekList;
    private EditText f1_goods_seek;

    private boolean isFirst = true;
    public static boolean isUpDate = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment1_market, container, false);
        initView();
        getSaleMsg();
        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.i(TAG, "onHiddenChanged: "+hidden);
        if (isUpDate){
            getSaleMsg();
            isUpDate = false;
        }
    }

    public void initView(){
        sellerList = rootView.findViewById(R.id.main_rv);
        f1_goods_seek = rootView.findViewById(R.id.f1_goods_seek);
        f1_goods_seek.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                final String goodsName = f1_goods_seek.getText().toString();
                if (!isFirst){
                    new Thread(){
                        @Override
                        public void run() {
                            Log.i(TAG, "run: 输入完成");
                            GetPostUtil gpu = new GetPostUtil("http://192.168.42.203:8080/seekGoods?goodsName="+goodsName);
                            br = gpu.sendGet();
                            handler.sendEmptyMessage(0x00702);
                        }
                    }.start();
                }
            }
        });
    }

    private static BufferedReader br;

    public void getSaleMsg(){
        new Thread(){
            @Override
            public void run() {
                GetPostUtil gpu = new GetPostUtil("http://192.168.42.203:8080/LoadSeller");
                br = gpu.sendGet();
                if (br != null){
                    Log.i(TAG, "run:获取商家成功");
                }
                handler.sendEmptyMessage(0x00701);
            }
        }.start();
    }
    private void setRecyclerView(final List<Map<String, String>> list){
        SellerRVAdapter adapter = new SellerRVAdapter(this.getContext(), list, new SellerRVAdapter.ItemListener() {
            @Override
            public void interGoods(int pos) {
                Intent intent = new Intent(getActivity(), GoodsAbstract.class);
                Bundle bundle = new Bundle();
                String sellerName = list.get(pos).get("username");
                int goodsId = Integer.parseInt(list.get(pos).get("goodsId"));
                bundle.putString("sellerName",sellerName);
                bundle.putInt("goodsId", goodsId);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        sellerList.setLayoutManager(layoutManager);
        sellerList.setAdapter(adapter);
    }
    private void seekGoods(List<Map<String, String>> list){
        String result = null;
        try{
            Log.i(TAG, "seekGoods: 查询商品");
            result = BufferedReaderToString.brToString(br);

            JSONArray jsonArray = new JSONArray(result.toString());
            for (int i = 0; i < jsonArray.length(); i++ ){
                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                System.out.println(jsonObject.getString("username"));
                saleMsgMap = new HashMap<>();
                saleMsgMap.put("username", jsonObject.getString("username"));
                saleMsgMap.put("price", jsonObject.getString("price"));
                saleMsgMap.put("name", jsonObject.getString("name"));
                saleMsgMap.put("goodsIntr", jsonObject.getString("goodsIntr"));
                saleMsgMap.put("goodsId", jsonObject.getInt("goodsId")+"");
                list.add(saleMsgMap);
            }
            br.close();
            setRecyclerView(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x00701){
                if (totalList == null){
                    totalList = new ArrayList<>();
                    isFirst = false;
                }else {
                    totalList.clear();
                }
                seekGoods(totalList);
            }
            if (msg.what == 0x00702){
                if (seekList == null){
                    seekList = new ArrayList<>();
                }else {
                    seekList.clear();
                }
                seekGoods(seekList);
            }
        }
    };
}
