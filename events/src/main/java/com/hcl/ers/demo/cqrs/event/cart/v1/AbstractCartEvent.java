package com.hcl.ers.demo.cqrs.event.cart.v1;

import java.util.UUID;

import com.hcl.ers.demo.cqrs.event.Event;

public abstract class AbstractCartEvent implements Event {
	
	private String docType = CartEventDocTypes.CartEvent.name();
	private long timestamp = System.currentTimeMillis();
	private String eventId = UUID.randomUUID().toString();
	private long eventNumber;
	
	private String cartId;	
	
	@Override
	public String getDocType() {
		return docType;
	}
	
	@Override
	public void setDocType(String docType) {
		this.docType = docType;
	}
	
	
	@Override
	public String getCartId() {
		return cartId;
	}
	
	@Override
	public void setCartId(String cartId) {
		this.cartId = cartId;
	}
	
	@Override
	public String getEventId() {
		return eventId; 
	}
	
	@Override
	public void setEventId(String eventId) {
		this.eventId = eventId; 
	}
	
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getEventNumber() {
		return eventNumber;
	}

	public void setSequenceNumber(long eventNumber) {
		this.eventNumber = eventNumber;
	}
}
