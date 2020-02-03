package com.jalan.tests;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import com.jalan.cksock.SockConfig;
import com.jalan.cksock.SockLogger;
import com.jalan.cksock.SockServerService;
import com.jalan.cksock.SockService;

public class MgmtConn {
	
	private static SockServerService sss;

	public static void main(String []args) throws Exception{
		LogManager.getRootLogger().setLevel(Level.DEBUG);
		SockLogger.autoConfigure();
		sss = new SockServerService(new SockConfig(951));
		
		sss.listen();
		
		System.out.println("clients in storage: " + sss.getActiveClients());
		
		sss.getClientConnectionObserver()
			.subscribe((event) -> {
				System.out.println("\""+ event.status +"\", client count: " + sss.getActiveClients().size());
				
		});
		
		for(int x = 0 ; x < 150 ; x++) {
			spawnConn();
		}
		
		
		//spawnConn();
		//spawnConn();		
	}
	
	public static void spawnConn() throws Exception{
		SockService ss = new SockService(new SockConfig("127.0.0.1", 951));
		
		ss.getConnectionObserver().subscribe((evt) -> {
			System.out.println("\""+ evt.status +"\" from ss");
		});
		
		Thread.sleep(500);
		
		new Thread(() -> {
			int clients = sss.getClients().size();
			clients *= 1000;
			
			try {
				Thread.sleep(clients);
				//Thread.sleep(500);
				ss.close();
				
				
				System.out.println("clients in storage: " + sss.getActiveClients().size());
			}catch(Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
	
}
