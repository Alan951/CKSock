package com.jalan.cksock; 

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import rx.subjects.PublishSubject;

public class SockService {
	public static final String CONNECTED_STATUS = "CONNECTED";
	public static final String DISCONNECTED_STATUS = "DISCONNECTED";
	public static final String ATTEMPT_CONNECT_STATUS = "ATTEMPT_CONNECT";
	
	private Logger logger = Logger.getLogger(SockService.class);
	
	private Socket socket;
	private IOObjectSocket ioSocket;
	private SockConfig conf;
	
	private long id;
	
	private PublishSubject<ConnectionStatus> observerConnection = PublishSubject.create();
	private PublishSubject<MessageWrapper> observerMessages;
	
	public SockService() {}
	
	public SockService(SockConfig sockConfig) throws IOException {
		this.conf = sockConfig;
		this.connect();
	}
	
	public void connect() throws IOException {		
		this.connect(this.conf);
	}
	
	public void connect(SockConfig conf) throws IOException{
		logger.info("onConnectSockService invoked!");
		this.setConfig(conf);
		
		new Thread(() -> {
			try {
				int attempts = 0;
				
				while((attempts <= conf.attemptTimes - 1) || conf.attemptTimes == -1) {
					logger.info("Attempting connect to " +conf);
					this.observerConnection.onNext(new ConnectionStatus("ATTEMPT_CONNECT_STATUS", this));
					
					if(this.connectSocket()) {
						break;
					}else {
						//Incremento el intento si son finitos
						if(conf.attemptTimes != -1)
							attempts++;
						
						if(conf.attemptTimes == -1 || attempts <= conf.attemptTimes - 1) {	
							Thread.sleep(1000 * 3); //Tiempo de espera para el siguiente intento
								
						}else {
							logger.warn("Can't connect to server");
							
							break;
						}
					}
				}
			}catch(InterruptedException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	private boolean connectSocket() throws IOException {
		try {
			this.socket = new Socket(this.conf.getAddress(), this.conf.getPort());
		}catch(IOException e) {
			//e.printStackTrace();
			logger.error("Error when attempt connect socket", e);
			return false;
		}
		
		this.onConnected();
		
		return true;
	}
	
	private void onConnected() throws IOException {
		this.observerMessages = PublishSubject.create();
		
		this.observerMessages.subscribe((Object newMessage) -> {
			this.logger.debug("NewMessage: " + newMessage);
		});
		
		this.ioSocket = new IOObjectSocket(this);
		this.ioSocket.start();
		
		logger.info((this.conf != null ? this.conf.getConnMode() : "00") + " onConnected invoked");
		
		this.observerConnection.onNext(new ConnectionStatus(SockService.CONNECTED_STATUS, this));
	}
	
	private void onDisconnected() throws IOException {
		logger.info((this.conf != null ? this.conf.getConnMode() : "00") + " onDisconnected invoked");
		
		this.observerConnection.onNext(new ConnectionStatus(SockService.DISCONNECTED_STATUS, this));
		
		if(this.conf != null && this.conf.connMode == SockConfig.CLIENT_MODE) {
			if(this.conf.isAutoConnect()) {
				this.connect();
			}
		}
		
	}
	
	public boolean close() throws IOException{
		this.observerMessages.onCompleted();
		
		logger.info((this.conf != null ? this.conf.getConnMode() : "00") + " onClose invoked");
		
		try {
		
			this.ioSocket.stop();
			this.socket.close();
			
			this.onDisconnected();
		}catch(IOException e) {
			//e.printStackTrace();
			
			return false;
		}
		
		return true;
	}
	
	public SockService setConfig(SockConfig conf) {
		this.conf = conf;
		
		return this;
	}
	
	public void setSocket(Socket socket) throws IOException {
		if(!socket.isClosed()) {
			this.socket = socket;
			
			this.onConnected();
		}
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public PublishSubject<MessageWrapper> getMessageObserver(){
		return this.observerMessages;
	}
	
	public void inComingData(Object message) {
		if(message instanceof MessageWrapper) {
			MessageWrapper messageWrap = (MessageWrapper)message;
			
			messageWrap.setSource(this);
			
			this.observerMessages.onNext(messageWrap);
		}else {
			this.observerMessages.onNext(new MessageWrapper(message, this));
		}
	}
	
	public void sendData(Object data) throws IOException {
		this.ioSocket.sendData(data);
	}
	
	public void sendDataPlz(Object data) {
		try {
			this.ioSocket.sendData(data);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public PublishSubject<ConnectionStatus> getConnectionObserver(){
		return this.observerConnection;
	}
	
	public Logger getLogger() {
		return this.logger;
	}

	public SockConfig getConf() {
		return conf;
	}

	@Override
	public String toString() {
		return "SockService [id=" + id + ", remoteIp=" + this.socket.getRemoteSocketAddress() + "]";
	}
	
	
}
