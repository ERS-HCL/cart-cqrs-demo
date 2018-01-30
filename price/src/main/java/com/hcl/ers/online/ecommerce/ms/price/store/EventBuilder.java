package com.hcl.ers.online.ecommerce.ms.price.store;

import com.hcl.ers.online.ecommerce.event.Event;
import com.hcl.ers.online.ecommerce.event.cart.v1.ItemAddedEvent;
import com.hcl.ers.online.ecommerce.event.cart.v1.ItemUpdatedEvent;
import com.hcl.ers.online.ecommerce.eventstore.JsonMapper;

public class EventBuilder {
	private static JsonMapper mapper = new JsonMapper();

	public static Event build(String json) {
		Event event = null;
		try {
			if(json.contains("ItemUpdatedEvent")) {
				event =  mapper.toObject(json, ItemUpdatedEvent.class);
			} else if (json.contains("ItemAddedEvent")) {
				event =  mapper.toObject(json, ItemAddedEvent.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return event;
	}
}
