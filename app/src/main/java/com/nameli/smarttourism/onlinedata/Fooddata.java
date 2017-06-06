package com.nameli.smarttourism.onlinedata;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/17.
 */

public class Fooddata extends BmobObject implements Parcelable{
    private String title;
    private String context;
    private boolean pic_status;
    private int price;
    private int specise;
    private ArrayList<String> list_remarkd;
    public Fooddata(){}
    public Fooddata(String title, String context, boolean pic_status, int price, int specise, ArrayList<String> list_remarkd) {
        this.title = title;
        this.context = context;
        this.pic_status = pic_status;
        this.price = price;
        this.specise = specise;
        this.list_remarkd = list_remarkd;
    }

    protected Fooddata(Parcel in) {
        title = in.readString();
        context = in.readString();
        pic_status = in.readByte() != 0;
        price = in.readInt();
        specise = in.readInt();
        list_remarkd = in.createStringArrayList();
    }

    public static final Creator<Fooddata> CREATOR = new Creator<Fooddata>() {
        @Override
        public Fooddata createFromParcel(Parcel in) {
            return new Fooddata(in);
        }

        @Override
        public Fooddata[] newArray(int size) {
            return new Fooddata[size];
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

    public ArrayList<String> getList_remarkd() {
        return list_remarkd;
    }

    public void setList_remarkd(ArrayList<String> list_remarkd) {
        this.list_remarkd = list_remarkd;
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
        dest.writeInt(price);
        dest.writeInt(specise);
        dest.writeStringList(list_remarkd);
    }
}
