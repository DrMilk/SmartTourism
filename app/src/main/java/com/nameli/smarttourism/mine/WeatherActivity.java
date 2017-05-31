package com.nameli.smarttourism.mine;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.nameli.smarttourism.R;
import com.nameli.smarttourism.Utils.L;

import java.util.List;

/**
 * Created by Administrator on 2017/3/18.
 */

public class WeatherActivity extends Activity implements WeatherSearch.OnWeatherSearchListener {
    private String TAG="WeatherActivity";
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    cityname=amapLocation.getDistrict();//城市信息
                    Log.i("Wu",amapLocation.getCity());
                    init();
                    searchliveweather();
                    searchforcastsweather();
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };
    private Animation anim_bird_go;
    private AnimationDrawable anim_bird;
    private ImageView img_bird;
    private TextView now_weather;
    private TextView text_data;
    private Animation anim_sunshine;
    private ImageView sunshine;
    private TextView today_temp;
    private TextView today_weather;
    private ImageView today_weather_img;
    private TextView tomorrow_temp;
    private TextView tomorrow_weather;
    private ImageView tomorrow_weather_img;
    private TextView nurture_temp;
    private TextView nurture_weather;
    private ImageView nurture_weather_img;
    private ImageView today_tempnum_img_left;
    private ImageView today_tempnum_img_right;
    private TextView today_tempnum_symbol;
    private TextView today_dum_level;
    private ImageView today_wind_img;
    private TextView today_wind_level;
    private WeatherSearchQuery mquery;
    private WeatherSearch mweathersearch;
    private LocalWeatherLive weatherlive;
    private LocalWeatherForecast weatherforecast;
    private List<LocalDayWeatherForecast> forecastlist = null;
    private String cityname;//天气搜索的城市，可以写名称或adcode；
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnHead();
        setContentView(R.layout.weather);
        loctioninit();
    }

    private void loctioninit() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
