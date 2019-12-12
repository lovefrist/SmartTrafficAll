package com.lenovo.smarttraffic.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.ui.activity.ConsumptionActivity;
import com.lenovo.smarttraffic.ui.activity.ForecastActivity;
import com.lenovo.smarttraffic.ui.activity.MetroActivity;
import com.lenovo.smarttraffic.ui.activity.SetInstallActivity;
import com.lenovo.smarttraffic.ui.activity.UserAdminActivity;
import com.lenovo.smarttraffic.util.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Amoly
 * @date 2019/4/11.
 * description：主页面
 */
public class MainContentFragment extends BaseFragment {
    private static MainContentFragment instance = null;
    private String weather = "http://192.168.3.5:8088/transportservice/action/GetWeather.do";
    private String sense = "http://192.168.3.5:8088/transportservice/action/GetAllSense.do";
    private String getPm25 = "http://192.168.3.5:8088/transportservice/action/GetSenseByName.do";
    private String[] data = new String[]{"优", "良", "轻度污染", "中度污染", "重度污染"};
    private int[] number = new int[]{36, 76, 116, 151};
    List<PieEntry> yvals = new ArrayList<>();
    List<Integer> colors = new ArrayList<>();
    private TextView tvtemToday, tvchangeToday, nexday, tvnextWeather;
    private ImageView homedayincome, homenextDayincome;
    private boolean keypm = true;
    private PieChart pieChart;
    private Handler handler = new Handler();
    private List<TextView> list = new ArrayList<>();
    private int anInt = 0;
    private Work work;

    String[][] deredr = new String[][]{
            {"弱", "中等", "强"}, {"适宜", "中", "较不适宜"},
            {"冷", "舒适", "温暖", "热"}, {"易发", "少发"}
    };
    String[][] strcolor = new String[][]{
            {"#4472c4", "#00b050", "#ff0000"}, {"#44dc68", "#ffc000", "#8149ac"},
            {"#3462f4", "#92d050", "#44dc68", "#ff0000"}, {"#ff0000", "#ffff40"}

    };
    int[][] numberall = new int[][]{
            {1001, 3001}, {3001, 6001}, {13, 22, 36}, {51}
    };

    public static MainContentFragment getInstance() {
        if (instance == null) {
            instance = new MainContentFragment();
        }
        return instance;
    }

    /**
     * 找到控件
     */
    @Override
    protected View getSuccessView() {
        View view = View.inflate(getActivity(), R.layout.fragment_home, null);


        setViewContent(view);
        showRingPieChart();
//        广播
        initroadcast();
        return view;
    }

    private void setViewContent(View view) {
        pieChart = view.findViewById(R.id.pc_PieChart);
        /**---------------------------------------------找到第二行的控件-------------------------------*/
        tvtemToday = view.findViewById(R.id.tv_temToday);
        tvchangeToday = view.findViewById(R.id.tv_changeToday);
        nexday = view.findViewById(R.id.nex_day);
        tvnextWeather = view.findViewById(R.id.tv_nextWeather);
        homedayincome = view.findViewById(R.id.home_day_income);
        homenextDayincome = view.findViewById(R.id.home_nextDay_income);

        /**---------------------------------------------找到第三行的控件-------------------------------*/

        TextView UltravioletText = view.findViewById(R.id.UltravioletText);
        TextView motionText = view.findViewById(R.id.motionText);
        TextView DressingText = view.findViewById(R.id.DressingText);
        TextView ColdText = view.findViewById(R.id.ColdText);
        list.clear();
        list.add(UltravioletText);
        list.add(motionText);
        list.add(DressingText);
        list.add(ColdText);
        /**------------------------------------------设置RelativeLayoutt天气情况只有登陆的时候才能点击---------------------------------------------  */
        RelativeLayout relativeLayouttoday = view.findViewById(R.id.rl_Today);
        RelativeLayout relativeLayoutnexday = view.findViewById(R.id.rl_nexDay);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        TextView userdata = navigationView.getHeaderView(0).findViewById(R.id.textViewuser);
        String chang = "点击头像登录";
        relativeLayouttoday.setOnClickListener(v -> {
            if (!chang.equals(userdata.getText().toString())) {
                Intent intent = new Intent(getActivity(), ForecastActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "您未登录，请登录后查看", Toast.LENGTH_SHORT).show();
            }
        });
        relativeLayoutnexday.setOnClickListener(v -> {
            if (!chang.equals(userdata.getText().toString())) {
                Intent intent = new Intent(getActivity(), ForecastActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "您未登录，请登录后查看", Toast.LENGTH_SHORT).show();
            }
        });

        /**--------------------------------------设置地铁系统只有登陆了才能点击-----------------------------------------------------------------------*/
        RelativeLayout metro = view.findViewById(R.id.metro);
        metro.setOnClickListener(v -> {
            if (!chang.equals(userdata.getText().toString())) {
                Intent intent = new Intent(getActivity(), MetroActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "您未登录，请登录后查看", Toast.LENGTH_SHORT).show();
            }
        });

        /**-----------------------------------------------------------设置用户中心只有登陆了才能点击-------------------------------------------------------------------------*/
        RelativeLayout relativeLayout = view.findViewById(R.id.home_manager_center);
        relativeLayout.setOnClickListener(v -> {
            if (!chang.equals(userdata.getText().toString())) {
                Intent intent = new Intent(getActivity(), UserAdminActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "您未登录，请登录后查看", Toast.LENGTH_SHORT).show();
            }
        });
        /**-----------------------------------------------------------设置消费中心点击-------------------------------------------------------------------------*/
        RelativeLayout consumption = view.findViewById(R.id.consumption);
        consumption.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SetInstallActivity.class);
            startActivity(intent);
        });

        /**-----------------------------------------------------------设置用户签到点击-------------------------------------------------------------------------*/
        RelativeLayout Sing = view.findViewById(R.id.Sing);
        consumption.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ConsumptionActivity.class);
            startActivity(intent);
        });
    }

    private void initroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        work = new Work();
