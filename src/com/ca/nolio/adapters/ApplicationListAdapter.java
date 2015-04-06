package com.ca.nolio.adapters;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.ca.nolio.model.Application;

public class ApplicationListAdapter extends ArrayAdapter<Application> {
	private List<Application> applications;

	public ApplicationListAdapter(Context context, int resource,
			int textViewResourceId, List<Application> applications) {
		super(context, resource, textViewResourceId, applications);
		this.applications = applications;
	}

	@Override
	public long getItemId(int position) {
		return applications.get(position).getId();
	}

	public String getApplicaitonName(int position) {
		return applications.get(position).getName();
	}

}
