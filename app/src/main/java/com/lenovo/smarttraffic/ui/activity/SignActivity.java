package com.lenovo.smarttraffic.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lenovo.smarttraffic.MainActivity;
import com.lenovo.smarttraffic.R;

public class SignActivity extends BaseActivity implements View.OnClickListener {
    private Button button;
    private String userfist = "点击头像登录";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(findViewById(R.id.toolbar), true, "用户签到");
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this,"跳转过来了",Toast.LENGTH_LONG).show();
        Log.d(TAG, "onActivityResult: ");
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_sign;
    }

    private void initView() {
        button = findViewById(R.id.outButton);
        SharedPreferences pef = getSharedPreferences("data", MODE_PRIVATE);
        int state = pef.getInt("state", -1);
       if (user==null){
           button.setEnabled(false);
       }
       button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.outButton) {
            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
            editor.putInt("state", 0);
            editor.apply();
            user = null;
            Toast.makeText(this, "退出登陆成功", Toast.LENGTH_SHORT).show();
            setResult(4);
            finish();
        }
    }
}
