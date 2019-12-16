package com.lenovo.smarttraffic.ui.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.sql.MyConnectSQL;
import com.lenovo.smarttraffic.util.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private final static String LOGIN_URL = "http://192.168.3.5:8088/transportservice/action/user_login.do";
    private final static String GetSUser = "http://192.168.3.5:8088/transportservice/action/GetSUserInfo.do";
    private final static String REGULAR_Length = "^.{0,15}$";
    private final static String RO1 = "R01";
    private final static String RO2 = "R02";
    private final static String RO3 = "R03";
    /**
     * 输入运行输入的位数
     */
    private static int INTEGER_COUNT = 14;

    private EditText mEditTextName;
    private EditText mEditTextPassword;
    private TextInputLayout mTextInputLayoutName;
    private TextInputLayout mTextInputLayoutPassword;
    private AlertDialog loyalerDialog;

    private CheckBox autoLogin, rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(findViewById(R.id.toolbar), true, getString(R.string.login));

        mTextInputLayoutName = findViewById(R.id.textInputLayoutName);
        mTextInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);

        mEditTextName = findViewById(R.id.editTextName);
        mTextInputLayoutName.setErrorEnabled(true);
        mEditTextPassword = findViewById(R.id.editTextPassword);
        mTextInputLayoutPassword.setErrorEnabled(true);
        Button loginButton = findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(this);
        LinearLayout linearLayout = findViewById(R.id.down_del);
        linearLayout.setOnClickListener(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override/*内容要改变之前调用*/
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /*从start位置开始，count个字符（空字符是0）将被after个字符替换*/
            }

            @Override/*内容要改变时调用*/
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*说明在s字符串中，从start位置开始的count个字符刚刚取代了长度为before的旧文本*/

                if (!s.toString().matches(REGULAR_Length)){
                    //指定字符串的那一部分s.subSequence(0, 14)得到0到14位的字符
                    mEditTextName.setText(s.subSequence(0, 14));
                    //设置光标的位置
                    mEditTextName.setSelection(13);
                    Toast.makeText(LoginActivity.this,"请不要输入大于14位的字符",Toast.LENGTH_LONG).show();
                }
            }

            @Override/*内容要改变之后调用*/
            public void afterTextChanged(Editable s) {
                //这个方法被调用，那么说明s字符串的某个地方已经被改变。
                boolean start = false;
                if (s.length() == 0) {
                    start = true;
                }
                checkName(s.toString(), start);
            }
        };
        mEditTextName.addTextChangedListener(textWatcher);
        mEditTextPassword.addTextChangedListener(textWatcher);


        autoLogin = findViewById(R.id.autologCB);
        rememberPass = findViewById(R.id.jzpwdCB);
        SharedPreferences pef = getSharedPreferences("data", MODE_PRIVATE);
        if (pef.getBoolean("understate", false)) {
            mEditTextName.setText(pef.getString("user", ""));
            mEditTextPassword.setText(pef.getString("Password", ""));
            rememberPass.setChecked(true);
            if (pef.getBoolean("Passwords", false)) {
                autoLogin.setChecked(true);
            } else {
                autoLogin.setChecked(false);
            }
        }
        if (pef.getBoolean("antologon", false)) {
            autoLogin.setChecked(true);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("要立即登录吗？");
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", (dialogInterface, i) -> logNewApp());
            builder.show();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginBtn:
                hideSoftInput(v);
                if (!checkName(mEditTextName.getText(), true)) {
                    return;
                }
                if (!checkPwd(mEditTextPassword.getText(), true)) {
                    return;
                }
                logNewApp();
                break;
            case R.id.down_del:
                hideSoftInput(v);
                break;
            default:
        }
    }

    /**
     * 与服务器获取数据进行判断是否登陆成功
     * 登陆的方法
     */
    public void logNewApp() {
        service.execute(() -> {
            try {
                handler.post(() -> {
                    View view = LayoutInflater.from(this).inflate(R.layout.log_layout, null);
                    loyalerDialog = new AlertDialog.Builder(this).create();
                    loyalerDialog.setView(view);
                    loyalerDialog.show();
                    //获取Windo当前的窗口
                    final Window window = loyalerDialog.getWindow();
                    //设置窗口背景颜色透明
                    window.setBackgroundDrawable(new ColorDrawable(100));
                    //设置在窗口的边界之外触摸时是否取消此对话框,但是点返回键还是可以
                    //loyalerDialog.setCanceledOnTouchOutside(false);
                    //设置在窗口的边界之外触摸时是否取消此对话框,点返回键也没有用
                    loyalerDialog.setCancelable(false);

                });
                HashMap<String, Object> map = new HashMap<>(1);
                map.put("UserName", mEditTextName.getText().toString());
                map.put("UserPwd", mEditTextPassword.getText().toString());
                String JsonData = NetworkUtil.getJsonData(map, LOGIN_URL);
                JSONObject jsonObject = new JSONObject(JsonData);
                Log.d(TAG, "logNewApp: "+JsonData);
                String ok = "S";
                String result = "RESULT";
                String ROT = jsonObject.getString("UserRole");
                if (ok.equals(jsonObject.getString(result))) {
                    addData();
                    handler.post(() -> {
                        user = mEditTextName.getText().toString();
                        getAllUserName(ROT);
                    });

                } else {
                    handler.post(() -> {
                        loyalerDialog.dismiss();
                        Toast toast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
                        toast.setText("登陆失败");
                        toast.show();
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
                /**模拟网络延迟*/
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                handler.post(() -> {
                    Toast toast = Toast.makeText(LoginActivity.this, null, Toast.LENGTH_SHORT);
                    toast.setText("登陆失败账号或者密码错误");
                    toast.show();
                    loyalerDialog.dismiss();
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 本地存入数据
     */
    private void addData() {
        //.eedit返回对象
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("user", mEditTextName.getText().toString());
        editor.putString("Password", mEditTextPassword.getText().toString());
        editor.putBoolean("understate", rememberPass.isChecked());
        editor.putBoolean("antologon", autoLogin.isChecked());
        editor.putInt("state", 1);
        editor.apply();
    }

    /**
     * 在账号表单下面设置提示如果输入框为空
     */
    private boolean checkPwd(CharSequence pswd, boolean isLogin) {
        if (TextUtils.isEmpty(pswd)) {
            if (isLogin) {
                mTextInputLayoutPassword.setError(getString(R.string.error_pwd_empty));
                return false;
            }
        } else {
            mTextInputLayoutPassword.setError(null);
        }
        return true;
    }

    /**
     * 在密码表单下面设置提示如果输入框为空
     */
    private boolean checkName(CharSequence name, boolean isLogin) {
        if (TextUtils.isEmpty(name)) {
            if (isLogin) {
                mTextInputLayoutName.setError(getString(R.string.error_login_empty));
                return false;
            }
        } else {
            mTextInputLayoutName.setError(null);
        }
        return true;
    }

    /**
     * g根据账号的内容返回该账户的名字
     * 如果为管理员则将用户的所以信息存入数据库
     */
    private void getAllUserName(String ROT) {
        service.execute(() -> {
            try {
                HashMap map = new HashMap(1);
                map.put("UserName", "user1");
                String data = NetworkUtil.getJsonData(map, GetSUser);
                JSONObject jsonObjectDETAIL = new JSONObject(data);
                Log.d(TAG, "getAllUserName: "+data);
                JSONArray Detail = jsonObjectDETAIL.getJSONArray("ROWS_DETAIL");
                        if (!(RO1.equals(ROT))) {
                            int[] icon = new int[]{R.drawable.touxiang_1, R.drawable.touxiang_2};
                            SQLiteDatabase db = MyConnectSQL.initMySQL(this, "userAdmin", null, 1, 2).getWritableDatabase();
                            Cursor cursor = db.query("userData", null, null, null, null, null, null);
                            if (!cursor.moveToFirst()) {
                                for (int i = 0; i < Detail.length(); i++) {
                                    JSONObject userAll = Detail.getJSONObject(i);

                                    ContentValues values = new ContentValues();
                                    String username = userAll.getString("username");
                                    String pname = userAll.getString("pname");
                                    String ptel = userAll.getString("ptel");
                                    String datetime = userAll.getString("pregisterdate");
                                    String psex = userAll.getString("psex");
                                    values.put("UserName", username);
                                    values.put("AdminName", pname);
                                    values.put("Phone", ptel);
                                    values.put("Time", datetime);
                                    values.put("psex", psex);
                                    HashMap<String, String> ROTMap = new HashMap<>(1);
                                    ROTMap.put("UserName", username);
                                    ROTMap.put("UserPwd", "123456");
                                    String JsonData = NetworkUtil.getJsonData(ROTMap, LOGIN_URL);
                                    JSONObject ObjectROT = new JSONObject(JsonData);
                                    String roData = ObjectROT.getString("UserRole");
                                    switch (roData) {
                                        case RO1:
                                            values.put("Admin", "普通用户");
                                            break;
                                        case RO2:
                                            values.put("Admin", "一般管理员");
                                            break;
                                        case RO3:
                                            values.put("Admin", "超级管理员");
                                            break;
                                        default:
                                            values.put("Admin", "暂无相关数据（游客）");
                                    }
                                    if("女".equals(psex)){
                                        values.put("imgUri",icon[0]);
                                    }else {
                                        values.put("imgUri",icon[1]);
                                    }
                                    values.put("state",0);
                                    db.insert("userData", null, values);
                                }
                            }
                        }
                        String name ="";
                        for (int i=0;i<Detail.length();i++){
                            JSONObject dataObject = Detail.getJSONObject(i);
                            if (dataObject.getString("username").equals(mEditTextName.getText().toString())){
                                name = dataObject.getString("pname");
                            }
                        }
                String finalName = name;
                InitApp.getHandler().post(() -> {
                    loyalerDialog.dismiss();
                    Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
                    toast.setText("登陆成功");
                    toast.show();
                            Intent intent = new Intent();
                            intent.putExtra("User", finalName);
                            setResult(1, intent);
                            finish();
                        });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                loyalerDialog.dismiss();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivityForResult(new Intent(this, IPSetupActivity.class), 0);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * startActivityForResult跳转页面返回得到getResult()的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                break;
            default:
        }
    }


    /**
     * 隐藏键盘输入法
     *
     * @param view The view.
     */
    public static void hideSoftInput(final View view) {
        //获取InPutMethodManager的方法很简单
        InputMethodManager imm = (InputMethodManager) InitApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

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
        //noinspection ConstantConditions
        //切换软键盘
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    /**
     * 添加右上角网络设置
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.network_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
