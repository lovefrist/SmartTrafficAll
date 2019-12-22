package com.lenovo.smarttraffic.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.entityclass.ReachInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * etc充值查询的适配器
 * @author asus
 */
public class EtcAdapter  extends RecyclerView.Adapter<EtcAdapter.MyViewHandier> {
    private Context context;
    private ArrayList<ReachInfo> reachIfs;
    public EtcAdapter(Context context, ArrayList<ReachInfo> reachIfs){
        this.context = context;
        this.reachIfs = reachIfs;
    }
    @NonNull
    @Override
    public EtcAdapter.MyViewHandier onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHandier(LayoutInflater.from(context).inflate(R.layout.etc_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHandier holder, int position) {
        if(reachIfs.size()!=0){
            if (position!=0){
                ReachInfo reachInfo = reachIfs.get(position-1);
                holder.tNumber.setText(position+"");
                holder.tCarNumber.setText(reachInfo.getCarNumber());
                holder.tMoney.setText(reachInfo.getMoney());
                holder.tUser.setText(reachInfo.getUser());
                holder.tReachTime.setText(reachInfo.getReachTime());
            }
        }
    }

    @Override
    public int getItemCount() {
        return reachIfs.size()+1;
    }

    class MyViewHandier extends RecyclerView.ViewHolder {
        TextView tNumber,tCarNumber,tMoney,tUser,tReachTime;
        public MyViewHandier(View itemView) {
            super(itemView);
            tNumber = itemView.findViewById(R.id.tv_number);
            tCarNumber = itemView.findViewById(R.id.tv_carNumber);
            tMoney = itemView.findViewById(R.id.tv_money);
            tUser = itemView.findViewById(R.id.tv_user);
            tReachTime = itemView.findViewById(R.id.tv_reachTime);
        }
    }
}
