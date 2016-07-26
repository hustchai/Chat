package com.cjy.JFrame;


import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public abstract class BaseFrame extends JFrame {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**警告信息*/
	public void Alert(Component pC,String message) {
		JOptionPane.showMessageDialog(pC,message,"警告",JOptionPane.WARNING_MESSAGE);
	}
	
	/**错误信息*/
	public void Error(Component pC,String message) {
		JOptionPane.showMessageDialog(pC,message,"错误",JOptionPane.ERROR_MESSAGE);
	}
	
	/**初始化组件*/
	public abstract void initComponent();
	
	/**设置组件位置*/
	public abstract void setLocation();
	
	/**初始化监听器*/
	public abstract void initListener();

}
