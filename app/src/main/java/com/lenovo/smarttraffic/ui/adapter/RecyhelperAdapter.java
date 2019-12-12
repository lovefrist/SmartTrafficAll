package com.lenovo.smarttraffic.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.myinterface.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 作用添加咨询页面的Adapter
 *
 * @author asus
 */
public class RecyhelperAdapter extends RecyclerView.Adapter<RecyhelperAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> list;
    private boolean ID;
    private OnItemClickListener mOnItemClickListener;
    private static final String TAG = "RecyhelperAdapter";
    List<ImageView> imageViewList = new ArrayList<>();
    private boolean api = false;

    public RecyhelperAdapter(Context context, ArrayList<String> list, boolean ID) {
        this.context = context;
        this.list = list;
        this.ID = ID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_textadapder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (ID) {
            if (api){
                if (position!=0){
                    holder.iMacs.setVisibility(View.VISIBLE);
                }else{
                    holder.iMacs.setVisibility(View.GONE);
                }
            }else {
                holder.iMacs.setVisibility(View.GONE);
            }
            holder.iMacs.setImageResource(R.drawable.minus);
            holder.textView.setText(list.get(position));
            holder.relativeLayout.setOnLongClickListener(v -> {
                Log.d(TAG, "onBindViewHolder: " + imageViewList.size());
                mOnItemClickListener.longOnClick();
                return false;
            });

            if (mOnItemClickListener != null) {
                holder.iMacs.setOnClickListener(v -> mOnItemClickListener.onClick(position));
            }
        } else {
            if (api){
                    holder.iMacs.setVisibility(View.VISIBLE);
            }else {
                holder.iMacs.setVisibility(View.GONE);
            }
            holder.iMacs.setImageResource(R.drawable.add2);
            holder.textView.setText(list.get(position));
            if (mOnItemClickListener != null) {
                holder.iMacs.setOnClickListener(v -> mOnItemClickListener.onClick(position));
            }
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void set(boolean api){
        this.api = api;
        notifyDataSetChanged();

    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView iMacs;
        RelativeLayout relativeLayout;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_textHander);
            iMacs = itemView.findViewById(R.id.iv_macs);
            relativeLayout = itemView.findViewById(R.id.relative_layout);
        }

    }

    public void getAllonClick(OnItemClickListener OnItemClickListener) {
        mOnItemClickListener = OnItemClickListener;
    }

    public void setAllViewVisibility(int data) {
        Log.d(TAG, "setAllViewVisibility: " + imageViewList.size());
        for (int i = 0; i < imageViewList.size(); i++) {
            imageViewList.get(i).setVisibility(data);

        }
//        notifyDataSetChanged();
    }

}

