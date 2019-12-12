package com.lenovo.smarttraffic.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.ui.adapter.LeftIndexAdapter;
import com.lenovo.smarttraffic.ui.adapter.SituationAdapter;
import com.lenovo.smarttraffic.util.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 天气预报完成
 * 国赛2019第二题
 *
 * @author asus
 */
public class ForecastActivity extends BaseActivity {
    /**
     * 获取天气预报的接口
     */
    private final static String WEATHER_URL = "http://192.168.3.5:8088/transportservice/action/GetWeather.do";
    /**
     * 获取传感器内容的接口
     */
    private final static String SENSE_URL = "http://192.168.3.5:8088/transportservice/action/GetAllSense.do";
    private static final String TAG = "ForecastActivity";

    private TextView tDate, tWeek, tTemperature, tType;
    private ImageView imType;
    private RecyclerView rvTop, rvBottom;

    private List<Map> mapList = new ArrayList<>();
    private List listLeft = new ArrayList();
    private SituationAdapter topAdapter;
    private LeftIndexAdapter bottomAdapter;

    private ArrayList<Entry> values1 = new ArrayList<>();
    private ArrayList<Entry> values2 = new ArrayList<>();

    private boolean keyindex = true;
    private LineChart lineChart;
    private LineDataSet set1;
    private LineDataSet set2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolBar(findViewById(R.id.toolbar), true, "天气预报");
        initView();
        rvTop.setLayoutManager(new GridLayoutManager(this, 6));
        topAdapter = new SituationAdapter(this, mapList);
        rvTop.setAdapter(topAdapter);

        getWeather();

