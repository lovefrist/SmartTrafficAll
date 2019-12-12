package com.lenovo.smarttraffic.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.CoordinatorLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * IP设置
 *
 * @author asus
 * @data 2019-12-3 11：49
 */
public class IPSetupActivity extends BaseActivity implements View.OnClickListener {
    private final int END_LONTH = 3;
    private List<EditText> listet;
    private String IPJudpg = "(2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(findViewById(R.id.toolbar), true, getString(R.string.setIP));
        listet = new ArrayList();
        listet.add(findViewById(R.id.et_IP1));
        listet.add(findViewById(R.id.et_IP2));
        listet.add(findViewById(R.id.et_IP3));
        listet.add(findViewById(R.id.et_IP4));
        for (int i = 0; i < listet.size(); i++) {
            int find = i;
            listet.get(i).addTextChangedListener(new TextWatcher() {
                //内容改变之前调用
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                //内容改变调用
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > END_LONTH && count != 0) {
                        //当整数位数输入到达被要求的上限,并且当前在输入字符,而不是减少字符
                        //subSequence取出CharSequence的第几位到第几位的内容
                        listet.get(find).setText(s.subSequence(0, s.toString().length() - 1));
                        //将光标的位置移动到那个位置
                        listet.get(find).setSelection(s.toString().length() - 2);
                    }
                    if (!s.toString().matches(IPJudpg)){
                        Toast.makeText(IPSetupActivity.this,"请按标准写",Toast.LENGTH_LONG).show();
                    }
                }

                //内容改变后调用
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
        Button button = findViewById(R.id.btn_elev);
        button.setOnClickListener(this);
        CoordinatorLayout coordinatorLayout = findViewById(R.id.cl_layout);
        coordinatorLayout.setOnClickListener(this);
    }
    /**
     * 隐藏键盘输入法
     *
     * @param view The view.
     */
    public void hideSoftInput(final View view) {
        //获取InPutMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
            //设置键盘隐藏
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                // RESULT_UNCHANGED_SHOWN软键盘窗口不变保持显示时的状态
                //RESULT_SHOWN软键盘窗口从隐藏切换到显示时的状态。
                if (resultCode == InputMethodManager.RESULT_UNCHANGED_SHOWN || resultCode == InputMethodManager.RESULT_SHOWN) {
                    toggleSoftInput();
                }
            }
        });
    }

    /**
     * 软键盘切换
     */
    public static void toggleSoftInput() {
        InputMethodManager imm =
                (InputMethodManager) InitApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        //切换软键盘
        assert imm != null;
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_ipsetup;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_elev:
                boolean keyin = true;
                for (int j=0;j<listet.size();j++){
                    if (listet.get(j).getText().toString().equals("")||listet.get(j).getText().toString().matches(IPJudpg)){
                        Toast.makeText(this,"输入的内容有误",Toast.LENGTH_SHORT).show();
                        keyin = false;
                        break;
                    }
                    Log.d(TAG, "onClick: "+listet.get(j).getText().toString());
                }
                if (keyin){
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                String Ip = "";
                for (int i =0;i<listet.size();i++){
                    if (i==listet.size()-1){
                        Ip = Ip+listet.get(i).getText().toString();
                    }else {
                        Ip = Ip+listet.get(i).getText().toString()+".";
                    }
                }
                    Log.d(TAG, "onClick: IP"+Ip);
                editor.putString("IP",Ip);
                editor.putString("Port","8088");
                editor.apply();
                Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                }
            case R.id.cl_layout:
                hideSoftInput(v);
                break;

            default:
        }
    }
}
