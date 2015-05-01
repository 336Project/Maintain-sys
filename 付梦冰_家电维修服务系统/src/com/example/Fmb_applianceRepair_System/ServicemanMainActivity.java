package com.example.Fmb_applianceRepair_System;

import com.application.MyApplication;
import com.bean.User;
import com.utils.MateriaDialogUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
/**
 * 维修人员主界面
 * @author Administrator
 *
 */
public class ServicemanMainActivity extends Activity implements OnClickListener{

	private LinearLayout ll_quit;
	private LinearLayout ll_userdate;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_serviceman_main);
		getUser();
		initView();//查找控件
	}

	private void getUser() {
		user = ((MyApplication)getApplication()).getUser();
	}

	private void initView() {
		ll_userdate = (LinearLayout) findViewById(R.id.ll_userdate);
		ll_userdate.setOnClickListener(this);
		ll_quit = (LinearLayout) findViewById(R.id.ll_quit);
		ll_quit.setOnClickListener(this);
		LinearLayout ll_account = (LinearLayout) findViewById(R.id.ll_account);
		ll_account.setOnClickListener(this);
		findViewById(R.id.ll_order).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.custom_main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_quit:
			btn_quit();
			break;
		case R.id.ll_userdate:
			btn_userdate();
			break;
		case R.id.ll_account:
			btn_account();
			break;
		case R.id.ll_order:
			btn_order();
			break;
		default:
			break;
		}
	}

	//订单信息
	private void btn_order() {
		if(user.roleCode==2){
			startActivity(new Intent(ServicemanMainActivity.this, OrderCompanyActivity.class) );
		}
		else if(user.roleCode==3){
			startActivity(new Intent(ServicemanMainActivity.this, OrderActivity.class) );
		}
	}

	private void btn_account() {
		startActivity(new Intent(ServicemanMainActivity.this, ServicemanActivity.class) );
	}

	//查看用户个人信息
	private void btn_userdate() {
		Intent intent = new Intent(ServicemanMainActivity.this,UserDateActivity.class);
		startActivity(intent);
	}

	//退出
	private void btn_quit() {
		MateriaDialogUtils.showBackUser(this);
	}

}
