package com.bean;

import java.io.Serializable;

/**
 * 
 * @author Administrator
 *	账户信息
 */
public class Account implements Serializable{
	public String balance; //余额
	public String completeTime; //完成时间
	public String createTime; //创建时间
	public long id;
	public String money; //操作的钱数
	public String nickName; //简称
	public String remark; //备注
	public String source; //用户操作
	public String status; //状态
	public String type; //用户操作
	public long userId;
	public String userName; //用户名
	public Account(String balance, String completeTime, String createTime,
			long id, String money, String nickName, String remark,
			String source, String status, String type, long userId,
			String userName) {
		super();
		this.balance = balance;
		this.completeTime = completeTime;
		this.createTime = createTime;
		this.id = id;
		this.money = money;
		this.nickName = nickName;
		this.remark = remark;
		this.source = source;
		this.status = status;
		this.type = type;
		this.userId = userId;
		this.userName = userName;
	}
}
