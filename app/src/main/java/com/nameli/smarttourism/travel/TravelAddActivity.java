package com.nameli.smarttourism.travel;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.nameli.smarttourism.R;
import com.nameli.smarttourism.Utils.MySdcard;
import com.nameli.smarttourism.Utils.MyUpload;
import com.nameli.smarttourism.Utils.T;
import com.nameli.smarttourism.customView.XuProcessDialog;
import com.nameli.smarttourism.onlinedata.Alldata;
import com.nameli.smarttourism.onlinedata.Traveldata;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/4/2.
 */

public class TravelAddActivity extends Activity implements View.OnClickListener{
    private String TAG="TravelAddActivity";
    private EditText edit_title;
    private EditText edit_context;
    private ImageView img_add;
    private Button button_ok;
    private String imgstr;
    private String tempUri;
    private MySdcard mysdcard=new MySdcard();
    private XuProcessDialog xudiglog;
    private Context mcontext;
    private MyUpload myUpload;
    private Alldata alldata;
    private TextView text_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);
        mcontext=this;
        myUpload=new MyUpload(mcontext);
        recivewdata();
        initView();
        initSdCard();
    }

    private void recivewdata() {
        Intent it=getIntent();
        Bundle bundle=it.getExtras();
        alldata= (Alldata) bundle.getSerializable("alldata");
    }

    private void initSdCard() {
        File imgfilefinal=new File(mysdcard.pathwriteimg+ File.separator+"img1"+".jpg");
        if(imgfilefinal.exists())
            imgfilefinal.delete();
    }

    private void initView() {
        edit_title= (EditText) findViewById(R.id.travel_add_titel);
        edit_context= (EditText) findViewById(R.id.travel_add_context);
        button_ok= (Button) findViewById(R.id.travel_add_ok);
        img_add= (ImageView) findViewById(R.id.travel_add_img);
        text_back= (TextView) findViewById(R.id.travel_add_back);
        button_ok.setOnClickListener(this);
        img_add.setOnClickListener(this);
        text_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.travel_add_ok:gook();break;
            case R.id.travel_add_img:openpicture();break;
            case R.id.travel_add_back:TravelAddActivity.this.finish();break;
        }
    }

    private void gook() {
        xudiglog=new XuProcessDialog(mcontext);
        xudiglog.show();
        Traveldata traveldata=new Traveldata(edit_title.getText().toString(),edit_context.getText().toString(),true,new ArrayList<String>());
        traveldata.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    myPicUpload(s.toString());
                    ArrayList<String> list;
                    list=alldata.getList_travel();
                    list.add(s);
                    alldata.setList_travel(list);
                    alldata.update(alldata.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                xudiglog.dismiss();
                                T.showShot(mcontext, "上传游记成功！");
                            } else {
                                xudiglog.dismiss();
                                T.showShot(mcontext, "上传游记失败！");
                            }
                        }
                    });
                }else {
                    xudiglog.dismiss();
                    T.showShot(mcontext,"上传游记失败！");
                }
            }
        });
    }

    private void myPicUpload(String s) {
        int howpic_i=1;
        switch (howpic_i){
            case 1:myUpload.goUpload("lismarttourism","listimg/"+s,mysdcard.pathwriteimg+ File.separator+"img1.jpg");break;
            case 2:myUpload.goUpload(" lismarttourism","listimg/"+s,mysdcard.pathwriteimg+ File.separator+"img1.jpg");
                myUpload.goUpload(" lismarttourism","listimg/"+s,mysdcard.pathwriteimg+ File.separator+"img2.jpg");break;
            case 3:myUpload.goUpload(" lismarttourism","listimg/"+s,mysdcard.pathwriteimg+ File.separator+"img1.jpg");
                myUpload.goUpload(" lismarttourism","listimg/"+s,mysdcard.pathwriteimg+ File.separator+"img2.jpg");
                myUpload.goUpload(" lismarttourism","listimg/"+s,mysdcard.pathwriteimg+ File.separator+"img3.jpg");;break;
            case 0:;break;
        }
    }
    private void openpicture() {
        imgstr="img1.jpg";
        Intent openAlbumIntent = new Intent(
                Intent.ACTION_PICK);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Wu",TAG+requestCode+"hahahah"+resultCode);
        if(resultCode!=0) {
            switch (requestCode) {
                case 1:
                    if (data.getData() != null) {
                        startPhotoZoom(data.getData());
                    }// 开始对图片进行裁剪处理
                    break;
                case 2:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
                case 3:
                    String resultname = data.getStringExtra("result");
                    if (resultname != null)
                        break;
                case 4:
                    String resultsign = data.getStringExtra("result");
                    if (resultsign != null)
                        //  text_sign.setText(resultsign);
                        break;
            }
        }
        switch (resultCode) {
            case 0:break;
            case 1:
                String sex=data.getStringExtra("sex");
                //  text_sex.setText(sex);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */

    protected void startPhotoZoom(Uri uri) {
        File cutFile;
        if (uri == null) {
            Log.i("Wu", "The uri is not exist.");
        }
        tempUri = getImageAbsolutePath(this,uri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 360);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        cutFile = new File(mysdcard.pathwriteimg+ File.separator+imgstr);
        Log.i("Wu",TAG+mysdcard.pathwriteimg+ File.separator+imgstr);
        if (cutFile.exists() == false) {
            try {
                cutFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // 解决方法
        intent.putExtra("output", Uri.fromFile(cutFile));  // 指定目标文件
        intent.putExtra("outputFormat", "JPEG"); //输出文件格式

        startActivityForResult(intent, 2);
    }
    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     *
     * @param
     */
    protected void setImageToView(Intent data) {
        //  Bundle extras = data.getExtras();
        // if (extras != null) {
        //      Bitmap photo = extras.getParcelable("data");
        Bitmap photo=convertToBitmap(mysdcard.pathwriteimg+ File.separator+ imgstr,500,360);
        //   Bitmap bip=Bit
        //    photo = Utils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
        //    img_head.setImageBitmap(photo);
        img_add.setImageBitmap(photo);
        //uploadPic(photo);
        //  }
    }
    public Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int)scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }
    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     * @param
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    private void setOnHead() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            );
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
}
