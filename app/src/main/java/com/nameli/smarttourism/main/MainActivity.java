package com.nameli.smarttourism.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.sunflower.FlowerCollector;
import com.nameli.smarttourism.R;
import com.nameli.smarttourism.Utils.JsonParser;
import com.nameli.smarttourism.Utils.L;
import com.nameli.smarttourism.Utils.T;
import com.nameli.smarttourism.customView.XuVoiceDialog;
import com.nameli.smarttourism.food.FoodDetailActivity;
import com.nameli.smarttourism.food.FoodFragment;
import com.nameli.smarttourism.hotel.HotelDetailActivity;
import com.nameli.smarttourism.hotel.HotelFragment;
import com.nameli.smarttourism.mine.DingweiMap;
import com.nameli.smarttourism.mine.MineFragment;
import com.nameli.smarttourism.mine.WeatherActivity;
import com.nameli.smarttourism.onlinedata.Alldata;
import com.nameli.smarttourism.onlinedata.Fooddata;
import com.nameli.smarttourism.onlinedata.Hoteldata;
import com.nameli.smarttourism.onlinedata.Spotdata;
import com.nameli.smarttourism.onlinedata.Traveldata;
import com.nameli.smarttourism.travel.TravelDetailActivity;
import com.nameli.smarttourism.travel.TravelFragment;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Administrator on 2017/3/16.
 */

