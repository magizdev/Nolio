package com.ca.nolio.adapters;

import java.util.List;

import com.ca.nolio.model.DeploymentLite;

import android.content.Context;
import android.widget.ArrayAdapter;

public class DeploymentListAdapter extends ArrayAdapter<DeploymentLite> {
	List<DeploymentLite> deployments;

	public DeploymentListAdapter(Context context, int resource,
			int textViewResourceId, List<DeploymentLite> deployments) {
		super(context, resource, textViewResourceId, deployments);
		this.deployments = deployments;
	}

	@Override
	public long getItemId(int position) {
		return deployments.get(position).getId();
	}

}
