package com.nameli.smarttourism.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nameli.smarttourism.R;
import com.nameli.smarttourism.Utils.L;
import com.nameli.smarttourism.onlinedata.Fooddata;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/6.
 */

public class FoodSearchActivity extends Activity{
    private String TAG="FoodSearchActivity";
    private ListView listView;
    private FoodListAdatapter adapter;
    private Context mcontext;
    private ArrayList<Fooddata> listdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mcontext=this;
        initView();
        recivedata();
    }

    private void recivedata() {
        Intent it=getIntent();
        Bundle bundle=it.getExtras();
        listdata=bundle.getParcelableArrayList("searchdata");
        L.i(TAG,listdata.size()+"aaaaaaaaaaaaaaaaaaa");
        adapter=new FoodListAdatapter(mcontext,listdata);
        listView.setAdapter(adapter);
    }

    private void initView() {
        listView= (ListView) findViewById(R.id.activity_search_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it=new Intent(mcontext,FoodDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("title",listdata.get(position).getTitle());
                bundle.putString("context",listdata.get(position).getContext());
                bundle.putString("price",listdata.get(position).getPrice()+"");
                bundle.putString("id",listdata.get(position).getObjectId());
                bundle.putStringArrayList("remarklist",listdata.get(position).getList_remarkd());
                it.putExtras(bundle);
                startActivity(it);
            }
        });
    }
}
