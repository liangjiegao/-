package com.example.gdei.fragment4;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.gdei.R;

/**
 * Created by SuperL on 2018/5/16.
 */

public class UserGoodsListActivity extends Activity{

    private RecyclerView myGoodsList;
    private ImageView mybuy_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybuy);

        initView();
    }
    private void initView(){
        mybuy_back = findViewById(R.id.mybuy_back);
        mybuy_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myGoodsList = findViewById(R.id.mybuy_goodsList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myGoodsList.setLayoutManager(manager);
        UserGoodsAdapter adapter = new UserGoodsAdapter(this);
        myGoodsList.setAdapter(adapter);
    }
}
