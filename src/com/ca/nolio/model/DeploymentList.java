package com.ca.nolio.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeploymentList extends ArrayList<DeploymentLite> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void LoadFromJson(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray deployments = jsonObject.getJSONArray("pageResults");
		for (int i = 0; i < deployments.length(); i++) {
			JSONObject deploymentObj = deployments.getJSONObject(i);
			DeploymentLite deployment = new DeploymentLite();
			deployment.LoadFromJson(deploymentObj.toString());
			this.add(deployment);
		}
	}

}
