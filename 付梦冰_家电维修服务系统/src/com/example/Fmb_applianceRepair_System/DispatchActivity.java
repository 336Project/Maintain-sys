package com.example.Fmb_applianceRepair_System;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.application.MyApplication;
import com.bean.Order;
import com.bean.User;
import com.constant.Constant;
import com.example.Fmb_applianceRepair_System.RepairActivity.ListCompanyAsyncTask;
import com.utils.HttpUtils;
import com.utils.ToastUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 派遣工人
 * 
 * @author Administrator
 * 
 */
public class DispatchActivity extends Activity implements OnClickListener {

	private Order order;
	private TextView tv_serviceman;
	private ArrayList<User> user_List = new ArrayList<User>();
	private String[] companyNameStrings;
	private String[] companyIdStrings;
	protected String idCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dispatch);
		initIntent();
		initView();
		getListServiceMan();
	}

	private void getListServiceMan() {
		new ListServiceManAsyncTask()
				.execute(Constant.getServicemanAllList_URL);
	}

	private void initIntent() {
		Intent intent = getIntent();
		order = (Order) intent.getSerializableExtra("order");
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		findViewById(R.id.tv_real).setOnClickListener(this);
		TextView tv_repairContent = (TextView) findViewById(R.id.tv_repairContent);
		tv_repairContent.setText(order.repairContent);
		TextView tv_telCustom = (TextView) findViewById(R.id.tv_telCustom);
		tv_telCustom.setText(order.contactTelUser);
		TextView tv_address = (TextView) findViewById(R.id.tv_address);
		tv_address.setText(order.address);
		tv_serviceman = (TextView) findViewById(R.id.tv_serviceman);
		tv_serviceman.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.dispatch, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_serviceman:
			selectServiceman();
			break;
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_real:
			inputServiceman();
			break;
		default:
			break;
		}
	}

	/**
	 * 确定派遣维修人员
	 */
	private void inputServiceman() {
		String serviceman = tv_serviceman.getText().toString().trim();
		if (serviceman.equals("")) {
			ToastUtils.showMessage(this, "请先选择维修人员");
			return;
		} else {
			Map<String, String> parMap=new HashMap<String, String>();
			parMap.put("orderId", order.id+"");
			parMap.put("servicemanId", idCode);
			new InputServicemanAsyncTask(parMap).execute(Constant.dispatchServiceman_URL);
		}

	}
	class InputServicemanAsyncTask extends AsyncTask<String, Void, String>{
		private Map<String, String> parMap;
		public InputServicemanAsyncTask(Map<String, String> parMap){
			this.parMap = parMap;
		}
		@Override
		protected String doInBackground(String... params) {
			String postByApi = HttpUtils.postByApi(params[0], parMap);
			return postByApi;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if("".equals(result)){
				ToastUtils.showNetConnectionFail(DispatchActivity.this);
			}
			else{
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.getBoolean("success");
					String msg = jsonObject.getString("msg");
					if(success){
						sendBroadcast(new Intent("orderUpdate"));
						ToastUtils.showMessage(DispatchActivity.this, "操作成功");
						finish();
					}
					else{
						ToastUtils.showMessage(DispatchActivity.this, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}

	private void selectServiceman() {
		companyNameStrings = new String[user_List.size()];
		companyIdStrings = new String[user_List.size()];
		for (int i = 0; i < user_List.size(); i++) {
			User user = user_List.get(i);
			companyNameStrings[i] = user.realName;
			companyIdStrings[i] = user.id + "";
		}
		new AlertDialog.Builder(DispatchActivity.this)
				.setTitle("选择维修人员")
				.setItems(companyNameStrings,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String codeName = companyNameStrings[which];
								idCode = companyIdStrings[which];
								tv_serviceman.setText(codeName);
							}
						}).create().show();
	}

	class ListServiceManAsyncTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			MyApplication application = (MyApplication) getApplication();
			Map<String, String> parMap = new HashMap<String, String>();
			parMap.put("parentId", application.getUser().id + "");
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if ("".equals(result)) {
				ToastUtils.showNetConnectionFail(DispatchActivity.this);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					user_List.clear();
					JSONArray jsonArray_data = jsonObject.getJSONArray("msg");
					for (int i = 0; i < jsonArray_data.length(); i++) {
						JSONObject jsonObject_data = jsonArray_data
								.getJSONObject(i);
						String realName = jsonObject_data.getString("realName");
						String userName = jsonObject_data.getString("userName");
						String roleName = jsonObject_data.getString("roleName");
						String nickName = jsonObject_data.getString("nickName");
						String balance = jsonObject_data.getString("balance");
						String email = jsonObject_data.getString("email");
						String tel = jsonObject_data.getString("tel");
						String source = jsonObject_data.getString("source");
						String status = jsonObject_data.getString("status");
						String lastLoginTime = jsonObject_data
								.getString("lastLoginTime");
						String registerTime = jsonObject_data
								.getString("registerTime");
						String introduction = jsonObject_data
								.getString("introduction");
						int id = jsonObject_data.getInt("id");
						int roleCode = jsonObject_data.getInt("roleCode");
						User user = new User(id, roleCode, roleName, userName,
								realName, nickName, balance, email, tel,
								source, status, lastLoginTime, registerTime,
								introduction);
						user_List.add(user);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
