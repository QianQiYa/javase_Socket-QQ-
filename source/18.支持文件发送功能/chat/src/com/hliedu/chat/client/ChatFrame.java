package com.hliedu.chat.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.hliedu.chat.entity.ChatStatus;
import com.hliedu.chat.entity.FontStyle;
import com.hliedu.chat.entity.TransferInfo;
import com.hliedu.chat.entity.User;
import com.hliedu.chat.io.IOStream;
import com.hliedu.chat.ulist.ImageCellRenderer;
import com.hliedu.chat.ulist.ImageListModel;
import com.hliedu.chat.util.FontSupport;

/**
 * 聊天主界面
 * 
 * 带你轻松学Java：恒骊学堂
 * www.hliedu.com
 * QQ群：107184365
 *
 */
public class ChatFrame extends JFrame {
	private static final long serialVersionUID = -8945833334986986964L;

	public static final Integer FRAME_WIDTH = 750;
	public static final Integer FRAME_HEIGHT = 600;
	
	//接收框
	public JTextPane acceptPane;
	
	//当前在线用户列表
	public JList lstUser;
	
	String userName;
	
	Socket socket;
	
	ChatFrame chatFrame;
	
	//Java中变量作用域
	JComboBox reciverBox;
	
	//字体
	JComboBox fontFamilyCmb;
	
