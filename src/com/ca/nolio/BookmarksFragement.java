package com.ca.nolio;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.ca.nolio.adapters.BookmarkAdapter;
import com.ca.nolio.interfaces.INolioServiceCallback;
import com.ca.nolio.model.Bookmark;
import com.ca.nolio.service.NolioService;
import com.ca.nolio.util.Configuration;

public class BookmarksFragement extends MenuFragment {
	static final String UPLOAD_BOOKMARK_SERVICE_STRING = "http://%s:8080/datamanagement/a/bookmarks";
	static final int SCAN_MODE_SESSION = 1;
	static final int SCAN_MODE_ADD_BOOKMARK = 2;
	private Configuration configuration;
	private int scan_mode = 2;
	private ListView bookmarkList;
	private List<Bookmark> bookmarks;
	private String sacnResult;
	private BookmarkAdapter adapter;

	@Override
	public int layoutResource() {
		return R.layout.bookmark_list;
	}

	@Override
	public int optionMenuResource() {
		return R.menu.bookmarks_list;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");// for Qr code, its
														// "QR_CODE_MODE"
														// instead of
														// "PRODUCT_MODE"
		intent.putExtra("SAVE_HISTORY", false);// this stops saving ur barcode
												// in barcode scanner app's
												// history
		startActivityForResult(intent, 0);
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (resultCode == Activity.RESULT_OK) {
				sacnResult = data.getStringExtra("SCAN_RESULT");
				// the
				// result
				if (sacnResult.equals("abc")) {
					scan_mode = SCAN_MODE_SESSION;
				} else {
					scan_mode = SCAN_MODE_ADD_BOOKMARK;
					ResultDialog addingBookmarkDialog = new ResultDialog(
							getActivity(), sacnResult);
					addingBookmarkDialog.show();
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				// Handle cancel
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container,
				savedInstanceState);
		configuration = new Configuration(this.getActivity());
		bookmarkList = (ListView) rootView.findViewById(R.id.bookmarkList);
		bookmarkList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (scan_mode == SCAN_MODE_SESSION) {
					String payload = adapter.getItemJson(arg2);
					String redirectSession = String.format(
							UPLOAD_BOOKMARK_SERVICE_STRING,
							configuration.getServer());
					NolioService serviceCall = new NolioService(
							NolioService.POST, getActivity(), "...", "TAG",
							new INolioServiceCallback() {

								@Override
								public void onCallback(String tag,
										String response) {
									// TODO Auto-generated method stub

								}
							});
					serviceCall.setPayload(payload);
					serviceCall.execute(redirectSession);
					scan_mode = SCAN_MODE_ADD_BOOKMARK;
				}

			}
		});
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		bookmarks = new Select().from(Bookmark.class).execute();
		adapter = new BookmarkAdapter(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				bookmarks);
		bookmarkList.setAdapter(adapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}

}
