package com.lenovo.smarttraffic.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.myinterface.UserListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 给线路选择的字段设置
 * 适配器
 *
 * @author asus
 */
public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.MyViewHolder> {

    private static UserListener metroListener;
    private Context context;
    private ArrayList<String> metroDataList;
    private ArrayList<HashMap<String, String>> hashMaps;

    public DialogAdapter(Context context, ArrayList<String> metroDataList, ArrayList<HashMap<String, String>> hashMaps) {
        this.context = context;
        this.metroDataList = metroDataList;
        this.hashMaps = hashMaps;
    }

    public static void getMetro(UserListener listener) {
        metroListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.dialog_recyley_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        boolean key = true;
        for (int i = 0; i < hashMaps.size(); i++) {
            if (hashMaps.get(i).get("place").equals(metroDataList.get(position))) {
                holder.tTransfer.setText(hashMaps.get(i).get("Route"));
                key = false;
                break;
            }
        }
        if (key) {
            holder.tTransfer.setVisibility(View.INVISIBLE);
        } else {
            holder.tTransfer.setVisibility(View.VISIBLE);
        }
        holder.tTextRoute.setText(metroDataList.get(position));
        holder.tTextRoute.setOnClickListener(v -> {
            metroListener.clareOnClick(position);
        });
        holder.tTextRoute1.setOnClickListener(v -> {
            metroListener.clareOnClick(position);
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return metroDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tTransfer, tTextRoute, tTextRoute1;

        MyViewHolder(View itemView) {
            super(itemView);
            tTextRoute = itemView.findViewById(R.id.tv_textRoute);
            tTransfer = itemView.findViewById(R.id.tv_Transfer);
            tTextRoute1 = itemView.findViewById(R.id.tv_textRoute1);

        }
    }
}
