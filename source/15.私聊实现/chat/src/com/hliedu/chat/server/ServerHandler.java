package com.hliedu.chat.server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JTextPane;

import com.hliedu.chat.entity.ChatStatus;
import com.hliedu.chat.entity.FontStyle;
import com.hliedu.chat.entity.TransferInfo;
import com.hliedu.chat.io.IOStream;
import com.hliedu.chat.server.ui.OnlineUserPanel;
import com.hliedu.chat.util.FontSupport;

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
	
	//每个人持有的连接在服务器端都是存在的，需要根据接收人来发送消息
	
	
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
			//记录日志
			log(tfi.getSender() + "给所有人发抖动");
		} else {
			//私聊
			send(tfi);
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
			List<FontStyle> contents = tfi.getContent();
			//记录日志
			FontSupport.fontDecode(serverFrame.serverInfoPanel.txtLog, contents, tfi.getSender(), "所有人");
		} else {
			//私聊
			send(tfi);
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
			
			//在线用户和管道的对应关系
			ChatServer.userSocketMap.put(userName , socket);
			
			//发系统消息给客户端，该用户已上线
			tfi = new TransferInfo();
			tfi.setStatusEnum(ChatStatus.NOTICE);
			String notice = " >>> "+ userName +" 上线啦";
			tfi.setNotice(notice);
			sendAll(tfi);
			
			//准备最新用户列表给当前客户端
			tfi = new TransferInfo();
			tfi.setUserOnlineArray(onlineUsers.toArray(new String [onlineUsers.size()]));
			tfi.setStatusEnum(ChatStatus.ULIST);
			sendAll(tfi);
			
			//刷新在线用户列表
			flushOnlineUserList();
			
			//记录日志
			log(notice);
		}else {
			//返回登录失败给客户端
			IOStream.writeMessage(socket , tfi);
			//记录日志
			log(tfi.getUserName() + "登录失败");
		}
	}
	
	/**
	 * 记录日志的方法
	 * @param log
	 */
	private void log(String log) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(date);
		JTextPane txtLog = serverFrame.serverInfoPanel.txtLog;
		String oldLog = txtLog.getText();
		txtLog.setText(oldLog + "\n" + dateStr + " " + log);
	}
	/**
	 * 刷新用户列表
	 */
	public void flushOnlineUserList() {
		JList lstUser = serverFrame.onlineUserPanel.lstUser;
		
		String[] userArray = onlineUsers.toArray(new String [onlineUsers.size()]);
		
		lstUser.setListData(userArray);
		serverFrame.serverInfoPanel.txtNumber.setText(userArray.length + "");
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

	public void send(TransferInfo tfi) {
		//发送给私聊的接收者，快捷键直接决定了你学Java的速度
		String reciver = tfi.getReciver();
		String sender = tfi.getSender();
		//根据reivcer拿到Socket管道
		//通过用户名为键，管道为值取做map
		Socket socket1 = ChatServer.userSocketMap.get(reciver);
		IOStream.writeMessage(socket1 , tfi);
		
		Socket socket2 = ChatServer.userSocketMap.get(sender);
		IOStream.writeMessage(socket2 , tfi);
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
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
