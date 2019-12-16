package com.lenovo.smarttraffic.ui.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.entityclass.UserInfo;
import com.lenovo.smarttraffic.myview.MyRealerViewSidestep;
import com.lenovo.smarttraffic.myview.MyViewRealerEvent;
import com.lenovo.smarttraffic.sql.MyConnectSQL;
import com.lenovo.smarttraffic.ui.adapter.SidestepAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 设置用户中心页面
 *
 * @author asus
 */
public class UserAdminActivity extends BaseActivity {
    private static final String USING_URI = "/GetSUserInfo.do";
    private ArrayList<UserInfo> userDataList = new ArrayList<>();
    private SidestepAdapter userAdapter;
    public  static  SQLiteDatabase db;
    @BindView(R.id.rv_Sidestep)
    MyViewRealerEvent rSidestep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
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
    }



    private void getUsername() {
        service.execute(() -> {
            Log.d(TAG, "getUsername: ");
            userDataList.clear();
            db = MyConnectSQL.initMySQL(this,"userAdmin",null,1,2).getWritableDatabase();
              Cursor cursor = db.query("userData",null,null,null,null,null,null);
              if (cursor.moveToFirst()){
                  do {
                      UserInfo info = new UserInfo();
                      info.setUsername(cursor.getString(1));
                      info.setPname(cursor.getString(2));
                      info.setPtel(cursor.getString(3));
                      info.setDatetime(cursor.getString(4));
                      info.setAdmin(cursor.getString(5));
                      info.setImgUri(cursor.getInt(6));
                      info.setPsex(cursor.getString(7));
                      info.setState(cursor.getString(8));
                      userDataList.add(info);
                  }while (cursor.moveToNext());
              }
                InitApp.getHandler().post(()->{
                    userAdapter.notifyDataSetChanged();
                });
        });
    }

    @Override
    protected void onResume() {
        getUsername();
        super.onResume();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {
        MyRealerViewSidestep.sidestepArrayList.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        MyRealerViewSidestep.sidestepArrayList.clear();
        super.onDestroy();
    }

}
