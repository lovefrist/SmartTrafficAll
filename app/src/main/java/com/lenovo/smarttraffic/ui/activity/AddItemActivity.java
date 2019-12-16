package com.lenovo.smarttraffic.ui.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.myinterface.OnItemClickListener;
import com.lenovo.smarttraffic.sql.MyConnectSQL;
import com.lenovo.smarttraffic.ui.adapter.RecyhelperAdapter;
import com.lenovo.smarttraffic.util.CommonUtil;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import butterknife.BindView;


/**
 * 实现添加内容的功能
 * 并且实现拖拽排序
 *
 * @author asus
 */
public class AddItemActivity extends BaseActivity {
    @BindView(R.id.rv_Subscribe)
    RecyclerView rvTop;
    @BindView(R.id.rv_contadd)
    RecyclerView rvBottom;
    @BindView(R.id.btn_ok)
    Button btnOK;
    private ArrayList<String> topData, bottomDate;
    private RecyhelperAdapter topAdapter, bottomAdapter;
    private  SQLiteDatabase db;
    private ArrayList<HashMap<String,Object>> sqrtDataList = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_additem;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(findViewById(R.id.toolbar), true, "添加资讯");
        db = MyConnectSQL.initMySQL(this, "ItemData", null, 4,1).getWritableDatabase();
        Cursor cursor = db.query("contentItem", null, null, null, null, null, null);
        bottomDate = new ArrayList<>();
        topData = new ArrayList<>();
        bottomDate = new ArrayList<>();
        if (!cursor.moveToFirst()){

            String[] data = CommonUtil.getStringArray(R.array.news_info);
            Log.d(TAG, "onCreate: "+data);
            for (int i =0;i<data.length;i++){
                ContentValues values = new ContentValues();
                if (i==0){
                    values.put("state",1);
                }else {
                    values.put("state",0);
                }
                values.put("itemcontent",data[i]);
                db.insert("contentItem",null,values);
            }
        }
        if (cursor.moveToFirst()) {

            do {

                int state =cursor.getInt(cursor.getColumnIndex("state"));

                if (state != 0){
                    HashMap<String,Object> map = new HashMap<>(2);
                    map.put("content",cursor.getString(3));
                    map.put("sqrt",cursor.getString(2));
                    sqrtDataList.add(map);
                }else {
                    bottomDate.add(cursor.getString(cursor.getColumnIndex("itemcontent")));
                }
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "onCreate: "+bottomDate.toString()+"\t"+sqrtDataList.toString());
        sqrtData();
        Log.d(TAG, "onCreate: "+topData.toString()+"\t"+sqrtDataList.toString());
        for (int i = 0; i <sqrtDataList.size() ; i++) {
            topData.add(sqrtDataList.get(i).get("content").toString());
        }
        rvTop.setLayoutManager(new GridLayoutManager(this, 5));
        topAdapter = new RecyhelperAdapter(this, topData, true);
        rvTop.setAdapter(topAdapter);

        bottomAdapter = new RecyhelperAdapter(this, bottomDate, false);
        rvBottom.setLayoutManager(new GridLayoutManager(this, 5));
        rvBottom.setAdapter(bottomAdapter);

        topAdapter.getAllonClick(new OnItemClickListener() {
            @Override
            public void longOnClick() {
                btnOK.setVisibility(View.VISIBLE);
                topAdapter.set(true);
                bottomAdapter.set(true);
            }

            @Override
            public void onClick(int position) {
                SystemClock.sleep(300);
                bottomDate.add(topData.remove(position));
                topAdapter.notifyDataSetChanged();
                bottomAdapter.notifyDataSetChanged();
            }
        });
        btnOK.setOnClickListener(v -> {
            Intent intent = new Intent();
            String[] dataTob = new String[topData.size()];
            String[] dataBottow = new String[bottomDate.size()];
            for (int i = 0; i < topData.size(); i++) {
                dataTob[i] = topData.get(i);
                ContentValues values = new ContentValues();
                values.put("state",1);
                values.put("sqrt",i);
                db.update("contentItem",values,"itemcontent = ?",new String[]{topData.get(i)});

            }

            for (int i=0;i<bottomDate.size();i++){
                dataBottow[i] = bottomDate.get(i);
                ContentValues values = new ContentValues();
                values.put("state",0);
                values.put("sqrt",20);
                db.update("contentItem",values,"itemcontent = ?",new String[]{bottomDate.get(i)});
            }
            intent.putExtra("contentall", dataTob);
            setResult(8, intent);
            finish();
        });

        bottomAdapter.getAllonClick(new OnItemClickListener() {
            @Override
            public void longOnClick() {

            }

            @Override
            public void onClick(int position) {

                topData.add(bottomDate.remove(position));
                topAdapter.notifyDataSetChanged();
                bottomAdapter.notifyDataSetChanged();
            }
        });

        initData();
    }

