package com.ca.nolio;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ca.nolio.interfaces.INolioServiceCallback;
import com.ca.nolio.model.ApplicationList;
import com.ca.nolio.service.NolioService;
import com.ca.nolio.util.Configuration;

public class LoginActivity extends Activity implements INolioServiceCallback {
	private static final String SERVICE_BASE_URL = "http://%s:8080/datamanagement/a/shallow_applications";
	private EditText server;
	private EditText username;
	private EditText password;
	private Button login;
	private Configuration configuration;
	private TextView message;
	private String applicationListResponse;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		server = (EditText) findViewById(R.id.serverName);
		username = (EditText) findViewById(R.id.userName);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		message = (TextView) findViewById(R.id.messageLabel);
		configuration = Configuration.getConfiguration(this);

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				message.setVisibility(View.GONE);
				configuration.setServer(server.getText().toString());
				String usernameAndPassword = username.getText().toString()
						+ ":" + password.getText().toString();
				String authenticate = Base64.encodeToString(
						usernameAndPassword.getBytes(), Base64.DEFAULT).trim();
				configuration.setUsername(username.getText().toString());
				configuration.setAuthenticate(authenticate);

				NolioService nolioServiceCall = new NolioService(
						NolioService.GET, LoginActivity.this, "Logging in...",
						"", LoginActivity.this);
				nolioServiceCall.execute(String.format(SERVICE_BASE_URL,
						configuration.getServer(), 0));
			}
		});
		
		if(configuration.getServer().length() > 0 ){
			server.setText(configuration.getServer());
		}
		
		if(configuration.getUsername().length() > 0 ){
			username.setText(configuration.getUsername());;
		}
		
//		if(configuration.getServer().length() > 0 ){
//			NolioService nolioServiceCall = new NolioService(
//					NolioService.GET, LoginActivity.this, "Logging in...",
//					"", LoginActivity.this);
//			nolioServiceCall.execute(String.format(SERVICE_BASE_URL,
//					configuration.getServer(), 0));
//		}
	}

	@Override
	public void onCallback(String tag, String response) {
		JSONObject returnMsg;
		try {
			returnMsg = new JSONObject(response);
			applicationListResponse = response;
			onSuccess();
		} catch (JSONException e) {
			e.printStackTrace();
			onFail();
		}
	}

	private void onSuccess() {
		Intent mainPage = new Intent();
		mainPage.setClass(this, MainActivity.class);
		mainPage.putExtra("Applications", applicationListResponse);
		startActivity(mainPage);
	}

	private void onFail() {
		message.setVisibility(View.VISIBLE);
	}
}
