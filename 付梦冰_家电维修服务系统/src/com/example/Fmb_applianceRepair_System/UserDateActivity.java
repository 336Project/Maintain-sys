package com.example.Fmb_applianceRepair_System;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.application.MyApplication;
import com.bean.User;
import com.constant.Constant;
import com.utils.HttpUtils;
import com.utils.SharePreferenceUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class UserDateActivity extends Activity implements OnClickListener{

	private User user;
	private TextView tv_userName;
	private TextView tv_realName;
	private TextView tv_nickName;
	private TextView tv_balance;
	private TextView tv_email;
	private TextView tv_tel;
	private TextView tv_status;
	private TextView tv_source;
	private TextView tv_lastLoginTime;
	private TextView tv_registerTime;
	private TextView tv_introduction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_date);
		
	}
	private void initView() {
		tv_userName = (TextView) findViewById(R.id.tv_userName);
		tv_realName = (TextView) findViewById(R.id.tv_realName);
		tv_nickName = (TextView) findViewById(R.id.tv_nickName);
		tv_balance = (TextView) findViewById(R.id.tv_balance);
		tv_email = (TextView) findViewById(R.id.tv_email);
		tv_tel = (TextView) findViewById(R.id.tv_tel);
		tv_status = (TextView) findViewById(R.id.tv_status);
		tv_source = (TextView) findViewById(R.id.tv_source);
		tv_lastLoginTime = (TextView) findViewById(R.id.tv_lastLoginTime);
		tv_registerTime = (TextView) findViewById(R.id.tv_registerTime);
		tv_introduction = (TextView) findViewById(R.id.tv_introduction);
		findViewById(R.id.iv_back).setOnClickListener(this);
		findViewById(R.id.tv_edit).setOnClickListener(this);
		String userName = SharePreferenceUtils.getUserName(this);
		String passWord = SharePreferenceUtils.getPassWord(this);
		new LoginAsyncTask(userName, passWord).execute(Constant.LOGIN_URL);
	}
	@Override
	protected void onStart() {
		super.onStart();
		initView();
		Log.e("hehe", "hehe");
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_date, menu);
		return true;
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_edit:
			Intent intent=new Intent();
			intent.setClass(UserDateActivity.this, UserDateUpdateActivity.class);
			startActivity(intent);
		}
	}
	class LoginAsyncTask extends AsyncTask<String, Void, String>{
		public String username;
		public String password;
		private Activity activity;
		public LoginAsyncTask(String username, String password) {
			super();
			this.username = username;
			this.password = password;
		}
		protected String doInBackground(String... params) {
			Map<String, String> parMap = new HashMap<String, String>();
			parMap.put("username", username);
			parMap.put("password", password);
			String result = HttpUtils.postByApi(params[0],
							parMap);
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if ("null".equals(result)) {
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONObject jsonObject_msg = jsonObject.getJSONObject("msg");
					String realName = jsonObject_msg.getString("realName");
					String userName = jsonObject_msg.getString("userName");
					String roleName = jsonObject_msg.getString("roleName");
					String nickName = jsonObject_msg.getString("nickName");
					String balance = jsonObject_msg.getString("balance");
					String email = jsonObject_msg.getString("email");
					String tel = jsonObject_msg.getString("tel");
					String source = jsonObject_msg.getString("source");
					String status = jsonObject_msg.getString("status");
					String lastLoginTime = jsonObject_msg.getString("lastLoginTime");
					String registerTime = jsonObject_msg.getString("registerTime");
					String introduction = jsonObject_msg.getString("introduction");
					int id = jsonObject_msg.getInt("id");
					int roleCode = jsonObject_msg.getInt("roleCode");
					User user = new User(id, roleCode, roleName, userName, realName,nickName,balance,email,tel,source,status,lastLoginTime,registerTime,introduction);
					MyApplication application = (MyApplication) getApplication();
					application.setUser(user);
					
					tv_realName.setText(realName);
					tv_userName.setText(userName);
					tv_nickName.setText(nickName);
					tv_balance.setText("гд"+balance);
					tv_email.setText(email);
					tv_tel.setText(tel);
					tv_status.setText(status);
					tv_source.setText(source);
					if(!lastLoginTime.equals("null")){
						tv_lastLoginTime.setText(lastLoginTime);
					}
					tv_registerTime.setText(registerTime);
					tv_introduction.setText(introduction);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			System.out.println(result);
		}
	}
}
