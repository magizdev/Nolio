package com.ca.nolio;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ca.nolio.adapters.ApplicationListAdapter;
import com.ca.nolio.adapters.NameIdAdapter;
import com.ca.nolio.interfaces.INolioServiceCallback;
import com.ca.nolio.model.ApplicationList;
import com.ca.nolio.model.ApprovalGate;
import com.ca.nolio.model.ApprovalGate.ApprovalGateStatus;
import com.ca.nolio.model.Deployment;
import com.ca.nolio.model.ManualApprovalGate;
import com.ca.nolio.model.NameIdList;
import com.ca.nolio.service.NolioService;
import com.ca.nolio.util.Configuration;

public class CreateDeploymentActivity extends Activity implements
		INolioServiceCallback, OnClickListener {
	private static final String APPLICATION_SERVICE_URL = "http://%s:8080/datamanagement/a/shallow_applications";
	private static final String PROJECTS_SERVICE_URL = "http://%s:8080/datamanagement/a/versions/%d";
	private static final String TEMPLATE_SERVICE_URL = "http://%s:8080/datamanagement/a/releasecandidates/versions/%d/pages?pageSize=50&pageStart=0";
	private static final String ENVIONMENT_SERVICE_URL = "http://%s:8080/datamanagement/a/shallow_applications/%d/environments";
	private static final String CREATE_DEPLOYMENT_SERVICE_URL = "http://%s:8080/datamanagement/a/deployments";
	private static final String TAG_LOAD_APPLICATIONS = "TAG_LOAD_APPLICATIONS";
	private static final String TAG_LOAD_PROJECTS = "TAG_LOAD_PROJECTS";
	private static final String TAG_LOAD_TEMPLATES = "TAG_LOAD_TEMPLATES";
	private static final String TAG_LOAD_ENVIRONMENTS = "TAG_LOAD_ENVIRONMENTS";
	private static final String TAG_CREATE_DEPLOYMENT = "TAG_CREATE_DEPLOYMENT";

	private Spinner applications;

	private Spinner projects;

	private Spinner templates;

	private Spinner environments;

	private EditText name;

	private Button create;

	private Configuration configuration;
	private long applicationId;
	private long projectId;
	private long templateId;
	private long environmentId;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_deployment);
		configuration = new Configuration(this);
		name = (EditText) findViewById(R.id.name);
		applications = (Spinner) findViewById(R.id.applications);
		applications.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				loadEnvironments(arg3);
				loadProjects(arg3);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		environments = (Spinner) findViewById(R.id.environments);
		projects = (Spinner) findViewById(R.id.projects);
		projects.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				loadTemplates(arg3);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		templates = (Spinner) findViewById(R.id.templates);
		create = (Button) findViewById(R.id.create);
		create.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		loadApplications();
	}

	private void loadApplications() {
		String getRelease = String.format(APPLICATION_SERVICE_URL,
				configuration.getServer());

		NolioService serviceCall = new NolioService(NolioService.GET, this,
				"Loading projects...", TAG_LOAD_APPLICATIONS, this);

		serviceCall.execute(new String[] { getRelease });
	}

	private void loadProjects(long applicationId) {

		String getRelease = String.format(PROJECTS_SERVICE_URL,
				configuration.getServer(), applicationId);

		NolioService serviceCall = new NolioService(NolioService.GET, this,
				"Loading projects...", TAG_LOAD_PROJECTS, this);

		serviceCall.execute(new String[] { getRelease });

	}

	private void loadTemplates(long projectId) {
		String getRelease = String.format(TEMPLATE_SERVICE_URL,
				configuration.getServer(), projectId);

		NolioService serviceCall = new NolioService(NolioService.GET, this,
				"Loading templates...", TAG_LOAD_TEMPLATES, this);

		serviceCall.execute(new String[] { getRelease });
	}

	private void loadEnvironments(long applicationId) {
		String getRelease = String.format(ENVIONMENT_SERVICE_URL,
				configuration.getServer(), applicationId);

		NolioService serviceCall = new NolioService(NolioService.GET, this,
				"Loading templates...", TAG_LOAD_ENVIRONMENTS, this);

		serviceCall.execute(new String[] { getRelease });
	}

	public void clearControls(View vw) {

	}

	public void postData(View vw) {

	}

	private void hideKeyboard() {

		InputMethodManager inputManager = (InputMethodManager) CreateDeploymentActivity.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(CreateDeploymentActivity.this
				.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public void onCallback(String tag, String response) {
		if (tag == TAG_LOAD_APPLICATIONS) {
			ApplicationList applications = new ApplicationList();
			try {
				applications.LoadFromJson(response);
			} catch (Exception e) {
				Log.e("", e.getLocalizedMessage(), e);
			}
			ApplicationListAdapter applicationAdapter = new ApplicationListAdapter(
					this, android.R.layout.simple_list_item_1,
					android.R.id.text1, applications);
			this.applications.setAdapter(applicationAdapter);

		} else if (tag == TAG_LOAD_PROJECTS) {
			NameIdList projects = new NameIdList();
			try {
				projects.LoadFromJson(response);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			NameIdAdapter projectsAdapter = new NameIdAdapter(this,
					android.R.layout.simple_list_item_1, android.R.id.text1,
					projects);
			this.projects.setAdapter(projectsAdapter);
		} else if (tag == TAG_LOAD_ENVIRONMENTS) {
			NameIdList environments = new NameIdList();
			try {
				environments.LoadFromJson(response);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			NameIdAdapter environmentsAdapter = new NameIdAdapter(this,
					android.R.layout.simple_list_item_1, android.R.id.text1,
					environments);
			this.environments.setAdapter(environmentsAdapter);
		} else if (tag == TAG_LOAD_TEMPLATES) {
			NameIdList templates = new NameIdList();
			try {
				templates.LoadFromJson(response);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			NameIdAdapter templatesAdapter = new NameIdAdapter(this,
					android.R.layout.simple_list_item_1, android.R.id.text1,
					templates);
			this.templates.setAdapter(templatesAdapter);
		} else if (tag == TAG_CREATE_DEPLOYMENT) {
			this.finish();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.create) {
			String executionRelease = String.format(
					CREATE_DEPLOYMENT_SERVICE_URL, configuration.getServer());
			NolioService serviceCall = new NolioService(NolioService.POST,
					this, "Starting...", TAG_CREATE_DEPLOYMENT, this);
//			serviceCall.addNameValuePair("template", value);
			serviceCall
					.setPayload(generatePayload());
			serviceCall.execute(new String[] { executionRelease });
		}
		// } else {
		// String approvalRelease = String.format(APPROVAL_SERVICE_URL,
		// configuration.getServer(), id);
		//
		// NolioService serviceCall = new NolioService(NolioService.PUT, this,
		// "Loading deployment detail...", TAG_APPROVAL, this);
		// if (v.getId() == R.id.btnApproval) {
		// // serviceCall.addNameValuePair("status", "APPROVED");
		// serviceCall.setPayload("{\"status\":\"APPROVED\"}");
		// } else if (v.getId() == R.id.btnDeny) {
		// // serviceCall.addNameValuePair("status", "DENY");
		// // serviceCall.addNameValuePair("denyReason",
		// // "Denyed from android");
		// serviceCall
		// .setPayload("{\"status\":\"DENY\",\"denyReason\":\"Denyed from android\"}");
		// }
		// serviceCall.execute(new String[] { approvalRelease });
		// }
	}

	private String generatePayload() {
		return String
				.format("{\"name\":\"%s\", \"environmentId\":\"%d\", \"deploymentExecutionOperation\":\"INIT\", \"releaseCandidateId\":\"%d\"}",
						name.getText().toString(),
						environments.getSelectedItemId(),
						templates.getSelectedItemId());
	}

}
