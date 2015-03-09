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
import android.widget.ImageView;
import android.widget.Toast;

public class TopUpActivity extends Activity implements OnClickListener{

	private EditText et_money;
	private User user;
	private EditText et_remark;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top_up);
		initView();
		getUser();
	}
	private void getUser() {
		user = ((MyApplication)getApplication()).getUser();
	}
	private void initView() {
		ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		et_money = (EditText) findViewById(R.id.et_money);
		et_remark = (EditText) findViewById(R.id.et_remark);
		findViewById(R.id.tv_realtopup).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.top_up, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_realtopup:
			realTopUp();
			break;
		default:
			break;
		}
	}
	//确认充值
	private void realTopUp() {
		String money = et_money.getText().toString().trim();
		if(money.equals("")){
			Toast.makeText(TopUpActivity.this, "请输入充值金额", Toast.LENGTH_SHORT).show();
			return;
		}
		new RealTopUpAsyncTask().execute(Constant.REALTOPUP_URL);
		
	}
	class RealTopUpAsyncTask extends AsyncTask<String, Void, String>{
		protected String doInBackground(String... params) {
			String id=String.valueOf(user.id);
			String money=et_money.getText().toString().trim();
			String source="用户操作";
			String remark=et_remark.getText().toString().trim();
			Map<String, String> parMap = new HashMap<String, String>();
			parMap.put("id", id);
			parMap.put("money", money);
			parMap.put("source", source);
			parMap.put("remark", remark);
			String result = HttpUtils.postByApi(params[0],
							parMap);
			return result;
		}
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if("".equals(result)){
				Toast.makeText(TopUpActivity.this, "亲，网络连接错误", Toast.LENGTH_SHORT).show();
			}
			else{
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.getBoolean("success");
					String msg = jsonObject.getString("msg");
					Toast.makeText(TopUpActivity.this, msg, Toast.LENGTH_SHORT).show();
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
