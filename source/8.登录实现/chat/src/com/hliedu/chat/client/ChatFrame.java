package com.hliedu.chat.client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

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

	/**
	 * 服务器窗体宽度
	 */
	public static final Integer FRAME_WIDTH = 750;
	
	/**
	 * 服务器窗体高度
	 */
	public static final Integer FRAME_HEIGHT = 600;
	
	public ChatFrame() {
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
		JTextPane acceptPane = new JTextPane();
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
		JList lstUser = new JList();
		lstUser.setFont(new Font("宋体", 0, 14));
		lstUser.setVisibleRowCount(17);
		lstUser.setFixedCellWidth(180);
		lstUser.setFixedCellHeight(18);
		
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
		frameBg.add(lblface);
		
		// 添加抖动效果
		JLabel lbldoudong = new JLabel(new ImageIcon("src/image/doudong.png"));
		lbldoudong.setBounds(43, 363, 25, 25);
		frameBg.add(lbldoudong);
		
		// 设置字体选择
		JLabel lblfontChoose = new JLabel(new ImageIcon("src/image/ziti.png"));
		lblfontChoose.setBounds(44, 363, 80, 25);
		frameBg.add(lblfontChoose);
		
		//字体下拉选项
		JComboBox fontFamilyCmb = new JComboBox();
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		String[] str = graphicsEnvironment.getAvailableFontFamilyNames();
		for (String string : str) {
			fontFamilyCmb.addItem(string);
		}
		fontFamilyCmb.setSelectedItem("楷体");
		fontFamilyCmb.setBounds(104, 363, 150, 25);
		frameBg.add(fontFamilyCmb);
		
		/*
		 * 发送按钮
		 */
		JButton send = new JButton("发 送");
		send.setBounds(15, 533, 125, 25);
		frameBg.add(send);
		
		setVisible(true);
	}

}