public class MainActivity extends Activity implements View.OnClickListener{
    private String TAG="MainActivity";
    private Context mcontext;
    public Alldata alldata;
    private LinearLayout linearLayout_firstab;
    private LinearLayout linearLayout_travel;
    private LinearLayout linearLayout_food;
    private LinearLayout linearLayout_hotel;
    private LinearLayout linearLayout_mine;
    private Fragment fragment_firstab;
    private Fragment fragment_travel;
    private Fragment fragment_food;
    private Fragment fragment_hotel;
    private Fragment fragment_mine;
    private FirstTabFragmentProcess fragment_process;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private int tab_num=0;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private SpeechRecognizer mIat;
    private SoundPool mtinksong;
    private HashMap<Integer,Integer> soundPoolMap;
    private XuVoiceDialog xuVoiceDialog;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private int count_speech=0;
    private String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext=this;
        initView();
        mbmobinitdata();
        initFragment();
        initspeech();
        initsound();
    }
    private void initsound() {
        //   AudioAttributes attr=new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        mtinksong=new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(0,mtinksong.load(this, R.raw.bdspeech_recognition_start,1));
        soundPoolMap.put(1,mtinksong.load(this, R.raw.bdspeech_recognition_cancel,1));
        soundPoolMap.put(2,mtinksong.load(this, R.raw.bdspeech_recognition_success,1));
        soundPoolMap.put(3,mtinksong.load(this, R.raw.bdspeech_recognition_error,1));
        soundPoolMap.put(4,mtinksong.load(this, R.raw.bdspeech_speech_end,1));
    }
    private void initspeech() {
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=56fba51c");
        FlowerCollector.setDebugMode(true);
        FlowerCollector.setCaptureUncaughtException(true);
        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        mIat= SpeechRecognizer.createRecognizer(this, null);
        Log.i("Wu","静鸡巴事");
//2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
//3.开始听写
//听写监听器
    }
    private void initView() {
        linearLayout_firstab= (LinearLayout) findViewById(R.id.activity_main_firsttab);
        linearLayout_travel= (LinearLayout) findViewById(R.id.activity_main_travel);
        linearLayout_food= (LinearLayout) findViewById(R.id.activity_main_food);
        linearLayout_hotel= (LinearLayout) findViewById(R.id.activity_main_hotel);
        linearLayout_mine= (LinearLayout) findViewById(R.id.activity_main_mine);
        linearLayout_firstab.setOnClickListener(this);
        linearLayout_food.setOnClickListener(this);
        linearLayout_hotel.setOnClickListener(this);
        linearLayout_mine.setOnClickListener(this);
        linearLayout_travel.setOnClickListener(this);
        img1= (ImageView) findViewById(R.id.bottom_img1);
        img2= (ImageView) findViewById(R.id.bottom_img2);
        img3= (ImageView) findViewById(R.id.bottom_img3);
        img4= (ImageView) findViewById(R.id.bottom_img4);
        img5= (ImageView) findViewById(R.id.bottom_img5);
        FloatingActionButton yuyin = (FloatingActionButton) findViewById(R.id.yuyin);
        yuyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        yuyin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()== MotionEvent.ACTION_DOWN){
                    mIat.cancel();
//                    mtinksong.play(soundPoolMap.get(0),     //声音资源
//                            volumnRatio,         //左声道
//                            volumnRatio,         //右声道
//                            1,             //优先级，0最低
//                            0,         //循环次数，0是不循环，-1是永远循环
//                            1);            //回放速度，0.5-2.0之间。1为正常速度
//                };
                    xuVoiceDialog=new XuVoiceDialog(mcontext);
                    xuVoiceDialog.show();
                    mtinksong.play(soundPoolMap.get(0),1,1,1,0,1);
                    //  img_yuyin.setVisibility(View.VISIBLE);
                    Log.i("Wu", mIat.startListening(mRecognizerListener)+"");
                    mIat.startListening(mRecognizerListener);
                    Log.i("Wu","按了");
                }
                if(event.getAction()== MotionEvent.ACTION_UP){
                    mIat.stopListening();
                    //        char_list_baseadapter.notify();
                    Log.i("Wu","松开");
                    xuVoiceDialog.dismiss();
                    //anim_draw.stop();
                    //    img_yuyin.setVisibility(View.INVISIBLE);
                    mtinksong.play(soundPoolMap.get(4),1,1,1,0,1);
                }
                return false;
            }
        });
        yuyin.setVisibility(View.GONE);
    }
    private void initFragment() {
        fragment_process=new FirstTabFragmentProcess();
        BmobQuery<Alldata> query = new BmobQuery<Alldata>();
        query.getObject("9eeb241fdc", new QueryListener<Alldata>() {

            @Override
            public void done(Alldata object, BmobException e) {
                if(e==null){
                    alldata=object;
                    img1.setImageResource(R.mipmap.host_index_cate_icon_s);
                    fragment_firstab=new FirstTabFragment();
                    Bundle bundle=new Bundle();
                    bundle.putStringArrayList("data",alldata.getList_spot());
                    fragment_firstab.setArguments(bundle);
                    fragment_hotel=new HotelFragment();
                    bundle=new Bundle();
                    bundle.putStringArrayList("data",alldata.getList_hotel());
                    fragment_hotel.setArguments(bundle);
                    fragment_food=new FoodFragment();
                    bundle=new Bundle();
                    bundle.putStringArrayList("data",alldata.getList_food());
                    fragment_food.setArguments(bundle);
                    fragment_travel=new TravelFragment();
                    bundle=new Bundle();
                    bundle.putStringArrayList("data",alldata.getList_travel());
                    fragment_travel.setArguments(bundle);
                    updataChangeFragment();
                    L.i(TAG,"all"+"下载成功");
                }else{
                    L.i(TAG,"all"+"下载失败");
                }
            }

        });
        fragment_mine=new MineFragment();
        fm=getFragmentManager();
        ft=fm.beginTransaction();
        if(fragment_firstab!=null)
            ft.replace(R.id.activity_main_main,fragment_firstab);
        else
            ft.replace(R.id.activity_main_main,fragment_process);
        ft.commit();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_firsttab:
                initBottomView();
                ft=fm.beginTransaction();
                tab_num=0;
                if(fragment_firstab!=null){
                    L.i(TAG,"fragment_firstab");
                    ft.replace(R.id.activity_main_main,fragment_firstab);
                    L.i(TAG,"fragment_firstab");
                }
                else {
                    ft.replace(R.id.activity_main_main,fragment_process);
                   fragment_process.updataTitle("景点");
                }

                ft.commit();break;
            case R.id.activity_main_travel:
                initBottomView();
                img2.setImageResource(R.mipmap.host_index_cart_icon_s);
                ft=fm.beginTransaction();
                tab_num=1;
                if(fragment_travel!=null){
                    L.i(TAG,"fragment_travel");
                    ft.replace(R.id.activity_main_main,fragment_travel);
                    L.i(TAG,"fragment_travel");
                }
                else{
                    ft.replace(R.id.activity_main_main,fragment_process);
                    fragment_process.updataTitle("游记");
                }
                ft.commit();break;
            case R.id.activity_main_food:
                initBottomView();
                img3.setImageResource(R.mipmap.host_index_cart_icon_s_food5);
                ft=fm.beginTransaction();
                tab_num=2;
                L.i(TAG,"美食1");
                if(fragment_food!=null){
                    L.i(TAG,"fragment_food");
                    ft.replace(R.id.activity_main_main,fragment_food);
                    L.i(TAG,"fragment_food");
                }
                else{
                    ft.replace(R.id.activity_main_main,fragment_process);
                    L.i(TAG,"美食");
                    fragment_process.updataTitle("美食");
                }
                ft.commit();break;
            case R.id.activity_main_hotel:
                initBottomView();
                img4.setImageResource(R.mipmap.host_index_home_icon_s);
                ft=fm.beginTransaction();
                tab_num=3;
                if(fragment_hotel!=null){
                     L.i(TAG,"fragment_hotel");
                     ft.replace(R.id.activity_main_main,fragment_hotel);
                     L.i(TAG,"fragment_hotel");
                }
                else{
                    ft.replace(R.id.activity_main_main,fragment_process);
                    fragment_process.updataTitle("酒店");
                }
                ft.commit();break;
            case R.id.activity_main_mine:
                initBottomView();
                img5.setImageResource(R.mipmap.host_index_mine_icon_s);
                ft=fm.beginTransaction();
                tab_num=4;
                if(fragment_mine!=null){
                    L.i(TAG,"fragment_mine");
                    ft.replace(R.id.activity_main_main,fragment_mine);
                    L.i(TAG,"fragment_mine");
                 }
                else
                    ft.replace(R.id.activity_main_main,fragment_process);
                ft.commit();break;
        }
    }

    private void initBottomView() {
        img2.setImageResource(R.mipmap.host_index_cart_icon);
        img1.setImageResource(R.mipmap.host_index_cate_icon);
        img3.setImageResource(R.mipmap.host_index_cart_icon_s_food5);
        img4.setImageResource(R.mipmap.host_index_home_icon);
        img5.setImageResource(R.mipmap.host_index_mine_icon);
    }

    public void updataChangeFragment(){
        switch (tab_num){
            case 0:
                ft=fm.beginTransaction();
                tab_num=0;
                if(fragment_firstab!=null)
                    ft.replace(R.id.activity_main_main,fragment_firstab);
                else {
                    ft.replace(R.id.activity_main_main,fragment_process);
                    fragment_process.updataTitle("景点");
                }
                ft.commit();break;
            case 1:
                ft=fm.beginTransaction();
                tab_num=1;
                if(fragment_travel!=null)
                    ft.replace(R.id.activity_main_main,fragment_travel);
                else{
                    ft.replace(R.id.activity_main_main,fragment_process);
                    fragment_process.updataTitle("游记");
                }
                ft.commit();break;
            case 2:
                tab_num=2;
                if(fragment_food!=null)
                    ft.replace(R.id.activity_main_main,fragment_food);
                else{
                    ft.replace(R.id.activity_main_main,fragment_process);
                    fragment_process.updataTitle("美食");
                }
                ft.commit();break;
            case 3:
                ft=fm.beginTransaction();
                tab_num=3;
                if(fragment_hotel!=null)
                    ft.replace(R.id.activity_main_main,fragment_hotel);
                else{
                    ft.replace(R.id.activity_main_main,fragment_process);
                    fragment_process.updataTitle("酒店");
                }
                ft.commit();break;
            case 4:
                ft=fm.beginTransaction();
                tab_num=4;
                if(fragment_mine!=null)
                    ft.replace(R.id.activity_main_main,fragment_mine);
                else
                    ft.replace(R.id.activity_main_main,fragment_process);
                ft.commit();break;
        }
    }
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            //   showTip("开始说话");
            Log.i("Wu","开始说话");
        }

        @Override
        public void onError(SpeechError var1) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限
