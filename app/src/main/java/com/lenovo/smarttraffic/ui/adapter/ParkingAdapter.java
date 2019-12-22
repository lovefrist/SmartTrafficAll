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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.entityclass.ParkInfo;
import com.lenovo.smarttraffic.ui.activity.ParkDetailsActivity;

import java.util.ArrayList;

/**
 * 停车场信息的适配器
 *
 * @author asus
 */

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.MyViewHandier> {
    private Context context;
    private ArrayList<ParkInfo> parkInfoArrayList;

    public ParkingAdapter(Context context, ArrayList<ParkInfo> parkInfoArrayList) {
        this.context = context;
        this.parkInfoArrayList = parkInfoArrayList;

    }

    @NonNull
    @Override
    public ParkingAdapter.MyViewHandier onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHandier(LayoutInflater.from(context).inflate(R.layout.parking_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHandier holder, int position) {
        ParkInfo parkInfo = parkInfoArrayList.get(position);
        holder.tPosition.setText((position+1) + "");
        holder.tContentPark.setText(parkInfo.getTitleName() + "\n空闲车位" + parkInfo.getEmptySpace() + "个， 停车费" + parkInfo.getRate() + "元/小时\n" + parkInfo.getAddress() + (parkInfo.getOpen() == 1 ? parkInfo.getDistance() + " m" : ""));
        holder.jump.setVisibility(parkInfo.getEmptySpace()==0? View.GONE:parkInfo.getOpen() == 1 ? View.VISIBLE : View.GONE);
        holder.tStatus.setVisibility(parkInfo.getOpen() == 1 ? parkInfo.getEmptySpace()==0?View.VISIBLE:View.GONE: View.VISIBLE);
        holder.jump.setOnClickListener(v -> {
            Intent intent = new Intent(context, ParkDetailsActivity.class);
            intent.putExtra("ParkInfo",parkInfo);
            ((Activity)context).startActivityForResult(intent,0);
        });
        holder.tStatus.setText(parkInfo.getOpen()==0?"关闭":"已满");
        holder.linearLayout.setBackgroundColor(parkInfo.getOpen()==1?parkInfo.getEmptySpace()==0?Color.LTGRAY:Color.WHITE:Color.LTGRAY);

    }

    @Override
    public int getItemCount() {
        return parkInfoArrayList.size();
    }

    class MyViewHandier extends RecyclerView.ViewHolder {
        TextView tPosition, tContentPark, tStatus;
        ImageView jump;
        LinearLayout linearLayout;

        public MyViewHandier(View itemView) {
            super(itemView);
            tPosition = itemView.findViewById(R.id.tv_position);
            tContentPark = itemView.findViewById(R.id.tv_contentPark);
            tStatus = itemView.findViewById(R.id.tv_status);
            jump = itemView.findViewById(R.id.iv_jump);
            linearLayout = itemView.findViewById(R.id.linear_layout);
        }
    }
}
