package com.example.Fmb_applianceRepair_System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.adapter.OrderAdpater;
import com.application.MyApplication;
import com.asynctask.UpdateOrderStatusAsyncTask;
import com.bean.Order;
import com.bean.User;
import com.constant.Constant;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.utils.HttpUtils;
import com.utils.ToastUtils;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class OrderActivity extends Activity implements OnClickListener,
		OnMenuItemClickListener {

	private User user;
	private ListView lv_orderlist;
	private LinearLayout ll_more;
	private ArrayList<Order> order_List = new ArrayList<Order>();
	private OrderAdpater orderAdpater;
	private PullToRefreshListView pl_orderlist;
	private AsyncTask<String, Void, String> orderAsyncTask;
	private String currentPage = "1";
	private String keyword = "";
	private EditText et_search;
	protected int currentPosition;
	private UserBroadcastReceiver userBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		getUser();
		initView();
		getOrderInfo();
		userBroadcastReceiver = new UserBroadcastReceiver();
		IntentFilter filter = new IntentFilter("orderUpdate");
		registerReceiver(userBroadcastReceiver, filter);
	}
	private void getOrderInfo() {
		if (orderAsyncTask != null) {
			orderAsyncTask.cancel(true);
		}
		orderAsyncTask = new OrderAsycTask().execute(Constant.order_GetOrderListByUser);
	}
	private void getUser() {
		user = ((MyApplication) getApplication()).getUser();
	}

	private void initView() {

		pl_orderlist = (PullToRefreshListView) findViewById(R.id.pl_orderlist);
		pl_orderlist.setOnRefreshListener(new OnRefreshListener<ListView>() {
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
				getOrderInfo();
			}
		});

		pl_orderlist.setMode(Mode.PULL_FROM_END);
		lv_orderlist = pl_orderlist.getRefreshableView();
		orderAdpater = new OrderAdpater(order_List, this);
		lv_orderlist.setAdapter(orderAdpater);
		lv_orderlist
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						menu.setHeaderTitle("操作");
						menu.add(0, 0, 0, "确认");
						menu.add(0, 1, 0, "修改");
						menu.add(0, 2, 0, "取消");
						menu.add(0, 3, 0, "工人已到");
						menu.add(0, 4, 0, "工人未到");
						menu.add(0, 5, 0, "确认支付");
						menu.add(0, 6, 0, "返回");
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
		lv_orderlist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == 0) {

				} else {
					Order order= order_List.get(position - 1);
					Intent intent = new Intent();
					intent.setClass(OrderActivity.this,
							OrderParticularActivity.class);
					intent.putExtra("order", order);
					startActivity(intent);
				}
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
				order_List.clear();
				getOrderInfo();
			}
		});
		findViewById(R.id.tv_cancel).setOnClickListener(this);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		String status = order_List.get(currentPosition).status;
		Order order = order_List.get(currentPosition);
		Map<String, String> parMap=new HashMap<String, String>();
		switch (item.getItemId()) {
		case 0:
			new Order_ConfirmAsyncTask().execute(Constant.order_Confirm);
			break;
		case 1:
			if(status.equals("新订单")){
				Intent intent=new Intent(OrderActivity.this, OrderUpdateActivity.class);
				intent.putExtra("order", order);
				startActivity(intent);
			}
			else{
				ToastUtils.showMessage(OrderActivity.this, "只有新订单才能修改");
			}
			break;
		case 2:
			new Order_ConfirmAsyncTask().execute(Constant.order_Cancel);
			break;
		case 3:
			if(status.equals("已派遣")||status.equals("工人未到")||status.equals("工人已到")){
				parMap.put("ids", order.id+"");
				parMap.put("status", "工人已到");
				new UpdateOrderStatusAsyncTask(this,parMap).execute(Constant.order_UpdateStatusById);
			}
			else{
				ToastUtils.showMessage(OrderActivity.this, "只有已派遣订单才能确定维修人员是否到达~");
			}
			break;
		case 4:
			if(status.equals("已派遣")||status.equals("工人未到")||status.equals("工人已到")){
				parMap.put("ids", order.id+"");
				parMap.put("status", "工人未到");
				new UpdateOrderStatusAsyncTask(this,parMap).execute(Constant.order_UpdateStatusById);
			}
			else{
				ToastUtils.showMessage(OrderActivity.this, "只有已派遣订单才能确定维修人员是否到达~");
			}
			break;
		case 5:
			if(status.equals("已完成")){
				Intent intent=new Intent(OrderActivity.this, PayActivity.class);
				intent.putExtra("order", order);
				startActivity(intent);
			}
			else{
				ToastUtils.showMessage(OrderActivity.this, "只有已完成订单才可支付");
			}
			break;
			
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	class Order_ConfirmAsyncTask extends AsyncTask<String, Void, String> {
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
						.showNetConnectionFail(OrderActivity.this);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.getBoolean("success");
					String msg = jsonObject.getString("msg");
					if (success) { // 成功
						ToastUtils.showMessage(
								OrderActivity.this, msg);
						currentPage = "1";
						order_List.clear();
						if(orderAdpater!=null){
							orderAdpater.notifyDataSetChanged();
						}
						getOrderInfo();
					} else { // 失败
						ToastUtils.showMessage(
								OrderActivity.this, msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class OrderAsycTask extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... params) {
			Map<String, String> parMap = new HashMap<String, String>();
			if (user != null) {
				parMap.put("keyword", user.nickName);
			}
			parMap.put("roleCode", user.roleCode+"");
			parMap.put("currentPage", currentPage);
			parMap.put("ids", user.id+"");
			parMap.put("keyword",keyword);
			
			
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pl_orderlist.onRefreshComplete();
			if ("".equals(result)) {
				ToastUtils.showNetConnectionFail(OrderActivity.this);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONObject jsonObject_msg = jsonObject.getJSONObject("msg");
					int recordsTotal = jsonObject_msg.getInt("recordsTotal");
					if (order_List.size() >= recordsTotal) {

					} else {
						JSONArray jsonArray_data = jsonObject_msg
								.getJSONArray("data");
						for (int i = 0; i < jsonArray_data.length(); i++) {
							JSONObject jsonObject_data = jsonArray_data
									.getJSONObject(i);
							int id = jsonObject_data.getInt("id");
							int userId = jsonObject_data.getInt("userId");
							String createTime = jsonObject_data
									.getString("createTime"); // 创建时间
							String completeTime = jsonObject_data
									.getString("completeTime"); // 完成时间
							String quoteTime = jsonObject_data
									.getString("quoteTime"); // 报价时间
							String customerUser = jsonObject_data
									.getString("customerUser"); // 用户
							String contactTelUser = jsonObject_data
									.getString("contactTelUser"); // 用户电话
							String customerCompany = jsonObject_data
									.getString("customerCompany"); // 维修公司名称
							String contactTelCompany = jsonObject_data
									.getString("contactTelCompany"); // 维修公司电话
							int companyId = jsonObject_data.getInt("companyId"); // 维修公司Id;
							String price = jsonObject_data.getString("price"); // 报价
							String repairContent = jsonObject_data
									.getString("repairContent"); // 报价内容
							String status = jsonObject_data.getString("status"); // 状态
							String address = jsonObject_data.getString("address"); // 状态
							String quoteContent = jsonObject_data.getString("quoteContent");
							String repairMan = jsonObject_data.getString("repairMan");
							Order order = new Order(id, userId, companyId,
									completeTime, createTime, quoteTime,
									status, repairContent, customerUser,
									customerCompany, contactTelUser,
									contactTelCompany, price,address,quoteContent,repairMan);
							order_List.add(order);
						}
						currentPage = String.valueOf((Integer
								.valueOf(currentPage) + 1));
					}
					if (orderAdpater != null) {
						orderAdpater.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.ll_more:
			btn_more();
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

	@SuppressLint("NewApi")
	private void btn_more() {
		PopupMenu popupMenu = new PopupMenu(OrderActivity.this, ll_more);
		popupMenu.getMenuInflater().inflate(R.menu.menu_order,
				popupMenu.getMenu());
		popupMenu.setOnMenuItemClickListener(this);
		popupMenu.show();
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item1:
			item_repair();
			break;
		default:
			break;
		}
		return false;
	}
	class UserBroadcastReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			currentPage = "1";
			order_List.clear();
			getOrderInfo();
		}
	}
	// 跳转到报修界面
	private void item_repair() {
		startActivity(new Intent(OrderActivity.this, RepairActivity.class));
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (userBroadcastReceiver != null) {
			unregisterReceiver(userBroadcastReceiver);
		}
	}
}
