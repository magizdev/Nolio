package com.ca.nolio.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApplicationList extends ArrayList<Application> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void LoadFromJson(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray apps = jsonObject.getJSONArray("list");
		for (int i = 0; i < apps.length(); i++) {
			JSONObject app = apps.getJSONObject(i);
			Application application = new Application();
			application.LoadFromJson(app.toString());
			this.add(application);
		}
	}
}
