package com.lenovo.smarttraffic.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;

import java.util.ArrayList;

/**
 * 地铁线路的
 * 适配器
 *
 * @author asus
 */
public class MetroAdapter extends RecyclerView.Adapter<MetroAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> arrayList;
    public MetroAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MetroAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.metro_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tContent.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tContent;
        public MyViewHolder(View itemView) {
            super(itemView);
            tContent = itemView.findViewById(R.id.tv_content);
        }
    }
}
