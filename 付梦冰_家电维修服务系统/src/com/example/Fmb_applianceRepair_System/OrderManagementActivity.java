package com.example.Fmb_applianceRepair_System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.adapter.OrderAdpater;
import com.bean.Order;
import com.constant.Constant;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.utils.HttpUtils;
import com.utils.ToastUtils;

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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class OrderManagementActivity extends Activity implements
		OnClickListener {

	private PullToRefreshListView pl_orderlist;
	private ArrayList<Order> order_List = new ArrayList<Order>();
	private ListView lv_orderlist;
	private String currentPage = "1";
	private String keyword = "";
	private AsyncTask<String, Void, String> getTradeListAsyncTask;
	private EditText et_search;
	private int currentPosition = 0;
	private OrderAdpater orderAdpater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_management);
		initView();
		getOrderList();
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		pl_orderlist = (PullToRefreshListView) findViewById(R.id.pl_orderlist);
		pl_orderlist
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
								.execute(Constant.GETORDER_URL);
					}
				});
		pl_orderlist.setMode(Mode.PULL_FROM_END);
		lv_orderlist = pl_orderlist.getRefreshableView();
		orderAdpater = new OrderAdpater(order_List, this);
		lv_orderlist.setAdapter(orderAdpater);
		lv_orderlist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == 0) {

				} else {
					Order order= order_List.get(position - 1);
					Intent intent = new Intent();
					intent.setClass(OrderManagementActivity.this,
							OrderParticularActivity.class);
					intent.putExtra("order", order);
					startActivity(intent);
				}
			}
		});

		lv_orderlist
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						menu.setHeaderTitle("操作");
						menu.add(0, 0, 0, "作废该条记录");
						menu.add(0, 1, 0, "删除该条记录");
						menu.add(0, 2, 0, "取消");
					}
				});
		lv_orderlist
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int position, long arg3) {
						currentPosition = position - 1;
						lv_orderlist.showContextMenu();
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
				order_List.clear();
				if (getTradeListAsyncTask != null) {
					getTradeListAsyncTask.cancel(true);
				}
				getTradeListAsyncTask = new GetTradeListAsyncTask()
						.execute(Constant.GETORDER_URL);
			}
		});
		findViewById(R.id.tv_cancel).setOnClickListener(this);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			new DeleteTradeAsyncTask().execute(Constant.INVALIAORDER_URL);
			break;
		case 1:
			new DeleteTradeAsyncTask().execute(Constant.DELETEORDER_URL);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void getOrderList() {
		if (getTradeListAsyncTask != null) {
			getTradeListAsyncTask.cancel(true);
		}
		getTradeListAsyncTask = new GetTradeListAsyncTask()
				.execute(Constant.GETORDER_URL);
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
			pl_orderlist.onRefreshComplete();
			if ("".equals(result)) {
				ToastUtils
						.showNetConnectionFail(OrderManagementActivity.this);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONObject jsonObject_msg = jsonObject.getJSONObject("msg");
					int recordsTotal = jsonObject_msg.getInt("recordsTotal");
					if(order_List.size()>=recordsTotal){
						
					}
					else{
						JSONArray jsonArray_data = jsonObject_msg
								.getJSONArray("data");
						for (int i = 0; i < jsonArray_data.length(); i++) {
							JSONObject jsonObject_data = jsonArray_data
									.getJSONObject(i);
							int id = jsonObject_data.getInt("id");
							int userId = jsonObject_data.getInt("userId");
							String createTime = jsonObject_data.getString("createTime"); //创建时间
							String completeTime = jsonObject_data.getString("completeTime"); //完成时间
							String quoteTime = jsonObject_data.getString("quoteTime"); //报价时间
							String customerUser = jsonObject_data.getString("customerUser"); //用户
							String contactTelUser = jsonObject_data.getString("contactTelUser"); //用户电话
							String customerCompany = jsonObject_data.getString("customerCompany"); //维修公司名称
							String contactTelCompany = jsonObject_data.getString("contactTelCompany"); //维修公司电话
							int companyId = jsonObject_data.getInt("companyId"); //维修公司Id;
							String price = jsonObject_data.getString("price"); //报价
							String repairContent = jsonObject_data.getString("repairContent"); //报价内容
							String status = jsonObject_data.getString("status"); //状态
							String address = jsonObject_data.getString("address"); // 状态
							String quoteContent = jsonObject_data.getString("quoteContent");
							String repairMan = jsonObject_data.getString("repairMan");
							Order order = new Order(id, userId, companyId, completeTime, createTime, quoteTime, status, repairContent, customerUser, customerCompany, contactTelUser, contactTelCompany, price,address,quoteContent,repairMan);
							order_List.add(order);
						}
						currentPage=String.valueOf((Integer.valueOf(currentPage)+1));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (orderAdpater != null) {
					orderAdpater.notifyDataSetChanged();
				}
			}
		}
	}

	class DeleteTradeAsyncTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
			long id = order_List.get(currentPosition).id;
			Map<String, String> parMap = new HashMap<String, String>();
			parMap.put("ids", id + "");
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if ("".equals(result)) {
				ToastUtils
						.showNetConnectionFail(OrderManagementActivity.this);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.getBoolean("success");
					String msg = jsonObject.getString("msg");
					if (success) { // 成功
						ToastUtils.showMessage(
								OrderManagementActivity.this, msg);
						currentPage = "1";
						
						order_List.clear();
						if(orderAdpater!=null){
							orderAdpater.notifyDataSetChanged();
						}
						if (getTradeListAsyncTask != null) {
							getTradeListAsyncTask.cancel(true);
						}
						getTradeListAsyncTask = new GetTradeListAsyncTask()
								.execute(Constant.GETORDER_URL);
					} else { // 失败
						ToastUtils.showMessage(
								OrderManagementActivity.this, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
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
			order_List.clear();
			et_search.setText("");
			break;
		default:
			break;
		}
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.transaction_management, menu);
		return true;
	}
}
