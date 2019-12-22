package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lenovo.smarttraffic.R;

import butterknife.BindView;

public class SecedeActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_queryETC)
    LinearLayout tQueryETC;
    @BindView(R.id.Sign)
    LinearLayout linearLayout;
    private Button button;
    private String userfist = "点击头像登录";

    @Override
    protected int getLayout() {
        return R.layout.activity_secede;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(findViewById(R.id.toolbar), true, "设置");
        initView();
    }

    private void initView() {
        button = findViewById(R.id.outButton);
        SharedPreferences pef = getSharedPreferences("data", MODE_PRIVATE);
        int state = pef.getInt("state", -1);
        if (user == null) {
            button.setEnabled(false);
        }
        button.setOnClickListener(this);
        tQueryETC.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.outButton) {

        }
        if (v.getId() == R.id.tv_queryETC) {


        }
        switch (v.getId()){
            case R.id.outButton:
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putInt("state", 0);
                editor.apply();
                user = null;
                Toast.makeText(this, "退出登陆成功", Toast.LENGTH_SHORT).show();
                setResult(4);
                finish();
                break;
            case R.id.tv_queryETC:
                if (BaseActivity.user != null) {
                    Intent intent = new Intent(this, SignActivity.class);
                    intent.putExtra("ETC", 1);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "请先登陆", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.Sign:
                Intent intent = new Intent(this, SignActivity.class);
                intent.putExtra("ETC", 0);
                startActivity(intent);
                finish();
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "跳转过来了", Toast.LENGTH_LONG).show();
    }
}
