package com.ca.nolio.adapters;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ca.nolio.R;
import com.ca.nolio.model.DeploymentLite;

public class DeploymentListAdapter extends BaseAdapter {
	List<DeploymentLite> deployments;
	
	private LayoutInflater mInflater;
	
	private Context context;

	public DeploymentListAdapter(Context context, List<DeploymentLite> deploymentLites) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.deployments = deploymentLites;
	}


	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		super.unregisterDataSetObserver(observer);
	}

	public int getCount() {
		return deployments.size();
	}

	public Object getItem(int position) {
		return deployments.get(position);
	}

	public long getItemId(int position) {
		return deployments.get(position).getId();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.deployment_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.deploymentName);
			holder.progress = (ProgressBar) convertView
					.findViewById(R.id.deploymentProgress);
			holder.progressTxt = (TextView) convertView.findViewById(R.id.progressTxt);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final long id = deployments.get(position).getId();
		final int tempPosition = position;
		holder.name.setText(deployments.get(position).getName());
		Double progress = deployments.get(position).getProgress();
		holder.progressTxt.setText(progress + "%");
		holder.progress.setProgress(progress.intValue());
		
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return deployments.isEmpty();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	public class ViewHolder {
		public TextView name;
		public ProgressBar progress;
		public TextView progressTxt;
	}


}
