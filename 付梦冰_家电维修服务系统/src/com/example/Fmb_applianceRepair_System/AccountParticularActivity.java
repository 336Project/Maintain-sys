package com.example.Fmb_applianceRepair_System;

import java.io.Serializable;

import com.bean.Account;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AccountParticularActivity extends Activity implements OnClickListener{

	private Account account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_particular);
		initIntent();
		initView();
	}

	private void initIntent() {
		Intent intent = getIntent();
		account = (Account) intent.getSerializableExtra("account");
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		TextView tv_userName = (TextView) findViewById(R.id.tv_userName);
		TextView tv_nickName = (TextView) findViewById(R.id.tv_nickName);
		TextView tv_createTime = (TextView) findViewById(R.id.tv_createTime);
		TextView tv_completeTime = (TextView) findViewById(R.id.tv_completeTime);
		TextView tv_balance = (TextView) findViewById(R.id.tv_balance);
		TextView tv_money = (TextView) findViewById(R.id.tv_money);
		TextView tv_type = (TextView) findViewById(R.id.tv_type);
		TextView tv_source = (TextView) findViewById(R.id.tv_source);
		TextView tv_status = (TextView) findViewById(R.id.tv_status);
		TextView tv_remark = (TextView) findViewById(R.id.tv_remark);
		
		tv_userName.setText(account.userName);
		tv_nickName.setText(account.nickName);
		tv_createTime.setText(account.createTime);
		if(!account.completeTime.equals("null")){
			tv_completeTime.setText(account.completeTime);
		}
		tv_balance.setText(account.balance);
		tv_money.setText(account.money);
		tv_type.setText(account.type);
		tv_source.setText(account.source);
		tv_status.setText(account.status);
		if(!account.remark.equals("null")){
			tv_remark.setText(account.remark);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.account_particular, menu);
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
