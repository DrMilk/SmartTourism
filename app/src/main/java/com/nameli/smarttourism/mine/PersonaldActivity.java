package com.nameli.smarttourism.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.nameli.smarttourism.R;
import com.nameli.smarttourism.Utils.L;
import com.nameli.smarttourism.Utils.T;
import com.nameli.smarttourism.customView.XuProcessDialog;
import com.nameli.smarttourism.login.LoginActivity;
import com.nameli.smarttourism.onlinedata.LiUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/3/18.
 */

public class PersonaldActivity extends Activity implements View.OnClickListener{
    private String TAG="PersonaldActivity";
    private BmobUser xuuser;
    private String phonenum;
    private String address;
    private String qq;
    private String wechat;
    private String email;
    private String sex;
    private String name;
    private EditText ed_qq;
    private EditText ed_wechat;
    private EditText ed_emial;
    private EditText ed_address;
    private EditText ed_name;
    private TextView text_user;
    private Button button_ok;
    private Button button_back;
    private Context mcontext;
    private XuProcessDialog xuprocessdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        mcontext=this;
        initview();
    }

    private void initview() {
        text_user= (TextView) findViewById(R.id.personal_phone);
        ed_qq= (EditText) findViewById(R.id.personal_qq);
        ed_wechat= (EditText) findViewById(R.id.personal_weixin);
        ed_emial= (EditText) findViewById(R.id.personal_email);
        ed_address= (EditText) findViewById(R.id.personal_address);
        ed_name= (EditText) findViewById(R.id.personal_user);
        button_ok= (Button) findViewById(R.id.button_ok);
        button_back= (Button) findViewById(R.id.button_back);
        button_ok.setOnClickListener(this);
        button_back.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        if(checkuser()){
            L.i(TAG,"到这步了吗");
            text_user.setText(xuuser.getUsername());
            ed_name.setText(name);
            ed_qq.setText(qq);
            ed_address.setText(address);
            ed_wechat.setText(wechat);
            ed_emial.setText(xuuser.getEmail());
        }
        super.onResume();
    }
    private boolean checkuser() {
        LiUser bmobUser = BmobUser.getCurrentUser(LiUser.class);
        L.i(TAG,"到这步了吗1");
        if(bmobUser != null){
            // 允许用户使用应用
            xuuser=  BmobUser.getCurrentUser();
            email=xuuser.getEmail();
            name= (String) BmobUser.getObjectByKey("name");
            qq= (String) BmobUser.getObjectByKey("qq");
            wechat= (String) BmobUser.getObjectByKey("wechat");
            address=(String) BmobUser.getObjectByKey("address");
            sex=(String) BmobUser.getObjectByKey("sex");
            //  text_username.setText(name);
            return true;
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
            Intent it=new Intent(PersonaldActivity.this, LoginActivity.class);
            startActivity(it);
            PersonaldActivity.this.finish();
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_ok:xuprocessdialog=new XuProcessDialog(mcontext);updatainfo();break;
            case R.id.button_back:PersonaldActivity.this.finish();break;
        }
    }

    private void updatainfo() {
        String namenew=ed_name.getText().toString().trim();
        String addressnew=ed_address.getText().toString().trim();
        String qqnew=ed_qq.getText().toString().trim();
        String wechatnew=ed_wechat.getText().toString().trim();
        String emailnew=ed_emial.getText().toString().trim();
        xuprocessdialog.show();
        LiUser newUser = new LiUser();
        if(!namenew.equals(name))
        newUser.setName(name);
        if(!addressnew.equals(addressnew))
        newUser.setAddress(address);
        if(!qq.equals(qqnew))
        newUser.setQq(qq);
        if(!wechat.equals(wechatnew))
        newUser.setWechat(wechat);
        if(!email.equals(emailnew))
        newUser.setEmail(email);
        newUser.update(xuuser.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    T.showShot(mcontext,"更改成功！");
                    xuprocessdialog.dismiss();
                }else{
                    T.showShot(mcontext,"更改失败！"+e.toString());
                    xuprocessdialog.dismiss();
                }
            }
        });
    }
}
