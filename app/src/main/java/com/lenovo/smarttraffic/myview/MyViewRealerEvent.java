package com.lenovo.smarttraffic.myview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author asus
 */
public class MyViewRealerEvent extends RecyclerView {
    public MyViewRealerEvent(Context context) {
        super(context);
    }

    public MyViewRealerEvent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewRealerEvent(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (e.getPointerCount()>=2){
            return true;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }
}