//        //注册广
//        work.getonClick(new BroadInter() {
//            @Override
//            public void onClick() {
//                refreshPage(null);
//            }
//            @Override
//            public void DataOnclick() {
//            }
//        });
        getContext().registerReceiver(work, intentFilter);
    }



    @Override
    protected Object requestData() {
        Log.e(TAG, "requestData: " );
        JSONObject jsonObject = null;
        try {
            Map map = new HashMap(1);
            map.put("UserName", "user1");
            String jsonData = NetworkUtil.getJsonData(map, weather);
            jsonObject = new JSONObject(jsonData);
            handler.post(()->{
                refreshPage(jsonData);
                Log.e(TAG, "requestData: 刷新" );
            });
            String wCurrent = jsonObject.getString("WCurrent");
            JSONArray detail = jsonObject.getJSONArray("ROWS_DETAIL");
            List list = new ArrayList();
            String data = "";
            for (int i = 0; i < detail.length(); i++) {
                JSONObject dataObject1 = new JSONObject(detail.get(i).toString());
                if (i < 3) {
                    String type = dataObject1.getString("type");
                    list.add(type);
                    if (i == 2) {
                        String temperature = dataObject1.getString("temperature");
                        data = temperature;
                    }
                }
            }
            List imgsrc = new ArrayList();
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i).toString().equals("晴")) {
                    imgsrc.add(R.drawable.sun);
                } else if (list.get(i).toString().equals("阴")) {
                    imgsrc.add(R.drawable.cloudy);
                } else {
                    imgsrc.add(R.drawable.minrainh);
                }
            }
            String finalData = data;
            handler.post(() -> {
                tvtemToday.setText(wCurrent + "°");
                tvchangeToday.setText(list.get(0).toString() + "转" + list.get(1).toString());
                nexday.setText(finalData + "°");
                tvnextWeather.setText(list.get(2).toString());
                homedayincome.setImageResource(Integer.parseInt(imgsrc.get(0).toString()));
                homenextDayincome.setImageResource(Integer.parseInt(imgsrc.get(1).toString()));
            });
        } catch (JSONException e) {
            e.printStackTrace();
            objlist = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    /**
     * 绘制圆环的基本信息
     */
    public void showRingPieChart() {
        pieChart.setNoDataText("网路不好检测一下");
        //显示为圆环
        pieChart.setDrawHoleEnabled(true);

        pieChart.setDrawCenterText(true);
        //设置中间文字
        pieChart.setCenterText("暂无数据");
        //设置饼图上面是否有文字
        pieChart.setDrawEntryLabels(false);
        //设置是否可以转动
        pieChart.setTouchEnabled(false);
        //设置右角的文字是否显示
        pieChart.getDescription().setEnabled(false);
        //设置点击是否有效果
        pieChart.setSaveFromParentEnabled(false);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        pieChart.setHoleRadius(80);
        //数据集合
        pieChart.notifyDataSetChanged();
    }

    /**
     * 设置天气信息
     */
    private void getweather() {
        service.execute(() -> {
            while (keypm) {
                try {
                    Map map = new HashMap();
                    map.put("UserName", "user1");
                    String jsonData = NetworkUtil.getJsonData(map, weather);
                    objlist = jsonData;
                    JSONObject jsonObject = new JSONObject(jsonData);
                    String wCurrent = jsonObject.getString("WCurrent");
                    JSONArray detail = jsonObject.getJSONArray("ROWS_DETAIL");
                    List list = new ArrayList();
                    String data = "";
                    for (int i = 0; i < detail.length(); i++) {
                        JSONObject dataObject1 = new JSONObject(detail.get(i).toString());
                        if (i < 3) {
                            String type = dataObject1.getString("type");
                            list.add(type);
                            if (i == 2) {
                                String temperature = dataObject1.getString("temperature");
                                data = temperature;
                            }
                        }
                    }
                    List imgsrc = new ArrayList();
                    for (int i = 1; i < list.size(); i++) {
                        if (list.get(i).toString().equals("晴")) {
                            imgsrc.add(R.drawable.sun);
                        } else if (list.get(i).toString().equals("阴")) {
                            imgsrc.add(R.drawable.cloudy);
                        } else {
                            imgsrc.add(R.drawable.minrainh);
                        }
                    }
                    String finalData = data;
                    handler.post(() -> {
                        tvtemToday.setText(wCurrent + "°");
                        tvchangeToday.setText(list.get(0).toString() + "转" + list.get(1).toString());
                        nexday.setText(finalData + "°");
                        tvnextWeather.setText(list.get(2).toString());
                        homedayincome.setImageResource(Integer.parseInt(imgsrc.get(0).toString()));
                        homenextDayincome.setImageResource(Integer.parseInt(imgsrc.get(1).toString()));
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    objlist = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * 获取pm2.5数据刷新圆环
     */
    private void setPieChart() {
        service.execute(() -> {
            while (keypm) {
                long starttime = System.currentTimeMillis();
                try {
                    yvals.clear();
                    colors.clear();
                    Map map = new HashMap(1);
                    map.put("SenseName", "pm2.5");
                    map.put("UserName", "user1");
                    String JsonData = NetworkUtil.getJsonData(map, getPm25);
                    objlist = JsonData;
                    JSONObject jsonObject = new JSONObject(JsonData);

                    int pm25 = jsonObject.getInt("pm2.5");
                    yvals.add(new PieEntry(pm25, "PM2.5"));
                    yvals.add(new PieEntry(80, "剩余PM2.5"));
                    colors.add(Color.parseColor("#DC143C"));
                    colors.add(Color.parseColor("#7FFF00"));
                    String name = "";
                    for (int i = 0; i < number.length; i++) {
                        if (pm25 < number[i]) {
                            name = data[i];
                            break;
                        }
                        if (pm25 > number[number.length - 1]) {
                            name = data[number.length];
                            break;
                        }
                    }
                    String finalName = name;
                    handler.post(() -> pieChartdata(finalName));

                } catch (JSONException e) {
                    e.printStackTrace();
                    yvals.clear();
                    colors.clear();
                    objlist = null;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                long endime = System.currentTimeMillis();
                if ((endime - starttime) < 3000) {
                    try {
                        Thread.sleep(3000 - (endime - starttime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 设置传感器信息
     */
    private void setTherrView() {
        service.execute(() -> {
            while (keypm) {
                try {
                    Map map = new HashMap(1);
                    map.put("UserName", "user1");
                    objlist = "";
                    String jsonData = NetworkUtil.getJsonData(map, sense);
                    JSONObject jsonObject = new JSONObject(jsonData);
                    objlist = jsonData;
                    int co2 = jsonObject.getInt("co2");
                    int lightIntensity = jsonObject.getInt("LightIntensity");
                    int humidity = jsonObject.getInt("humidity");
                    int temperature = jsonObject.getInt("temperature");
                    List indexlist = new ArrayList();
                    indexlist.add(lightIntensity);
                    indexlist.add(co2);
                    indexlist.add(temperature);
                    indexlist.add(humidity);
                    handler.post(() -> {
                        for (int i = 0; i < numberall.length; i++) {
                            int index = numberall[i].length;
                            for (int k = 0; k < index + 1; k++) {
                                if (k == index) {
                                    list.get(i).setTextColor(Color.parseColor(strcolor[i][index]));
                                    list.get(i).setText(deredr[i][index] + "");
                                    break;
                                } else if (Integer.parseInt(indexlist.get(k).toString()) < numberall[i][k]) {

                                    list.get(i).setText(deredr[i][k] + "");
                                    list.get(i).setTextColor(Color.parseColor(strcolor[i][k]));
                                    break;
                                }
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    objlist = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 设置PieChart内容
     */
    private void pieChartdata(String finalName) {

        PieDataSet dataset = new PieDataSet(yvals, "");
        //填充每个区域的颜色
        dataset.setColors(colors);
        //是否在图上显示数值
        dataset.setDrawValues(false);
        // 当ValuePosits为OutsiDice时，指示偏移为切片大小的百分比
        dataset.setValueLinePart1OffsetPercentage(80f);
        // 当值位置为外边线时，表示线的颜色。
        dataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //设置每条之前的间隙
        dataset.setSliceSpace(0);
        //设置饼状Item被选中时变化的距
        //填充数据
        PieData pieData = new PieData(dataset);
        // 格式化显示的数据为%百分比
        pieData.setValueFormatter(new PercentFormatter());

        pieChart.setData(pieData);
        pieChart.getData().notifyDataChanged();
        pieChart.setCenterText(finalName);
        pieChart.invalidate();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_day_income:
                break;
            default:
        }
    }

    @Override
    public void onStop() {
        keypm = false;
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        anInt = 1;
        keypm = true;
        getweather();
        setPieChart();
        setTherrView();

    }



    @Override
    public void onDestroy() {
        if (instance != null) {
            instance = null;
        }
        super.onDestroy();
    }
    class Work extends BroadcastReceiver{
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network network = connectivityManager.getActiveNetwork();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (network == null) {
                    InitApp.getHandler().post(()->{
                        refreshPage(null);
                    });

                }
            } else {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    }
                } else {
                    InitApp.getHandler().post(()->{
                        refreshPage(null);
                    });
                }
            }
        }
    }

}
