package com.ca.nolio.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReportData {
	long applicationId;
	String applicationName;
	String name;
	String status;
	long duration;

	public void loadFromJson(String jsonString) throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonString);
		this.applicationId = jsonObject.getLong("applicationId");
		this.applicationName = jsonObject.getString("applicationName");
		this.name = jsonObject.getString("name");
		this.status = jsonObject.getJSONObject("releaseStatus").getString(
				"result");
		this.duration = 0;
		JSONArray stages = jsonObject.getJSONArray("shallowStages");
		for (int i = 0; i < stages.length(); i++) {
			JSONObject stage = stages.getJSONObject(i);
			this.duration += stage.getLong("durationUTC");
		}
	}

	public long getApplicationId() {
		return applicationId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public String getName() {
		return name;
	}

	public String getStatus() {
		return status;
	}

	public long getDuration() {
		return duration;
	}
}
