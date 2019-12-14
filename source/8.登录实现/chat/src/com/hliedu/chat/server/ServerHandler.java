package com.hliedu.chat.server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

import com.hliedu.chat.entity.TransferInfo;
import com.hliedu.chat.io.IOStream;

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
	
	public ServerHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
		//默认重复拿
		while(true) {
			System.out.println("服务器端的循环");
			try {
				//模拟一直拿消息，产生阻塞
				Object obj = IOStream.readMessage(socket);
				System.out.println("服务端：" + obj);
				if(obj instanceof TransferInfo) {
					TransferInfo tfi = (TransferInfo)obj;
					boolean flag = checkUserLogin(tfi);
					tfi.setLoginSucceessFlag(false);
					if(flag) {
						//返回登录成功给客户端
						tfi.setLoginSucceessFlag(true);
						IOStream.writeMessage(socket , tfi);
					}else {
						System.out.println("登录失败");
						//返回登录失败给客户端
						IOStream.writeMessage(socket , tfi);
					}
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
