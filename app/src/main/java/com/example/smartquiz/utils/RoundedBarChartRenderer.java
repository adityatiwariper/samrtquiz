package com.example.smartquiz.utils;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class RoundedBarChartRenderer extends BarChartRenderer {

    private final int startColor;
    private final int endColor;
    private final Paint mPaint;

    public RoundedBarChartRenderer(BarChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler,
                                   int startColor, int endColor) {
        super(chart, animator, viewPortHandler);
        this.startColor = startColor;
        this.endColor = endColor;
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {
        Transformer transformer = mChart.getTransformer(dataSet.getAxisDependency());

        for (int i = 0; i < dataSet.getEntryCount(); i++) {
            BarEntry entry = dataSet.getEntryForIndex(i);
            float left = entry.getX() - mChart.getBarData().getBarWidth() / 2f;
            float right = entry.getX() + mChart.getBarData().getBarWidth() / 2f;
            float top = entry.getY();
            float bottom = 0f;

            float[] pts = new float[]{left, bottom, right, top};
            transformer.pointValuesToPixel(pts);

            RectF rect = new RectF(pts[0], pts[1], pts[2], pts[3]);

            LinearGradient gradient = new LinearGradient(
                    rect.left, rect.top, rect.right, rect.bottom,
                    startColor, endColor, Shader.TileMode.MIRROR
            );

            mPaint.setShader(gradient);
            c.drawRoundRect(rect, 25f, 25f, mPaint);
            mPaint.setShader(null);
        }
    }
}

