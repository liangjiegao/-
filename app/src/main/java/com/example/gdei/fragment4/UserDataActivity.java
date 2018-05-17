package com.example.gdei.fragment4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.gdei.R;

/**
 * Created by gdei on 2018/5/15.
 */

public class UserDataActivity extends Activity implements View.OnClickListener{

    private RecyclerView data_item;
    private ImageButton data_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        initView();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        data_item.setLayoutManager(manager);
        UserDataAdapter adapter = new UserDataAdapter(this, null, new UserDataAdapter.DataItemListener() {
            @Override
            public void onClick() {


            }
        });
        data_item.setAdapter(adapter);

    }
    private void initView(){

        data_item = findViewById(R.id.data_item);
        data_back = findViewById(R.id.data_back);
        data_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.data_back:
                finish();
                break;
        }
    }
}
