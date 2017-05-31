package com.nameli.smarttourism.onlinedata;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/17.
 */

public class Hoteldata extends BmobObject {
    private String title;
    private String context;
    private boolean pic_status;
    private int price;
    private int specise;
    private ArrayList<String> list_remark;
    public Hoteldata(){}
    public Hoteldata(String title, String context, boolean pic_status, int price, int specise, ArrayList<String> list_remark) {
        this.title = title;
        this.context = context;
        this.pic_status = pic_status;
        this.price = price;
        this.specise = specise;
        this.list_remark = list_remark;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public boolean isPic_status() {
        return pic_status;
    }

    public void setPic_status(boolean pic_status) {
        this.pic_status = pic_status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSpecise() {
        return specise;
    }

    public void setSpecise(int specise) {
        this.specise = specise;
    }

    public ArrayList<String> getList_remark() {
        return list_remark;
    }

    public void setList_remark(ArrayList<String> list_remark) {
        this.list_remark = list_remark;
    }
}
