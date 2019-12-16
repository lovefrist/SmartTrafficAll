package com.lenovo.smarttraffic.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;

/**
 * 仿qq侧滑删除
 *
 * @author asus
 */
public class MyRealerViewSidesteptow extends HorizontalScrollView {
    private int leftLayoutWidth;
    private int rightLayoutWidth;
    private float startDx;
    private float endDx;
    private static final String TAG = "MyRealerViewSidestep";
    /**
     * 设置全局变量存储MyRealerViewSidestep对象方便引用对象
     */
    public static MyRealerViewSidesteptow smoothScrollTo;

    public MyRealerViewSidesteptow(Context context) {

        super(context);

    }

    public MyRealerViewSidesteptow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRealerViewSidesteptow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //将itemViewHonder放入静态集合里面

        //得到手机屏幕的宽带
        leftLayoutWidth = canvas.getWidth();
        ViewGroup allLayout = (ViewGroup) getChildAt(0);
        ViewGroup leftLayout = (ViewGroup) allLayout.getChildAt(0);
        ViewGroup rightLayout = (ViewGroup) allLayout.getChildAt(1);
        //得到左边的该视图关联的布局参数
        ViewGroup.LayoutParams leftLayoutParams = leftLayout.getLayoutParams();
        //设置左边的该视图关联的宽
        leftLayoutParams.width = canvas.getWidth();
        //设置左边的该视图关联的布局参数
        leftLayout.setLayoutParams(leftLayoutParams);
        //得到右边的视图宽度
        rightLayoutWidth = rightLayout.getWidth();
        super.onDraw(canvas);
    }

    public void closeDown() {
        smoothScrollTo(0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startDx = ev.getX();
                /**设置点击时只有点击的item才不还原*/
                    if (smoothScrollTo == null){
                        smoothScrollTo =this;
                    }
                if (smoothScrollTo !=this){
                    smoothScrollTo.smoothScrollTo(0,0);
                    smoothScrollTo = this;
                }

                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                endDx = ev.getX();
                int SD = (int) -(endDx - startDx);
                //如果手指的移动距离大于右边布局宽带的三分之二松开手指时会自动滑倒最大距离否则反正

                if (SD > (rightLayoutWidth / 2)) {
                    smoothScrollTo(rightLayoutWidth, 0);

                    /**防止点击一时另外一个还没有离开就滑动
                     * 导致最后结果有两个ViewHonder滑动出来
                     * 只能让UP手指最后一个离开的滑动出来
                     * */

                } else {
                    smoothScrollTo(0, 0);

                }
                return true;
            default:
                return super.onTouchEvent(ev);
        }

    }

    @Override
    protected void onDetachedFromWindow() {

        super.onDetachedFromWindow();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
