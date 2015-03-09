package com.example.Fmb_applianceRepair_System;

import com.utils.MateriaDialogUtils;

import me.drakeet.materialdialog.MaterialDialog;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private MaterialDialog mMaterialDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		findViewById(R.id.ll_more).setOnClickListener(this);
		findViewById(R.id.ll_back).setOnClickListener(this);
		findViewById(R.id.ll_userdate).setOnClickListener(this);
		findViewById(R.id.ll_rolelist).setOnClickListener(this);
		findViewById(R.id.ll_usermanagement).setOnClickListener(this);
		findViewById(R.id.ll_trade).setOnClickListener(this);
		findViewById(R.id.ll_ordermanagement).setOnClickListener(this);
		findViewById(R.id.ll_account).setOnClickListener(this);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_userdate:
			btn_userdate();
			break;
		case R.id.ll_more:
			Toast.makeText(this, "更多精彩内容，敬请期待", Toast.LENGTH_SHORT).show();
			break;
		case R.id.ll_back:
			back();
			break;
		case R.id.ll_rolelist:
			btn_rolelist();
			break;
		case R.id.ll_usermanagement:
			btn_userlist();
			break;
		case R.id.ll_trade:
			btn_tradelist();
			break;
		case R.id.ll_ordermanagement:
			btn_orderlist();
			break;
		case R.id.ll_account:
			btn_accountlist();
			break;
		default:
			break;
		}
	}
	private void btn_accountlist() {
		Intent intent = new Intent(this, AccountManagementActivity.class);
		startActivity(intent);
	}

	private void btn_orderlist() {
		Intent intent = new Intent(this, OrderManagementActivity.class);
		startActivity(intent);
	}

	private void btn_tradelist() {
		Intent intent = new Intent(this, TransactionManagementActivity.class);
		startActivity(intent);
	}

	private void btn_userlist() {
		Intent intent = new Intent(this, UserManagementActivity.class);
		startActivity(intent);
	}

	private void btn_rolelist() {
		Intent intent = new Intent(this, RoleManagementActivity.class);
		startActivity(intent);
	}

	// 查看用户个人信息
	private void btn_userdate() {
		Intent intent = new Intent(this, UserDateActivity.class);
		startActivity(intent);
	}

	private void back() {
		MateriaDialogUtils.showBackUser(this);
	}

}
