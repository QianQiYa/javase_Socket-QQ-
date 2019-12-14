package com.hliedu.chat.ulist;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import com.hliedu.chat.entity.User;

/**
 * JList的模型类，构成JList的模型元素
 * 这个模型类，最终是提供给某一个类去调用
 * @author Ron
 *
 */
public class ImageListModel extends AbstractListModel<User>{

	private static final long serialVersionUID = -8573230159537206489L;
	
	//为了配合某一个类去调用，所以我们得返回相应的类型
	private List<User> list = new ArrayList<User>();
	
	//是我们自己提供的
	public void addElement(User str) {
		list.add(str);
	}
	
	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public User getElementAt(int index) {
		return list.get(index);
	}

}
