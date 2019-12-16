package com.lenovo.smarttraffic.ui.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.entityclass.UserInfo;
import com.lenovo.smarttraffic.myinterface.UserListener;
import com.lenovo.smarttraffic.myview.MyRealerViewSidesteptow;
import com.lenovo.smarttraffic.ui.activity.UserAdminActivity;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 用户收藏页面的Adapter
 *
 * @author asus
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyCollectionViewHandier> {
    private Context context;
    private ArrayList<UserInfo> data;
    private static UserListener listener;

    public CollectionAdapter(Context context, ArrayList<UserInfo> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public CollectionAdapter.MyCollectionViewHandier onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCollectionViewHandier(LayoutInflater.from(context).inflate(R.layout.collection_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCollectionViewHandier holder, int position) {
        UserInfo info = data.get(position);

        holder.iHeadPortrait.setImageResource(info.getImgUri());
        holder.tUserName.setText("用户名：" + info.getUsername());
        holder.tAdminName.setText("姓名" + info.getPname());
        holder.tPhone.setText("电话" + info.getPtel());
        holder.tTime.setText(info.getDatetime());
        holder.tAdmin.setText(info.getAdmin());
        holder.tRoof.setText(info.getStateTop() == 1 ? "取消置顶" : "置顶");
        holder.linearLayout.setBackgroundColor(Color.parseColor(info.getStateTop() == 1?"#cccccc":"#ffffff"));
        holder.tRoof.setOnClickListener(v -> {
            holder.scrollitem.closeDown();
            info.setStateTop(info.getStateTop() == 0 ? 1 : 0);
            ContentValues values = new ContentValues();
            values.put("StateTop", info.getStateTop());
            //            UserAdminActivity.db.update("userData", values, "UserName = '" + info.getUsername() + "'", null);
            Collections.sort(data, (o1, o2) -> o2.getStateTop() - o1.getStateTop());

            notifyDataSetChanged();
        });
        holder.tCollect.setOnClickListener(v -> {
            holder.scrollitem.closeDown();
            ContentValues values = new ContentValues();
            values.put("state", 0);
            UserAdminActivity.db.update("userData", values, "UserName ='" + info.getUsername() + "'", null);
            data.remove(position);
            notifyItemRemoved(position);
        });

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyCollectionViewHandier extends RecyclerView.ViewHolder {
        TextView tUserName, tAdminName, tPhone, tTime, tAdmin, tSeeDetails, tCollect, tRoof;
        ImageView iHeadPortrait;
        MyRealerViewSidesteptow scrollitem;
        LinearLayout linearLayout;
        public MyCollectionViewHandier(View itemView) {
            super(itemView);
            tUserName = itemView.findViewById(R.id.tv_userName);
            tAdminName = itemView.findViewById(R.id.tv_adminName);
            tPhone = itemView.findViewById(R.id.tv_phone);
            tTime = itemView.findViewById(R.id.tv_time);
            tAdmin = itemView.findViewById(R.id.tv_admin);
            tSeeDetails = itemView.findViewById(R.id.tv_seeDetails);
            iHeadPortrait = itemView.findViewById(R.id.img_HeadPortrait);
            tCollect = itemView.findViewById(R.id.tv_collect);
            scrollitem = itemView.findViewById(R.id.scroll_item);
            tRoof = itemView.findViewById(R.id.tv_roof);
            linearLayout = itemView.findViewById(R.id.content_layout);
        }
    }

}
