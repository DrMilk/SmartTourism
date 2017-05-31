package com.nameli.smarttourism.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.nameli.smarttourism.R;
import com.nameli.smarttourism.Utils.T;
import com.nameli.smarttourism.login.LoginActivity;
import com.nameli.smarttourism.main.SpotDetailActivity;
import com.nameli.smarttourism.main.SpotListAdatapter;
import com.nameli.smarttourism.onlinedata.LiUser;
import com.nameli.smarttourism.onlinedata.Spotdata;

import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Administrator on 2017/3/18.
 */

public class CollectActivity extends Activity {
    private ListView listView;
    private Context mcontext;
    private ArrayList<Spotdata> list_data;
    private ArrayList<String> list_str;
    private SpotListAdatapter spotListAdatapter;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        mcontext=this;
        list_data=new ArrayList<Spotdata>();
        listView= (ListView) findViewById(R.id.mine_collect_listview);
        spotListAdatapter=new SpotListAdatapter(mcontext,list_data);
        listView.setAdapter(spotListAdatapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it=new Intent(CollectActivity.this,SpotDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("title",list_data.get(position).getTitle());
                bundle.putString("context",list_data.get(position).getContext());
                bundle.putString("id",list_data.get(position).getObjectId());
                it.putExtras(bundle);
                startActivity(it);
            }
        });
    }

    @Override
    protected void onResume() {
        if(checkuser()){
            if(list_str.size()!=0){
                for (int i=0;i<list_str.size();i++){
                    BmobQuery<Spotdata> query = new BmobQuery<Spotdata>();
                    query.getObject(list_str.get(i), new QueryListener<Spotdata>() {
                        @Override
                        public void done(Spotdata object, BmobException e) {
                            if(e==null){
                                list_data.add(object);
                            }else{
                            }
                            updataview();
                        }

                    });
                }
            }else {
                T.showShot(mcontext,"暂时还没有收藏啊~");
            }
        }
        super.onResume();
    }
    private boolean checkuser() {
        LiUser bmobUser = BmobUser.getCurrentUser(LiUser.class);
        if(bmobUser != null){
            // 允许用户使用应用
            list_str= (ArrayList<String>) BmobUser.getCurrentUser(LiUser.class).getList_collect();
            //  text_username.setText(name);
            return true;
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
            Intent it=new Intent(CollectActivity.this, LoginActivity.class);
            startActivity(it);
            CollectActivity.this.finish();
            return false;
        }
    }
    private void updataview() {
        if(list_data.size()==list_str.size()){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    spotListAdatapter.notifyDataSetChanged();
                }
            });
        }
    }
}
