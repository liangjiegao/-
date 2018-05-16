package com.example.gdei.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.gdei.CommUser;
import com.example.gdei.MainActivity;
import com.example.gdei.R;
import com.example.gdei.util.GetPostUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;

/**
 * Created by gdei on 2018/5/9.
 */

public class LoginView extends Activity {


    private RelativeLayout login_main_rl;
    private EditText login_et_userName, login_et_password;
    private Button login_bt_login, login_bt_register, login_bt_weCharLogin, login_bt_forgetPassword;
    //记录用户输入的账号密码
    private String userName, password;
    private BufferedReader br;  //服务器响应的数据
    //处理发送登录、注册请求等请求， 并 处理返回数据
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //判断登录请求
            if (msg.what == 0x123) {
                String str = "";
                StringBuffer result = new StringBuffer();
                try {
                    while ((str = br.readLine()) != null) {
                        result.append(str);
                    }
                    System.out.println(result.toString());
                    if (result.toString().equals("账号不存在！")) {
                        Snackbar.make(login_main_rl, "账号不存在！！", Snackbar.LENGTH_LONG).show();
                    } else {
                        if (result.toString().equals("密码错误！")) {
                            Snackbar.make(login_main_rl, "密码错误！！", Snackbar.LENGTH_LONG).show();
                        } else {
                            JSONArray jsonArray = new JSONArray(result.toString());
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if (jsonObject.getString("username").equals(userName)){
                                Intent toMainView = new Intent(LoginView.this, MainActivity.class);
                                Bundle bundle = new Bundle();   //存放账号和密码， 用于查询个人信息
                                bundle.putString("userName", userName);
                                bundle.putString("password", password);
                                toMainView.putExtras(bundle);
                                startActivity(toMainView);
                                finish();
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

    }

    /**
     * 初始化界面
     */
    public void initView() {
        login_main_rl = findViewById(R.id.login_main_rl);
        login_et_userName = findViewById(R.id.login_et_userName);
        login_et_password = findViewById(R.id.login_et_password);
        login_bt_login = findViewById(R.id.login_bt_login);
        login_bt_register = findViewById(R.id.login_bt_register);
        login_bt_weCharLogin = findViewById(R.id.login_bt_weCharLogin);
        login_bt_forgetPassword = findViewById(R.id.login_bt_forgetPassword);
        login_bt_login = findViewById(R.id.login_bt_login);

        login_bt_login.setOnClickListener(new MyListener());
        login_bt_register.setOnClickListener(new MyListener());
    }

    /**
     * 处理登录业务
     */
    public void login() {
        //获取账号密码输入信息
        userName = login_et_userName.getText().toString();
        password = login_et_password.getText().toString();
        //如果有输入且输入不为空
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            try {
                //判断密码是否纯数字
                Integer.parseInt(password);
            } catch (Exception e) {
                Snackbar.make(login_main_rl, "密码格式有误！！", Snackbar.LENGTH_LONG).show();
                return;
            }
            new Thread(){
                @Override
                public void run() {
                    //发送get请求， 并获取返回BufferedReader结果
                    GetPostUtil gpu = new GetPostUtil("http://192.168.42.203:8080/Login?userName=" + userName + "&password=" + password);
                    br  = gpu.sendGet();
                    handler.sendEmptyMessage(0x123);
                }
            }.start();

        } else {
            Snackbar.make(login_main_rl, "账号和密码都不能为空！！", Snackbar.LENGTH_LONG).show();
        }
    }

    public void initContext() {

    }
    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login_bt_login:
                    login();
                    break;
                case R.id.login_bt_register:
                    Intent toRegister = new Intent(LoginView.this, Register.class);
                    startActivity(toRegister);
            }
        }
    }
}
