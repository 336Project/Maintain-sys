package com.example.Fmb_applianceRepair_System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.adapter.AccountAdpater;
import com.application.MyApplication;
import com.bean.Account;
import com.bean.User;
import com.constant.Constant;
import com.example.Fmb_applianceRepair_System.AccountManagementActivity.AccountConfirmByIdAsyncTask;
import com.example.Fmb_applianceRepair_System.AccountManagementActivity.DeleteAccountAsyncTask;
import com.example.Fmb_applianceRepair_System.AccountManagementActivity.UserBroadcastReceiver;
import com.example.Fmb_applianceRepair_System.AccountManagementActivity.UserListAsyncTask;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.utils.HttpUtils;
import com.utils.ToastUtils;

import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

@SuppressLint("NewApi")
public class AccountActivity extends Activity implements OnClickListener,
		OnMenuItemClickListener {

	private ListView lv_accountlist;
	private User user;
	private ArrayList<Account> account_List = new ArrayList<Account>();
	private AccountAdpater accountAdpater;
	private LinearLayout ll_more;
	private PullToRefreshListView pl_accountlist;
	private AsyncTask<String, Void, String> accountAsycTask;
	private String currentPage = "1";
	private String keyword = "";
	private EditText et_search;
	private UserBroadcastReceiver userBroadcastReceiver;
	private int currentPosition;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		getUser();
		initView();
		getAccountInfo();
		userBroadcastReceiver = new UserBroadcastReceiver();
		IntentFilter filter = new IntentFilter("updateaccount");
		registerReceiver(userBroadcastReceiver, filter);
	}

	private void getUser() {
		user = ((MyApplication) getApplication()).getUser();
	}

	// 获得账号信息
	private void getAccountInfo() {
		if (accountAsycTask != null) {
			accountAsycTask.cancel(true);
		}
		accountAsycTask = new AccountAsycTask()
				.execute(Constant.GETACCOUNT_URL);
	}

	private void initView() {

		pl_accountlist = (PullToRefreshListView) findViewById(R.id.pl_accountlist);
		pl_accountlist.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				int flags = DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL;

				String label = DateUtils.formatDateTime(
						getApplicationContext(), System.currentTimeMillis(),
						flags);
				// 更新最后刷新时间
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// 执行加载更多数据任务.
				getAccountInfo();
			}
		});

		pl_accountlist.setMode(Mode.PULL_FROM_END);
		lv_accountlist = pl_accountlist.getRefreshableView();
		accountAdpater = new AccountAdpater(account_List, this);
		lv_accountlist.setAdapter(accountAdpater);
		lv_accountlist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == 0) {
				} else {
					Account account = account_List.get(position - 1);
					Intent intent = new Intent();
					intent.setClass(AccountActivity.this,
							AccountParticularActivity.class);
					intent.putExtra("account", account);
					startActivity(intent);
				}
			}
		});

		lv_accountlist
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						menu.setHeaderTitle("操作");
						menu.add(0, 0, 0, "取消充值");
						menu.add(0, 1, 0, "返回");
					}
				});
		lv_accountlist.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				currentPosition = position - 1;
				lv_accountlist.showContextMenu();
				return true;
			}
		});

		ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		ll_more = (LinearLayout) findViewById(R.id.ll_more);
		ll_more.setOnClickListener(this);

		et_search = (EditText) findViewById(R.id.et_search);
		et_search.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				keyword = s.toString();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				currentPage = "1";
				account_List.clear();
				getAccountInfo();
			}
		});
		findViewById(R.id.tv_cancel).setOnClickListener(this);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			new CancelAccountAsyncTask().execute(Constant.account_Cancel);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	class CancelAccountAsyncTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			long id = account_List.get(currentPosition).id;
			Map<String, String> parMap = new HashMap<String, String>();
			parMap.put("ids", id + "");
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if ("".equals(result)) {
				ToastUtils
						.showNetConnectionFail(AccountActivity.this);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.getBoolean("success");
					String msg = jsonObject.getString("msg");
					if (success) { // 成功
						ToastUtils.showMessage(
								AccountActivity.this, msg);
						currentPage = "1";
						account_List.clear();
						if(accountAdpater!=null){
							accountAdpater.notifyDataSetChanged();
						}
						getAccountInfo();
					} else { // 失败
						ToastUtils.showMessage(
								AccountActivity.this, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.account, menu);
		return true;
	}

	class AccountAsycTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			Map<String, String> parMap = new HashMap<String, String>();
			if (user != null) {
				parMap.put("ids", user.id + "");
			}
			parMap.put("currentPage", currentPage);
			parMap.put("keyword", keyword);
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pl_accountlist.onRefreshComplete();
			if ("".equals(result)) {
				ToastUtils.showNetConnectionFail(AccountActivity.this);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONObject jsonObject_msg = jsonObject.getJSONObject("msg");
					int recordsTotal = jsonObject_msg.getInt("recordsTotal");
					if (account_List.size() >= recordsTotal) {

					} else {
						JSONArray jsonArray_data = jsonObject_msg
								.getJSONArray("data");
						for (int i = 0; i < jsonArray_data.length(); i++) {
							JSONObject jsonObject_data = jsonArray_data
									.getJSONObject(i);
							String balance = jsonObject_data
									.getString("balance");
							String completeTime = jsonObject_data
									.getString("completeTime");
							String createTime = jsonObject_data
									.getString("createTime");
							long id = jsonObject_data.getLong("id");
							long userId = jsonObject_data.getLong("userId");
							String money = jsonObject_data.getString("money");
							String nickName = jsonObject_data
									.getString("nickName");
							String remark = jsonObject_data.getString("remark");
							String source = jsonObject_data.getString("source");
							String status = jsonObject_data.getString("status");
							String type = jsonObject_data.getString("type");
							String userName = jsonObject_data
									.getString("userName");
							Account account = new Account(balance,
									completeTime, createTime, id, money,
									nickName, remark, source, status, type,
									userId, userName);
							account_List.add(account);
						}
						currentPage = String.valueOf((Integer
								.valueOf(currentPage) + 1));
					}
					if (accountAdpater != null) {
						accountAdpater.notifyDataSetChanged();
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
		case R.id.tv_cancel:
			keyword = "";
			currentPage = "1";
			account_List.clear();
			et_search.setText("");
			break;
		case R.id.ll_more:
			btn_more();
			break;
		}
	}

	// 显示更多操作
	@SuppressLint("NewApi")
	private void btn_more() {
		PopupMenu popupMenu = new PopupMenu(AccountActivity.this, ll_more);
		popupMenu.getMenuInflater().inflate(R.menu.menu_account,
				popupMenu.getMenu());
		popupMenu.setOnMenuItemClickListener(this);
		popupMenu.show();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item1:
			item_topUp();
			break;
		case R.id.item2:
			item_cashwithdrawal();
			break;
		default:
			break;
		}
		return false;
	}

	// 充值
	private void item_topUp() {
		startActivity(new Intent(AccountActivity.this, TopUpActivity.class));
	}

	// 充值
	private void item_cashwithdrawal() {
		startActivity(new Intent(AccountActivity.this,
				CashWithdrawalActivity.class));
	}

	class UserBroadcastReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			currentPage = "1";
			account_List.clear();
			getAccountInfo();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (userBroadcastReceiver != null) {
			unregisterReceiver(userBroadcastReceiver);
		}
	}
}
