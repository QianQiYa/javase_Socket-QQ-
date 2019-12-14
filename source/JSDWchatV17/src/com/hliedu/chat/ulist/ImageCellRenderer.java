package com.hliedu.chat.ulist;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.SwingConstants;

import com.hliedu.chat.entity.User;

/**
 * DefaultListCellRenderer是JList的渲染器，它继承了JLabel
 * 所以本类可以看成一个JLabel
 * 
 * 带你轻松学Java：恒骊学堂
 * www.hliedu.com
 * QQ群：107184365
 *
 */
public class ImageCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 6686996369807205937L;

	/**
	 * list：JList对象
	 * value：重点，它就是模型数据
	 * index：当前选择的单元格下标，从0开始
	 * isSelected：单元格选中的状态，你选择A之后再选择A，它返回false
	 */
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, 
			int index, boolean isSelected, boolean cellHasFocus) {
		
		if(value instanceof User) {
			User user = (User)value;
			String uiconPath = user.getUiconPath();
			String userName = user.getUserName();
			String motto = user.getMotto();
			ImageIcon icon = new ImageIcon(uiconPath);
			icon.setImage(icon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
			setIcon(icon);
			String text = "<html><body><span color='gray' style='font-size:15px;'>" + userName + "</span><br/>" + motto + "</body></html>";
			//这个setTtext方法支持html语句
			setText(text);
			setForeground(Color.BLUE);
//			setBackground(bg);//设置背景
			setVerticalTextPosition(SwingConstants.TOP);
			setHorizontalTextPosition(SwingConstants.RIGHT);
		}
		return this;
	}
}
