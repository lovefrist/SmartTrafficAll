package com.lenovo.smarttraffic.myinterface;

import java.util.Map;

/**
 * 实现在Adapter接口的回调确认是那个按键
 * @author asus
 */
public interface ClickItemlistener {
    /**
     *点击视图进行跳转的接口
     * @param  map 回调回去的数据
     * */
    void onClick(Map map);
}
