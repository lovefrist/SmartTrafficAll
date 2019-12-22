package com.lenovo.smarttraffic.ui.activity;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.myview.GestureImageView;
import com.lenovo.smarttraffic.ui.adapter.MetroAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * 线路情况详情
 * 查询第几个线路页面
 *
 * @author asus
 */
public class MetroLineActivity extends BaseActivity {
    private static final String IMG_URI = "http://192.168.3.5:8088/transportservice";
    private static final String METRO_URI = "GetMetroInfo.do";
    @BindView(R.id.metro_left)
    RecyclerView rMetroLeft;
    @BindView(R.id.metro_right)
    RecyclerView rMetroRight;
    @BindView(R.id.metro_button)
    RecyclerView rMetroButton;
    @BindView(R.id.tv_distance)
    TextView tDistance;
    @BindView(R.id.tv_money)
    TextView tMoney;
    @BindView(R.id.iv_linr)
    ImageView iLin;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;

    private ArrayList<String> leftList = new ArrayList<>();
    private ArrayList<String> rightList = new ArrayList<>();
    private ArrayList<String> buttonList = new ArrayList<>();
    private MetroAdapter leftAdapter;
    private MetroAdapter rightAdapter;
    private MetroAdapter buttonAdapter;
    private String imgMap;
    private boolean swishKey = true;
    private Dialog dialog;
    private boolean switchData = true;
    private ViewGroup.LayoutParams iLinParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_line;
    }


    private void initView() {

        rMetroLeft.setLayoutManager(new LinearLayoutManager(this));
        rMetroRight.setLayoutManager(new LinearLayoutManager(this));
        rMetroButton.setLayoutManager(new GridLayoutManager(this, 3));
        dialog = new Dialog(this, R.style.FullActivity);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(attributes);
    }

    private void initData() {
        leftAdapter = new MetroAdapter(this, leftList);
        rightAdapter = new MetroAdapter(this, rightList);
        buttonAdapter = new MetroAdapter(this, buttonList);
        rMetroLeft.setAdapter(leftAdapter);
        rMetroRight.setAdapter(rightAdapter);
        rMetroButton.setAdapter(buttonAdapter);
        iLin.setOnClickListener(v -> {
            Drawable drawable = iLin.getDrawable();
            dialog.setContentView(getImageView(drawable));
                dialog.show();
        });

        getData();
        dialog.setOnDismissListener(dialogInterface -> {
//            ((ViewGroup) iLin.getParent()).removeView(iLin);
//            linearLayout.addView(iLin, iLinParams);
        });
        dialog.setOnShowListener(dialog1 -> {
//            iLinParams = iLin.getLayoutParams();
//            ((ViewGroup) iLin.getParent()).removeView(iLin);
//            dialog.setContentView(iLin);
        });
    }


    //动态的ImageView
    private GestureImageView getImageView(Drawable drawable1) {
        GestureImageView imageView = new GestureImageView(this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
//        //宽高
//        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
////        imageView设置图片
//        @SuppressLint("ResourceType") InputStream is = getResources().openRawResource(R.drawable.metro_001);
//        Drawable drawable = BitmapDrawable.createFromStream(is, null);
        imageView.setImageDrawable(drawable1);

        return imageView;
    }


    private void getData() {
        HashMap<String, Object> hashMap = new HashMap<>(2);
        hashMap.put("Line", getIntent().getIntExtra("number", 1));
        hashMap.put("UserName", "user1");
        service.execute(() -> {
            try {
                String metro = getJsonData(hashMap, METRO_URI);
                JSONObject allObject = new JSONObject(metro);
                JSONArray jsonArray = allObject.getJSONArray("ROWS_DETAIL");
                JSONObject metroObject = jsonArray.getJSONObject(0);
                JSONArray array = metroObject.getJSONArray("sites");
                imgMap = metroObject.getString("map");
                String name = metroObject.getString("name");
                for (int i = 0; i < array.length(); i++) {

                    if (((array.length()) & 1) == 0) {
                        if (i < (array.length() / 2)) {
                            leftList.add(array.optString(i));
                        } else {
                            rightList.add(array.optString(i));
                        }
                    } else {
                        if (i <= (array.length() / 2)) {
                            leftList.add(array.optString(i));
                        } else {
                            rightList.add(array.optString(i));
                        }
                    }

                }
                JSONArray arrayTime = metroObject.getJSONArray("time");

                for (int i = 0; i < arrayTime.length(); i++) {
                    JSONObject time = arrayTime.getJSONObject(i);
                    String site = time.getString("site");
                    String startTime = time.getString("starttime");
                    String endTime = time.getString("endtime");
                    buttonList.add(site);
                    buttonList.add(startTime);
                    buttonList.add(endTime);
                }
                int metroLength = array.length();

                InitApp.getHandler().post(() -> {
                    rightAdapter.notifyDataSetChanged();
                    leftAdapter.notifyDataSetChanged();
                    buttonAdapter.notifyDataSetChanged();
                    tDistance.setText(metroLength + "站/" + ((metroLength - 1) * 2) + "公里");
                    tMoney.setText("最高票价" + String.format("%.2f", ((metroLength - 1) * 2) * 0.2) + "元");
                    Glide.with(this).load(IMG_URI + imgMap).into(iLin);
                    initToolBar(findViewById(R.id.toolbar), true, name+"线路详情");

                });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
