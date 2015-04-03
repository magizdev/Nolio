package com.ca.nolio.adapters;

import java.util.List;
import java.util.zip.Inflater;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ca.nolio.R;
import com.ca.nolio.model.ReportData;

public class ReportAdapter extends ArrayAdapter<ReportData> {
	private Context context;

	public ReportAdapter(Context context, int layout, int resource,
			List<ReportData> objects) {
		super(context, layout, resource, objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View row = inflater.inflate(R.layout.row, null);
		if (position % 2 == 0)
			row.setBackgroundColor(Color.rgb(0xF9, 0xFA, 0xFC));
		else
			row.setBackgroundColor(Color.rgb(0xF3, 0xF4, 0xF8));

		TextView application = (TextView) row.findViewById(R.id.col1);
		TextView name = (TextView) row.findViewById(R.id.col2);
		TextView duration = (TextView) row.findViewById(R.id.col3);
		TextView status = (TextView) row.findViewById(R.id.col4);

		ReportData data = getItem(position);
		application.setText(data.getApplicationName());
		name.setText(data.getName());
		duration.setText(data.getDuration() + "");
		status.setText(data.getStatus());
		return row;
	}
}
