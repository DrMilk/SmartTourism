package com.nameli.smarttourism.lbstest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 2017/4/3.
 */
//bean  添加覆盖物
public class MarkInfo implements Serializable{
    private String TAG="MarkInfo";
    private double latitude;
    private double longitude;
    private int ImageId;
    private String name;
    private String distance;
    private int zanNum;

//    public static List<MarkInfo> infos = new ArrayList<MarkInfo>();//实例化list 演示从服务器返回的数据
//
//    static
//    {
//        infos.add(new MarkInfo(32.079254, 118.787623, java.R.mipmap.a01, "英伦贵族小旅馆", "距离209米", 1888));
//        infos.add(new MarkInfo(34.242952, 108.972171, java.R.mipmap.a02, "沙井国际洗浴会所",
//                "距离897米", 456));
//        infos.add(new MarkInfo(34.242852, 108.973171, java.R.mipmap.a03, "五环服装城",
//                "距离249米", 1456));
//        infos.add(new MarkInfo(34.242152, 108.971971, java.R.mipmap.a04, "老米家泡馍小炒",
//                "距离679米", 1456));
//    }

    public MarkInfo(double latitude, double longitude, int imageId,
                    String name, String distance, int zanNum) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        ImageId = imageId;
        this.name = name;
        this.distance = distance;
        this.zanNum = zanNum;
    }
    public MarkInfo() {
        super();
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public int getImageId() {
        return ImageId;
    }
    public void setImageId(int imageId) {
        ImageId = imageId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDistance() {
        return distance;
    }
    public void setDistance(String distance) {
        this.distance = distance;
    }
    public int getZanNum() {
        return zanNum;
    }
    public void setZanNum(int zanNum) {
        this.zanNum = zanNum;
    }

}