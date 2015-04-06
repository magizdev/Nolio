package com.ca.nolio;

import org.json.JSONException;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.ca.nolio.chart.BarChart;
import com.ca.nolio.chart.PieChart;
import com.ca.nolio.interfaces.INolioServiceCallback;
import com.ca.nolio.model.ReportDataList;
import com.ca.nolio.service.NolioService;
import com.ca.nolio.util.Configuration;

public class DashboardFragement extends MenuFragment implements
		INolioServiceCallback {
	static final String REPORT_SERVICE_STRING = "http://%s:8080/datamanagement/a/reports/releasesReports?fromIndex=0&toIndex=100&reportType=OFFLINE&advanced=RELEASE_DATE~-1_-1_604800000";
	static final String REPORT_SERVICE_TAG = "REPORT_SERVICE_TAG";
	private Configuration configuration;
	private LinearLayout pieChartArea;
	private LinearLayout barChartArea;
	private ReportDataList reportsData;

	@Override
	public int layoutResource() {
		return R.layout.fragment_dashboard;
	}

	@Override
	public int optionMenuResource() {
		return 0;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container,
				savedInstanceState);
		
		ArrayAdapter<String> chartTypeAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_list_item_1,
				android.R.id.text1);
		chartTypeAdapter.addAll(getResources().getStringArray(
				R.array.chartTypes));
		configuration = new Configuration(this.getActivity());
		pieChartArea = (LinearLayout) rootView.findViewById(R.id.pieChartArea);
		barChartArea = (LinearLayout) rootView.findViewById(R.id.barChartArea);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshData();
	}

	@Override
	public void onCallback(String tag, String response) {
		reportsData = new ReportDataList();
		if (tag == REPORT_SERVICE_TAG) {
			try {
				reportsData.loadFromJson(response);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			drawReports();
		}
	}

	private void refreshData() {
		NolioService nolioService = new NolioService(NolioService.GET,
				this.getActivity(), "Loading data...", REPORT_SERVICE_TAG, this);
		String serviceUrl = String.format(REPORT_SERVICE_STRING,
				configuration.getServer());
		nolioService.execute(serviceUrl);
	}

	private void drawReports() {
		if (reportsData == null) {
			refreshData();
			return;
		}

		BarChart barChart = new BarChart(getActivity());
		barChartArea.addView(barChart.generateChartView(reportsData));
		PieChart pieChart = new PieChart(this.getActivity());
		pieChartArea.addView(pieChart.generateChartView(reportsData));

		pieChartArea.requestLayout();
		barChartArea.requestLayout();
	}

}
