package com.jalan.tests;

import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import com.jalan.cksock.SockConfig;
import com.jalan.cksock.SockLogger;
import com.jalan.cksock.SockServerService;
import com.jalan.cksock.SockService;
import com.sun.javafx.scene.control.behavior.ScrollBarBehavior.ScrollBarKeyBinding;

public class TestConnCycle {

	public static void main(String [] args) throws IOException, InterruptedException{
		LogManager.getRootLogger().setLevel(Level.DEBUG);
		SockLogger.autoConfigure();
		
		SockServerService sss = new SockServerService(new SockConfig(951));
		sss.listen();
		
		sss.getClientMessagesObserver().subscribe((message) -> {
			System.out.println(message);
		});
		
		SockConfig conf = new SockConfig();
		conf.setAddress("127.0.0.1");
		conf.setPort(951);
		conf.setAutoConnect(true);
		conf.setAttemptTimes(-1);
		conf.setConnMode(SockConfig.CLIENT_MODE);
		SockService ss = new SockService(conf);
		
		
		sss.getClientConnectionObserver().subscribe((evt) -> {
			if(evt.status.equals(SockService.CONNECTED_STATUS)) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sss.stop();
			}
		});
	}
	
}
