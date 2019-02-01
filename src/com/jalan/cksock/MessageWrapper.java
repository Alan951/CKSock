package com.jalan.cksock;

import java.io.Serializable;
import java.util.UUID;

public class MessageWrapper implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private UUID uuid;
	private UUID responseOf;
	private Object payload;
	private transient SockService source;
	private transient SockService destination;
	
	public MessageWrapper() {
		this.uuid = UUID.randomUUID();
	}
	
	public MessageWrapper(Object payload, SockService source, SockService destination) {
		this();
		this.payload = payload;
		this.source = source;
		this.destination = destination;
	}
	
	public MessageWrapper(Object payload, SockService source) {
		this();
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
		return "MessageWrapper [payload=" + payload + ", source=" + source + ", destination=" + destination + "]";
	}
}
