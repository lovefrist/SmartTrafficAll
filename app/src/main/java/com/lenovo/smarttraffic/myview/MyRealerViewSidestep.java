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
 * @author asus
 */
public class MyRealerViewSidestep extends HorizontalScrollView {
    private int leftLayoutWidth;
    private int rightLayoutWidth;
    private float startDx;
    private float endDx;
    private boolean switchItem= false;
    private static final String TAG = "MyRealerViewSidestep";

    public MyRealerViewSidestep(Context context) {
        super(context);
    }

    public MyRealerViewSidestep(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRealerViewSidestep(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static ArrayList<MyRealerViewSidestep> sidestepArrayList = new ArrayList<>();

    @Override
    protected void onDraw(Canvas canvas) {
        //将itemViewHonder放入静态集合里面
        int indexclass = sidestepArrayList.indexOf(this);
        if (indexclass == -1) {
            sidestepArrayList.add(this);
        }
        //得到手机屏幕的宽带
        leftLayoutWidth = canvas.getWidth();
        ViewGroup allLayout = (ViewGroup) getChildAt(0);
        ViewGroup leftLayout = (ViewGroup) allLayout.getChildAt(0);
        ViewGroup rightLayout = (ViewGroup)allLayout.getChildAt(1);
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


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startDx = ev.getX();
                /**设置点击时只有点击的item才不还原*/
                for (int i = 0; i < sidestepArrayList.size(); i++) {
                    if (sidestepArrayList.get(i) != this) {
                        sidestepArrayList.get(i).smoothScrollTo(0, 0);
                    }
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                endDx = ev.getX();
                int SD = (int) -(endDx - startDx);
                //如果手指的移动距离大于右边布局宽带的三分之二松开手指时会自动滑倒最大距离否则反正

                if (SD>(rightLayoutWidth/1.5)){
                    smoothScrollTo(rightLayoutWidth,0);
                    switchItem = true;
                    /**防止点击一时另外一个还没有离开就滑动
                     * 导致最后结果有两个ViewHonder滑动出来
                     * 只能让UP手指最后一个离开的滑动出来
                     * */
                    for (int i = 0; i < sidestepArrayList.size(); i++) {
                        if (sidestepArrayList.get(i) != this) {
                            sidestepArrayList.get(i).smoothScrollTo(0, 0);
                        }
                    }
                }else {
                    if (getScaleX()==0){
                        switchItem =false;
                    }
                    if (!switchItem){
                        smoothScrollTo(0,0);
                    }

                }
                return true;
            default:
                return super.onTouchEvent(ev);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
