package com.ca.nolio;

import com.ca.nolio.adapters.ApplicationListAdapter;
import com.ca.nolio.util.Configuration;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ApplicationSelectListener implements OnItemSelectedListener {

	private ApplicationListAdapter adapter;
	private Configuration configuration;
	private OnApplicationChangedListener applicationChangedListener;
	
	public ApplicationSelectListener(ApplicationListAdapter adapter, Configuration configuration, OnApplicationChangedListener applicationChangedListener){
		this.adapter = adapter;
		this.configuration = configuration;
		this.applicationChangedListener = applicationChangedListener;
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0,
			View arg1, int arg2, long arg3) {
		long applicationId = adapter.getItemId(arg2);
		String applicationName = adapter.getApplicaitonName(arg2);
		configuration.setApplication(applicationName, applicationId);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
