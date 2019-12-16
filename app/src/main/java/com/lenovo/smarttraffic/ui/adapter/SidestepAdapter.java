package com.lenovo.smarttraffic.ui.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lenovo.smarttraffic.R;
import com.lenovo.smarttraffic.entityclass.UserInfo;
import com.lenovo.smarttraffic.myview.MyRealerViewSidestep;
import com.lenovo.smarttraffic.ui.activity.CollectionActivity;
import com.lenovo.smarttraffic.ui.activity.RecordActivity;
import com.lenovo.smarttraffic.ui.activity.UserAdminActivity;

import java.util.ArrayList;

/**
 * 用户侧滑删除效果
 *
 * @author asus
 */
public class SidestepAdapter extends RecyclerView.Adapter<SidestepAdapter.MyViewHandier> {
    private Context context;
    private ArrayList<UserInfo> dataList;

    public SidestepAdapter(Context context,ArrayList<UserInfo> dataList) {
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
        UserInfo info = dataList.get(position);
         info.getState();
        setData(holder,info);
        holder.tCollect.setText(info.getState().equals("1")?"已收藏":"收藏");
        holder.tCollect.setOnClickListener(v -> {
            if (holder.tCollect.getText().equals("收藏")){
                ContentValues values = new ContentValues();
                values.put("state",1);
                UserAdminActivity.db.update("userData",values,"UserName ='"+info.getUsername()+"'",null);
                holder.tCollect.setText("已收藏");
            }else {
                Toast.makeText(context,"收藏过了不用收藏",Toast.LENGTH_LONG).show();
            }
        });
        holder.tSeeDetails.setOnClickListener(v1 -> {
            Intent intent = new Intent(context,CollectionActivity.class);
            context.startActivity(intent);
        });
        holder.iHeadPortrait.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecordActivity.class);
            intent.putExtra("user",info);
            context.startActivity(intent);
        });

    }

    /**
     * 取消重复代码
     * 作用存入数据
     * */
    private void setData(MyViewHandier holder,UserInfo info){


        holder.iHeadPortrait.setImageResource(info.getImgUri());
        holder.tUserName.setText("用户名："+info.getUsername());
        holder.tAdminName.setText("姓名"+info.getPname());
        holder.tPhone.setText("电话"+info.getPtel());
        holder.tTime.setText(info.getDatetime());
        holder.tAdmin.setText(info.getAdmin());
        Intent intent = new Intent();
        intent.putExtra("use",dataList.get(1));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /**
     * 用于标识在* <code> position </ code>处表示该项目所需的视图类型
     * 如果传入的是固定值那么视图将复用不创建
     * 如果传入的不是固定的那么视图将有行就创多少行
     * */
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHandier extends RecyclerView.ViewHolder {
        TextView tUserName, tAdminName, tPhone, tTime, tAdmin, tSeeDetails,tCollect;
        ImageView iHeadPortrait;
        MyRealerViewSidestep scrollitem;
        public MyViewHandier(View itemView) {
            super(itemView);
            tUserName = itemView.findViewById(R.id.tv_userName);
            tAdminName = itemView.findViewById(R.id.tv_adminName);
            tPhone = itemView.findViewById(R.id.tv_phone);
            tTime = itemView.findViewById(R.id.tv_time);
             tAdmin = itemView.findViewById(R.id.tv_admin);
            tSeeDetails = itemView.findViewById(R.id.tv_seeDetails);
            iHeadPortrait = itemView.findViewById(R.id.img_HeadPortrait);
            tCollect =itemView.findViewById(R.id.tv_collect);
            scrollitem=itemView.findViewById(R.id.scroll_item);
        }
    }



}
