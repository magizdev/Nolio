package com.ca.nolio;

import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.ca.nolio.adapters.ApplicationListAdapter;
import com.ca.nolio.adapters.DeploymentListAdapter;
import com.ca.nolio.adapters.ReportAdapter;
import com.ca.nolio.chart.BarChart;
import com.ca.nolio.chart.PieChart;
import com.ca.nolio.interfaces.INolioServiceCallback;
import com.ca.nolio.model.ApplicationList;
import com.ca.nolio.model.DeploymentList;
import com.ca.nolio.model.ReportDataList;
import com.ca.nolio.service.NolioService;
import com.ca.nolio.util.Configuration;

public class ReportFragement extends MenuFragment implements
		INolioServiceCallback {
	static final String REPORT_SERVICE_STRING = "http://%s:8080/datamanagement/a/reports/releasesReports?fromIndex=0&toIndex=100&reportType=OFFLINE&advanced=RELEASE_DATE~-1_-1_604800000";
	static final String REPORT_SERVICE_TAG = "REPORT_SERVICE_TAG";
	private Configuration configuration;
	private ListView reports;

	@Override
	public int layoutResource() {
		return R.layout.fragment_report;
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
		reports = (ListView) rootView.findViewById(android.R.id.list);
		configuration = new Configuration(this.getActivity());
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		NolioService nolioService = new NolioService(NolioService.GET,
				this.getActivity(), "Loading data...", REPORT_SERVICE_TAG, this);
		String serviceUrl = String.format(REPORT_SERVICE_STRING,
				configuration.getServer());
		nolioService.execute(serviceUrl);
	}

	@Override
	public void onCallback(String tag, String response) {
		if (tag == REPORT_SERVICE_TAG) {
			ReportDataList reportsData = new ReportDataList();
			try {
				reportsData.loadFromJson(response);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ReportAdapter adapter = new ReportAdapter(getActivity(),
					R.layout.row, R.id.col1, reportsData);
			reports.setAdapter(adapter);
		}
	}
}
