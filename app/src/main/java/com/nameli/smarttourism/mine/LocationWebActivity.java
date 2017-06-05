package com.nameli.smarttourism.mine;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.nameli.smarttourism.R;

/**
 * Created by Administrator on 2017/6/5.
 */

public class LocationWebActivity extends Activity{
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
    }

    private void initView() {
        webView= (WebView) findViewById(R.id.webview);
        webView.loadUrl("http://map.baidu.com/");
    }
}
