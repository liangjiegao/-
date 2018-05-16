package com.example.gdei;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.gdei.fragment1.MarketFragment;
import com.example.gdei.fragment2.IntroducedFragment;
import com.example.gdei.fragment3.NewsFragment;
import com.example.gdei.fragment4.UserMsgFragment;
import com.example.gdei.util.GetPostUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    private BottomNavigationBar bnb;    //底部导航栏
    /**
     * 四个fragment
     */
    private Fragment fragment1;
    private Fragment fragment2;
    private Fragment fragment3;
    private Fragment fragment4;
    private Fragment nowFragment;
    //fragment事务相关变量
    FragmentManager manager;
    FragmentTransaction transaction;

    private BufferedReader br;      //服务器响应的数据
    private String userName;

    private FrameLayout main_fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        addFirstFragment();
        loadUserMsg();
    }
    //初始化界面
    public void initView(){
        bnb = findViewById(R.id.home_bnb);
        bnb.addItem(new BottomNavigationItem(R.mipmap.ic_launcher,"跳蚤市场"))
            .addItem(new BottomNavigationItem(R.mipmap.ic_launcher,"发布"))
            .addItem(new BottomNavigationItem(R.mipmap.ic_launcher,"消息"))
            .addItem(new BottomNavigationItem(R.mipmap.ic_launcher,"我的"))
            .setFirstSelectedPosition(0)
            .initialise();
        bnb.setTabSelectedListener(this);
    }

    /**
     * 添加显示的第一个fragment
     */
    public void addFirstFragment(){
        if (fragment2 == null){
            fragment2 = new IntroducedFragment();
        }
        if (fragment1 == null){
            fragment1 = new MarketFragment();
        }
        nowFragment = fragment2;
        fragment2 = null;
        changeFragment(nowFragment, fragment1);
    }

    /**
     * 控制fragment之间的切换
     * @param fromFragment
     * @param toFragment
     */
    public void changeFragment(Fragment fromFragment, Fragment toFragment){
        if (toFragment != null){
            nowFragment = toFragment;
        }
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        if (!toFragment.isAdded()){
            transaction.hide(fromFragment).add(R.id.main_fragment,toFragment).commit();
        }else {
            transaction.hide(fromFragment).show(toFragment).commit();
        }

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
    private MarketFragment marketFragment;
    @Override
    public void onTabSelected(int position) {
        switch (position){
            case 0:
                if (fragment1 == null){
                    fragment1 = new MarketFragment();
                }
                changeFragment(nowFragment, fragment1);
                break;
            case 1:
                if (fragment2 == null){
                    fragment2 = new IntroducedFragment();
                }
                changeFragment(nowFragment, fragment2);
                break;
            case 2:
                if (fragment3 == null){
                    fragment3 = new NewsFragment();
                }
                changeFragment(nowFragment, fragment3);
                break;
            case 3:
                if (fragment4 == null){
                    fragment4 = new UserMsgFragment();
                }
                changeFragment(nowFragment, fragment4);
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x00301){
                String str = "";
                StringBuffer result = new StringBuffer();
                try {
                    while ((str = br.readLine()) != null) {
                        result.append(str);
                    }
                    //System.out.println(result.toString());
                    JSONArray jsonArray = new JSONArray(result.toString());
                    JSONObject jsonObject1 = new JSONObject(jsonArray.getString(0));
                    MyContext.username = jsonObject1.optString("username","");
                    //System.out.println(jsonObject1.getString("username"));
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
                    // return list;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
