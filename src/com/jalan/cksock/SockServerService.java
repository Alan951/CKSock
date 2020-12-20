package com.jalan.cksock;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import rx.subjects.PublishSubject;

public class SockServerService {
	
	private ServerSocket serverSock;
	private SockConfig sockConfig;
	
	private List<SockService> clientSocks;
	
	private PublishSubject<MessageWrapper> observerClientMessages;
	private PublishSubject<ConnectionStatus> observerClientConnection = PublishSubject.create();
	
	private boolean flagInComConn;
	
	private Logger logger = Logger.getLogger(SockServerService.class);
	
	public SockServerService(SockConfig sockConfig) {
		clientSocks = new ArrayList<SockService>();
		this.sockConfig = sockConfig;
		
	}
	
	public List<SockService> getClients() {
		return this.clientSocks;
	}
	
	public boolean listen() throws BindException{
		try {
			this.flagInComConn = true;
			serverSock = new ServerSocket(this.sockConfig.getPort());
			logger.info("[*] Listening over "+this.sockConfig.getAddress()+":"+this.sockConfig.getPort());
			
			this.observerClientMessages = PublishSubject.create();
			
			new Thread(() -> {
				long idAI = 0;
				
				while(this.flagInComConn) {
					idAI++;
					
					try {
						Socket socket = serverSock.accept();
						
						if(!flagInComConn)
							continue;
						
						SockService sockService = new SockService();
						
						
						SockConfig conf = new SockConfig();
						conf.setConnMode(-1);
						sockService.setConfig(conf);
						
						
						sockService.setSocket(socket);
						sockService.setId(idAI);
						
						sockService.getMessageObserver().subscribe((msg) -> {
							this.logger.debug("new message from " + sockService.toString() + ": " + msg);
							this.observerClientMessages.onNext(msg);
						});
						
						sockService.getConnectionObserver()
							.filter((evt) -> evt.status.equals(SockService.DISCONNECTED_STATUS))
							.subscribe((evt) -> {
								
							this.observerClientConnection.onNext(new ConnectionStatus(SockService.DISCONNECTED_STATUS, evt.service));
							
							logger.info((evt.service.getConf() != null ? evt.service.getConf().getConnMode() : "00") + " SockClient disconected: " + evt.service);
							
							sockService.getConnectionObserver().onCompleted();
							
						});
					
						this.clientSocks.add(sockService);						
						this.observerClientConnection.onNext(new ConnectionStatus(SockService.CONNECTED_STATUS, sockService));
						
						logger.info("New connection: " + sockService);
					}catch(SocketException e) {
						if(!e.getMessage().equals("socket closed")) {
							e.printStackTrace();
						}
						
						this.logger.debug("Socket accept interrupted", e);
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}catch(BindException e) {
			throw e;
		}catch(IOException e) {
			logger.debug("Server socket closed");
			
			if(this.flagInComConn)
				logger.error(e);
			
			return false;
		}
		
		return true;
	}

	public void closeAll() {
		this.logger.info("stopping " + this.clientSocks.size() + " services");
		
		this.clientSocks.forEach((client) -> {
			try {
				client.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		});		
		
		this.clientSocks.clear();
	}
	
	public void close(long id) {
		Optional<SockService> sockClient = this.clientSocks.stream().filter(client -> client.getId() == id).findFirst();
		if(sockClient.isPresent()) {
			try {
				sockClient.get().close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		try {
			this.logger.info("ServerSocket closed");
			flagInComConn = false;
			this.serverSock.close();
			
			this.closeAll();
		}catch(IOException e) {
			logger.info("Server Socket closed");
		}
	}
	
	public boolean sendAll(Object data) {
		for(SockService client : this.clientSocks) {
			sendData(data, client.getId());
		}
		
		return true;
	}
	
	public boolean sendData(Object data, long id) {
		Optional<SockService> sockFiltered = this.clientSocks.stream()
				.filter((client) -> client.getId() == id)
				.findFirst();
		//TODO: Validar que el socket se encuentre up
		if(sockFiltered.isPresent()) {
			sockFiltered.get().sendDataPlz(data);
			return true;
		}else {
			return false;
		}
	}
	
	public Logger getLogger() {
		return this.logger;
	}
	
	public PublishSubject<MessageWrapper> getClientMessagesObserver(){
		return this.observerClientMessages;
	}
	
	public PublishSubject<ConnectionStatus> getClientConnectionObserver(){
		return this.observerClientConnection;
	}
}
