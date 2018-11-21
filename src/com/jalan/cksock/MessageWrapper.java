package com.jalan.cksock;

import java.io.Serializable;

public class MessageWrapper implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Object payload;
	private transient SockService source;
	private transient SockService destination;
	
	public MessageWrapper(Object payload, SockService source, SockService destination) {
		this.payload = payload;
		this.source = source;
		this.destination = destination;
	}
	
	public MessageWrapper(Object payload, SockService source) {
		this.payload = payload;
		this.source = source;
	}

	public Object getPayload() {
		return payload;
	}
	
	public void setPayload(Object payload) {
		this.payload = payload;
	}
	
	public SockService getSource() {
		return source;
	}
	
	public void setSource(SockService source) {
		this.source = source;
	}
	
	public SockService getDestination() {
		return destination;
	}
	
	public void setDestination(SockService destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return "MessageWrapper [payload=" + payload + ", source=" + source + ", destination=" + destination + "]";
	}
}
