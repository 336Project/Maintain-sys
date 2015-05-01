package com.example.Fmb_applianceRepair_System;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.application.MyApplication;
import com.bean.Order;
import com.bean.User;
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
import android.widget.TextView;

public class OrderQuoteActivity extends Activity implements OnClickListener {

	private Order order;
	protected String idCode;
	private TextView tv_content;
	private TextView tv_contactTelUser;
	private EditText et_contactTelCompany;
	private EditText et_price;
	private EditText et_quoteContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_quote);
		initIntent();
		initView();
	}
	private void initIntent() {
		Intent intent = getIntent();
		order = (Order) intent.getSerializableExtra("order");
	}

	private void initView() {
		MyApplication application = (MyApplication) getApplication();
		User user = application.getUser();
		findViewById(R.id.iv_back).setOnClickListener(this);
		findViewById(R.id.tv_save).setOnClickListener(this);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_content.setText(order.repairContent);
		tv_contactTelUser = (TextView) findViewById(R.id.tv_contactTelUser);
		tv_contactTelUser.setText(order.contactTelUser);
		et_contactTelCompany = (EditText) findViewById(R.id.et_contactTelCompany);
		et_contactTelCompany.setText(user.tel+"");
		et_price = (EditText) findViewById(R.id.et_price);
		et_quoteContent = (EditText) findViewById(R.id.et_quoteContent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.account_update, menu);
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

	private void save() {
		String contactTelCompany = et_contactTelCompany.getText().toString().trim();
		String price = et_price.getText().toString().trim();
		String quoteContent = et_quoteContent.getText().toString().trim();
		if (contactTelCompany.equals("")) {
			ToastUtils.showMessage(OrderQuoteActivity.this, "联系电话不能为空");
			return;
		}
		if (price.equals("")) {
			ToastUtils.showMessage(OrderQuoteActivity.this, "报修金额不能为空");
			return;
		}
		if (quoteContent.equals("")) {
			ToastUtils.showMessage(OrderQuoteActivity.this, "清单明细不能为空");
			return;
		}
		Map<String, String> parMap=new HashMap<String, String>();
		parMap.put("ids", order.id + "");
		parMap.put("price", price);
		parMap.put("contactTelCompany", contactTelCompany);
		parMap.put("quoteContent", quoteContent);
		new SaveOrderAsyncTask(parMap).execute(Constant.order_Quote);
	}

	class SaveOrderAsyncTask extends AsyncTask<String, Void, String> {
		private Map<String, String> parMap;
		public SaveOrderAsyncTask(Map<String, String> parMap){
			this.parMap = parMap;
		}
		@Override
		protected String doInBackground(String... params) {
			return HttpUtils.postByApi(params[0], parMap);
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if ("".equals(result)) {
				ToastUtils.showNetConnectionFail(OrderQuoteActivity.this);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					String msg = jsonObject.getString("msg");
					boolean success = jsonObject.getBoolean("success");
					if (success) {
						ToastUtils.showMessage(OrderQuoteActivity.this, msg);
						sendBroadcast(new Intent("orderUpdate"));
						finish();
					} else {
						ToastUtils.showMessage(OrderQuoteActivity.this, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
