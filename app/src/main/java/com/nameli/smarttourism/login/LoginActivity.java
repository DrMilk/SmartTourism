package com.nameli.smarttourism.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nameli.smarttourism.R;
import com.nameli.smarttourism.Utils.L;
import com.nameli.smarttourism.Utils.MySdcard;
import com.nameli.smarttourism.Utils.SharePreferenceUtil;
import com.nameli.smarttourism.Utils.StringLegalUtil;
import com.nameli.smarttourism.Utils.T;
import com.nameli.smarttourism.customView.XuProcessDialog2;
import com.nameli.smarttourism.main.MainActivity;
import com.nameli.smarttourism.onlinedata.Alldata;
import com.nameli.smarttourism.onlinedata.Fooddata;
import com.nameli.smarttourism.onlinedata.Hoteldata;
import com.nameli.smarttourism.onlinedata.LiUser;
import com.nameli.smarttourism.onlinedata.Remakdata;
import com.nameli.smarttourism.onlinedata.Traveldata;

import java.util.ArrayList;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/3/16.
 */

public class LoginActivity extends Activity implements View.OnClickListener{
    private String TAG="LoginActivity";
    private EditText edit_userid;
    private EditText edit_password;
    private Button button_ok;
    private Button button_sign;
    private ImageView img_on;
    private boolean button_status=true;
    private Context mcontext;
    private XuProcessDialog2 xuloginprocess;
    private MySdcard mySdcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mcontext=this;
        initView();
//        mbmobinitdata();
        mySdcard=new MySdcard();
        mySdcard.initWuSdcard(mcontext);
    }

    private void initView() {
        button_ok= (Button) findViewById(R.id.button_ok);
        button_sign= (Button) findViewById(R.id.button_sign);
        img_on= (ImageView) findViewById(R.id.login_img_on);
        edit_userid= (EditText) findViewById(R.id.login_uesrid);
        edit_password= (EditText) findViewById(R.id.login_password);
        ImageView img_weibo= (ImageView) findViewById(R.id.login_weibo);
        ImageView img_qq= (ImageView) findViewById(R.id.login_qq);
        ImageView img_weixin= (ImageView) findViewById(R.id.login_weixin);
        img_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!button_status){
                    img_on.setImageResource(R.mipmap.lottery_fill_order_button_off);
                    SharePreferenceUtil.putSettingDataBoolean(mcontext,SharePreferenceUtil.AUTOLOGIN,false);
                    button_status=true;
                }else {
                    img_on.setImageResource(R.mipmap.lottery_fill_order_button_on);
                    button_status=false;
                    SharePreferenceUtil.putSettingDataBoolean(mcontext,SharePreferenceUtil.AUTOLOGIN,true);
                }

            }
        });
        button_ok.setOnClickListener(this);
        button_sign.setOnClickListener(this);
        img_weibo.setOnClickListener(this);
        img_qq.setOnClickListener(this);
        img_weixin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_ok:gologin();break;
            case R.id.button_sign:
                Intent it1=new Intent(LoginActivity.this,SignActivity.class);startActivity(it1);break;
            case R.id.login_weibo:
                T.showShot(mcontext,"-未实现此接口-");break;
            case R.id.login_qq:T.showShot(mcontext,"-未实现此接口-");break;
            case R.id.login_weixin:T.showShot(mcontext,"-未实现此接口-");break;
        }
    }

    private void gologin() {
        //uploadtest();
        button_ok.setEnabled(false);
        boolean jundge_legal=true;
        String id=edit_userid.getText().toString().trim();
        String password=edit_password.getText().toString().trim();
        if(!StringLegalUtil.isHaveLength(id)){
            edit_userid.setError("请输入手机号！");
            jundge_legal=false;
        }else if(!StringLegalUtil.isCorrectPhonenum(id)){
            edit_userid.setError("请输入正确的手机号！");
            jundge_legal=false;
        }
        if(jundge_legal){
            xuloginprocess=new XuProcessDialog2(mcontext);xuloginprocess.show();
            LiUser.loginByAccount(id,password, new LogInListener<LiUser>() {
                @Override
                public void done(LiUser xuUser, BmobException e) {
                    if(xuUser!=null) {
                        Intent it=new Intent(LoginActivity.this,MainActivity.class);startActivity(it);
                        LoginActivity.this.finish();
                    }else {
                        T.showShot(mcontext,"登录失败！");
                        button_ok.setEnabled(true);
                    }
                    xuloginprocess.dismiss();
                }
            });
        }else {
            button_ok.setEnabled(true);
        }
    }

    private void uploadtest() {
        Fooddata fooddata=new Fooddata("水煮肉片","水煮肉片灰常好吃",true,97,4,new ArrayList<String>());
        fooddata.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });
        Hoteldata hoteldata=new Hoteldata("易蓝大酒店","地下创业园楼下快来吧",true,180,2,new ArrayList<String>());
        hoteldata.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });
        Traveldata traveldata=new Traveldata("上海一日游","地下创业园楼下快来吧",true,new ArrayList<String>());
        traveldata.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });
        Remakdata remakdata=new Remakdata("看样子挺好的样子","18249028972");
        remakdata.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });
       Alldata alldata=new Alldata();
        ArrayList<String> list_spot=new ArrayList<>();
        list_spot.add("99d05919b6");
        list_spot.add("40bbf211d6");
        list_spot.add("5ad99134f8");
        list_spot.add("ddd0c4a445");
        list_spot.add("5bf37ec134");
        list_spot.add("f707e19325");
        list_spot.add("3a1f70cc3e");
        list_spot.add("dd356d5a9a");
        list_spot.add("9c142046ec");
        alldata.setList_food(new ArrayList<String>());
        alldata.setList_hotel(new ArrayList<String>());
        alldata.setList_spot(list_spot);
        alldata.setList_travel(new ArrayList<String>());
        alldata.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    L.i(TAG,"上传成功！");
                }
            }
        });
    }

    private boolean checkuser() {
        LiUser bmobUser = BmobUser.getCurrentUser(LiUser.class);
        if(bmobUser != null){
            // 允许用户使用应用
            //  String name= (String) BmobUser.getObjectByKey("treename");
            //  text_username.setText(name);
            userrun();
            return true;
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
            return false;
        }
    }

    private void userrun() {
        Intent it=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(it);
    }
    @Override
    protected void onResume() {
        if(SharePreferenceUtil.getSettingDataBoolean(mcontext,SharePreferenceUtil.AUTOLOGIN)){
            if(checkuser()){

            }
        }else {
            img_on.setImageResource(R.mipmap.lottery_fill_order_button_off);
        }
        super.onResume();
    }
}
