package com.lenovo.smarttraffic.myinterface;

/**
 * 实现广播的接口回调实现有网和无网的事件处理
 * */
public interface BroadInter {
    /**
     * 没有网络的接口回调方法
     * */
    void netEvent();
    /**
     * 有网的接口回调方法
     * */
    void netFreeEvent();
}
