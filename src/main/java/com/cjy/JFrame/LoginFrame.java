package com.cjy.JFrame;



import com.cjy.PO.RegisterUser;
import com.cjy.client.LoginClient;
import com.cjy.util.WindowUtil;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoginFrame extends BaseFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel mainJpanel;
	private JLabel usernameJLabel,passwordJLable;
	private JTextField usernameJTextField;
	private JPasswordField passwordJTextField;
	private JButton okJButton,cancelJButton;
	private String user;


	@Override
	public void initComponent() {
		mainJpanel = new JPanel();
		mainJpanel.setLayout(null);
		this.setContentPane(mainJpanel);
		
		
		usernameJLabel = new JLabel("用户名:");
		
		passwordJLable = new JLabel("密 码 :");
		
		usernameJTextField = new JTextField(20);
		
		passwordJTextField = new JPasswordField(20);
		
		okJButton = new JButton("登录");
		
		cancelJButton = new JButton("取消");
		
		
		
		mainJpanel.add(usernameJLabel);
		mainJpanel.add(passwordJLable);
		mainJpanel.add(usernameJTextField);
		mainJpanel.add(passwordJTextField);
		mainJpanel.add(okJButton);
		mainJpanel.add(cancelJButton);
		
	}

	@Override
	public void setLocation() {
		usernameJLabel.setBounds(80, 100, 100, 20);
		usernameJTextField.setBounds(160, 100, 200, 20);
		passwordJLable.setBounds(80, 160, 100, 20);
		passwordJTextField.setBounds(160, 160, 200, 20);
		okJButton.setBounds(80, 220, 100, 20);
		cancelJButton.setBounds(260, 220, 100, 20);
	}

	@Override
	public void initListener() {


		okJButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameJTextField.getText();
				if("".equals(username)) {
					Alert(LoginFrame.this,"请输入用户名");
					return ;
				}

				String password = passwordJTextField.getText();

				if("".equals(password)) {
					Alert(LoginFrame.this,"请输入密码");
					return ;
				}

				final RegisterUser registerUser = new RegisterUser(username,password);

				new Thread(
						new Runnable() {
							@Override
							public void run() {
								try {
									LoginClient loginClient = new LoginClient("localhost",9001,LoginFrame.this);
									loginClient.setMessage(registerUser);
									user = registerUser.getUsername();
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						}
				).start();


			}
		});
		
		
		
		
			
		
		
	}
	
	public LoginFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 400);
		setTitle("登录");
		setResizable(false);
		initComponent();
		setLocation();
		WindowUtil.centreWindow(this);
		initListener();
		setVisible(true);
	}


	public void callback(String msg) {
		if("登录成功".equals(msg)) {
			new ChatFrame(user);
			this.setVisible(false);
		} else {
			Alert(LoginFrame.this,msg);
		}

	}
	
	public static void main(String[] args) {
		new LoginFrame();
	}
	

}
