package com.jalan.cksock;

import java.io.IOException;

public interface IIOSocket {
	
	public boolean isUp();
	
	public boolean sendData(Object obj) throws IOException;
	
	public boolean start() throws IOException;
	
	public boolean stop() throws IOException;
	
}
