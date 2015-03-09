package com.application;

import com.bean.User;

import android.app.Application;

public class MyApplication extends Application{
	public User user;
	public void onCreate() {
		super.onCreate();
		
	}
	public void setUser(User user){
		this.user=user;
	}
	public User getUser(){
		return user;
	}
}
