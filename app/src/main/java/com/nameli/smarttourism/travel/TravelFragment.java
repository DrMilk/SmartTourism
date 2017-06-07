package com.nameli.smarttourism.travel;

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
import android.widget.TextView;


import com.nameli.smarttourism.R;
import com.nameli.smarttourism.Utils.L;
import com.nameli.smarttourism.Utils.T;
import com.nameli.smarttourism.food.FoodSearchActivity;
import com.nameli.smarttourism.main.MainActivity;
import com.nameli.smarttourism.onlinedata.Fooddata;
import com.nameli.smarttourism.onlinedata.Spotdata;
import com.nameli.smarttourism.onlinedata.Traveldata;

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

public class TravelFragment extends Fragment {
    private SearchView searchView;
    private String TAG="TravelFragment";
    private ListView listView;
    private List<Traveldata> list_data;
    private Context mcontext;
    private ArrayList<String> list_str;
    private ArrayList<Traveldata> list_travel;
    private TravelListAdatapter travelFragment;
    private TextView text_add;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        mcontext=getActivity();
        list_str=getArguments().getStringArrayList("data");
        L.i(TAG,list_str.size()+"长度");
        if(list_str==null)
            list_str=new ArrayList<>();
        if(list_travel==null){
            list_travel=new ArrayList<>();
            for(int i=0;i<list_str.size();i++){
                BmobQuery<Traveldata> query = new BmobQuery<Traveldata>();
                query.getObject(list_str.get(i), new QueryListener<Traveldata>() {
                    @Override
                    public void done(Traveldata object, BmobException e) {
                        if(e==null){
                            list_travel.add(object);
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
            if(list_travel.size()==list_str.size()){
                updataContext();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        L.i(TAG,"Gengxinle");
                        travelFragment.notifyDataSetChanged();
                    }
                });
            }
    }
    private boolean updataContext() {
        L.i(TAG,"排序");
        final SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        final Date[] data1 = {null};
        final Date[] data2 = {null};
        for (int i=0;i<list_travel.size();i++){
            Log.i(TAG,list_travel.get(i).getCreatedAt()+"日期");
        }
        Comparator<Traveldata> comparator = new Comparator<Traveldata>(){
            public int compare(Traveldata s1, Traveldata s2) {
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
        if(list_travel.size()>1){
            Collections.sort(list_travel,comparator);
        }
        return true;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_travel,null);
        mcontext=getActivity();
        listView= (ListView) view.findViewById(R.id.activity_travel_listview);
        travelFragment=new TravelListAdatapter(mcontext,list_travel);
        listView.setAdapter(travelFragment);
        text_add= (TextView) view.findViewById(R.id.travel_text_add);
        text_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getActivity(),TravelAddActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("alldata",((MainActivity)getActivity()).alldata);
                it.putExtras(bundle);
                startActivity(it);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it=new Intent(getActivity(),TravelDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("title",list_travel.get(position).getTitle());
                bundle.putString("context",list_travel.get(position).getContext());
                bundle.putStringArrayList("remarklist",list_travel.get(position).getList_remark());
                bundle.putString("id",list_travel.get(position).getObjectId());
                it.putExtras(bundle);
                startActivity(it);
            }
        });
        searchView= (SearchView) view.findViewById(R.id.search_view);
        searchView= (SearchView) view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length()!=0){
                    L.i(TAG,"我要搜索"+query);
                    ArrayList<Traveldata> listpassdata=new ArrayList<Traveldata>();
                    for(int i=0;i<list_travel.size();i++){
                        if(list_travel.get(i).getTitle().contains(query)){
                            listpassdata.add(list_travel.get(i));
                            L.i(TAG,"listpassdata找到一个");
                        }
                    }
                    if(listpassdata.size()!=0){
                        Intent it=new Intent(getActivity(),TravelSearchActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putParcelableArrayList("searchdata",listpassdata);
                        ArrayList<String> listpassaddress=new ArrayList<String>();
                        for(int i=0;i<listpassdata.size();i++){
                            listpassaddress.add(listpassdata.get(i).getObjectId());
                            L.i(TAG,listpassdata.get(i).getObjectId()+"11111");
                            L.i(TAG,listpassaddress.get(i)+"11111");
                        }
                        bundle.putStringArrayList("searchdataaddress",listpassaddress);
//                        bundle.putString("title",list.get(0).getTitle());
//                        bundle.putString("context",list.get(0).getContext());
//                        bundle.putString("price",list.get(0).getPrice()+"");
//                        bundle.putString("id",list.get(0).getObjectId());
//                        bundle.putStringArrayList("remarklist",list.get(0).getList_remarkd());
                        it.putExtras(bundle);
                        getActivity().startActivity(it);
                    }else {
                        T.showShot(mcontext,"没有搜索到");
                    }
                }
                L.i(TAG,"搜索");
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
