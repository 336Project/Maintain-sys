package com.example.Fmb_applianceRepair_System;

import com.bean.Order;
import com.bean.Trade;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class OrderParticularActivity extends Activity implements
		OnClickListener {

	private Order order;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_particular);
		initIntent();
		initView();
	}

	private void initIntent() {
		Intent intent = getIntent();
		order = (Order) intent.getSerializableExtra("order");
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		TextView tv_customerUser = (TextView) findViewById(R.id.tv_customerUser);
		tv_customerUser.setText(order.customerUser);
		TextView tv_contactTelUser = (TextView) findViewById(R.id.tv_contactTelUser);
		tv_contactTelUser.setText(order.contactTelUser);
		TextView tv_customerCompany = (TextView) findViewById(R.id.tv_customerCompany);
		tv_customerCompany.setText(order.customerCompany);
		TextView tv_contactTelCompany = (TextView) findViewById(R.id.tv_contactTelCompany);
		tv_contactTelCompany.setText(order.contactTelCompany);
		TextView tv_createTime = (TextView) findViewById(R.id.tv_createTime);
		tv_createTime.setText(order.createTime);
		TextView tv_completeTime = (TextView) findViewById(R.id.tv_completeTime);
		if (order.completeTime.equals("null")) {

		} else {
			tv_completeTime.setText(order.completeTime);
		}
		TextView tv_price = (TextView) findViewById(R.id.tv_price);
		if (order.price.equals("null")) {

		} else {
			tv_price.setText(order.price);
		}
		TextView tv_repairContent = (TextView) findViewById(R.id.tv_repairContent);
		tv_repairContent.setText(order.repairContent);
		TextView tv_status = (TextView) findViewById(R.id.tv_status);
		tv_status.setText(order.status);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.order_particular, menu);
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
