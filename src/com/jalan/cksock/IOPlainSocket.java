package com.jalan.cksock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.log4j.Logger;

public class IOPlainSocket {
	
	private SockService service;
	
	private PrintWriter out;
	private BufferedReader in;
	
	private Thread inThread;
	
	private boolean flagIn;
	
	private static Logger logger = Logger.getLogger(IOPlainSocket.class);
	
	public IOPlainSocket(SockService sockService) {
		this.service = sockService;
	}
	
	public boolean start() throws IOException{
		this.init();
		
		return true;
	}
	
	private void init() throws IOException{
		initOUT();
		initIN();
	}
	
	public boolean isUp() {
		return true;
	}
	
	private void initOUT() throws IOException {
		this.out = new PrintWriter(this.service.getSocket().getOutputStream(), true);
	}
	
	private void initIN() throws IOException {
		this.in = new BufferedReader(new InputStreamReader(this.service.getSocket().getInputStream()));
		
		this.inThread = new Thread(() -> {
			this.flagIn = true;
			
			logger.debug("Input thread started!");
			
			String inMessage = null;
			String data = null;
			
			logger.debug("waiting for messages");
			
			while(this.flagIn) {
				try {
					
					data = this.in.readLine();
					
					this.logger.debug("recivied data invoked: " + data);
					
					inMessage = new String(Base64.getDecoder().decode(data), StandardCharsets.UTF_8);
					
					
					service.inComingData(inMessage);
					
				}catch(IOException e) {
					this.flagIn = false;
					
					try {
						this.stop();
					}catch(IOException t) {
						t.printStackTrace();
					}
				}
			}
		}); 
		
		this.inThread.start();
	}
	
	public boolean sendData(String str) {
		String data = new String(Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8)));
		
		logger.debug("sendData invoked, payload: " + data);

		this.out.println(data);
		
		return true;
	}
	
	public boolean stop() throws IOException{
		logger.debug("socket stopped invoked");
		
		this.flagIn = false;
		
		this.inThread.interrupt();
		
		this.closeIn();
		this.closeOut();
		
		return true;
	}
	
	private void closeOut() throws IOException {
		this.out.flush();
		this.out.close();
	}
	
	private void closeIn() throws IOException {
		this.in.close();
	}
	
}
