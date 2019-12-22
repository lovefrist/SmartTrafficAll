package com.lenovo.smarttraffic.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lenovo.smarttraffic.R;

/**
 * 出行建议
 * @author asus
 *
 */
public class MetroAdviceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        initToolBar(findViewById(R.id.toolbar),true,"出行建议");
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_metroadvice;
    }
}
