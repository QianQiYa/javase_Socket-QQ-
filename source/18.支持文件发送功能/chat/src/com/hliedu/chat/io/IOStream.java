package com.hliedu.chat.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * IO流工具类
 * 
 * 带你轻松学Java：恒骊学堂
 * www.hliedu.com
 * QQ群：107184365
 *
 */
public class IOStream {

	/**
	 * 从Socket中读取对象
	 * @param socket
	 * @return
	 */
	public static Object readMessage(Socket socket) {
		Object obj = null;
		try {
			InputStream is = socket.getInputStream();
			
			//我们需要判断，读取出来的数据是什么类型的，对象、文件
			//文件是二进制，byte[]数组来做
			DataInputStream dis = new DataInputStream(is);
			//对象数据所占的字节数
			int objByteLen = dis.readInt();
			byte[] objByte = new byte[objByteLen];
			dis.readFully(objByte);
			obj = ByteObjectConvert.byteToObject(objByte);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 开发迭代的过程中，你不要直接去修改已有的方法，违反了面向对象中开闭原则
	 * 对修改关闭，对扩展开放
	 * 
	 * 根据Socket管道写出消息
	 * @param socket
	 */
	public static void writeMessage(Socket socket , Object obj) {
		try {
			OutputStream os = socket.getOutputStream();
			//需要把Object对象替换成byte数组
			byte[] objByte = ByteObjectConvert.objectToByte(obj);
			
			DataOutputStream dos = new DataOutputStream(os);
			//写出一个对象大小
			dos.writeInt(objByte.length);
			//写出一个对象
			dos.write(objByte);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}