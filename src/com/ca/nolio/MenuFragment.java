package com.ca.nolio;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public abstract class MenuFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(layoutResource(), container, false);
		return rootView;
	}

	public abstract int layoutResource();

	public boolean onCreateOptionsMenu(MenuInflater inflater, Menu menu) {
		if (optionMenuResource() != 0) {
			inflater.inflate(optionMenuResource(), menu);
		} else {
			menu.clear();
		}
		return true;
	}

	public abstract int optionMenuResource();

	public abstract boolean onOptionsItemSelected(MenuItem item);
}
