package com.example.gdei.fragment4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gdei.MyContext;
import com.example.gdei.R;
import com.example.gdei.util.BufferedReaderToString;
import com.example.gdei.util.GetPostUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;

/**
 * Created by gdei on 2018/5/15.
 */

public class UserMsgActivity extends Activity implements View.OnClickListener{

    private ImageView user_msg_back;
    private TextView user_msg_tv_username, user_msg_user_name ,user_msg_atten, user_msg_friends,user_msg_Intr;
    private Button user_msg_edit_intr;
    private RelativeLayout user_msg_background_rl, user_msg_top;
    private BufferedReader br;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_msg);

        initView();
        if (user_msg_background_rl.getY() == 0){
            user_msg_top.setBackgroundColor(0000);
        }
        loadUserMsg();
    }

    private void initView(){

        user_msg_tv_username = findViewById(R.id.user_msg_tv_username);
        user_msg_user_name = findViewById(R.id.user_msg_user_name);
        user_msg_atten = findViewById(R.id.user_msg_atten);
        user_msg_friends = findViewById(R.id.user_msg_friends);
        user_msg_Intr = findViewById(R.id.user_msg_Intr);
        user_msg_back = findViewById(R.id.user_msg_back);
        user_msg_edit_intr = findViewById(R.id.user_msg_edit_intr);
        user_msg_background_rl = findViewById(R.id.user_msg_background_rl);
        user_msg_top = findViewById(R.id.user_msg_top);
        user_msg_back.setOnClickListener(this);
        user_msg_edit_intr.setOnClickListener(this);
    }
    /**
     * 加载用户相关数据
     */
    public void loadUserMsg(){
        new Thread(){
            @Override
            public void run() {
                GetPostUtil gpu = new GetPostUtil("http://192.168.42.203:8080/LoadUserMsg?userName=superL");
                br = gpu.sendGet();
                handler.sendEmptyMessage(0x00301);
            }
        }.start();
    }
    private void setMsgInView(){

        user_msg_tv_username.setText(MyContext.username);
        user_msg_user_name.setText(MyContext.username);
        user_msg_Intr.setText(MyContext.selfIntr);
        user_msg_friends.setText("粉丝\t"+MyContext.attrnMe);
        user_msg_atten.setText("关注\t"+MyContext.myAtten);

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x00301){

                try {
                    String result = BufferedReaderToString.brToString(br);
                    JSONArray jsonArray = new JSONArray(result.toString());
                    JSONObject jsonObject1 = new JSONObject(jsonArray.getString(0));
                    MyContext.username = jsonObject1.optString("username","");
                    MyContext.selfIntr= jsonObject1.optString("Intr","");
                    System.out.println(MyContext.selfIntr);
                    MyContext.myAtten=jsonObject1.optInt("myAtten");
                    MyContext.attrnMe=jsonObject1.optInt("attenMe");
                    MyContext.myBuy=jsonObject1.optInt("myBuy");
                    MyContext.saleGoodsStore=jsonObject1.optInt("saleGoodsStore");
                    JSONObject jsonObject2 = new JSONObject(jsonArray.getString(1));
                    MyContext.collection=jsonObject2.optInt("collection");
                    System.out.println(MyContext.collection);
                    JSONObject jsonObject3 = new JSONObject(jsonArray.getString(2));
                    MyContext.saleGoods=jsonObject3.optInt("saleNum");
                    JSONObject jsonObject4 = new JSONObject(jsonArray.getString(3));
                    MyContext.soldGoods=jsonObject4.optInt("soldNum");
                    br.close();
                    setMsgInView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_msg_back:
                finish();
                break;
            case R.id.user_msg_edit_intr:
                Intent intent = new Intent(UserMsgActivity.this, UserData.class);
                startActivity(intent);
                break;
        }
    }
}
