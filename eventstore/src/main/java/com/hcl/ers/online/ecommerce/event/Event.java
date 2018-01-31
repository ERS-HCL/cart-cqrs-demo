package com.hcl.ers.online.ecommerce.event;

import java.util.Map;

public interface Event {
	
	String getEventId();
	void setEventId(String eventId);
	
	long getEventNumber();
	void setEventNumber(long eventNumber);
		
	Map<String, String> getTags();
	void setTags(Map<String, String> tags);
	
	long getTimestamp();
	void setTimestamp(long timestamp);
}
