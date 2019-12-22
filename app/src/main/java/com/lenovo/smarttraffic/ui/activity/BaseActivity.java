package com.lenovo.smarttraffic.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.util.ToolbarUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public abstract class BaseActivity extends SupportActivity {
    public static String user = "user1";
    public static Handler handler = new Handler();
    public static ExecutorService service =
            new ThreadPoolExecutor(7, 20, 10, TimeUnit.MILLISECONDS,
                    //等待队列
                    new ArrayBlockingQueue<Runnable>(10),
                    //销毁策略 如果线程大于等待的和最大的相加,丢弃任务，执行销毁策略
                    new ThreadPoolExecutor.CallerRunsPolicy());
    private static String hendr;
    public String TAG = getClass().getSimpleName();
    private Unbinder unbind;

    public static String newGetJsonData(HashMap<String, Object> map, String uri) throws IOException {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        MediaType mediaType = MediaType.parse("application/json");
        String parms = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(mediaType, parms);
        Request request = new Request.Builder().post(requestBody).url( uri).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    public static String getJsonData(HashMap<String, Object> map, String uri) throws IOException {

        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        MediaType mediaType = MediaType.parse("application/json");
        String parms = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(mediaType, parms);
        Request request = new Request.Builder().post(requestBody).url(hendr + uri).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    private static void mainGetIppPort() {
        SharedPreferences preferences = InitApp.getInstance().getSharedPreferences("IpPort", Context.MODE_PRIVATE);
        String Ip = preferences.getString("IP", "192.168.3.5");
        String Port = preferences.getString("Port", "8088");

        hendr = "http://" + Ip + ":" + Port + "/transportservice/action/";
    }

    /**
     * 初始化 Toolbar
     */
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        ToolbarUtil.setTitleCenter(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        unbind = ButterKnife.bind(this);
        InitApp.getInstance().addActivity(this);
        mainGetIppPort();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressedSupport();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressedSupport() {
        //fragment逐个退出
        int count = getSupportFragmentManager().getBackStackEntryCount();
        //判断是不是最后一层
        if (count == 0) {
            super.onBackPressedSupport();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * 得到控件的布局
     *
     * @return 返回布局的int
     **/
    protected abstract int getLayout();

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart: ");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        InitApp.getInstance().removeActivity(this);
        unbind.unbind();
    }
}

