package com.bean;

import java.io.Serializable;

/**
 * 
 * @author Administrator
 *	�˻���Ϣ
 */
public class Account implements Serializable{
	public String balance; //���
	public String completeTime; //���ʱ��
	public String createTime; //����ʱ��
	public long id;
	public String money; //������Ǯ��
	public String nickName; //���
	public String remark; //��ע
	public String source; //�û�����
	public String status; //״̬
	public String type; //�û�����
	public long userId;
	public String userName; //�û���
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
