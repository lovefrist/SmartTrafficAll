package com.lenovo.smarttraffic.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.myinterface.AdapterOnClick;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 用户侧滑删除效果
 *
 * @author asus
 */
public class SidestepAdapter extends RecyclerView.Adapter<SidestepAdapter.MyViewHandier> {
    private Context context;
    private ArrayList<HashMap<String,String>> dataList;
    private AdapterOnClick adapterOnClick;
    public SidestepAdapter(Context context,ArrayList<HashMap<String,String>> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public SidestepAdapter.MyViewHandier onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHandier(LayoutInflater.from(context).inflate(R.layout.sideslip_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHandier holder, int position) {
        HashMap<String,String> map = dataList.get(position);
        holder.iHeadPortrait.setImageResource(Integer.parseInt(map.get("imgUri")));
        holder.tUserName.setText(map.get("username"));
        holder.tAdminName.setText(map.get("pname"));
        holder.tPhone.setText(map.get("ptel"));
        holder.tTime.setText(map.get("datetime"));
        holder.tAdmin.setText(map.get("Admin"));
        holder.iHeadPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterOnClick.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHandier extends RecyclerView.ViewHolder {
        TextView tUserName, tAdminName, tPhone, tTime, tAdmin, tSeeDetails;
        ImageView iHeadPortrait;

        public MyViewHandier(View itemView) {
            super(itemView);
            tUserName = itemView.findViewById(R.id.tv_userName);
            tAdminName = itemView.findViewById(R.id.tv_adminName);
            tPhone = itemView.findViewById(R.id.tv_phone);
            tTime = itemView.findViewById(R.id.tv_time);
             tAdmin = itemView.findViewById(R.id.tv_admin);
            tSeeDetails = itemView.findViewById(R.id.tv_seeDetails);
            iHeadPortrait = itemView.findViewById(R.id.img_HeadPortrait);

        }
    }

    public void setOnClick(AdapterOnClick onClick){
        adapterOnClick = onClick;

    }


}
