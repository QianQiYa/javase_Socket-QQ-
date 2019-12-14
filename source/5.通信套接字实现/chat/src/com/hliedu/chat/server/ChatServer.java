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
			//等待连接，阻塞实现，会得到一个客户端的连接
			Socket socket = sso.accept();
			InputStream is = socket.getInputStream();
			int len = 0;
			byte []  buf = new byte[1024];
			while((len = is.read(buf)) != -1) {
				
			}
			System.out.println(new String(buf));
//			OutputStream os = socket.getOutputStream();
//			os.write("服务器端接收到连接，你能听到吗".getBytes());
//			os.close();
//			System.out.println("服务器接受到客户端的连接：" + socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new ChatServer();
	}
}
