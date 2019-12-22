package com.lenovo.smarttraffic.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.entityclass.ReachInfo;
import com.lenovo.smarttraffic.myinterface.WebListener;
import com.lenovo.smarttraffic.sql.MyConnectSql;
import com.lenovo.smarttraffic.ui.activity.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 * html5 js代码回调
 * Toast弹出框
 *
 * @author asus
 */
public class WebUtil extends Object {
    private static final String ACCOUNT_URI = "http://192.168.3.5:8088/transportservice/action/GetCarAccountBalance.do";
    private static final String RECHARGE_URI = "http://192.168.3.5:8088/transportservice/action/SetCarAccountRecharge.do";
    private static boolean deceiving = true;
    private static WebListener myWeb;

    public static void webSetMoney(WebListener webListener) {
        myWeb = webListener;
    }

    @JavascriptInterface
    public void ToastData() {
        if (deceiving) {
            Toast.makeText(InitApp.getInstance().getBaseContext(), "签到有彩蛋，积分+100", Toast.LENGTH_LONG).show();
            deceiving = false;
        } else {
            Toast.makeText(InitApp.getInstance().getBaseContext(), "您已领取", Toast.LENGTH_LONG).show();
        }
    }

    @JavascriptInterface
    public void queryMoney(String carId) {
        HashMap<String, String> map = new HashMap<>(2);
        map.put("CarId", carId);
        map.put("UserName", "user1");
        try {
            String data = NetworkUtil.getJsonData(map, ACCOUNT_URI);
            JSONObject jsonObject = new JSONObject(data);
            int Balance = jsonObject.getInt("Balance");

            myWeb.webQueryMoney(Balance);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void Recharge(String carId, String reachMoney) {
        HashMap<String, String> map = new HashMap<>(3);
//        {"CarId":2,"Money":100, "UserName":"user1"}
        map.put("CarId", carId);
        map.put("Money", reachMoney);
        map.put("UserName", "user1");

        try {
            String data = NetworkUtil.getJsonData(map, RECHARGE_URI);
            MyConnectSql connectSql = MyConnectSql.initMySQL(InitApp.getInstance().getBaseContext(), "Reach", null, 1, 3);
            SQLiteDatabase database = connectSql.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("CarNumber",carId);
            values.put("Money",reachMoney);
            values.put("User", BaseActivity.user);
            Date  date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd hh:ss");
            String time =format.format(date);
            values.put("ReachTime",time);
            database.insert("car",null,values);
            Log.d("", "Recharge: 存入数据库成功");
            map.clear();
            map.put("CarId", carId);
            map.put("UserName", "user1");
            try {
                String queryData = NetworkUtil.getJsonData(map, ACCOUNT_URI);
                JSONObject jsonObject = new JSONObject(queryData);
                int Balance = jsonObject.getInt("Balance");
                myWeb.webReachMoney(Balance);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void sqrtReach(String catId, String sqrt) {

        MyConnectSql connectSql = MyConnectSql.initMySQL(InitApp.getInstance().getBaseContext(), "Reach", null, 1, 3);
        SQLiteDatabase database = connectSql.getWritableDatabase();
        //desc降序默认升序
        boolean desc = "0".equals(sqrt);

        Cursor cursor = database.query("car",null,null,null,null,null,"ReachTime"+(desc?" desc":"")+"");
        Log.d("TAG", "sqrtReach: "+cursor.getCount());
        ArrayList<ReachInfo> arrayList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                if (cursor.getString(1).equals(catId)){
                    ReachInfo reachInfo = new ReachInfo();
                    reachInfo.setCarNumber(cursor.getString(1));
                    reachInfo.setMoney(cursor.getString(2));
                    reachInfo.setUser(cursor.getString(3));
                    reachInfo.setReachTime(cursor.getString(4));
                    arrayList.add(reachInfo);
                }
            }while (cursor.moveToNext());
        }

        myWeb.recyclerData(arrayList);
    }
}
