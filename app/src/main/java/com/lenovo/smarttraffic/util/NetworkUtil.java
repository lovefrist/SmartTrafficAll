package com.lenovo.smarttraffic.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lenovo.smarttraffic.InitApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络的工具类
 * @author asus
 */
public  class NetworkUtil {
    private String TAG = "封装对象MyokHttpClient";
    private static String Handy = "";
    private static NetworkUtil networkUtil;


    public static NetworkUtil getInstance(){
        if (networkUtil == null){
            networkUtil = new NetworkUtil();
        }
        return networkUtil;
    }
        /**
         *
         * 工具请求数据通过OkHttpClient
         * @param map 请求的JSON map 通过gson.toJson(map) 变化城JSON代码向服务器发送请求
         * @param uri 请求数据的地址
         * @return 返回向服务器请求数据得到的JSON代码
         * */
    public static String getJsonData(Map map, String uri) throws IOException {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        MediaType mediaType = MediaType.parse("application/json");
        String parms = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(mediaType, parms);
        Request request = new Request.Builder().post(requestBody).url(uri).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = okHttpClient.newCall(request).execute();
       String data= response.body().string();
        return data;
    }

    public static String newGetJsonData(Map map, String uri) throws IOException {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        MediaType mediaType = MediaType.parse("application/json");
        String parms = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(mediaType, parms);
        Request request = new Request.Builder().post(requestBody).url(Handy +uri).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    private void magnetInPort() {
        SharedPreferences preferences = InitApp.getInstance().getSharedPreferences("IpPort",0);
       String Ip = preferences.getString("IP","193.168.3.5");
       String Port = preferences.getString("Port","8033");
       Handy = "http://"+Ip+":"+Port+"transportservice/action";
    }

    public  List getIpPort() {
        ArrayList<String> list = new ArrayList<>();
        SharedPreferences preferences = InitApp.getInstance().getSharedPreferences("IpPort", 0);
        list.add(preferences.getString("IP", "192.168.3.5"));
        list.add(preferences.getString("Port", "8088"));
        return list;
    }

}
