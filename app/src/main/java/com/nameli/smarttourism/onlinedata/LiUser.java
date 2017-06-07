package com.nameli.smarttourism.onlinedata;

import java.util.ArrayList;
import java.util.Date;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/11/29.
 */

public class LiUser extends BmobUser{
    private String sex;
    private Date birthday;
    private String name;
    private String qq;
    private String wechat;
    private String address;
    private String passwordshow;
    private ArrayList<String> list_collect;
    public LiUser(){}

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        if(name!=null)
            return name;
        return "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQq() {
        if(qq!=null)
        return qq;
        return "";
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        if(wechat!=null)
            return wechat;
        return "";
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getAddress() {
        if(address!=null)
            return address;
        return "";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPasswordshow() {
        return passwordshow;
    }

    public void setPasswordshow(String passwordshow) {
        this.passwordshow = passwordshow;
    }

    public ArrayList<String> getList_collect() {
        if(list_collect!=null)
            return list_collect;
        return new ArrayList<>();
    }

    public void setList_collect(ArrayList<String> list_collect) {
        this.list_collect = list_collect;
    }
}
