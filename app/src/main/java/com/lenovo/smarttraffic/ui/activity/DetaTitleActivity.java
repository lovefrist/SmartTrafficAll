package com.lenovo.smarttraffic.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lenovo.smarttraffic.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

/**
 * 2019年国赛第三题
 * 详情页面
 * @author asus
 */
public class DetaTitleActivity extends BaseActivity {
    private static final String IMG_URL = "http://192.168.3.5:8088/transportservice";
    @BindView(R.id.tv_tbyLayoutTitle)
    TextView tTbyLayoutTitle;
    @BindView(R.id.tv_tbyLayoutContent)
    TextView tTbyLayoutContent;
    @BindView(R.id.tv_fgDate)
    TextView tFgDate;
    @BindView(R.id.iv_viewpager)
    ImageView imViewpager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String title = intent.getExtras().getString("title");
        String tblayoutTitle = intent.getExtras().getString("tblaycontentoutTitle");
        String data = intent.getExtras().getString("date");
        String content = intent.getExtras().getString("content");
        String imgUri = intent.getExtras().getString("imgUri");
        initToolBar(findViewById(R.id.toolbar),true,title);
        tTbyLayoutTitle.setText("分类"+tblayoutTitle);
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(data, position);
        String time = new SimpleDateFormat("yyyy年MM月dd日 HH点mm分").format(dateValue);
        tFgDate.setText("时间: "+time);
        tTbyLayoutContent.setText("\u3000\u3000"+content);
        if (!imgUri.equals("")){
            Glide.with(this).load(IMG_URL+imgUri).into(imViewpager);
        }


    }

    @Override
    protected int getLayout() {
        return R.layout.activity_deta_title;
    }
}
