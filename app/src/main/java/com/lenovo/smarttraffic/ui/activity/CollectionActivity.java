package com.lenovo.smarttraffic.ui.activity;

import android.database.Cursor;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.entityclass.UserInfo;
import com.lenovo.smarttraffic.myinterface.UserListener;
import com.lenovo.smarttraffic.myview.MyRealerViewSidestep;
import com.lenovo.smarttraffic.ui.adapter.CollectionAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 2019国赛第五题
 * 用户收藏页面
 *
 * @author asus
 */
public class CollectionActivity extends BaseActivity {
    @BindView(R.id.rv_userCollection)
    RecyclerView rCollection;
    private ArrayList<UserInfo> collectionMap = new ArrayList<>();
    private CollectionAdapter collectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        getData();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_collection;
    }

    private void initData() {
        initToolBar(findViewById(R.id.toolbar),true,"用户收藏");
        rCollection.setLayoutManager(new LinearLayoutManager(this));
        collectionAdapter = new CollectionAdapter(this, collectionMap);
        rCollection.setAdapter(collectionAdapter);

    }

    private void getData() {
        service.execute(() -> {
            Cursor cursor = UserAdminActivity.db.query("userData", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getString(8).equals("1")){
                    UserInfo info = new UserInfo();
                    info.setUsername(cursor.getString(1));
                    info.setPname(cursor.getString(2));
                    info.setPtel(cursor.getString(3));
                    info.setDatetime(cursor.getString(4));
                    info.setAdmin(cursor.getString(5));
                    info.setImgUri(cursor.getInt(6));
                    info.setState(cursor.getString(8));
                    info.setStateTop(0);
                    collectionMap.add(info);
                    }
                } while (cursor.moveToNext());
            }
            InitApp.getHandler().post(() -> {
                if (collectionMap.size() == 0) {
                    TextView textView = new TextView(this);
                    textView.setText("暂无收藏用户");
                    textView.setGravity(Gravity.CENTER);
                    DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                    ViewGroup.LayoutParams layoutParams = drawerLayout.getLayoutParams();
                    drawerLayout.addView(textView, 1, layoutParams);
                }
               collectionAdapter.notifyDataSetChanged();
            });

        });
    }

    @Override
    protected void onDestroy() {

        MyRealerViewSidestep.sidestepArrayList.clear();
        super.onDestroy();
    }


}
