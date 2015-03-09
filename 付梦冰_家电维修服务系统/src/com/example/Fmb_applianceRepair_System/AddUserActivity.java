package com.example.Fmb_applianceRepair_System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bean.Role;
import com.constant.Constant;
import com.utils.HttpUtils;
import com.utils.ToastUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class AddUserActivity extends Activity implements OnClickListener{
	private ArrayList<Role> role_List=new ArrayList<Role>();
	private EditText et_roleName;
	private String[] nameStrings;
	private String[] codeStrings;
	private EditText et_userName;
	private EditText et_password;
	private EditText et_passwordAgain;
	private EditText et_nickName;
	private EditText et_realName;
	private EditText et_email;
	private EditText et_tel;
	private String roleCode="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_user);
		initView();
		getlistRole(); //获取角色下拉列表数据
	}
	private void getlistRole() {
		new GetListRoleAsyncTask().execute(Constant.GETLISTROLE_URL);
	}
	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		findViewById(R.id.tv_save).setOnClickListener(this);
		et_userName = (EditText) findViewById(R.id.et_userName);
		et_roleName = (EditText) findViewById(R.id.et_roleName);
		et_roleName.setOnClickListener(this);
		et_password = (EditText) findViewById(R.id.et_password);
		et_passwordAgain = (EditText) findViewById(R.id.et_passwordAgain);
		et_nickName = (EditText) findViewById(R.id.et_nickName);
		et_realName = (EditText) findViewById(R.id.et_realName);
		et_email = (EditText) findViewById(R.id.et_email);
		et_tel = (EditText) findViewById(R.id.et_tel);
		
	}
	class GetListRoleAsyncTask extends AsyncTask<String, Void, String>{
		protected String doInBackground(String... params) {
			Map<String, String> parMap=new HashMap<String, String>();
			parMap.put("type", "1");
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if("".equals(result)){
				ToastUtils.showNetConnectionFail(AddUserActivity.this);
			}
			else{
				role_List.clear();
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONArray jsonArray_msg = jsonObject.getJSONArray("msg");
					for (int i = 0; i < jsonArray_msg.length(); i++) {
						JSONObject jsonObject_msg = jsonArray_msg.getJSONObject(i);
						String code = jsonObject_msg.getString("code");
						String name = jsonObject_msg.getString("name");
						Role role = new Role(code, name);
						role_List.add(role);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.et_roleName:
			showRoleList();
			break;
		case R.id.tv_save:
			save();
			break;
		default:
			break;
		}
	}
	private void save() {
		String userName = et_userName.getText().toString().trim();
		String roleName = et_roleName.getText().toString().trim();
		String password = et_password.getText().toString().trim();
		String passwordAgain = et_passwordAgain.getText().toString().trim();
		String nickName = et_nickName.getText().toString().trim();
		String realName = et_realName.getText().toString().trim();
		String email = et_email.getText().toString().trim();
		String tel = et_tel.getText().toString().trim();
		
		if(userName.equals("")){
			ToastUtils.showMessage(AddUserActivity.this, "用户名不能为空");
			return;
		}
		if(roleName.equals("")){
			ToastUtils.showMessage(AddUserActivity.this, "用户类型不能为空");
			return;
		}
		if(password.equals("")){
			ToastUtils.showMessage(AddUserActivity.this, "密码不能为空");
			return;
		}
		if(passwordAgain.equals("")){
			ToastUtils.showMessage(AddUserActivity.this, "再次输入的密码不能为空");
			return;
		}
		if(nickName.equals("")){
			ToastUtils.showMessage(AddUserActivity.this, "昵称不能为空");
			return;
		}
		if(realName.equals("")){
			ToastUtils.showMessage(AddUserActivity.this, "姓名不能为空");
			return;
		}
		if(email.equals("")){
			ToastUtils.showMessage(AddUserActivity.this, "邮箱不能为空");
			return;
		}
		if(tel.equals("")){
			ToastUtils.showMessage(AddUserActivity.this, "电话不能为空");
			return;
		}
		if(!password.equals(passwordAgain)){
			ToastUtils.showMessage(AddUserActivity.this, "两次输入的密码不一致");
			return;
		}
		new AddUserAsyncTask().execute(Constant.ADDUSER_URL);
		
	}
	private void showRoleList() {
		nameStrings = new String[role_List.size()];
		codeStrings = new String[role_List.size()];
		for (int i = 0; i < role_List.size(); i++) {
			Role role = role_List.get(i);
			nameStrings[i]=role.name;
			codeStrings[i]=role.code;
		}
		new AlertDialog.Builder(AddUserActivity.this)
		.setTitle("用户类型")
		.setItems(nameStrings, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String codeName = nameStrings[which];
				roleCode=codeStrings[which];
				et_roleName.setText(codeName);
			}
		}).create().show();
	}
	class AddUserAsyncTask extends AsyncTask<String, Void, String>{
		protected String doInBackground(String... params) {
			Map<String, String> parMap=new HashMap<String, String>();
			String userName = et_userName.getText().toString().trim();
			String password = et_password.getText().toString().trim();
			String nickName = et_nickName.getText().toString().trim();
			String realName = et_realName.getText().toString().trim();
			String email = et_email.getText().toString().trim();
			String tel = et_tel.getText().toString().trim();
			parMap.put("userName", userName);
			parMap.put("email", email);
			parMap.put("tel", tel);
			parMap.put("realName", realName);
			parMap.put("nickName", nickName);
			parMap.put("password", password);
			parMap.put("roleCode", roleCode);
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				boolean success = jsonObject.getBoolean("success");
				String msg = jsonObject.getString("msg");
				if(success){
					ToastUtils.showMessage(AddUserActivity.this, msg);
					Intent intent=new Intent();
					intent.setAction("updateuser");
					sendBroadcast(intent);
				}
				else{
					ToastUtils.showMessage(AddUserActivity.this, msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_user, menu);
		return true;
	}
}
