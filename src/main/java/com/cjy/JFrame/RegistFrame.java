package com.cjy.JFrame;

import com.cjy.PO.RegisterUser;
import com.cjy.client.RegistClient;
import com.cjy.util.WindowUtil;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class RegistFrame extends BaseFrame {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel mainJPanel;
	private JLabel usernameJLabel,passwordJLabel,okPasswordJLable;
	private JTextField usernameTextField;
	private JPasswordField passwordField,okPasswordField;
	private JButton okButton,CancelButton;
	
	
	public RegistFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(420, 300);
		setTitle("注册");
		setResizable(false);
		initComponent();
		setLocation();
		WindowUtil.centreWindow(this);
		initListener();
		setVisible(true);
	}

	@Override
	public void initComponent() {
		mainJPanel = new JPanel();
		mainJPanel.setLayout(null);
		this.setContentPane(mainJPanel);
		
		usernameJLabel = new JLabel("用户名：");
		passwordJLabel = new JLabel("密码：");
		okPasswordJLable = new JLabel("确认密码：");
		
		usernameTextField = new JTextField(20);
		passwordField = new JPasswordField(20);
		okPasswordField = new JPasswordField(20);
		
		
		okButton = new JButton("确定");
		CancelButton = new JButton("取消");
				
		mainJPanel.add(usernameJLabel);
		mainJPanel.add(passwordJLabel);
		mainJPanel.add(okPasswordJLable);
		mainJPanel.add(usernameTextField);
		mainJPanel.add(passwordField);
		mainJPanel.add(okPasswordField);
		mainJPanel.add(okButton);
		mainJPanel.add(CancelButton);
		
	}

	@Override
	public void setLocation() {
		usernameJLabel.setBounds(50, 50, 100, 20);
		passwordJLabel.setBounds(50, 100, 100, 20);
		okPasswordJLable.setBounds(50, 150, 100, 20);
		
		usernameTextField.setBounds(170, 50, 200, 20);
		passwordField.setBounds(170, 100, 200, 20);
		okPasswordField.setBounds(170, 150, 200, 20);
		
		okButton.setBounds(50, 220, 100, 20);
		CancelButton.setBounds(270, 220, 100, 20);
	}

	@Override
	public void initListener() {

		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String username = usernameTextField.getText();
				if("".equals(username)) {
					Error(RegistFrame.this,"请输入用户名");
					return ;
				}

				final String password = passwordField.getText();
				String okPasswrod = okPasswordField.getText();

				if("".equals(password) || "".equals(okPasswrod)) {
					Error(RegistFrame.this,"密码或确认密码不能为空");
					passwordField.setText("");
					okPasswordField.setText("");
					return ;
				}

				if(!password.equals(okPasswrod)) {
					Error(RegistFrame.this,"两次密码不同");
					passwordField.setText("");
					okPasswordField.setText("");
					return ;
				}
				final RegistClient registClient = new RegistClient("localhost",9000,RegistFrame.this);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try{
							registClient.setMessage(new RegisterUser(username,password));
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}).start();


			}
		});

		CancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				passwordField.setText("");
				okPasswordField.setText("");
			}
		});
		
	}

	public void callback(String msg) {
		Alert(RegistFrame.this,msg);
		if("注册成功".equals(msg)) {
			this.setVisible(false);
			new LoginFrame();
		}
	}
	
	public static void main(String[] args) {
		new RegistFrame();
	}

}
