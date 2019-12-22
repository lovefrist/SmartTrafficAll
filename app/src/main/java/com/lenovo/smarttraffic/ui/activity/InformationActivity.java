package com.lenovo.smarttraffic.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.lenovo.smarttraffic.R;

public class InformationActivity extends BaseActivity {
    private RecyclerView information;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        information = findViewById(R.id.rv_information);
        initToolBar(findViewById(R.id.toolbar),true,"天气信息");

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_information;
    }
}
