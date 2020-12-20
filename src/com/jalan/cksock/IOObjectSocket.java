package com.jalan.cksock;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class IOObjectSocket {
	private SockService service;
	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	private Thread oisThread;
	
	private boolean flagOis;
	
	private static Logger logger = Logger.getLogger(IOObjectSocket.class);
	
	public IOObjectSocket() {}
	
	public IOObjectSocket(SockService service) {
		this.service = service;
	}
	
	public boolean itsUp() {
		return false;
	}
	
	public void start() throws IOException{
		this.initStreams();
	}
	
	private void initStreams() throws IOException {
		this.initOOS();
		this.initOIS();
	}
	
	private void initOOS() throws IOException {
		this.oos = new ObjectOutputStream(this.service.getSocket().getOutputStream());
	}
	
	private void initOIS() throws IOException {
		this.ois = new ObjectInputStream(this.service.getSocket().getInputStream());
		
		this.oisThread = new Thread(() -> {
			this.flagOis = true;
			
			logger.info("OIS thread started");
			
			Object inMessage = "";
			
			while(this.flagOis) { //While thread is up
				try {
					logger.info("waiting for messages");
					
					while(true) {
						Object readObject = this.ois.readObject();
						if(readObject == null)
							break;
						logger.debug("inMessage IOSocket: " + readObject);
						if(this.service.getConf() != null && (readObject instanceof String)) {
							readObject = new Gson().fromJson((String) readObject, MessageWrapper.class);
						}
						
						this.service.inComingData(readObject);
					}
				}catch(ClassNotFoundException e) {
					logger.error("Error al parsear el mensaje de entrada", e);
				}catch(IOException e2) {
					this.flagOis = false;
					try {
						stop();
					}catch(IOException e3) {
						e3.printStackTrace();
					}
				}
			}
		});
		
		this.oisThread.start();
	}
	
	public void sendData(Object data) throws IOException {
		logger.debug("sendData invoked: " + data);
		
		this.oos.writeObject(data);
	}
	
	public void sendData(MessageWrapper data) throws IOException {
		logger.debug("sendData invoked: " + data);
		
		this.oos.writeObject(data);
	}
	
	public void stop() throws IOException {
		logger.debug("socket stopped invoked");
		
		this.flagOis = false;
		this.oisThread.interrupt();
		
		this.closeOIS();
		this.closeOOS();
	}
	
	private void closeOOS() throws IOException {
		this.oos.flush();
		this.oos.close();
	}
	
	private void closeOIS() throws IOException {
		this.ois.close();
	}
}
