package com.example.Fmb_applianceRepair_System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bean.Role;
import com.constant.Constant;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.utils.HttpUtils;
import com.utils.ToastUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RoleManagementActivity extends Activity implements OnClickListener {
	
	private ArrayList<Role> role_List=new ArrayList<Role>();
	private PullToRefreshListView pl_rolelist;
	private RoleAdapter roleAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_role_management);
		initView();
		getRoleList();
	}
	private void getRoleList() {
		new GetRoleListAsyncTask().execute(Constant.GETLISTROLE_URL);
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(this);
		pl_rolelist = (PullToRefreshListView) findViewById(R.id.pl_rolelist);
		pl_rolelist.setOnRefreshListener(new OnRefreshListener<ListView>() {
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
				new GetRoleListAsyncTask().execute(Constant.GETLISTROLE_URL);
			}
		});
		ListView lv_rolelist = pl_rolelist.getRefreshableView();
		roleAdapter = new RoleAdapter();
		lv_rolelist.setAdapter(roleAdapter);
	}
	class GetRoleListAsyncTask extends AsyncTask<String, Void, String>{

		protected String doInBackground(String... params) {
			Map<String, String> parMap=new HashMap<String, String>();
			parMap.put("type", "1");
			String result = HttpUtils.postByApi(params[0], parMap);
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//当数据加载完成，需要调用onRefreshComplete.
			pl_rolelist.onRefreshComplete();
			if("".equals(result)){
				ToastUtils.showNetConnectionFail(RoleManagementActivity.this);
			}
			else{
				role_List.clear();
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONArray jsonArray_msg = jsonObject.getJSONArray("msg");
					for (int i = 0; i < jsonArray_msg.length(); i++) {
						JSONObject jsonObject_msg = jsonArray_msg.getJSONObject(i);
						String code = jsonObject_msg.getString("code");
						String name = jsonObject_msg.getString("name");
						Role role = new Role(code, name);
						role_List.add(role);
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(roleAdapter!=null){
					roleAdapter.notifyDataSetChanged();
				}
			}
		}
	}
	class RoleAdapter extends BaseAdapter{

		public int getCount() {
			return role_List.size();
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			Role role = role_List.get(position);
			View inflate = getLayoutInflater().inflate(R.layout.lv_role, null);
			TextView tv_code = (TextView) inflate.findViewById(R.id.tv_code);
			TextView tv_name = (TextView) inflate.findViewById(R.id.tv_name);
			tv_code.setText(role.code);
			tv_name.setText(role.name);
			return inflate;
		}
		public Object getItem(int position) {
			return null;
		}
		public long getItemId(int position) {
			return 0;
		}
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.role_management, menu);
		return true;
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
	}
}
