package com.jalan.tests;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import com.google.gson.Gson;
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
		
		Gson gson = new Gson();
		
		SockServerService sss = new SockServerService(serverConfig);
		sss.listen();
		
		
		sss.getClientMessagesObserver().subscribe((message) -> {
			System.out.println(message);
			
			System.out.println(gson.fromJson(message.getPayload().toString(), Persona.class));
		});
		
	
		SockConfig clientConfig = new SockConfig("127.0.0.1", 951);
		clientConfig.setUseJson(true);
		
		SockService ss = new SockService(clientConfig);
		ss.getConnectionObserver().filter(conn -> conn.status == SockService.CONNECTED_STATUS).subscribe((conn) -> {
			Persona persona = new Persona();
			persona.setEdad(24);
			persona.setNombre("Jorge Alan");
			
			
			
			String personaJson = gson.toJson(persona);
			
			conn.service.sendData(personaJson);
		
		});
		
			
		
	}
	
}
