package com.example.gdei.fragment2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.gdei.MyContext;
import com.example.gdei.R;
import com.example.gdei.fragment1.MarketFragment;
import com.example.gdei.fragment4.UserMsgFragment;
import com.example.gdei.util.GetPostUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by gdei on 2018/5/11.
 */

public class IntroducedFragment extends Fragment {

    private View rootView;
    private EditText f2_bt_goodsName, f2_et_goodsIntr,f2_et_price;
    private Button f2_b_introduce;
    private LinearLayout f2_ll_main;
    private BufferedReader br;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment2_introduced, container, false);
        initView();

        return rootView;

    }
    private void initView(){

        f2_bt_goodsName = rootView.findViewById(R.id.f2_bt_goodsName);
        f2_et_goodsIntr = rootView.findViewById(R.id.f2_et_goodsIntr);
        f2_et_price = rootView.findViewById(R.id.f2_et_price);
        f2_b_introduce = rootView.findViewById(R.id.f2_b_introduce);
        f2_ll_main = rootView.findViewById(R.id.f2_ll_main);

        f2_b_introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击");
                if (verificationInput()){
                    System.out.println("输入正确");
                    introduceGoods();
                }
            }
        });
    }
    private String goodsName;
    private String goodsIntr;
    private String goodsPrice;
    private boolean verificationInput(){
        goodsName = f2_bt_goodsName.getText().toString();
        goodsIntr = f2_et_goodsIntr.getText().toString();
        goodsPrice = f2_et_price.getText().toString();
        if (goodsName.equals("")){
            Snackbar.make(f2_ll_main, "物品标题必须填写！！", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (goodsIntr.equals("")){
            Snackbar.make(f2_ll_main, "物品描述必须填写！！", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (goodsPrice.equals("")){
            Snackbar.make(f2_ll_main, "物品价格必须填写！！", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private void clearText(){
        f2_bt_goodsName.setText("");
        f2_et_goodsIntr.setText("");
        f2_et_price.setText("");
    }
    private void introduceGoods(){
        new Thread(){
            @Override
            public void run() {
                GetPostUtil gpu = new GetPostUtil("http://192.168.42.203:8080/IntroduceGoods?goodsStore="+
                        MyContext.saleGoodsStore+"&goodsName="+goodsName+"&goodsIntr="+goodsIntr+"&price="+goodsPrice);
                br = gpu.sendGet();
                handler.sendEmptyMessage(0x00501);
            }
        }.start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x00501) {
                String str = "";
                StringBuffer result = new StringBuffer();
                try {
                    while ((str = br.readLine()) != null) {
                        result.append(str);
                    }
                    System.out.println(result);
                    if (result.toString().equals("1")) {
                        Snackbar.make(f2_ll_main, "物品发布成功！", Snackbar.LENGTH_LONG).show();
                        MarketFragment.isUpDate = true;
                        UserMsgFragment.isUpdate = true;
                        clearText();
                    }else {
                        Snackbar.make(f2_ll_main, "物品发布失败！", Snackbar.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

}
