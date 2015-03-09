package com.example.Fmb_applianceRepair_System;

import java.util.HashMap;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

import com.application.MyApplication;
import com.bean.User;
import com.constant.Constant;
import com.utils.HttpUtils;
import com.view.CustomDialog;
import com.view.CustomDialog.Builder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author Administrator
 *	�û���Ϣ����
 */
public class UserDateUpdateActivity extends Activity implements OnClickListener{

	private User user;
	private Builder builder_save;
	private EditText et_email;
	private EditText et_realName;
	private EditText et_tel;
	private EditText et_introduction;
	private CustomDialog customDialog_save;
	private MaterialDialog mMaterialDialog;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_date_update);
		getUser();
		initView();
	}
	private void getUser() {
		user = ((MyApplication)getApplication()).getUser();
	}
	private void initView() {
		et_realName = (EditText) findViewById(R.id.et_realName);
		et_email = (EditText) findViewById(R.id.et_email);
		et_tel = (EditText) findViewById(R.id.et_tel);
		et_introduction = (EditText) findViewById(R.id.et_introduction);
		et_realName.setText(user.realName);
		et_email.setText(user.email);
		et_tel.setText(user.tel);
		et_introduction.setText(user.introduction);
		
		findViewById(R.id.iv_back).setOnClickListener(this);
		findViewById(R.id.tv_save).setOnClickListener(this);
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_date_update, menu);
		return true;
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_save:
			save();
			break;
		}
	}
	//�����û���Ϣ
	private void save() {
		String email=et_email.getText().toString().trim();
		String realName=et_realName.getText().toString().trim();
		String tel=et_tel.getText().toString().trim();
		String introduction=et_introduction.getText().toString().trim();
		if(realName.equals("")){
			Toast.makeText(UserDateUpdateActivity.this, "��ʵ��������Ϊ��", Toast.LENGTH_SHORT).show();
			return;
		}
		if(email.equals("")){
			Toast.makeText(UserDateUpdateActivity.this, "���䲻��Ϊ��", Toast.LENGTH_SHORT).show();
			return;
		}
		if(tel.equals("")){
			Toast.makeText(UserDateUpdateActivity.this, "�ֻ��Ų���Ϊ��", Toast.LENGTH_SHORT).show();
			return;
		}
		if(introduction.equals("")){
			Toast.makeText(UserDateUpdateActivity.this, "���˼�鲻��Ϊ��", Toast.LENGTH_SHORT).show();
			return;
		}
		mMaterialDialog = new MaterialDialog(UserDateUpdateActivity.this);
		mMaterialDialog.setTitle("��ʾ").setMessage("�Ƿ񱣴��û���Ϣ")
				.setPositiveButton("ȷ��", new View.OnClickListener() {
					public void onClick(View v) {
						mMaterialDialog.dismiss();
						new SaveUserDateAsyncTask().execute(Constant.SAVEUSERDATE_URL);
					}
				}).setNegativeButton("ȡ��", new View.OnClickListener() {
					public void onClick(View v) {
						mMaterialDialog.dismiss();
					}
				}).setCanceledOnTouchOutside(false).show();
	}
	class SaveUserDateAsyncTask extends AsyncTask<String, Void, String>{
		protected String doInBackground(String... params) {
			String email=et_email.getText().toString().trim();
			String realName=et_realName.getText().toString().trim();
			String tel=et_tel.getText().toString().trim();
			String introduction=et_introduction.getText().toString().trim();
			String id=String.valueOf(user.id);
			Map<String, String> parMap = new HashMap<String, String>();
			
			parMap.put("id", id);
			parMap.put("email", email);
			parMap.put("realName", realName);
			parMap.put("tel", tel);
			parMap.put("introduction", introduction);
			String result = HttpUtils.postByApi(params[0],parMap);
			return result;
		}
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if("".equals(result)){
				Toast.makeText(UserDateUpdateActivity.this, "�û���Ϣ����ʧ�ܣ�������", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(UserDateUpdateActivity.this, "�û���Ϣ���³ɹ�", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}
}
