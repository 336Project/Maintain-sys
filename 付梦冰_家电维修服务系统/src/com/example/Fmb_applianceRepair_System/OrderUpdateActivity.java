package com.example.Fmb_applianceRepair_System;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bean.Order;
import com.bean.User;
import com.constant.Constant;
import com.example.Fmb_applianceRepair_System.AccountAddActivity.UserSmall;
import com.example.Fmb_applianceRepair_System.RepairActivity.ListCompanyAsyncTask;
import com.utils.HttpUtils;
import com.utils.ToastUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class OrderUpdateActivity extends Activity implements OnClickListener {

	private Order order;
	private EditText et_content;
	private EditText et_contactTelUser;
	private TextView tv_customerCompany;
	private ArrayList<User> user_List = new ArrayList<User>();
	private String[] companyNameStrings;
	private String[] companyIdStrings;
	protected String idCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_update);
		initIntent();
		initView();
		getListCompany();
	}

	private void getListCompany() {
		new ListCompanyAsyncTask().execute(Constant.GETLISTCOMPANY_URL);
	}

	class ListCompanyAsyncTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			Map<String, String> parMap = new HashMap<String, String>();
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if ("".equals(result)) {
				ToastUtils.showNetConnectionFail(OrderUpdateActivity.this);
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

	private void initIntent() {
		Intent intent = getIntent();
		order = (Order) intent.getSerializableExtra("order");
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		findViewById(R.id.tv_save).setOnClickListener(this);
		et_content = (EditText) findViewById(R.id.et_content);
		et_contactTelUser = (EditText) findViewById(R.id.et_contactTelUser);
		tv_customerCompany = (TextView) findViewById(R.id.tv_customerCompany);
		tv_customerCompany.setOnClickListener(this);
		et_content.setText(order.repairContent);
		et_contactTelUser.setText(order.contactTelUser);
		tv_customerCompany.setText(order.customerCompany);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.account_update, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_customerCompany:
			selectCompany();
			break;
		case R.id.tv_save:
			save();
			break;
		default:
			break;
		}
	}

	private void save() {
		if (et_content.getText().toString().trim().equals("")) {
			ToastUtils.showMessage(OrderUpdateActivity.this, "维修内容不能为空");
			return;
		}
		if (et_contactTelUser.getText().toString().trim().equals("")) {
			ToastUtils.showMessage(OrderUpdateActivity.this, "联系电话不能为空");
			return;
		}
		if (tv_customerCompany.getText().toString().trim().equals("")) {
			ToastUtils.showMessage(OrderUpdateActivity.this, "维修公司不能为空");
			return;
		}
		new SaveOrderAsyncTask().execute(Constant.order_Update);
	}

	class SaveOrderAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			String repairContent = et_content.getText().toString().trim();
			String contactTelUser = et_contactTelUser.getText().toString()
					.trim();
			Map<String, String> parMap = new HashMap<String, String>();
			parMap.put("orderId", order.id + "");
			parMap.put("companyId", idCode);
			parMap.put("contactTelUser", contactTelUser);
			parMap.put("repairContent", repairContent);
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if ("".equals(result)) {
				ToastUtils.showNetConnectionFail(OrderUpdateActivity.this);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					String msg = jsonObject.getString("msg");
					boolean success = jsonObject.getBoolean("success");
					if (success) {
						ToastUtils.showMessage(OrderUpdateActivity.this, msg);
						Intent intent = new Intent();
						intent.setAction("orderUpdate");
						sendBroadcast(intent);
						finish();
					} else {
						ToastUtils.showMessage(OrderUpdateActivity.this, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void selectCompany() {
		companyNameStrings = new String[user_List.size()];
		companyIdStrings = new String[user_List.size()];
		for (int i = 0; i < user_List.size(); i++) {
			User user = user_List.get(i);
			companyNameStrings[i] = user.realName;
			companyIdStrings[i] = user.id + "";
		}
		new AlertDialog.Builder(OrderUpdateActivity.this)
				.setTitle("用户选择")
				.setItems(companyNameStrings,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String codeName = companyNameStrings[which];
								idCode = companyIdStrings[which];
								tv_customerCompany.setText(codeName);
							}
						}).create().show();
	}
}
