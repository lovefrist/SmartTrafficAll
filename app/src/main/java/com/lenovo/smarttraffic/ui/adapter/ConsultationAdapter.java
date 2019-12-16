package com.lenovo.smarttraffic.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.myinterface.ClickItemlistener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 2019国赛第四题
 * 设置页面内容咨询的适配器
 *
 * @author asus
 */
public class ConsultationAdapter extends RecyclerView.Adapter {
    private static final String IMG_URL = "http://192.168.3.5:8088/transportservice";
    private static final String TAG = "ConsultationAdapter";

    private Context context;
    private static ClickItemlistener clickItemlistener;

    private ArrayList<HashMap<String, String>> hashMapList;

    public ConsultationAdapter(Context context, ArrayList<HashMap<String, String>> hashMapList) {
        this.context = context;
        this.hashMapList = hashMapList;
    }

    /**
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position(将新View绑定到适配器位置后将添加到其中的ViewGroup。).
     * @param viewType The view type of the new View(新视图的视图类型).
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new NoImagesViewHolder(LayoutInflater.from(context).inflate(R.layout.consultation_layout, parent, false));
        } else {
            return new YesImagesViewHolder(LayoutInflater.from(context).inflate(R.layout.consultationinimages_layout, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HashMap map = hashMapList.get(position);
        if (map.get("imgUri") == null) {
            if (holder instanceof NoImagesViewHolder){
            ((NoImagesViewHolder) holder).tConnectNo.setText(map.get("content").toString());
            ((NoImagesViewHolder) holder).tTitleNo.setText(map.get("title").toString());
            ((NoImagesViewHolder) holder).tDate.setText(map.get("createTime").toString());
            ((NoImagesViewHolder) holder).linearLayoutNo.setOnClickListener(v -> {
                clickItemlistener.onClick(map);
            });
            }
        } else {
            if (holder instanceof YesImagesViewHolder) {
                Glide.with(context).load(IMG_URL + map.get("imgUri")).into(((YesImagesViewHolder) holder).iRepresentative);
                ((YesImagesViewHolder) holder).tConnectYes.setText(map.get("content").toString());
                ((YesImagesViewHolder) holder).tTitleYes.setText(map.get("title").toString());
                ((YesImagesViewHolder) holder).tTime.setText(map.get("createTime").toString());
                ((YesImagesViewHolder) holder).linearLayoutYes.setOnClickListener(v -> {
                    clickItemlistener.onClick(map);
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return hashMapList.size();

    }

    /**
     * position position to query(查询位置)
     *
     * @return integer value identifying the type of the view needed to represent the item at
     * <code>position</code>. Type codes need not be contiguous.(返回项目的所需的视图的类型)
     */
    @Override
    public int getItemViewType(int position) {
        if (hashMapList.get(position).get("imgUri") == null) {
            return 0;
        } else {
            return 1;
        }
    }

    class YesImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView iRepresentative;
        TextView tTitleYes, tConnectYes, tTime;
        LinearLayout linearLayoutYes;

        YesImagesViewHolder(View itemView) {
            super(itemView);
            iRepresentative = itemView.findViewById(R.id.img_representative);
            tTitleYes = itemView.findViewById(R.id.text_titleYes);
            tConnectYes = itemView.findViewById(R.id.text_connectYes);
            tTime = itemView.findViewById(R.id.text_time);
            linearLayoutYes = itemView.findViewById(R.id.linear_layout);
        }
    }


    class NoImagesViewHolder extends RecyclerView.ViewHolder {
        TextView tTitleNo, tDate, tConnectNo;
        LinearLayout linearLayoutNo;

        NoImagesViewHolder(View itemView) {
            super(itemView);
            tTitleNo = itemView.findViewById(R.id.text_titleNo);
            tDate = itemView.findViewById(R.id.text_date);
            tConnectNo = itemView.findViewById(R.id.text_connectNo);
            linearLayoutNo = itemView.findViewById(R.id.linear_layout);
        }
    }

    public static void getClick(ClickItemlistener itmeInter) {
        clickItemlistener = itmeInter;
    }
}


