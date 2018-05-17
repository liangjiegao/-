package com.example.gdei.fragment4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.gdei.MyContext;
import com.example.gdei.R;
import com.example.gdei.util.BufferedReaderToString;
import com.example.gdei.util.GetPostUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gdei on 2018/5/11.
 */

public class UserMsgFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "UserMsgFragment";
    private View rootView;
    private LinearLayout f4_userMsgSetting;
    private TextView f4_userName, f4_userIntr;
    private RadioButton f4_love, f4_rb_myAtten, f4_rb_attenMe;
    private RecyclerView f4_userState;
    private UserStateSettingAdapter adapter;
    private List<Integer> stateNumList;
    private BufferedReader br;
    //获取列表标题
    String[] stateTexts ;

    //定义是否刷新
    public static boolean isUpdate = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment4_usermsg, container, false);
        initView();
        stateTexts = getResources().getStringArray(R.array.UserMsgFRV);
        viewUpdate();
        //f4_userState.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));


        return rootView ;
    }
    public void initView(){

        f4_userMsgSetting = rootView.findViewById(R.id.f4_userMsgSetting);
        f4_userMsgSetting.setOnClickListener(this);
        f4_userName = rootView.findViewById(R.id.f4_userName);
        f4_userName.setText(MyContext.username);
        f4_userIntr = rootView.findViewById(R.id.f4_userIntr);
        f4_userIntr.setText(MyContext.selfIntr);
        f4_love = rootView.findViewById(R.id.f4_love);
        f4_rb_myAtten = rootView.findViewById(R.id.f4_rb_myAtten);
        f4_rb_myAtten.setText("关注数\t"+MyContext.myAtten);
        f4_rb_attenMe = rootView.findViewById(R.id.f4_rb_attenMe);
        f4_rb_attenMe.setText("粉丝数\t"+MyContext.attrnMe);
        f4_userState = rootView.findViewById(R.id.f4_userState);


    }

    public void viewUpdate(){
        stateNumList = new ArrayList<>();
        stateNumList.add(MyContext.saleGoodsNum);
        stateNumList.add(MyContext.soldGoodsNum);
        stateNumList.add(MyContext.collectionNum);
        stateNumList.add(MyContext.myBuyNum);
        adapter = new UserStateSettingAdapter(this.getContext(), stateNumList,stateTexts, new UserStateSettingAdapter.MyGoodsStateItemListener() {
            @Override
            public void onClick(int pos) {

                Intent showItem = new Intent(getActivity(), UserGoodsListActivity.class);
                showItem.putExtra("pos", pos);
                showItem.putExtra("title", stateTexts[pos]);
                startActivity(showItem);

            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        f4_userState.setLayoutManager(manager);
        f4_userState.setAdapter(adapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        
        if (isUpdate){
            Log.i(TAG, "onHiddenChanged: ");
            new Thread(){
                @Override
                public void run() {
                    GetPostUtil gpu = new GetPostUtil("http://192.168.42.203:8080/LoadUserMsg?userName=superL&mode=1");
                    br = gpu.sendGet();
                    handler.sendEmptyMessage(0x00704);
                }
            }.start();
            isUpdate = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.f4_userMsgSetting:
                Intent intent = new Intent(getActivity(), UserMsgActivity.class);
                startActivity(intent);
                break;
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x00704){
                String result = BufferedReaderToString.brToString(br);
                Log.i(TAG, "handleMessage: "+result);
                try {
                    JSONArray jsonArray = new JSONArray(result.toString());

                    JSONObject jsonObject0 = new JSONObject(jsonArray.getString(0));
                    MyContext.collectionNum=jsonObject0.optInt("collection");
                    System.out.println(MyContext.collectionNum);

                    JSONObject jsonObject1 = new JSONObject(jsonArray.getString(1));
                    MyContext.saleGoodsNum=jsonObject1.optInt("saleNum");

                    JSONObject jsonObject2 = new JSONObject(jsonArray.getString(2));
                    MyContext.soldGoodsNum=jsonObject2.optInt("soldNum");

                    JSONObject jsonObject3 = new JSONObject(jsonArray.getString(3));
                    MyContext.myBuyNum=jsonObject3.optInt("buyNum");
                    viewUpdate();
                    br.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
