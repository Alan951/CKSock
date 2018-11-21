package com.jalan.cksock;

public class ConnectionStatus{
	public String status;
	public SockService service;
	
	public ConnectionStatus(String status, SockService service) {
		this.status = status;
		this.service = service;
	}
}