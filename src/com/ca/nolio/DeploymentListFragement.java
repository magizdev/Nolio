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
	static final String APPLICATION_SERVICE_STRING = "http://%s:8080/datamanagement/a/shallow_applications";
	static final String DEPLOYMENT_SERVICE_STRING = "http://%s:8080/datamanagement/a/reports/releasesReports?pageSize=50&pageStart=0&reportType=%s&advanced=APP_ID~%d";
	static final String APPLICATION_SERVICE_TAG = "APPLICATION_SERVICE_TAG";
	static final String DEPLOYMENT_SERVICE_TAG = "DEPLOYMENT_SERVICE_TAG";
	static final String TAB_APPROVALS = "APPROVAL";
	static final String TAB_PENDING = "PENDING";
	static final String TAB_RUNNING = "RUNNING";
	static final String TAB_RECENT = "RECENT";
	private Configuration configuration;
	private Spinner applicationsSpinner;
	private long applicationId;
	private String stateFilter;
	private ApplicationListAdapter adapter;
	private TabHost stateGroupHost;
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
		createDeployment.setClass(getActivity(), CreateDeploymentActivity.class);
		startActivity(createDeployment);
		return false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container,
				savedInstanceState);
		applicationsSpinner = (Spinner) rootView
				.findViewById(R.id.applications);
		configuration = new Configuration(this.getActivity());
		stateGroupHost = (TabHost) rootView.findViewById(R.id.stateGroups);
		depolymentList = (ListView) rootView.findViewById(R.id.deploymentList);
		setupTabs();
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		NolioService nolioService = new NolioService(NolioService.GET,
				this.getActivity(), "Loading applications...",
				APPLICATION_SERVICE_TAG, this);
		String serviceUrl = String.format(APPLICATION_SERVICE_STRING,
				configuration.getServer());
		nolioService.execute(serviceUrl);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		stateGroupHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				if (tabId == TAB_APPROVALS) {
					stateGroupHost.setCurrentTab(0);
					updateTab(tabId, R.id.tab_approvals);
				} else if (tabId == TAB_PENDING) {
					stateGroupHost.setCurrentTab(1);
					updateTab(tabId, R.id.tab_pending);
				} else if (tabId == TAB_RUNNING) {
					stateGroupHost.setCurrentTab(2);
					updateTab(tabId, R.id.tab_running);
				} else if (tabId == TAB_RECENT) {
					stateGroupHost.setCurrentTab(3);
					updateTab(tabId, R.id.tab_recent);
				}
			}
		});
		stateGroupHost.setCurrentTab(0);
		// manually start loading stuff in the first tab
		updateTab(TAB_APPROVALS, R.id.tab_approvals);
	}

	@Override
	public void onCallback(String tag, String response) {
		if (tag == APPLICATION_SERVICE_TAG) {
			ApplicationList applications = new ApplicationList();
			try {
				applications.LoadFromJson(response);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			adapter = new ApplicationListAdapter(this.getActivity(),
					android.R.layout.simple_list_item_1, android.R.id.text1,
					applications);

			applicationsSpinner.setAdapter(adapter);
			applicationsSpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							applicationId = adapter.getItemId(arg2);
							loadDeployments();
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}
					});
		} else if (tag == DEPLOYMENT_SERVICE_TAG) {
			DeploymentList deployments = new DeploymentList();
			try {
				deployments.LoadFromJson(response);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			deploymentAdapter = new DeploymentListAdapter(this.getActivity(),
					android.R.layout.simple_list_item_1, android.R.id.text1,
					deployments);
			depolymentList.setAdapter(deploymentAdapter);
			depolymentList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
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

	}

	private void setupTabs() {
		stateGroupHost.setup(); // you must call this before adding your tabs!
		stateGroupHost.addTab(newTab(TAB_APPROVALS,
				R.string.deployment_tab_approvals, R.id.tab_approvals));
		stateGroupHost.addTab(newTab(TAB_PENDING,
				R.string.deployment_tab_pending, R.id.tab_pending));
		stateGroupHost.addTab(newTab(TAB_RUNNING,
				R.string.deployment_tab_running, R.id.tab_running));
		stateGroupHost.addTab(newTab(TAB_RECENT,
				R.string.deployment_tab_recent, R.id.tab_recent));
	}

	private TabSpec newTab(String tag, int labelId, int tabContentId) {
		String label = getActivity().getResources().getString(labelId);
		TabSpec tabSpec = stateGroupHost.newTabSpec(tag);
		tabSpec.setIndicator(label);
		tabSpec.setContent(tabContentId);
		return tabSpec;
	}

	private void updateTab(String tabId, int placeholder) {
		switch (placeholder) {
		case R.id.tab_approvals:
			stateFilter = TAB_APPROVALS;
			break;
		case R.id.tab_pending:
			stateFilter = TAB_PENDING;
			break;
		case R.id.tab_running:
			stateFilter = TAB_RUNNING;
			break;
		case R.id.tab_recent:
			stateFilter = TAB_RECENT;
			break;
		default:
			break;
		}
		loadDeployments();
	}

	private void loadDeployments() {
		NolioService nolioService = new NolioService(NolioService.GET,
				this.getActivity(), "Loading deployments...",
				DEPLOYMENT_SERVICE_TAG, this);
		String serviceUrl = String.format(DEPLOYMENT_SERVICE_STRING,
				configuration.getServer(), stateFilter, applicationId);
		nolioService.execute(serviceUrl);
	}

}
