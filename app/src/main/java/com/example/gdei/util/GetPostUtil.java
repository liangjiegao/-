package com.example.gdei.util;

import android.widget.Toast;

import com.example.gdei.view.LoginView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.security.auth.callback.Callback;

/**
 * Created by gdei on 2018/5/9.
 */

public class GetPostUtil {
    private static  HttpClient httpClient;

    private  String url;
    public GetPostUtil(String url){
        this.url = url;
    }

    public  BufferedReader sendGet(){
        BufferedReader br = null;
        httpClient = new DefaultHttpClient();
        FutureTask<BufferedReader> task = new FutureTask<BufferedReader>(new Callable<BufferedReader>() {
            @Override
            public BufferedReader call() throws Exception {
                try {
                    //创建Url对象
                    //URL myUrl = new URL(url+"?"+param);
                    //System.out.println(url+"?"+param);
                    //通过url对象获取UrlConnection
                    //URLConnection conn = myUrl.openConnection();
                    //设置请求属性

                    //建立实际连接
                    //conn.connect();
                    //通过BufferedReader来读取conn的输入流
                    //BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    //return br;

                    HttpGet get = new HttpGet(url);
                    HttpResponse response = httpClient.execute(get);

                    HttpEntity entity = response.getEntity();
                    if (entity != null){
                        //获取服务器响应数据
                        BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
                        return br;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("异常");
                }
                return null;
            }
        });

        new Thread(task,"有返回值的线程").start();

        try {
            br = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (br!= null){
            return br;
        }
        return null;
    }
}
