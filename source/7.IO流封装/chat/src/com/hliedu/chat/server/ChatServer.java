package com.hliedu.chat.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器启动入口
 * 
 * 带你轻松学Java：恒骊学堂
 * www.hliedu.com
 * QQ群：107184365
 *
 */
public class ChatServer {

	public ChatServer() {
		try {
			//建立服务器的Socket监听
			ServerSocket sso = new ServerSocket(8888);
			
			//循环是为了解决多客户端使用
			while(true) {
				//等待连接，阻塞实现，会得到一个客户端的连接
				Socket socket = sso.accept();
				ServerHandler serverHandler = new ServerHandler(socket);
				serverHandler.start();
				System.out.println("服务器接受到客户端的连接：" + socket);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new ChatServer();
	}
}