	public ChatFrame(String userName,Socket socket) {
		
		chatFrame = this;
		
		this.socket = socket;
		this.userName = userName;
		
		this.setTitle("聊天室主界面");
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		//窗体不可扩大
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//获取屏幕
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		//屏幕居中处理
		setLocation((width-FRAME_WIDTH)/2, (height-FRAME_HEIGHT)/2);
		
		//加载窗体的背景图片
		ImageIcon imageIcon = new ImageIcon("src/image/beijing.jpg");
		//创建一个标签并将图片添加进去
		JLabel frameBg = new JLabel(imageIcon);
		//设置图片的位置和大小
		frameBg.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		this.add(frameBg);
		
		// 接收框
		acceptPane = new JTextPane();
		acceptPane.setOpaque(false);//设置透明
		acceptPane.setFont(new Font("宋体", 0, 16));
		
		// 设置接收框滚动条
		JScrollPane scoPaneOne = new JScrollPane(acceptPane);
		scoPaneOne.setBounds(15, 20, 500, 332);
		//设置背景透明
		scoPaneOne.setOpaque(false);
		scoPaneOne.getViewport().setOpaque(false);
		frameBg.add(scoPaneOne);


		//当前在线用户列表
		lstUser = new JList();
		lstUser.setFont(new Font("宋体", 0, 14));
		lstUser.setVisibleRowCount(17);
		lstUser.setFixedCellWidth(180);
		lstUser.setFixedCellHeight(60);
		
		
		//声明菜单
		JPopupMenu popupMenu = new JPopupMenu();
		
		//私聊按钮（菜单项）
		JMenuItem privateChat = new JMenuItem("私聊");
		privateChat.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//做一系列的动作，那这个动作包含了什么呢？
				//伪代码，重要的是逻辑能力，业务逻辑
				//告诉发送消息的接收人是谁？
				//
				Object reciverObj = lstUser.getSelectedValue();
				if(reciverObj instanceof User) {
					User user = (User)reciverObj;
					String reciver = user.getUserName();
					reciverBox.removeAllItems();
					reciverBox.addItem("All");
					reciverBox.addItem(reciver);
					reciverBox.setSelectedItem(reciver);
				}
				
			}
		});
		popupMenu.add(privateChat);
		
		
		//黑名单按钮（菜单项）
		JMenuItem blackList = new JMenuItem("黑名单");
		blackList.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		popupMenu.add(blackList);
		
		
		//添加点击事件，（需要确认是右键）
		lstUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				//监听是左键还是邮件
				if(e.isMetaDown()) {
					if(lstUser.getSelectedIndex() >= 0) {
						//弹出菜单,JavaScript JS
						popupMenu.show(lstUser , e.getX() , e.getY());
					}
				}
			}
		});
		
		
		JScrollPane spUser = new JScrollPane(lstUser);
		spUser.setFont(new Font("宋体", 0, 14));
		spUser.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		spUser.setBounds(530, 17, 200, 507);
		frameBg.add(spUser);
		
		
		// 输入框
		JTextPane sendPane = new JTextPane();
		sendPane.setOpaque(false);
		sendPane.setFont(new Font("宋体", 0, 16));
		
		JScrollPane scoPane = new JScrollPane(sendPane);// 设置滚动条
		scoPane.setBounds(15, 400, 500, 122);
		scoPane.setOpaque(false);
		scoPane.getViewport().setOpaque(false);
		frameBg.add(scoPane);
		
		// 添加表情选择
		JLabel lblface = new JLabel(new ImageIcon("src/image/face.png"));
		lblface.setBounds(14, 363, 25, 25);
		lblface.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FaceFrame face = new FaceFrame(sendPane);
			}
		});
		frameBg.add(lblface);
		
		// 添加抖动效果
		JLabel lbldoudong = new JLabel(new ImageIcon("src/image/doudong.png"));
		lbldoudong.setBounds(43, 363, 25, 25);
		lbldoudong.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				//抖动功能实现
				TransferInfo tfi = new TransferInfo();
				tfi.setStatusEnum(ChatStatus.DD);
				tfi.setSender(userName);
				String reciver = "All";
				//我们要获取到，当前要发送给谁？
				Object reciverObj = reciverBox.getSelectedItem();
				if(reciverObj != null) {
					reciver = String.valueOf(reciverObj);
				}
				//接收人
				tfi.setReciver(reciver);
				IOStream.writeMessage(socket, tfi);
			}
			
		});
		frameBg.add(lbldoudong);
		
		// 设置字体选择
		JLabel lblfontChoose = new JLabel(new ImageIcon("src/image/ziti.png"));
		lblfontChoose.setBounds(44, 363, 80, 25);
		lblfontChoose.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				JColorChooser colorChooser = new JColorChooser();
				Color color = colorChooser.showDialog(ChatFrame.this, "字体颜色", Color.BLACK);
				//字体改变
				FontSupport.setFont(sendPane, color, fontFamilyCmb.getSelectedItem().toString(), Font.BOLD, 60);
			}
			
		});
		frameBg.add(lblfontChoose);
		
		//字体下拉选项
		fontFamilyCmb = new JComboBox();
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		String[] str = graphicsEnvironment.getAvailableFontFamilyNames();
		for (String string : str) {
			fontFamilyCmb.addItem(string);
		}
		fontFamilyCmb.setSelectedItem("楷体");
		fontFamilyCmb.setBounds(104, 363, 150, 25);
		frameBg.add(fontFamilyCmb);
		
		//label标签
		JLabel reciverLabel = new JLabel("聊天对象");
		reciverLabel.setBounds(304, 363, 80, 25);
		frameBg.add(reciverLabel);

		//下拉选择框
		reciverBox = new JComboBox();
		reciverBox.setSelectedItem("All");
		reciverBox.addItem("All");
		reciverBox.setBounds(374, 363, 150, 25);
		frameBg.add(reciverBox);
		
		/*
		 * 发送按钮
		 */
		JButton send = new JButton("发 送");
		send.setBounds(15, 533, 125, 25);
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TransferInfo tif = new TransferInfo();
				//包装了所有文字对应的属性
				List<FontStyle> fontStyles = FontSupport.fontEncode(sendPane);
				tif.setContent(fontStyles);
				//发送人
				tif.setSender(userName);
				String reciver = "All";
				//我们要获取到，当前要发送给谁？
				Object reciverObj = reciverBox.getSelectedItem();
				if(reciverObj != null) {
					reciver = String.valueOf(reciverObj);
				}
				//接收人
				tif.setReciver(reciver);
				//本次处理的消息类型为登录
				tif.setStatusEnum(ChatStatus.CHAT);
				IOStream.writeMessage(socket, tif);
				sendPane.setText("");
			}
		});
		frameBg.add(send);
		
		JButton fileBtn = new JButton("传文件");
		fileBtn.setBounds(205, 533, 125, 25);
		fileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String reciver = "All";
				//我们要获取到，当前要发送给谁？
				Object reciverObj = reciverBox.getSelectedItem();
				if(reciverObj != null) {
					reciver = String.valueOf(reciverObj);
					//文件发送框
					FileSendFrame fileSendFrame = new FileSendFrame(socket , reciver , userName);
				}else {
					JOptionPane.showMessageDialog(null, "请右键私聊某一用户后使用该功能");
				}
				
			}
		});
		frameBg.add(fileBtn);
		//客户端关闭窗体退出
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					System.out.println(userName + "窗口关闭");
					TransferInfo tfi = new TransferInfo();
					tfi.setStatusEnum(ChatStatus.QUIT);
					tfi.setUserName(userName);
					tfi.setNotice(userName + "已离开聊天室.....");
					IOStream.writeMessage(socket, tfi);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		setVisible(true);
	}

}
