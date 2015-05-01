package com.example.Fmb_applianceRepair_System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.application.MyApplication;
import com.bean.User;
import com.constant.Constant;
import com.example.Fmb_applianceRepair_System.OrderUpdateActivity.SaveOrderAsyncTask;
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
/**
 * @author Administrator
 *报修界面
 */
public class RepairActivity extends Activity implements OnClickListener{

	private EditText et_content;
	private EditText et_tel;
	private TextView tv_company;
	private ArrayList<User> user_List = new ArrayList<User>();
	private String[] companyNameStrings;
	private String[] companyIdStrings;
	protected String idCode;
	private User user;
	private EditText et_address;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repair);
		initView();
		initUser();
		getListCompany();
	}
	
	private void initUser() {
		user = ((MyApplication)getApplication()).getUser();
	}

	//获得公司列表
	private void getListCompany() {
		new ListCompanyAsyncTask().execute(Constant.GETLISTCOMPANY_URL);
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		et_content = (EditText) findViewById(R.id.et_content);
		et_tel = (EditText) findViewById(R.id.et_tel);
		et_address = (EditText) findViewById(R.id.et_address);
		tv_company = (TextView) findViewById(R.id.tv_company);
		tv_company.setOnClickListener(this);
		findViewById(R.id.tv_realtopup).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.repair, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_company:
			selectCompany();
			break;
		case R.id.tv_realtopup:
			save();
			break;
		}
	}
	private void save() {
		if (et_content.getText().toString().trim().equals("")) {
			ToastUtils.showMessage(RepairActivity.this, "维修内容不能为空");
			return;
		}
		if (et_tel.getText().toString().trim().equals("")) {
			ToastUtils.showMessage(RepairActivity.this, "联系电话不能为空");
			return;
		}
		if (tv_company.getText().toString().trim().equals("")) {
			ToastUtils.showMessage(RepairActivity.this, "维修公司不能为空");
			return;
		}
		new SaveOrderAsyncTask().execute(Constant.order_Repair);
	}
	
	class SaveOrderAsyncTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {

			String repairContent = et_content.getText().toString().trim();
			String contactTelUser = et_tel.getText().toString().trim();
			String address = et_address.getText().toString().trim();
			Map<String, String> parMap = new HashMap<String, String>();
			parMap.put("companyId", idCode);
			parMap.put("contactTelUser", contactTelUser);
			parMap.put("repairContent", repairContent);
			parMap.put("customerUser", user.realName);
			parMap.put("ids", user.id+"");
			parMap.put("address",address);
			
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if ("".equals(result)) {
				ToastUtils.showNetConnectionFail(RepairActivity.this);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					String msg = jsonObject.getString("msg");
					boolean success = jsonObject.getBoolean("success");
					if (success) {
						ToastUtils.showMessage(RepairActivity.this, msg);
						Intent intent = new Intent();
						intent.setAction("orderUpdate");
						sendBroadcast(intent);
						finish();
					} else {
						ToastUtils.showMessage(RepairActivity.this, msg);
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
		new AlertDialog.Builder(RepairActivity.this)
				.setTitle("用户选择")
				.setItems(companyNameStrings,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								String codeName = companyNameStrings[which];
								idCode = companyIdStrings[which];
								tv_company.setText(codeName);
							}
						}).create().show();
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
				ToastUtils.showNetConnectionFail(RepairActivity.this);
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
