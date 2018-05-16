package com.example.gdei.fragment4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.gdei.MyContext;
import com.example.gdei.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gdei on 2018/5/11.
 */

public class UserMsgFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private LinearLayout f4_userMsgSetting;
    private TextView f4_userName, f4_userIntr;
    private RadioButton f4_love, f4_rb_myAtten, f4_rb_attenMe;
    private RecyclerView f4_userState;
    private UserStateSettingAdapter adapter;
    private List<Integer> stateNumList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment4_usermsg, container, false);
        initView();
        //f4_userState.addItemDecoration(new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL_LIST));
        adapter = new UserStateSettingAdapter(this.getContext(), stateNumList);
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        f4_userState.setLayoutManager(manager);

        f4_userState.setAdapter(adapter);
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
        stateNumList = new ArrayList<>();
        stateNumList.add(MyContext.saleGoods);
        stateNumList.add(MyContext.soldGoods);
        stateNumList.add(MyContext.myBuy);
        stateNumList.add(MyContext.collection);

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
}
