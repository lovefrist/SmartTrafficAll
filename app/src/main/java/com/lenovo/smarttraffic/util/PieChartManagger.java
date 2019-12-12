package com.lenovo.smarttraffic.util;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.List;

/**
 * Created by zhang on 2018/7/5.
 */

public class PieChartManagger {

    public PieChart pieChart;

    public PieChartManagger(PieChart pieChart) {
        this.pieChart = pieChart;
//        initPieChart();
    }

    //初始化
    private void initPieChart() {
        //  是否显示中间的洞
        pieChart.setDrawHoleEnabled(false);
        //设置中间洞的大小
        pieChart.setHoleRadius(40f);
        // 半透明圈
        pieChart.setTransparentCircleRadius(30f);
        //设置半透明圆圈的颜色
        pieChart.setTransparentCircleColor(Color.WHITE);
        //设置半透明圆圈的透明度
        pieChart.setTransparentCircleAlpha(125);

        //饼状图中间可以添加文字
        pieChart.setDrawCenterText(false);
        //设置中间文字
        pieChart.setCenterText("民族");
        //中间问题的颜色
        pieChart.setCenterTextColor(Color.parseColor("#a1a1a1"));
        //中间文字的大小px
        pieChart.setCenterTextSizePixels(36);
        pieChart.setCenterTextRadiusPercent(1f);
        //中间文字的样式
        pieChart.setCenterTextTypeface(Typeface.DEFAULT);
        //中间文字的偏移量
        pieChart.setCenterTextOffset(0, 0);
        // 初始旋转角度
        pieChart.setRotationAngle(0);
        // 可以手动旋转
        pieChart.setRotationEnabled(true);
        //显示成百分比
        pieChart.setUsePercentValues(true);
        //取消右下角描述
        pieChart.getDescription().setEnabled(false);
        //是否显示每个部分的文字描述
        pieChart.setDrawEntryLabels(false);
        //描述文字的颜色
        pieChart.setEntryLabelColor(Color.RED);
        //描述文字的大小
        pieChart.setEntryLabelTextSize(14);
        //描述文字的样式
        pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);

        //图相对于上下左右的偏移
        pieChart.setExtraOffsets(20, 8, 75, 8);
        //图标的背景色
        pieChart.setBackgroundColor(Color.TRANSPARENT);
        //设置pieChart图表转动阻力摩擦系数[0,1]
        pieChart.setDragDecelerationFrictionCoef(0.75f);
        //获取图例
        Legend legend = pieChart.getLegend();
        //设置图例水平显示
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        //顶部
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //右对其
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //x轴的间距
        legend.setXEntrySpace(7f);
        //y轴的间距
        legend.setYEntrySpace(10f);
        //图例的y偏移量
        legend.setYOffset(10f);
        //图例x的偏移量
        legend.setXOffset(10f);
        //图例文字的颜色

        legend.setTextColor(Color.parseColor("#a1a1a1"));
        //图例文字的大小
        legend.setTextSize(13);

    }


    /**
     * 显示实心圆
     *
     * @param yvals
     * @param colors
     */
    public void showSolidPieChart(List<PieEntry> yvals, List<Integer> colors) {
        //数据集合
        PieDataSet dataset = new PieDataSet(yvals, "");

        //填充每个区域的颜色
        dataset.setColors(colors);
        //是否在图上显示数值
        dataset.setDrawValues(true);
//        文字的大小
        dataset.setValueTextSize(14);
//        文字的颜色
        dataset.setValueTextColor(Color.RED);
//        文字的样式
        dataset.setValueTypeface(Typeface.DEFAULT_BOLD);

//      当值位置为外边线时，表示线的前半段长度。
        dataset.setValueLinePart1Length(0.4f);
//      当值位置为外边线时，表示线的后半段长度。
        dataset.setValueLinePart2Length(0.4f);
//      当ValuePosits为OutsiDice时，指示偏移为切片大小的百分比
        dataset.setValueLinePart1OffsetPercentage(80f);
        // 当值位置为外边线时，表示线的颜色。
        dataset.setValueLineColor(Color.parseColor("#a1a1a1"));
//        设置Y值的位置是在圆内还是圆外
        dataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        设置Y轴描述线和填充区域的颜色一致
//        dataset.setUsingSliceColorAsValueLineColor(false);

//        设置每条之前的间隙
        dataset.setSliceSpace(0);

        //设置饼状Item被选中时变化的距离
        dataset.setSelectionShift(5f);
        //填充数据
        PieData pieData = new PieData(dataset);
//        格式化显示的数据为%百分比
        pieData.setValueFormatter(new PercentFormatter());
//        显示试图
        pieChart.setData(pieData);
    }


    /**
     * 显示圆环
     *
     * @param yvals
     * @param colors
     */
    public void showRingPieChart(List<PieEntry> yvals, List<Integer> colors,String name) {
        Log.d("", "showRingPieChart: 进来了");
        pieChart.setNoDataText("网路不好检测一下");
        //显示为圆环
        pieChart.setDrawHoleEnabled(true);
        //设置中间洞的大小
        pieChart.setDrawCenterText(true);
        //设置中间文字
        pieChart.setCenterText(name);
        //设置饼图上面是否有文字
        pieChart.setDrawEntryLabels(false);
        //设置是否可以转动
        pieChart.setTouchEnabled(true);
        //设置右角的文字是否显示
        pieChart.getDescription().setEnabled(false);
        //设置点击是否有效果
        pieChart.setSaveFromParentEnabled(false);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        pieChart.setHoleRadius(85f);
//        pieChart.setText
        //数据集合
        PieDataSet dataset = new PieDataSet(yvals, "");
        //填充每个区域的颜色
        dataset.setColors(colors);
        //是否在图上显示数值
        dataset.setDrawValues(false);
        // 文字的大小
        dataset.setValueTextSize(14);
        //  文字的颜色
        dataset.setValueTextColor(Color.RED);
        //文字的样式
        dataset.setValueTypeface(Typeface.DEFAULT_BOLD);

        // 当值位置为外边线时，表示线的前半段长度。
        dataset.setValueLinePart1Length(0.4f);
        //当值位置为外边线时，表示线的后半段长度。
        dataset.setValueLinePart2Length(0.4f);
        // 当ValuePosits为OutsiDice时，指示偏移为切片大小的百分比
        dataset.setValueLinePart1OffsetPercentage(80f);
        // 当值位置为外边线时，表示线的颜色。
        dataset.setValueLineColor(Color.parseColor("#a1a1a1"));
        //设置Y值的位置是在圆内还是圆外
        dataset.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //设置Y轴描述线和填充区域的颜色一致

        //设置每条之前的间隙
        dataset.setSliceSpace(0);

        //设置饼状Item被选中时变化的距离
        dataset.setSelectionShift(5f);
        //填充数据
        PieData pieData = new PieData(dataset);
        // 格式化显示的数据为%百分比
        pieData.setValueFormatter(new PercentFormatter());
        //显示试图
        pieChart.setData(pieData);
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();

    }


}