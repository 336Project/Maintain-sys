package com.example.Fmb_applianceRepair_System;

import java.util.HashMap;
import java.util.Map;

import com.asynctask.LoginAsyncTask;
import com.constant.Constant;
import com.utils.HttpUtils;
import com.utils.SharePreferenceUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	private EditText et_password;
	private EditText et_username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
	}

	private void initView() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		findViewById(R.id.button1).setOnClickListener(this);
		findViewById(R.id.tv_register).setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			login();
			break;
		case R.id.tv_register:
			startActivity(new Intent(LoginActivity.this,RegisterUserActivity.class));
			break;
		default:
			break;
		}
	}

	private void login() {
		String username = et_username.getText().toString().trim();
		String password = et_password.getText().toString().trim();
		 SharePreferenceUtils.setUserName(this, username);
		 SharePreferenceUtils.setPassWord(this, password);
		if ("".equals(username) || "".equals(password)) {
			Toast.makeText(this, "请输入用户名或密码", Toast.LENGTH_SHORT).show();
		} else {
			new LoginAsyncTask(this,username, password).execute(Constant.LOGIN_URL);
		}
	}
}
