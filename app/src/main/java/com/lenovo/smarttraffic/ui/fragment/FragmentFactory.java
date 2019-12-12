package com.lenovo.smarttraffic.ui.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;

/**
 * Fragment工厂类
 */
public class FragmentFactory {
    /**
     * 根据position生产不同的fragment
     *
     * @param position
     * @return
     */
    private static FragmentFactory mFactory = null;
    private static SparseArray<Fragment> map = new SparseArray<>();
    public static FragmentFactory getInstance() {
        if (mFactory == null) {
            mFactory = new FragmentFactory();
        }
        return mFactory;
    }

    public Fragment getFragment(int position) {
        Fragment fragment = null;
        if (map.get(position) != null) {
            return map.get(position);
        }

        switch (position) {
            case 1:
                fragment = new ForFragment();
                break;
            case 0:
            default:
                fragment = new FirstFragment();
                break;
        }
        map.put(position, fragment);
        return fragment;
    }
}