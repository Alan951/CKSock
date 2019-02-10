package com.jalan.cksock;

import java.io.Serializable;
import java.util.UUID;

public class MessageWrapper2<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private UUID uuid;
	private UUID responseOf;
	private T payload;
	private transient SockService source;
	private transient SockService destination;
	
	public MessageWrapper2() {
		this.uuid = UUID.randomUUID();
	}
	
	public MessageWrapper2(T payload, SockService source, SockService destination) {
		this();
		this.payload = payload;
		this.source = source;
		this.destination = destination;
	}
	
	public MessageWrapper2(T payload, SockService source) {
		this();
		this.payload = payload;
		this.source = source;
	}

	public T getPayload() {
		return payload;
	}
	
	public void setPayload(T payload) {
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

	public UUID getUuid() {
		return uuid;
	}

	public UUID getResponseOf() {
		return responseOf;
	}

	public void setResponseOf(UUID responseOf) {
		this.responseOf = responseOf;
	}

	@Override
	public String toString() {
		return "MessageWrapper2 [payload=" + payload + ", source=" + source + ", destination=" + destination + "]";
	}
}
