package com.adapter;

import java.util.ArrayList;

import com.bean.Account;
import com.example.Fmb_applianceRepair_System.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AccountAdpater extends BaseAdapter{
	
	private ArrayList<Account> account_List;
	private Context context;

	public AccountAdpater(ArrayList<Account> account_List,Context context){
		this.account_List = account_List;
		this.context = context;
	}
	public int getCount() {
		return account_List.size();
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = null;
		ViewHolder viewHolder=null;
		if(convertView==null){
			view = layoutInflater.inflate(R.layout.lv_account, null);
			TextView tv_type = (TextView) view.findViewById(R.id.tv_type);
			TextView tv_createtime = (TextView) view.findViewById(R.id.tv_createtime);
			TextView tv_status = (TextView) view.findViewById(R.id.tv_status);
			viewHolder=new ViewHolder(tv_type, tv_createtime, tv_status);
			view.setTag(viewHolder);
		}
		else{
			view=convertView;
			viewHolder= (ViewHolder) view.getTag();
		}
		Account account = account_List.get(position);
		viewHolder.tv_type.setText(account.type);
		viewHolder.tv_createtime.setText(account.createTime);
		viewHolder.tv_status.setText(account.status);
		
		return view;
	}
	public Object getItem(int position) {
		return null;
	}
	public long getItemId(int position) {
		return 0;
	}
	class ViewHolder{
		public TextView tv_type;
		public TextView tv_createtime;
		public TextView tv_status;
		public ViewHolder(TextView tv_type, TextView tv_createtime,
				TextView tv_status) {
			super();
			this.tv_type = tv_type;
			this.tv_createtime = tv_createtime;
			this.tv_status = tv_status;
		}
	}
}
