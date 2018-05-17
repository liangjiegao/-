package com.example.gdei.fragment4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gdei.R;
import com.example.gdei.util.BufferedReaderToString;
import com.example.gdei.util.GetPostUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SuperL on 2018/5/16.
 */

public class UserGoodsListActivity extends Activity{
    private static final String TAG = "UserGoodsListActivity";
    private RecyclerView myGoodsList;
    private ImageView mybuy_back;
    private TextView mybuy_title;
    UserGoodsAdapter adapter;
    private List<Map<String, String>> list;

    private int UMFPos = -1;
    private String title = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybuy);
        list = new ArrayList<>();
        initView();
        getShowFromPosition();
    }
    private void initView(){
        myGoodsList = findViewById(R.id.mybuy_goodsList);
        mybuy_title = findViewById(R.id.mybuy_title);
        mybuy_back = findViewById(R.id.mybuy_back);
        mybuy_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void getShowFromPosition(){
        String myGoodsState = "";
        Intent UMFIntent = getIntent();
        if (UMFIntent != null){
            UMFPos = UMFIntent.getIntExtra("pos",-1);
            title = UMFIntent.getStringExtra("title");
            mybuy_title.setText(title);
        }
        switch (UMFPos){
            case -1:
                break;
            case 0:
                myGoodsState = "mySeGoods";
                break;
            case 1:
                myGoodsState = "mySoGoods";
                break;
            case 2:
                myGoodsState = "myCollGoods";
                break;
            case 3:
                myGoodsState = "myBuyGoods";
                break;
        }
        loadUserGoods(myGoodsState);
    }
    private void showUserSale(){
        adapter = new UserGoodsAdapter(this, list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myGoodsList.setLayoutManager(manager);
        myGoodsList.setAdapter(adapter);
    }
    private BufferedReader br;
    private void loadUserGoods(final String goodsState){

        new Thread(){
            @Override
            public void run() {
                GetPostUtil gpu = new GetPostUtil("http://192.168.42.203:8080/LoadUserGoodsFromState?userName=superL&goodsState="+goodsState);
                br = gpu.sendGet();
                handler.sendEmptyMessage(0x00704);
            }
        }.start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x00704){
                String result = BufferedReaderToString.brToString(br);
                if (list != null){
                    list.clear();
                }
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String goodsName = jsonObject.optString("name");
                        double goodsPrice = jsonObject.optDouble("price");
                        Map<String, String> map = new HashMap<>();
                        map.put("goodsName", goodsName);
                        map.put("goodsPrice", goodsPrice+"");
                        list.add(map);
                        Log.i(TAG, "handleMessage: "+i);
                    }
                    showUserSale();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
