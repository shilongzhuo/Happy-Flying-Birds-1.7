/**
 * ClassName: ServerMain.java
 * Author: qiujy
 * CreateTime: 2009-4-15
 * EMAIL: qjyong@gmail.com
 * Copyright 2009 ++YONG All rights reserved.
 */
package server;

import java.io.IOException;
import java.net.*;
import javax.swing.*;
import server.controller.RequestProcessor;
import server.ui.ServerInfoFrame;

/** 服务器入口程序 */
public class ServerMain {
	public static void main(String[] args) {
		int port = Integer.parseInt(DataBuffer.configProp.getProperty("port"));
		//初始化服务器套节字
		try {
			DataBuffer.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		new Thread(new Runnable() {//启动新线程进行客户端连接监听
			public void run() {
				try {
					while (true) {
						// 监听客户端的连接
						Socket socket = DataBuffer.serverSocket.accept();
						System.out.println("客户来了：" 
								+ socket.getInetAddress().getHostAddress()
								+ ":" + socket.getPort());
						
						//针对每个客户端启动一个线程，在线程中调用请求处理器来处理每个客户端的请求
						new Thread(new RequestProcessor(socket)).start();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		//设置外观感觉
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//启动服务器监控窗体
		new ServerInfoFrame(); 
	}
}