package com.example.Fmb_applianceRepair_System;

import java.io.Serializable;

import com.bean.Order;
import com.utils.ToastUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PayActivity extends Activity implements OnClickListener{

	private Order order;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		initIntent();
		initView();
	}

	private void initView() {
		TextView tv_price = (TextView) findViewById(R.id.tv_price);
		tv_price.setText(order.price);
		TextView tv_order = (TextView) findViewById(R.id.tv_order);
		tv_order.setText(System.currentTimeMillis()+"");
		LinearLayout ll_pay1=(LinearLayout) findViewById(R.id.ll_pay1);
		ll_pay1.setOnClickListener(this);
		LinearLayout ll_pay2=(LinearLayout) findViewById(R.id.ll_pay2);
		ll_pay2.setOnClickListener(this);
		LinearLayout ll_pay3=(LinearLayout) findViewById(R.id.ll_pay3);
		ll_pay3.setOnClickListener(this);
		findViewById(R.id.iv_back).setOnClickListener(this);
	}

	private void initIntent() {
		Intent intent = getIntent();
		order = (Order) intent.getSerializableExtra("order");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pay, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_pay1:
			ToastUtils.showMessage(this, "该支付方式暂不支持~");
			break;
		case R.id.ll_pay2:
			ToastUtils.showMessage(this, "该支付方式暂不支持~");
			break;
		case R.id.ll_pay3:
			ToastUtils.showMessage(this, "该支付方式暂不支持~");
			break;
		case R.id.iv_back:
			finish();
			break;
		default:
			break;
		}
	}

}
