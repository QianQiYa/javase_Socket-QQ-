package com.hliedu.chat.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.hliedu.chat.entity.ChatStatus;
import com.hliedu.chat.entity.TransferInfo;
import com.hliedu.chat.io.IOStream;

public class FileSendFrame extends JFrame {

    private static final long serialVersionUID = -569174783640213362L;

    public static final Integer FRAME_WIDTH = 450;
    public static final Integer FRAME_HEIGHT = 200;


    FileSendFrame fileFrame;

    public FileSendFrame(Socket socket, String reciver, String sender) {

        fileFrame = this;

        setTitle("文件发送界面");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        //获取屏幕
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        setLocation((width - FRAME_WIDTH) / 2, (height - FRAME_HEIGHT) / 2);
        setLayout(null);

        JLabel fileLabel = new JLabel("文件路径: ");
        fileLabel.setBounds(60, 20, 150, 30);
        fileLabel.setFont(new Font("宋体", 0, 16));
        add(fileLabel);


        //账号文本框
        JTextField filePathField = new JTextField();
        filePathField.setBounds(150, 20, 240, 30);
        add(filePathField);

        JButton enter = new JButton("选择文件");
        enter.setBounds(60, 130, 130, 25);
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                //设置选择文件是，可不可以选择文件夹
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                //打开文件选择窗体
                int state = jfc.showDialog(new JLabel(), "选择文件");

                if (state == JFileChooser.CANCEL_OPTION) {
                    //取消     结束该进程
                    return;
                }
                //获取文件
                File file = jfc.getSelectedFile();
                filePathField.setText(file.getAbsolutePath());
            }
        });
        add(enter);

        //文件发送按钮
        JButton sendFileBtn = new JButton("发送文件");
        sendFileBtn.setBounds(250, 130, 140, 25);
        sendFileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String filePath = filePathField.getText();
                    if (filePath != null && !"".equals(filePath)) {
                        //做文件上传处理
                        //把文件从磁盘读取到内存当中
                        File file = new File(filePath);
                        FileInputStream fis = new FileInputStream(file);
                        //读取文件内容字节数
                        int fileLen = fis.available();
                        byte[] fileData = new byte[fileLen];
                        fis.read(fileData);//此时fileData中的数据就是文件数据
                        fis.close();

                        //把fis弄成byte数组，然后使用byte数组进行文件上传，对内存消耗非常大
                        TransferInfo tfi = new TransferInfo();
                        //发送人
                        tfi.setSender(sender);
                        //接收人
                        tfi.setReciver(reciver);
                        //文件数据
                        tfi.setFileByte(fileData);
                        //文件名
                        tfi.setFileName(file.getName());
                        tfi.setStatusEnum(ChatStatus.SEND_FILE);

                        //发送文件
                        IOStream.writeMessage(socket, tfi);

                        //隐藏当前窗体
                        fileFrame.dispose();
                        JOptionPane.showMessageDialog(null, "文件发送成功，等待对方接收！");
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

            }
        });
        add(sendFileBtn);

//		//滚动进度条
//		JProgressBar progressBar = new JProgressBar();
//		// 设置进度的 最小值 和 最大值
//        progressBar.setMinimum(MIN_PROGRESS);
//        progressBar.setMaximum(MAX_PROGRESS);
//        progressBar.setBounds(60, 80, 330, 25);
//        // 设置当前进度值
//        progressBar.setValue(currentProgress);
//        // 绘制百分比文本（进度条中间显示的百分数）
//        progressBar.setStringPainted(true);
//        // 添加进度改变通知
//        progressBar.addChangeListener(new ChangeListener() {
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                System.out.println("当前进度值: " + progressBar.getValue() + "; " +
//                        "进度百分比: " + progressBar.getPercentComplete());
//            }
//        });
//        add(progressBar);
//        
//        new Timer(500, new ActionListener() {
//	        @Override
//	        public void actionPerformed(ActionEvent e) {
//	            currentProgress++;
//	            if (currentProgress > MAX_PROGRESS) {
//	                currentProgress = MIN_PROGRESS;
//	                
//	            }
//	            progressBar.setValue(currentProgress);
//	            progressBar.setString("传输完成");
//	        }
//	    }).start();

        setVisible(true);
    }


//    private static final int MIN_PROGRESS = 0;
//    private static final int MAX_PROGRESS = 100;
//
//    private static int currentProgress = MIN_PROGRESS;

}
