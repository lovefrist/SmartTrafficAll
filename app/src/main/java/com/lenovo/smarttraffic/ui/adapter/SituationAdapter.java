package com.lenovo.smarttraffic.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;

import java.util.List;
import java.util.Map;

/**
 *
 * @time 2019/11/3 10:12
 * @author asus
 */
public class SituationAdapter extends RecyclerView.Adapter<SituationAdapter.MyHolder> {
    private Context context;
    private List<Map> mapList;
    private static final String TAG = "SituationAdapter";
    /**
     * @param context  传进来的容器
     * @param mapList   传起来的list
     */
    public SituationAdapter(Context context, List<Map> mapList){
        this.context = context;
        this.mapList = mapList;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.weather_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.timed.setText(mapList.get(position).get("time").toString());
        holder.tWeek.setText(mapList.get(position).get("week").toString());
        holder.tWeather.setText(mapList.get(position).get("type").toString());
        holder.ivRvp.setImageResource(Integer.parseInt(mapList.get(position).get("src").toString()));
    }

    @Override
    public int getItemCount() {

        return mapList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        TextView timed, tWeek,tWeather;
        ImageView ivRvp;
        MyHolder(View itemView) {
            super(itemView);
            timed = itemView.findViewById(R.id.tv_timedd);
            tWeek = itemView.findViewById(R.id.tv_weeder);
            tWeather = itemView.findViewById(R.id.tv_weather);
            ivRvp = itemView.findViewById(R.id.iv_rvp);
        }
    }
}
