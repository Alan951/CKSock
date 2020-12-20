package com.jalan.tests;

import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import com.google.gson.Gson;
import com.jalan.cksock.MessageWrapper;
import com.jalan.cksock.SockConfig;
import com.jalan.cksock.SockLogger;
import com.jalan.cksock.SockServerService;
import com.jalan.cksock.SockService;

public class SizeTest {

	public static void main(String ...args) throws IOException, InterruptedException {
		LogManager.getRootLogger().setLevel(Level.INFO);
		SockLogger.autoConfigure();
		
		SockServerService sss = new SockServerService(new SockConfig(951));
		sss.listen();
		
		Gson gson = new Gson();
		
		sss.getClientMessagesObserver().subscribe((messageWrapper) -> {
			
			Message message = gson.fromJson((String) messageWrapper.getPayload(), Message.class);
			
			System.out.println(message);
			if(message.action.equals("testsize")) {
				int length = Integer.parseInt(message.arg);
				String arg = "";
				
				for(int x = 0 ; x < length ; x++) {
					char c = (char)getRandomInt(48, 122);
					arg += c;
				}
				
				System.out.println("message generated = text with " + arg.length() + " characters");
				
				message.arg = arg;
				
				messageWrapper.getSource().sendDataPlz(gson.toJson(message));
			}
		});
	}
	
	public static int getRandomInt(int min, int max) {
		return (int) Math.floor(Math.random() * (max - min + 1) + min);
	}
	
	public class Message {
		public String action;
		public String arg;
		
		
		@Override
		public String toString() {
			return "Message [action=" + action + ", arg=" + arg + "]";
		}
	}
	
}
