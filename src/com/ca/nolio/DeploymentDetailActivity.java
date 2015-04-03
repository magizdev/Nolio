package com.ca.nolio;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ca.nolio.interfaces.INolioServiceCallback;
import com.ca.nolio.model.ApprovalGate;
import com.ca.nolio.model.ApprovalGate.ApprovalGateStatus;
import com.ca.nolio.model.Deployment;
import com.ca.nolio.model.ManualApprovalGate;
import com.ca.nolio.service.NolioService;
import com.ca.nolio.util.Configuration;

public class DeploymentDetailActivity extends Activity implements
		INolioServiceCallback, OnClickListener {
	private static final String SERVICE_BASE_URL = "http://%s:8080/datamanagement/a/releases/%d";
	private static final String APPROVAL_SERVICE_URL = "http://%s:8080/datamanagement/a/approval_gates/manual_approval_gate/%d";
	private static final String EXECUTION_SERVICE_URL = "http://%s:8080/datamanagement/a/releases/%d/execution";
	private static final String TAG_APPROVAL = "TAG_APPROVAL";
	private static final String TAG_LOAD_DETAIL = "TAG_LOAD_DETAIL";
	private static final String TAG_EXECUTION = "TAG_EXECUTION";

	private static final String TAG = "NOLIO_DEPLOYMENT_DETAIL";

	private TextView name;

	private TextView application;

	private TextView environment;

	private TextView status;

	private Button approval;

	private Button deny;

	private Button run;

	private ListView steps;
	private Configuration configuration;
	private long id;
	private Deployment deployment;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deployment_detail);
		configuration = new Configuration(this);
		name = (TextView) findViewById(R.id.releaseName);
		application = (TextView) findViewById(R.id.applicationName);
		environment = (TextView) findViewById(R.id.environment);
		status = (TextView) findViewById(R.id.status);
		steps = (ListView) findViewById(R.id.steps);
		approval = (Button) findViewById(R.id.btnApproval);
		approval.setOnClickListener(this);
		deny = (Button) findViewById(R.id.btnDeny);
		deny.setOnClickListener(this);
		run = (Button) findViewById(R.id.btnRun);
		run.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		id = getIntent().getLongExtra("id", 0);
		loadDeployment(id);
	}

	public void loadDeployment(long id) {

		String getRelease = String.format(SERVICE_BASE_URL,
				configuration.getServer(), id);

		NolioService serviceCall = new NolioService(NolioService.GET, this,
				"Loading deployment detail...", TAG_LOAD_DETAIL, this);

		serviceCall.execute(new String[] { getRelease });

	}

	public void clearControls(View vw) {

	}

	public void postData(View vw) {

	}

	private void hideKeyboard() {

		InputMethodManager inputManager = (InputMethodManager) DeploymentDetailActivity.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(DeploymentDetailActivity.this
				.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public void onCallback(String tag, String response) {
		if (tag == TAG_LOAD_DETAIL) {
			deployment = new Deployment();
			try {
				deployment.loadFromJson(response);
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
			}
			name.setText(deployment.getName());
			application.setText(deployment.getApplication());
			environment.setText(deployment.getEnvironment());
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1);
			adapter.addAll(deployment.getSteps());
			steps.setAdapter(adapter);

			approval.setVisibility(View.GONE);
			deny.setVisibility(View.GONE);
			run.setVisibility(View.GONE);

			status.setText(deployment.getStatus());

			if (deployment.getStatus().equals("PENDING_APPROVAL")) {
				approval.setVisibility(View.VISIBLE);
				deny.setVisibility(View.VISIBLE);
			} else if (deployment.getStatus().equals("PENDING")
					|| deployment.getStatus().equals("PENDING_VALIDATION")) {
				run.setVisibility(View.VISIBLE);
			}
		} else if (tag == TAG_APPROVAL || tag == TAG_EXECUTION) {
			loadDeployment(id);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnRun) {
			String executionRelease = String.format(EXECUTION_SERVICE_URL,
					configuration.getServer(), id);
			NolioService serviceCall = new NolioService(NolioService.PUT, this,
					"Starting...", TAG_EXECUTION, this);
			if (deployment.getStatus().equals("PENDING_VALIDATION")) {
				serviceCall.setPayload("{\"executionOperation\":\"RUN\"}");
			} else {
				serviceCall
						.setPayload("{\"executionOperation\":\"RUN_ALL_THE_WAY\"}");
			}
			serviceCall.execute(new String[] { executionRelease });

		} else {
			String approvalRelease = String.format(APPROVAL_SERVICE_URL,
					configuration.getServer(), id);

			NolioService serviceCall = new NolioService(NolioService.PUT, this,
					"Loading deployment detail...", TAG_APPROVAL, this);
			if (v.getId() == R.id.btnApproval) {
				serviceCall.setPayload("{\"status\":\"APPROVED\"}");
			} else if (v.getId() == R.id.btnDeny) {
				serviceCall
						.setPayload("{\"status\":\"DENY\",\"denyReason\":\"Denyed from android\"}");
			}
			serviceCall.execute(new String[] { approvalRelease });
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.deployment_detail, menu);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.refresh_deployment){
			loadDeployment(id);
		}
		return true;
	}

}
