package com.ca.nolio.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Pair;

public class NameIdList extends ArrayList<NameIdItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void LoadFromJson(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray apps = jsonObject.getJSONArray("list");
		for (int i = 0; i < apps.length(); i++) {
			JSONObject app = apps.getJSONObject(i);
			NameIdItem entry = new NameIdItem(app.getString("name"), app.getLong("id"));
			this.add(entry);
		}
	}
}
