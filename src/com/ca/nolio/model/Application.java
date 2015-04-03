package com.ca.nolio.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Application {
	String name;
	String description;
	long id;

	public void LoadFromJson(String json) throws JSONException {
		JSONObject appJsonObject = new JSONObject(json);
		this.name = appJsonObject.getString("name");
		this.description = appJsonObject.getString("description");
		this.id = appJsonObject.getLong("id");
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return name;
	}
}
