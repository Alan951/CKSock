package com.jalan.cksock;

public class SockConfig {
	public int connMode;
	public String address;
	public int port;
	public boolean attemptConnect;
	public int attemptTimes;
	public boolean autoConnect;
	public boolean useJson;
	
	public static final int SERVER_MODE = 0;
	public static final int CLIENT_MODE = 1;
	
	public SockConfig() {}
	
	public SockConfig(int port) {
		this.connMode = SERVER_MODE;
		this.address = "localhost";
		this.port = port;
	}
	
	public SockConfig(String address, int port) {
		this.address = address;
		this.port = port;
		this.attemptTimes = 1;
		this.connMode = CLIENT_MODE;
	}
	
	public SockConfig(String address, int port, boolean attemptConnect, int attempt_times, boolean autoConnect) {
		this.address = address;
		this.port = port;
		this.attemptConnect = attemptConnect;
		this.attemptTimes = attempt_times;
		this.autoConnect = autoConnect;
		this.connMode = CLIENT_MODE;
	}
	
	public boolean isUseJson() {
		return useJson;
	}

	public void setUseJson(boolean useJson) {
		this.useJson = useJson;
	}

	public String getAddress() {
		return address;
	}
	
	public SockConfig setAddress(String address) {
		this.address = address;
		
		return this;
	}
	
	public int getPort() {
		return port;
	}
	
	public SockConfig setPort(int port) {
		this.port = port;
		
		return this;
	}
	
	public boolean isAttemptConnect() {
		return attemptConnect;
	}
	
	public SockConfig setAttemptConnect(boolean attemptConnect) {
		this.attemptConnect = attemptConnect;
		
		return this;
	}
	
	public int getAttemptTimes() {
		return attemptTimes;
	}
	
	public SockConfig setAttemptTimes(int attemptTimes) {
		this.attemptTimes = attemptTimes;
		
		return this;
	}
	
	public boolean isAutoConnect() {
		return autoConnect;
	}

	public void setAutoConnect(boolean autoConnect) {
		this.autoConnect = autoConnect;
	}

	public int getConnMode() {
		return connMode;
	}

	public void setConnMode(int connMode) {
		this.connMode = connMode;
	}

	@Override
	public String toString() {
		return "Config [address=" + address + ", port=" + port + ", attemptConnect=" + attemptConnect
				+ ", attempt_times=" + attemptTimes + "]";
	}
	
}
