package com.example.Fmb_applianceRepair_System;

import com.bean.Order;
import com.bean.User;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class UserParticularActivity extends Activity implements OnClickListener{

	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_particular);
		initIntent();
		initView();
	}

	private void initIntent() {
		Intent intent = getIntent();
		user = (User) intent.getSerializableExtra("user");
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		TextView tv_userName = (TextView) findViewById(R.id.tv_userName);
		TextView tv_roleName = (TextView) findViewById(R.id.tv_roleName);
		TextView tv_realName = (TextView) findViewById(R.id.tv_realName);
		TextView tv_nickName = (TextView) findViewById(R.id.tv_nickName);
		TextView tv_email = (TextView) findViewById(R.id.tv_email);
		TextView tv_tel = (TextView) findViewById(R.id.tv_tel);
		TextView tv_balance = (TextView) findViewById(R.id.tv_balance);
		TextView tv_registerTime = (TextView) findViewById(R.id.tv_registerTime);
		TextView tv_lastLoginTime = (TextView) findViewById(R.id.tv_lastLoginTime);
		TextView tv_introduction = (TextView) findViewById(R.id.tv_introduction);
		TextView tv_source = (TextView) findViewById(R.id.tv_source);
		TextView tv_status = (TextView) findViewById(R.id.tv_status);
		
		tv_userName.setText(user.userName);
		tv_roleName.setText(user.roleName);
		tv_realName.setText(user.realName);
		tv_nickName.setText(user.nickName);
		tv_email.setText(user.email);
		tv_tel.setText(user.tel);
		tv_balance.setText(user.balance);
		tv_registerTime.setText(user.registerTime);
		if(!user.lastLoginTime.equals("null")){
			tv_lastLoginTime.setText(user.lastLoginTime);
		}
		if(!user.introduction.equals("null")){
			tv_introduction.setText(user.introduction);
		}
		tv_source.setText(user.source);
		tv_status.setText(user.status);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_particular, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
	}

}
