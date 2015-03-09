package com.example.Fmb_applianceRepair_System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bean.Role;
import com.constant.Constant;
import com.utils.HttpUtils;
import com.utils.ToastUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AccountAddActivity extends Activity implements OnClickListener{
	
	private ArrayList<UserSmall> user_List=new ArrayList<UserSmall>();
	private TextView tv_userName;
	private String[] nameStrings;
	private String[] idStrings;
	private String idCode="";
	private EditText et_money;
	private EditText et_remark;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_add);
		initView();
		getUserList();
	}

	private void getUserList() {
		new GetUserListAsyncTask().execute(Constant.user_GetAllUser);
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		findViewById(R.id.tv_save).setOnClickListener(this);
		tv_userName = (TextView) findViewById(R.id.tv_userName);
		tv_userName.setOnClickListener(this);
		et_money = (EditText) findViewById(R.id.et_money);
		et_remark = (EditText) findViewById(R.id.et_remark);
	}
	class GetUserListAsyncTask extends AsyncTask<String, Void, String>{
		protected String doInBackground(String... params) {
			Map<String, String> parMap=new HashMap<String, String>();
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if("".equals(result)){
				ToastUtils.showMessage(AccountAddActivity.this, "网络连接错误，请重试");
			}
			else{
				try {
					user_List.clear();
					JSONObject jsonObject = new JSONObject(result);
					JSONArray jsonArray_msg = jsonObject.getJSONArray("msg");
					for (int i = 0; i < jsonArray_msg.length(); i++) {
						JSONObject jsonObject_msg = jsonArray_msg.getJSONObject(i);
						int id = jsonObject_msg.getInt("id");
						String userName = jsonObject_msg.getString("userName");
						UserSmall userSmall = new UserSmall(id+"", userName);
						user_List.add(userSmall);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	class UserSmall{
		public String id;
		public String userName;
		public UserSmall(String id, String userName) {
			super();
			this.id = id;
			this.userName = userName;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.account_add, menu);
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_userName:
			showUserList();
			break;
		case R.id.tv_save:
			save();
			break;
		default:
			break;
		}
	}
	private void save() {
		String money = et_money.getText().toString().trim();
		String remark = et_remark.getText().toString().trim();
		
		if(tv_userName.getText().toString().trim().equals("")){
			ToastUtils.showMessage(AccountAddActivity.this, "用户名不能为空");
			return;
		}
		if(et_money.getText().toString().trim().equals("")){
			ToastUtils.showMessage(AccountAddActivity.this, "充值不能为空");
			return;
		}
		if(et_remark.getText().toString().trim().equals("")){
			ToastUtils.showMessage(AccountAddActivity.this, "备注不能为空");
			return;
		}
		
		new SaveAccountAsyncTask(money, remark).execute(Constant.account_addByAdmin);
	}
	
	class SaveAccountAsyncTask extends AsyncTask<String, Void, String>{
		private String money;
		private String remark;
		public SaveAccountAsyncTask(String money,String remark){
			this.money = money;
			this.remark = remark;
		}
		protected String doInBackground(String... params) {
			Map<String, String> parMap=new HashMap<String, String>();
			parMap.put("id", idCode);
			parMap.put("money", money);
			parMap.put("remark", remark);
			
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if("".equals(result)){
				ToastUtils.showNetConnectionFail(AccountAddActivity.this);
			}
			else{
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.getBoolean("success");
					String msg = jsonObject.getString("msg");
					if(success){
						Intent intent=new Intent();
						intent.setAction("updateaccount");
						sendBroadcast(intent);
					}
					ToastUtils.showMessage(AccountAddActivity.this, msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private void showUserList() {
		nameStrings = new String[user_List.size()];
		idStrings = new String[user_List.size()];
		for (int i = 0; i < user_List.size(); i++) {
			UserSmall userSmall = user_List.get(i);
			nameStrings[i]=userSmall.userName;
			idStrings[i]=userSmall.id;
		}
		new AlertDialog.Builder(AccountAddActivity.this)
		.setTitle("用户选择")
		.setItems(nameStrings, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String codeName = nameStrings[which];
				idCode=idStrings[which];
				tv_userName.setText(codeName);
			}
		}).create().show();
	}
}
