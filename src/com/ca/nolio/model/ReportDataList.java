package com.ca.nolio.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReportDataList extends ArrayList<ReportData> {

	public void loadFromJson(String jsonString) throws JSONException {
		JSONObject returnResult = new JSONObject(jsonString);
		JSONArray jsonArray = returnResult.getJSONArray("pageResults");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			ReportData data = new ReportData();
			data.loadFromJson(jsonObject.toString());
			this.add(data);
		}
	}
}
