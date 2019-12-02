package com.jalan.cksock;

public interface IIOSocket {
	
	public boolean start();
	
	public boolean stop();
	
	public boolean isUp();
	
	public boolean sendData(Object obj);
	
	public boolean sendData(String str);
	
}
