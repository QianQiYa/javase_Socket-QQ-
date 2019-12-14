package com.hliedu.chat.client;

import java.net.Socket;

import javax.swing.JOptionPane;

import com.hliedu.chat.entity.TransferInfo;
import com.hliedu.chat.io.IOStream;

/**
 * 客户端开辟的去消息的线程
 * 
 * 带你轻松学Java：恒骊学堂
 * www.hliedu.com
 * QQ群：107184365
 *
 */
public class ClientHandler extends Thread{

	Socket socket;
	LoginFrame loginFrame;
	
	public ClientHandler(Socket socket , LoginFrame loginFrame) {
		this.socket = socket;
		this.loginFrame = loginFrame;
	}
	
	@Override
	public void run() {
		//默认重复拿
		while(true) {
			try {
				//模拟一直拿消息，产生阻塞
				Object obj = IOStream.readMessage(socket);
				if(obj instanceof TransferInfo) {
					TransferInfo tfi = (TransferInfo)obj;
					loginResult(tfi);
					
				}
				System.out.println("客户端" +obj);
				if("登录成功".equals(obj)) {
					System.out.println("客户端收到登录成功的消息");
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 登录结果的处理
	 * @param tfi
	 */
	public void loginResult(TransferInfo tfi) {
		if(tfi.getLoginSucceessFlag()) {
			//登录成功，打开主界面
			ChatFrame chatFrame = new ChatFrame();
			loginFrame.dispose();//关闭窗体
		}else {
			//登录失败
			System.out.println("客户端接收到登录失败");
		}
	}
}
