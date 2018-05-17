package com.example.gdei.goodsAbstract;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gdei.MyContext;
import com.example.gdei.R;
import com.example.gdei.fragment1.MarketFragment;
import com.example.gdei.fragment4.UserMsgFragment;
import com.example.gdei.util.BufferedReaderToString;
import com.example.gdei.util.GetPostUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;

/**
 * Created by gdei on 2018/5/14.
 */

public class GoodsAbstract extends Activity implements View.OnClickListener{
    private static final String TAG = "GoodsAbstract";
    private RelativeLayout goods_msg_body;
    private ImageButton goods_msg_ib_back_home;
    private TextView goods_msg_goods_price, goods_msg_seller_name, goods_msg_location, goods_msg_tv_praise2;
    private TextView goods_msg_tv_goods_Intr, goods_msg_tv_goodsName;
    private Button goods_msg_want_buy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_msg);
        initView();
        getSellerAndGoodsMsg();
    }
    public void initView(){
        goods_msg_body = findViewById(R.id.goods_msg_body);
        goods_msg_goods_price = findViewById(R.id.goods_msg_goods_price);
        goods_msg_seller_name = findViewById(R.id.goods_msg_seller_name);
        goods_msg_location = findViewById(R.id.goods_msg_location);
        goods_msg_tv_praise2 = findViewById(R.id.goods_msg_tv_praise2);
        goods_msg_tv_goods_Intr = findViewById(R.id.goods_msg_tv_goods_Intr);
        goods_msg_tv_goodsName = findViewById(R.id.goods_msg_tv_goodsName);
        goods_msg_ib_back_home = findViewById(R.id.goods_msg_ib_back_home);
        goods_msg_want_buy = findViewById(R.id.goods_msg_want_buy);

        goods_msg_ib_back_home.setOnClickListener(this);
        goods_msg_want_buy.setOnClickListener(this);
    }

    private BufferedReader br;
    private  String sellerName;
    private int goodsId;
    private void getSellerAndGoodsMsg(){
        Intent intent = getIntent();
        if (intent != null){
            Bundle bundle = intent.getExtras();
            if (bundle != null){
                sellerName  = bundle.getString("sellerName");
                goodsId = bundle.getInt("goodsId");
                if (!TextUtils.isEmpty(sellerName) && goodsId != 0){
                    new Thread(){
                        @Override
                        public void run() {
                            GetPostUtil gpu = new GetPostUtil("http://192.168.42.203:8080/LoadGoods?username="+sellerName+"&goodsId="+goodsId);
                            br = gpu.sendGet();
                            handler.sendEmptyMessage(0x00302);
                        }
                    }.start();
                }
            }
        }
    }
    String goodsIntr;
    double price;
    String goodsName;
    int seStore;
    private void setViewText(){
        Log.i(TAG, "setViewText: ");
        goods_msg_goods_price.setText(price+"");
        goods_msg_seller_name.setText(sellerName);
        goods_msg_tv_praise2.setText(price+"");
        goods_msg_tv_goodsName.setText(goodsName);
        goods_msg_tv_goods_Intr.setText(goodsIntr);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x00302){
                try{
                    String result = BufferedReaderToString.brToString(br);
                    JSONArray jsonArray = new JSONArray(result.toString());
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    goodsIntr  = jsonObject.optString("goodsIntr","");
                    price = jsonObject.optDouble("price",0.0);
                    goodsName = jsonObject.optString("name","");
                    Log.i(TAG, "handleMessage: setViewText");
                    setViewText();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if (msg.what == 0x00303){
                String result = BufferedReaderToString.brToString(br);
                Log.i(TAG, "handleMessage: "+result);
                if (result.equals("1")){
                    Snackbar.make(goods_msg_body, "购买成功", Snackbar.LENGTH_SHORT).show();
                    MarketFragment.isUpDate = true;
                    UserMsgFragment.isUpdate = true;
                    handler.sendEmptyMessageDelayed(0x000, 500);
                }else {
                    Snackbar.make(goods_msg_body, "购买失败", Snackbar.LENGTH_SHORT).show();
                }
            }
            if (msg.what == 0x000){
                finish();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goods_msg_ib_back_home:
                finish();
                break;
            case R.id.goods_msg_want_buy:
                new Thread(){
                    @Override
                    public void run() {
                        GetPostUtil gpu = new GetPostUtil("http://192.168.42.203:8080/UserBuy?seGoodsId="+goodsId+"&buyStoreId="+ MyContext.myBuyStore);
                        br = gpu.sendGet();
                        handler.sendEmptyMessage(0x00303);
                    }
                }.start();
        }
    }
}
