package com.adapter;

import java.util.ArrayList;

import com.bean.Account;
import com.bean.Order;
import com.example.Fmb_applianceRepair_System.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderAdpater extends BaseAdapter{
	
	private ArrayList<Order> order_List;
	private Context context;

	public OrderAdpater(ArrayList<Order> order_List,Context context){
		this.order_List = order_List;
		this.context = context;
	}
	public int getCount() {
		return order_List.size();
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = null;
		ViewHolder viewHolder=null;
		if(convertView==null){
			view = layoutInflater.inflate(R.layout.lv_order, null);
			
			TextView tv_customerCompany = (TextView) view.findViewById(R.id.tv_customerCompany);
			TextView tv_contactTelCompany = (TextView) view.findViewById(R.id.tv_contactTelCompany);
			TextView tv_createTime = (TextView) view.findViewById(R.id.tv_createTime);
			TextView tv_quoteTime = (TextView) view.findViewById(R.id.tv_quoteTime);
			TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
			TextView tv_status = (TextView) view.findViewById(R.id.tv_status);
			viewHolder = new ViewHolder(tv_customerCompany, tv_contactTelCompany, tv_createTime, tv_quoteTime, tv_price, tv_status);
			view.setTag(viewHolder);
		}
		else{
			view=convertView;
			viewHolder= (ViewHolder) view.getTag();
		}
		Order order = order_List.get(position);
		viewHolder.tv_customerCompany.setText(order.customerCompany);
		viewHolder.tv_contactTelCompany.setText(order.contactTelCompany);
		viewHolder.tv_createTime.setText(order.createTime);
		if(!order.quoteTime.equals("null")){
			viewHolder.tv_quoteTime.setText(order.quoteTime);
		}
		if(!order.price.equals("null")){
			viewHolder.tv_price.setText(order.price);
		}
		viewHolder.tv_status.setText(order.status);
		return view;
	}
	public Object getItem(int position) {
		return null;
	}
	public long getItemId(int position) {
		return 0;
	}
	class ViewHolder{
		public TextView tv_customerCompany;
		public TextView tv_contactTelCompany;
		public TextView tv_createTime;
		public TextView tv_quoteTime;
		public TextView tv_price;
		public TextView tv_status;
		public ViewHolder(TextView tv_customerCompany,
				TextView tv_contactTelCompany, TextView tv_createTime,
				TextView tv_quoteTime, TextView tv_price, TextView tv_status) {
			super();
			this.tv_customerCompany = tv_customerCompany;
			this.tv_contactTelCompany = tv_contactTelCompany;
			this.tv_createTime = tv_createTime;
			this.tv_quoteTime = tv_quoteTime;
			this.tv_price = tv_price;
			this.tv_status = tv_status;
		}
		
	}
}
