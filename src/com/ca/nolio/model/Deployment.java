package com.ca.nolio.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ca.nolio.model.ApprovalGate.ApprovalGateStatus;

public class Deployment {
	String name;
	String application;
	String environment;
	List<String> steps;
	List<ApprovalGate> approvalGates;
	String status;

	public Deployment() {
		steps = new ArrayList<String>();
		approvalGates = new ArrayList<ApprovalGate>();
	}

	public void loadFromJson(String jsonString) throws JSONException {
		JSONObject jso = new JSONObject(jsonString);

		name = jso.getString("name");
		application = jso.getString("applicationName");
		//environment = jso.getString("environmentName");

		List<Step> stepList = new ArrayList<Step>();
		JSONArray stagesArray = jso.getJSONArray("fullStages");
		for (int i = 0; i < stagesArray.length(); i++) {
			JSONObject stage = stagesArray.getJSONObject(i);
			JSONArray modules = stage.getJSONArray("shallowModules");
			for (int j = 0; j < modules.length(); j++) {
				JSONObject module = modules.getJSONObject(j);
				Step step = new Step();
				step.setId(module.getLong("id"));
				step.setName(module.getString("name"));
				stepList.add(step);
			}
		}
		Collections.sort(stepList, new Step.StepComparator());

		for (Step step : stepList) {
			steps.add(step.name);
		}
		
		JSONObject approvalGates = jso.getJSONObject("approvalGates");
		JSONObject manualApprovalGate = approvalGates.getJSONObject("manualApprovalGate");
		ManualApprovalGate manualApprovalGateObj = new ManualApprovalGate();
		String manualApprovalGateStatus = manualApprovalGate.getString("status");
		if(manualApprovalGateStatus.equals("WAITING_FOR_APPROVAL")){
			manualApprovalGateObj.status = ApprovalGateStatus.WAITING_FOR_APPROVAL;
		}
		this.approvalGates.add(manualApprovalGateObj);
		
		JSONObject status = jso.getJSONObject("releaseStatus");
		this.status = status.getString("releaseStatusText");
	}

	public String getName() {
		return name;
	}

	public String getApplication() {
		return application;
	}

	public String getEnvironment() {
		return environment;
	}

	public List<String> getSteps() {
		return steps;
	}
	
	public List<ApprovalGate> getApprovalGates(){
		return approvalGates;
	}
	
	public String getStatus(){
		return status;
	}
}
