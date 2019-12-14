
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务端应用程序
 * @author Administrator
 * 
 */
public class Server {
	//运行在服务端的Socket
	private ServerSocket server;
	//线程池，用于管理客户端连接的交互线程
	private ExecutorService threadPool;
	//保存所有客户端输出流的集合
	private List<PrintWriter> allOut;
	/**
	 * 构造方法，用于初始化服务端
	 * @throws IOException 
	 */
	public Server() throws IOException{
		try {
			/*
			 * 创建ServerSocket时需要指定服务端口
			 */
			System.out.println("初始化服务端");
			server = new ServerSocket(8088);
			//初始化线程池
			threadPool = 
				Executors.newFixedThreadPool(50);
			
			//初始化存放所有客户端输出流的集合
			allOut = new ArrayList<PrintWriter>();
			
			System.out.println("服务端初始化完毕");
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 服务端开始工作的方法
	 */
	public void start(){
		try{
			/*
			 * ServerSocket的accept方法
			 * 用于监听8088端口，等待客户端的连接
			 * 该方法是一个阻塞方法，直到一个
			 * 客户端连接，否则该方法一直阻塞。
			 * 若一个客户端连接了，会返回该客户端的
			 * Socket
			 */
			while(true){
				System.out.println("等待客户端连接...");
				Socket socket = server.accept();
				/*
				 * 当一个客户端连接后，启动一个线程
				 * ClientHandler，将该客户端的
				 * Socket传入，使得该线程处理与该
				 * 客户端的交互。
				 * 这样，我们能再次进入循环，接收
				 * 下一个客户端的连接了。
				 */
				Runnable handler
					= new ClientHandler(socket);
//				Thread t = new Thread(handler);
//				t.start();
				/*
				 * 使用线程池分配空闲线程来处理
				 * 当前连接的客户端
				 */
				threadPool.execute(handler);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 将给定的输出流存入共享集合
	 * @param pw
	 */
	public synchronized void addOut(PrintWriter pw){
		allOut.add(pw);
	}
	/**
	 * 将给定的输出流从共享集合中删除
	 * @param pw
	 */
	public synchronized void removeOut(PrintWriter pw){
		allOut.remove(pw);
	}
	/**
	 * 将给定的消息转发给所有客户端
	 * @param message
	 */
	public synchronized void sendMessage(String message){
		for(PrintWriter pw : allOut){
			pw.println(message);
		}
	}
	
	
	public static void main(String[] args){
		Server server;
		try {
			server = new Server();
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("服务端初始化失败");
		}	
	}
	
	/**
	 * 服务端中的一个线程，用于与某个客户端
	 * 交互。
	 * 使用线程的目的是使得服务端可以处理多
	 * 客户端了。
	 * @author Administrator
	 *
	 */
	class ClientHandler implements Runnable{
		//当前线程处理的客户端的Socket
		private Socket socket;
		//当前客户端的IP
		private String ip;
		//当前客户端的昵称
		private String nickName;
		
		/**
		 * 根据给定的客户端的Socket，创建
		 * 线程体
		 * @param socket
		 */
		public ClientHandler(Socket socket){
			this.socket = socket;
			/*
			 * 通过socket获取远端的地址信息
			 * 对于服务端而言，远端就是客户端了
			 */
			InetAddress address 
						= socket.getInetAddress();
			//获取远端计算机的IP地址
			ip = address.getHostAddress();
//		address.getCanonicalHostName()
			//获取客户端的端口号
			int port = socket.getPort();
			System.out.println(
							ip+":"+port+" 客户端连接了");
			
			//改为了使用昵称了，所以不在这里通知了
//			//通知其他用户，该用户上线了
//			sendMessage("["+ip+"]上线了");
		}
		/**
		 * 该线程会将当前Socket中的输入流获取
		 * 用来循环读取客户端发送过来的消息。
		 */
		public void run() {
			/*
			 * 定义在try语句外的目的是，为了在
			 * finally中也可以引用到
			 */
			PrintWriter pw = null;
			try{
				/*
				 * 为了让服务端与客户端发送信息，
				 * 我们需要通过socket获取输出流。
				 */
				OutputStream out
					= socket.getOutputStream();
				//转换为字符流，用于指定编码集
				OutputStreamWriter osw
					= new OutputStreamWriter(
												out,"UTF-8");
				//创建缓冲字符输出流
				pw = new PrintWriter(osw,true);
				
				/*
				 * 将该客户端的输出流存入共享集合
				 * 以便使得该客户端也能接收服务端
				 * 转发的消息
				 */
//				allOut.add(pw);
				addOut(pw);
				//输出当前在线人数
				System.out.println(
								"当前在线人数为:"+
								allOut.size());
				
				/*
				 * 通过刚刚连上的客户端的Socket获取
				 * 输入流，来读取客户端发送过来的信息
				 */
				InputStream in 
					=	socket.getInputStream();
				/*
				 * 将字节输入流包装为字符输出流，这样
				 * 可以指定编码集来读取每一个字符
				 */
				InputStreamReader isr
					= new InputStreamReader(
												in,"UTF-8");
				/*
				 * 将字符流转换为缓冲字符输入流
				 * 这样就可以以行为单位读取字符串了 
				 */
				BufferedReader br
					= new BufferedReader(isr);
				
				/*
				 * 当创建好当前客户端的输入流后
				 * 读取的第一个字符串，应当是昵称
				 */
				nickName = br.readLine();
				//通知所有客户端，当前用户上线了
				sendMessage("["+nickName+"]上线了");
				
				
				String message = null;
				//读取客户端发送过来的一行字符串
				/*
				 * 读取客户端发送过来的信息这里
				 */
				while((message = br.readLine())!=null){
//					System.out.println(
//							"客户端说:" + message);
//					pw.println(message);
					/*
					 * 当读取到客户端发送过来的一条
					 * 消息后，将该消息转发给所有
					 * 客户端
					 */
//					for(PrintWriter o : allOut){
//						o.println(message);
//					}
					sendMessage(nickName+"说:"+message);
				}
							
			}catch(Exception e){
				//在Windows中的客户端，
				//报错通常是因为客户端断开了连接
				
			}finally{
				/*
				 * 首先将该客户端的输出流从共享
				 * 集合中删除。
				 */
//				allOut.remove(pw);
				removeOut(pw);
				//输出当前在线人数
				System.out.println(
						"当前在线人数为:"+
						allOut.size());
				
				//通知其他用户，该用户下线了
				sendMessage("["+nickName+"]下线了");
				
				
				/*
				 * 无论是linux用户还是windows
				 * 用户，当与服务端断开连接后
				 * 我们都应该在服务端也与客户端
				 * 断开连接
				 */
				try {
					socket.close();
				} catch (IOException e) {
				}
				System.out.println(
							"一个客户端下线了...");
			}
			
		}
		
	}
	
}






