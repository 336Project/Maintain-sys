package com.bean;

import java.io.Serializable;

public class Order implements Serializable{
	public long id;
	public long userId;
	public long companyId;
	public String completeTime; //���ʱ��
	public String createTime; //����ʱ��
	public String quoteTime; //����ʱ��
	public String status; //��ע
	public String repairContent;
	public String customerUser;
	public String customerCompany;
	public String contactTelUser;
	public String contactTelCompany;
	public String price;
	public Order(long id, long userId, long companyId, String completeTime,
			String createTime, String quoteTime, String status,
			String repairContent, String customerUser, String customerCompany,
			String contactTelUser, String contactTelCompany, String price) {
		super();
		this.id = id;
		this.userId = userId;
		this.companyId = companyId;
		this.completeTime = completeTime;
		this.createTime = createTime;
		this.quoteTime = quoteTime;
		this.status = status;
		this.repairContent = repairContent;
		this.customerUser = customerUser;
		this.customerCompany = customerCompany;
		this.contactTelUser = contactTelUser;
		this.contactTelCompany = contactTelCompany;
		this.price = price;
	}
}
