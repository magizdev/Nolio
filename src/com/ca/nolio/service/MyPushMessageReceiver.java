package com.ca.nolio.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.ca.nolio.DeploymentListFragement;
import com.ca.nolio.LoginActivity;
import com.ca.nolio.MainActivity;
import com.ca.nolio.interfaces.INolioServiceCallback;
import com.ca.nolio.util.Configuration;
import com.ca.nolio.util.Utils;

public class MyPushMessageReceiver extends FrontiaPushMessageReceiver implements INolioServiceCallback {
    /** TAG to Log */
    public static final String TAG = MyPushMessageReceiver.class
            .getSimpleName();
    
    private static final String SERVICE_BASE_URL = "http://%s:8080/datamanagement/a/shallow_applications?DeviceId=%s";

    @Override
    public void onBind(Context context, int errorCode, String appid,
            String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        if (errorCode == 0) {
            Utils.setBind(context, true);
        }
        Configuration configuration = Configuration.getConfiguration(context);
		NolioService nolioServiceCall = new NolioService(
				NolioService.GET, context, null,
				"", MyPushMessageReceiver.this);
		nolioServiceCall.execute(String.format(SERVICE_BASE_URL,
				configuration.getServer(), channelId));
    }

    /**
     * 接收�?传消�?�的函数。
     * 
     * @param context
     *            上下文
     * @param message
     *            推�?的消�?�
     * @param customContentString
     *            自定义内容,为空或者json字符串
     */
    @Override
    public void onMessage(Context context, String message,
            String customContentString) {
        String messageString = "�?传消�?� message=\"" + message
                + "\" customContentString=" + customContentString;
        Log.d(TAG, messageString);

        // 自定义内容获�?�方�?，mykey和myvalue对应�?传消�?�推�?时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Demo更新界�?�展示代�?，应用请在这里加入自己的处�?�逻辑
        updateContent(context, messageString);
    }

    /**
     * 接收通知点击的函数。注：推�?通知被用户点击�?，应用无法通过接�?�获�?�通知的内容。
     * 
     * @param context
     *            上下文
     * @param title
     *            推�?的通知的标题
     * @param description
     *            推�?的通知的�??述
     * @param customContentString
     *            自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title,
            String description, String customContentString) {
        String notifyString = "通知点击 title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        Log.d(TAG, notifyString);

        // 自定义内容获�?�方�?，mykey和myvalue对应通知推�?时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Demo更新界�?�展示代�?，应用请在这里加入自己的处�?�逻辑
        updateContent(context, notifyString);
    }

    /**
     * setTags() 的回调函数。
     * 
     * @param context
     *            上下文
     * @param errorCode
     *            错误�?。0表示�?些tag已�?设置�?功；�?�0表示所有tag的设置�?�失败。
     * @param successTags
     *            设置�?功的tag
     * @param failTags
     *            设置失败的tag
     * @param requestId
     *            分�?给对云推�?的请求的id
     */
    @Override
    public void onSetTags(Context context, int errorCode,
            List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo更新界�?�展示代�?，应用请在这里加入自己的处�?�逻辑
        updateContent(context, responseString);
    }

    /**
     * delTags() 的回调函数。
     * 
     * @param context
     *            上下文
     * @param errorCode
     *            错误�?。0表示�?些tag已�?删除�?功；�?�0表示所有tag�?�删除失败。
     * @param successTags
     *            �?功删除的tag
     * @param failTags
     *            删除失败的tag
     * @param requestId
     *            分�?给对云推�?的请求的id
     */
    @Override
    public void onDelTags(Context context, int errorCode,
            List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo更新界�?�展示代�?，应用请在这里加入自己的处�?�逻辑
        updateContent(context, responseString);
    }

    /**
     * listTags() 的回调函数。
     * 
     * @param context
     *            上下文
     * @param errorCode
     *            错误�?。0表示列举tag�?功；�?�0表示失败。
     * @param tags
     *            当�?应用设置的所有tag。
     * @param requestId
     *            分�?给对云推�?的请求的id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
            String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags="
                + tags;
        Log.d(TAG, responseString);

        // Demo更新界�?�展示代�?，应用请在这里加入自己的处�?�逻辑
        updateContent(context, responseString);
    }

    /**
     * PushManager.stopWork() 的回调函数。
     * 
     * @param context
     *            上下文
     * @param errorCode
     *            错误�?。0表示从云推�?解绑定�?功；�?�0表示失败。
     * @param requestId
     *            分�?给对云推�?的请求的id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        Log.d(TAG, responseString);

        // 解绑定�?功，设置未绑定flag，
        if (errorCode == 0) {
            Utils.setBind(context, false);
        }
        // Demo更新界�?�展示代�?，应用请在这里加入自己的处�?�逻辑
        updateContent(context, responseString);
    }

    private void updateContent(Context context, String content) {
        Log.d(TAG, "updateContent");
        String logText = "" + Utils.logStringCache;

        if (!logText.equals("")) {
            logText += "\n";
        }

        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
        logText += sDateFormat.format(new Date()) + ": ";
        logText += content;

        Utils.logStringCache = logText;

        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }

	@Override
	public void onCallback(String tag, String response) {
		// TODO Auto-generated method stub
		
	}

}
