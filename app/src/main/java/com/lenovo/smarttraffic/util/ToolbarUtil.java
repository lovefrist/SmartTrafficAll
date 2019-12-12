package com.lenovo.smarttraffic.util;

import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * @author asus
 */
public class ToolbarUtil {
    public static void setTitleCenter(Toolbar toolbar) {
        /*得到标题 方便二次setTitle 直接使用*/
        final CharSequence title = toolbar.getTitle();
        /*getChildCount() 是得到当前控件的子控件个数*/
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            /*得到子布局*/
            View view = toolbar.getChildAt(i);
            /*instanceof 是测试 左边是否是右边的实例*/
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (title.equals(textView.getText())) {
                    textView.setGravity(Gravity.CENTER);
                    /*LayoutParams 作用动态控制子view的摆放位置*/
                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.CENTER;
                    textView.setLayoutParams(params);
                }
            }
        }
        toolbar.setTitle(title);
    }
}