//            Log.i("Wu",var1.getErrorCode()+"");
//            Log.i("Wu",var1.getErrorDescription());
//            anim_draw.stop();
//            linear_line.setVisibility(View.INVISIBLE);
            mtinksong.play(soundPoolMap.get(3),1,1,0,0,1);
            T.showShot(mcontext,"语音识别失败！");
            //      showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            //   showTip("结束说话");
//            anim_draw.stop();
//            linear_line.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onResult(com.iflytek.cloud.RecognizerResult var1, boolean var2) {
            key="";
//            anim_draw.stop();
//            linear_line.setVisibility(View.INVISIBLE);
             count_speech=count_speech%2;
//            Log.i("Wu",count_speech+"");
            if(count_speech==0) {
                mtinksong.play(soundPoolMap.get(2),1,1,0,0,1);
//                updatelistview(printResult(var1),R.mipmap.register_default_photo,true);
//                wuspeechreflect(var1);
                L.i("Wu",printResult(var1));
                Log.i("Wu", "成功了 叫你爸爸1" + var1.getResultString());
                Log.i("Wu", "成功了 叫你爸爸2" + var1.toString());
                Log.i("Wu", "成功了 叫你爸爸3" + var1.describeContents());
                key=printResult(var1);
                if(key.contains("景点")){
                    BmobQuery<Spotdata> query=new BmobQuery<Spotdata>();
                    String keynew=key.replace("景点","").replace("。","").trim();
                    L.i(TAG,keynew);
                    T.showShot(mcontext,"正在为你查找"+keynew);
                    query.addWhereEqualTo("title",keynew);
                    query.findObjects(new FindListener<Spotdata>() {
                        @Override
                        public void done(List<Spotdata> list, BmobException e) {
                            if(e==null){
                                Spotdata spotdata=list.get(0);
                                Intent it=new Intent(MainActivity.this,SpotDetailActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("title",spotdata.getTitle());
                                bundle.putString("context",spotdata.getContext());
                                bundle.putString("id",spotdata.getObjectId());
                                it.putExtras(bundle);
                                MainActivity.this.startActivity(it);
                            }else {
                                T.showShot(mcontext,"对不起没有找到"+key);
                                L.i(TAG,"对不起没有找到"+key+e.toString());
                            }
                        }
                    });
                }else if(key.contains("游记")){
                    BmobQuery<Traveldata> query=new BmobQuery<>();
                    String keynew=key.replace("游记","");
                    T.showShot(mcontext,"正在为你查找"+keynew);
                    query.addWhereEqualTo("title",keynew);
                    query.findObjects(new FindListener<Traveldata>() {
                        @Override
                        public void done(List<Traveldata> list, BmobException e) {
                            if(e==null){
                                Intent it=new Intent(MainActivity.this,TravelDetailActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("title",list.get(0).getTitle());
                                bundle.putString("context",list.get(0).getContext());
                                bundle.putStringArrayList("remarklist",list.get(0).getList_remark());
                                bundle.putString("id",list.get(0).getObjectId());
                                it.putExtras(bundle);
                                startActivity(it);
                            }else {
                                T.showShot(mcontext,"对不起没有找到"+key);
                            }
                        }
                    });
                }else if(key.contains("美食")){
                    BmobQuery<Fooddata> query=new BmobQuery<>();
                    String keynew=key.replace("美食","");
                    T.showShot(mcontext,"正在为你查找"+keynew);
                    query.addWhereEqualTo("title",keynew);
                    query.findObjects(new FindListener<Fooddata>() {
                        @Override
                        public void done(List<Fooddata> list, BmobException e) {
                            if(e==null){
                                Intent it=new Intent(MainActivity.this,FoodDetailActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("title",list.get(0).getTitle());
                                bundle.putString("context",list.get(0).getContext());
                                bundle.putString("price",list.get(0).getPrice()+"");
                                bundle.putString("id",list.get(0).getObjectId());
                                bundle.putStringArrayList("remarklist",list.get(0).getList_remarkd());
                                it.putExtras(bundle);
                                MainActivity.this.startActivity(it);
                            }else {
                                T.showShot(mcontext,"对不起没有找到"+key);
                            }
                        }
                    });
                }else if(key.contains("酒店")){
                    BmobQuery<Hoteldata> query=new BmobQuery<>();
                    String keynew=key.replace("酒店","");
                    T.showShot(mcontext,"正在为你查找"+keynew);
                    query.addWhereEqualTo("title",keynew);
                    query.findObjects(new FindListener<Hoteldata>() {
                        @Override
                        public void done(List<Hoteldata> list, BmobException e) {
                            if(e==null){
                                Intent it=new Intent(MainActivity.this,HotelDetailActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("title",list.get(0).getTitle());
                                bundle.putString("context",list.get(0).getContext());
                                bundle.putString("price",list.get(0).getPrice()+"");
                                bundle.putString("id",list.get(0).getObjectId());
                                bundle.putStringArrayList("remarklist",list.get(0).getList_remark());
                                it.putExtras(bundle);
                                startActivity(it);
                            }else {
                                T.showShot(mcontext,"对不起没有找到"+key);
                            }
                        }
                    });
                }else if(key.contains("天气")){
                    Intent it4=new Intent(MainActivity.this,WeatherActivity.class);startActivity(it4);
                }else if(key.contains("位置")){
                    Intent it5=new Intent(MainActivity.this,DingweiMap.class);startActivity(it5);
                }else{
                    T.showShot(mcontext,"没有找到"+key);
                    T.showShot(mcontext,"请说关键字"+"景点 游记 美食 酒店 天气 位置等...");
                }
//                //     Log.i("Wu","成功了 叫你爸爸3"+var1.writeToParcel(););
            }
            count_speech++;
        }
        @Override
        public void onVolumeChanged(int var1, byte[] var2) {
            if(var1>1){
//                anim_draw.start();
            }
        }
        private String printResult(com.iflytek.cloud.RecognizerResult results) {
            String text = JsonParser.parseIatResult(results.getResultString());
          //  Log.i("Wu","text"+text);
            String sn = null;
            // 读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString());
                sn = resultJson.optString("sn");
             //   Log.i("Wu","textsn"+sn);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mIatResults.put(sn, text);
            StringBuffer resultBuffer = new StringBuffer();
            int i=0;
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
          //      Log.i("Wu","key"+mIatResults.get(key)+i);
                i++;
            }
           // Log.i("Wu",resultBuffer.toString());
            return resultBuffer.toString();
        }
        @Override
        public void onEvent(int var1, int var2, int var3, Bundle var4) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };
    private void mbmobinitdata() {
        Bmob.initialize(this, "5d01eaa5fc932e51cba2f9ed91c32855");
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(this)
                //设置appkey
                .setApplicationId("5d01eaa5fc932e51cba2f9ed91c32855")
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024*1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
    }
}
