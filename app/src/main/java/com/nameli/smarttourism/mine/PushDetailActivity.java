package com.nameli.smarttourism.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nameli.smarttourism.R;
import com.nameli.smarttourism.Utils.MyUpload;
import com.nameli.smarttourism.Utils.T;
import com.nameli.smarttourism.customView.PopupWindowShare;
import com.nameli.smarttourism.login.LoginActivity;
import com.nameli.smarttourism.onlinedata.LiUser;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/3/17.
 */

public class PushDetailActivity extends Activity implements View.OnClickListener{
    private Context mcontext;
    private String id;
    private TextView title;
    private TextView context;
    private String title_text;
    private String context_text;
    private Button button_collect;
    private String name;
    private ArrayList<String> list_collect;
    private MyUpload myUpload;
    private ImageView img;
    private LinearLayout back_line;
    private TextView img_share;
    private PopupWindowShare mPopupWindows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_detail);
        mcontext=this;
        myUpload=new MyUpload(mcontext);
        receviewdata();
        intiview();
    }

    private void receviewdata() {
        Bundle bundle=getIntent().getExtras();
        title_text=bundle.getString("title");
        context_text=bundle.getString("context");
        id=bundle.getString("id");
    }

    private void intiview() {
        title= (TextView) findViewById(R.id.spot_title);
        context= (TextView) findViewById(R.id.spot_context);
        button_collect= (Button) findViewById(R.id.button_collect);
        img= (ImageView) findViewById(R.id.spot_img);
        back_line= (LinearLayout) findViewById(R.id.detail_spot_back);
        img_share= (TextView) findViewById(R.id.detail_spot_share);
        back_line.setOnClickListener(this);
        img_share.setOnClickListener(this);
        myUpload.download_asynchronous("mysmarttourism","listimg/"+id,img);
        title.setText(title_text);
        context.setText(context_text);
    }
    private boolean checkuser() {
        LiUser bmobUser = BmobUser.getCurrentUser(LiUser.class);
        if(bmobUser != null){
            // 允许用户使用应用
            name= (String) BmobUser.getObjectByKey("objectId");
            list_collect= (ArrayList<String>) bmobUser.getList_collect();
            return true;
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
            userrun();
            return false;
        }
    }

    private void userrun() {
        Intent it=new Intent(PushDetailActivity.this, LoginActivity.class);
        startActivity(it);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_spot_back:PushDetailActivity.this.finish();break;
            case R.id.detail_spot_share: openpopupwindow();break;
        }
    }
    private void openpopupwindow() {
        WindowManager windowmanager=this.getWindowManager();
        int height=windowmanager.getDefaultDisplay().getHeight();
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.5f;
        this.getWindow().setAttributes(params);
        mPopupWindows = new PopupWindowShare(mcontext,height);
        mPopupWindows.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = PushDetailActivity.this.getWindow().getAttributes();
                params.alpha = 1f;
                PushDetailActivity.this.getWindow().setAttributes(params);
            }
        });
        //出问题了
        mPopupWindows.showAtLocation(PushDetailActivity.this.findViewById(R.id.main_content), Gravity.BOTTOM , 0, 0);
    }
}
