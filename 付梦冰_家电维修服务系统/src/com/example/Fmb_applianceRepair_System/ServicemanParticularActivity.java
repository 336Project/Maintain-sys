package com.example.Fmb_applianceRepair_System;

import java.io.Serializable;

import com.bean.User;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ServicemanParticularActivity extends Activity {

	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_serviceman_particular);
		initIntent();
		initView();
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		TextView tv_userName = (TextView) findViewById(R.id.tv_userName);
		tv_userName.setText(user.userName);
		TextView tv_realName = (TextView) findViewById(R.id.tv_realName);
		tv_realName.setText(user.realName);
		TextView tv_email = (TextView) findViewById(R.id.tv_email);
		tv_email.setText(user.email);
		TextView tv_tel = (TextView) findViewById(R.id.tv_tel);
		tv_tel.setText(user.tel);
		TextView tv_introduction = (TextView) findViewById(R.id.tv_introduction);
		tv_introduction.setText(user.introduction);
	}

	private void initIntent() {
		user = (User) getIntent().getSerializableExtra("user");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.serviceman_particular, menu);
		return true;
	}

}
