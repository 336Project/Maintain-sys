package com.asynctask;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.utils.HttpUtils;
import com.utils.ToastUtils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class UpdateOrderStatusAsyncTask extends AsyncTask<String, Void, String>{
	private Map<String, String> parMap;
	private Context mContext;
	public UpdateOrderStatusAsyncTask(Context mContext,Map<String, String> parMap){
		this.mContext = mContext;
		this.parMap = parMap;
	}
	@Override
	protected String doInBackground(String... params) {
		return HttpUtils.postByApi(params[0], parMap);
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if("".equals(result)){
			ToastUtils.showNetConnectionFail(mContext);
		}
		else{
			try {
				JSONObject jsonObject = new JSONObject(result);
				boolean success = jsonObject.getBoolean("success");
				String msg = jsonObject.getString("msg");
				if(success){
					ToastUtils.showMessage(mContext, msg);
					mContext.sendBroadcast(new Intent("orderUpdate"));
				}
				else{
					ToastUtils.showMessage(mContext, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
