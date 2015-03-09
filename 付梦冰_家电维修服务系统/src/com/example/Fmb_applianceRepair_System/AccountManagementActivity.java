package com.example.Fmb_applianceRepair_System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bean.Account;
import com.bean.Order;
import com.bean.User;
import com.constant.Constant;
import com.example.Fmb_applianceRepair_System.OrderManagementActivity.DeleteTradeAsyncTask;
import com.example.Fmb_applianceRepair_System.OrderManagementActivity.GetTradeListAsyncTask;
import com.example.Fmb_applianceRepair_System.RoleManagementActivity.GetRoleListAsyncTask;
import com.example.Fmb_applianceRepair_System.RoleManagementActivity.RoleAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.utils.HttpUtils;
import com.utils.ToastUtils;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AccountManagementActivity extends Activity implements OnClickListener,OnMenuItemClickListener {
	private String currentPage = "1";
	private String keyword = "";
	private PullToRefreshListView pl_userlist;
	private ArrayList<Account> account_List = new ArrayList<Account>();
	private UserAdapter userAdapter;
	private AsyncTask<String, Void, String> userListAsyncTask;
	private EditText et_search;
	private int currentPosition = 0;
	private ListView lv_userlist;
	private UserBroadcastReceiver userBroadcastReceiver;
	private LinearLayout ll_more;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_management);
		initView();
		getUserList();
		userBroadcastReceiver = new UserBroadcastReceiver();
		IntentFilter filter=new IntentFilter("updateaccount");
		registerReceiver(userBroadcastReceiver, filter);
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		ll_more = (LinearLayout) findViewById(R.id.ll_more);
		ll_more.setOnClickListener(this);
		pl_userlist = (PullToRefreshListView) findViewById(R.id.pl_userlist);
		pl_userlist.setOnRefreshListener(new OnRefreshListener<ListView>() {
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
				getUserList();
			}
		});
		pl_userlist.setMode(Mode.PULL_FROM_END);
		lv_userlist = pl_userlist.getRefreshableView();
		userAdapter = new UserAdapter();
		lv_userlist.setAdapter(userAdapter);
		lv_userlist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == 0) {

				} else {
					Account account = account_List.get(position - 1);
					Intent intent = new Intent();
					intent.setClass(AccountManagementActivity.this,
							AccountParticularActivity.class);
					intent.putExtra("account", account);
					startActivity(intent);
				}
			}
		});

		lv_userlist
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						menu.setHeaderTitle("操作");
						menu.add(0, 0, 0, "确定");
						menu.add(0, 1, 0, "删除");
						menu.add(0, 2, 0, "取消");
					}
				});
		lv_userlist.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				currentPosition = position - 1;
				lv_userlist.showContextMenu();
				return true;
			}
		});

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
				if (userListAsyncTask != null) {
					userListAsyncTask.cancel(true);
				}
				userListAsyncTask = new UserListAsyncTask()
						.execute(Constant.GETACCOUNTFROMADMIN_URL);
			}
		});
		findViewById(R.id.tv_cancel).setOnClickListener(this);

	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			new AccountConfirmByIdAsyncTask().execute(Constant.account_ConfirmById);
			break;
		case 1:
			new DeleteAccountAsyncTask().execute(Constant.DELETEUSER_URL);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	@SuppressLint("NewApi")
	private void btn_more() {
		 PopupMenu popupMenu = new PopupMenu(AccountManagementActivity.this, ll_more);  
		 popupMenu.getMenuInflater().inflate(R.menu.menu_accountmanagement, popupMenu.getMenu());  
		 popupMenu.setOnMenuItemClickListener(this);
		 popupMenu.show();
	}
	// 确认充值
	class AccountConfirmByIdAsyncTask extends AsyncTask<String, Void, String> {
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
						.showNetConnectionFail(AccountManagementActivity.this);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.getBoolean("success");
					String msg = jsonObject.getString("msg");
					if (success) { // 成功
						ToastUtils.showMessage(
								AccountManagementActivity.this, msg);
						currentPage = "1";
						account_List.clear();
						if(userAdapter!=null){
							userAdapter.notifyDataSetChanged();
						}
						getUserList();
					} else { // 失败
						ToastUtils.showMessage(
								AccountManagementActivity.this, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// 删除账户交易信息
	class DeleteAccountAsyncTask extends AsyncTask<String, Void, String> {
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
						.showNetConnectionFail(AccountManagementActivity.this);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.getBoolean("success");
					String msg = jsonObject.getString("msg");
					if (success) { // 成功
						ToastUtils.showMessage(
								AccountManagementActivity.this, msg);
						currentPage = "1";
						account_List.clear();
						if(userAdapter!=null){
							userAdapter.notifyDataSetChanged();
						}
						getUserList();
					} else { // 失败
						ToastUtils.showMessage(
								AccountManagementActivity.this, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void getUserList() {
		if (userListAsyncTask != null) {
			userListAsyncTask.cancel(true);
		}
		userListAsyncTask = new UserListAsyncTask()
				.execute(Constant.GETACCOUNTFROMADMIN_URL);
	}

	class UserListAsyncTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			Map<String, String> parMap = new HashMap<String, String>();
			parMap.put("currentPage", currentPage);
			parMap.put("keyword", keyword);
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pl_userlist.onRefreshComplete();
			if ("".equals(result)) {
				ToastUtils.showNetConnectionFail(AccountManagementActivity.this);
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
							
							long id = jsonObject_data.getLong("id");
							long userId = jsonObject_data.getLong("userId");
							String balance = jsonObject_data.getString("balance");
							String completeTime = jsonObject_data.getString("completeTime"); //完成时间
							String createTime = jsonObject_data.getString("createTime"); //创建时间
							String money = jsonObject_data.getString("money"); //简称
							String nickName = jsonObject_data.getString("nickName"); //操作的钱数
							String remark = jsonObject_data.getString("remark"); //备注
							String source = jsonObject_data.getString("source"); //来源
							String status = jsonObject_data.getString("status"); //状态
							String type = jsonObject_data.getString("type"); //用户操作
							String userName = jsonObject_data.getString("userName"); //用户名
							
							Account account = new Account(balance, completeTime, createTime, id, money, nickName, remark, source, status, type, userId, userName);
							account_List.add(account);
						}
						currentPage = String.valueOf((Integer
								.valueOf(currentPage) + 1));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (userAdapter != null) {
					userAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	class UserAdapter extends BaseAdapter {
		public int getCount() {
			return account_List.size();
		}
		public Object getItem(int position) {
			return null;
		}
		public long getItemId(int position) {
			return 0;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			Account account = account_List.get(position);
			
			View inflate = getLayoutInflater().inflate(R.layout.lv_account_admin, null);
			TextView tv_createTime = (TextView) inflate.findViewById(R.id.tv_createTime);
			TextView tv_userName = (TextView) inflate.findViewById(R.id.tv_userName);
			TextView tv_money = (TextView) inflate.findViewById(R.id.tv_money);
			TextView tv_type = (TextView) inflate.findViewById(R.id.tv_type);
			TextView tv_status = (TextView) inflate.findViewById(R.id.tv_status);
			
			tv_createTime.setText(account.createTime);
			tv_userName.setText(account.userName);
			tv_money.setText(account.money);
			tv_type.setText(account.type);
			tv_status.setText(account.status);
			
			return inflate;
		}
	}
	class UserBroadcastReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent) {
			currentPage="1";
			account_List.clear();
			if(userAdapter!=null){
				userAdapter.notifyDataSetChanged();
			}
			getUserList();
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_management, menu);
		return true;
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
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(userBroadcastReceiver!=null){
			unregisterReceiver(userBroadcastReceiver);
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item1:
			item_addAccount();
			break;
		default:
			break;
		}
		return false;
	}
	//新增账户记录
	private void item_addAccount() {
		Intent intent=new Intent(AccountManagementActivity.this, AccountAddActivity.class);
		startActivity(intent);
	}

}
