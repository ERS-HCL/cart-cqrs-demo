package com.onefoundation.cqrsdemo.cart.event;

public interface Event {
	
	long getSequenceNumber();
	void setSequenceNumber(long sequenceNumber);
	
	String getCartId();
	void setCartId(String cartId);
	
	String getEventName();
	void setEventName(String eventName);
	
	String getEventId();
	void setEventId(String eventId);
	
	
	long getTimestamp();
	void setTimestamp(long timestamp);
	
	
	String getDocType();
	void setDocType(String docType);

}
