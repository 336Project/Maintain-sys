package com.example.Fmb_applianceRepair_System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bean.Role;
import com.bean.Trade;
import com.constant.Constant;
import com.example.Fmb_applianceRepair_System.RoleManagementActivity.GetRoleListAsyncTask;
import com.example.Fmb_applianceRepair_System.RoleManagementActivity.RoleAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.utils.HttpUtils;
import com.utils.ToastUtils;

import android.opengl.ETC1;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionManagementActivity extends Activity implements
		OnClickListener {

	private PullToRefreshListView pl_transactionlist;
	private ArrayList<Trade> trade_List = new ArrayList<Trade>();
	private TanscationAdapter tanscationAdapter;
	private ListView lv_transcationlist;
	private String currentPage = "1";
	private String keyword = "";
	private AsyncTask<String, Void, String> getTradeListAsyncTask;
	private EditText et_search;
	private int currentPosition = 0;
	private int recordsTotal=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction_management);
		initView();
		getTranscationList();
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		pl_transactionlist = (PullToRefreshListView) findViewById(R.id.pl_transactionlist);
		pl_transactionlist
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {

						int flags = DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL;
						String label = DateUtils.formatDateTime(
								getApplicationContext(),
								System.currentTimeMillis(), flags);
						// 更新最后刷新时间
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						// 执行加载更多数据任务.
						new GetTradeListAsyncTask()
								.execute(Constant.GETLISTTRADE_URL);
					}
				});
		pl_transactionlist.setMode(Mode.PULL_FROM_END);
		lv_transcationlist = pl_transactionlist.getRefreshableView();
		tanscationAdapter = new TanscationAdapter();
		lv_transcationlist.setAdapter(tanscationAdapter);
		lv_transcationlist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == 0) {

				} else {
					Trade trade = trade_List.get(position - 1);
					Intent intent = new Intent();
					intent.setClass(TransactionManagementActivity.this,
							TransactionParticularActivity.class);
					intent.putExtra("trade", trade);
					startActivity(intent);
				}
			}
		});

		lv_transcationlist
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						menu.setHeaderTitle("操作");
						menu.add(0, 0, 0, "删除该条记录");
						menu.add(0, 1, 0, "取消");
					}
				});
		lv_transcationlist
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int position, long arg3) {
						currentPosition = position - 1;
						lv_transcationlist.showContextMenu();
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
				trade_List.clear();
				if (getTradeListAsyncTask != null) {
					getTradeListAsyncTask.cancel(true);
				}
				getTradeListAsyncTask = new GetTradeListAsyncTask()
						.execute(Constant.GETLISTTRADE_URL);
			}
		});
		findViewById(R.id.tv_cancel).setOnClickListener(this);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			new DeleteTradeAsyncTask().execute(Constant.DELETETRADE_URL);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void getTranscationList() {
		if (getTradeListAsyncTask != null) {
			getTradeListAsyncTask.cancel(true);
		}
		getTradeListAsyncTask = new GetTradeListAsyncTask()
				.execute(Constant.GETLISTTRADE_URL);
	}

	class GetTradeListAsyncTask extends AsyncTask<String, Void, String> {
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
			// 当数据加载完成，需要调用onRefreshComplete.
			pl_transactionlist.onRefreshComplete();
			if ("".equals(result)) {
				ToastUtils
						.showNetConnectionFail(TransactionManagementActivity.this);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONObject jsonObject_msg = jsonObject.getJSONObject("msg");
					int recordsTotal = jsonObject_msg.getInt("recordsTotal");
					if(trade_List.size()>=recordsTotal){
						
					}
					else{
						JSONArray jsonArray_data = jsonObject_msg
								.getJSONArray("data");
						for (int i = 0; i < jsonArray_data.length(); i++) {
							JSONObject jsonObject_data = jsonArray_data
									.getJSONObject(i);
							int id = jsonObject_data.getInt("id");
							int fromUserId = jsonObject_data.getInt("fromUserId"); // 资金流出的用户id
							int toUserId = jsonObject_data.getInt("toUserId"); // 资金流进的用户id
							String fromUserName = jsonObject_data
									.getString("fromUserName");// 资金流出的用户账号
							String toUserName = jsonObject_data
									.getString("toUserName");// 资金流进的用户账号
							String fromUserNickName = jsonObject_data
									.getString("fromUserNickName"); // 资金流出的用户昵称
							String toUserNickName = jsonObject_data
									.getString("toUserNickName"); // 资金流进的用户昵称
							String money = jsonObject_data.getString("money"); // 交易金额
							String status = jsonObject_data.getString("status"); // 状态
							String time = jsonObject_data.getString("time"); // 交易时间
							Trade trade = new Trade(id, fromUserId, toUserId,
									fromUserName, toUserName, fromUserNickName,
									toUserNickName, money, status, time);
							trade_List.add(trade);
						}
						currentPage=String.valueOf((Integer.valueOf(currentPage)+1));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (tanscationAdapter != null) {
					tanscationAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	class DeleteTradeAsyncTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			Integer id = trade_List.get(currentPosition).getId();
			Map<String, String> parMap = new HashMap<String, String>();
			parMap.put("ids", id + "");
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if ("".equals(result)) {
				ToastUtils
						.showNetConnectionFail(TransactionManagementActivity.this);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.getBoolean("success");
					String msg = jsonObject.getString("msg");
					if (success) { // 成功
						ToastUtils.showMessage(
								TransactionManagementActivity.this, msg);
						currentPage = "1";
						trade_List.clear();
						if(tanscationAdapter!=null){
							tanscationAdapter.notifyDataSetChanged();
						}
						if (getTradeListAsyncTask != null) {
							getTradeListAsyncTask.cancel(true);
						}
						getTradeListAsyncTask = new GetTradeListAsyncTask()
								.execute(Constant.GETLISTTRADE_URL);
					} else { // 失败
						ToastUtils.showMessage(
								TransactionManagementActivity.this, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}

	class TanscationAdapter extends BaseAdapter {
		public int getCount() {
			return trade_List.size();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Trade trade = trade_List.get(position);
			View inflate = getLayoutInflater().inflate(R.layout.lv_transaction,
					null);
			TextView tv_time = (TextView) inflate.findViewById(R.id.tv_time);
			TextView tv_money = (TextView) inflate.findViewById(R.id.tv_money);
			TextView tv_status = (TextView) inflate
					.findViewById(R.id.tv_status);
			tv_time.setText(trade.getTime());
			tv_money.setText(trade.getMoney());
			tv_status.setText(trade.getStatus());
			return inflate;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_cancel:
			keyword = "";
			currentPage = "1";
			trade_List.clear();
			et_search.setText("");
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.transaction_management, menu);
		return true;
	}
}
