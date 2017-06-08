package com.nameli.smarttourism.onlinedata;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/6/8.
 */

public class Pusheddata extends BmobObject implements Parcelable{
    private String title;
    private String message;
    private String context;
    private String address;
    public Pusheddata(){}

    public Pusheddata(String title, String message, String context, String address) {
        this.title = title;
        this.message = message;
        this.context = context;
        this.address = address;
    }

    protected Pusheddata(Parcel in) {
        title = in.readString();
        message = in.readString();
        context = in.readString();
        address = in.readString();
    }

    public static final Creator<Pusheddata> CREATOR = new Creator<Pusheddata>() {
        @Override
        public Pusheddata createFromParcel(Parcel in) {
            return new Pusheddata(in);
        }

        @Override
        public Pusheddata[] newArray(int size) {
            return new Pusheddata[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(message);
        dest.writeString(context);
        dest.writeString(address);
    }
}
