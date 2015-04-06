package com.ca.nolio.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeploymentLite {
	long id;
	String name;
	double progress;

	public void LoadFromJson(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		this.id = jsonObject.getLong("id");
		this.name = jsonObject.getString("name");
		JSONArray stages = jsonObject.getJSONArray("shallowStages");
		for (int i = 0; i < stages.length(); i++) {
			if (stages.getJSONObject(i).getString("type")
					.equalsIgnoreCase("run")) {
				progress = stages.getJSONObject(i).getDouble("progress");
			}
		}
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getProgress() {
		return progress;
	}

	@Override
	public String toString() {
		return name;
	}
}
