package com.ca.nolio.chart;

import java.util.HashMap;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

import com.ca.nolio.model.ReportData;
import com.ca.nolio.model.ReportDataList;

public class BarChart extends BaseChart {

	public BarChart(Context context) {
		super(context);
	}

	private int seriesCount;
	private ReportDataList reportsData;
	private HashMap<String, Long> data;
	private int maxY;
	private XYMultipleSeriesDataset mBarDataset;
	private XYMultipleSeriesRenderer mBarRenderer;

	@Override
	public GraphicalView generateChartView(ReportDataList reportsData) {
		this.reportsData = reportsData;
		data = new HashMap<String, Long>();
		for (ReportData tmpData : reportsData) {
			data.put(tmpData.getName(), tmpData.getDuration());
		}
		mBarDataset = buildBarDataset();
		mBarRenderer = buildBarRenderer();
		return ChartFactory.getBarChartView(this.context, mBarDataset,
				mBarRenderer, org.achartengine.chart.BarChart.Type.DEFAULT);
	}

	private XYMultipleSeriesDataset buildBarDataset() {
		seriesCount = 0;

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		HashMap<Long, XYSeries> categoryMap = new HashMap<Long, XYSeries>();

		seriesCount = 1;
		int i = 1;
		XYSeries series = new XYSeries("");
		categoryMap.put(1L, series);
		for (String name : data.keySet()) {
			series.add(i++, data.get(name));
		}

		dataset.addSeries(series);

		return dataset;

	}

	private XYMultipleSeriesRenderer buildBarRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setLabelsTextSize(15);
		renderer.setBarWidth(20);
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(0x00FAFAFA);
		renderer.setMarginsColor(0x00FAFAFA);
		renderer.setLabelsColor(Color.BLUE);
		renderer.setAxesColor(Color.BLACK);
		renderer.setXLabelsColor(Color.BLACK);
		renderer.setYLabelsColor(0, Color.BLACK);
		renderer.setLegendTextSize(15);
		int length = COLORS.length;
		for (int i = 0; i < seriesCount; i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(COLORS[i % length]);
			r.setDisplayChartValues(true);
			if (seriesCount == 1) {
				r.setShowLegendItem(false);
			}
			renderer.addSeriesRenderer(r);
		}

		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setPanEnabled(false, false);
		renderer.setYAxisMin(0);
		renderer.setXLabels(0);
		renderer.setXAxisMin(0);

		int j = 1;
		for (String name : data.keySet()) {
				renderer.removeXTextLabel(j);
				renderer.addXTextLabel(j, name);
				j++;
		}

		renderer.setZoomEnabled(false);
		renderer.setZoomRate(1f);
		renderer.setBarSpacing(1f);
		return renderer;
	}

}
