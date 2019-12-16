package com.lenovo.smarttraffic.ui.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.ui.adapter.ConsultationAdapter;
import com.lenovo.smarttraffic.util.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;


/**
 * 2018国赛第四天
 * 编写viewPager推荐页面窗口
 *
 * @author asus
 * @time 2019/12/9
 */
public class FirstFragment extends BaseFragment {
    /***高速公路公告咨询的连接地址*/
    private final static String MOTORWAY_URI = "http://192.168.3.5:8088/transportservice/action/GetMotorwayAnnouncement.do";
    /**
     * 故宫的连接地址
     */
    private final static String SPOT_URI = "http://192.168.3.5:8088/transportservice/action/GetSpotInfo.do";
    /**
     * 新闻信息的链接地址
     */
    private final static String NEWSINFO_URI = "http://192.168.3.5:8088/transportservice/action/GetNewsInfo.do";
    private RecyclerView rConsultation;
    private ConsultationAdapter consultationAdapter;
    private ArrayList<HashMap<String, String>> maplists = new ArrayList<>();
    private int viewpagerRefresh = 0;

    @Override
    protected View getSuccessView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.recommend_layout, null);
        initView(view);
        setContent();
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    protected Object requestData() {
        String jsonData = null;
        maplists.clear();
        try {
            if (viewpagerRefresh != 0) {
                for (int j = 0; j < viewpagerRefresh; j++) {
                    for (int i = 0; i < 2; i++) {
                        HashMap<String, String> map = new HashMap(4);
                        map.put("title", "1");
                        map.put("content", "2");
                        map.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        map.put("imgUri", null);
                        maplists.add(map);
                        Log.d(TAG, "requestData1:->>>> " + maplists.size());
                    }
                }
            }

            /**获取新闻内容*/
            HashMap poastm = new HashMap(1);
            poastm.put("UserName", "user1");
            String wotodata = NetworkUtil.getJsonData(poastm, MOTORWAY_URI);
            jsonData = wotodata;
            JSONObject waterwayData = new JSONObject(wotodata);
            JSONArray jsonArray = waterwayData.getJSONArray("ROWS_DETAIL");

            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, String> map = new HashMap(4);
                JSONObject objectContent = jsonArray.getJSONObject(i);
                String title = objectContent.getString("Title");
                String content = objectContent.getString("Content");
                String createTime = objectContent.getString("CreateTime");
                map.put("title", title);
                map.put("content", content);
                map.put("createTime", createTime);
                map.put("imgUri", null);
                maplists.add(map);
            }

            /**获取旅游信息*/
            String spotdata = NetworkUtil.getJsonData(poastm, SPOT_URI);
            jsonData = spotdata;
            JSONObject objectSpot = new JSONObject(spotdata);
            JSONArray spotArray = objectSpot.getJSONArray("ROWS_DETAIL");
            for (int i = 0; i < spotArray.length(); i++) {
                HashMap<String, String> map = new HashMap<>(4);
                JSONObject spotContent = spotArray.getJSONObject(i);
                String title = spotContent.getString("name");
                String imgUri = spotContent.getString("img");
                String content = spotContent.getString("info");
                map.put("title", title);
                map.put("imgUri", imgUri);
                map.put("content", content);
                map.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                maplists.add(map);
            }

            /**获取新闻的信息*/
            String newinfo = NetworkUtil.getJsonData(poastm, NEWSINFO_URI);
            jsonData = newinfo;
            Log.d(TAG, "requestData: " + newinfo);
            JSONObject newsObject = new JSONObject(newinfo);
            JSONArray newsinfoArray = newsObject.getJSONArray("ROWS_DETAIL");
            for (int i = 0; i < newsinfoArray.length(); i++) {
                HashMap<String, String> map = new HashMap<>(4);
                JSONObject jsonObject = newsinfoArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String content = jsonObject.getString("content");
                String createTime = jsonObject.getString("createtime");
                map.put("title", title);
                map.put("content", content);
                map.put("createTime", createTime);
                map.put("imgUri", null);
                maplists.add(map);
            }
            sortData();
            InitApp.getHandler().post(() -> {
                consultationAdapter.notifyDataSetChanged();
            });
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "requestData: 网络出现错误");
            InitApp.getHandler().post(()->{
                refreshPage(null);
            });

            return null;
        }
        if (viewpagerRefresh != 0) {
            InitApp.getHandler().post(() -> {
                Toast toast = Toast.makeText(getContext(), "也为你加载了一条数据", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 20);
                toast.show();
            });
        }
        return jsonData;
    }

    /**
     * 对集合进行排序
     */
    private void sortData() {
        Collections.sort(maplists, (o1, o2) -> {
            Date date1 = stringToDate(o1.get("createTime"));
            Date date2 = stringToDate(o2.get("createTime"));
            if (date1.after(date2)) {
                return -1;
            }
            return 1;
        });
    }

    /**
     * 对时间进行格式化处理
     *
     * @param dateString 需要时间进行格式化的String对象
     */
    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }


    /**
     * 找到控件
     */
    private void initView(View view) {
        rConsultation = view.findViewById(R.id.rv_consultation);
    }

    /**
     * 设置控件属性
     */
    private void setContent() {
        rConsultation.setLayoutManager(new LinearLayoutManager(getContext()));
        consultationAdapter = new ConsultationAdapter(getContext(), maplists);
        rConsultation.setAdapter(consultationAdapter);
    }

    /**
     * 获取咨询内容
     */

    @Override
    public void onClick(View view) {

    }

    /**
     * 类似于 Activity的 onNewIntent()
     */
    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);
        Toast.makeText(_mActivity, args.getString("from"), Toast.LENGTH_SHORT).show();
    }

    /**
     * itemActivity中的SwipeRefreshLayout刷新调用方法
     */
    public void chanrData() {
        viewpagerRefresh++;
        service.execute(() -> {
            requestData();
        });
    }

    /**
     * 检测当前的网络状态
     */
    public void checkState_21orNew() {
        //获得ConnectivityManager对象
        ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        //获取所有网络连接的信息
        Network[] networks = connMgr.getAllNetworks();
        //用于存放网络连接信息
        StringBuilder sb = new StringBuilder();
        //通过循环将网络信息逐个取出来
        for (int i = 0; i < networks.length; i++) {
            //获取ConnectivityManager对象对应的NetworkInfo对象
            NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
            sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
        }
        Log.d(TAG, "checkState_21orNew: " + sb);
    }
}
