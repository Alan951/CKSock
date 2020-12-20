package com.jalan.cksock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class IOPlainSocket {
	private SockService service;
	
	private BufferedReader in;
	private PrintWriter out;
	private Thread inThread;
	private boolean flagIn;
	
	private static Logger logger = Logger.getLogger(IOPlainSocket.class);
	
	public IOPlainSocket() {}
	
	public IOPlainSocket(SockService service) {
		this.service = service;
	}
	
	public boolean itsUp() {
		return false;
	}
	
	public void start() throws IOException{
		this.initStreams();
	}
	
	private void initStreams() throws IOException {
		this.initOut();
		this.initIn();
	}
	
	private void initOut() throws IOException {
		this.out = new PrintWriter(this.service.getSocket().getOutputStream(), true);
	}
	
	private void initIn() throws IOException {
		this.in = new BufferedReader(new InputStreamReader(this.service.getSocket().getInputStream()));
		
		this.inThread = new Thread(() -> {
			this.flagIn = true;
			
			logger.info("server-input thread started");
			
			while(this.flagIn) {
				try {
					String readLine = this.in.readLine();
					if(readLine == null) {
						throw new IOException();
					}
					
					logger.debug("data recievied: " + readLine);
					this.service.inComingData(new String(Base64.getDecoder().decode(readLine), StandardCharsets.UTF_8));
				}catch(IOException e) {
					this.flagIn = false;
					
					try {
						this.service.close();
					}catch(IOException e2) {
						e2.printStackTrace();
					}
				}
			}
			
		});
		
		this.inThread.start();
	}
	
	public void sendData(String data) throws IOException {
		data = new String(Base64.getEncoder().encode(data.getBytes(StandardCharsets.UTF_8)));
		logger.debug("sendData invoked: " + data);
		
		this.out.println(data);
	}
	
	public boolean stop() throws IOException {
		logger.debug("socket stopped invoked");
		
		this.flagIn = false;		
		this.closeIn();
		this.closeOut();
		this.inThread.interrupt();
		
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
