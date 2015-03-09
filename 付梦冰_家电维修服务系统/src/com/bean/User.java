package com.bean;

import java.io.Serializable;

public class User implements Serializable{
	public int id;
	public int roleCode;
	public String roleName;
	public String userName;
	public String realName;
	public String nickName;
	public String balance; //余额
	public String email; //邮箱
	public String tel; //电话
	public String source; //账号来源
	public String status; //账号状态
	public String lastLoginTime; //上次登录时间
	public String registerTime; //注册时间
	public String introduction; //简介
	
	public User(int id, int roleCode, String roleName, String userName,
			String realName, String nickName, String balance, String email,
			String tel, String source, String status, String lastLoginTime, String registerTime, String introduction) {
		super();
		this.id = id;
		this.roleCode = roleCode;
		this.roleName = roleName;
		this.userName = userName;
		this.realName = realName;
		this.nickName = nickName;
		this.balance = balance;
		this.email = email;
		this.tel = tel;
		this.source = source;
		this.status = status;
		this.lastLoginTime = lastLoginTime;
		this.registerTime = registerTime;
		this.introduction = introduction;
	}
}
