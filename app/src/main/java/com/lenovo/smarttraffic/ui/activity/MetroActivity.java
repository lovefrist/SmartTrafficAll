package com.lenovo.smarttraffic.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lenovo.smarttraffic.R;

public class MetroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(findViewById(R.id.toolbar),true,"地铁中心");

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_metro;
    }
}
