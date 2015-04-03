package com.ca.nolio.adapters;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.ca.nolio.model.NameIdItem;

public class NameIdAdapter extends ArrayAdapter<NameIdItem> {
	private List<NameIdItem> items;

	public NameIdAdapter(Context context, int resource, int textViewResourceId,
			List<NameIdItem> items) {
		super(context, resource, textViewResourceId, items);
		this.items = items;
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).second;
	}

}
