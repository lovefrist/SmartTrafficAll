package com.lenovo.smarttraffic.ui.adapter;

import android.content.Context;
import android.graphics.Color;
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

/**
 * 生活指数的适配器
 *
 * @author asus
 */
public class LeftIndexAdapter extends RecyclerView.Adapter<LeftIndexAdapter.myViewHider> {
    Context context;

    List list;
    String[] namedata = new String[]{
            "紫外线指数", "空气污染指数", "运动指数", "穿衣指数", "感冒指数", "洗衣指数"
    };
    int[] srcint = new int[]{
            R.drawable.ultraviolet, R.drawable.leaf, R.drawable.motion, R.drawable.dressing, R.drawable.cold, R.drawable.carindex

    };
    String[][] deredr = new String[][]{
            {"弱", "中等", "强"}, {"优", "良", "轻度污染", "中度污染", "重度污染"}, {"适宜", "中", "较不适宜"},
            {"冷", "舒适", "温暖", "热"}, {"易挥发", "少发"}, {"适宜", "较不适宜", "不适宜"}
    };
    String[][] strcolor = new String[][]{
            {"#4472c4", "#00b050", "#ff0000"}, {"#44dc68", "#92d050", "#ffff40", "#bf9000", "#993300"},
            {"#44dc68", "#ffc000", "#8149ac"}, {"#3462f4", "#92d050", "#44dc68", "#ff0000"}, {"#ff0000", "#ffff40"}, {"#ffffff", "#ffffff", "#ffffff"}

    };
    int[][] number = new int[][]{
            {1001, 3001}, {36, 76, 116, 151}, {3001, 6001}, {13, 22, 36}, {51}, {1,7}
    };

    public LeftIndexAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public myViewHider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHider(LayoutInflater.from(context).inflate(R.layout.layout_left, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHider holder, int position) {
        holder.textIndex.setText(namedata[position]);
        holder.textIndex.setBackgroundColor(Color.parseColor("#0000CD"));
        holder.imgChart.setImageResource(srcint[position]);
        if (position == 5) {
            holder.thumbedIndex.setText("");
        } else {
            holder.thumbedIndex.setText(list.get(position).toString());
        }
        int index = number[position].length;
        for (int i = 0; i < index + 1; i++) {
            if (i == index) {
                holder.tendered.setBackgroundColor(Color.parseColor(strcolor[position][index]));
                holder.tendered.setText(deredr[position][index]);
                break;
            } else if (Integer.parseInt(list.get(position).toString()) < number[position][i]) {
                holder.tendered.setText(deredr[position][i]);
                holder.tendered.setBackgroundColor(Color.parseColor(strcolor[position][i]));
                break;
            }
        }

    }


    @Override
    public int getItemCount() {
        Log.e("", "getItemCount: "+list.size() );
        return list.size();
    }

    class myViewHider extends RecyclerView.ViewHolder {
        TextView textIndex, thumbedIndex, tendered;
        ImageView imgChart;

        public myViewHider(View itemView) {
            super(itemView);
            textIndex = itemView.findViewById(R.id.tv_textIndex);
            thumbedIndex = itemView.findViewById(R.id.tv_numberIndex);
            tendered = itemView.findViewById(R.id.tv_degree);
            imgChart = itemView.findViewById(R.id.iv_chart);
        }
    }
}
