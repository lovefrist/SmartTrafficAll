package com.lenovo.smarttraffic.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.ui.fragment.FirstFragment;
import com.lenovo.smarttraffic.ui.fragment.FragmentFactory;
import com.lenovo.smarttraffic.util.CommonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * ViewPager有多少个Fragment
 *
 * @author asus
 */
public class BasePagerAdapter extends FragmentStatePagerAdapter {

    private static SparseArray<Fragment> map = new SparseArray<>();
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private String[] tabArr;
    private FragmentManager fm;


    public BasePagerAdapter(FragmentManager fm) {
        super(fm);
        this.tabArr = CommonUtil.getStringArray(R.array.tab_names);
        this.fm = fm;
    }

    public BasePagerAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] titles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = new ArrayList<>(Arrays.asList(titles));
        this.fm = fm;
    }

    public BasePagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
        this.fm = fm;
    }

    public BasePagerAdapter(FragmentManager fm, ArrayList<String> titleList) {
        super(fm);
        this.titleList = titleList;
        this.fm = fm;
    }


    /**
     * ----------------返回是那个Fragment页面----------
     */
    @Override
    public Fragment getItem(int position) {
        if (map.get(position) != null) {
            return map.get(position);
        }
        map.put(position,new FirstFragment());
        return map.get(position);
    }

    /**
     * 创建多少个Froagment页面
     */
    @Override
    public int getCount() {
        if (titleList == null) {
            return tabArr.length;
        } else {
            return titleList.size();
        }
    }

    /**
     * 返回页面的title
     */
    @Override
    public CharSequence getPageTitle(int position) {
        if (titleList == null) {
            return tabArr[position];
        } else {
            return titleList.get(position);
        }
    }


    /**
     * 返回Item滑动的位置
     */
    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        //得到缓存的fragment
//        Fragment fragment= (Fragment) super.instantiateItem(container, position);
//        //获取到tag
//        String fragmentTag=fragment.getTag();
//        //如果这个fragment需要更新
//        if (true){
//            android.support.v4.app.FragmentTransaction ft=fm.beginTransaction();
//            //移除旧的fragment
//            ft.remove(fragment);
//            //换成新的fragment
//            fragment = new FirstFragment();
//            //添加新fragment时必须用前面获得的tag，这点很重要
//            ft.add(container.getId(), fragment, fragmentTag ==null? fragment.getClass().getName()+position : fragmentTag);
//            ft.attach(fragment);
//            ft.commit();
//            //复位更新标志
//        }else{
//            fragment = new FirstFragment();
//        }
//        return fragment;
//    }

    public void recreateItems(List<Fragment> fragmentList, List<String> titleList) {
        this.fragmentList = fragmentList;
        this.titleList = titleList;
        //更新视图刷新功能
        notifyDataSetChanged();
    }

    public void recreateItemsTitle(String[] titleList) {

        this.titleList = new ArrayList<>(Arrays.asList(titleList));
        map.clear();
        notifyDataSetChanged();
    }


}
