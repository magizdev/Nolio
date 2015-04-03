package com.ca.nolio.model;

import org.json.JSONException;
import org.json.JSONObject;

public class DeploymentLite {
	long id;
	String name;

	public void LoadFromJson(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		this.id = jsonObject.getLong("id");
		this.name = jsonObject.getString("name");
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
