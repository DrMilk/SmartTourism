package com.nameli.smarttourism.onlinedata;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/17.
 */

public class Spotdata extends BmobObject implements Parcelable{
    private String title;
    private String context;
    private boolean pic_status;

    public Spotdata(String title, String context, boolean pic_status) {
        this.title = title;
        this.context = context;
        this.pic_status = pic_status;
    }

    protected Spotdata(Parcel in) {
        title = in.readString();
        context = in.readString();
        pic_status = in.readByte() != 0;
    }

    public static final Creator<Spotdata> CREATOR = new Creator<Spotdata>() {
        @Override
        public Spotdata createFromParcel(Parcel in) {
            return new Spotdata(in);
        }

        @Override
        public Spotdata[] newArray(int size) {
            return new Spotdata[size];
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(context);
        dest.writeByte((byte) (pic_status ? 1 : 0));
    }
}
