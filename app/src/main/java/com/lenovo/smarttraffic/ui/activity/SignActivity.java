package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.entityclass.ReachInfo;
import com.lenovo.smarttraffic.myinterface.WebListener;
import com.lenovo.smarttraffic.ui.adapter.EtcAdapter;
import com.lenovo.smarttraffic.util.WebUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;

/**
 * 用户签到中心页面
 * 使用webView
 *
 * @author asus
 */
public class SignActivity extends BaseActivity {
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rv_reach)
     RecyclerView rReach;
    Toolbar toolbar;
    private EtcAdapter etcAdapter;
    private ArrayList<ReachInfo> reachInfoList = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_sign;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }


    private void initView() {
        initToolBar(findViewById(R.id.toolbar), true, "用户签到");
        toolbar = findViewById(R.id.toolbar);
    }

    private void initData() {
        webView.getSettings().setJavaScriptEnabled(true);
        Intent intent = getIntent();
        int data = intent.getIntExtra("ETC",0);
        webView.loadUrl(data == 0?"file:///android_asset/index.html":"file:///android_asset/ETC.html");
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        webView.addJavascriptInterface(new WebUtil(), "java");
        rReach.setLayoutManager(new LinearLayoutManager(this));



        WebUtil.webSetMoney(new WebListener() {
            @Override
            public void webQueryMoney(int number) {
                runOnUiThread(() -> webView.loadUrl("javascript:setQueryMoney('"+number+"元')"));
            }

            @Override
            public void webReachMoney(int number) {

                runOnUiThread(() -> webView.loadUrl("javascript:setReachMoney('"+number+"元')"));

            }

            @Override
            public void recyclerData(ArrayList<ReachInfo> reachIfs) {
                runOnUiThread(()->{
                    rReach.setVisibility(View.VISIBLE);
                    etcAdapter = new EtcAdapter(SignActivity.this,reachIfs);
                    rReach.setAdapter(etcAdapter);
                });

            }
        });
    }

    @Override
    public void onBackPressedSupport() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressedSupport();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    class MyWebChromeClient extends WebChromeClient {
        /*获取页面的title**/
        @Override
        public void onReceivedTitle(WebView view, String title) {
            toolbar.setTitle(title);
            super.onReceivedTitle(view, title);
        }

        /*获取页面的进度*/
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            SystemClock.sleep(100);
            progressBar.setVisibility(newProgress == 100 ? View.GONE : View.VISIBLE);
            progressBar.setProgress(newProgress);
            super.onProgressChanged(view,newProgress);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.d(TAG, "onConsoleMessage: "+consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);
        }
    }

    class MyWebViewClient extends WebViewClient {
        /*页面开始的时候加载*/
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "onPageStarted: "+url);
            if ("file:///android_asset/index.html".equals(url)){
                service.execute(()->{
                    String DATA = "http://192.168.3.5:8088/transportservice/action/GetSUserInfo.do";
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("UserName","user1");
                    try {
                        String Data = newGetJsonData(map,DATA);
                        JSONObject jsonObject = new JSONObject(Data);
                        JSONArray userArray = jsonObject.getJSONArray("ROWS_DETAIL");
                        for (int i = 0; i <userArray.length() ; i++) {
                            JSONObject user = userArray.getJSONObject(i);
                            if (user.getString("username").equals(BaseActivity.user)){
                                String ptel = user.getString("ptel");
                                String pname = user.getString("pname");
                                runOnUiThread(()->{
                                    view.loadUrl("javascript:setInfo('"+pname+"','"+ptel+"')");
                                });
                                break;
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                });
            }


        }

        /**
         * 页面完成的时候加载
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        /*如果主机应用程序希望保留当前的WebView *并自行处理url，则为true，否则返回false。*/
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }
    }

}
