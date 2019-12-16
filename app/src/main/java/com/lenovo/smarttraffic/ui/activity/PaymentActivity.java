package com.lenovo.smarttraffic.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.entityclass.RecordInfo;
import com.lenovo.smarttraffic.util.MyUtil;

import java.lang.annotation.ElementType;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import butterknife.BindView;

/**
 * 违章支付页面
 *
 * @author asus
 */
public class PaymentActivity extends BaseActivity {
    @BindView(R.id.iv_QRData)
    ImageView iQRData;
    private boolean QRKey = true;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        initToolBar(findViewById(R.id.toolbar),true,"违章查询");
        Intent intent = getIntent();
        RecordInfo recordInfo =intent.getParcelableExtra("record");
        position = intent.getIntExtra("position",-1);

        service.execute(()->{
            while (QRKey){

                Bitmap bitmap =MyUtil.createBitmap("测试",500,500,"UTF-8","Q","1", Color.BLACK, Color.WHITE);
                InitApp.getHandler().post(()->{
                    iQRData.setImageBitmap(bitmap);
                });

                SystemClock.sleep(3000);
            }
        });
        iQRData.setOnLongClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(PaymentActivity.this).create();
            dialog.setCanceledOnTouchOutside(false);
            View view = LayoutInflater.from(this).inflate(R.layout.payment_dialog,null);
            dialog.setView(view);
            dialog.show();
            ImageView imageView =view.findViewById(R.id.iv_carMame);
            imageView.setImageResource(recordInfo.getImgUri());
            TextView tCarNumber =view.findViewById(R.id.tv_carNumber);
            tCarNumber.setText("车票号"+recordInfo.getCarNumber());
            TextView pmoney = view.findViewById(R.id.pmoney);
            pmoney.setText("罚款"+recordInfo.getPmoney());
            TextView tPscore = view.findViewById(R.id.tv_pscore);
            tPscore.setText("扣分"+recordInfo.getPscore());
            TextView tPaddr = view.findViewById(R.id.tv_paddr);
            tPaddr.setText("地点"+recordInfo.getPaddr());
            TextView tPdate = view.findViewById(R.id.tv_pdate);
            tPdate.setText(recordInfo.getPdate().replace("\n",""));

            Button bPay = view.findViewById(R.id.bt_pay);
            Button bDelete = view.findViewById(R.id.bt_dealer);
            bPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QRKey = false;
                    dialog.dismiss();
                    View view1 = LayoutInflater.from(PaymentActivity.this).inflate(R.layout.log_layout,null);
                    TextView textView = view1.findViewById(R.id.tv_content);
                    textView.setText("支付中");
                    Toast toast  = Toast.makeText(PaymentActivity.this,null,Toast.LENGTH_LONG);
                    toast.setView(view1);
                    showMyToast(toast,3000);
                }
            });
            bDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            return true;
        });
    }

    public void showMyToast(final Toast toast, final int cnt) {
        final Timer timer =new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        },0,3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_payment;
    }

    @Override
    public void onBackPressedSupport() {
        Intent intent = new Intent();
        if (QRKey){
            intent.putExtra("position",position);
            intent.putExtra("state",0);
            QRKey = false;
        }else {
            intent.putExtra("position",position);
            intent.putExtra("state",1);
        }
        setResult(2,intent);
        finish();
    }
}
