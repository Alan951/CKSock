package com.jalan.tests;

import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import com.jalan.cksock.SockConfig;
import com.jalan.cksock.SockLogger;
import com.jalan.cksock.SockServerService;
import com.jalan.cksock.SockService;

public class TestConnCycle {

	public static void main(String [] args) throws IOException, InterruptedException{
		LogManager.getRootLogger().setLevel(Level.DEBUG);
		SockLogger.autoConfigure();
		
		SockServerService sss = new SockServerService(new SockConfig(951));
		sss.listen();
		
		sss.getClientMessagesObserver().subscribe((message) -> {
			System.out.println(message);
		});
		
		
		SockService ss = new SockService(new SockConfig("127.0.0.1", 951));
		
		
		sss.getClientConnectionObserver().subscribe((evt) -> {
			if(evt.status.equals(SockService.CONNECTED_STATUS)) {
				sss.stop();
			}
		});
	}
	
}
