package com.ca.nolio.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.ca.nolio.interfaces.INolioServiceCallback;
import com.ca.nolio.util.Configuration;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class NolioService extends AsyncTask<String, Integer, String> {

	public static final int POST = 1;
	public static final int GET = 2;
	public static final int PUT = 3;

	private static final String TAG = "WebServiceTask";

	// connection timeout, in milliseconds (waiting to connect)
	private static final int CONN_TIMEOUT = 3000;

	// socket timeout, in milliseconds (waiting for data)
	private static final int SOCKET_TIMEOUT = 5000;

	private int taskType = GET;
	private Context mContext = null;
	private String processMessage = "Processing...";

	private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

	private ProgressDialog pDlg = null;
	private INolioServiceCallback callback;
	private Configuration configuration;
	private String tag;
	private String payload;

	public NolioService(int taskType, Context mContext, String processMessage,
			String tag, INolioServiceCallback callback) {

		this.taskType = taskType;
		this.mContext = mContext;
		configuration = Configuration.getConfiguration(mContext);
		this.processMessage = processMessage;
		this.tag = tag;
		this.callback = callback;
	}

	public void addNameValuePair(String name, String value) {

		params.add(new BasicNameValuePair(name, value));
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	private void showProgressDialog() {

		pDlg = new ProgressDialog(mContext);
		pDlg.setMessage(processMessage);
		pDlg.setProgressDrawable(mContext.getWallpaper());
		pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDlg.setCancelable(false);
		pDlg.show();

	}

	@Override
	protected void onPreExecute() {

		// hideKeyboard();
		showProgressDialog();

	}

	protected String doInBackground(String... urls) {

		String url = urls[0];
		String result = "";

		HttpResponse response = doResponse(url);

		if (response == null) {
			return result;
		} else {

			try {

				result = inputStreamToString(response.getEntity().getContent());

			} catch (IllegalStateException e) {
				Log.e(TAG, e.getLocalizedMessage(), e);

			} catch (IOException e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
			}

		}

		return result;
	}

	@Override
	protected void onPostExecute(String response) {

		callback.onCallback(tag, response);
		pDlg.dismiss();

	}

	// Establish connection and socket (data retrieval) timeouts
	private HttpParams getHttpParams() {

		HttpParams htpp = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
		HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);

		return htpp;
	}

	private HttpResponse doResponse(String url) {

		// Use our connection and data timeouts as parameters for our
		// DefaultHttpClient
		HttpClient httpclient = new DefaultHttpClient(getHttpParams());

		HttpResponse response = null;

		try {
			switch (taskType) {

			case POST:
				HttpPost httppost = new HttpPost(url);
				httppost.addHeader("Authorization",
						"Basic " + configuration.getAuthenticate());
				httppost.setEntity(new UrlEncodedFormEntity(params));
				httppost.setEntity(new StringEntity(payload));

				response = httpclient.execute(httppost);
				break;
			case GET:
				HttpGet httpget = new HttpGet(url);
				httpget.addHeader("Authorization",
						"Basic " + configuration.getAuthenticate());
				response = httpclient.execute(httpget);
				break;
			case PUT:
				HttpPut httpput = new HttpPut(url);
				httpput.addHeader("Authorization",
						"Basic " + configuration.getAuthenticate());
				httpput.addHeader("Content-Type",
						"application/json; charset=utf-8");
				httpput.setEntity(new UrlEncodedFormEntity(params));
				httpput.setEntity(new StringEntity(payload));
				response = httpclient.execute(httpput);
			}
		} catch (Exception e) {

			Log.e(TAG, e.getLocalizedMessage(), e);

		}

		return response;
	}

	private String inputStreamToString(InputStream is) {

		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		try {
			// Read response until the end
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		} catch (IOException e) {
			Log.e(TAG, e.getLocalizedMessage(), e);
		}

		// Return full string
		return total.toString();
	}

}
