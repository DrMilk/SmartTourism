package com.nameli.smarttourism.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.nameli.smarttourism.R;
import com.nameli.smarttourism.Utils.L;
import com.nameli.smarttourism.Utils.MyUpload;
import com.nameli.smarttourism.onlinedata.Spotdata;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/17.
 */

public class SpotListAdatapter extends BaseAdapter {
    private String TAG="SpotListAdatapter";
    private MyUpload myUpload;
    private ArrayList<Spotdata> list_data;
    private MyViewHolder wuViewHolder;
    private LayoutInflater mlayoutinflater;
    public SpotListAdatapter(Context mcontext, ArrayList<Spotdata> list_data){
        mlayoutinflater= LayoutInflater.from(mcontext);
        myUpload=new MyUpload(mcontext);
        this.list_data=list_data;
    }

    @Override
    public int getCount() {
        return list_data.size();
    }

    @Override
    public Object getItem(int position) {
        return list_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            L.i(TAG,"到了吗1");
            wuViewHolder=new MyViewHolder();
            convertView=mlayoutinflater.inflate(R.layout.listitem_spot,null);
            wuViewHolder.text_context= (TextView) convertView.findViewById(R.id.listitem_context);
            wuViewHolder.text_title= (TextView) convertView.findViewById(R.id.listitem_title);
            wuViewHolder.img= (ImageView) convertView.findViewById(R.id.listitem_img);
            convertView.setTag(wuViewHolder);
        }else {
            wuViewHolder= (MyViewHolder) convertView.getTag();
        }
        wuViewHolder.text_title.setText(list_data.get(position).getTitle());
        wuViewHolder.text_context.setText(list_data.get(position).getContext());
        L.i(TAG,"到了吗1");
        myUpload.download_asynchronous("lismarttourism","listimg/"+list_data.get(position).getObjectId(),wuViewHolder.img);
        return convertView;
    }
    private class MyViewHolder{
        private TextView text_title;
        private TextView text_context;
        private ImageView img;
    }

    public ArrayList<Spotdata> getList_data() {
        return list_data;
    }

    public void setList_data(ArrayList<Spotdata> list_data) {
        this.list_data = list_data;
    }
}
