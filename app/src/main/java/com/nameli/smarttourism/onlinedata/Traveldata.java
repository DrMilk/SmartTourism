package com.nameli.smarttourism.onlinedata;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/17.
 */

public class Traveldata extends BmobObject{
    private String title;
    private String context;
    private boolean pic_status;
    private ArrayList<String> list_remark;
    public Traveldata(){}
    public Traveldata(String title, String context, boolean pic_status, ArrayList<String> list_remark) {
        this.title = title;
        this.context = context;
        this.pic_status = pic_status;
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

    public ArrayList<String> getList_remark() {
        return list_remark;
    }

    public void setList_remark(ArrayList<String> list_remark) {
        this.list_remark = list_remark;
    }
}
