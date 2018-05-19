package com.example.gdei.fragment2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gdei.MyContext;
import com.example.gdei.R;
import com.example.gdei.fragment1.MarketFragment;
import com.example.gdei.fragment4.UserMsgFragment;
import com.example.gdei.util.GetPostUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by gdei on 2018/5/11.
 */

public class IntroducedFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "IntroducedFragment";
    private View rootView;
    private EditText f2_bt_goodsName, f2_et_goodsIntr,f2_et_price;
    private Button f2_b_introduce;
    private LinearLayout f2_ll_main;
    private BufferedReader br;
    private ImageView f2_ib_takeP, f2_ib_delete_photo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment2_introduced, container, false);
        initView();

        return rootView;

    }
    private void initView(){

        f2_bt_goodsName = rootView.findViewById(R.id.f2_bt_goodsName);
        f2_et_goodsIntr = rootView.findViewById(R.id.f2_et_goodsIntr);
        f2_et_price = rootView.findViewById(R.id.f2_et_price);
        f2_b_introduce = rootView.findViewById(R.id.f2_b_introduce);
        f2_ll_main = rootView.findViewById(R.id.f2_ll_main);
        f2_ib_takeP = rootView.findViewById(R.id.f2_ib_takeP);
        f2_ib_delete_photo = rootView.findViewById(R.id.f2_ib_delete_photo);

        f2_b_introduce.setOnClickListener(this);
        f2_ib_takeP.setOnClickListener(this);
    }
    private String goodsName;
    private String goodsIntr;
    private String goodsPrice;
    private boolean verificationInput(){
        goodsName = f2_bt_goodsName.getText().toString();
        goodsIntr = f2_et_goodsIntr.getText().toString();
        goodsPrice = f2_et_price.getText().toString();
        if (goodsName.equals("")){
            Snackbar.make(f2_ll_main, "物品标题必须填写！！", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (goodsIntr.equals("")){
            Snackbar.make(f2_ll_main, "物品描述必须填写！！", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (goodsPrice.equals("")){
            Snackbar.make(f2_ll_main, "物品价格必须填写！！", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private void clearText(){
        f2_bt_goodsName.setText("");
        f2_et_goodsIntr.setText("");
        f2_et_price.setText("");
    }
    private void introduceGoods(){
        new Thread(){
            @Override
            public void run() {
                GetPostUtil gpu = new GetPostUtil("http://192.168.42.203:8080/IntroduceGoods?goodsStore="+
                        MyContext.saleGoodsStore+"&goodsName="+goodsName+"&goodsIntr="+goodsIntr+"&price="+goodsPrice);
                br = gpu.sendGet();
                handler.sendEmptyMessage(0x00501);
            }
        }.start();
    }
    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);

    }
    private void saveImageToGallery(Context context, Bitmap bitmap){
        //查找文件夹是否存在
        File appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "image");
        if (!appDir.exists()){
            //目录不存在，则创建
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis()+".jpg";
        File file = new File(appDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);  //获取file输出流，准备写入图片
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            ScannerByReceiver(context, file.getAbsolutePath());
            if (!bitmap.isRecycled()){      //当图片储存过大时， 为了避免出现OOM， 及时回收bitmap
                System.gc();    //通知系统回收
            }
            Toast.makeText(context, "图片保存成功" , Toast.LENGTH_SHORT).show();
        }

    }
    /*Receiver扫描更新图片库*/
    private void ScannerByReceiver(Context context, String path){
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+path)));
    }
    private Bitmap bitmap;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
            Bundle bundle = data.getExtras();
            if (bundle != null){
                bitmap = (Bitmap) bundle.get("data");

                f2_ib_takeP.setImageBitmap(bitmap);

        }
            break;
            default:
                break;
        }
    }
    private void deleteImg(){
        Log.i(TAG, "deleteImg: ");
        f2_ib_takeP.setBackgroundResource(R.drawable.img);

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x00501) {
                String str = "";
                StringBuffer result = new StringBuffer();
                try {
                    while ((str = br.readLine()) != null) {
                        result.append(str);
                    }
                    System.out.println(result);
                    if (result.toString().equals("1")) {
                        Snackbar.make(f2_ll_main, "物品发布成功！", Snackbar.LENGTH_LONG).show();
                        saveImageToGallery(IntroducedFragment.this.getContext(), bitmap);
                        
                        MarketFragment.isUpDate = true;
                        UserMsgFragment.isUpdate = true;
                        clearText();
                    }else {
                        Snackbar.make(f2_ll_main, "物品发布失败！", Snackbar.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.f2_b_introduce:
                if (verificationInput()){
                    introduceGoods();
                }
                break;
            case R.id.f2_ib_takeP:
                takePhoto();
                break;
            case R.id.f2_ib_delete_photo:
                deleteImg();
                break;
        }
    }
}
