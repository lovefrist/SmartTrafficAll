package com.lenovo.smarttraffic.myinterface;

/**
 * @author  asus
 */
public interface OnItemClickListener {
    /**
     *接口长按点击事情回调
     * */
    void longOnClick();
    /**
     * 照片点击事情回调
     * @param position  ....
     * */
    void onClick(int position);
}
