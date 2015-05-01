package com.bean;

import java.io.Serializable;

public class User implements Serializable{
	public int id;
	public int roleCode;
	public String roleName;
	public String userName;
	public String realName;
	public String nickName;
	public String balance; //���
	public String email; //����
	public String tel; //�绰
	public String source; //�˺���Դ
	public String status; //�˺�״̬
	public String lastLoginTime; //�ϴε�¼ʱ��
	public String registerTime; //ע��ʱ��
	public String introduction; //���
	private int parentId; //������˾
	
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

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
}
