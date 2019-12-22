package com.lenovo.smarttraffic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lenovo.smarttraffic.ui.activity.BaseActivity;
import com.lenovo.smarttraffic.ui.activity.ForecastActivity;
import com.lenovo.smarttraffic.ui.activity.Item1Activity;
import com.lenovo.smarttraffic.ui.activity.LoginActivity;
import com.lenovo.smarttraffic.ui.activity.SecedeActivity;
import com.lenovo.smarttraffic.ui.activity.UserAdminActivity;
import com.lenovo.smarttraffic.ui.fragment.DesignFragment;
import com.lenovo.smarttraffic.ui.fragment.MainContentFragment;
import com.lenovo.smarttraffic.util.ToolbarUtil;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    /*** 调转到登陆页面的标识符*/
    private final static int INTENT_LOG = 1;
    /*** 调转到item1页面的标识符*/
    private final static int INTENT_LTEM1 = 2;
    /**调转到item2页面的标识符*/
    private final static int INTENT_LTEM2 = 3;
    /*** 跳转到退出页面的标识符*/
    private final static int INTENT_LOGOUT = 4;
    /**跳转到个人页面的标识符*/
    private final static int INTENT_USER = 4;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private MainContentFragment mMainContent;
    private DesignFragment mDesignFragment;
    private static final String POSITION = "position";
    private static final String SELECT_ITEM = "bottomItem";
    private static final int FRAGMENT_MAIN = 0;
    private static final int FRAGMENT_DESIGN = 1;
    private BottomNavigationView bottomNavigation;
    private TextView textView;
    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
//        if (savedInstanceState != null) {
//            //使用fragmentation加载根组件
//            loadMultipleRootFragment(R.id.container, 0, mMainContent, mDesignFragment);
//            // 恢复 recreate 前的位置
//            showFragment(savedInstanceState.getInt(POSITION));
//            bottomNavigation.setSelectedItemId(savedInstanceState.getInt(SELECT_ITEM));
//        } else {
//
//        }
        showFragment(FRAGMENT_MAIN);
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        CircleImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.ivAvatar);
        textView = navigationView.getHeaderView(0).findViewById(R.id.textViewuser);
        setSupportActionBar(mToolbar);
        ToolbarUtil.setTitleCenter(mToolbar);
        imageView.setOnClickListener(this);
        /*设置选择item监听*/
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initData() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        mDrawer.addDrawerListener(toggle);
        if (BaseActivity.user!= null) {
            if ("user1".equals(BaseActivity.user)){
                textView.setText("王安生");
            }
        }
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_main:
                    showFragment(FRAGMENT_MAIN);
                    break;
                case R.id.action_creative:
                    showFragment(FRAGMENT_DESIGN);
                    break;
                default:
                    break;
            }
            return true;
        });

    }

    /**
     * 显示Fragment
     */
    private void showFragment(int index) {

//        position = index;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragment(ft);
        switch (index) {
            case FRAGMENT_MAIN:
                mToolbar.setTitle(R.string.title_main);
                if (mMainContent == null) {
                    Log.e(TAG, "showFragment: 添加mMainContent");
                    mMainContent = MainContentFragment.getInstance();
                    ft.add(R.id.container, mMainContent, MainContentFragment.class.getName());

                } else {
                    ft.show(mMainContent);
                }
                break;
            case FRAGMENT_DESIGN:
                mToolbar.setTitle(R.string.creative_design);
                if (mDesignFragment == null) {
                    Log.e(TAG, "showFragment: 添加mDesignFragment");
                    mDesignFragment = DesignFragment.getInstance();
                    ft.add(R.id.container, mDesignFragment, DesignFragment.class.getName());
                } else {
                    ft.show(mDesignFragment);
                }
                break;
            default:
                break;
        }
        ft.commit();

    }

    /**
     * 隐藏Fragment
     */
    private void hideFragment(FragmentTransaction ft) {
        // 如果不为空，就先隐藏起来
        //要全部都隐藏才能show
        if (mMainContent != null) {
            ft.hide(mMainContent);
        }
        if (mDesignFragment != null) {
            ft.hide(mDesignFragment);
        }
    }

    @Override
    public void onBackPressedSupport() {
        /*打开或关闭左边的菜单*/
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            showExitDialog();
        }
    }

    /**
     * 是否退出项目
     */
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定退出吗");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", (dialogInterface, i) -> InitApp.getInstance().exitApp());
        builder.show();
    }

    /**
     * serResult（）执行后会执行的方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_LOG:
                if (data != null) {
                    textView.setText(data.getExtras().getString("User"));
                }
                break;
            case INTENT_LTEM1:

                break;
            case INTENT_LOGOUT:
                textView.setText("点击头像登陆");
                break;
            default:
        }
    }

    /**
     * 侧滑栏点击事情
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        /*设置选中item事件*/
        int id = item.getItemId();
        String string = null;
        switch (id) {
            case R.id.nav_account:
                startActivityForResult(new Intent(this, UserAdminActivity.class), INTENT_USER);
                break;
            case R.id.item_1:
                startActivityForResult(new Intent(this, Item1Activity.class), INTENT_LTEM1);
                break;
            case R.id.item_2:
                startActivityForResult(new Intent(this, ForecastActivity.class), INTENT_LTEM1);
                break;
            case R.id.item_3:
                string = "item3";
                break;
            case R.id.nav_setting:
                Intent intent = new Intent(this, SecedeActivity.class);
                startActivityForResult(intent, INTENT_LOGOUT);
                break;
            case R.id.nav_about:
                string = "关于";
                break;
            default:
                break;
        }

        //关闭动画
        mDrawer.closeDrawers();
        return true;
    }

    @Override
    public void onClick(View view) {
        //点击头像跳转登录界面
        if (view.getId() == R.id.ivAvatar) {
            startActivityForResult(new Intent(this, LoginActivity.class), INTENT_LOG);
        }
        mDrawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onStart() {

        super.onStart();
    }
}
