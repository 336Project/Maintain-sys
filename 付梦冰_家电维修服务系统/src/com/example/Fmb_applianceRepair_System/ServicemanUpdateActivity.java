package com.example.Fmb_applianceRepair_System;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.application.MyApplication;
import com.bean.User;
import com.constant.Constant;
import com.example.Fmb_applianceRepair_System.ServicemanAddActivity.AddUserAsyncTask;
import com.utils.HttpUtils;
import com.utils.ToastUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class ServicemanUpdateActivity extends Activity implements OnClickListener{

	private User user;
	private EditText et_realName;
	private EditText et_email;
	private EditText et_tel;
	private EditText et_introduction;
	private AsyncTask<String, Void, String> saveUserDateAsyncTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_serviceman_update);
		initIntent();
		initView();
	}

	private void initIntent() {
		Intent intent = getIntent();
		user = (User) intent.getSerializableExtra("user");
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		findViewById(R.id.tv_save).setOnClickListener(this);
		TextView tv_userName = (TextView) findViewById(R.id.tv_userName);
		tv_userName.setText(user.userName);
		et_realName = (EditText) findViewById(R.id.et_realName);
		et_realName.setText(user.realName);
		et_email = (EditText) findViewById(R.id.et_email);
		et_email.setText(user.email);
		et_tel = (EditText) findViewById(R.id.et_tel);
		et_tel.setText(user.tel);
		et_introduction = (EditText) findViewById(R.id.et_introduction);
		et_introduction.setText(user.introduction);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.serviceman_update, menu);
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
		String realName = et_realName.getText().toString().trim();
		String nickName = realName;
		String email = et_email.getText().toString().trim();
		String tel = et_tel.getText().toString().trim();
		String introduction = et_introduction.getText().toString().trim();
		String roleCode ="4";
		if(email.equals("")){
			ToastUtils.showMessage(this, "邮箱不能为空~");
			return;
		}
		if(tel.equals("")){
			ToastUtils.showMessage(this, "手机不能为空~");
			return;
		}
		Map<String, String> parMap=new HashMap<String, String>();
		parMap.put("id", user.id+"");
		parMap.put("email", email);
		parMap.put("tel", tel);
		parMap.put("realName", realName);
		parMap.put("introduction", introduction);
		if(saveUserDateAsyncTask!=null)
			saveUserDateAsyncTask.cancel(true);
		saveUserDateAsyncTask = new SaveUserDateAsyncTask(parMap).execute(Constant.SAVEUSERDATE_URL);
	}
	class SaveUserDateAsyncTask extends AsyncTask<String, Void, String>{
		private Map<String, String> parMap;
		public SaveUserDateAsyncTask(Map<String, String> parMap){
			this.parMap = parMap;
		}
		protected String doInBackground(String... params) {
	
			String result = HttpUtils.postByApi(params[0],parMap);
			return result;
		}
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if("".equals(result)){
				ToastUtils.showMessage(ServicemanUpdateActivity.this, "用户信息更新失败，请重试");
			}
			else{
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.getBoolean("success");
					String msg = jsonObject.getString("msg");
					if(success){
						ToastUtils.showMessage(ServicemanUpdateActivity.this, msg);
						Intent intent=new Intent();
						intent.setAction("updateServicemanList");
						sendBroadcast(intent);
						finish();
					}
					else{
						ToastUtils.showMessage(ServicemanUpdateActivity.this, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
