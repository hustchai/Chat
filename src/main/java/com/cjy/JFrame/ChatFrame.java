package com.cjy.JFrame;

import com.cjy.PO.Message;
import com.cjy.client.ChatClient;
import com.cjy.util.WindowUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by jychai on 16/7/23.
 */
public class ChatFrame extends BaseFrame {

    private JPanel mainJpanel;
    private JScrollPane showJpanel;
    private JPanel inputJpanel;
    private JPanel buttomJpanel;
    private Button okButton;
    private JTextArea inputTextArea;
    private JTextArea showTextArea;

    private String username;

    private java.util.List<Message> messages = new LinkedList<Message>();

    ChatClient chatClient = new ChatClient("localhost",9003,ChatFrame.this);



    @Override
    public void initComponent() {
        mainJpanel = new JPanel();
        mainJpanel.setLayout(null);
        this.setContentPane(mainJpanel);

        showJpanel = new JScrollPane();
        showJpanel.setLayout(null);

        inputJpanel = new JPanel();
        inputJpanel.setLayout(null);

        buttomJpanel = new JPanel();
        buttomJpanel.setLayout(null);





        okButton = new Button("发送");
        inputTextArea = new JTextArea();
        showTextArea = new JTextArea();

        showJpanel.add(showTextArea);
        inputJpanel.add(inputTextArea);
        buttomJpanel.add(okButton);

        mainJpanel.add(showJpanel);
        mainJpanel.add(inputJpanel);
        mainJpanel.add(buttomJpanel);


    }

    @Override
    public void setLocation() {
        showJpanel.setBounds(0,0,600,400);
        inputJpanel.setBounds(0,400,600,150);
        buttomJpanel.setBounds(0,550,600,50);

        okButton.setBounds(520,0,80,40);
        inputTextArea.setBounds(0,0,600,145);
        showTextArea.setBounds(0,0,600,400);
    }

    @Override
    public void initListener() {

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = inputTextArea.getText();
                if ("".equals(content)) {
                    Alert(ChatFrame.this, "请输入要发送的内容");
                    return;
                }

                final Message message = new Message();
                message.setUsername(username);
                message.setContent(content);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            chatClient.setMessage(message);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }).start();



            }
        });

    }

    public ChatFrame(String username) {
        this.username = username;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setTitle("正在聊天");
        setResizable(false);
        initComponent();
        setLocation();
        WindowUtil.centreWindow(this);
        initListener();
        setVisible(true);
    }

    public void callBack(Object object) {
        messages.add((Message)object);
        StringBuilder sb = new StringBuilder();
        for(Message message : messages) {
            sb.append(message.getUsername());
            sb.append("\r\n");
            sb.append("\t");
            sb.append(message.getContent());
            sb.append("\r\n");
        }
        showTextArea.setText(sb.toString());

    }

}
