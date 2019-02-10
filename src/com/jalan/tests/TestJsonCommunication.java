package com.jalan.tests;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import com.jalan.cksock.MessageWrapper;
import com.jalan.cksock.MessageWrapper2;
import com.jalan.cksock.SockConfig;
import com.jalan.cksock.SockLogger;
import com.jalan.cksock.SockServerService;
import com.jalan.cksock.SockService;

public class TestJsonCommunication {

	public static void main(String []args) throws IOException, InterruptedException{
		LogManager.getRootLogger().setLevel(Level.DEBUG);
		SockLogger.autoConfigure();
		
		SockConfig serverConfig = new SockConfig(951);
		serverConfig.setUseJson(true);
		
		SockServerService sss = new SockServerService(serverConfig);
		sss.listen();
		
		
		sss.getClientMessagesObserver().subscribe((message) -> {
			System.out.println(message);
			
			if(message.getPayload() instanceof TaskList) {
				System.out.println("TASK LIST: " + (TaskList)message.getPayload());
			}
		});
		
	
		SockConfig clientConfig = new SockConfig("127.0.0.1", 951);
		clientConfig.setUseJson(true);
		
		SockService ss = new SockService(clientConfig);
		ss.getConnectionObserver().filter(conn -> conn.status == SockService.CONNECTED_STATUS).subscribe((conn) -> {
			MessageWrapper2<String> msg = new MessageWrapper2<String>();
			msg.setPayload("Hola prro!");
			
			ss.sendDataPlz(msg);
			
			MessageWrapper2<TaskList> msg2 = new MessageWrapper2<TaskList>();
			
			TaskList taskList = new TaskList(new ArrayList<String>(Arrays.asList("Hola", "Como", "andamos", "Prro", "del", "mal")));
			
			msg2.setPayload(taskList);
			
			ss.sendDataPlz(msg2);
		});
		
			
		
	}
	
}
