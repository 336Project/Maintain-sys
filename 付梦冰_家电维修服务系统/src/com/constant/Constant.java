package com.constant;

public class Constant {
	public static final String USER_SET="user.set";
	public  static final String URL="http://192.168.56.1:8080";
	public  static final String LOGIN_URL=URL+"/sechand-platform/app/appUserAction!login.action";
	public  static final String GETLISTUSER_URL=URL+"/sechand-platform/app/appUserAction!listUsersByParams.action";
	public  static final String DELETEUSER_URL=URL+"/sechand-platform/app/appUserAction!deleteUserByIds.action";
	public  static final String ADDUSER_URL=URL+"/sechand-platform/app/appUserAction!addByManual.action";
	public  static final String RESETPASSWORD_URL=URL+"/sechand-platform/app/appUserAction!resetPassword.action";
	public  static final String GETACCOUNT_URL=URL+"/sechand-platform/app/accountAction!listAccountsByParams.action";
	public  static final String GETACCOUNTFROMADMIN_URL=URL+"/sechand-platform/app/accountAction!listAccountsByParamsFromAdmin.action";
	public  static final String GETORDER_URL=URL+"/sechand-platform/app/appOrderAction!listOrdersByParams.action";
	public  static final String DELETEORDER_URL=URL+"/sechand-platform/app/appOrderAction!deleteOrderByIds.action";
	public  static final String INVALIAORDER_URL=URL+"/sechand-platform/app/appOrderAction!disableOrderByIds.action";
	public  static final String SAVEUSERDATE_URL=URL+"/sechand-platform/app/appUserAction!updateUser.action";
	public  static final String SAVEUSERDATEFROMADMINISTRATOR_URL=URL+"/sechand-platform/app/appUserAction!updateUserFromAdministrator.action";
	public  static final String GETLISTCOMPANY_URL=URL+"/sechand-platform/app/appUserAction!listCompany.action";
	public  static final String REALTOPUP_URL=URL+"/sechand-platform/app/accountAction!applyRecharge.action";
	public  static final String PICKUP_URL=URL+"/sechand-platform/app/accountAction!applyPickup.action";
	public  static final String GETLISTROLE_URL=URL+"/sechand-platform/app/appRoleAction!listRole.action";
	public  static final String GETLISTTRADE_URL=URL+"/sechand-platform/app/appTradeAction!listTradesByParams.action";
	public  static final String DELETETRADE_URL=URL+"/sechand-platform/app/appTradeAction!deleteTradeByIds.action";
	//-------维修人员信息-------
	public  static final String getServicemanList_URL=URL+"/sechand-platform/app/appUserAction!listRepairByParams.action";
	public  static final String getServicemanAllList_URL=URL+"/sechand-platform/app/appUserAction!listServiceman.action";
	public  static final String dispatchServiceman_URL=URL+"/sechand-platform/app/appOrderAction!dispatch.action";
	
	//-------账户操作------
	public  static final String account_ConfirmById=URL+"/sechand-platform/app/accountAction!confirmAccount.action";
	public  static final String account_DeleteById=URL+"/sechand-platform/app/accountAction!deleteAccountByIds.action";
	public  static final String account_addByAdmin=URL+"/sechand-platform/app/accountAction!applyRechargeByAdmin.action";
	public  static final String account_Cancel=URL+"/sechand-platform/app/accountAction!cancelAccountById.action";
	//------用户操作------
	public  static final String user_GetAllUser=URL+"/sechand-platform/app/appUserAction!listAllUsers.action";
	public  static final String user_RegisterUser=URL+"/sechand-platform/app/appUserAction!register.action";
	
	//------订单操作------
	public  static final String order_Confirm=URL+"/sechand-platform/app/appOrderAction!confirmOrderById.action";
	public  static final String order_Cancel=URL+"/sechand-platform/app/appOrderAction!cancelOrderById.action";
	public  static final String order_Update=URL+"/sechand-platform/app/appOrderAction!updateByCustomer.action";
	public  static final String order_Repair=URL+"/sechand-platform/app/appOrderAction!repairByCustomer.action";
	public  static final String order_GetOrderListByUser=URL+"/sechand-platform/app/appOrderAction!listCustomerOrdersByParams.action";
	public  static final String order_Quote=URL+"/sechand-platform/app/appOrderAction!quoteOrderByCompany.action";
	public  static final String order_CompleteOrderById=URL+"/sechand-platform/app/appOrderAction!completeOrderById.action";
	public  static final String order_UpdateStatusById=URL+"/sechand-platform/app/appOrderAction!updateStatusByIds.action";
}
