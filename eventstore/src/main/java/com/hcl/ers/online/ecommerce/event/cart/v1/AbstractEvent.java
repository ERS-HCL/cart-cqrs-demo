package com.hcl.ers.online.ecommerce.event.cart.v1;

import java.util.Map;
import java.util.UUID;

import com.hcl.ers.online.ecommerce.event.Event;

public abstract class AbstractEvent implements Event {
	
	private long timestamp = System.currentTimeMillis();
	private String eventId = UUID.randomUUID().toString();
	private long eventNumber;
	
	private Map<String, String> tags;		
	
	@Override
	public Map<String, String> getTags() {
		return tags;
	}
	
	@Override
	public void setTags(Map<String, String> tags) {
		this.tags = tags;
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

	public void setEventNumber(long eventNumber) {
		this.eventNumber = eventNumber;
	}
}
