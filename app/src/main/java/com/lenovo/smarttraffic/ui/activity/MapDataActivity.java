package com.lenovo.smarttraffic.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.entityclass.MapSignInfo;
import com.lenovo.smarttraffic.entityclass.ParkInfo;
import com.lenovo.smarttraffic.ui.adapter.ParkingAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * @author asus
 */
public class MapDataActivity extends BaseActivity {
    private static final String PARK_URI = "http://192.168.3.5:8088/transportservice/action/GetCarParkInfo.do";
    private static final String RATE_URI = "http://192.168.3.5:8088/transportservice/action/GetParkRate.do";
    private static final String regEx = "[^0-9]";
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.iv_Location)
    ImageView tLocation;
    @BindView(R.id.iv_style)
    ImageView iStyle;
    @BindView(R.id.iv_sign)
    ImageView iSign;
    @BindView(R.id.LinearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.RecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;
    @BindView(R.id.ll_Route)
    LinearLayout llRoute;

    private AMap aMap;
    private ParkingAdapter parkingAdapter;
    private ArrayList<ParkInfo> parkInfoArrayList = new ArrayList<>();
    private ArrayList<LatLng> tempList = new ArrayList();
    private boolean rout = true;

    @Override
    protected int getLayout() {
        return R.layout.activity_mapdata;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //重新周期方法OnCreate()是一个消息响应函数
        //OnCreate 函数是用来“表示一个窗口正在生成”
        //一个窗口创建（Create）之后，会向操作系统发送WM_CREATE消息，OnCreate()函数主要是用来响应此消息的
        //在OnCreate函数里实现我们要在窗口里面增加的东西，例如按扭，状态栏，工具栏等。这些子窗口一般是定义成类中的一个成员变量，因为要保证生命周期。
        // 一般以m_开头来表示成员(member)。OnCreate()不产生窗口，只是在窗口显示前设置窗口的属性如风格、位置等，Create()负责注册并产生窗口
        //$$覆盖MapDataActivity的生命周期时MapDataActivity活动不可见
        mapView.onCreate(savedInstanceState);
        initView();
        initData();
    }

    /**
     * 初始化AMap对象
     */
    private void initView() {
        initToolBar(findViewById(R.id.toolbar), true, "用户停车");
        if (aMap == null) {
            aMap = mapView.getMap();
//            getUiSettings
            UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setLogoBottomMargin(-100);
        }
        //显示实时交通状况
        aMap.setTrafficEnabled(true);
        /**
         *MAP_TYPE_SATELLITE地图样式为卫星
         *MAP_TYPE_NORMAL 地图样式为交通
         *MAP_TYPE_NIGHT 地图样式为夜间模式
         *              MAP_TYPE_NAVI
         * */
        //绘制marker
        addMarker(MapSignInfo.Map_LUOCHAHAI, R.mipmap.marker_self);
//        marker.setIcon(BitmapDescriptorFactory.fromView());自定义icon工厂类实现
        Context context = this;
        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View view = LayoutInflater.from(context).inflate(R.layout.connect_item, null);
                TextView textView = view.findViewById(R.id.tv_title);
                textView.setText("罗刹湖");
                return view;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
        iStyle.setOnClickListener(v -> {
            Log.d(TAG, "initView: 打开");
            llRoute.setVisibility(rout ? View.VISIBLE : View.GONE);
            rout = !rout;
        });
        iSign.setOnClickListener(v -> {
            recyclerView.setVisibility(View.VISIBLE);
            getParData();
        });


//        /**初始化蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。*/
//
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        //设置连续定位下的的定位间隔，单次定位模式下不会生效单位为毫秒
//        myLocationStyle.interval(3000);
//        //设置定位蓝点
//        aMap.setMyLocationStyle(myLocationStyle);
//        //设置默认定位按钮是否显示，非必需设置。
//        aMap.getUiSettings().setMyLocationButtonEnabled(false);
//        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
//        aMap.setMyLocationEnabled(true);

    }
    /*设置动画效果和确定中心点 设置地图中心点*/

    private void changeCamera(CameraUpdate update) {
        //moveCamera改变定位中心点
        aMap.moveCamera(update);
        aMap.animateCamera(CameraUpdateFactory.zoomIn(), 500, null);
    }
    /*在图片上加载点方法*/

    private void addMarker(LatLng latLnge, int res) {
        Marker marker = aMap.addMarker(new MarkerOptions().position(latLnge)
                .icon(BitmapDescriptorFactory
                        .fromResource(res))
                .draggable(false));
        tempList.add(latLnge);
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (LatLng latLng : tempList) {
            b.include(latLng);
        }
        LatLngBounds bounds = b.build();
        /*newLatLngBoundsRect()方法参数注释
//latlngbounds - 地图显示经纬度范围。
//paddingLeft - 设置经纬度范围和mapView左边缘的空隙。
//paddingRight - 设置经纬度范围和mapView右边缘的空隙。
//paddingTop - 设置经纬度范围和mapView上边缘的空隙。
//paddingBottom - 设置经纬度范围和mapView下边缘的空隙。*/
        aMap.animateCamera(CameraUpdateFactory.newLatLngBoundsRect(bounds, 100, 100, 100, 100), 500, null);
    }

    /*初始化数据*/

    private void initData() {
        //Marker 点击事件回调return返回值点击事件是否拦截
        aMap.setOnMarkerClickListener(marker -> {
            if ("Marker1".equals(marker.getId())) {
                recyclerView.setVisibility(View.GONE);
            }
            return false;
        });

        tLocation.setOnClickListener(v -> {
            //     newCameraPosition 3D效果 newLatLng只是定位的中心不放大
            /**
             * 第一个缩放中心的经纬度
             * 得二个缩放中心的大小
             * */
            changeCamera(CameraUpdateFactory.newLatLngZoom(MapSignInfo.Map_LUOCHAHAI, 16));
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        parkingAdapter = new ParkingAdapter(this, parkInfoArrayList);
        recyclerView.setAdapter(parkingAdapter);

    }

    /*获取停车场信息*/

    private void getParData() {
        service.execute(() -> {
            HashMap<String, Object> map = new HashMap(1);
            map.put("UserName", "user1");
            try {
                parkInfoArrayList.clear();
                String ParkData = newGetJsonData(map, PARK_URI);
                String rateData = newGetJsonData(map, RATE_URI);
                JSONObject rateObject = new JSONObject(rateData);
                int rate = rateObject.getInt("Money");
                JSONObject jsonObject = new JSONObject(ParkData);
                String longitude = jsonObject.getString("longitude");
                String latitude = jsonObject.getString("latitude");
                JSONArray parkArray = jsonObject.getJSONArray("ROWS_DETAIL");
                for (int i = 0; i < parkArray.length(); i++) {
                    ParkInfo parkInfo = new ParkInfo();
                    JSONObject parkObject = parkArray.getJSONObject(i);
                    String titleName = parkObject.getString("name");
                    String address = parkObject.getString("address");
                    int EmptySpace = parkObject.getInt("EmptySpace");
                    int distance = parkObject.getInt("distance");
                    int open = parkObject.getInt("open");
                    int AllSpace = parkObject.getInt("AllSpace");
                    String remarks = parkObject.getString("remarks");
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(remarks);
                    String data = m.replaceAll("");
                    int allrate = Integer.parseInt(data);
                    parkInfo.setAllSpace(AllSpace);
                    parkInfo.setTitleName(titleName);
                    parkInfo.setAddress(address);
                    parkInfo.setEmptySpace(EmptySpace);
                    parkInfo.setDistance(distance);
                    parkInfo.setOpen(open);
                    parkInfo.setRate(rate);

                    parkInfo.setLongitude(Double.parseDouble(longitude));
                    parkInfo.setLatitude(Double.parseDouble(latitude));
                    parkInfo.setAllRate(allrate);
                    parkInfoArrayList.add(parkInfo);
                }
                sqrtData();
                InitApp.getHandler().post(() -> {
                    parkingAdapter.notifyDataSetChanged();
                });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

    /*根据距离我的位置的长远进行排序*/

    private void sqrtData() {
        Collections.sort(parkInfoArrayList, (o1, o2) -> o1.getDistance() - o2.getDistance());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            if (data != null) {
                ParkInfo parkInfo = data.getParcelableExtra("ParkInfo");
                addMarker(new LatLng(parkInfo.getLongitude(), parkInfo.getLatitude()), R.mipmap.marker_one);
                Toast.makeText(this, "已为你定位到" + parkInfo.getTitleName() + "停车场", Toast.LENGTH_LONG).show();
                //设置两点之间的连线
                aMap.addPolyline((new PolylineOptions())
                        .add(MapSignInfo.Map_LUOCHAHAI)
                        .add(new LatLng(parkInfo.getLongitude(), parkInfo.getLatitude()))
                        .geodesic(true).color(Color.BLACK));
            }
        }
    }

    /*显示全部的坐标点的第二种方法*/

    public static LatLngBounds createBounds(Double latA, Double lngA, Double latB, Double lngB) {
        LatLng northeastLatLng;
        LatLng southwestLatLng;

        Double topLat, topLng;
        Double bottomLat, bottomLng;
        if (latA >= latB) {
            topLat = latA;
            bottomLat = latB;
        } else {
            topLat = latB;
            bottomLat = latA;
        }
        if (lngA >= lngB) {
            topLng = lngA;
            bottomLng = lngB;
        } else {
            topLng = lngB;
            bottomLng = lngA;
        }
        northeastLatLng = new LatLng(topLat, topLng);
        southwestLatLng = new LatLng(bottomLat, bottomLng);
        return new LatLngBounds(southwestLatLng, northeastLatLng);

    }

    /**当前Activity被其他Activity覆盖其上或被锁屏：系统会调用onPause方法，暂停当前Activity的执行。*/
    /**
     * 活动准备好和用户进行交互的时候调用 这个时候活动位于返回栈的栈顶，并且处于运行状态
     * 消息处理函数OnPaint()
     * View类里添加了消息处理函数OnPaint()时，OnPaint()就会覆盖掉OnDraw()。
     * 当活动B在活动A之前启动时，将在A上调用此回调。在A onPause()返回之前将不会创建B ，因此请确保此处不要做任何冗长的操作。
     */
    @Override

    protected void onResume() {
        super.onResume();
        //将map显示出来不显示本身的活动
        Log.d(TAG, "onResume: 执行mapView的生命周期");
        mapView.onResume();
    }

    /**
     * 活动销毁之前调用
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 执行mapView的生命周期"+mapView);
        //当活动销毁的时候MapView的方法也调用

        if (mapView!=null){
            mapView.onDestroy();
        }
    }

    /**
     * 启动和恢复另外一个活动时调用
     */
    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: 执行mapView的生命周期");
        super.onPause();
        mapView.onPause();
    }


}
