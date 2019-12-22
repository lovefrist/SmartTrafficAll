package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.entityclass.ParkInfo;

import butterknife.BindView;

/**
 *停车场的详细信息
 * @author asus
 */
public class ParkDetailsActivity extends BaseActivity {
    @BindView(R.id.titleName)
    TextView titleName;
    @BindView(R.id.checkbox)
    CheckBox checkBox;
    @BindView(R.id.tv_address)
    TextView tAddress;
    @BindView(R.id.tv_distance)
    TextView tDistance;
    @BindView(R.id.tv_parkMoney)
    TextView tParkMoney;
    @BindView(R.id.tv_parkLot)
    TextView tParkLot;
    @BindView(R.id.tv_aiiDay)
    TextView tAiiDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        initToolBar(findViewById(R.id.toolbar),true,"停车场详情");
    }

    private void initData() {
        Intent intent = getIntent();
        ParkInfo parkInfo =intent.getParcelableExtra("ParkInfo");
        titleName.setText(parkInfo.getTitleName());
        checkBox.setChecked(parkInfo.getOpen()==1);
        checkBox.setHighlightColor(Color.RED);
        tAddress.setText(parkInfo.getAddress());
        tDistance.setText(parkInfo.getDistance()+"米");
        tParkMoney.setText(parkInfo.getRate()+"元/小时");
        tParkLot.setText(parkInfo.getEmptySpace()+"个/"+parkInfo.getAllSpace());
        tAiiDay.setText("每小时"+parkInfo.getRate()+"元，最高"+parkInfo.getAllRate()+"/元每天");

        Log.d(TAG, "getParData: "+parkInfo.getLatitude());
        tAddress.setOnClickListener(v -> {
            Intent strIntent1 = new Intent();
            strIntent1.putExtra("ParkInfo",parkInfo);

            setResult(1,strIntent1);
            finish();
        });
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_park_details;
    }
}
