package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.myinterface.UserListener;
import com.lenovo.smarttraffic.ui.adapter.DialogAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * 地铁中心的活动页面
 *
 * @author asus
 */
public class MetroActivity extends BaseActivity {
    private static final String METRO_URI = "GetMetroInfo.do";
    @BindView(R.id.et_start)
    EditText eStart;
    @BindView(R.id.et_end)
    EditText eEnd;
    @BindView(R.id.btn_query)
    Button bQuery;
    private ArrayList<TextView> textViewArrayList = new ArrayList<>();
    private ArrayList<TextView> arrayListDialog = new ArrayList<>();
    private ArrayList<String> metroDataList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();
    private AlertDialog dialog;
    private AlertDialog dialogMetro1;
    private ArrayList<DialogAdapter> dialogArrayList = new ArrayList<>();
    private ArrayList<TextView> textList = new ArrayList<>();
    private TextView textView;
    private DialogAdapter adapter;
    private boolean status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initToolBar(findViewById(R.id.toolbar), true, "地铁中心");
        initView();
        initData();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_metro;
    }



    private void initView() {
        addListText();
        dialog = new AlertDialog.Builder(this).create();
        View view = LayoutInflater.from(this).inflate(R.layout.yuan_layout, null);
        bQuery.setBackgroundColor(Color.parseColor("#cccccc"));
        bQuery.setEnabled(false);
        bQuery.setOnClickListener(v ->{
            Intent intent = new Intent(this,MetroAdviceActivity.class);
            intent.putExtra("start",eStart.getText().toString());
            intent.putExtra("end",eEnd.getText().toString());
            startActivity(intent);
        });
        dialog.setView(view);
        addListDialog(view);
//        setFocusableInTouchMode
        eStart.setFocusableInTouchMode(false);
        eStart.setOnClickListener(v -> {
            status = true;
            dialog.show();
        });
        eEnd.setFocusableInTouchMode(false);
        eEnd.setOnClickListener(v -> {
            status = false;
            dialog.show();
        });

        DialogAdapter.getMetro(position -> {
            if (status){
                eStart.setText(metroDataList.get(position));
            }else {
                eEnd.setText(metroDataList.get(position));
            }

            dialogMetro1.dismiss();
            if (eEnd.getText()!=null&&eStart.getText()!=null){
                bQuery.setEnabled(true);
                bQuery.setBackgroundColor(Color.parseColor("#159ad4"));
            }else {
                bQuery.setBackgroundColor(Color.parseColor("#cccccc"));
                bQuery.setEnabled(false);

            }
            if (eEnd.getText().toString().equals(eStart.getText().toString())){
              Toast toast =  Toast.makeText(this,null,Toast.LENGTH_LONG);
              toast.setText("起点和终点一样建议你走路");
              toast.show();
                bQuery.setBackgroundColor(Color.parseColor("#cccccc"));
              bQuery.setEnabled(false);

            }
        });
    }

    private void initData() {
        for (int i = 0; i < textViewArrayList.size(); i++) {
            int finalI1 = i;
            textViewArrayList.get(i).setOnClickListener(v -> {
                Intent intent = new Intent(this, MetroLineActivity.class);
                switch (finalI1){
                    case 3:
                        intent.putExtra("number", 5);
                        break;
                    case 4:
                        intent.putExtra("number", 6);
                        break;
                    case 5:
                        intent.putExtra("number", 4);
                        break;
                    case 6:
                        intent.putExtra("number", 8);
                        break;
                    case 7:
                        intent.putExtra("number", 7);
                        break;
                    default:
                        intent.putExtra("number", (finalI1 + 1));
                        break;
                }
                startActivity(intent);
            });

        }
    }

    private void addListText() {
        textViewArrayList.add(findViewById(R.id.tv_Route1));
        textViewArrayList.add(findViewById(R.id.tv_Route2));
        textViewArrayList.add(findViewById(R.id.tv_Route3));
        textViewArrayList.add(findViewById(R.id.tv_Route4));
        textViewArrayList.add(findViewById(R.id.tv_Route5));
        textViewArrayList.add(findViewById(R.id.tv_Route6));
        textViewArrayList.add(findViewById(R.id.tv_Route7));
        textViewArrayList.add(findViewById(R.id.tv_Route8));
    }

    private void addListDialog(View view) {
        View view1 = LayoutInflater.from(this).inflate(R.layout.layout_dialog, null);
        RecyclerView recyclerView = view1.findViewById(R.id.rv_dialog);
        textView = view1.findViewById(R.id.tv_title);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new DialogAdapter(this, metroDataList, hashMaps);
        recyclerView.setAdapter(adapter);

        dialogMetro1 = new AlertDialog.Builder(this, R.style.FullActivity).create();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogMetro1.getWindow().setAttributes(attributes);
        dialogMetro1.setView(view1);

        arrayListDialog.add(view.findViewById(R.id.tv_Route1));
        arrayListDialog.add(view.findViewById(R.id.tv_Route2));
        arrayListDialog.add(view.findViewById(R.id.tv_Route3));
        arrayListDialog.add(view.findViewById(R.id.tv_Route4));
        arrayListDialog.add(view.findViewById(R.id.tv_Route5));
        arrayListDialog.add(view.findViewById(R.id.tv_Route6));
        arrayListDialog.add(view.findViewById(R.id.tv_Route7));
        arrayListDialog.add(view.findViewById(R.id.tv_Route8));

        for (int i = 0; i < arrayListDialog.size(); i++) {
            int finalI = i;
            arrayListDialog.get(i).setOnClickListener(v -> {
                getMetroInfo(finalI);
                dialog.dismiss();
                dialogMetro1.show();
            });
        }

    }

    private void getMetroInfo(int index) {
        service.execute(() -> {
            metroDataList.clear();
            hashMaps.clear();
            int newIndex = 0;
            switch (index){
                case 3:
                    newIndex = 5;
                    break;
                case 4:
                    newIndex =6;
                    break;
                case 5:
                    newIndex =4;
                    break;
                case 6:
                    newIndex =8;
                    break;
                case 7:
                    newIndex =7;
                    break;
                default:
                    newIndex =index+1;
                    break;
            }
            HashMap<String, Object> hashMap = new HashMap<>(2);
            hashMap.put("Line", newIndex);
            hashMap.put("UserName", "user1");
            ArrayList<String> list = new ArrayList<>();
            try {
                String MetroInfo = getJsonData(hashMap, METRO_URI);
                JSONObject metro = new JSONObject(MetroInfo);
                JSONArray metroArray = metro.getJSONArray("ROWS_DETAIL");
                JSONObject metroData = metroArray.getJSONObject(0);
                JSONArray dataArray = metroData.getJSONArray("sites");
                String name = metroData.getString("name");
                InitApp.getHandler().post(() -> {
                    Log.d(TAG, "getMetroInfo: " + textView);
                    textView.setText(name + "路图");
                });
                for (int i = 0; i < dataArray.length(); i++) {
                    String map = dataArray.optString(i);
                    metroDataList.add(map);
                }
                JSONArray transferData = metroData.getJSONArray("transfersites");
                for (int i = 0; i < transferData.length(); i++) {
                    list.add(transferData.optString(i));
                }
                hashMap.clear();
                hashMap.put("Line", 0);
                hashMap.put("UserName", "user1");
                String allMetro = getJsonData(hashMap, METRO_URI);
                JSONObject allObject = new JSONObject(allMetro);
                JSONArray allArray = allObject.getJSONArray("ROWS_DETAIL");


                for (int i = 0; i < allArray.length(); i++) {

                    if ((i+1)!= newIndex) {
                        JSONObject indexObject = allArray.getJSONObject(i);
                        JSONArray sitesArray = indexObject.getJSONArray("sites");
                        for (int j = 0; j < sitesArray.length(); j++) {
                            for (int k = 0; k < list.size(); k++) {
                                if (list.get(k).equals(sitesArray.optString(j))) {
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("place", list.get(k));
                                    map.put("Route", indexObject.getString("name"));
                                    hashMaps.add(map);
                                    break;
                                }
                            }
                        }
                    }
                }
                InitApp.getHandler().post(() -> {
                    Log.d(TAG, "getMetroInfo: "+hashMaps.toString());
                    adapter.notifyDataSetChanged();
                });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
