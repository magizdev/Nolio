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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.ca.nolio.adapters.ApplicationListAdapter;
import com.ca.nolio.adapters.DeploymentListAdapter;
import com.ca.nolio.interfaces.INolioServiceCallback;
import com.ca.nolio.model.ApplicationList;
import com.ca.nolio.model.DeploymentList;
import com.ca.nolio.service.NolioService;
import com.ca.nolio.util.Configuration;

public class DeploymentListFragement extends MenuFragment implements
		INolioServiceCallback {
	static final String DEPLOYMENT_SERVICE_STRING = "http://%s:8080/datamanagement/a/reports/releasesReportsPage?pageSize=50&pageStart=0&reportType=ALL&advanced=APP_ID~%d";
	static final String DEPLOYMENT_SERVICE_TAG = "DEPLOYMENT_SERVICE_TAG";
	private Configuration configuration;
	private ListView depolymentList;
	private DeploymentListAdapter deploymentAdapter;

	@Override
	public int layoutResource() {
		return R.layout.fragment_deployment_list;
	}

	@Override
	public int optionMenuResource() {
		return R.menu.deployment_list;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent createDeployment = new Intent();
		createDeployment
				.setClass(getActivity(), CreateDeploymentActivity.class);
		startActivity(createDeployment);
		return false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container,
				savedInstanceState);
		configuration = new Configuration(this.getActivity());
		depolymentList = (ListView) rootView.findViewById(R.id.deploymentList);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadDeployments();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCallback(String tag, String response) {
		DeploymentList deployments = new DeploymentList();
		try {
			deployments.LoadFromJson(response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		deploymentAdapter = new DeploymentListAdapter(this.getActivity(),
				deployments);
		depolymentList.setAdapter(deploymentAdapter);
		depolymentList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				long deploymentId = deploymentAdapter.getItemId(arg2);
				Intent startDeploymentDetailIntent = new Intent();
				startDeploymentDetailIntent.setClass(
						DeploymentListFragement.this.getActivity(),
						DeploymentDetailActivity.class);
				startDeploymentDetailIntent.putExtra("id", deploymentId);
				DeploymentListFragement.this.getActivity().startActivity(
						startDeploymentDetailIntent);
			}
		});

	}

	private void loadDeployments() {
		NolioService nolioService = new NolioService(NolioService.GET,
				this.getActivity(), "Loading deployments...",
				DEPLOYMENT_SERVICE_TAG, this);
		String serviceUrl = String.format(DEPLOYMENT_SERVICE_STRING,
				configuration.getServer(), configuration.getApplicationId());
		nolioService.execute(serviceUrl);
	}

}
