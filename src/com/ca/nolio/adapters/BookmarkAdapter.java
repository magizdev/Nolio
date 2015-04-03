package com.ca.nolio.adapters;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.ca.nolio.model.Bookmark;

public class BookmarkAdapter extends ArrayAdapter<Bookmark> {
	private List<Bookmark> bookmarks;

	public BookmarkAdapter(Context context, int resource,
			int textViewResourceId, List<Bookmark> bookmarks) {
		super(context, resource, textViewResourceId, bookmarks);
		this.bookmarks = bookmarks;
	}

	public String getItemJson(int position) {
		return bookmarks.get(position).postJson;
	}

}
