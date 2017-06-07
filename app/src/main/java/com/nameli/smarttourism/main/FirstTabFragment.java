package com.nameli.smarttourism.main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;


import com.nameli.smarttourism.R;
import com.nameli.smarttourism.Utils.L;
import com.nameli.smarttourism.Utils.T;
import com.nameli.smarttourism.food.FoodSearchActivity;
import com.nameli.smarttourism.onlinedata.Hoteldata;
import com.nameli.smarttourism.onlinedata.Spotdata;

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

public class FirstTabFragment extends Fragment {
    private String TAG="FirstTabFragment";
    private ViewPager banner_viewPager;
    private Context mcontext;
    private List<ImageView> list_banner;
    private LinearLayout ll;
    private List<ImageView> list_banner_point;
    Thread banner_thread;
    private int num_baner=200;
    private ListView listview_spot;
    private boolean thread_lock=true;
    private ArrayList<Spotdata> list_spot;
    private ArrayList<String> list_str;
    private XuBannerViewpagerAdapter xubv;
    private SpotListAdatapter spotListAdatapter;
    private SearchView searchView;
    private ArrayList<Spotdata> sv_all=new ArrayList<>();
    private ArrayList<Spotdata> sv_limit=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                banner_viewPager.setCurrentItem(num_baner++);
            }
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
        if(list_spot==null){
            list_spot=new ArrayList<>();
            for(int i=0;i<list_str.size();i++){
                BmobQuery<Spotdata> query = new BmobQuery<Spotdata>();
                query.getObject(list_str.get(i), new QueryListener<Spotdata>() {
                    @Override
                    public void done(Spotdata object, BmobException e) {
                        if(e==null){
                            list_spot.add(object);
                            L.i(TAG,"all"+"下载成功");
                            if(list_spot.size()==list_str.size()){
                                updataview();
                            }
                        }else{
                            L.i(TAG,"all"+"下载失败");
                            L.i(TAG,e.toString());
                            updataview();
                        }
                    }

                });
            }
        }
        super.onCreate(savedInstanceState);
    }

    private void updataview() {
            updataContext();
            sv_all=new ArrayList<>();
            for(int i=0;i<list_spot.size();i++){
                sv_all.add(list_spot.get(i));
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    L.i(TAG,"Gengxinle");
                    spotListAdatapter.notifyDataSetChanged();
                }
            });
    }
    private boolean updataContext() {
        L.i(TAG,"排序");
        final SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        final Date[] data1 = {null};
        final Date[] data2 = {null};
        for (int i=0;i<list_spot.size();i++){
            Log.i(TAG,list_spot.get(i).getCreatedAt()+"日期");
        }
        Comparator<Spotdata> comparator = new Comparator<Spotdata>(){
            public int compare(Spotdata s1, Spotdata s2) {
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
        if(list_spot.size()>1){
            Collections.sort(list_spot,comparator);
        }
        return true;
    }
    private void updataviewlimit(String s){
        if(s.length()==0){
            for(int q=0;q<list_spot.size();q++){
                list_spot.remove(q);
            }
            for(int i=0;i<sv_all.size();i++){
                list_spot.add(sv_all.get(i));
            }
            spotListAdatapter.setList_data(list_spot);
        }else {
            for(int i=0;i<list_spot.size();i++){
                L.i(TAG,"循环~"+list_spot.size()+"  "+i);
                if(list_spot.get(i).getTitle().contains(s)){
                    //sv_limit.add(list_spot.get(i));
                    L.i(TAG,"搜索到了吗！");
                }else{
                    list_spot.remove(i);
                    i--;
                }
            }
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                L.i(TAG,"Gengxinle");
                spotListAdatapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onStart() {
        thread_lock=true;
        super.onStart();
    }

    @Override
    public void onStop() {
        thread_lock=false;
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_spot,null);
        banner_viewPager= (ViewPager) view.findViewById(R.id.main_banner_viewpager);
        ll= (LinearLayout) view.findViewById(R.id.main_banner_linear_point);
        listview_spot= (ListView) view.findViewById(R.id.activity_spot_listview);
        searchView= (SearchView) view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length()!=0) {
                    L.i(TAG, "我要搜索" + query);
                    ArrayList<Spotdata> listpassdata = new ArrayList<Spotdata>();
                    for (int i = 0; i < list_spot.size(); i++) {
                        if (list_spot.get(i).getTitle().contains(query)) {
                            listpassdata.add(list_spot.get(i));
                            L.i(TAG, "listpassdata找到一个");
                        }
                    }
                    if (listpassdata.size() != 0) {
                        L.i(TAG,listpassdata.get(0).getObjectId());
                        Intent it = new Intent(getActivity(), SpotSearchActivity.class);
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
        list_banner=new ArrayList<>();
        ImageView img1=new ImageView(mcontext);
        ImageView img2=new ImageView(mcontext);
        ImageView img3=new ImageView(mcontext);
        ImageView img4=new ImageView(mcontext);
        img1.setImageResource(R.mipmap.qcode_banner_01);
        img2.setImageResource(R.mipmap.qcode_banner_02);
        img3.setImageResource(R.mipmap.qcode_banner_03);
        img4.setImageResource(R.mipmap.qcode_banner_04);
        list_banner.add(img1);
        list_banner.add(img2);
        list_banner.add(img3);
        list_banner.add(img4);
        spotListAdatapter=new SpotListAdatapter(mcontext,list_spot);
        listview_spot.setAdapter(spotListAdatapter);
        listview_spot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it=new Intent(getActivity(),SpotDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("title",list_spot.get(position).getTitle());
                bundle.putString("context",list_spot.get(position).getContext());
                bundle.putString("id",list_spot.get(position).getObjectId());
                it.putExtras(bundle);
                getActivity().startActivity(it);
            }
        });
        xubv=new XuBannerViewpagerAdapter(list_banner);
        banner_viewPager.setAdapter(xubv);
        banner_viewPager.setPageTransformer(false,new XuBannerPageTransformer(1));
        banner_viewPager.setCurrentItem(200);
        banner_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                num_baner=position;
                position%=4;
                for(int ii=0;ii<4;ii++){
                    if(ii==position)
                        ll.getChildAt(ii).setBackgroundResource(R.drawable.banner_on_point);
                    else
                        ll.getChildAt(ii).setBackgroundResource(R.drawable.banner_off_point);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        list_banner_point=new ArrayList<>();
        for(int i=0;i<4;i++){
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(20,20);
            params.rightMargin=6;
            params.leftMargin=6;
            ImageView v = new ImageView(mcontext);
            if(i==0){
                v.setBackgroundResource(R.drawable.banner_on_point);
            }else {
                v.setBackgroundResource(R.drawable.banner_off_point);
            }
            v.setLayoutParams(params);
            ll.addView(v);
        }
        return view;
    }

    @Override
    public void onResume() {
        if(banner_thread==null){
            banner_thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        if(thread_lock){
                            handler.sendEmptyMessage(1);
                        }
                        try {
                            Thread.sleep(2000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
            banner_thread.start();
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
