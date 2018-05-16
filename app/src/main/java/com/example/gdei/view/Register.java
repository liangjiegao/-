package com.example.gdei.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.gdei.R;
import com.example.gdei.util.GetPostUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.regex.Pattern;

/**
 * Created by gdei on 2018/5/11.
 */

public class Register extends Activity{

    private RelativeLayout main_register;
    private EditText register_username, register_password;
    private Button register_ok;
    private boolean clicked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }
    public void initView(){

        register_username = findViewById(R.id.register_username);
        register_password = findViewById(R.id.register_password);
        main_register = findViewById(R.id.main_register);
        register_ok = findViewById(R.id.register_ok);
        setListener();
    }
    private boolean isStandardU;
    private boolean isStandardP;
    private void setListener(){
        register_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus){
                    isStandardU = formatAccount(register_username.getText().toString(), null);
                    if (!isStandardU){
                        Snackbar.make(main_register, "请输入3到9位由字符数字或下划线组成的用户名！", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });
        register_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus){
                    isStandardP = formatAccount(null, register_password.getText().toString());
                    if (!isStandardP){
                        Snackbar.make(main_register, "请输入6到13位由纯数字组成的密码！", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });

        register_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
    private boolean formatAccount(String userName, String password){
        if (userName != null){
            boolean isOk = Pattern.matches("[0-9a-z/_]{3,9}",userName);
            return isOk;
        }
        if (password != null){
            boolean isOk = Pattern.matches("[0-9]{6,13}",password);
            return isOk;
        }
        return false;
    }
    public BufferedReader br;
    private void register(){
        final String userName = register_username.getText().toString();
        final String password = register_password.getText().toString();
        System.out.println("isStandardU="+isStandardU+"  isStandardP="+isStandardP);
        if (!isStandardU){
            isStandardU = formatAccount(userName,null);
        }
        if (!isStandardP){
            isStandardP = formatAccount(null, password);
        }
        if (isStandardU && isStandardP){
            new Thread(){
                @Override
                public void run() {
                    GetPostUtil gpu = new GetPostUtil("http://192.168.42.203:8080/Register?userName="+userName+"&password="+password);
                    br  = gpu.sendGet();
                    handler.sendEmptyMessage(0x00201);
                }
            }.start();
        }else {
            Snackbar.make(main_register, "请输入正确格式的账号和密码！", Snackbar.LENGTH_LONG).show();
        }

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            FutureTask<BufferedReader> task = null;
            if (msg.what == 0x345){
                System.out.println("成功");
                //Intent toLogin = new Intent(Register.this, LoginView.class);
                //startActivity(toLogin);
                finish();
            }else if (msg.what == 0x00201) {
                if (br == null){
                    Snackbar.make(main_register, "服务器响应失败！", Snackbar.LENGTH_LONG).show();
                    return;
                }
                try {
                    String str = "";
                    StringBuffer result = new StringBuffer();
                    while ((str = br.readLine()) != null) {
                        result.append(str);
                    }
                    if (result.toString().equals("1")) {
                        Snackbar.make(main_register, "注册成功！", Snackbar.LENGTH_LONG).show();
                        handler.sendEmptyMessageDelayed(0x345, 1000);
                    } else {
                        Snackbar.make(main_register, result, Snackbar.LENGTH_LONG).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
