package com.lenovo.smarttraffic.ui.activity;

import android.os.Bundle;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.myview.MyRealerViewSidestep;

/**
 *
 * @author asus
 */
public class ConsumptionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(findViewById(R.id.toolbar),true,"签到中心");
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_consumption;
    }
}
