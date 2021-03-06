package com.nameli.smarttourism.mine;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nameli.smarttourism.R;
import com.nameli.smarttourism.Utils.L;
import com.nameli.smarttourism.lbstest.*;
import com.nameli.smarttourism.login.LoginActivity;
import com.nameli.smarttourism.onlinedata.LiUser;

import cn.bmob.v3.BmobUser;


/**
 * Created by Administrator on 2017/3/17.
 */

public class MineFragment extends Fragment implements View.OnClickListener{
    private TextView text_setting;
    private TextView text_personal;
    private TextView text_changepassword;
    private TextView text_collect;
    private TextView text_weather;
    private TextView text_location;
    private TextView text_about;
    private TextView text_name;
    private TextView text_id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_mine,null);
        text_name= (TextView) view.findViewById(R.id.maintab_name);
        text_id= (TextView) view.findViewById(R.id.maintab_id);
        text_changepassword= (TextView) view.findViewById(R.id.mine_change);
        text_setting= (TextView) view.findViewById(R.id.mine_setting);
        text_personal= (TextView) view.findViewById(R.id.mine_personal);
        text_weather= (TextView) view.findViewById(R.id.mine_weather);
        text_collect= (TextView) view.findViewById(R.id.mine_collect);
        text_location= (TextView) view.findViewById(R.id.mine_location);
        text_about= (TextView) view.findViewById(R.id.mine_about);
        text_changepassword.setOnClickListener(this);
        text_collect.setOnClickListener(this);
        text_personal.setOnClickListener(this);
        text_setting.setOnClickListener(this);
        text_weather.setOnClickListener(this);
        text_location.setOnClickListener(this);
        text_about.setOnClickListener(this);
        text_name.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mine_change:Intent it=new Intent(getActivity(),ChangePasswordActivity.class);startActivity(it);break;
            case R.id.mine_collect:Intent it1=new Intent(getActivity(),CollectActivity.class);startActivity(it1);break;
            case R.id.mine_personal:Intent it2=new Intent(getActivity(),PersonaldActivity.class);startActivity(it2);break;
            case R.id.mine_setting:Intent it3=new Intent(getActivity(),SettingActivity.class);startActivity(it3);break;
            case R.id.mine_weather:Intent it4=new Intent(getActivity(),WeatherActivity.class);startActivity(it4);break;
            case R.id.mine_location:Intent it5=new Intent(getActivity(), com.nameli.smarttourism.lbstest.LocationActivity.class);startActivity(it5);break;
            case R.id.mine_about:Intent it6=new Intent(getActivity(),AboutActivity.class);startActivity(it6);break;
            case R.id.maintab_name:String str=text_name.getText().toString();
                if(str.equals("帐号未登录")){
                    Intent it7=new Intent(getActivity(),LoginActivity.class);
                    startActivity(it7);
                    getActivity().finish();
                };break;
        }
    }
    private boolean checkuser() {
        LiUser bmobUser = BmobUser.getCurrentUser(LiUser.class);
        if (bmobUser != null) {
            // 允许用户使用应用
            text_name.setText(bmobUser.getName());
            text_id.setText(bmobUser.getUsername());
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkuser();
    }
}
