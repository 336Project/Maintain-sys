package com.example.Fmb_applianceRepair_System;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.application.MyApplication;
import com.bean.User;
import com.constant.Constant;
import com.utils.HttpUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author Administrator
 * 提现
 */
public class CashWithdrawalActivity extends Activity implements OnClickListener{

	private User user;
	private EditText et_money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cash_withdrawal);
		getUser(); //获得用户
		initView();
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		TextView tv_balance = (TextView) findViewById(R.id.tv_balance);
		tv_balance.setText(user.balance);
		et_money = (EditText) findViewById(R.id.et_money);
		TextView tv_realgetmoney = (TextView) findViewById(R.id.tv_realgetmoney);
		tv_realgetmoney.setOnClickListener(this); //取钱
	}
	private void getUser() {
		user = ((MyApplication)getApplication()).getUser();
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cash_withdrawal, menu);
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_realgetmoney:
			getMoney();
			break;
		default:
			break;
		}
	}
	//取钱
	private void getMoney() {
		String getMoney = et_money.getText().toString().trim();
		if(getMoney.equals("")){
			Toast.makeText(CashWithdrawalActivity.this, "充值金额不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		new GetMoneyAsyncTask(user.id, getMoney).execute(Constant.PICKUP_URL);
		
	}
	class GetMoneyAsyncTask extends AsyncTask<String, Void, String>{
		private int id;
		private String money;
		public GetMoneyAsyncTask(int id, String money){
			this.id = id;
			this.money = money;
		}
		protected String doInBackground(String... params) {
			Map<String, String> parMap=new HashMap<String, String>();
			String id_String = String.valueOf(id);
			parMap.put("id", id_String);
			parMap.put("money", money);
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if("".equals(result)){
				Toast.makeText(CashWithdrawalActivity.this, "亲，网络连接错误", Toast.LENGTH_SHORT).show();
			}
			else{
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.getBoolean("success");
					String msg = jsonObject.getString("msg");
					Toast.makeText(CashWithdrawalActivity.this, msg, Toast.LENGTH_SHORT).show();
					if(success){
						Intent intent=new Intent();
						intent.setAction("updateaccount");
						sendBroadcast(intent);
						finish();
					}
					else{
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
