package com.example.Fmb_applianceRepair_System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.application.MyApplication;
import com.bean.User;
import com.constant.Constant;
import com.example.Fmb_applianceRepair_System.UserManagementActivity.DeleteUserAsyncTask;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
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
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.PopupMenu.OnMenuItemClickListener;

@SuppressLint("NewApi")
public class ServicemanActivity extends Activity implements OnClickListener,
		OnMenuItemClickListener {

	private ListView lv_accountlist;
	private User user;
	private LinearLayout ll_more;
	private PullToRefreshListView pl_accountlist;
	private String currentPage = "1";
	private String keyword = "";
	private EditText et_search;
	private UserBroadcastReceiver userBroadcastReceiver;
	private int currentPosition;
	private AsyncTask<String, Void, String> servicemanListAsycTask;
	private ArrayList<User> user_List = new ArrayList<User>();
	private UserAdapter userAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_serviceman);
		getUser();
		initView();
		getServicemanListInfo();
		userBroadcastReceiver = new UserBroadcastReceiver();
		IntentFilter filter = new IntentFilter("updateServicemanList");
		registerReceiver(userBroadcastReceiver, filter);
	}
	private void getUser() {
		user = ((MyApplication) getApplication()).getUser();
	}
	// 获得账号信息
	private void getServicemanListInfo() {
		if (servicemanListAsycTask != null) {
			servicemanListAsycTask.cancel(true);
		}
		servicemanListAsycTask = new ServicemanListAsycTask()
				.execute(Constant.getServicemanList_URL);
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
				getServicemanListInfo();
			}
		});

		pl_accountlist.setMode(Mode.PULL_FROM_END);
		lv_accountlist = pl_accountlist.getRefreshableView();
		userAdapter = new UserAdapter();
		lv_accountlist.setAdapter(userAdapter);
		lv_accountlist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == 0) {
				} else {
					User user = user_List.get(position - 1);
					Intent intent = new Intent();
					intent.setClass(ServicemanActivity.this,
							ServicemanParticularActivity.class);
					intent.putExtra("user", user);
					startActivity(intent);
				}
			}
		});

		lv_accountlist.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						menu.setHeaderTitle("操作");
						menu.add(0, 0, 0, "修改");
						menu.add(0, 1, 0, "删除");
						menu.add(0, 2, 0, "取消");
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
				user_List.clear();
				getServicemanListInfo();
			}
		});
		findViewById(R.id.tv_cancel).setOnClickListener(this);
	}
	class UserAdapter extends BaseAdapter {
		public int getCount() {
			return user_List.size();
		}
		public Object getItem(int position) {
			return null;
		}
		public long getItemId(int position) {
			return 0;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			User user = user_List.get(position);
			View inflate = getLayoutInflater().inflate(R.layout.lv_serviceman, null);
			TextView tv_userName = (TextView) inflate
					.findViewById(R.id.tv_userName);
			TextView tv_realName = (TextView) inflate
					.findViewById(R.id.tv_realName);
			TextView tv_tel = (TextView) inflate
					.findViewById(R.id.tv_tel);
			tv_userName.setText(user.userName);
			tv_realName.setText(user.realName);
			tv_tel.setText(user.tel);
			return inflate;
		}
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			User user = user_List.get(currentPosition);
			Intent intent = new Intent();
			intent.setClass(ServicemanActivity.this,
					ServicemanUpdateActivity.class);
			intent.putExtra("user", user);
			startActivity(intent);
			break;
		case 1:
			new DeleteUserAsyncTask().execute(Constant.DELETEUSER_URL);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	// 删除用户
		class DeleteUserAsyncTask extends AsyncTask<String, Void, String> {
			protected String doInBackground(String... params) {
				long id = user_List.get(currentPosition).id;
				Map<String, String> parMap = new HashMap<String, String>();
				parMap.put("ids", id + "");
				String result = HttpUtils.postByApi(params[0], parMap);
				return result;
			}

			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if ("".equals(result)) {
					ToastUtils
							.showNetConnectionFail(ServicemanActivity.this);
				} else {
					try {
						JSONObject jsonObject = new JSONObject(result);
						boolean success = jsonObject.getBoolean("success");
						String msg = jsonObject.getString("msg");
						if (success) { // 成功
							ToastUtils.showMessage(
									ServicemanActivity.this, msg);
							currentPage = "1";
							user_List.clear();
							if(userAdapter!=null){
								userAdapter.notifyDataSetChanged();
							}
							getServicemanListInfo();
						} else { // 失败
							ToastUtils.showMessage(
									ServicemanActivity.this, msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	
	class CancelAccountAsyncTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			long id = user_List.get(currentPosition).id;
			Map<String, String> parMap = new HashMap<String, String>();
			parMap.put("ids", id + "");
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if ("".equals(result)) {
				ToastUtils
						.showNetConnectionFail(ServicemanActivity.this);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.getBoolean("success");
					String msg = jsonObject.getString("msg");
					if (success) { // 成功
						ToastUtils.showMessage(
								ServicemanActivity.this, msg);
						currentPage = "1";
						user_List.clear();
						if(userAdapter!=null){
							userAdapter.notifyDataSetChanged();
						}
						getServicemanListInfo();
					} else { // 失败
						ToastUtils.showMessage(ServicemanActivity.this, msg);
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

	class ServicemanListAsycTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			Map<String, String> parMap = new HashMap<String, String>();
			if (user != null) {
				parMap.put("id", user.id + "");
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
				ToastUtils.showNetConnectionFail(ServicemanActivity.this);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONObject jsonObject_msg = jsonObject.getJSONObject("msg");
					int recordsTotal = jsonObject_msg.getInt("recordsTotal");
					if (user_List.size() >= recordsTotal) {

					} else {
						JSONArray jsonArray_data = jsonObject_msg
								.getJSONArray("data");
						for (int i = 0; i < jsonArray_data.length(); i++) {
							JSONObject jsonObject_data = jsonArray_data
									.getJSONObject(i);
							String realName = jsonObject_data
									.getString("realName");
							String userName = jsonObject_data
									.getString("userName");
							String roleName = jsonObject_data
									.getString("roleName");
							String nickName = jsonObject_data
									.getString("nickName");
							String balance = jsonObject_data
									.getString("balance");
							String email = jsonObject_data.getString("email");
							String tel = jsonObject_data.getString("tel");
							String source = jsonObject_data.getString("source");
							String status = jsonObject_data.getString("status");
							String lastLoginTime = jsonObject_data
									.getString("lastLoginTime");
							String registerTime = jsonObject_data
									.getString("registerTime");
							String introduction = jsonObject_data
									.getString("introduction");
							int id = jsonObject_data.getInt("id");
							int roleCode = jsonObject_data.getInt("roleCode");
							int parentId = jsonObject_data.getInt("parentId");
							User user = new User(id, roleCode, roleName,
									userName, realName, nickName, balance,
									email, tel, source, status, lastLoginTime,
									registerTime, introduction);
							user.setParentId(parentId);
							user_List.add(user);
						}
						currentPage = String.valueOf((Integer
								.valueOf(currentPage) + 1));
					}
					if (userAdapter != null) {
						userAdapter.notifyDataSetChanged();
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
			user_List.clear();
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
		PopupMenu popupMenu = new PopupMenu(ServicemanActivity.this, ll_more);
		popupMenu.getMenuInflater().inflate(R.menu.menu_serviceman,popupMenu.getMenu());
		popupMenu.setOnMenuItemClickListener(this);
		popupMenu.show();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item1:
			startActivity(new Intent(ServicemanActivity.this, ServicemanAddActivity.class));
			break;
		default:
			break;
		}
		return false;
	}

	class UserBroadcastReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			currentPage = "1";
			user_List.clear();
			getServicemanListInfo();
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
