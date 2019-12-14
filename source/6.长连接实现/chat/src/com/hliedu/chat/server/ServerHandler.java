package com.hliedu.chat.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

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
				InputStream is = socket.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				Object obj = ois.readObject();
				System.out.println("服务端：" + obj);
				Thread.sleep(1000);
				if("zs".equals(obj)) {
					OutputStream os = socket.getOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(os);
					oos.writeObject("登录成功");
					oos.flush();
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
