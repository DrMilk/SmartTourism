package com.nameli.smarttourism.food;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
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

public class FoodFragment extends Fragment {
    private Spinner spinner ;
    private ArrayList<Fooddata> list_food_kind=new ArrayList<>();
    private String TAG="FoodFragment";
    private ListView listView;
    private ArrayList<Fooddata> list_food;
    private Context mcontext;
    private ArrayList<String> list_str;
    private  FoodListAdatapter foodListAdatapter;
    private SearchView searchView;
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
        if(list_food==null){
            list_food=new ArrayList<>();
            for(int i=0;i<list_str.size();i++){
                BmobQuery<Fooddata> query = new BmobQuery<Fooddata>();
                query.getObject(list_str.get(i), new QueryListener<Fooddata>() {
                    @Override
                    public void done(Fooddata object, BmobException e) {
                        if(e==null){
                            list_food.add(object);
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
        if(list_food.size()==list_str.size()){
            updataContext();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    L.i(TAG,"Gengxinle");
                    foodListAdatapter.notifyDataSetChanged();
                }
            });
        }
    }
    private boolean updataContext() {
        L.i(TAG,"排序");
        final SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        final Date[] data1 = {null};
        final Date[] data2 = {null};
        for (int i=0;i<list_food.size();i++){
            Log.i(TAG,list_food.get(i).getCreatedAt()+"日期");
        }
        Comparator<Fooddata> comparator = new Comparator<Fooddata>(){
            public int compare(Fooddata s1, Fooddata s2) {
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
        if(list_food.size()>1){
            Collections.sort(list_food,comparator);
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_food,null);
        mcontext=getActivity();
        listView= (ListView) view.findViewById(R.id.activity_food_listview);
//        list_food.add(new Fooddata("大闸蟹","金秋时节，又是吃蟹的好日子。而最近",true,200));
//        list_food.add(new Fooddata("水煮肉片","北京市区免费接，8点人民广场准时出发",true,24));
//        list_food.add(new Fooddata("油香儿","美国市区免费接，8点人民广场准时出发",true,26));
//        list_food.add(new Fooddata("宫保鸡丁","泰国市区免费接，8点人民广场准时出发",true,75));
//        list_food.add(new Fooddata("水煮肉片","上海市区免费接，8点人民广场准时出发",true,100));
//        list_food.add(new Fooddata("油香儿","上海市区免费接，8点人民广场准时出发",true,151));
//        list_food.add(new Fooddata("大闸蟹","上海市区免费接，8点人民广场准时出发",true,97));
//        list_food.add(new Fooddata("宫保鸡丁","上海市区免费接，8点人民广场准时出发",true,20));
        foodListAdatapter=new FoodListAdatapter(mcontext,list_food);
        listView.setAdapter(foodListAdatapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it=new Intent(getActivity(),FoodDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("title",list_food.get(position).getTitle());
                bundle.putString("context",list_food.get(position).getContext());
                bundle.putString("price",list_food.get(position).getPrice()+"");
                bundle.putString("id",list_food.get(position).getObjectId());
                bundle.putStringArrayList("remarklist",list_food.get(position).getList_remarkd());
                it.putExtras(bundle);
                getActivity().startActivity(it);
                L.i(TAG,"点击了吗");
            }
        });
        spinner = (Spinner) view.findViewById(R.id.spin_food);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    foodListAdatapter.setList_data(list_food);
                    foodListAdatapter.notifyDataSetChanged();
                }else {
                    list_food_kind = new ArrayList<Fooddata>();
                    for(int i=0;i<list_food.size();i++){
                        if(list_food.get(i).getSpecise()==position){
                            list_food_kind.add(list_food.get(i));
                        }
                    }
                    foodListAdatapter.setList_data(list_food_kind);
                    foodListAdatapter.notifyDataSetChanged();
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
                if(query.length()!=0){
                    L.i(TAG,"我要搜索"+query);
                    ArrayList<Fooddata> listpassdata=new ArrayList<Fooddata>();
                    for(int i=0;i<list_food.size();i++){
                        if(list_food.get(i).getTitle().contains(query)){
                            listpassdata.add(list_food.get(i));
                            L.i(TAG,"listpassdata找到一个");
                        }
                    }
                    if(listpassdata.size()!=0){
                        Intent it=new Intent(getActivity(),FoodSearchActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putParcelableArrayList("searchdata",listpassdata);
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
                    }else {
                        T.showShot(mcontext,"没有搜索到");
                    }
//                    BmobQuery<Fooddata> query1 = new BmobQuery<Fooddata>();
//                    //查询username字段的值含有“sm”的数据
//                    query1.addWhereEqualTo("title", query);
//                    query1.setLimit(10);
//                    query1.findObjects(new FindListener<Fooddata>() {
//                        @Override
//                        public void done(List<Fooddata> list, BmobException e) {
//                            if(e==null){
//                                L.i(TAG,list.size()+"aaaaaaaaaaaaaaaaaaa");
//                                Intent it=new Intent(getActivity(),FoodDetailActivity.class);
//                                Bundle bundle=new Bundle();
//                                bundle.putString("title",list.get(0).getTitle());
//                                bundle.putString("context",list.get(0).getContext());
//                                bundle.putString("price",list.get(0).getPrice()+"");
//                                bundle.putString("id",list.get(0).getObjectId());
//                                bundle.putStringArrayList("remarklist",list.get(0).getList_remarkd());
//                                it.putExtras(bundle);
//                                getActivity().startActivity(it);
//                                L.i(TAG,"点击了吗");
//                            }else {
//                                L.i(TAG,e.toString());
//                                T.showShot(mcontext,"没有搜索到");
//                            }
//
//                        }
//                    });
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
