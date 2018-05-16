package com.example.gdei.fragment3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gdei.R;

/**
 * Created by gdei on 2018/5/13.
 */

public class NewsFragment extends Fragment{

    private View rootView;
    private RecyclerView f3_rv_news;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment3_news, container, false);

        return rootView;
    }
    private void initView(){

        f3_rv_news = rootView.findViewById(R.id.f3_rv_news);

    }
}
