package com.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	public static void showNetConnectionFail(Context context){
		Toast.makeText(context, "��,��������ʧ��,������", Toast.LENGTH_SHORT).show();
	}
	public static void showMessage(Context context,String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
