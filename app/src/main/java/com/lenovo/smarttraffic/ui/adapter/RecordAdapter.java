package com.lenovo.smarttraffic.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.entityclass.RecordInfo;
import com.lenovo.smarttraffic.ui.activity.PaymentActivity;

import java.util.ArrayList;

/**
 * 违章详情的查询页面的Adapter
 * @author asus
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHandier> {

   private Context context;
   private ArrayList<RecordInfo> arrayList;
    public RecordAdapter(Context context, ArrayList<RecordInfo> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public RecordAdapter.RecordViewHandier onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecordViewHandier(LayoutInflater.from(context).inflate(R.layout.record_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHandier holder, int position) {
        RecordInfo info = arrayList.get(position);
        holder.tSerial.setText(position+1+"");
        holder.iLogos.setImageResource(info.getImgUri());
        holder.tCarId.setText(info.getCarNumber());
        holder.tPlace.setText(info.getPaddr());
        holder.tReason.setText("\u3000\u3000\u3000"+info.getPremarks());
        holder.tDeduction.setText(info.getPscore()==0?"无":info.getPscore()+"");
        holder.tFine.setText(info.getPmoney()==0?"无":info.getPmoney()+"");
        holder.tTime.setText(info.getPdate());
        if (info.getNumber()==0){
            holder.tStatus.setText("未处理");
            holder.tStatus.setTextColor(info.getPmoney()==0?Color.parseColor("#cccccc"):Color.parseColor("#ff0000"));
        }else {
            holder.tStatus.setText("已处理");
            holder.tStatus.setTextColor(Color.GREEN);
            holder.tStatus.setEnabled(false);
        }
        holder.tStatus.setEnabled(info.getPmoney()!=0);
        holder.tStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PaymentActivity.class);
                intent.putExtra("record",info);
                intent.putExtra("position",position);
                ((Activity)context).startActivityForResult(intent,0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    class RecordViewHandier extends RecyclerView.ViewHolder {
        TextView tSerial,tCarId,tPlace,tReason,tDeduction,tFine,tTime,tStatus;
        ImageView iLogos;
        public RecordViewHandier(View itemView) {
            super(itemView);
            tSerial = itemView.findViewById(R.id.tv_serial);
            tCarId = itemView.findViewById(R.id.tv_carId);
            tPlace = itemView.findViewById(R.id.tv_place);
            tReason = itemView.findViewById(R.id.tv_Reason);
            tDeduction = itemView.findViewById(R.id.tv_Deduction);
            tFine = itemView.findViewById(R.id.tv_fine);
            tStatus = itemView.findViewById(R.id.tv_status);
            tTime = itemView.findViewById(R.id.tv_time);
            iLogos = itemView.findViewById(R.id.iv_logos);
       }
    }
}
