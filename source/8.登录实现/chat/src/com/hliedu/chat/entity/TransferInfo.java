package com.hliedu.chat.entity;

import java.io.Serializable;

/**
 * 数据封装
 * 
 * 带你轻松学Java：恒骊学堂
 * www.hliedu.com
 * QQ群：107184365
 *
 */
public class TransferInfo implements Serializable{

	private static final long serialVersionUID = 6543722756249559791L;
	
	private String userName;
	private String password;
	
	//登录成功标志
	private Boolean loginSucceessFlag = false;
	
	public Boolean getLoginSucceessFlag() {
		return loginSucceessFlag;
	}
	public void setLoginSucceessFlag(Boolean loginSucceessFlag) {
		this.loginSucceessFlag = loginSucceessFlag;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