   private void sqrtData(){
     Collections.sort(sqrtDataList, new Comparator<HashMap<String, Object>>() {
         @Override
         public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
             Log.d(TAG, "compare: "+o1.get("sqrt").toString()+"\t"+o2.get("sqrt").toString());
             if (Integer.parseInt(o1.get("sqrt").toString())>Integer.parseInt(o2.get("sqrt").toString())){
                 return 1;
             }else {
                 return -1;
             }
         }
     });
    }

    private void initData() {
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            /**指定在此ViewHolder上允许哪些移动。*/
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                int swipeFlags = 0;//滑动方向
                return makeMovementFlags(dragFlags, swipeFlags);//返回由给定的拖动和滑动标志组成的整数
            }

            /**
             * 拖动执行的方法
             * 如果{@code viewHolder}已移至的适配器位置，则为true*
             * * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to.（）
             *  * @param viewHolder   The ViewHolder which is being dragged by the user.(当前移动的viewHolder)
             ** @param target       The ViewHolder over which the currently active item is being dragged.（当前活动项目被拖动到的ViewHolder。）
             * */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                if (viewHolder.getAdapterPosition() != 0) {
                    if (target.getAdapterPosition() != 0) {
                        if (viewHolder.getAdapterPosition() >= 0 && viewHolder.getAdapterPosition() < topData.size() && target.getAdapterPosition() >= 0 && target.getAdapterPosition() < topData.size()) {
                            Collections.swap(topData, viewHolder.getAdapterPosition(), target.getAdapterPosition());

                            topAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                            return false;
                        }
                    }
                }
                return true;
            }

            /**
             * 侧滑删除实现的效果
             * */
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d(TAG, "onSwiped: ");
                topData.remove(viewHolder.getAdapterPosition());
                topAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }

            /**滚动事件的开关*/
            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            /**滑动事件的开关*/
            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

//            @Override
//            public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
//                return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
//            }

            /**
             * 当 侧滑滑动的距离 / RecyclerView的宽大于该方法返回值，那么就会触发侧滑删除的操作。
             * 具体是：此时ItemView会做位移动画，当ItemView不可见时，会触发ItemTouchHelper的onSwiped方法，
             * 进而我们在onSwiped方法里面对Adapter进行remove操作。
             * */
//            @Override
//            public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
//                return super.getSwipeThreshold(viewHolder);
//            }
//
//            //            当侧滑的速度大于该方法的返回值，也会触发侧滑删除的操作。
//            @Override
//            public float getSwipeEscapeVelocity(float defaultValue) {
//                Log.d(TAG, "getSwipeEscapeVelocity: " + defaultValue);
//                return 1000;
//            }

//            @Override
//            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                Log.d(TAG, "onChildDraw: DX ="+dX+"\tDY="+dY+"\t actionState ="+actionState+"\t是不是在滑动"+isCurrentlyActive);
//                float mCurrentScrollX = 0;
//                boolean mFirstInactive = true;
//                int mCurrentScrollXWhenInactive = 0;
//                float mInitXWhenInactive = 0;
//                double mDefaultScrollX = 0;
//                if (dX == 0){
//                    mCurrentScrollX = viewHolder.itemView.getScrollX();
//                    mFirstInactive = true;
//                }
//                Log.d(TAG, "onChildDraw: ScrollX"+viewHolder.itemView.getScrollX());
//                if (isCurrentlyActive){ // 手指滑动
//                    //基于当前距离滑动
//                    if (dX>200){
//                        viewHolder.itemView.scrollTo(200,0);
//                        viewHolder.itemView.setScaleX(200);
//                    }else {
//                        viewHolder.itemView.scrollTo((int) Math.max((mCurrentScrollX+(-dX)),mCurrentScrollX),0);
//                    }
//
//                }
////                else
////                    {
////                    if (mFirstInactive) {
////                        mFirstInactive = false;
////                        mCurrentScrollXWhenInactive = viewHolder.itemView.getScrollX();
////                        mInitXWhenInactive = dX;
////                    }
////                    if (viewHolder.itemView.getScrollX() >= mDefaultScrollX) {
////                        // 当手指松开时，ItemView的滑动距离大于给定阈值，那么最终就停留在阈值，显示删除按钮。
////                        viewHolder.itemView.scrollTo((int) Math.max(mCurrentScrollX + (int) -dX, mDefaultScrollX), 0);
////                    } else {
////                        // 这里只能做距离的比例缩放，因为回到最初位置必须得从当前位置开始，dx不一定与ItemView的滑动距离相等
////                        viewHolder.itemView.scrollTo((int) (mCurrentScrollXWhenInactive * dX / mInitXWhenInactive), 0);
////                    }
////                }
//            }
        });
        //手动打开拖拽功能
//        helper.startDrag();
        helper.attachToRecyclerView(rvTop);
    }


}
