package com.hliedu.chat.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

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
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		//默认重复拿
		while(true) {
			System.out.println("客户端的循环");
			try {
				//模拟一直拿消息，产生阻塞
				Object obj = IOStream.readMessage(socket);
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
}
