package com.example.smartquiz.ui;

import static com.github.mikephil.charting.animation.Easing.EaseInOutQuad;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartquiz.R;
import com.example.smartquiz.db.DBHelper;
import com.example.smartquiz.utils.RoundedBarChartRenderer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PerformanceActivity extends AppCompatActivity {
    TextView tvTotal, tvAvg, tvBest;

    private BarChart barChart;
    private PieChart pieChart;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        tvTotal = findViewById(R.id.tvTotal);
      //  btnBackHome = findViewById(R.id.btnBackHome);
        tvAvg = findViewById(R.id.tvAvg);
        // tvBest = findViewById(R.id.tvBest);
        db = new DBHelper(this);

        initializeCharts();
        List<Float> testScores = Arrays.asList(75f, 82f, 89f, 78f);


        // 2. Load the initial data
        updateBarChartData(barChart, testScores);
       // setupPieChart();

        tvTotal.setText("Total tests: " + db.getTotalTests());
        tvAvg.setText("Average score: " + db.getAverageScore());
    }

    private void initializeCharts() {
        // Initialize bar chart
        LinearLayout barChartContainer = findViewById(R.id.barChartContainer);

        //  Create an instance of your BarChart programmatically
        barChart = new BarChart(this);

        //  Define LayoutParams to control how the chart fills the container
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        barChart.setLayoutParams(layoutParams);

        // Important: Remove the placeholder TextView
        barChartContainer.removeAllViews();

        //  Add your newly created BarChart to the container
        barChartContainer.addView(barChart);

        //  Find the CONTAINER from your XML layout
        LinearLayout pieChartContainer = findViewById(R.id.pieChartContainer);

        //  Create a new PieChart instance programmatically
        pieChart = new PieChart(this);

        // Define LayoutParams to make the chart fill the container
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        pieChart.setLayoutParams(layoutParams1);

        // IMPORTANT: Clear the placeholder TextView ("Pie Chart will be implemented...")
        pieChartContainer.removeAllViews();

        //  Add your newly created PieChart to the container
        pieChartContainer.addView(pieChart);

        //  NOW that pieChart is a valid, attached view, you can call your setup method
        setupPieChart();

        configureBarChart(barChart);
    }


    private void setupPieChart() {
        // Sample data for subject performance
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(92f, "Physics"));
        entries.add(new PieEntry(88f, "Chemistry"));
        entries.add(new PieEntry(85f, "Biology"));
        entries.add(new PieEntry(90f, "Math"));
        entries.add(new PieEntry(78f, "History"));

        PieDataSet dataSet = new PieDataSet(entries, "Subject Performance");

        // Use a pre-defined color palette for simplicity and good aesthetics
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        PieData pieData = new PieData(dataSet);

        // FIX 1: Use PercentFormatter to correctly display percentages
        pieData.setValueFormatter(new PercentFormatter(pieChart));

        // Configure chart appearance
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);

        // FIX 1 (cont.): Tell the chart to calculate and use percent values
        pieChart.setUsePercentValues(true);

        // FIX 2: Disable entry labels to avoid clutter
        pieChart.setDrawEntryLabels(false);

        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        // Configure the legend
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setTextColor(Color.parseColor("#49454F"));
        legend.setTextSize(12f);

        // FIX 3: Animate the chart (no need for extra invalidate)
        pieChart.animateY(1400, EaseInOutQuad);
    }


    private void configureBarChart(BarChart barChart) {
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBorders(false);
        barChart.setTouchEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setPinchZoom(true);

        // X-axis setup
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.parseColor("#49454F"));
        xAxis.setTextSize(12f);

        // Y-axis setup
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.parseColor("#E7E0EC"));
        leftAxis.setTextColor(Color.parseColor("#49454F"));
        leftAxis.setTextSize(12f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(100f);

        barChart.getAxisRight().setEnabled(false);

        // IMPORTANT: Set the custom renderer only ONCE
        barChart.setRenderer(new RoundedBarChartRenderer(
                barChart,
                barChart.getAnimator(),
                barChart.getViewPortHandler(),
                Color.parseColor("#6750A4"),
                Color.parseColor("#D0BCFF")
        ));
    }
    private void updateBarChartData(BarChart barChart, List<Float> scores) {
        if (scores == null || scores.isEmpty()) {
            barChart.clear(); // Clear the chart if there is no data
            barChart.invalidate();
            return;
        }

        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            entries.add(new BarEntry(i, scores.get(i)));
        }

        BarDataSet dataSet;

        // Check if chart already has data
        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            dataSet = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            dataSet.setValues(entries);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            dataSet = new BarDataSet(entries, "Test Scores");
            dataSet.setDrawValues(true);
            dataSet.setValueTextColor(Color.parseColor("#49454F"));
            dataSet.setValueTextSize(12f);

            BarData barData = new BarData(dataSet);
            barChart.setData(barData);
        }

        // --- All your dynamic adjustments based on data size ---

        // Adjust bar width dynamically
        float barWidth = scores.size() > 6 ? 0.3f : 0.6f;
        barChart.getBarData().setBarWidth(barWidth);

        // Enable/disable scrolling
        barChart.setDragEnabled(scores.size() > 6);

        // Adjust X-axis labels
        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelCount(scores.size(), false);
        if (scores.size() > 4) {
            xAxis.setLabelRotationAngle(-45f);
        } else {
            xAxis.setLabelRotationAngle(-30f);
        }

        String[] labels = new String[scores.size()];
        for (int i = 0; i < scores.size(); i++) {
            labels[i] = "Test " + (i + 1);
        }
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        // --- Refresh the chart ---

        barChart.setFitBars(true); // Ensure bars are sized correctly
        barChart.animateY(1000);
        barChart.invalidate(); // Redraw the chart
    }

}