//设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
//设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
//设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
//设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
//给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
//启动定位
        mLocationClient.startLocation();
    }

    private void init() {
//        WeartherprogressDialog wuDialog=new WeartherprogressDialog(this);
//        wuDialog.show();
        goanim();
        now_weather=(TextView)findViewById(R.id.weather_now_weather);
        today_temp=(TextView)findViewById(R.id.weather_today_temp);
        today_weather=(TextView)findViewById(R.id.weather_today_weather);
        today_weather_img=(ImageView)findViewById(R.id.weather_today__weather_img);
        tomorrow_temp=(TextView)findViewById(R.id.weather_tomorrow_temp);
        tomorrow_weather=(TextView)findViewById(R.id.weather_tomorrow_weather);
        tomorrow_weather_img=(ImageView)findViewById(R.id.weather_tomorrow__weather_img);
        nurture_temp=(TextView)findViewById(R.id.weather_nurture_temp);
        nurture_weather=(TextView)findViewById(R.id.weather_nurture_weather);
        nurture_weather_img=(ImageView)findViewById(R.id.weather_nurture__weather_img);
        text_data=(TextView)findViewById(R.id.weather_data);
        TextView city =(TextView)findViewById(R.id.city);
        city.setText(cityname);
        // reporttime1 = (TextView)findViewById(R.id.reporttime1);
        // reporttime2 = (TextView)findViewById(R.id.reporttime2);
        today_tempnum_img_left=(ImageView)findViewById(R.id.weather_today_temp_img_left);
        today_tempnum_img_right=(ImageView)findViewById(R.id.weather_today_temp_img_right);
        today_tempnum_symbol=(TextView)findViewById(R.id.weather_today_temp_symbol);
        today_dum_level=(TextView)findViewById(R.id.weather_today_humidity);
        today_wind_img=(ImageView)findViewById(R.id.weather_today_wind_img);
        today_wind_level=(TextView) findViewById(R.id.weather_today_wind_level);
    }

    private void goanim() {
        //  sunshine=(ImageView)findViewById(R.id.weather_sunshine) ;
        img_bird=(ImageView)findViewById(R.id.weather_bird);
        ImageView weather_fog_cloud=(ImageView)findViewById(R.id.weather_fog_cloud);
        ImageView weather_fog_cloud2=(ImageView)findViewById(R.id.weather_fog_cloud2);
        ImageView weather_fog_fog1=(ImageView)findViewById(R.id.weather_fog_fog3);
        ImageView weather_fog_fog2=(ImageView)findViewById(R.id.weather_fog_fog4);
        LinearInterpolator li=new LinearInterpolator();
        Animation anim_cloud= AnimationUtils.loadAnimation(this,R.anim.fog_cloud);
        anim_bird_go= AnimationUtils.loadAnimation(this,R.anim.birdflygo);
        anim_bird_go.setInterpolator(li);
        anim_cloud.setInterpolator(li);
        anim_bird=(AnimationDrawable)img_bird.getDrawable();
        anim_bird.start();
        img_bird.startAnimation(anim_bird_go);
        weather_fog_cloud.startAnimation(anim_cloud);
        weather_fog_cloud2.startAnimation(anim_cloud);
        weather_fog_fog1.startAnimation(anim_cloud);
        weather_fog_fog2.startAnimation(anim_cloud);
        //  anim_sunshine= AnimationUtils.loadAnimation(this,R.anim.sunshine);
        //  sunshine.startAnimation(anim_sunshine);
    }

    private void searchforcastsweather() {
        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_FORECAST);//检索参数为城市和天气类型，实时天气为1、天气预报为2
        mweathersearch=new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }
    private void searchliveweather() {
        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_LIVE);//检索参数为城市和天气类型，实时天气为1、天气预报为2
        mweathersearch=new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }
    /**
     * 实时天气查询回调
     */
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult , int rCode) {
        if (rCode == 1000) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                weatherlive = weatherLiveResult.getLiveResult();
                //  reporttime1.setText(weatherlive.getReportTime()+"发布");
                now_weather.setText(weatherlive.getWeather());
                imgtemperture(weatherlive.getTemperature());
                // Temperature.setText(weatherlive.getTemperature()+"°");
                today_wind_img.setImageResource(imgwindDirection(weatherlive.getWindDirection()));
                today_wind_level.setText(weatherlive.getWindPower()+"级");
                today_dum_level.setText(weatherlive.getHumidity()+"%");
                L.i(TAG,weatherlive.getWeather());
            }else {
                //     ToastUtil.show(WeatherSearchActivity.this, R.string.no_result);
                L.i(TAG,"aaaa");
            }
        }else {
            L.i(TAG,rCode+"");
            //    ToastUtil.showerror(WeatherSearchActivity.this, rCode);
        }
    }
    private void imgtemperture(String str) {
        int num= Integer.parseInt(str);
        if(num<0){
            today_tempnum_symbol.setText("—");
        }
        num= Math.abs(num);
        int ten_num=num/10;
        switch (ten_num){
            case 0:;break;
            case 1:today_tempnum_img_left.setImageResource(R.mipmap.t1);break;
            case 2:today_tempnum_img_left.setImageResource(R.mipmap.t2);break;
            case 3:today_tempnum_img_left.setImageResource(R.mipmap.t3);break;
            case 4:today_tempnum_img_left.setImageResource(R.mipmap.t4);break;
            case 5:today_tempnum_img_left.setImageResource(R.mipmap.t5);break;
        }
        int singer_num=num%10;
        switch (singer_num){
            case 0:today_tempnum_img_right.setImageResource(R.mipmap.t0);break;
            case 1:today_tempnum_img_right.setImageResource(R.mipmap.t1);break;
            case 2:today_tempnum_img_right.setImageResource(R.mipmap.t2);break;
            case 3:today_tempnum_img_right.setImageResource(R.mipmap.t3);break;
            case 4:today_tempnum_img_right.setImageResource(R.mipmap.t4);break;
            case 5:today_tempnum_img_right.setImageResource(R.mipmap.t5);break;
            case 6:today_tempnum_img_right.setImageResource(R.mipmap.t6);break;
            case 7:today_tempnum_img_right.setImageResource(R.mipmap.t7);break;
            case 8:today_tempnum_img_right.setImageResource(R.mipmap.t8);break;
            case 9:today_tempnum_img_right.setImageResource(R.mipmap.t9);break;
        }

    }
    private int imgwindDirection(String str) {
        int wuid=0;
        switch (str){
            case "北":wuid=R.mipmap.trend_wind_1;break;
            case "东":wuid=R.mipmap.trend_wind_3;break;
            case "南":wuid=R.mipmap.trend_wind_5;break;
            case "西":wuid=R.mipmap.trend_wind_7;break;
            case "东北":wuid=R.mipmap.trend_wind_2;break;
            case "西北":wuid=R.mipmap.trend_wind_8;break;
            case "东南":wuid=R.mipmap.trend_wind_4;break;
            case "西南":wuid=R.mipmap.trend_wind_6;break;
            case "无风向":wuid=R.mipmap.trend_wind_1;break;
            case "旋转不定":wuid=R.mipmap.trend_wind_1;break;

        }
        return wuid;
    }

    /**
     * 天气预报查询结果回调
     * */
    @Override
    public void onWeatherForecastSearched(
            LocalWeatherForecastResult weatherForecastResult, int rCode) {
        if (rCode == 1000) {
            if (weatherForecastResult!=null && weatherForecastResult.getForecastResult()!=null
                    && weatherForecastResult.getForecastResult().getWeatherForecast()!=null
                    && weatherForecastResult.getForecastResult().getWeatherForecast().size()>0) {
                L.i(TAG,"到这步了吗");
                weatherforecast = weatherForecastResult.getForecastResult();
                forecastlist= weatherforecast.getWeatherForecast();
                fillforecast();

            }else {
                //   ToastUtil.show(WeatherSearchActivity.this, R.string.no_result);
            }
        }else {
            L.i(TAG,rCode+"错误码");
            //   ToastUtil.showerror(WeatherSearchActivity.this, rCode);
        }
    }
    private void fillforecast() {
         //  reporttime2.setText(weatherforecast.getReportTime()+"发布");
        String forecast="";
        //   for (int i = 0; i < forecastlist.size(); i++) {
        //       LocalDayWeatherForecast localdayweatherforecast=forecastlist.get(i);
        //           String temp =String.format("%-3s/%3s",
        //                   localdayweatherforecast.getDayTemp()+"°",
        //                   localdayweatherforecast.getNightTemp()+"°"+localdayweatherforecast.getDayWeather());
        //           String date = localdayweatherforecast.getDate();
        //           forecast+=date+"  "+week+"                       "+temp+"\n\n";
        //       }
        LocalDayWeatherForecast localdayweatherforecast0=forecastlist.get(0);
        String week = null;
        switch (Integer.valueOf(localdayweatherforecast0.getWeek())) {
            case 1:
                week = "周一";
                break;
            case 2:
                week = "周二";
                break;
            case 3:
                week = "周三";
                break;
            case 4:
                week = "周四";
                break;
            case 5:
                week = "周五";
                break;
            case 6:
                week = "周六";
                break;
            case 7:
                week = "周日";
                break;
            default:
                break;
        }
        text_data.setText(localdayweatherforecast0.getDate()+" "+week);
        StringBuffer sb0=new StringBuffer();
        sb0.append(localdayweatherforecast0.getDayTemp()+"/");
        sb0.append(localdayweatherforecast0.getNightTemp()+"°C");
        String weather0=localdayweatherforecast0.getDayWeather();
        today_temp.setText(sb0);
        today_weather.setText(weather0);
        L.i(TAG,"sb0.toString()");
        L.i(TAG,sb0.toString());
        today_weather_img.setImageResource(imgsourse(localdayweatherforecast0.getDayWeather()));
        LocalDayWeatherForecast localdayweatherforecast1=forecastlist.get(1);
        //   String temp1=String.format("%-2s/%2s",localdayweatherforecast1.getDayTemp()+localdayweatherforecast1.getNightTemp()+"°C");
        StringBuffer sb=new StringBuffer();
        sb.append(localdayweatherforecast1.getDayTemp()+"/");
        sb.append(localdayweatherforecast1.getNightTemp()+"°C");
        String weather1=localdayweatherforecast1.getDayWeather();
        tomorrow_temp.setText(sb);
        tomorrow_weather.setText(weather1);
        tomorrow_weather_img.setImageResource(imgsourse(localdayweatherforecast1.getDayWeather()));
        LocalDayWeatherForecast localdayweatherforecast2=forecastlist.get(2);
        //   String temp1=String.format("%-2s/%2s",localdayweatherforecast1.getDayTemp()+localdayweatherforecast1.getNightTemp()+"°C");
        StringBuffer sb2=new StringBuffer();
        sb2.append(localdayweatherforecast2.getDayTemp()+"/");
        sb2.append(localdayweatherforecast2.getNightTemp()+"°C");
        String weather2=localdayweatherforecast2.getDayWeather();
        nurture_temp.setText(sb2);
        nurture_weather.setText(weather2);
        L.i(TAG,"sb2.toString()");
        L.i(TAG,sb2.toString());
        nurture_weather_img.setImageResource(imgsourse(localdayweatherforecast2.getDayWeather()));
        //    forecasttv.setText(forecast);
    }
    private int imgsourse(String str){
        int mid=0;
        switch (str){
            case "晴":mid=R.mipmap.ww0;break;
            case "多云":mid=R.mipmap.ww2;break;
            case "阴":mid=R.mipmap.ww1;break;
            case "阵雨":mid=R.mipmap.ww3;break;
            case "雷阵雨":mid=R.mipmap.ww6;break;
            case "雷阵雨并伴有冰雹":mid=R.mipmap.ww6;break;
            case "雨夹雪":mid=R.mipmap.ww6;break;
            case "小雨":mid=R.mipmap.ww7;break;
            case "中雨":mid=R.mipmap.ww8;break;
            case "大雨":mid=R.mipmap.ww9;break;
            case "暴雨":mid=R.mipmap.ww10;break;
            case "大暴雨":mid=R.mipmap.ww10;break;
            case "特大暴雨":mid=R.mipmap.ww10;break;
            case "阵雪":mid=R.mipmap.ww13;break;
            case "小雪":mid=R.mipmap.ww14;break;
            case "中雪":mid=R.mipmap.ww15;break;
            case "大雪":mid=R.mipmap.ww16;break;
            case "暴雪":mid=R.mipmap.ww17;break;
            case "雾":mid=R.mipmap.ww45;break;
            case "冻雨":mid=R.mipmap.ww2;break;
            case "沙尘暴":mid=R.mipmap.ww45;break;
            case "小雨-中雨":mid=R.mipmap.ww7;break;
            case "中雨-大雨":mid=R.mipmap.ww8;break;
            case "大雨-暴雨":mid=R.mipmap.ww9;break;
            case "暴雨-大暴雨":mid=R.mipmap.ww10;break;
            case "大暴雨-特大暴雨":mid=R.mipmap.ww10;break;
            case "小雪-中雪":mid=R.mipmap.ww14;break;
            case "中雪-大雪":mid=R.mipmap.ww15;break;
            case "大雪-暴雪":mid=R.mipmap.ww16;break;
            case "浮尘":mid=R.mipmap.ww45;break;
            case "扬沙":mid=R.mipmap.ww45;break;
            case "强沙尘暴":mid=R.mipmap.ww45;break;
            case "飑":mid=R.mipmap.ww45;break;
            case "龙卷风":mid=R.mipmap.ww45;break;
            case "弱高吹雪":mid=R.mipmap.ww45;break;
            case "轻霾":mid=R.mipmap.ww45;break;
            case "霾":mid=R.mipmap.ww45;break;
        }
        return mid;
    }
    private void setOnHead() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    //       | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            );
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    //        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //   window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
}
