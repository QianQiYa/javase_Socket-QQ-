package com.hliedu.chat.server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;

import com.hliedu.chat.entity.ChatStatus;
import com.hliedu.chat.entity.TransferInfo;
import com.hliedu.chat.io.IOStream;
import com.hliedu.chat.server.ui.OnlineUserPanel;

/**
 * 服务器端开辟一个线程，来处理一直读消息
 * 
 * 带你轻松学Java：恒骊学堂
 * www.hliedu.com
 * QQ群：107184365
 *
 */
public class ServerHandler extends Thread {

	Socket socket;
	
	ServerFrame serverFrame;
	
	public ServerHandler(Socket socket , ServerFrame serverFrame) {
		this.socket = socket;
		
		this.serverFrame = serverFrame;
	}
	
	static List<String> onlineUsers = new ArrayList<>();
	static List<Socket> onlineSockets = new ArrayList<>();
	
	@Override
	public void run() {
		
		//默认重复拿
		while(true) {
			try {
				//模拟一直拿消息，产生阻塞
				Object obj = IOStream.readMessage(socket);
				if(obj instanceof TransferInfo) {
					TransferInfo tfi = (TransferInfo)obj;
					if(tfi.getStatusEnum() == ChatStatus.LOGIN) {
						//这是登录
						loginHandler(tfi);
						
					} else if(tfi.getStatusEnum() == ChatStatus.CHAT){
						//这是聊天消息
						chatHandler(tfi);
					} else if(tfi.getStatusEnum() == ChatStatus.DD){
						//这是抖动的消息
						doudong(tfi);
					}
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * 发送抖动消息到客户端
	 * @param tfi
	 */
	private void doudong(TransferInfo tfi) {
		//转发给其他用户
		String reciver = tfi.getReciver();
		if("All".equals(reciver)) {
			//发送给所有人
			sendAll(tfi);
		} else {
			//私聊
			
		}
	}
	
	/**
	 * 处理客户端聊天请求
	 * @param tfi
	 */
	private void chatHandler(TransferInfo tfi) {
		//转发给其他用户
		String reciver = tfi.getReciver();
		if("All".equals(reciver)) {
			//发送给所有人
			sendAll(tfi);
		} else {
			//私聊
			
		}
	}
	
	/**
	 * 处理客户端的登录请求
	 * @param tfi
	 */
	private void loginHandler(TransferInfo tfi) {
		boolean flag = checkUserLogin(tfi);
		tfi.setLoginSucceessFlag(false);
		if(flag) {
			//返回登录成功给客户端
			tfi.setLoginSucceessFlag(true);
			tfi.setStatusEnum(ChatStatus.LOGIN);
			IOStream.writeMessage(socket , tfi);
			String userName = tfi.getUserName();
			
			//统计在线人数
			onlineUsers.add(userName);
			onlineSockets.add(socket);
			
			//发系统消息给客户端，该用户已上线
			tfi = new TransferInfo();
			tfi.setStatusEnum(ChatStatus.NOTICE);
			tfi.setNotice(" >>> "+ userName +" 上线啦");
			sendAll(tfi);
			
			//准备最新用户列表给当前客户端
			tfi = new TransferInfo();
			tfi.setUserOnlineArray(onlineUsers.toArray(new String [onlineUsers.size()]));
			tfi.setStatusEnum(ChatStatus.ULIST);
			sendAll(tfi);
			
			//刷新在线用户列表
			flushOnlineUserList();
		}else {
			System.out.println("登录失败");
			//返回登录失败给客户端
			IOStream.writeMessage(socket , tfi);
		}
	}
	
	/**
	 * 刷新用户列表
	 */
	public void flushOnlineUserList() {
		JList lstUser = serverFrame.onlineUserPanel.lstUser;
		
		String[] userArray = onlineUsers.toArray(new String [onlineUsers.size()]);
		
		lstUser.setListData(userArray);
	}
	
	/**
	 * 发送消息给所有人
	 * @param tfi
	 */
	public void sendAll(TransferInfo tfi) {
		for (int i = 0; i < onlineSockets.size(); i++) {
			Socket tempSocket = onlineSockets.get(i);
			IOStream.writeMessage(tempSocket , tfi);
		}
	}
	
	/**
	 * 登录功能
	 * @param tfi
	 * @return
	 */
	public boolean checkUserLogin(TransferInfo tfi) {
		try {
			String userName = tfi.getUserName();
			String password = tfi.getPassword();
			FileInputStream fis = new FileInputStream(new File("src/user.txt"));
			DataInputStream dis = new DataInputStream(fis);
			String row = null;
			while((row = dis.readLine()) != null) {
				//从文件中读取的行
				if((userName+"|"+password).equals(row)) {
					System.out.println("服务端：用户名，密码正确");
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
