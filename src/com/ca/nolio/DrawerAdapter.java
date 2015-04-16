package com.ca.nolio;

import java.util.Arrays;
import java.util.List;

import com.ca.nolio.adapters.ApplicationListAdapter;
import com.ca.nolio.model.ApplicationList;
import com.ca.nolio.util.Configuration;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class DrawerAdapter extends BaseAdapter {
	private Context context;
	private List<String> labels;
	private int selectedIndex;
	private Configuration configuration;
	private ApplicationList applicationList;

	public DrawerAdapter(Context context, ApplicationList applicationList) {
		this.context = context;
		labels = Arrays.asList(context.getResources().getStringArray(
				R.array.Pages));
		selectedIndex = 0;
		configuration = Configuration.getConfiguration(context);
		this.applicationList = applicationList;

	}

	public void select(int index) {
		selectedIndex = index;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return labels.size();
	}

	@Override
	public Object getItem(int position) {
		return labels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		HeaderHolder headerHolder;

		if (position == 0) {
			if (convertView == null) {
				headerHolder = new HeaderHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.drawer_header, null);
				headerHolder.username = (TextView) convertView
						.findViewById(R.id.loggedInUser);
				headerHolder.applications = (Spinner) convertView
						.findViewById(R.id.applications);
				final ApplicationListAdapter adapter = new ApplicationListAdapter(
						context, android.R.layout.simple_list_item_1,
						android.R.id.text1, applicationList);
				headerHolder.applications.setAdapter(adapter);

				headerHolder.applications
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						long applicationId = adapter.getItemId(arg2);
						String applicationName = adapter.getApplicaitonName(arg2);
						configuration.setApplication(applicationName, applicationId);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

			} else {
				headerHolder = (HeaderHolder) convertView.getTag();
			}

//			headerHolder.username.setText(configuration.getUsername());

		} else {
			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.drawer_item, null);
				viewHolder.label = (TextView) convertView
						.findViewById(R.id.drawerItemName);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.label.setText(labels.get(position));
			if (position == selectedIndex) {
				viewHolder.label.setBackgroundColor(Color.WHITE);
				viewHolder.label.setTextColor(Color.BLACK);
			} else {
				viewHolder.label.setBackgroundColor(Color.TRANSPARENT);
				viewHolder.label.setTextColor(Color.WHITE);
			}
		}
		return convertView;
	}

	class ViewHolder {
		public TextView label;
	}

	class HeaderHolder {
		public TextView username;
		public Spinner applications;
	}

}
