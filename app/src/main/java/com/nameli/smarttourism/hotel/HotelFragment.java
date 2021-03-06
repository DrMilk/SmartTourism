package com.nameli.smarttourism.hotel;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;


import com.nameli.smarttourism.R;
import com.nameli.smarttourism.Utils.L;
import com.nameli.smarttourism.Utils.T;
import com.nameli.smarttourism.food.FoodDetailActivity;
import com.nameli.smarttourism.food.FoodSearchActivity;
import com.nameli.smarttourism.onlinedata.Fooddata;
import com.nameli.smarttourism.onlinedata.Hoteldata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Administrator on 2017/3/17.
 */

public class HotelFragment extends Fragment {
    private Spinner spinner ;
    private ArrayList<Hoteldata> list_hotel_kind=new ArrayList<>();
    private String TAG="HotelFragment";
    private ListView listView;
    private ArrayList<Hoteldata> list_hotel;
    private Context mcontext;
    private ArrayList<String> list_str;
    private HotelListAdatapter hotelListAdatapter;
    private SearchView searchView;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        mcontext=getActivity();
        list_str=getArguments().getStringArrayList("data");
        L.i(TAG,list_str.size()+"长度");
        if(list_str==null)
            list_str=new ArrayList<>();
        if(list_hotel==null){
            list_hotel=new ArrayList<>();
            for(int i=0;i<list_str.size();i++){
                BmobQuery<Hoteldata> query = new BmobQuery<Hoteldata>();
                query.getObject(list_str.get(i), new QueryListener<Hoteldata>() {
                    @Override
                    public void done(Hoteldata object, BmobException e) {
                        if(e==null){
                            list_hotel.add(object);
                            L.i(TAG,"all"+"下载成功");
                        }else{
                            L.i(TAG,"all"+"下载失败");
                        }
                        updataview();
                    }


                });
            }
        }
        super.onCreate(savedInstanceState);
    }

    private void updataview() {
        if(list_hotel.size()==list_str.size()){
            updataContext();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    L.i(TAG,"Gengxinle");
                    hotelListAdatapter.notifyDataSetChanged();
                }
            });
        }
    }
    private boolean updataContext() {
        L.i(TAG,"排序");
        final SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        final Date[] data1 = {null};
        final Date[] data2 = {null};
        for (int i=0;i<list_hotel.size();i++){
            Log.i(TAG,list_hotel.get(i).getCreatedAt()+"日期");
        }
        Comparator<Hoteldata> comparator = new Comparator<Hoteldata>(){
            public int compare(Hoteldata s1, Hoteldata s2) {
                //排序日期
                try {
                    data1[0] =sdf.parse(s1.getCreatedAt());
                    data2[0] =sdf.parse(s2.getCreatedAt());
                } catch (ParseException e) {
                    Log.i(TAG,"wenti");
                    e.printStackTrace();
                }
                if(data1[0].getTime()> data2[0].getTime()){
                    return -1;
                }else {
                    return 1;
                }
            }
        };
        if(list_hotel.size()>1){
            Collections.sort(list_hotel,comparator);
        }
        return true;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_hotel,null);
        mcontext=getActivity();
        listView= (ListView) view.findViewById(R.id.activity_hotel_listview);
//        list_hotel.add(new Hoteldata("上海龙之梦大酒店","行政套房(大床)",true,1959));
//        list_hotel.add(new Hoteldata("如家酒店","行政套房(大床)",true,364));
//        list_hotel.add(new Hoteldata("万达酒店","行政套房(大床)，8点人民广场准时出发",true,3626));
//        list_hotel.add(new Hoteldata("如家酒店","泰国市区免费接，8点人民广场准时出发",true,75));
//        list_hotel.add(new Hoteldata("上海龙之梦大酒店","上海市区免费接，8点人民广场准时出发",true,4100));
//        list_hotel.add(new Hoteldata("万达酒店","上海市区免费接，8点人民广场准时出发",true,7151));
//        list_hotel.add(new Hoteldata("如家酒店","上海市区免费接，8点人民广场准时出发",true,297));
//        list_hotel.add(new Hoteldata("如家酒店","上海市区免费接，8点人民广场准时出发",true,2034));
        hotelListAdatapter=new HotelListAdatapter(mcontext,list_hotel);
        listView.setAdapter(hotelListAdatapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it=new Intent(getActivity(),HotelDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("title",list_hotel.get(position).getTitle());
                bundle.putString("context",list_hotel.get(position).getContext());
                bundle.putString("price",list_hotel.get(position).getPrice()+"");
                bundle.putString("id",list_hotel.get(position).getObjectId());
                bundle.putStringArrayList("remarklist",list_hotel.get(position).getList_remark());
                it.putExtras(bundle);
                startActivity(it);
            }
        });
        spinner = (Spinner) view.findViewById(R.id.spin_food);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    hotelListAdatapter.setList_data(list_hotel);
                    hotelListAdatapter.notifyDataSetChanged();
                }else {
                    list_hotel_kind = new ArrayList<Hoteldata>();
                    for(int i=0;i<list_hotel.size();i++){
                        if(list_hotel.get(i).getSpecise()==position){
                            list_hotel_kind.add(list_hotel.get(i));
                        }
                    }
                    hotelListAdatapter.setList_data(list_hotel_kind);
                    hotelListAdatapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchView=(SearchView)view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length()!=0) {
                    L.i(TAG, "我要搜索" + query);
                    ArrayList<Hoteldata> listpassdata = new ArrayList<Hoteldata>();
                    for (int i = 0; i < list_hotel.size(); i++) {
                        if (list_hotel.get(i).getTitle().contains(query)) {
                            listpassdata.add(list_hotel.get(i));
                            L.i(TAG, "listpassdata找到一个");
                        }
                    }
                    if (listpassdata.size() != 0) {
                        Intent it = new Intent(getActivity(), HotelSearchActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("searchdata", listpassdata);
                        ArrayList<String> listpassaddress=new ArrayList<String>();
                        for(int i=0;i<listpassdata.size();i++){
                            listpassaddress.add(listpassdata.get(i).getObjectId());
                        }
                        bundle.putStringArrayList("searchdataaddress",listpassaddress);
//                        bundle.putString("title",list.get(0).getTitle());
//                        bundle.putString("context",list.get(0).getContext());
//                        bundle.putString("price",list.get(0).getPrice()+"");
//                        bundle.putString("id",list.get(0).getObjectId());
//                        bundle.putStringArrayList("remarklist",list.get(0).getList_remarkd());
                        it.putExtras(bundle);
                        getActivity().startActivity(it);
                    } else {
                        T.showShot(mcontext, "没有搜索到");
                    }
                }
                //   updataviewlimit(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }
}
