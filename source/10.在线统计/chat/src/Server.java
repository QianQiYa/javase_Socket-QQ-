
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
 * �����Ӧ�ó���
 * @author Administrator
 * 
 */
public class Server {
	//�����ڷ���˵�Socket
	private ServerSocket server;
	//�̳߳أ����ڹ���ͻ������ӵĽ����߳�
	private ExecutorService threadPool;
	//�������пͻ���������ļ���
	private List<PrintWriter> allOut;
	/**
	 * ���췽�������ڳ�ʼ�������
	 * @throws IOException 
	 */
	public Server() throws IOException{
		try {
			/*
			 * ����ServerSocketʱ��Ҫָ������˿�
			 */
			System.out.println("��ʼ�������");
			server = new ServerSocket(8088);
			//��ʼ���̳߳�
			threadPool = 
				Executors.newFixedThreadPool(50);
			
			//��ʼ��������пͻ���������ļ���
			allOut = new ArrayList<PrintWriter>();
			
			System.out.println("����˳�ʼ�����");
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * ����˿�ʼ�����ķ���
	 */
	public void start(){
		try{
			/*
			 * ServerSocket��accept����
			 * ���ڼ���8088�˿ڣ��ȴ��ͻ��˵�����
			 * �÷�����һ������������ֱ��һ��
			 * �ͻ������ӣ�����÷���һֱ������
			 * ��һ���ͻ��������ˣ��᷵�ظÿͻ��˵�
			 * Socket
			 */
			while(true){
				System.out.println("�ȴ��ͻ�������...");
				Socket socket = server.accept();
				/*
				 * ��һ���ͻ������Ӻ�����һ���߳�
				 * ClientHandler�����ÿͻ��˵�
				 * Socket���룬ʹ�ø��̴߳������
				 * �ͻ��˵Ľ�����
				 * �������������ٴν���ѭ��������
				 * ��һ���ͻ��˵������ˡ�
				 */
				Runnable handler
					= new ClientHandler(socket);
//				Thread t = new Thread(handler);
//				t.start();
				/*
				 * ʹ���̳߳ط�������߳�������
				 * ��ǰ���ӵĿͻ���
				 */
				threadPool.execute(handler);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * ����������������빲����
	 * @param pw
	 */
	public synchronized void addOut(PrintWriter pw){
		allOut.add(pw);
	}
	/**
	 * ��������������ӹ�������ɾ��
	 * @param pw
	 */
	public synchronized void removeOut(PrintWriter pw){
		allOut.remove(pw);
	}
	/**
	 * ����������Ϣת�������пͻ���
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
			System.out.println("����˳�ʼ��ʧ��");
		}	
	}
	
	/**
	 * ������е�һ���̣߳�������ĳ���ͻ���
	 * ������
	 * ʹ���̵߳�Ŀ����ʹ�÷���˿��Դ����
	 * �ͻ����ˡ�
	 * @author Administrator
	 *
	 */
	class ClientHandler implements Runnable{
		//��ǰ�̴߳���Ŀͻ��˵�Socket
		private Socket socket;
		//��ǰ�ͻ��˵�IP
		private String ip;
		//��ǰ�ͻ��˵��ǳ�
		private String nickName;
		
		/**
		 * ���ݸ����Ŀͻ��˵�Socket������
		 * �߳���
		 * @param socket
		 */
		public ClientHandler(Socket socket){
			this.socket = socket;
			/*
			 * ͨ��socket��ȡԶ�˵ĵ�ַ��Ϣ
			 * ���ڷ���˶��ԣ�Զ�˾��ǿͻ�����
			 */
			InetAddress address 
						= socket.getInetAddress();
			//��ȡԶ�˼������IP��ַ
			ip = address.getHostAddress();
//		address.getCanonicalHostName()
			//��ȡ�ͻ��˵Ķ˿ں�
			int port = socket.getPort();
			System.out.println(
							ip+":"+port+" �ͻ���������");
			
			//��Ϊ��ʹ���ǳ��ˣ����Բ�������֪ͨ��
//			//֪ͨ�����û������û�������
//			sendMessage("["+ip+"]������");
		}
		/**
		 * ���̻߳Ὣ��ǰSocket�е���������ȡ
		 * ����ѭ����ȡ�ͻ��˷��͹�������Ϣ��
		 */
		public void run() {
			/*
			 * ������try������Ŀ���ǣ�Ϊ����
			 * finally��Ҳ�������õ�
			 */
			PrintWriter pw = null;
			try{
				/*
				 * Ϊ���÷������ͻ��˷�����Ϣ��
				 * ������Ҫͨ��socket��ȡ�������
				 */
				OutputStream out
					= socket.getOutputStream();
				//ת��Ϊ�ַ���������ָ�����뼯
				OutputStreamWriter osw
					= new OutputStreamWriter(
												out,"UTF-8");
				//���������ַ������
				pw = new PrintWriter(osw,true);
				
				/*
				 * ���ÿͻ��˵���������빲����
				 * �Ա�ʹ�øÿͻ���Ҳ�ܽ��շ����
				 * ת������Ϣ
				 */
//				allOut.add(pw);
				addOut(pw);
				//�����ǰ��������
				System.out.println(
								"��ǰ��������Ϊ:"+
								allOut.size());
				
				/*
				 * ͨ���ո����ϵĿͻ��˵�Socket��ȡ
				 * ������������ȡ�ͻ��˷��͹�������Ϣ
				 */
				InputStream in 
					=	socket.getInputStream();
				/*
				 * ���ֽ���������װΪ�ַ������������
				 * ����ָ�����뼯����ȡÿһ���ַ�
				 */
				InputStreamReader isr
					= new InputStreamReader(
												in,"UTF-8");
				/*
				 * ���ַ���ת��Ϊ�����ַ�������
				 * �����Ϳ�������Ϊ��λ��ȡ�ַ����� 
				 */
				BufferedReader br
					= new BufferedReader(isr);
				
				/*
				 * �������õ�ǰ�ͻ��˵���������
				 * ��ȡ�ĵ�һ���ַ�����Ӧ�����ǳ�
				 */
				nickName = br.readLine();
				//֪ͨ���пͻ��ˣ���ǰ�û�������
				sendMessage("["+nickName+"]������");
				
				
				String message = null;
				//��ȡ�ͻ��˷��͹�����һ���ַ���
				/*
				 * ��ȡ�ͻ��˷��͹�������Ϣ����
				 */
				while((message = br.readLine())!=null){
//					System.out.println(
//							"�ͻ���˵:" + message);
//					pw.println(message);
					/*
					 * ����ȡ���ͻ��˷��͹�����һ��
					 * ��Ϣ�󣬽�����Ϣת��������
					 * �ͻ���
					 */
//					for(PrintWriter o : allOut){
//						o.println(message);
//					}
					sendMessage(nickName+"˵:"+message);
				}
							
			}catch(Exception e){
				//��Windows�еĿͻ��ˣ�
				//����ͨ������Ϊ�ͻ��˶Ͽ�������
				
			}finally{
				/*
				 * ���Ƚ��ÿͻ��˵�������ӹ���
				 * ������ɾ����
				 */
//				allOut.remove(pw);
				removeOut(pw);
				//�����ǰ��������
				System.out.println(
						"��ǰ��������Ϊ:"+
						allOut.size());
				
				//֪ͨ�����û������û�������
				sendMessage("["+nickName+"]������");
				
				
				/*
				 * ������linux�û�����windows
				 * �û����������˶Ͽ����Ӻ�
				 * ���Ƕ�Ӧ���ڷ����Ҳ��ͻ���
				 * �Ͽ�����
				 */
				try {
					socket.close();
				} catch (IOException e) {
				}
				System.out.println(
							"һ���ͻ���������...");
			}
			
		}
		
	}
	
}






