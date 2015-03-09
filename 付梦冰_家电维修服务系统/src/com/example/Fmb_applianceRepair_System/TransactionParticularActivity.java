package com.example.Fmb_applianceRepair_System;

import java.io.Serializable;

import com.bean.Trade;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TransactionParticularActivity extends Activity implements OnClickListener{

	private TextView tv_fromUserName;
	private TextView tv_fromUserNickName;
	private TextView tv_toUserName;
	private TextView tv_toUserNickName;
	private TextView tv_money;
	private TextView tv_time;
	private TextView tv_status;
	private Trade trade;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction_particular);
		initIntent();
		initView();
	}

	private void initIntent() {
		Intent intent = getIntent();
		trade = (Trade) intent.getSerializableExtra("trade");
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		tv_fromUserName = (TextView) findViewById(R.id.tv_fromUserName);
		tv_fromUserNickName = (TextView) findViewById(R.id.tv_fromUserNickName);
		tv_toUserName = (TextView) findViewById(R.id.tv_toUserName);
		tv_toUserNickName = (TextView) findViewById(R.id.tv_toUserNickName);
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_status = (TextView) findViewById(R.id.tv_status);
		tv_fromUserName.setText(trade.getFromUserName());
		tv_fromUserNickName.setText(trade.getFromUserNickName());
		tv_toUserName.setText(trade.getToUserName());
		tv_toUserNickName.setText(trade.getToUserNickName());
		tv_money.setText(trade.getMoney());
		tv_time.setText(trade.getTime());
		tv_status.setText(trade.getStatus());
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.transaction_particular, menu);
		return true;
	}
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
