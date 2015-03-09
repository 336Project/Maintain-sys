package com.utils;

import com.example.Fmb_applianceRepair_System.LoginActivity;

import me.drakeet.materialdialog.MaterialDialog;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MateriaDialogUtils {
	private static MaterialDialog mMaterialDialog;

	public static void showBackUser(final Activity activity) {
		mMaterialDialog = new MaterialDialog(activity);
		mMaterialDialog.setTitle("��ʾ").setMessage("�Ƿ��˳���ǰ�û�")
				.setPositiveButton("ȷ��", new View.OnClickListener() {
					public void onClick(View v) {
						mMaterialDialog.dismiss();
						Intent intent = new Intent(activity,
								LoginActivity.class);
						activity.startActivity(intent);
						activity.finish();
					}
				}).setNegativeButton("ȡ��", new View.OnClickListener() {
					public void onClick(View v) {
						mMaterialDialog.dismiss();
					}
				}).setCanceledOnTouchOutside(false).show();
	}
}
