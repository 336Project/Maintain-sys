package com.utils;

import com.constant.Constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceUtils {
	
	public static void setUserName(Context context,String username){
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_SET, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit(); 	//��ȡ�༭��
		editor.putString("username",username);
		editor.commit();//�ύ�޸�
	}
	public static String getUserName(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_SET, Context.MODE_PRIVATE);
		String username = sharedPreferences.getString("username","");
		return username;
	}
	public static void setPassWord(Context context,String password){
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_SET, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit(); 	//��ȡ�༭��
		editor.putString("password",password);
		editor.commit();//�ύ�޸�
	}
	public static String getPassWord(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.USER_SET, Context.MODE_PRIVATE);
		String password = sharedPreferences.getString("password","");
		return password;
	}
}
