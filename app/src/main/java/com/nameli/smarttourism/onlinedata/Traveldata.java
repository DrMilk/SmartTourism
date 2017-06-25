package com.nameli.smarttourism.onlinedata;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/17.
 */

public class Traveldata extends BmobObject implements Parcelable{
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

    protected Traveldata(Parcel in) {
        title = in.readString();
        context = in.readString();
        pic_status = in.readByte() != 0;
        list_remark = in.createStringArrayList();
    }

    public static final Creator<Traveldata> CREATOR = new Creator<Traveldata>() {
        @Override
        public Traveldata createFromParcel(Parcel in) {
            return new Traveldata(in);
        }

        @Override
        public Traveldata[] newArray(int size) {
            return new Traveldata[size];
        }
    };

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
        if(list_remark==null){
            return new ArrayList<String>();
        }
        return list_remark;
    }

    public void setList_remark(ArrayList<String> list_remark) {
        this.list_remark = list_remark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(context);
        dest.writeByte((byte) (pic_status ? 1 : 0));
        dest.writeStringList(list_remark);
    }
}
