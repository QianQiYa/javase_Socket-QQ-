

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * �ͻ���Ӧ�ó���
 * @author Administrator
 *
 */
public class Client {
	//Socket���������ӷ���˵�ServerSocket
	private Socket socket;
	/**
	 * �ͻ��˹��췽�������ڳ�ʼ���ͻ���
	 * @throws Exception 
	 */
	public Client() throws Exception{	
		try {
			/*
			 * ����Socket����ʱ���ͻ᳢�Ը���
			 * �����ĵ�ַ��˿����ӷ���ˡ�
			 * ���ԣ����ö��󴴽��ɹ���˵����
			 * ���������������
			 * 
			 *
			 * 
			 */
			System.out.println("�������ӷ����...");
			socket = new Socket(
									"localhost",8088);
			System.out.println("�ɹ����ӷ����");
		} catch (Exception e) {
			throw e;
		}	
	}
	/**
	 * �ͻ�����������
	 */
	public void start(){
		try{
			//�����������̣߳������շ���˵���Ϣ
			Runnable runn = 
				new GetServerInfoHandler();
			Thread t = new Thread(runn);
			t.start();
			
			
			/*
			 * ����ͨ��Socket��getOutputStream()
			 * ������ȡһ������������ڽ���Ϣ����
			 * �������
			 */
			OutputStream out = 
							socket.getOutputStream();
			/*
			 * ʹ���ַ���������ָ���ı��뼯���ַ���
			 * ת��Ϊ�ֽں���ͨ��out���͸������
			 */
			OutputStreamWriter osw
				= new OutputStreamWriter(
													out,"UTF-8");
			/*
			 * ���ַ�����װΪ�����ַ������Ϳ���
			 * ����Ϊ��λд���ַ�����
			 */
			PrintWriter pw 
				= new PrintWriter(osw,true);
			
			
			
			/*
			 * ����һ��Scanner�����ڽ����û�
			 * ������ַ���
			 */
			Scanner scanner 
						= new Scanner(System.in);
			//�����ӭ����
			System.out.println(
								"��ӭ���������������");
			while(true){
				/*
				 * ���������ǳ�
				 */
				System.out.println("�������ǳ�:");
				String nickname = scanner.nextLine();
				if(nickname.trim().length()>0){
					pw.println(nickname);
					break;
				}
				System.out.println("�ǳƲ����ǿ�");
			}
			
			while(true){
				String str = scanner.nextLine();
				pw.println(str);
			}	
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		try{
			Client client = new Client();
			client.start();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("�ͻ��˳�ʼ��ʧ��");
		}
	}
	/**
	 * ���̵߳�������ѭ�����շ���˷��͹���
	 * ����Ϣ�������������̨
	 * @author Administrator
	 */
	class GetServerInfoHandler 
								implements Runnable{
		public void run() {
			try{
				/*
				 * ͨ��Socket��ȡ������
				 */
				InputStream in
					= socket.getInputStream();
				//��������ת��Ϊ�ַ���������ָ������
				InputStreamReader isr
					= new InputStreamReader(
												in,"UTF-8");
				//���ַ�������ת��Ϊ������
				BufferedReader br
					= new BufferedReader(isr);
				
				String message = null;
				//ѭ����ȡ����˷��͵�ÿһ���ַ���
				while((message=br.readLine())!=null){
					//������˷��͵��ַ������������̨
					System.out.println(message);
				}		
			}catch(Exception e){
				
			}
		}
		
	} 
	
}


