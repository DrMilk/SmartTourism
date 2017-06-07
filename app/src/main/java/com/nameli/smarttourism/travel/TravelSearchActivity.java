package com.nameli.smarttourism.travel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nameli.smarttourism.R;
import com.nameli.smarttourism.Utils.L;
import com.nameli.smarttourism.food.FoodDetailActivity;
import com.nameli.smarttourism.hotel.HotelListAdatapter;
import com.nameli.smarttourism.onlinedata.Hoteldata;
import com.nameli.smarttourism.onlinedata.Traveldata;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/6.
 */

public class TravelSearchActivity extends Activity{
    private String TAG="TravelSearchActivity";
    private ListView listView;
    private TravelListAdatapter adapter;
    private Context mcontext;
    private ArrayList<Traveldata> listdata;
    private TextView textback;
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
        ArrayList<String> listdataaddress=bundle.getStringArrayList("searchdataaddress");
        if(listdataaddress==null){
            L.i(TAG,"空");
        }
        L.i(TAG,listdataaddress.size()+"bbbbbbbbbbbbbbb");
        for(int i=0;i<listdata.size();i++){
            listdata.get(i).setObjectId(listdataaddress.get(i));
        }
        L.i(TAG,listdata.size()+"aaaaaaaaaaaaaaaaaaa");
        adapter=new TravelListAdatapter(mcontext,listdata);
        listView.setAdapter(adapter);
    }

    private void initView() {
        listView= (ListView) findViewById(R.id.activity_search_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it=new Intent(mcontext,TravelDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("title",listdata.get(position).getTitle());
                bundle.putString("context",listdata.get(position).getContext());
                bundle.putString("id",listdata.get(position).getObjectId());
                bundle.putStringArrayList("remarklist",listdata.get(position).getList_remark());
                it.putExtras(bundle);
                startActivity(it);
            }
        });
        textback= (TextView) findViewById(R.id.search_back);
        textback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
