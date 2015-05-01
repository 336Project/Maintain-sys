package com.example.Fmb_applianceRepair_System;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.application.MyApplication;
import com.constant.Constant;
import com.utils.HttpUtils;
import com.utils.ToastUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class ServicemanAddActivity extends Activity implements OnClickListener {

	private EditText et_userName;
	private EditText et_realName;
	private EditText et_email;
	private EditText et_tel;
	private EditText et_introduction;
	private EditText et_password;
	private EditText et_passwordAgain;
	private AsyncTask<String, Void, String> addUserAsyncTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_serviceman_add);
		initView();
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		findViewById(R.id.tv_save).setOnClickListener(this);
		et_userName = (EditText) findViewById(R.id.et_userName);
		et_realName = (EditText) findViewById(R.id.et_realName);
		et_email = (EditText) findViewById(R.id.et_email);
		et_tel = (EditText) findViewById(R.id.et_tel);
		et_introduction = (EditText) findViewById(R.id.et_introduction);
		et_password = (EditText) findViewById(R.id.et_password);
		et_passwordAgain = (EditText) findViewById(R.id.et_passwordAgain);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.serviceman_add, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_save:
			save();
			break;
		default:
			break;
		}
	}
	/**
	 * 保存维修人员信息
	 */
	private void save() {
		String userName = et_userName.getText().toString().trim();
		String realName = et_realName.getText().toString().trim();
		String nickName = realName;
		String email = et_email.getText().toString().trim();
		String tel = et_tel.getText().toString().trim();
		String introduction = et_introduction.getText().toString().trim();
		String password = et_password.getText().toString().trim();
		String passwordAgain = et_passwordAgain.getText().toString().trim();
		String roleCode ="4";
		if(userName.equals("")){
			ToastUtils.showMessage(this, "用户名不能为空~");
			return;
		}
		if(password.equals("")){
			ToastUtils.showMessage(this, "密码不能为空~");
			return;
		}
		if(email.equals("")){
			ToastUtils.showMessage(this, "邮箱不能为空~");
			return;
		}
		if(tel.equals("")){
			ToastUtils.showMessage(this, "手机不能为空~");
			return;
		}
		if(!password.equals(passwordAgain)){
			ToastUtils.showMessage(this, "两次输入的密码不一致~");
			return;
		}
		Map<String, String> parMap=new HashMap<String, String>();
		parMap.put("userName", userName);
		parMap.put("email", email);
		parMap.put("tel", tel);
		parMap.put("realName", realName);
		parMap.put("nickName", nickName);
		parMap.put("password", password);
		parMap.put("roleCode", roleCode);
		parMap.put("introduction", introduction);
		MyApplication application = (MyApplication) getApplication();
		parMap.put("parentId", application.getUser().id+"");
		if(addUserAsyncTask!=null)
			addUserAsyncTask.cancel(true);
		addUserAsyncTask = new AddUserAsyncTask(parMap).execute(Constant.user_RegisterUser);
	}
	
	class AddUserAsyncTask extends AsyncTask<String, Void, String>{
		private Map<String, String> parMap;

		public AddUserAsyncTask(Map<String, String> parMap){
			this.parMap = parMap;
		}
		protected String doInBackground(String... params) {
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				boolean success = jsonObject.getBoolean("success");
				String msg = jsonObject.getString("msg");
				if(success){
					ToastUtils.showMessage(ServicemanAddActivity.this, msg);
					sendBroadcast(new Intent("updateServicemanList"));
				}
				else{
					ToastUtils.showMessage(ServicemanAddActivity.this, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
