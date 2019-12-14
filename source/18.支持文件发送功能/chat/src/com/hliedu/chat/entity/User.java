package com.hliedu.chat.entity;

/**
 * 好友列表展示
 * @author Ron
 *
 */
public class User {

	private String userName;
	private String motto;
	private String nickName;
	private String uiconPath;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMotto() {
		return motto;
	}
	public void setMotto(String motto) {
		this.motto = motto;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUiconPath() {
		return uiconPath;
	}
	public void setUiconPath(String uiconPath) {
		this.uiconPath = uiconPath;
	}
	
}
