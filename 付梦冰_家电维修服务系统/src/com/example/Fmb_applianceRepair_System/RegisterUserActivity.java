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
import android.widget.TextView;

public class RegisterUserActivity extends Activity implements OnClickListener{
	private ArrayList<Role> role_List=new ArrayList<Role>();
	private TextView tv_roleName;
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
	private EditText et_introduction;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user);
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
		tv_roleName = (TextView) findViewById(R.id.tv_roleName);
		tv_roleName.setOnClickListener(this);
		et_password = (EditText) findViewById(R.id.et_password);
		et_passwordAgain = (EditText) findViewById(R.id.et_passwordAgain);
		et_nickName = (EditText) findViewById(R.id.et_nickName);
		et_realName = (EditText) findViewById(R.id.et_realName);
		et_email = (EditText) findViewById(R.id.et_email);
		et_tel = (EditText) findViewById(R.id.et_tel);
		et_introduction = (EditText) findViewById(R.id.et_introduction);
		
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
				ToastUtils.showNetConnectionFail(RegisterUserActivity.this);
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
		case R.id.tv_roleName:
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
		String roleName = tv_roleName.getText().toString().trim();
		String password = et_password.getText().toString().trim();
		String passwordAgain = et_passwordAgain.getText().toString().trim();
		String nickName = et_nickName.getText().toString().trim();
		String realName = et_realName.getText().toString().trim();
		String email = et_email.getText().toString().trim();
		String tel = et_tel.getText().toString().trim();
		String introduction = et_introduction.getText().toString().trim();
		
		if(userName.equals("")){
			ToastUtils.showMessage(RegisterUserActivity.this, "用户名不能为空");
			return;
		}
		if(roleName.equals("")){
			ToastUtils.showMessage(RegisterUserActivity.this, "用户类型不能为空");
			return;
		}
		if(password.equals("")){
			ToastUtils.showMessage(RegisterUserActivity.this, "密码不能为空");
			return;
		}
		if(passwordAgain.equals("")){
			ToastUtils.showMessage(RegisterUserActivity.this, "再次输入的密码不能为空");
			return;
		}
		if(nickName.equals("")){
			ToastUtils.showMessage(RegisterUserActivity.this, "昵称不能为空");
			return;
		}
		if(realName.equals("")){
			ToastUtils.showMessage(RegisterUserActivity.this, "姓名不能为空");
			return;
		}
		if(email.equals("")){
			ToastUtils.showMessage(RegisterUserActivity.this, "邮箱不能为空");
			return;
		}
		if(tel.equals("")){
			ToastUtils.showMessage(RegisterUserActivity.this, "电话不能为空");
			return;
		}
		if(introduction.equals("")){
			ToastUtils.showMessage(RegisterUserActivity.this, "简介不能为空");
			return;
		}
		if(!password.equals(passwordAgain)){
			ToastUtils.showMessage(RegisterUserActivity.this, "两次输入的密码不一致");
			return;
		}
		new AddUserAsyncTask().execute(Constant.user_RegisterUser);
		
	}
	private void showRoleList() {
		nameStrings = new String[2];
		codeStrings = new String[2];
		nameStrings[0]="维修公司";
		nameStrings[1]="普通用户";
		codeStrings[0]="2";
		codeStrings[1]="3";
		new AlertDialog.Builder(RegisterUserActivity.this)
		.setTitle("用户类型")
		.setItems(nameStrings, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String codeName = nameStrings[which];
				roleCode=codeStrings[which];
				tv_roleName.setText(codeName);
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
			String introduction = et_introduction.getText().toString().trim();
			parMap.put("userName", userName);
			parMap.put("email", email);
			parMap.put("tel", tel);
			parMap.put("realName", realName);
			parMap.put("nickName", nickName);
			parMap.put("password", password);
			parMap.put("roleCode", roleCode);
			parMap.put("introduction", introduction);
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
					ToastUtils.showMessage(RegisterUserActivity.this, msg);
				}
				else{
					ToastUtils.showMessage(RegisterUserActivity.this, msg);
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
