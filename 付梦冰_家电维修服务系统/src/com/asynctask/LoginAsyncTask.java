package com.asynctask;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.application.MyApplication;
import com.bean.User;
import com.example.Fmb_applianceRepair_System.CustomMainActivity;
import com.example.Fmb_applianceRepair_System.LoginActivity;
import com.example.Fmb_applianceRepair_System.MainActivity;
import com.utils.HttpUtils;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class LoginAsyncTask extends AsyncTask<String, Void, String>{
	public String username;
	public String password;
	private Activity activity;
	public LoginAsyncTask(Activity activity,String username, String password) {
		super();
		this.activity = activity;
		this.username = username;
		this.password = password;
	}
	protected String doInBackground(String... params) {
		Map<String, String> parMap = new HashMap<String, String>();
		parMap.put("username", username);
		parMap.put("password", password);
		String result = HttpUtils.postByApi(params[0],
						parMap);
		return result;
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if ("".equals(result)) {
			Toast.makeText(activity, "网络连接错误",Toast.LENGTH_SHORT).show();
		} else {
			try {
				JSONObject jsonObject = new JSONObject(result);
				boolean success = jsonObject.getBoolean("success");
				if(!success){
					Toast.makeText(activity, "用户名或密码错误",Toast.LENGTH_SHORT).show();
				}
				else{
					JSONObject jsonObject_msg = jsonObject.getJSONObject("msg");
					String realName = jsonObject_msg.getString("realName");
					String userName = jsonObject_msg.getString("userName");
					String roleName = jsonObject_msg.getString("roleName");
					String nickName = jsonObject_msg.getString("nickName");
					String balance = jsonObject_msg.getString("balance");
					String email = jsonObject_msg.getString("email");
					String tel = jsonObject_msg.getString("tel");
					String source = jsonObject_msg.getString("source");
					String status = jsonObject_msg.getString("status");
					String lastLoginTime = jsonObject_msg.getString("lastLoginTime");
					String registerTime = jsonObject_msg.getString("registerTime");
					String introduction = jsonObject_msg.getString("introduction");
					
					int id = jsonObject_msg.getInt("id");
					int roleCode = jsonObject_msg.getInt("roleCode");
					User user = new User(id, roleCode, roleName, userName, realName,nickName,balance,email,tel,source,status,lastLoginTime,registerTime,introduction);
					MyApplication application = (MyApplication) activity.getApplication();
					application.setUser(user);
					if(roleCode==1){
						Intent intent = new Intent(activity,MainActivity.class);
						activity.startActivity(intent);
					}
					else{
						Intent intent = new Intent(activity,CustomMainActivity.class);
						activity.startActivity(intent);
					}
					activity.finish();
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		System.out.println(result);
	}
	
}
