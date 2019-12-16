package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.entityclass.RecordInfo;
import com.lenovo.smarttraffic.entityclass.UserInfo;
import com.lenovo.smarttraffic.ui.adapter.RecordAdapter;
import com.lenovo.smarttraffic.util.NetworkUtil;
import com.lenovo.smarttraffic.util.ScreenUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * 违章查询页面
 * 点击头像进入
 *
 * @author asus
 */
public class RecordActivity extends BaseActivity {
    private static final String PECS_URI = "http://192.168.3.5:8088/transportservice/action/GetCarPeccancy.do";
    private static final String CARIN_URI = "http://192.168.3.5:8088/transportservice/action/GetCarInfo.do";
    private static final String TYPE_URI = "http://192.168.3.5:8088/transportservice/action/GetPeccancyType.do";
    private static final String USERALL_URI = "http://192.168.3.5:8088/transportservice/action/GetSUserInfo.do";
    @BindView
            (R.id.rv_record)
    RecyclerView rRecord;
    @BindView(R.id.iv_portrait)
    ImageView iPortrait;
    @BindView(R.id.tv_name)
    TextView tName;
    @BindView(R.id.tv_Gender)
    TextView tGender;
    @BindView(R.id.tv_Telephone)
    TextView tTelephone;
    @BindView(R.id.layout_View)
    LinearLayout layoutView;

    private ArrayList<RecordInfo> infoArrayList = new ArrayList<>();
    private RecordAdapter recordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        UserInfo info = intent.getParcelableExtra("user");
        iPortrait.setImageResource(info.getImgUri());
        tName.setText(info.getPname());
        tGender.setText(info.getPsex());
        tTelephone.setText(info.getPtel());
        rRecord.setLayoutManager(new LinearLayoutManager(this));
        recordAdapter = new RecordAdapter(this, infoArrayList);
        rRecord.setAdapter(recordAdapter);
        getData(info.getUsername());
        initToolBar(findViewById(R.id.toolbar), true, "违章详情");
    }

    /**
     * 获取违章思路
     * 1根据用户名活动用户信息得到身份证号(一般管理员权限（user1,user2）)
     * 2根据身份证号得到名下车辆(一般管理员权限)
     * 3根据车辆得到车辆违章记录
     **/
    private void getData(String username) {
        service.execute(() -> {
            HashMap<String, Object> map = new HashMap<>(1);
            map.put("UserName", "user1");
            //获取用户身份证信息
            ArrayList<String> carList = null;
            ArrayList<String> carbrandList = null;
            try {
                String UriAll = newGetJsonData(map, USERALL_URI);
                JSONObject jsonObject = new JSONObject(UriAll);
                JSONArray userArray = jsonObject.getJSONArray("ROWS_DETAIL");
                String userpcardid = null;
                for (int i = 0; i < userArray.length(); i++) {
                    JSONObject userOb = userArray.getJSONObject(i);
                    if (userOb.getString("username").equals(username)) {
                        userpcardid = userOb.getString("pcardid");
                        break;
                    }
                }
                String pecsData = newGetJsonData(map, CARIN_URI);
                JSONObject pecs = new JSONObject(pecsData);
                JSONArray pecsArray = pecs.getJSONArray("ROWS_DETAIL");
                carList = new ArrayList<>();
                carbrandList = new ArrayList<>();
                //根据身份证查询用户名下车辆
                for (int i = 0; i < pecsArray.length(); i++) {
                    JSONObject pecsOb = pecsArray.getJSONObject(i);
                    if (pecsOb.getString("pcardid").equals(userpcardid)) {
                        carList.add(pecsOb.getString("carnumber"));
                        carbrandList.add(pecsOb.getString("carbrand"));
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            if (carbrandList != null) {


                //根据车辆信息查询车辆违章信息
                for (int i = 0; i < carList.size(); i++) {
                    try {
                        map.clear();
                        map.put("UserName", "user1");
                        map.put("carnumber", carList.get(i));
                        String carData = newGetJsonData(map, PECS_URI);
                        JSONObject car = new JSONObject(carData);
                        JSONArray carArray = car.getJSONArray("ROWS_DETAIL");
                        for (int j = 0; j < carArray.length(); j++) {
                            JSONObject catinfo = carArray.getJSONObject(j);
                            String carnumber = catinfo.getString("carnumber");
                            String pcode = catinfo.getString("pcode");
                            String paddr = catinfo.getString("paddr");
                            String pdatetime = catinfo.getString("pdatetime");
                            map.clear();
                            map.put("UserName", "user1");
                            String typeData = newGetJsonData(map, TYPE_URI);
                            JSONObject type = new JSONObject(typeData);
                            JSONArray typeArray = type.getJSONArray("ROWS_DETAIL");
                            for (int k = 0; k < typeArray.length(); k++) {
                                JSONObject typeinfo = typeArray.getJSONObject(k);
                                if (typeinfo.getString("pcode").equals(pcode)) {
                                    int pmoney = typeinfo.getInt("pmoney");
                                    int pscore = typeinfo.getInt("pscore");
                                    String premarks = typeinfo.getString("premarks");
                                    RecordInfo recordInfo = new RecordInfo();
                                    recordInfo.setImgUri(getResources().getIdentifier(carbrandList.get(i), "drawable", getPackageName()));
                                    recordInfo.setNumber(0);
                                    recordInfo.setCarNumber(carList.get(i));
                                    recordInfo.setPaddr(paddr);
                                    recordInfo.setPremarks(premarks);
                                    recordInfo.setPscore(pscore);
                                    recordInfo.setPmoney(pmoney);
                                    String timeData = pdatetime.replace(" ", "\n");
                                    recordInfo.setPdate(timeData);
                                    infoArrayList.add(recordInfo);
                                    break;
                                }
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            InitApp.getHandler().post(() -> {
                if (infoArrayList.size() == 0) {
                    TextView textView = new TextView(this);
                    textView.setText("恭喜你！没有违章记录！<-0 -0>");
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.RED);
                    layoutView.addView(textView, 1);
                } else {
                    recordAdapter.notifyDataSetChanged();
                }
            });


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 2) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                int position = bundle.getInt("position");
                int state = bundle.getInt("state");

                infoArrayList.get(position).setNumber(state);

                recordAdapter.notifyItemChanged(position);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_record;
    }
}
