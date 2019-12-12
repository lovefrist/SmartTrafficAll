package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.sql.MyConnectSQL;
import com.lenovo.smarttraffic.ui.adapter.BasePagerAdapter;
import com.lenovo.smarttraffic.ui.adapter.ConsultationAdapter;
import com.lenovo.smarttraffic.ui.fragment.FirstFragment;
import com.lenovo.smarttraffic.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */

public class Item1Activity extends BaseActivity {
    @BindView(R.id.tab_layout_list)
    TabLayout tabLayoutList;
    @BindView(R.id.header_layout)
    LinearLayout headerLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    BasePagerAdapter basePagerAdapter;
    @BindView(R.id.ic_addItem)
    ImageView iAddItem;
    @BindView(R.id.swipe_Refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitView();
        InitData();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_list_tab;
    }

    private void InitView() {
        initToolBar(findViewById(R.id.toolbar), true, getString(R.string.item1));
        //和viewPager进行相管理
        tabLayoutList.setupWithViewPager(viewPager);
        //设置文字的布局在那个位置
        tabLayoutList.setTabMode(TabLayout.MODE_SCROLLABLE);
        //设置布局的布局颜色
        headerLayout.setBackgroundColor(CommonUtil.getInstance().getColor());
    }

    private void InitData() {
       ArrayList<String> titleList = new ArrayList<>();
        SQLiteDatabase db = MyConnectSQL.initMySQL(this, "ItemData", null, 4,1).getWritableDatabase();
       Cursor cursor = db.query("contentItem",null,"state = ?",new String[]{"1"},null,null,null);
        if (cursor.moveToFirst()){
            do {
              String itemcontent =  cursor.getString(cursor.getColumnIndex("itemcontent"));
              titleList.add(itemcontent);
            }while (cursor.moveToNext());
        }
        if (titleList.size() > 0) {
            basePagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), titleList);
        } else {
            basePagerAdapter = new BasePagerAdapter(getSupportFragmentManager());
        }

        viewPager.setAdapter(basePagerAdapter);
        //多少个页面在屏幕外显预加载
        viewPager.setOffscreenPageLimit(0);
        iAddItem.setOnClickListener(v -> startActivityForResult(new Intent(Item1Activity.this, AddItemActivity.class), 8));
        ConsultationAdapter.getClick(map -> {
            Intent intent = new Intent(Item1Activity.this, DetaTitleActivity.class);
            intent.putExtra("title", map.get("title").toString());
            String tblayoutTitle = basePagerAdapter.getPageTitle(viewPager.getCurrentItem()).toString();
            Log.d(TAG, "InitData: " + tblayoutTitle);
            intent.putExtra("date", map.get("createTime").toString());
            intent.putExtra("tblaycontentoutTitle", tblayoutTitle);
            intent.putExtra("content", map.get("content").toString());
            if (map.get("imgUri") != null) {
                intent.putExtra("imgUri", map.get("imgUri").toString());
            } else {
                intent.putExtra("imgUri", "");
            }
            Item1Activity.this.startActivityForResult(intent, 1);
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.cardview_shadow_start_color);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                service.execute(() -> {
                    SystemClock.sleep(3000);
                    InitApp.getHandler().post(() -> {
                        Log.d(TAG, "onRefresh: " + basePagerAdapter.getPageTitle(1));
//                        Fragment fragment = basePagerAdapter.getItem(viewPager.getCurrentItem());
//                        if (fragment instanceof  FirstFragment){
////                            FirstFragment firstFragment = (FirstFragment) fragment;
////                            firstFragment.chanrData();
//
//                        }
                        Fragment fragment = basePagerAdapter.getItem(viewPager.getCurrentItem());
                        if (fragment instanceof FirstFragment) {
                            FirstFragment firstFragment = (FirstFragment) fragment;
                            service.execute(() -> {
                                firstFragment.chanrData();
                            });
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    });
                });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8) {
            if (data != null) {
                String[] datall = data.getExtras().getStringArray("contentall");
                basePagerAdapter.recreateItemsTitle(datall);
                SQLiteDatabase database = MyConnectSQL.initMySQL(this, "ItemData", null, 4,1).getWritableDatabase();
               Cursor cursor = database.query("contentItem",null,null,null,null,null,null);
               if (cursor.moveToFirst()){
                   do {
                      String itemcontent = cursor.getString(cursor.getColumnIndex("itemcontent"));
                       String state = cursor.getString(cursor.getColumnIndex("state"));
                       Log.d(TAG, "onActivityResult: "+itemcontent+"\t"+state);
                   }while (cursor.moveToNext());
               }

            }
        }
    }

    /**
     * 活动准备好和用户进行交互的时候调用 这个时候活动位于返回栈的栈顶，并且处于运行状态
     */
    @Override
    protected void onResume() {
        super.onResume();
        headerLayout.setBackgroundColor(CommonUtil.getInstance().getColor());
    }

}
