package com.ca.nolio;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ca.nolio.model.Bookmark;

public class ResultDialog extends Dialog implements OnClickListener {

	private Context context;
	private Bookmark bookmark;

	public ResultDialog(Context context, String jobJson) {
		super(context);
		this.context = context;
		this.setContentView(R.layout.dialog_view);
		TextView jobName = (TextView) findViewById(R.id.jobName);
		Button follow = (Button) findViewById(R.id.btn_follow);

		JSONObject job;
		String jobNameString = "default";
		try {
			job = new JSONObject(jobJson);
			jobNameString = job.getString("jobName");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jobName.setText(jobNameString);
		bookmark = new Bookmark();
		bookmark.name = jobNameString;
		bookmark.postJson = jobJson;
		follow.setOnClickListener(this);

		this.setCancelable(false);
	}

	@Override
	public void onClick(View v) {
		bookmark.save();
		this.dismiss();
	}

}
