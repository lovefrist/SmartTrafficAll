package com.lenovo.smarttraffic.myinterface;

import com.lenovo.smarttraffic.entityclass.ReachInfo;

import java.util.ArrayList;

/**
 * webView 的接口回调
 * @author asus
 */
public interface WebListener {
    /**
     * webView 的接口回调
     * @param number 标识符号完成什么任务
     * */
    void webQueryMoney(int number);
    /**
     * webView 的接口回调
     * @param number 标识符号完成什么任务
     * */
    void webReachMoney(int number);

    void recyclerData(ArrayList<ReachInfo> reachIfs);
}
