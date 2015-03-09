package com.example.Fmb_applianceRepair_System;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import me.drakeet.materialdialog.MaterialDialog;

import com.bean.Order;
import com.bean.User;
import com.constant.Constant;
import com.example.Fmb_applianceRepair_System.UserDateUpdateActivity.SaveUserDateAsyncTask;
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
import android.widget.Toast;

public class UserUpdateActivity extends Activity implements OnClickListener{

	private User user;
	private EditText et_nickName;
	private EditText et_realName;
	private EditText et_email;
	private EditText et_tel;
	private MaterialDialog mMaterialDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_update);
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
		tv_userName.setText(user.userName);
		TextView tv_roleName = (TextView) findViewById(R.id.tv_roleName);
		tv_roleName.setText(user.roleName);
		et_nickName = (EditText) findViewById(R.id.et_nickName);
		et_nickName.setText(user.nickName);
		et_realName = (EditText) findViewById(R.id.et_realName);
		et_realName.setText(user.realName);
		et_email = (EditText) findViewById(R.id.et_email);
		et_email.setText(user.email);
		et_tel = (EditText) findViewById(R.id.et_tel);
		et_tel.setText(user.tel);
		findViewById(R.id.tv_save).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_update, menu);
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
		
		String nickName=et_nickName.getText().toString().trim();
		String realName=et_realName.getText().toString().trim();
		String tel=et_tel.getText().toString().trim();
		String email=et_email.getText().toString().trim();
		if(nickName.equals("")){
			Toast.makeText(UserUpdateActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(realName.equals("")){
			Toast.makeText(UserUpdateActivity.this, "真实姓名不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if(tel.equals("")){
			Toast.makeText(UserUpdateActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if(email.equals("")){
			Toast.makeText(UserUpdateActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		mMaterialDialog = new MaterialDialog(UserUpdateActivity.this);
		mMaterialDialog.setTitle("提示").setMessage("是否保存用户信息")
				.setPositiveButton("确定", new View.OnClickListener() {
					public void onClick(View v) {
						mMaterialDialog.dismiss();
						new SaveUserDateAsyncTask().execute(Constant.SAVEUSERDATEFROMADMINISTRATOR_URL);
					}
				}).setNegativeButton("取消", new View.OnClickListener() {
					public void onClick(View v) {
						mMaterialDialog.dismiss();
					}
				}).setCanceledOnTouchOutside(false).show();
	}
	class SaveUserDateAsyncTask extends AsyncTask<String, Void, String>{
		protected String doInBackground(String... params) {
			
			String nickName=et_nickName.getText().toString().trim();
			String realName=et_realName.getText().toString().trim();
			String tel=et_tel.getText().toString().trim();
			String email=et_email.getText().toString().trim();
			String id=String.valueOf(user.id);
			Map<String, String> parMap = new HashMap<String, String>();
			
			parMap.put("id", id);
			parMap.put("email", email);
			parMap.put("realName", realName);
			parMap.put("tel", tel);
			parMap.put("nickName", nickName);
			String result = HttpUtils.postByApi(params[0],parMap);
			return result;
		}
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if("".equals(result)){
				ToastUtils.showMessage(UserUpdateActivity.this, "用户信息更新失败，请重试");
			}
			else{
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.getBoolean("success");
					String msg = jsonObject.getString("msg");
					if(success){
						ToastUtils.showMessage(UserUpdateActivity.this, msg);
						Intent intent=new Intent();
						intent.setAction("updateuser");
						sendBroadcast(intent);
						finish();
					}
					else{
						ToastUtils.showMessage(UserUpdateActivity.this, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
