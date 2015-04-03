package com.ca.nolio.chart;

import java.util.HashMap;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

import com.ca.nolio.model.ReportData;
import com.ca.nolio.model.ReportDataList;

public class PieChart extends BaseChart {
	public PieChart(Context context) {
		super(context);
	}

	private DefaultRenderer mPieRenderer;
	private CategorySeries mPieSeries;

	@Override
	public GraphicalView generateChartView(ReportDataList reportsData) {
		mPieRenderer = new DefaultRenderer();
		mPieSeries = new CategorySeries("");
		mPieRenderer.setStartAngle(180);
		mPieRenderer.setDisplayValues(true);
		mPieRenderer.setLegendTextSize(30);
		mPieRenderer.setLabelsTextSize(30);
		mPieRenderer.setLabelsColor(Color.BLUE);

		HashMap<String, Integer> chartData = getChartData(reportsData);

		int index = 0;
		for (String series : chartData.keySet()) {
			mPieSeries.add(series, chartData.get(series));
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(getSeriesColor(series, index++));
			renderer.setChartValuesTextSize(40);
			mPieRenderer.addSeriesRenderer(renderer);

		}

		return ChartFactory.getPieChartView(this.context, mPieSeries,
				mPieRenderer);

	}

	private HashMap<String, Integer> getChartData(ReportDataList reportsData) {
		HashMap<String, Integer> chartData = new HashMap<String, Integer>();
		for (ReportData data : reportsData) {
			if (chartData.containsKey(data.getStatus())) {
				int count = chartData.get(data.getStatus()) + 1;
				chartData.put(data.getStatus(), count);
			} else {
				chartData.put(data.getStatus(), 1);
			}
		}
		return chartData;
	}

	private int getSeriesColor(String seriesName, int index) {
		if (seriesName.equals("FAILURE")) {
			return Color.RED;
		}
		if (seriesName.equals("SUCCESS")) {
			return Color.GREEN;
		}
		if (seriesName.equals("CANCELLED_DEPLOYMENT")) {
			return Color.YELLOW;
		}
		return COLORS[index % COLORS.length];
	}

}
