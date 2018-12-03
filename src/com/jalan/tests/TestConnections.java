package com.jalan.tests;

import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import com.jalan.cksock.SockConfig;
import com.jalan.cksock.SockServerService;
import com.jalan.cksock.SockService;

import com.jalan.cksock.SockLogger;

public class TestConnections {

	public static void main(String [] args) throws IOException, InterruptedException{
		LogManager.getRootLogger().setLevel(Level.OFF);
		SockLogger.autoConfigure();
		int cantidad = 6000;
		
		SockServerService sss = new SockServerService(new SockConfig(951));
		sss.listen();
		
		
		sss.getClientMessagesObserver().subscribe((message) -> {
			System.out.println(message);
		});
		
		for(int x = 0 ; x < cantidad ; x++) {
			SockService ss = new SockService(new SockConfig("127.0.0.1", 951));
			ss.getConnectionObserver().filter(conn -> conn.status == SockService.CONNECTED_STATUS).subscribe((conn) -> {
				new Thread(new Task(conn.service)).start();
			});
		}
			
		
			
		Thread.sleep(2000);
		System.out.println(" Count:  "  + sss.getClients().size() + ": " + sss.getClients());
	}
	
}