        rvBottom.setLayoutManager(new GridLayoutManager(this, 6));
        bottomAdapter = new LeftIndexAdapter(this, listLeft);
        rvBottom.setAdapter(bottomAdapter);
        getSense();
        setChartStyles();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_forecast;
    }

    /**
     * 设置lineChart的样式
     */
    private void setChartStyles() {
        lineChart = findViewById(R.id.lc_chart);
        //没有数据时显示的文字
        lineChart.setNoDataText("网路出现错误无法获取数据");
        //没有数据时显示的颜色
        lineChart.setNoDataTextColor(Color.RED);
        //chart 绘图区后面的背景矩形将绘制
        lineChart.setDrawGridBackground(true);
        //禁止绘制图表边框的线
        lineChart.setDrawBorders(false);
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);
        /**
         * 设置线的样式
         * */
        //设置数据1  参数1：数据源 参数2：图例名称
        set1 = new LineDataSet(values1, "测试数据1");
        set1.setColor(Color.rgb(0, 191, 255));
        //设置线宽
        set1.setLineWidth(1f);
        //设置显示值的文字大小
        set1.setValueTextSize(9f);
        //设置禁用范围背景填充
        set1.setDrawFilled(false);
        //格式化显示数据
        DecimalFormat decimalFormat = new DecimalFormat("###,###,##0");
        set1.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> decimalFormat.format(value));
        set2 = new LineDataSet(values2, "测试数据2");
        set2.setColor(Color.rgb(255, 215, 0));
        set2.setLineWidth(1f);
        set2.setDrawFilled(false);
        set2.setCircleRadius(3f);
        //设置字体的大小
        set2.setValueTextSize(10f);

        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //保存LineDataSet集合
        YAxis rightAxis = lineChart.getAxisRight();
        //设置图表右边的y轴禁用
        rightAxis.setEnabled(false);
        //获取左边的轴线
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setEnabled(false);
        //是否运行触摸
        lineChart.setTouchEnabled(false);
        // 是否可以拖拽
        lineChart.setDragEnabled(false);
        // 是否运行缩放和扩大
        lineChart.setScaleEnabled(false);
        //获取图例
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        XAxis xAxis = lineChart.getXAxis();
        //设置轴启用或禁用 如果禁用以下的设置全部不生效
        xAxis.setEnabled(false);
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(5.5f);
    }

    private void setChartData() {
        //判断图表中原来是否有数据
        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            //获取数据1
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values1);
            set2 = (LineDataSet) lineChart.getData().getDataSetByIndex(1);
            set2.setValues(values2);
            Log.d(TAG, "setChartData: 1");
            //刷新数据
        } else {
            //设置数据1  参数1：数据源 参数2：图例名称
            set1 = new LineDataSet(values1, "测试数据1");
            set1.setColor(Color.rgb(0, 191, 255));
            //设置线宽
            set1.setLineWidth(1f);
            //设置显示值的文字大小
            set1.setValueTextSize(9f);
            //设置禁用范围背景填充
            set1.setDrawFilled(false);
            //格式化显示数据
            DecimalFormat decimalFormat = new DecimalFormat("###,###,##0");
            set1.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> decimalFormat.format(value));
            set2 = new LineDataSet(values2, "测试数据2");
            set2.setColor(Color.rgb(255, 215, 0));
            set2.setLineWidth(1f);
            set2.setDrawFilled(false);
            set2.setCircleRadius(3f);
            //设置字体的大小
            set2.setValueTextSize(10f);

            set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            Log.d(TAG, "setChartData: 2");
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        // 添加LineDataSet
        dataSets.add(set1);
        dataSets.add(set2);
        //创建LineData对象 属于LineChart折线图的数据集合
        LineData data = new LineData(dataSets);
        // 添加到图表中
        lineChart.setData(data);
        //绘制图表
        lineChart.invalidate();
        //刷新数据
        lineChart.getData().notifyDataChanged();
        lineChart.notifyDataSetChanged();
    }

    /**
     * 获取当天星期几
     */
    public static String getWeek(int number) {
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        int dat = i + number;
        switch (dat) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }

    /**
     * 找到控件
     */
    private void initView() {
        tDate = findViewById(R.id.time_date);
        tWeek = findViewById(R.id.tv_week);
        tTemperature = findViewById(R.id.tv_temperature);
        tType = findViewById(R.id.tv_type);
        imType = findViewById(R.id.iv_type);
        rvTop = findViewById(R.id.rv_Top);
        rvBottom = findViewById(R.id.rv_Bottom);
    }

    /**
     * 设置折线图和设置第一行数据
     */
    private void getWeather() {
        service.execute(() -> {
            try {
                HashMap<String, Object> map = new HashMap<>(1);
                map.put("UserName", "user1");
                String JsonData = NetworkUtil.getJsonData(map, WEATHER_URL);
                JSONObject jsonObject = new JSONObject(JsonData);
                String wCurrent = jsonObject.getString("WCurrent");
                JSONArray array = jsonObject.getJSONArray("ROWS_DETAIL");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    String temperature = object.getString("temperature");
                    String[] temperatures = temperature.split("~");
                    String wData = object.getString("WData");
                    String type = object.getString("type");
                    Map map1 = new HashMap();
                    if (i == 1) {

                        String week = new SimpleDateFormat("EEE").
                                format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(wData + " 00:00:01").getTime());

                        InitApp.getHandler().post(() -> {
                            tDate.setText(wData);
                            tWeek.setText(getWeek(0));
                            tTemperature.setText(wCurrent);
                            tType.setText(type);
                            switch (type) {
                                case "晴":
                                    imType.setImageResource(R.drawable.fine);
                                    break;
                                case "小雨":
                                    imType.setImageResource(R.drawable.minrain);
                                    break;
                                case "阴":
                                    imType.setImageResource(R.drawable.overcast);
                                default:
                                    break;
                            }
                        });
                    }
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(wData);
                    String time = new SimpleDateFormat("MM月dd日").format(date);
                    String week = new SimpleDateFormat("EEE").format(date);

                    map1.put("time", time);
                    map1.put("week", i == 0 ? "(今日" + week + ")" : week);
                    map1.put("type", type);

                    switch (type) {
                        case "晴":
                            map1.put("src", R.drawable.fine);
                            break;
                        case "小雨":
                            map1.put("src", R.drawable.minrain);
                            break;
                        case "阴":
                            map1.put("src", R.drawable.overcast);
                        default:
                            break;
                    }

                    mapList.add(map1);

                    if (i == 0) {
                        values1.add(new Entry(-1, Integer.parseInt(temperatures[0])));
                        values2.add(new Entry(-1, Integer.parseInt(temperatures[1])));
                    }
                    values1.add(new Entry(i, Integer.parseInt(temperatures[0])));
                    values2.add(new Entry(i, Integer.parseInt(temperatures[1])));

                    if (i == array.length() - 1) {
                        values1.add(new Entry(i + 1, Integer.parseInt(temperatures[0])));
                        values2.add(new Entry(i + 1, Integer.parseInt(temperatures[1])));
                    }
                }
                handler.post(() -> {
                    Log.d(TAG, "getWeather: " + mapList.size());
                    topAdapter.notifyDataSetChanged();
                    setChartData();
                });
            } catch (JSONException | ParseException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void getSense() {
        service.execute(() -> {
            while (keyindex) {
                long startTime = System.currentTimeMillis();
                List list = new ArrayList();
                try {
                    Map map = new HashMap(1);
                    map.put("UserName", "user1");
                    /**获取传感器指数*/
                    String JsonData = NetworkUtil.getJsonData(map, SENSE_URL);
                    JSONObject jsonObject = new JSONObject(JsonData);
                    int pm25 = jsonObject.getInt("pm2.5");
                    int co2 = jsonObject.getInt("co2");
                    int lightIntensity = jsonObject.getInt("LightIntensity");
                    int humidity = jsonObject.getInt("humidity");
                    int temperature = jsonObject.getInt("temperature");
                    list.add(lightIntensity);
                    list.add(pm25);
                    list.add(co2);
                    list.add(temperature);
                    list.add(humidity);
                    /**获取天气预报*/
                    String JsonData1 = NetworkUtil.getJsonData(map, WEATHER_URL);
                    JSONObject o1 = new JSONObject(JsonData1);
                    JSONArray array = o1.getJSONArray("ROWS_DETAIL");
                    int number = 0;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o2 = new JSONObject(array.get(i).toString());
                        if (i < 3) {
                            String type = o2.getString("type");

                            boolean num = ( i == 0 || i == 1);
                            if ("小雨".equals(type) && num) {
                                number = number + 5;
                            } else if ("小雨".equals(type)) {
                                number++;
                            }
                        }
                    }
                    list.add(number);
                    listLeft.clear();
                    listLeft.addAll(list);
                    handler.post(() -> {
                        bottomAdapter.notifyDataSetChanged();
                    });

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                long endTime = System.currentTimeMillis();
                if (endTime - startTime < 3000) {
                    SystemClock.sleep(3000 - (endTime - startTime));
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        keyindex = false;
    }
}
