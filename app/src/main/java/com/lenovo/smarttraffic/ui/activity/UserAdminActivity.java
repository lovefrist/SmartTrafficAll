package com.lenovo.smarttraffic.ui.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.myinterface.AdapterOnClick;
import com.lenovo.smarttraffic.myview.MyRealerViewSidestep;
import com.lenovo.smarttraffic.myview.MyViewRealerEvent;
import com.lenovo.smarttraffic.sql.MyConnectSQL;
import com.lenovo.smarttraffic.ui.adapter.SidestepAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * 设置用户中心页面
 *
 * @author asus
 */
public class UserAdminActivity extends BaseActivity {
    private static final String USING_URI = "/GetSUserInfo.do";
    private ArrayList<HashMap<String,String>> userDataList = new ArrayList<>();
    private SidestepAdapter userAdapter;

    @BindView(R.id.rv_Sidestep)
    MyViewRealerEvent rSidestep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        getUsername();
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_user_admin;
    }

    private void initData() {
        initToolBar(findViewById(R.id.toolbar), true, "用户中心");
        rSidestep.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new  SidestepAdapter(this,userDataList);
        rSidestep.setAdapter(userAdapter);
        userAdapter.setOnClick(position -> {
            SQLiteDatabase db = MyConnectSQL.initMySQL(this,"userAdmin",null,1,3).getWritableDatabase();
            Cursor cursor = db.query("userCollection",null,null,null,null,null,null);
            if (cursor.moveToFirst()){
                do {
                    if (userDataList.get(position).get("username").equals(cursor.getString(cursor.getColumnIndex("UserName")))){
                        return;
                    }
                }while (cursor.moveToNext());
                addCollection(position,db);
            }else {
                addCollection(position,db);
            }
        });
    }

    /**
     * 向存储收藏用户的数据库存入数据
     * */
    private void addCollection(int position, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("UserName",userDataList.get(position).get("username"));
        values.put("AdminName",userDataList.get(position).get("pname"));
        values.put("Phone",userDataList.get(position).get("ptel"));
        values.put("Time",userDataList.get(position).get("datetime"));
        values.put("Admin",userDataList.get(position).get("Admin"));
        values.put("imgUri",userDataList.get(position).get("imgUri"));
        db.insert("userCollection",null,values);

    }


    private void getUsername() {
        service.execute(() -> {
                SQLiteDatabase db = MyConnectSQL.initMySQL(this,"userAdmin",null,1,2).getWritableDatabase();
              Cursor cursor = db.query("userData",null,null,null,null,null,null);
              if (cursor.moveToFirst()){
                  do {
                      HashMap<String,String> map = new HashMap<>(6);
                      String  username = cursor.getString(cursor.getColumnIndex("UserName"));
                      String  pname = cursor.getString(cursor.getColumnIndex("AdminName"));
                      String  Phone = cursor.getString(cursor.getColumnIndex("Phone"));
                      String  datetime = cursor.getString(cursor.getColumnIndex("Time"));
                      String  Admin = cursor.getString(cursor.getColumnIndex("Admin"));
                      String  imgUri = cursor.getString(cursor.getColumnIndex("imgUri"));
                      map.put("username",username);
                      map.put("pname",pname);
                      map.put("ptel",Phone);
                      map.put("datetime",datetime);
                      map.put("Admin",Admin);
                      map.put("imgUri",imgUri);
                      userDataList.add(map);
                  }while (cursor.moveToNext());
              }

                InitApp.getHandler().post(()->{
                    userAdapter.notifyDataSetChanged();
                });

        });
    }


    @Override
    protected void onDestroy() {
        MyRealerViewSidestep.sidestepArrayList.clear();
        super.onDestroy();
    }

}
